package com.example.groupproject;


import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.QuadCurve;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.util.Scanner;
import java.net.Socket;

/**
 * Contains and handles all display methods to display the game and chat windows
 * and some logic to handle the application.
 */
public class GameApp extends Application {

    Scene gameScene;
    Scene chatScene;
    Stage stage = new Stage();
    Pane canvas = new Pane();

    static Client client;

    String myToken = "X";

    boolean threadStarted = false;
    Group tokens = new Group();
    int board[] = {0, 0, 0, 0, 0, 0, 0, 0, 0};

    /**
     * Resets all variables in preparation for another game
     */
    public void clear() {
        tokens.getChildren().clear();
        board = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
    }

    /**
     * This function displays the winner page
     *
     * @param stage  The current stage that the application is on.
     */
    public void drawWinner(Stage stage) {
        Scene winScene;
        Pane winCanvas = new Pane();
        String phrase = "You Win!";
        Button exitWinScreen = new Button("Nice");
        Text youWin = new Text(285, 250, phrase);
        youWin.setScaleX(8);
        youWin.setScaleY(8);
        exitWinScreen.resizeRelocate(280, 460, 1, 1);
        exitWinScreen.setOnAction(e -> stage.close());
        clear();

        winCanvas.getChildren().addAll(exitWinScreen, youWin);
        winScene = new Scene(winCanvas, 600, 500);
        stage.setScene(winScene);
    }

    /**
     * This function displays the loser page
     *
     * @param stage  The current stage that the application is on.
     */
    public void drawLoser(Stage stage) {
        Scene winScene;
        Pane winCanvas = new Pane();
        String phrase = "You Lose :(";
        Button exitWinScreen = new Button("Tsk..");
        Text youWin = new Text(285, 250, phrase);
        youWin.setScaleX(8);
        youWin.setScaleY(8);
        exitWinScreen.resizeRelocate(280, 460, 1, 1);
        exitWinScreen.setOnAction(e -> stage.close());
        clear();

        winCanvas.getChildren().addAll(exitWinScreen, youWin);
        winScene = new Scene(winCanvas, 600, 500);
        stage.setScene(winScene);
    }

    /**
     * This function displays the draw page
     *
     * @param stage  The current stage that the application is on.
     */
    public void drawDraw(Stage stage) {
        Scene drawScene;
        Pane drawCanvas = new Pane();
        String phrase = "It's a Draw!";
        Button exitDrawScreen = new Button("Ok");
        Text itsDraw = new Text(285, 250, phrase);
        itsDraw.setScaleX(8);
        itsDraw.setScaleY(8);
        exitDrawScreen.resizeRelocate(280, 460, 1, 1);
        exitDrawScreen.setOnAction(e -> stage.close());
        clear();

        drawCanvas.getChildren().addAll(exitDrawScreen, itsDraw);
        drawScene = new Scene(drawCanvas, 600, 500);
        stage.setScene(drawScene);
    }

    /**
     * This function displays a page that says that a player left
     *
     * @param stage  The current stage that the application is on.
     */
    public void drawOpponentLeft(Stage stage) {
        Scene winScene;
        Pane winCanvas = new Pane();
        String phrase = "Opponent Left!";
        Button exitWinScreen = new Button("Exit");
        Text youWin = new Text(285, 250, phrase);
        youWin.setScaleX(8);
        youWin.setScaleY(8);
        exitWinScreen.resizeRelocate(280, 460, 1, 1);
        exitWinScreen.setOnAction(e -> stage.close());
        clear();

        winCanvas.getChildren().addAll(exitWinScreen, youWin);
        winScene = new Scene(winCanvas, 600, 500);
        stage.setScene(winScene);
    }
    /**
     * This function determines if the location the player clicked on was a valid tile
     *
     * @param mouseX The x value of the mouse position.
     * @param mouseY The y value of the mouse position.
     * @return a string that is invalid or an index of the playing board
     */
    public String checkMove(double mouseX, double mouseY) {
        String loc = "INVALID";

        if (mouseX >= 150 && mouseX <= 250 && mouseY >= 100 && mouseY <= 200 && board[0] == 0) {
            loc = "0";
            board[0] = 1;
        } else if (mouseX >= 250 && mouseX <= 350 && mouseY >= 100 && mouseY <= 200 && board[1] == 0) {
            loc = "1";
            board[1] = 1;
        } else if (mouseX >= 350 && mouseX <= 450 && mouseY >= 100 && mouseY <= 200 && board[2] == 0) {
            loc = "2";
            board[2] = 1;
        } else if (mouseX >= 150 && mouseX <= 250 && mouseY >= 200 && mouseY <= 300 && board[3] == 0) {
            loc = "3";
            board[3] = 1;
        } else if (mouseX >= 250 && mouseX <= 350 && mouseY >= 200 && mouseY <= 300 && board[4] == 0) {
            loc = "4";
            board[4] = 1;
        } else if (mouseX >= 350 && mouseX <= 450 && mouseY >= 200 && mouseY <= 300 && board[5] == 0) {
            loc = "5";
            board[5] = 1;
        } else if (mouseX >= 150 && mouseX <= 250 && mouseY >= 300 && mouseY <= 400 && board[6] == 0) {
            loc = "6";
            board[6] = 1;
        } else if (mouseX >= 250 && mouseX <= 350 && mouseY >= 300 && mouseY <= 400 && board[7] == 0) {
            loc = "7";
            board[7] = 1;
        } else if (mouseX >= 350 && mouseX <= 450 && mouseY >= 300 && mouseY <= 400 && board[8] == 0) {
            loc = "8";
            board[8] = 1;
        }

        return loc;
    }

