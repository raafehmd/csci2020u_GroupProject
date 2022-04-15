package com.example.groupproject;


import java.util.Scanner;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * This class connects with the server and sends the server information
 */
public class Client {

    private Socket socket;
    private Scanner in;
    private PrintWriter out;

    /**
     * This function connects the client to the server and begins sending and recieving data.
     *
     * @param serverAddress is the address of the server the client is connecting to
     */
    public Client(String serverAddress) throws Exception {
        socket = new Socket(serverAddress, 58901);
        in = new Scanner(socket.getInputStream());
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    /**
     * This function recieves a message from the server.
     *
     * @return a message of a user
     */
    public Scanner getScanner() {
        return in;
    }

    /**
     * Javadoc text.
     *
     * @return
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * This function sends a message to the server.
     *
     * @param msg a message from the user
     */
    public void printOut(String msg) {
        out.println(msg);
    }

    public static void main(String[] args) throws Exception {
    }
}
