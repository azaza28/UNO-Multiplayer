package com.example.unogui.UNO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Deck implements Serializable {
    private Card[] cards;
    private int CardsInDeck;

    public Deck() {//Constructor
        cards = new Card[108];
        Reset();
    }

    public void Reset() {//Method to reset the deck
        Card.Color[] colors = Card.Color.values();
        CardsInDeck = 0;

        for (int i = 0; i < colors.length - 1; i++) {//Loop for color setting
            Card.Color color = colors[i];
            cards[CardsInDeck++] = new Card(color, Card.Value.getValue(0));//Creates four zeros with different colors

            for (int j = 1; j < 10; j++) {//Loop for value setting for numbers 1-9
                cards[CardsInDeck++] = new Card(color, Card.Value.getValue(j));
                cards[CardsInDeck++] = new Card(color, Card.Value.getValue(j));//Creates two cards of the same value in four colors
            }

            Card.Value[] values = new Card.Value[]{Card.Value.DrawTwo, Card.Value.Skip, Card.Value.Reverse};//Creates special cards

            for (Card.Value value : values) {//for each loop to create special cards of different colors
                cards[CardsInDeck++] = new Card(color, value);
                cards[CardsInDeck++] = new Card(color, value);//Creates two cards of the same value in four colors
            }
        }

        Card.Value[] values = new Card.Value[]{Card.Value.Wild, Card.Value.Wild_Four};

        for (Card.Value value : values) {//for-each loop to set the Wild and wild_four cards
            for (int i = 0; i < 4; i++) {//for each loop to create special cards of different colors
                cards[CardsInDeck++] = new Card(Card.Color.Wild, value);
            }
        }
    }

    public void ReplaceDeckWith(ArrayList<Card> cards){//deck=stockpile
        this.cards=cards.toArray(new Card[cards.size()]);
        this.CardsInDeck=this.cards.length;
    }

    public boolean IsEmpty(){//Checks if the deck is empty
        return CardsInDeck == 0;
    }

    public void shuffle(){
        int n = cards.length;
        Random random = new Random();

        for(int i = 0; i< cards.length;i++){//Swaps two elements random and linearly met in loop
            int randVal= i + random.nextInt(n-i);
            Card randCard=cards[randVal];
            cards[randVal]=cards[i];
            cards[i]=randCard;
        }
    }

    public Card DrawCard() throws IllegalArgumentException{
        if(IsEmpty()){
            throw new IllegalArgumentException("Cannot draw a card, since there is no cards in the deck");
        }
        return cards[--CardsInDeck];
    }

    public Card[] DrawCard(int num){
        if(num<0){
            throw new IllegalArgumentException("Must draw a positive number of cards from deck.");
        }
        if(num>CardsInDeck){
            throw new IllegalArgumentException("Cannot draw "+num+" cards, as there is only "+CardsInDeck+" cards.");
        }

        Card[] take=new Card[num];
        for(int i = 0; i < num;i++){
            take[i]=cards[--CardsInDeck];
        }
        return take;
    }


}
/*The deck consists of 108 cards:
four each of "Wild" and "Wild Draw Four",
and 25 each of four colors (red, yellow, green, blue).
Each color consists of one zero, two each of 1 through 9,
and two each of "Skip", "Draw Two", and "Reverse".
These last three types are known as "action cards".*/