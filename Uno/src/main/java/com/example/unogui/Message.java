package com.example.unogui;


import java.io.Serializable;

// must implement Serializable in order to be sent
public class Message implements Serializable{
    private String text;

    public Message(String txt) {
        this.text = txt;
    }

    public String getText() {
        return text;
    }
}