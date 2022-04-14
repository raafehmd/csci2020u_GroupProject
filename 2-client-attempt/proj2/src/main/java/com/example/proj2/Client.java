package com.example.proj2;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class Client extends Application {
    DataInputStream fromChatRoom = null;
    DataOutputStream toChatRoom = null;
    String player = "Player 1";
    TextArea textArea = new TextArea();
    VBox vBox = new VBox();
    ClientClass client;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Menu menu = new Menu("Menu");
        MenuBar menuBar = new MenuBar();
        MenuItem newChat = new MenuItem("New chat");
        MenuItem exit = new MenuItem("Exit");
        menu.getItems().add(newChat);
        menu.getItems().add(exit);
        menuBar.getMenus().add(menu);

        Button backToGame = new Button();
        backToGame.setText("Game");


        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(5, 5, 5, 5));
        Button send = new Button("Send");
        pane.setRight(send);

        TextField textField = new TextField();
        textField.setPromptText("Type anything...");
        textField.setAlignment(Pos.BOTTOM_LEFT);
        pane.setCenter(textField);


        BorderPane topPane = new BorderPane();
        /*Label playerName = new Label(player);
        playerName.setAlignment(Pos.TOP_CENTER);
        playerName.setFont(Font.font("Arial Rounded MT Bold", 20));
        topPane.setCenter(playerName);*/
        topPane.setLeft(menuBar);
        topPane.setPadding(new Insets(5,5,5,5));
        topPane.setRight(backToGame);


        BorderPane primaryPane = new BorderPane();
        vBox.setPrefSize(450, 200);

        textArea.setEditable(false);
        textField.setFocusTraversable(true);

        textArea.setFocusTraversable(false);
        ScrollPane scrollPane = new ScrollPane(vBox);
        primaryPane.setCenter(scrollPane);
        primaryPane.setBottom(pane);
        primaryPane.setTop(topPane);
        BorderPane.setAlignment(topPane, Pos.CENTER);


        Scene scene = new Scene(primaryPane, 500, 300);
        textField.requestFocus();
        primaryStage.setHeight(300);
        primaryStage.setWidth(500);
        primaryStage.setTitle("User");
        primaryStage.setScene(scene);
        primaryStage.show();

        try {
            client = new ClientClass(new Socket("localhost", 6666));
            System.out.println("Connected to server.");
        } catch (IOException e){
            e.printStackTrace();
        }


        client.receiveMessage(vBox);
        vBox.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                scrollPane.setVvalue((Double) t1);
            }
        });

        send.setOnAction(e-> {addLabelTo(textField);});
        textField.setOnAction(e -> {
            addLabelTo(textField);
        });
        exit.setOnAction(e -> {
            Platform.exit();
            System.exit(0);
        });
        newChat.setOnAction(e -> {
            vBox.getChildren().clear();
        });
    }

    public void addLabelTo(TextField textField){
        String message = textField.getText();
        if (!message.isEmpty()){
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_RIGHT);

            hBox.setPadding(new Insets(5,5,5,10));
            Text text = new Text(message);
            TextFlow textFlow = new TextFlow(text);

            textFlow.setStyle("-fx-color: rgb(0,0,0);" +
                    "-fx-background-color: rgb(233,233,235);" +
                    "-fx-background-radius: 20px;");
            textFlow.setPadding(new Insets(5,10,5,10));

            hBox.getChildren().add(textFlow);
            vBox.getChildren().add(hBox);

            client.sendMessage(message);
            textField.clear();
        }
    }

    public static  void addLabelFrom(String message, VBox vBox){
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5,5,5,10));

        Text text =  new Text(message);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-background-color: rgb(233,233,235);" +
                "-fx-background-radius: 20px;");
        textFlow.setPadding(new Insets(5,10,5,10));
        hBox.getChildren().add(textFlow);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vBox.getChildren().add(hBox);
            }
        });
    }
}
