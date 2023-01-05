package com.example.unogui.UNO;


import com.example.unogui.UnoGUI;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;


public class Game implements Serializable {
    private int CurrentPlayer;//Active player, whoose turn it is
    private ArrayList<String> PlayerIDs;//String of usernames of the players.

    public Deck deck;
    private ArrayList<ArrayList<Card>> PlayerHand;//All cards of the players
    /*
    Array list of Array list of cards. each player's hand
     */
    public ArrayList<Card> StockPile;

    private Card.Color ValidColor;
    private Card.Value ValidValue;

    boolean GameDirection;
    boolean Uno;



    public Game(ArrayList<String> PlayerIDs){
        deck=new Deck();
        deck.shuffle();
        StockPile = new ArrayList<Card>();

        this.PlayerIDs=PlayerIDs;
        CurrentPlayer = 0;
        GameDirection = false;

        PlayerHand= new ArrayList<ArrayList<Card>>();

        for (int i=  0; i < PlayerIDs.size();i++){
            ArrayList<Card> hand = new ArrayList<Card>(Arrays.asList(deck.DrawCard(7)));//Generates 7 cards for each player
            PlayerHand.add(hand);//Stores all cards of each player in this array
        }
    }
    public Game(ArrayList<String> PlayerIDs,int CurrentPlayer, Deck deck, ArrayList<ArrayList<Card>> PlayerHand,
                ArrayList<Card>StockPile, Card.Color ValidColor, Card.Value ValidValue, boolean GameDirection ){
        this.PlayerIDs=PlayerIDs;
        this.CurrentPlayer=CurrentPlayer;
        this.deck=deck;
        this.PlayerHand=PlayerHand;
        this.StockPile=StockPile;
        this.ValidColor=ValidColor;
        this.ValidValue=ValidValue;
        this.GameDirection=GameDirection;
    }

    public Card.Value getValidValue() {
        return ValidValue;
    }

    public ArrayList<ArrayList<Card>> getPlayerHand() {
        return PlayerHand;
    }

    public ArrayList<Card> getStockPile() {
        return StockPile;
    }

    public ArrayList<String> getPlayerIDs() {
        return PlayerIDs;
    }

    public boolean isGameDirection() {
        return GameDirection;
    }

    public Card.Color getValidColor() {
        return ValidColor;
    }

    public Deck getDeck() {
        return deck;
    }

    public int getCurrentPlayer() {
        return CurrentPlayer;
    }

    public void Start(){
        Card card = deck.DrawCard();
        ValidColor = card.getColor();
        ValidValue = card.getValue();

        if(card.getValue()==Card.Value.Wild || card.getValue()==Card.Value.Wild_Four || card.getValue()==Card.Value.DrawTwo){
            Start();
        }

        if(card.getValue()==Card.Value.Skip){
            System.out.println(PlayerIDs.get(CurrentPlayer)+ " was skipped.");


            if(GameDirection==false){//Default direction of the game
                CurrentPlayer = (CurrentPlayer+1) % PlayerIDs.size();
            }
            else if(GameDirection==true){//Reverse direction of the game
                CurrentPlayer = (CurrentPlayer-1) % PlayerIDs.size();
                if(CurrentPlayer==-1){
                    CurrentPlayer=PlayerIDs.size()-1;
                }
            }
        }

        if(card.getValue()==Card.Value.Reverse){
            System.out.println(PlayerIDs.get(CurrentPlayer)+ " has changed the game direction.");
            GameDirection=!GameDirection;//Changes the value of GameDirection to the opposite one
            CurrentPlayer = PlayerIDs.size()-1;
        }
        StockPile.add(card);
    }


    public Card GetTopCard(){
        return new Card(ValidColor,ValidValue);
    }



    public boolean IsGameOver(){
        for(String player: PlayerIDs){
            if(HasEmptyHand(player)){
                return true;
            }
        }
        return false;
    }

