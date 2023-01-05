package com.example.unogui;

import com.example.unogui.UNO.Game;
import javafx.application.Platform;  //javafx.application.Platform;
import javafx.scene.layout.VBox;  //VBox lays out its children in a single vertical column. If the vbox has a border and/or padding set, then the contents will be layed out within those insets.
/*
The java. net. InetAddress class provides methods to get the IP address of any hostname. An IP address is represented by 32-bit or 128-bit unsigned number. InetAddress can handle both IPv4 and IPv6 addresses.*/
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket; //This class implements server sockets. A server socket waits for requests to come in over the network. It performs some operation based on that request, and then possibly returns a result to the requester.

import java.net.Socket;  //The actual work of the socket is performed by an instance of the SocketImpl class. An application, by changing the socket factory that creates the socket implementation, can configure itself to create sockets appropriate to the local firewall.
import java.util.ArrayList; //esizable-array implementation of the List interface. Implements all optional list operations, and permits all elements, including null.

public class Server{
    private ServerSocket serverSocket;
    private  Socket socket;
    //socket server port on which it will listen
    private int port = 9876;  //port number
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    static ArrayList<String> names;
    private ArrayList<Player> ClientHandlers;
    String ServerName;

    private static Game game;

    public Server() throws IOException {
        try {
            serverSocket = new ServerSocket(6969);
            System.out.println("Server Socket was set");

        }catch (IOException e){
            System.out.println("ERROR CREATING SOCKET");
            e.printStackTrace();
        }

        System.out.println("Server IP: " + InetAddress.getLocalHost().getHostAddress());
        System.out.println("Waiting for the client request");
    }

    public static void SetGame(Game gamee){
        game =gamee;
    }

    public static Game GetGame() {
        return game;
    }

    public void run(VBox vbox){
        try{
            while (!serverSocket.isClosed()){
                this.socket = serverSocket.accept();
                System.out.println("A new user has joined");
                Player player = new Player(socket,ServerName);
                System.out.println("Running a thread");
                Thread thread = new Thread(player);
                thread.start();

                names=player.GetAllNames();

                Player.SendNames(names);

                ClientHandlers=player.GetClientHandlers();

                System.out.println("Connected clients' names:");
                for(String name: names){
                    System.out.println(name);
                }
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        HostController.Add(names,vbox);
                    }
                });
            }
        } catch (IOException e) {
            CloseEverything();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void CloseEverything(){
        try {
            if(socket != null){
                socket.close();
            }
            if(objectInputStream != null) {
                objectInputStream.close();
            }
            if(objectOutputStream !=null) {
                objectOutputStream.close();
            }
        }catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    public static ArrayList GetNames() throws InterruptedException {
        return names;
    }
}