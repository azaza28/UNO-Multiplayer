package com.example.unogui;


import com.example.unogui.UNO.Game;
import javafx.application.Platform;
//public final class Platform
//extends Object

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Player implements Runnable{
    public static ArrayList<Player>clientHandlers = new ArrayList<>();
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    Socket socket;
    String name;
    String ServerName;
    public static ArrayList<String> names=new ArrayList<>();
    static Game game;

    public Player(Socket socket,String ServerName) throws IOException, ClassNotFoundException {
        try {
            this.socket = socket;
            this.ServerName=ServerName;
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            Message message = (Message)objectInputStream.readObject();
            name= message.getText();
            names.add(name);

            clientHandlers.add(this);


        } catch (IOException e){
            CloseEverything(socket,objectOutputStream,objectInputStream);
        }
    }

    @Override
    public void run() {//Listens for messages
        Message message;
        try{
            while (socket.isConnected()) {
                try {
                    message = (Message) objectInputStream.readObject();
                    System.out.println("Message received from client: "+message.getText());
                    if(message.getText().equals("names")){
                        names.add(ServerName);
                        SendToAll(new Message("names"));
                    }


                    else if (message.getText().equals("game")) {
                        game= (Game) objectInputStream.readObject();
                        Game g =new Game(game.GetPlayers(),game.getCurrentPlayer(),game.getDeck(),game.getPlayerHand(),game.getStockPile(),game.getValidColor(),game.getValidValue(),game.isGameDirection());
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        UnoGUI.LoadGame(HostController.GetName(),g);
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            });

                        UnoGUI.SendGame(g);
                    }
                }catch (IOException | ClassNotFoundException e){
                    CloseEverything(socket,objectOutputStream,objectInputStream);
                    break;
                }
            }

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void CloseEverything(Socket socket, ObjectOutputStream objectOutputStream,ObjectInputStream objectInputStream) throws IOException {
        RemovePlayer();
        try {
            if(objectOutputStream!=null)
            objectOutputStream.close();
            if(objectInputStream!=null)
                objectInputStream.close();
            if(socket!=null)
                socket.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }



    public void SendToAll(Message message) throws IOException {//Change instance of message to Game
        if (message.getText().equals("names")){
            for(Player player:clientHandlers){
                try{
                    player.objectOutputStream.writeObject(message);
                    player.objectOutputStream.writeObject(names);
                } catch (IOException e) {
                    CloseEverything(socket,objectOutputStream,objectInputStream);
                }
            }
            System.out.println("Names are sent to everyone.");
        }
    }

    public void RemovePlayer() throws IOException {
        clientHandlers.remove(this);
        SendToAll(new Message(name+" has left the room."));
    }

    public ArrayList<Player> GetClientHandlers(){
        return clientHandlers;
    }

    public static void SendGame(Game game) {
        Game g =new Game(game.GetPlayers(),game.getCurrentPlayer(),game.getDeck(),game.getPlayerHand(),game.getStockPile(),game.getValidColor(),game.getValidValue(),game.isGameDirection());
        System.out.println("Server's cards :"+g.GetPlayerHandSize(g.GetPreviousPlayer()));
        for(Player player:clientHandlers){
            try{
                player.objectOutputStream.writeObject(new Message("game"));
                player.objectOutputStream.writeObject(g);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UnoGUI.LoadGame(HostController.GetName(),g);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public static void StartGame() throws InterruptedException, IOException {
        game = new Game(Server.GetNames());
        game.Start();
        UnoGUI.SendGame(game);
    }

    public static Game GetGame(){
        return game;
    }


    public static ArrayList<String> GetAllNames(){
        ArrayList<String>playerNames= new ArrayList<>();
        for(int i = 0;i <clientHandlers.size();i++) {
            playerNames.add(clientHandlers.get(i).name);
        }
        return playerNames;
    }

    public static void SendNames(ArrayList<String> nm){
        for(Player player:clientHandlers){
            try{
                player.objectOutputStream.writeObject(new Message("names"));
                player.objectOutputStream.writeObject(nm);
                System.out.println("Names were sent");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