    public String GetCurrentPlayer(){
        return this.PlayerIDs.get(this.CurrentPlayer);
    }

    public void setPlayerHand(ArrayList<ArrayList<Card>> cards){
        PlayerHand=cards;
    }

    public String GetPreviousPlayer(){
        if(CurrentPlayer!=0)
            return PlayerIDs.get(CurrentPlayer-1);
        else{
            return PlayerIDs.get(PlayerIDs.size()-1);
        }
    }


    public ArrayList<String> GetPlayers(){
        return PlayerIDs;
    }

    public ArrayList<Card> GetPlayerHand(String pid){
        int index = PlayerIDs.indexOf(pid);//goes through PLayer IDs and return the hand of a certain player
        return PlayerHand.get(index);
    }

    public int GetPlayerHandSize(String pid){
        return GetPlayerHand(pid).size();
    }

    public ImageView GetGraphic(Card card){
        Image img = new Image("file:target/classes/com/example/unogui/"+card.getColor()+"_"+card.getValue()+".png");
        ImageView view = new ImageView(img);

        return (new ImageView(img));
    }

    public Card GetPlayerCard(String pid, int choice){
        ArrayList<Card> hand = GetPlayerHand(pid);
        return hand.get(choice);
    }


    public boolean HasEmptyHand(String pid){
        return GetPlayerHand(pid).isEmpty();
    }


    public boolean ValidCardPlay(Card card){
        return card.getColor()==ValidColor || card.getValue()==ValidValue;
    }


    public void CheckPlayerTurn(String pid) throws InvalidPlayerTurnException{
        if(!(GetCurrentPlayer().equals(pid))){
            System.out.println("Current: " + GetCurrentPlayer());
            System.out.println("Player ID: "+pid);
            throw new InvalidPlayerTurnException("It is not " + pid + "'s turn",pid);
        }
    }

    public void SubmitDraw(String pid) throws InvalidPlayerTurnException, IOException {
        CheckPlayerTurn(pid);
        if(deck.IsEmpty()){
            deck.ReplaceDeckWith(StockPile);
            deck.shuffle();
        }
        GetPlayerHand(pid).add(deck.DrawCard());
        if (GameDirection == false){
            CurrentPlayer = (CurrentPlayer + 1)%PlayerIDs.size();
        }

        else if(GameDirection == true){
            CurrentPlayer = (CurrentPlayer-1)%PlayerIDs.size();
            if (CurrentPlayer==-1){
                CurrentPlayer = PlayerIDs.size()-1;
            }
        }
        UnoGUI.SendGame(this);
    }

    public void UnoPressed(){
        Uno=true;
    }

    public void SetCardColor(Card.Color color){
        ValidColor = color;
    }


