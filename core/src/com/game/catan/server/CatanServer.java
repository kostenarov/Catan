package com.game.catan.server;

import com.game.catan.Functionality.Functionality;
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
    private final Map map;
    private final Functionality functionality = new Functionality();

    public CatanServer(Map map) {
        clients = new ArrayList<>();
        this.map = map;

        try {
            serverSocket = new ServerSocket(12345);
            System.out.println("Server started. Waiting for clients...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket);

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clients.add(clientHandler);

                clientHandler.sendMap(map);

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
        private final Socket socket;
        private ObjectOutputStream outputStream;
        private ObjectInputStream inputStream;

        public ClientHandler(Socket socket) {
            this.socket = socket;

            try {
                outputStream = new ObjectOutputStream(socket.getOutputStream());
                inputStream = new ObjectInputStream(socket.getInputStream());
                System.out.println(map.getCenterCell());
                outputStream.writeObject(map);
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
                    Object input = inputStream.readObject();
                    System.out.println("Received from client: " + input);
                    if(input instanceof String) {
                        if(input.equals("End Turn")) {
                            currentPlayerIndex = (currentPlayerIndex + 1) % clients.size();
                        }
                        else if(input.equals("Dice Throw")) {
                            int diceThrow = functionality.diceThrow();
                            System.out.println(diceThrow);
                            outputStream.writeObject(diceThrow);
                            outputStream.reset();
                        }
                    }
                    //broadcastTurnNotification();
                }
            } catch (IOException | ClassNotFoundException e) {
                clients.remove(this);
                System.out.println("Client disconnected: " + socket);
            }
        }

        public void sendMap(Map map) {
        	try {
        		outputStream.writeObject(map);
        		outputStream.reset();
        	} catch (IOException e) {
        		e.printStackTrace();
        	}
        }
    }
}
