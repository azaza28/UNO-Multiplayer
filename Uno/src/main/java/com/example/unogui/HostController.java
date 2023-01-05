package com.example.unogui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class HostController{
    @FXML
    VBox vbox;
    static String name;
    @FXML
    Button start;
    @FXML
    Button cancel;
    @FXML
    Label ipLabel;

    private static Parent root;
    private static Stage stage;
    private static Scene scene;

    private Server server;

    public HostController(){
    }

    public void DisplayName(String username) throws UnknownHostException {
        String ip = InetAddress.getLocalHost().getHostAddress();
        ipLabel.setText(ip);
        this.name = username;

        Label label = new Label(username);
        Font font = Font.font("Verdana", FontWeight.BOLD, 36);
        label.setFont(font);
        label.setTextFill(Color.valueOf("#fa6868"));
        vbox.getChildren().add(label);
    }


    public void Cancel(ActionEvent e) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("UnoGUI.fxml"));
        root = loader.load();
        server.CloseEverything();

        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(this.getClass().getResource("UnoGUI.css").toExternalForm());

        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
        stage.show();
    }



    public void StartServer() {
        try {
            this.server = new Server();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    server.run(vbox);
                }
            }).start();
            Socket socket;
            socket = new Socket(InetAddress.getLocalHost().getHostAddress(),6969);
            Client client = new Client(socket,name);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    client.ListenToMessage(vbox);
                }
            }).start();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void Add(ArrayList<String> usernames, VBox vbox) {
        vbox.getChildren().clear();


        for(String username: usernames) {
            Label label = new Label(username);
            Font font = Font.font("Verdana", FontWeight.BOLD, 40);
            label.setFont(font);
            label.setTextFill(Color.valueOf("#fa6868"));
            vbox.getChildren().add(label);
        }
    }

    public void Start(ActionEvent e) throws IOException, InterruptedException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Player.StartGame();
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }).start();
    }

    public static String GetName() {
        return name;
    }

}
