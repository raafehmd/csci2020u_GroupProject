package com.example.proj2;

import javafx.scene.layout.VBox;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerClass {

    private ServerSocket serverSocket;
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public ServerClass(ServerSocket serverSocket) {
        try {
            this.serverSocket = serverSocket;
            this.socket = serverSocket.accept();
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
                        Server.addLabelFrom(message, vBox);
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
