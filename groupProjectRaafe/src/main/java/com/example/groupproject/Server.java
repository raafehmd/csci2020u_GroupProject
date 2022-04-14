package com.example.groupproject;

import javafx.scene.layout.VBox;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.spec.RSAOtherPrimeInfo;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.Executors;

public class Server {

    public static void main(String[] args) throws Exception {
        try (var listener = new ServerSocket(58901)) {
            System.out.println("Tic Tac Toe Server is Running...");
            var pool = Executors.newFixedThreadPool(200);
            while (true) {
                Game game = new Game();
                Chat chat1 = new Chat(listener.accept());
                Chat chat2 = new Chat(listener.accept());
                pool.execute(game.new Player(listener.accept(), "X"));
                pool.execute(game.new Player(listener.accept(), "O"));
                pool.execute((Runnable) chat1);
                pool.execute((Runnable) chat2);
            }
        }
    }
}

class Game {

    // Board cells numbered 0-8, top to bottom, left to right; null if empty
    private Player[] board = new Player[9];

    Player currentPlayer;

    public boolean hasWinner() {
        return (board[0] != null && board[0] == board[1] && board[0] == board[2])
                || (board[3] != null && board[3] == board[4] && board[3] == board[5])
                || (board[6] != null && board[6] == board[7] && board[6] == board[8])
                || (board[0] != null && board[0] == board[3] && board[0] == board[6])
                || (board[1] != null && board[1] == board[4] && board[1] == board[7])
                || (board[2] != null && board[2] == board[5] && board[2] == board[8])
                || (board[0] != null && board[0] == board[4] && board[0] == board[8])
                || (board[2] != null && board[2] == board[4] && board[2] == board[6]);
    }

    public boolean boardFilledUp() {
        for (Player p : board) {
            if (p == null) {
                return false;
            }
        }
        return true;
    }

    public synchronized void move(int loc, Player player) {
        if (player != currentPlayer) {
            throw new IllegalStateException("Not your turn");
        } else if (player.opponent == null) {
            throw new IllegalStateException("You don't have an opponent yet");
        } else if (board[loc] != null) {
            throw new IllegalStateException("Cell already occupied");
        }
        board[loc] = currentPlayer;
        currentPlayer = currentPlayer.opponent;
    }


    class Player implements Runnable {
        String token;
        Player opponent;
        Socket socket;
        Scanner input;
        PrintWriter output;

        public Player(Socket socket, String token) {
            this.socket = socket;
            this.token = token;
        }

        @Override
        public void run() {
            try {
                setup();
                processCommands();
            } catch (Exception e) {
                e.printStackTrace();
            }
//            } finally {
//                if (opponent != null && opponent.output != null) {
//                    opponent.output.println("OTHER_PLAYER_LEFT");
//                    System.out.println("sending left mssg");
//                }
//                try {
//                    socket.close();
//                } catch (IOException e) {
//                }
//            }
        }

        private void setup() throws IOException {
            input = new Scanner(socket.getInputStream());
            output = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("WELCOME " + token);
            output.println("WELCOME " + token);
            if (token == "X") {
                currentPlayer = this;
            } else {
                opponent = currentPlayer;
                opponent.opponent = this;
            }
        }

        private void processCommands() {
            while (input.hasNextLine()) {
                var command = input.nextLine();
                if (command.startsWith("QUIT")) {
                    return;
                } else if (command.startsWith("MOVE")) {
                    processMoveCommand(Integer.parseInt(command.substring(5)));
                }
            }
        }

        private void processMoveCommand(int loc) {
            try {
                move(loc, this);
                output.println("VALID_MOVE " + loc);
                opponent.output.println("OPPONENT_MOVED " + loc);
                if (hasWinner()) {
                    output.println("VICTORY");
                    opponent.output.println("DEFEAT");
                } else if (boardFilledUp()) {
                    output.println("DRAW");
                    opponent.output.println("DRAW");
                }
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }
}

class Chat {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public Chat(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            System.out.println("Error creating server");
            e.printStackTrace();
            close(socket, bufferedReader, bufferedWriter);
        }
    }

    public void sendMessage(String message){
        try {
            bufferedWriter.write(message);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e){
            e.printStackTrace();
            System.out.println("Error sending message");
            close(socket, bufferedReader, bufferedWriter);
        }
    }

    public void receiveMessage(VBox vBox){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (socket.isConnected()){
                    try {
                        String message = bufferedReader.readLine();
                        GameApp.addLabelFrom(message, vBox);
                    } catch (IOException e){
                        e.printStackTrace();
                        System.out.println("Error receiving message");
                        close(socket, bufferedReader, bufferedWriter);
                        break;
                    }
                }
            }
        }).start();
    }

    public void close(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        try {
            bufferedReader.close();
            bufferedWriter.close();
            socket.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
