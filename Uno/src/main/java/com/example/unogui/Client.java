package com.example.unogui;

import com.example.unogui.UNO.Game;
import javafx.application.Platform;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;


public class Client {
    Socket socket = null;
    private String name;
    int ClientID;
    ArrayList<String> names = new ArrayList<>();
    Scanner in = new Scanner(System.in);
    ObjectInputStream objectInputStream = null;
    static ObjectOutputStream objectOutputStream=null;



    public Client(Socket soc, String name) {
        try {
            this.socket = soc;
            this.name = name;
            //Client's Name =(his actual username)+his # of created Client instance.

            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());

            objectOutputStream.writeObject(new Message(name));

        } catch (IOException e) {
            CloseEverything(socket, objectOutputStream, objectInputStream);
        }
    }

    public void ListenToMessage(VBox vbox) {
        Message MesFromChat;
        while (socket.isConnected()) {
            try {
                MesFromChat = (Message) objectInputStream.readObject();
                System.out.println("RECEIVED MESSAGE CLIENT:"+MesFromChat.getText());

                if(MesFromChat.getText().equals("names")){
                    names=(ArrayList<String>) objectInputStream.readObject();

                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            JoinController.Add(names,vbox);
                                        }
                                    });


                                } else if (MesFromChat.getText().equals("game")) {
                                    Game g = (Game) objectInputStream.readObject();
                                    g =new Game(g.GetPlayers(),g.getCurrentPlayer(),g.getDeck(),g.getPlayerHand(),g.getStockPile(),g.getValidColor(),g.getValidValue(),g.isGameDirection());
                                    Game finalG = g;
                                if(!g.IsGameOver()){
                                    System.out.println("The game has not ended yet.");
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                if(JoinController.GetName()!=null)
                                                UnoGUI.LoadGame(JoinController.GetName(),finalG);
                                                else
                                                    UnoGUI.LoadGame(HostController.GetName(),finalG);
                                            } catch (IOException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }
                                    });
                    }
                    else{
                                    Game finalG1 = g;
                                    Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println("Previous: "+ finalG1.GetPreviousPlayer());
                                System.out.println("Host: " +HostController.GetName());
                                System.out.println("Join: " + JoinController.GetName());
                                if(finalG1.GetPreviousPlayer().equals(HostController.GetName()) || finalG1.GetPreviousPlayer().equals(JoinController.GetName())){
                                    try {
                                        UnoGUI.Win();
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                                else{
                                    try {
                                        UnoGUI.Lose();
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                }

                            }
                        });
                        CloseEverything(socket,objectOutputStream,objectInputStream);
                        break;
                    }
                                }

                            } catch (IOException e) {
                                CloseEverything(socket, objectOutputStream, objectInputStream);
                            } catch (ClassNotFoundException e) {
                                CloseEverything(socket, objectOutputStream, objectInputStream);
                            }
                        }
                    }

    private void CloseEverything(Socket socket, ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream) {
        try {
            if(objectInputStream!=null)
                objectInputStream.close();
            if(objectOutputStream!=null)
                objectOutputStream.close();
            if(socket!=null)
                socket.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void SendGame(Game game) throws IOException {
        objectOutputStream.writeObject(new Message("game"));
        objectOutputStream.writeObject(game);
    }
}