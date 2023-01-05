package com.example.unogui.UNO;

import javafx.geometry.Pos;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

public class InvalidColorSubmissionException extends Exception {
    private Card.Color expected;
    private Card.Color actual;

    public InvalidColorSubmissionException(String message, Card.Color actual, Card.Color expected) {
        super(message);
        this.actual = actual;
        this.expected = expected;
        Notifications.create().text(message).darkStyle().position(Pos.BASELINE_RIGHT).hideAfter(Duration.seconds(5)).showWarning();

    }
}
