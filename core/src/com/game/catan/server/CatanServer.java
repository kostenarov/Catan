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
    private Map map;
    private final Functionality functionality = new Functionality();

    public CatanServer(Map map) {
        clients = new ArrayList<>();
        this.map = map;

        try {
            serverSocket = new ServerSocket(12345);
            System.out.println("Server started. Waiting for clients...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket + "with id: " + clients.size());

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clients.add(clientHandler);

                //clientHandler.sendMap(map);

                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMap(Map map) {
    	this.map = map;
    }

    private void broadcastTurnNotification() {
        for (ClientHandler client : clients) {
            client.sendTurnNotification(currentPlayerIndex);
        }
    }

    private void broadcastDiceThrow(int diceThrow) {
    	for (ClientHandler client : clients) {
    		client.sendDiceThrow(diceThrow);
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
                sendMap(map);
                outputStream.writeObject(clients.size());
                outputStream.reset();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void sendTurnNotification(int currentPlayerIndex) {
            try {
                if(currentPlayerIndex == clients.indexOf(this)) {
                    outputStream.writeObject(100);
                    outputStream.reset();
                    //sendMap(map);
                }
                else {
                    outputStream.writeObject(200);
                    outputStream.reset();
                }
                outputStream.reset();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void sendDiceThrow(int diceThrow) {
        	try {
        		outputStream.writeObject(diceThrow);
        		outputStream.reset();
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
                            broadcastTurnNotification();
                            Object map = inputStream.readObject();
                            if(map instanceof Map) {
                                setMap((Map) map);
                                sendMap((Map) map);
                            }
                        }
                        else if(input.equals("Dice Throw")) {
                            int diceThrow = functionality.diceThrow();
                            System.out.println(diceThrow);
                            broadcastDiceThrow(diceThrow);
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
