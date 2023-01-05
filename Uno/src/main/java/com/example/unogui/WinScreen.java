package com.example.unogui;

import com.example.unogui.UNO.Game;
import javafx.fxml.FXML;
import javafx.scene.control.Label;  //Label is a non-editable text control. A Label is useful for displaying text that is required to fit within a specific space, and thus may need to use an ellipsis or truncation to size the string to fit. Labels also are useful in that they can have mnemonics which, if used, will send focus to the Control listed as the target of the labelFor property.

import javafx.scene.layout.VBox;   //VBox lays out its children in a single vertical column. If the vbox has a border and/or padding set, then the contents will be layed out within those insets.
import javafx.scene.paint.Paint; //Base class for a color or gradients used to fill shapes and backgrounds when rendering the scene graph.
import javafx.scene.text.Font; //The Font class represents fonts, which are used to render text on screen.

//The size of a Font is described as being specified in points which are a real world measurement of approximately 1/72 inch.


/*The first file you edit is the FXMLExample.java file. This file includes the code for setting up the application main class and for defining the stage and scene. More specific to FXML, the file uses the FXMLLoader class, which is responsible for loading the FXML source file and returning the resulting object graph.*/
public class WinScreen {

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
