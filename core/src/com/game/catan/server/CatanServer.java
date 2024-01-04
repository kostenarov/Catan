package com.game.catan.server;

import java.util.HashMap;
import java.util.List;
import java.net.Socket;
import java.util.ArrayList;
import java.io.IOException;
import java.net.ServerSocket;

import com.game.catan.Functionality.Deck;
import com.game.catan.Map.Map;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import com.game.catan.Functionality.Functionality;

public class CatanServer {
    private ServerSocket serverSocket;
    private final List<ClientHandler> clients;
    private int currentPlayerIndex;
    private Map map;
    private HashMap<String, Deck> playerResources;
    private boolean isWorking = true;

    public CatanServer(Map map) {
        clients = new ArrayList<>();
        this.map = map;
        playerResources = new HashMap<>();

        try {
            serverSocket = new ServerSocket(12345);
            System.out.println("Server started. Waiting for clients...");

            while (isWorking) {
                setUpConnection();
            }
        } catch (IOException e) {
            isWorking = false;
            System.out.println("Server stopped");
        }
    }

    private void setUpConnection() throws IOException {
        Socket clientSocket = serverSocket.accept();
        System.out.println("Client connected: " + clientSocket + "with id: " + clients.size());
        playerResources.put(String.valueOf(clients.size()), new Deck());

        ClientHandler clientHandler = new ClientHandler(clientSocket);
        clients.add(clientHandler);

        new Thread(clientHandler).start();
    }

    public void setMap(Map map) {
        this.map = map;
    }

    private void broadcastTurnNotification() {
        for (ClientHandler client : clients) {
            client.sendTurnNotification(currentPlayerIndex);
        }
    }

    private void broadcastMap() {
        for (ClientHandler client : clients) {
            client.sendMap(map);
        }
    }

    private void broadcastDiceThrow(int diceThrow) {
        for (ClientHandler client : clients) {
            client.sendDiceThrow(diceThrow);
        }
    }

    private void broadcastDeck(int diceThrow) {
        for (ClientHandler client : clients) {
            client.sendDeck(diceThrow);
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
                System.out.println("Could not create client handler");
            }
        }

        @Override
        public void run() {
            try {
                while (isWorking) {
                    Object input = inputStream.readObject();
                    System.out.println("Received from client: " + input);
                    endTurnAndDiceThrowChecker(input);
                }
            } catch (IOException | ClassNotFoundException e) {
                clients.remove(this);
                System.out.println("Client disconnected: " + socket);
            }
        }

        private void endTurnAndDiceThrowChecker(Object input) {
            if(input instanceof String) {
                endTurnFunc((String) input);
                diceThrowFunc((String) input);
                resourcePressFunc((String) input);
            }
        }

        private void endTurnFunc(String input) {
            if(input.equals("End Turn")) {
                currentPlayerIndex = (currentPlayerIndex + 1) % clients.size();
                System.out.println("Current player index: " + currentPlayerIndex);
                broadcastTurnNotification();
                broadcastMap();
            }
        }

        private void diceThrowFunc(String input) {
            if(input.equals("Dice Throw")) {
                int diceThrow = Functionality.diceThrow();
                System.out.println(diceThrow);
                broadcastDiceThrow(diceThrow);
                broadcastDeck(diceThrow);
            }
        }

        private void resourcePressFunc(String input) {
            if(input.contains("Resource")) {
                System.out.println(input + " by user " + currentPlayerIndex);
            }
        }

        private void sendMap(Map map) {
            try {
                outputStream.writeObject(map);
                outputStream.reset();
            } catch (IOException e) {
                System.out.println("Could not send map");
            }
        }

        private void sendTurnNotification(int currentPlayerIndex) {
            try {
                if(currentPlayerIndex == clients.indexOf(this)) {
                    outputStream.writeObject(100);
                    outputStream.reset();
                }
                else {
                    outputStream.writeObject(200);
                    outputStream.reset();
                }
                outputStream.reset();
            } catch (IOException e) {
                System.out.println("Could not send turn notification");
            }
        }

        private void sendDiceThrow(int diceThrow) {
            try {
                outputStream.writeObject(diceThrow);
                outputStream.reset();
            } catch (IOException e) {
                System.out.println("Could not send dice throw");
            }
        }

        private void sendDeck(int diceThrow) {
            try {
                Deck tempDeck = playerResources.get(String.valueOf(clients.indexOf(this)));
                System.out.println(clients.indexOf(this));
                Deck acquiredResources = Functionality.getResources(diceThrow, map, tempDeck.getResources(), clients.indexOf(this));
                tempDeck.addDeck(acquiredResources);
                System.out.println(tempDeck.getResources());
                outputStream.writeObject(tempDeck.getResources());
                outputStream.reset();
            } catch (IOException e) {
                System.out.println("Could not send deck");
            }
        }
    }
}