    /**
     * This function adds the client's own message (outgoing) to the chat box
     *
     * @param textField the message that will be sent to another client.
     * @param vBox the layout of the scene
     */
    public void drawToken(String token, int loc) {

        if (loc == 0) {
            if (token.equals("X")) {
                Line X1 = new Line(170, 120, 230, 180);
                Line X2 = new Line(230, 120, 170, 180);
                tokens.getChildren().addAll(X1, X2);
            } else {
                Circle O1 = new Circle(200, 150, 30);
                O1.setFill(Color.TRANSPARENT);
                O1.setStroke(Color.BLACK);
                tokens.getChildren().addAll(O1);
            }
        } else if (loc == 1) {
            if (token.equals("X")) {
                Line X1 = new Line(270, 120, 330, 180);
                Line X2 = new Line(330, 120, 270, 180);
                tokens.getChildren().addAll(X1, X2);
            } else {
                Circle O1 = new Circle(300, 150, 30);
                O1.setFill(Color.TRANSPARENT);
                O1.setStroke(Color.BLACK);
                tokens.getChildren().addAll(O1);
            }
        } else if (loc == 2) {
            if (token.equals("X")) {
                Line X1 = new Line(370, 120, 430, 180);
                Line X2 = new Line(430, 120, 370, 180);
                tokens.getChildren().addAll(X1, X2);
            } else {
                Circle O1 = new Circle(400, 150, 30);
                O1.setFill(Color.TRANSPARENT);
                O1.setStroke(Color.BLACK);
                tokens.getChildren().addAll(O1);
            }
        } else if (loc == 3) {
            if (token.equals("X")) {
                Line X1 = new Line(170, 220, 230, 280);
                Line X2 = new Line(230, 220, 170, 280);
                tokens.getChildren().addAll(X1, X2);
            } else {
                Circle O1 = new Circle(200, 250, 30);
                O1.setFill(Color.TRANSPARENT);
                O1.setStroke(Color.BLACK);
                tokens.getChildren().addAll(O1);
            }
        } else if (loc == 4) {
            if (token.equals("X")) {
                Line X1 = new Line(270, 220, 330, 280);
                Line X2 = new Line(330, 220, 270, 280);
                tokens.getChildren().addAll(X1, X2);
            } else {
                Circle O1 = new Circle(300, 250, 30);
                O1.setFill(Color.TRANSPARENT);
                O1.setStroke(Color.BLACK);
                tokens.getChildren().addAll(O1);
            }
        } else if (loc == 5) {
            if (token.equals("X")) {
                Line X1 = new Line(370, 220, 430, 280);
                Line X2 = new Line(430, 220, 370, 280);
                tokens.getChildren().addAll(X1, X2);
            } else {
                Circle O1 = new Circle(400, 250, 30);
                O1.setFill(Color.TRANSPARENT);
                O1.setStroke(Color.BLACK);
                tokens.getChildren().addAll(O1);
            }
        } else if (loc == 6) {
            if (token.equals("X")) {
                Line X1 = new Line(170, 320, 230, 380);
                Line X2 = new Line(230, 320, 170, 380);
                tokens.getChildren().addAll(X1, X2);
            } else {
                Circle O1 = new Circle(200, 350, 30);
                O1.setFill(Color.TRANSPARENT);
                O1.setStroke(Color.BLACK);
                tokens.getChildren().addAll(O1);
            }
        } else if (loc == 7) {
            if (token.equals("X")) {
                Line X1 = new Line(270, 320, 330, 380);
                Line X2 = new Line(330, 320, 270, 380);
                tokens.getChildren().addAll(X1, X2);
            } else {
                Circle O1 = new Circle(300, 350, 30);
                O1.setFill(Color.TRANSPARENT);
                O1.setStroke(Color.BLACK);
                tokens.getChildren().addAll(O1);
            }
        } else if (loc == 8) {
            if (token.equals("X")) {
                Line X1 = new Line(370, 320, 430, 380);
                Line X2 = new Line(430, 320, 370, 380);
                tokens.getChildren().addAll(X1, X2);
            } else {
                Circle O1 = new Circle(400, 350, 30);
                O1.setFill(Color.TRANSPARENT);
                O1.setStroke(Color.BLACK);
                tokens.getChildren().addAll(O1);
            }
        }
    }

