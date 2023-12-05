package com.game.catan.server;

import com.game.catan.Map.Map;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class CatanServer {
    private ServerSocket serverSocket;
    private final List<ClientHandler> clients;
    private int currentPlayerIndex;
    private Map map;

    public CatanServer() {
        clients = new ArrayList<>();

        try {
            serverSocket = new ServerSocket(12345);
            System.out.println("Server started. Waiting for clients...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket);

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clients.add(clientHandler);

                broadcastTurnNotification();

                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void broadcastTurnNotification() {
        for (ClientHandler client : clients) {
            client.sendTurnNotification(currentPlayerIndex);
        }
    }

    private class ClientHandler implements Runnable {
        private Socket socket;
        private ObjectOutputStream outputStream;
        private ObjectInputStream inputStream;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            try {
                outputStream = new ObjectOutputStream(socket.getOutputStream());
                inputStream = new ObjectInputStream(socket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void sendTurnNotification(int currentPlayerIndex) {
            try {
                outputStream.writeObject(currentPlayerIndex);
                outputStream.reset(); // Ensure the object is resent
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                while (true) {
                    // Handle client requests, for simplicity, assume it's "End Turn" for now
                    // You may want to implement a more sophisticated communication protocol
                    inputStream.readObject(); // Wait for client message

                    // Rotate players and notify clients about the new turn
                    currentPlayerIndex = (currentPlayerIndex + 1) % clients.size();
                    broadcastTurnNotification();
                }
            } catch (IOException | ClassNotFoundException e) {
                // Handle client disconnection
                clients.remove(this);
                System.out.println("Client disconnected: " + socket);
            }
        }
    }

    public static void main(String[] args) {
    	new CatanServer();
    }
}
