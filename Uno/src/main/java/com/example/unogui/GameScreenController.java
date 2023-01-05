package com.example.unogui;

import com.example.unogui.UNO.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.util.ArrayList;

public class GameScreenController{
    @FXML FlowPane flow;
    @FXML Button deck;
    @FXML Button top;
    @FXML Label Current;
    @FXML VBox PlayerVbox;
    @FXML Button DrawButton;
    @FXML Button UnoButton;
    String name;

    public GameScreenController() {
    }

    public void LoadGame(String name, Game game){
        Image img = new Image("file:target/classes/com/example/unogui/Card_Back_Alt.png");
        ImageView view = new ImageView(img);

        if(game.GetCurrentPlayer().equals(name)){
            Current.setText("Current Player: YOU");
        }
        else{
            Current.setText("Current Player: "+game.GetCurrentPlayer());
        }

        Label label;

        for(String Player:game.GetPlayers()){
            if(name.equals(Player)){
                label=new Label("YOU-" + game.GetPlayerHandSize(Player)+ " cards");
            }
            else{
                label=new Label(Player+"-" +game.GetPlayerHandSize(Player)+ " cards");
            }
            label.setTextFill(Paint.valueOf("RED"));
            label.setFont(new Font("Verdana",15));
            PlayerVbox.getChildren().add(label);
        }

        deck.setGraphic(view);
        deck.setBackground(Background.EMPTY);
        deck.setOnAction((ActionEvent event) -> {
            try {
                game.SubmitDraw(name);
            } catch (InvalidPlayerTurnException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        UnoButton.setOnAction((ActionEvent event) ->{
            if(game.GetPlayerHandSize(name)==2)
            game.UnoPressed();

            else{
                String message = "Uno button was pressed not before second-last card submission. Penalty-1 extra card.";

                if(game.deck.IsEmpty()){
                    game.deck.ReplaceDeckWith(game.StockPile);
                    game.deck.shuffle();
                }

                game.GetPlayerHand(name).add(game.deck.DrawCard());


                Notifications.create().text(message).darkStyle().position(Pos.BASELINE_RIGHT).hideAfter(Duration.seconds(5)).showWarning();
            }

        });

        DrawButton.setOnAction((ActionEvent event) -> {
            try {
                game.SubmitDraw(name);
            } catch (InvalidPlayerTurnException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });



        Deck deckk = new Deck();
        deckk.Reset();
        deckk.shuffle();

        Card TopCard = game.GetTopCard();


        top.setGraphic(game.GetGraphic(TopCard));
        top.setBackground(Background.EMPTY);

        ArrayList<Button> buttons = new ArrayList<>();

        int flag = 0;
        for(int i = 0;i<game.GetPlayerHandSize(name);i++){
            Card card = game.GetPlayerHand(name).get(i);
            buttons.add(new Button());

            view = game.GetGraphic(card);
            view.setFitHeight(136);
            view.setFitWidth(96);

            buttons.get(i).setPrefWidth(96);
            buttons.get(i).setPrefHeight(136);
            buttons.get(i).setId(String.valueOf(i));
            buttons.get(i).setBackground(Background.EMPTY);
            int finalI = i;
            buttons.get(i).setOnAction((ActionEvent event) -> {
                try {
                    game.SubmitPlayerCard(name, game.GetPlayerCard(name, finalI),game.GetPlayerCard(name, finalI).getColor());
                    flow.getChildren().remove(buttons.get(finalI));
                    buttons.remove(finalI);

                    //top.setGraphic(game.GetGraphic(game.GetTopCard()));
                } catch (InvalidColorSubmissionException e) {
                    throw new RuntimeException(e);
                } catch (InvalidValueSubmissionException e) {
                    throw new RuntimeException(e);
                } catch (InvalidPlayerTurnException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (NoUnoException e) {
                    throw new RuntimeException(e);
                }
            });
            if(card.getColor()== TopCard.getColor() || card.getValue()==TopCard.getValue() || card.getColor()==Card.Color.Wild){
                buttons.get(i).setStyle("-fx-border-color: #00ff40;\n-fx-border-width:2;");
                flag = 1;
            }
            buttons.get(i).setGraphic(view);
            flow.getChildren().add(buttons.get(i));
        }
        if (flag==0){
            deck.setStyle("-fx-border-color: #00ff40;\n-fx-border-width:2;");
        }
    }
}