    /**
     * This function adds the client's own message (outgoing) to the chat box
     *
     * @param textField the message that will be sent to another client.
     * @param vBox the layout of the scene
     */
    public void sendMessage(TextField textField, VBox vBox) {
        String messageToSend = textField.getText();
        if (!messageToSend.isEmpty()) {
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_RIGHT);
            hBox.setPadding(new Insets(5, 5, 5, 10));

            Text text = new Text(messageToSend);
            TextFlow tf = new TextFlow(text);

            tf.setStyle("-fx-color: rgb(0,0,0);" +
                    "-fx-background-color: rgb(233,233,235);" +
                    "-fx-background-radius: 20px;");
            tf.setPadding(new Insets(5, 10, 5, 10));

            hBox.getChildren().add(tf);
            vBox.getChildren().add(hBox);

            client.printOut("CHAT " + messageToSend);
            textField.clear();
        }
    }

    /**
     * This function adds incoming messages to the chat box
     *
     * @param messageFromClient the message that will be recieved from another client.
     * @param vbox the layout of the scene
     */
    public static void getMessage(String messageFromClient, VBox vbox) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5, 5, 5, 10));

        Text text = new Text(messageFromClient);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-background-color: rgb(233,233,235);" +
                "-fx-background-radius: 20px;");
        textFlow.setPadding(new Insets(5, 10, 5, 10));
        hBox.getChildren().add(textFlow);

        Platform.runLater(() -> vbox.getChildren().add(hBox));
    }

    @Override
    public void start(Stage stage) throws IOException, Exception {

        // Designing game scene
        stage.setTitle("Tic Tac & Chat - Game Window");

        //Dropdown menu to do stuff
        Menu fileMenu = new Menu("Menu");
        //Wipes everything and creates a new game
        MenuItem item1 = new MenuItem("New Game");
        //Closes the application
        MenuItem item2 = new MenuItem("Exit");
        item2.setOnAction(e -> stage.close());
        //Adding all the menu options to the menu
        fileMenu.getItems().addAll(item1, item2);
        MenuBar menuBar = new MenuBar(fileMenu);

        //Draw left player
        Label label1 = new Label("PLAYER 1");
        label1.resizeRelocate(45, 140, 1, 1);
        Circle head = new Circle(20);
        head.resizeRelocate(47, 178, 10, 10);
        QuadCurve body = new QuadCurve(35, 250, 65, 170, 100, 250);

        //Draw right player
        Label label2 = new Label("PLAYER 2");
        label2.resizeRelocate(510, 140, 1, 1);
        Circle head2 = new Circle(20);
        head2.resizeRelocate(512, 178, 10, 10);
        QuadCurve body2 = new QuadCurve(500, 250, 530, 170, 565, 250);

        //Draw the game board
        //Vertical lines
        Line board1 = new Line(250, 100, 250, 400);
        Line board2 = new Line(350, 100, 350, 400);
        //Horizontal lines
        Line board3 = new Line(150, 200, 450, 200);
        Line board4 = new Line(150, 300, 450, 300);


        // Designing chat scene
        // Drop down menu
        Menu menu = new Menu("Menu");
        MenuBar menuBar_chat = new MenuBar();
        // Empties chat box
        MenuItem newChat = new MenuItem("New Chat");
        // Closes application
        MenuItem exit = new MenuItem("Exit");
        menu.getItems().add(newChat);
        menu.getItems().add(exit);
        menuBar_chat.getMenus().add(menu);

        // Return to game button
        Button backToGame = new Button();
        backToGame.setText("Game");

        // Create text field and send button
        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(5, 5, 5, 5));
        Button send = new Button("Send");
        pane.setRight(send);
        TextField textField = new TextField();
        textField.setPromptText("Type anything...");
        textField.setAlignment(Pos.BOTTOM_LEFT);
        pane.setCenter(textField);

        // Placing top row objects (menu bar, back to game button)
        BorderPane topPane = new BorderPane();
        topPane.setLeft(menuBar_chat);
        topPane.setPadding(new Insets(5, 5, 5, 5));
        topPane.setRight(backToGame);

        // Creating vBox and ScrollPane which will contain the chat
        BorderPane primaryPane = new BorderPane();
        VBox vBox = new VBox();
        vBox.setPrefSize(450, 200);
        textField.setFocusTraversable(true);
        ScrollPane scrollPane = new ScrollPane(vBox);
        primaryPane.setCenter(scrollPane);
        primaryPane.setBottom(pane);
        primaryPane.setTop(topPane);
        BorderPane.setAlignment(topPane, Pos.CENTER);

        chatScene = new Scene(primaryPane, 500, 300);
        textField.requestFocus();

        backToGame.setOnAction(e -> {
            stage.setTitle("Tic Tac & Chat - Game Window");
            stage.setScene(gameScene);
        });
        textField.setOnAction(e -> {
            sendMessage(textField, vBox);
        });
        send.setOnAction(e -> {
            sendMessage(textField, vBox);
        });
        exit.setOnAction(e -> {
            Platform.exit();
            System.exit(0);
        });
        newChat.setOnAction(e -> {
            vBox.getChildren().clear();
        });

        item1.setOnAction(e -> {
            client.printOut("NEW " + myToken);
            vBox.getChildren().clear();
            clear();
        });

        //create button and change scene on press
        Button goToChat = new Button("Chat");
        goToChat.resizeRelocate(280, 460, 1, 1);
        goToChat.setOnAction(e -> {
            stage.setTitle("Tic Tac & Chat - Chat Window");
            stage.setScene(chatScene);
        });


        //Mouse click system to spawn tokens
        canvas.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                String loc = checkMove(event.getX(), event.getY());
                if (!loc.equals("INVALID")) {
                    client.printOut("MOVE " + loc);
                }
            }
        });

        if (!threadStarted) {
            new Thread(() -> {
                Socket socket = client.getSocket();
                try {
                    Scanner in = client.getScanner();
                    var response = in.nextLine();
                    var token = response.substring(8);
                    myToken = token;
                    if (token.equals("X")) {
                        head2.setFill(Color.GREY);
                        body2.setFill(Color.GREY);
                    } else {
                        head.setFill(Color.GREY);
                        body.setFill(Color.GREY);
                    }
                    var opponentToken = token.equals("X") ? "O" : "X";
                    while (in.hasNextLine()) {
                        response = in.nextLine();
                        if (response.startsWith("VALID_MOVE")) {
                            int loc = Integer.parseInt(response.substring(11));
                            Platform.runLater(() -> {
                                drawToken(token, loc);
                            });
                        } else if (response.startsWith("OPPONENT_MOVED")) {
                            var loc = Integer.parseInt(response.substring(15));
                            Platform.runLater(() -> {
                                drawToken(opponentToken, loc);
                            });
                        } else if (response.startsWith("CHAT")) {
                            final String message = response.substring(5);
                            Platform.runLater(() -> {
                                getMessage(message, vBox);
                            });
                        } else if (response.startsWith("VICTORY")) {
                            Platform.runLater(() -> {
                                drawWinner(stage);
                            });
                            break;
                        } else if (response.startsWith("DEFEAT")) {
                            Platform.runLater(() -> {
                                drawLoser(stage);
                            });
                            break;
                        } else if (response.startsWith("DRAW")) {
                            Platform.runLater(() -> {
                                drawDraw(stage);
                            });
                            break;
                        } else if (response.startsWith("OTHER_PLAYER_LEFT")) {
                            Platform.runLater(() -> {
                                drawOpponentLeft(stage);
                            });
                            break;
                        } else if (response.equals("NEW")) {
                            Platform.runLater(() -> {
                                clear();
                                vBox.getChildren().clear();
                            });
                        }
                    }
                    client.printOut("QUIT");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            threadStarted = true;
        }

        //Add all elements to the canvas and display them
        canvas.getChildren().addAll(goToChat, board1, board2, board3, board4, label1, head, body, label2, head2, body2, menuBar, tokens);
        gameScene = new Scene(canvas, 600, 500);
        stage.setScene(gameScene);
        stage.show();
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Pass the server IP as the sole command line argument");
            return;
        }
        client = new Client(args[0]);
        launch(args);
    }
}
