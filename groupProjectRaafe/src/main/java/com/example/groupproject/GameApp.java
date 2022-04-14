package com.example.groupproject;


import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.QuadCurve;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Scanner;
import java.net.Socket;

public class GameApp extends Application {

    Scene gameScene;
    Pane canvas = new Pane();
    Stage stage0;
    static Client client;

    boolean turn = true;
    boolean threadStarted = false;
    Group tokens = new Group();
    int board[] = {0, 0, 0, 0, 0, 0, 0, 0, 0};

    // Cleans all variables for another game
    public void clear() {
        tokens.getChildren().clear();
        board = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
        turn = true;
    }

    //This function displays the winner page
    public void drawWinner(Stage stage) {
        Scene winScene;
        Pane winCanvas = new Pane();
        String phrase = "You Win!";
        Button exitWinScreen = new Button("Nice");
        Text youWin = new Text(285, 250, phrase);
        youWin.setScaleX(8);
        youWin.setScaleY(8);
        exitWinScreen.resizeRelocate(280, 460, 1, 1);
        exitWinScreen.setOnAction(e -> stage.setScene(gameScene));
        clear();

        winCanvas.getChildren().addAll(exitWinScreen, youWin);
        winScene = new Scene(winCanvas, 600, 500);
        stage.setScene(winScene);
    }

    //This function displays the loser page
    public void drawLoser(Stage stage) {
        Scene winScene;
        Pane winCanvas = new Pane();
        String phrase = "You Lose :(";
        Button exitWinScreen = new Button("Tsk..");
        Text youWin = new Text(285, 250, phrase);
        youWin.setScaleX(8);
        youWin.setScaleY(10);
        exitWinScreen.resizeRelocate(280, 460, 1, 1);
        exitWinScreen.setOnAction(e -> stage.setScene(gameScene));
        clear();

        winCanvas.getChildren().addAll(exitWinScreen, youWin);
        winScene = new Scene(winCanvas, 600, 500);
        stage.setScene(winScene);
    }

    //This function displays the game draw page
    public void drawDraw(Stage stage) {
        Scene drawScene;
        Pane drawCanvas = new Pane();
        String phrase = "It's a Draw!";
        Button exitDrawScreen = new Button("Ok");
        Text itsDraw = new Text(285, 250, phrase);
        itsDraw.setScaleX(8);
        itsDraw.setScaleY(8);
        exitDrawScreen.resizeRelocate(280, 460, 1, 1);
        exitDrawScreen.setOnAction(e -> stage.setScene(gameScene));
        clear();

        drawCanvas.getChildren().addAll(exitDrawScreen, itsDraw);
        drawScene = new Scene(drawCanvas, 600, 500);
        stage.setScene(drawScene);
    }

    //This function displays the player left page
    public void drawOpponentLeft(Stage stage) {
        Scene winScene;
        Pane winCanvas = new Pane();
        String phrase = "Opponent Left!";
        Button exitWinScreen = new Button("Exit");
        Text youWin = new Text(285, 250, phrase);
        youWin.setScaleX(8);
        youWin.setScaleY(8);
        exitWinScreen.resizeRelocate(280, 460, 1, 1);
        exitWinScreen.setOnAction(e -> stage.setScene(gameScene));
        clear();

        winCanvas.getChildren().addAll(exitWinScreen, youWin);
        winScene = new Scene(winCanvas, 600, 500);
        stage.setScene(winScene);
    }

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

    // This function draws the players corresponding token within the tile they clicked in if it is their turn
    public void drawToken(String token, int loc) {

        // System.out.println(mouseX + " " + mouseY); // For debugging purposes

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

    @Override
    public void start(Stage stage) throws IOException, Exception {

        stage0 = stage;

        stage.setTitle("Tic Tac & Chat");

        //Dropdown menu to do stuff                                             ///////////////NEED TO FINISH IMPLEMENTATION///////////////
        Menu fileMenu = new Menu("Menu");
        //Wipes everything and creates a new game
        MenuItem item1 = new MenuItem("New Game");
        item1.setOnAction(e -> clear());
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


        //create button and change scene on press                                                           ///////////////NEED TO FINISH IMPLEMENTATION///////////////
        Button goToChat = new Button("Chat");
        goToChat.setOnAction(e -> turn = true); /////////////// THIS LINE OF CODE IS TO TEST TURN BASED SYSTEM, DELETE WHEN DONE. ///////////////
        goToChat.resizeRelocate(280, 460, 1, 1);
        //goToChat.setOnAction(e -> stage.setScene(chatScene)); /////////////// UNCOMMENT WHEN CHAT SCENE EXISTS. ///////////////


        //Mouse click system to spawn tokens
        canvas.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                String loc = checkMove(event.getX(), event.getY());
                if (!loc.equals("INVALID")) {
                    client.printOut("MOVE " + loc);
                    System.out.println("sent location");
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
        System.out.println("connected");
        launch(args);
    }
}
