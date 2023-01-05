package com.example.unogui;

import com.example.unogui.UNO.Card;
import com.example.unogui.UNO.Game;
import javafx.event.ActionEvent;   //The only valid EventType for the ActionEvent.
import javafx.fxml.FXML;
import javafx.scene.control.Button; //A simple button control. The button control can contain text and/or a graphic. A button control has three different modes
import javafx.stage.Stage;
//Construct a new ActionEvent with the specified event source and target.
import java.io.IOException; //Signals that an I/O exception of some sort has occurred. This class is the general class of exceptions produced by failed or interrupted I/O operations.

public class WildCard{
    Game game;
    static Card.Color color;

    @FXML
    public void Red(ActionEvent e) throws IOException {
        game = UnoGUI.GetGame();
        game.setValidColor(0);
        UnoGUI.SendGame(game);
        ((Stage)(((Button)e.getSource()).getScene().getWindow())).close();
    }

    @FXML
    public void Green(ActionEvent e) throws IOException {
        game = UnoGUI.GetGame();
        game.setValidColor(1);
        UnoGUI.SendGame(game);
        ((Stage)(((Button)e.getSource()).getScene().getWindow())).close();
        color = Card.Color.Green;
    }

    @FXML
    public void Blue(ActionEvent e) throws IOException {
        game = UnoGUI.GetGame();
        game.setValidColor(3);
        UnoGUI.SendGame(game);
        ((Stage)(((Button)e.getSource()).getScene().getWindow())).close();
        color = Card.Color.Blue;
    }

    @FXML
    public void Yellow(ActionEvent e) throws IOException {
        game = UnoGUI.GetGame();
        game.setValidColor(2);
        UnoGUI.SendGame(game);
        ((Stage)(((Button)e.getSource()).getScene().getWindow())).close();
        color = Card.Color.Yellow;
    }
}
