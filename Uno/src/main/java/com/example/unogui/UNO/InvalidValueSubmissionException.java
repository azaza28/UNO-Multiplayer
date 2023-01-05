package com.example.unogui.UNO;

import javafx.geometry.Pos;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

public class InvalidValueSubmissionException extends Exception {
    private Card.Value expected;
    private Card.Value actual;

    public InvalidValueSubmissionException(String message, Card.Value actual, Card.Value expected) {
        super(message);
        this.actual = actual;
        this.expected = expected;
        Notifications.create().text(message).darkStyle().position(Pos.BASELINE_RIGHT).hideAfter(Duration.seconds(5)).showWarning();
    }
}