    public void SubmitPlayerCard(String pid, Card card, Card.Color DeclaredColor) throws InvalidColorSubmissionException, InvalidValueSubmissionException, InvalidPlayerTurnException, IOException, NoUnoException {
        CheckPlayerTurn(pid);

        ArrayList<Card>phand = GetPlayerHand(pid);

        if(!ValidCardPlay(card)){
            if(card.getColor()==Card.Color.Wild){
                ValidColor=card.getColor();
                ValidValue=card.getValue();
            }

            if(card.getColor()!= ValidColor && card.getColor()!=Card.Color.Wild){
                System.out.println("Invalid Player move, expected color: "+ValidColor+" but got color "+ card.getColor());
                throw new InvalidColorSubmissionException("Invalid Player move, expected color: "+ValidColor+" but got color "+ card.getColor(),card.getColor(),ValidColor);
            }

            else if(card.getValue()!=ValidValue && card.getValue()!=Card.Value.Wild_Four && card.getValue()!=Card.Value.Wild){
                System.out.println("Invalid Player move, expected value: "+ValidValue+" but got value "+ card.getValue());

                throw new InvalidValueSubmissionException("Invalid Player move, expected value: "+ValidValue+" but got value "+ card.getValue(),card.getValue(),ValidValue);
            }
        }
        phand.remove(card);
        if(GetPlayerHandSize(this.PlayerIDs.get(CurrentPlayer))==1 && Uno==false){
            String message = "Uno button was not pressed prior to pre-last card submission. Penalty-1 extra card.";

            if(deck.IsEmpty()){
                deck.ReplaceDeckWith(StockPile);
                deck.shuffle();
            }

            GetPlayerHand(pid).add(deck.DrawCard());


            Notifications.create().text(message).darkStyle().position(Pos.BASELINE_RIGHT).hideAfter(Duration.seconds(5)).showWarning();

        }
        else{
            Uno=false;
        }

        if(HasEmptyHand(this.PlayerIDs.get(CurrentPlayer))){
            System.out.println(this.PlayerIDs.get(CurrentPlayer)+" has won! Thank you for playing!");
        }

        ValidColor = card.getColor();
        ValidValue = card.getValue();
        StockPile.add(card);

        if(GameDirection==false){
            CurrentPlayer = (CurrentPlayer+1)%PlayerIDs.size();
        }
        else if(GameDirection ==true){
            CurrentPlayer = (CurrentPlayer-1)%PlayerIDs.size();
            if(CurrentPlayer==-1){
                CurrentPlayer = PlayerIDs.size()-1;
            }
        }
        if(card.getColor()==Card.Color.Wild){
            ValidColor = DeclaredColor;
        }

        if(card.getValue() == Card.Value.DrawTwo){
            pid = PlayerIDs.get(CurrentPlayer);
            GetPlayerHand(pid).add(deck.DrawCard());
            GetPlayerHand(pid).add(deck.DrawCard());
            System.out.println(pid + " drew TWO cards!");
        }

        if(card.getValue() == Card.Value.Wild_Four){
            pid = PlayerIDs.get(CurrentPlayer);
            GetPlayerHand(pid).add(deck.DrawCard());
            GetPlayerHand(pid).add(deck.DrawCard());
            GetPlayerHand(pid).add(deck.DrawCard());
            GetPlayerHand(pid).add(deck.DrawCard());
            System.out.println(pid + " drew FOUR cards!");
        }

        if(card.getValue() == Card.Value.Skip){
            System.out.println(PlayerIDs.get(CurrentPlayer) + " was skipped!");

            if(GameDirection==false){
                CurrentPlayer = (CurrentPlayer+1)%PlayerIDs.size();
            } else if (GameDirection==true) {
                CurrentPlayer = (CurrentPlayer-1)%PlayerIDs.size();
                if(CurrentPlayer==-1){
                    CurrentPlayer=PlayerIDs.size()-1;
                }
            }
        }

        if(card.getValue() == Card.Value.Reverse){
            System.out.println(pid + " changed the GameDirection!");


            GameDirection=!GameDirection;

            if(GameDirection==true){
                CurrentPlayer = (CurrentPlayer-2)%PlayerIDs.size();
                if(CurrentPlayer==-1){
                    CurrentPlayer = PlayerIDs.size()-1;
                }
                if(CurrentPlayer==-2){
                    CurrentPlayer = PlayerIDs.size()-2;
                }
            }
            else if(GameDirection==false){
                CurrentPlayer = (CurrentPlayer+2)%PlayerIDs.size();
            }
        }
        System.out.println("Sending the game from Game class");
        Game g =new Game(this.GetPlayers(),this.getCurrentPlayer(),this.getDeck(),this.getPlayerHand(),this.getStockPile(),this.getValidColor(),this.getValidValue(),this.isGameDirection());
        if(card.getColor()==Card.Color.Wild){
            UnoGUI.LoadWild();
        }
        else
            UnoGUI.SendGame(g);
    }

    public void setValidColor(int num) {
        ValidColor=Card.Color.getColor(num);
    }
}


