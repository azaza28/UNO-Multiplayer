package com.example.unogui.UNO;


import java.io.Serializable;

public class Card implements Serializable {

    public Card(final Color color, final Value value){
        this.color=color;
        this.value=value;
    }

    public enum Color{
        Red, Green, Yellow, Blue, Wild;
        private static final Color[] colors = Color.values();

        public static Color getColor(int i){
            return Color.colors[i];
        }
    }

    public enum Value{
       Zero,One,Two,Three,Four,Five,Six,Seven,Eight,Nine,DrawTwo,Skip,Reverse,Wild,Wild_Four;

        private static final Value[] values = Value.values();

        public static Value getValue(int i){
            return Value.values[i];
        }
    }

    private final Color color;
    private final Value value;

    public Color getColor(){
        return color;
    }

    public Value getValue(){
        return value;
    }

    public String toString(){
        return color+ "_"+value;
    }
}
