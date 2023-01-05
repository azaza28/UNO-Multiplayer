package com.example.unogui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;


public class Controller {
    @FXML
    TextField txt1;
    @FXML
    Button submit;
    @FXML
    Button join;
    @FXML
    Button host;
    private Parent root;
    private Stage stage;
    private Scene scene;

    public void SwitchToHost(ActionEvent event) throws IOException {

        String username = txt1.getText();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("HostScene.fxml"));
        root = loader.load();

        HostController hostController = loader.getController();
        hostController.DisplayName(username);

        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(this.getClass().getResource("HostScene.css").toExternalForm());
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
        stage.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                hostController.StartServer();
            }
        }).start();

    }

    public void SwitchToJoin(ActionEvent event) throws IOException {
        String username = txt1.getText();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("JoinScene.fxml"));
        root = loader.load();

        JoinController joinController = loader.getController();
        joinController.DisplayName(username);

        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(this.getClass().getResource("JoinScene.css").toExternalForm());

        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
        stage.show();
    }

    public void handleButton1Action(ActionEvent event) {
        if(!txt1.getText().isEmpty()) {
            if(txt1.getText().isBlank()){
                txt1.setText("");
            }
            else {
                System.out.println(txt1.getText());
                txt1.setBackground(Background.fill(Color.GRAY));
                txt1.setDisable(true);
                submit.setDisable(true);
                submit.setBackground(Background.fill(Color.GRAY));
                join.setDisable(false);
                host.setDisable(false);
            }
        }
    }
}
