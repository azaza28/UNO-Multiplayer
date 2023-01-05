package com.example.unogui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class JoinController{
    @FXML
    Button cancel;
    @FXML
    Button submit;
    @FXML
    TextField ipTxt;
    @FXML
    VBox  vbox;




    private static Parent root;
    private static Stage stage;
    private static Scene scene;
    static String name;
    ArrayList<String> names;
    Client client;


    public JoinController() throws IOException {
    }

    public static String GetName() {
        return name;
    }

    public void DisplayName(String username) {
            this.name=username;

            Label label = new Label(username);
            Font font = Font.font("Verdana", FontWeight.BOLD,  40);
            label.setFont(font);
            label.setTextFill(Color.valueOf("#fa6868"));
            vbox.getChildren().add(label);
    }

    public void Cancel(ActionEvent e) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("UnoGUI.fxml"));
        root = loader.load();

        stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(this.getClass().getResource("UnoGUI.css").toExternalForm());

        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
        stage.show();
    }




    public void handleButton1Action(ActionEvent event) throws IOException, ClassNotFoundException {
        if(!ipTxt.getText().isEmpty()) {
            Socket socket;
            socket = new Socket(ipTxt.getText(), 6969);

            client = new Client(socket,name);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    client.ListenToMessage(vbox);
                }
            }).start();

            ipTxt.setBackground(Background.fill(Color.GRAY));
            ipTxt.setDisable(true);
            submit.setDisable(true);
            submit.setBackground(Background.fill(Color.GRAY));
        }
    }


    public static void Add(ArrayList<String> usernames, VBox vboxx) {
        vboxx.getChildren().clear();

        for(String username: usernames) {
            Font font = Font.font("Verdana", FontWeight.BOLD, 40);
            Label label = new Label(username);
            label.setFont(font);
            label.setTextFill(Color.valueOf("#fa6868"));
            System.out.println("Adding names in VBOX");

            vboxx.getChildren().add(label);
        }
    }

}
