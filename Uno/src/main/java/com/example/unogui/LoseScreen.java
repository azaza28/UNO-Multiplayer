package com.example.unogui;

import com.example.unogui.UNO.Game;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

public class LoseScreen {

    @FXML
    VBox PlayerVbox;
    public void Load(Game game){
        Label label;
        for(String Player:game.GetPlayers()){
            if(game.GetPlayerHandSize(Player)==0){
                label=new Label("YOU-" + game.GetPlayerHandSize(Player)+ " cards");
            }
            else{
                label=new Label(Player+"-" +game.GetPlayerHandSize(Player)+ " cards");
            }
            label.setTextFill(Paint.valueOf("RED"));
            label.setFont(new Font("Verdana",15));
            PlayerVbox.getChildren().add(label);
        }
    }
}
