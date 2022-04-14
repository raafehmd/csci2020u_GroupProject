package com.example.groupproject;


import java.util.Scanner;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    private Socket socket;
    private Scanner in;
    private PrintWriter out;

    public Client(String serverAddress) throws Exception {
        socket = new Socket(serverAddress, 58901);
        in = new Scanner(socket.getInputStream());
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    public Scanner getScanner() {
        return in;
    }

    public Socket getSocket() {
        return socket;
    }

    public void printOut(String msg) {
        out.println(msg);
    }

    public static void main(String[] args) throws Exception {
    }
}
