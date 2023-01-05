package com.example.unogui.UNO;
import javafx.geometry.Pos;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

public class InvalidPlayerTurnException extends Exception {
    String PlayerID;

    public InvalidPlayerTurnException(String message, String PlayerID) {
        super(message);

        this.PlayerID = PlayerID;
        Notifications.create().text(message).darkStyle().position(Pos.BASELINE_RIGHT).hideAfter(Duration.seconds(5)).showWarning();
    }

}
