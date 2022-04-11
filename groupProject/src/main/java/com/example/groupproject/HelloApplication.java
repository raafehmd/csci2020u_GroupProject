package com.example.groupproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.QuadCurve;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {


    //Initialize variables
    Scene gameScene;
    Pane canvas = new Pane();
    String token = "O";
    boolean turn = true;
    // this group allows the tokens to be wiped at any time to start a new game
    Group tokens = new Group();
    // This is an int list to determine if the slot already has a token within the grid
    // 0 -> unoccupied, 1 -> X, 2 -> O
    int[][] board = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};

    // Cleans all variables for another game
    public void clear() {
        tokens.getChildren().clear();
        board = new int[][]{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
        turn = true;
    }

    //This function displays the winner page
    public void drawWinner(Stage stage, String token) {
        Scene winScene;
        Pane winCanvas = new Pane();
        String phrase = token + " wins";
        Button exitWinScreen = new Button("Nice");
        Text youWin = new Text(285, 250, phrase);
        youWin.setScaleX(15);
        youWin.setScaleY(15);
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
        Button exitDrawScreen = new Button("Ah man..");
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

    // This function checks if the game has been won/lost/drawn
    public int checkWinner(int[] loc) {

        int i = loc[0];
        int j = loc[1];
        int playerToken = board[i][j];

        // This int won will indicate whether we have a win/lose/draw
        // won = 0 -> LOSS, 1 -> WIN (Current Player), 2 -> DRAW
        int won = 1;

        // check for winner in row
        if (board[i] != new int[]{0, 0, 0}) {
            for (int token : board[i]) {
                if (token != playerToken) {
                    won = 0;
                    break;
                }
            }
        }

        // check for winner in column
        if (won == 0) {
            won = 1;
            for (int ii = 0; ii < 3; ii++) {
                if (board[ii][j] != playerToken) {
                    won = 0;
                    break;
                }
            }
        }

        // check for winner on diagonals
        // left diagonal
        if (i == j && won == 0) {
            won = 1;
            for (int ii = 0; ii < 3; ii++) {
                for (int jj = 0; jj < 3; jj++) {
                    if (ii == jj && board[ii][jj] != playerToken) {
                        won = 0;
                        break;
                    }
                }
            }
        } else if (i + j == 2 && won == 0) { // right diagonal
            won = 1;
            for (int ii = 0; ii < 3; ii++) {
                for (int jj = 0; jj < 3; jj++) {
                    if (ii + jj == 2 && board[ii][jj] != playerToken) {
                        won = 0;
                        break;
                    }
                }
            }
        }

        // check for draw
        if (won == 0) { // making sure player hasn't won
            won = 2;
            for (int[] row : board) {
                if (won == 2) {
                    for (int entry : row) {
                        if (entry == 0) {
                            won = 0;
                            break;
                        }
                    }
                }
            }
        }

        return won;
    }

    // This function draws the players corresponding token within the tile they clicked in if it is their turn
    public int[] drawToken(String token, double mouseX, double mouseY) {

        // System.out.println(mouseX + " " + mouseY); // For debugging purposes

        int[] loc = {0, 0};

        int intToken = 1;
        if (token == "O") {
            intToken = 2;
        }

        if (turn == true) {
            if (mouseX >= 150 && mouseX <= 250 && mouseY >= 100 && mouseY <= 200 && board[0][0] == 0) {
                if (token == "X") {
                    Line X1 = new Line(170, 120, 230, 180);
                    Line X2 = new Line(230, 120, 170, 180);
                    tokens.getChildren().addAll(X1, X2);
                }
                if (token == "O") {
                    Circle O1 = new Circle(200, 150, 30);
                    O1.setFill(Color.TRANSPARENT);
                    O1.setStroke(Color.BLACK);
                    tokens.getChildren().addAll(O1);
                }
                board[0][0] = intToken;
            }
            if (mouseX >= 250 && mouseX <= 350 && mouseY >= 100 && mouseY <= 200 && board[0][1] == 0) {
                if (token == "X") {
                    Line X1 = new Line(270, 120, 330, 180);
                    Line X2 = new Line(330, 120, 270, 180);
                    tokens.getChildren().addAll(X1, X2);
                }
                if (token == "O") {
                    Circle O1 = new Circle(300, 150, 30);
                    O1.setFill(Color.TRANSPARENT);
                    O1.setStroke(Color.BLACK);
                    tokens.getChildren().addAll(O1);
                }
                board[0][1] = intToken;
                loc = new int[]{0, 1};
            }
            if (mouseX >= 350 && mouseX <= 450 && mouseY >= 100 && mouseY <= 200 && board[0][2] == 0) {
                if (token == "X") {
                    Line X1 = new Line(370, 120, 430, 180);
                    Line X2 = new Line(430, 120, 370, 180);
                    tokens.getChildren().addAll(X1, X2);
                }
                if (token == "O") {
                    Circle O1 = new Circle(400, 150, 30);
                    O1.setFill(Color.TRANSPARENT);
                    O1.setStroke(Color.BLACK);
                    tokens.getChildren().addAll(O1);
                }
                board[0][2] = intToken;
                loc = new int[]{0, 2};
            }
            if (mouseX >= 150 && mouseX <= 250 && mouseY >= 200 && mouseY <= 300 && board[1][0] == 0) {
                if (token == "X") {
                    Line X1 = new Line(170, 220, 230, 280);
                    Line X2 = new Line(230, 220, 170, 280);
                    tokens.getChildren().addAll(X1, X2);
                }
                if (token == "O") {
                    Circle O1 = new Circle(200, 250, 30);
                    O1.setFill(Color.TRANSPARENT);
                    O1.setStroke(Color.BLACK);
                    tokens.getChildren().addAll(O1);
                }
                board[1][0] = intToken;
                loc = new int[]{1, 0};
            }
            if (mouseX >= 250 && mouseX <= 350 && mouseY >= 200 && mouseY <= 300 && board[1][1] == 0) {
                if (token == "X") {
                    Line X1 = new Line(270, 220, 330, 280);
                    Line X2 = new Line(330, 220, 270, 280);
                    tokens.getChildren().addAll(X1, X2);
                }
                if (token == "O") {
                    Circle O1 = new Circle(300, 250, 30);
                    O1.setFill(Color.TRANSPARENT);
                    O1.setStroke(Color.BLACK);
                    tokens.getChildren().addAll(O1);
                }
                board[1][1] = intToken;
                loc = new int[]{1, 1};
            }
            if (mouseX >= 350 && mouseX <= 450 && mouseY >= 200 && mouseY <= 300 && board[1][2] == 0) {
                if (token == "X") {
                    Line X1 = new Line(370, 220, 430, 280);
                    Line X2 = new Line(430, 220, 370, 280);
                    tokens.getChildren().addAll(X1, X2);
                }
                if (token == "O") {
                    Circle O1 = new Circle(400, 250, 30);
                    O1.setFill(Color.TRANSPARENT);
                    O1.setStroke(Color.BLACK);
                    tokens.getChildren().addAll(O1);
                }
                board[1][2] = intToken;
                loc = new int[]{1, 2};
            }
            if (mouseX >= 150 && mouseX <= 250 && mouseY >= 300 && mouseY <= 400 && board[2][0] == 0) {
                if (token == "X") {
                    Line X1 = new Line(170, 320, 230, 380);
                    Line X2 = new Line(230, 320, 170, 380);
                    tokens.getChildren().addAll(X1, X2);
                }
                if (token == "O") {
                    Circle O1 = new Circle(200, 350, 30);
                    O1.setFill(Color.TRANSPARENT);
                    O1.setStroke(Color.BLACK);
                    tokens.getChildren().addAll(O1);
                }
                board[2][0] = intToken;
                loc = new int[]{2, 0};
            }
            if (mouseX >= 250 && mouseX <= 350 && mouseY >= 300 && mouseY <= 400 && board[2][1] == 0) {
                if (token == "X") {
                    Line X1 = new Line(270, 320, 330, 380);
                    Line X2 = new Line(330, 320, 270, 380);
                    tokens.getChildren().addAll(X1, X2);
                }
                if (token == "O") {
                    Circle O1 = new Circle(300, 350, 30);
                    O1.setFill(Color.TRANSPARENT);
                    O1.setStroke(Color.BLACK);
                    tokens.getChildren().addAll(O1);
                }
                board[2][1] = intToken;
                loc = new int[]{2, 1};
            }
            if (mouseX >= 350 && mouseX <= 450 && mouseY >= 300 && mouseY <= 400 && board[2][2] == 0) {
                if (token == "X") {
                    Line X1 = new Line(370, 320, 430, 380);
                    Line X2 = new Line(430, 320, 370, 380);
                    tokens.getChildren().addAll(X1, X2);
                }
                if (token == "O") {
                    Circle O1 = new Circle(400, 350, 30);
                    O1.setFill(Color.TRANSPARENT);
                    O1.setStroke(Color.BLACK);
                    tokens.getChildren().addAll(O1);
                }
                board[2][2] = intToken;
                loc = new int[]{2, 2};
            }
            //turn = false;
        }

        return loc;
    }

    @Override
    public void start(Stage stage) throws IOException, Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
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
        Label label1 = new Label("player 1");
        label1.resizeRelocate(45, 140, 1, 1);
        Circle head = new Circle(20);
        head.resizeRelocate(47, 178, 10, 10);
        QuadCurve body = new QuadCurve(35, 250, 65, 170, 100, 250);

        //Draw right player
        Label label2 = new Label("player 2");
        label2.resizeRelocate(510, 140, 1, 1);
        Circle head2 = new Circle(20);
        head2.resizeRelocate(512, 178, 10, 10);
        QuadCurve body2 = new QuadCurve(500, 250, 530, 170, 565, 250);
        head2.setFill(Color.GREY);
        body2.setFill(Color.GREY);

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
            //Limit gameplay to turns
            if (turn) {
                if (event.getButton() == MouseButton.PRIMARY) {
                    int[] loc = drawToken(token, event.getX(), event.getY());
                    int res = checkWinner(loc);
                    if (res == 1) {
                        System.out.println(token + " IS THE WINNER");
                        drawWinner(stage, token);
                    } else if (res == 2) {
                        System.out.println("GAME DRAW");
                        drawDraw(stage);
                    }
                }
            }
        });


        // Testing game mechanics by changing token mid-game
        // to simulate back and forth between players
        canvas.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.C) {
                if (token == "X") {
                    token = "O";
                } else if (token == "O") {
                    token = "X";
                }
            }
        });


        //Add all elements to the canvas and display them
        canvas.getChildren().addAll(goToChat, board1, board2, board3, board4, label1, head, body, label2, head2, body2, menuBar, tokens);
        gameScene = new Scene(canvas, 600, 500);
        stage.setScene(gameScene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}