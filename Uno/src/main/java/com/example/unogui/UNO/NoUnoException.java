package com.example.unogui.UNO;

import javafx.geometry.Pos;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

public class NoUnoException extends Exception {

    public NoUnoException(String message) {
        super(message);
        Notifications.create().text(message).darkStyle().position(Pos.BASELINE_RIGHT).hideAfter(Duration.seconds(5)).showWarning();
    }
}

