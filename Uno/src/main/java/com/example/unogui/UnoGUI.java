package com.example.unogui;

import com.example.unogui.UNO.Game;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;  //Loads an object hierarchy from an XML document.
import javafx.scene.Parent;  //JavaFX layouts are components which contains other components inside them.
import javafx.scene.Scene; //The stage is the outer frame for a JavaFX application. The stage typically corresponds to a window.
import javafx.stage.Stage;//To display anything on a stage in a JavaFX application, you need a scene. A stage can only show one scene at a time, but it is possible to exchange the scene at runtime.

import java.io.IOException;
//Application class from which JavaFX applications extend.
/*
JavaFX creates an application thread for running the application start method,
 processing input events, and running animation timelines.
 Creation of JavaFX Scene and Stage objects as well as modification of scene graph operations to live objects (those objects already attached to a scene) must be done on the JavaFX application thread.*/
public class UnoGUI  extends Application {
    static Parent root; //JavaFX layouts are components which contains other components inside them.
    static Scene scene; //To display anything on a stage in a JavaFX application, you need a scene.
    static Stage stage; //The stage is the outer frame for a JavaFX application

    static Game game;

    public static void SetGame(Game g){
        game = g;
    }

    public static Game GetGame(){
        return game;
    }

    public static void LoadWild() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(UnoGUI.class.getResource("WildCard.fxml"));

        root = fxmlLoader.load();
        scene = new Scene(root);
        stage.setFullScreen(true);  //It will make your screen full
        stage.setFullScreenExitHint("");
        scene.getStylesheets().add(UnoGUI.class.getResource("WildCard.css").toExternalForm());
        stage.setScene(scene);
        stage.show();

    }

    public static void Win() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(UnoGUI.class.getResource("WinScreen.fxml"));

        root = fxmlLoader.load();

        WinScreen winScreen =fxmlLoader.getController();
        winScreen.Load(game);

        scene = new Scene(root);
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
        stage.setScene(scene);
        stage.show();
    }

    public static void Lose() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(UnoGUI.class.getResource("LoseScreen.fxml"));

        root = fxmlLoader.load();

        LoseScreen loseScreen =fxmlLoader.getController();
        loseScreen.Load(game);

        scene = new Scene(root);
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
        stage.setScene(scene);
        stage.show();
    }

    @Override

    public void start(Stage stage) throws Exception {
    try {
         this.stage=stage;
         root = FXMLLoader.load(getClass().getResource("UnoGUI.fxml"));
         scene = new Scene(root);
         stage.setFullScreen(true);
         stage.setFullScreenExitHint("");

         scene.getStylesheets().add(getClass().getResource("UnoGUI.css").toExternalForm());
         stage.setScene(scene);
         stage.show();
    } catch (Exception e){
         e.printStackTrace();
        }
    }

    public static void SendGame(Game gam) throws IOException {
        Game g =new Game(gam.GetPlayers(),gam.getCurrentPlayer(),gam.getDeck(),gam.getPlayerHand(),gam.getStockPile(),gam.getValidColor(),gam.getValidValue(),gam.isGameDirection());
        if(JoinController.GetName()==null){
            System.out.println("Player Game:" +g.GetTopCard().getColor()+" "+g.GetTopCard().getValue());
            System.out.println("Game was sent from UnoGUI class by host");
            Player.SendGame(g);
        }
        else{
            System.out.println("Player Game:" +g.GetTopCard().getColor()+" "+g.GetTopCard().getValue());
            System.out.println("Game was sent from UnoGUI class by client");
            Client.SendGame(g);
        }
    }

    public static void LoadGame(String namo, Game gam) throws IOException {
        Game g =new Game(gam.GetPlayers(),gam.getCurrentPlayer(),gam.getDeck(),gam.getPlayerHand(),gam.getStockPile(),gam.getValidColor(),gam.getValidValue(),gam.isGameDirection());
        SetGame(g);
        if(g.IsGameOver()){
            Win();
        }
        System.out.println(g.GetCurrentPlayer());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(JoinController.class.getResource("GameScreen.fxml"));

        root = fxmlLoader.load();
        scene = new Scene(root);
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");

        GameScreenController gameScreenController = fxmlLoader.getController();
        gameScreenController.LoadGame(namo,g);

        scene.getStylesheets().add(UnoGUI.class.getResource("GameScreen.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

}
