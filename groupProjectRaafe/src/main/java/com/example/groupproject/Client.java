package com.example.groupproject;


import java.io.IOException;
import java.util.Scanner;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    boolean turn = true;

    int board[] = {0, 0, 0, 0, 0, 0, 0, 0, 0};

    private Socket socket;
    private Scanner in;
    private PrintWriter out;
    private GameApp app;

    public Client(String serverAddress) throws Exception {
        socket = new Socket(serverAddress, 58901);
        in = new Scanner(socket.getInputStream());
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    public Scanner getScanner() {
        return in;
    }

    public void closeSocket() throws IOException {
        socket.close();
    }

    public String getNextLine() {
        return in.nextLine();
    }

    public void printOut(String msg) {
        out.println(msg);
    }

    public void play() throws IOException {
        try {
            var response = in.nextLine();
            var token = response.substring(8);
            var opponentToken = token == "X" ? "O" : "X";
            while (in.hasNextLine()) {
                System.out.println("here!!");
                response = in.nextLine();
                if (response.startsWith("VALID_MOVE")) {
                    int loc = Integer.parseInt(response.substring(12));
                    app.drawToken(token, loc);
                } else if (response.startsWith("OPPONENT_MOVED")) {
                    var loc = Integer.parseInt(response.substring(15));
                    app.drawToken(opponentToken, loc);
                } else if (response.startsWith("MESSAGE")) {
                } else if (response.startsWith("VICTORY")) {
                    app.drawWinner(app.stage0);
                    break;
                } else if (response.startsWith("DEFEAT")) {
                    app.drawLoser(app.stage0);
                    break;
                } else if (response.startsWith("DRAW")) {
                    app.drawDraw(app.stage0);
                    break;
                } else if (response.startsWith("OTHER_PLAYER_LEFT")) {
                    app.drawOpponentLeft(app.stage0);
                    break;
                }
            }
            out.println("QUIT");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Pass the server IP as the sole command line argument");
            return;
        }
        Client client = new Client(args[0]);
        System.out.println("connected");
    }
}
