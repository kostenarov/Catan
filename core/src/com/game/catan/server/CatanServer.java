package com.game.catan.server;

import java.util.*;
import java.net.Socket;
import java.util.HashMap;
import java.io.IOException;
import java.net.ServerSocket;

import com.game.catan.Functionality.Checkers;
import com.game.catan.Functionality.PointCounter;
import com.game.catan.Map.Cell.RoadCell;
import com.game.catan.Map.Cell.VillageCell;
import com.game.catan.Map.Map;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.game.catan.Functionality.Deck;
import com.game.catan.Functionality.Functionality;

public class CatanServer {
    private ServerSocket serverSocket;
    private final List<ClientHandler> clients;
    private int currentPlayerIndex;
    private Map map;
    private final HashMap<Integer, Deck> playerResources;
    private final ArrayList<String> villagePaths;
    private final ArrayList<String> roadPaths;
    private boolean isWorking = true;
    private int diceThrow;
    private boolean isDiceThrown = false;
    private boolean isRobberMoved = false;
    private final int initialSequenceCounter = 0;
    private final PointCounter pointCounter;

    public CatanServer(Map map) {
        clients = new ArrayList<>();
        this.map = map;
        playerResources = new HashMap<>();
        villagePaths = new ArrayList<>();
        roadPaths = new ArrayList<>();
        pointCounter = new PointCounter();
        villagePaths.add("Villages/yellowVillage.png");
        villagePaths.add("Villages/blueVillage.png");
        villagePaths.add("Villages/greenVillage.png");
        villagePaths.add("Villages/redVillage.png");
        roadPaths.add("Roads/yellowNormalRoad.png");
        roadPaths.add("Roads/blueNormalRoad.png");
        roadPaths.add("Roads/greenNormalRoad.png");
        roadPaths.add("Roads/redNormalRoad.png");
        try {
            serverSocket = new ServerSocket(12345);
            System.out.println("Server started. Waiting for clients...");

            while (isWorking) {
                if(clients.size() < 4) {
                    setUpConnection();
                }
            }
        } catch (IOException e) {
            isWorking = false;
            System.out.println("Server stopped");
        }

    }

    private void setUpConnection() throws IOException {
        Socket clientSocket = serverSocket.accept();
        System.out.println("Client connected: " + clientSocket + "with id: " + clients.size());
        playerResources.put(clients.size(), new Deck());
        pointCounter.addPlayer(clients.size());

        ClientHandler clientHandler = new ClientHandler(clientSocket);
        clients.add(clientHandler);
        broadcastPlayersAmount();
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

    private void broadcastDiceThrow() {
        for (ClientHandler client : clients) {
            client.sendDiceThrow(diceThrow);
        }
    }

    private void broadcastDeck() {
        for (ClientHandler client : clients) {
            client.sendDeck(diceThrow);
        }
    }

    private void broadcastRobberMove() {
        for (ClientHandler client : clients) {
            client.sendRobberMove();
        }
    }

    private void broadcastVillage(VillageCell villageCell) {
        for (ClientHandler client : clients) {
            client.sendVillage(villageCell);
        }
    }

    private void broadcastRoad(RoadCell roadCell) {
        for (ClientHandler client : clients) {
            client.sendRoad(roadCell);
        }
    }

    private void broadcastPlayersAmount() {
        for (ClientHandler client : clients) {
            client.sendPlayerAmount();
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
                villagePressFunc((String) input);
                RoadPressFunc((String) input);
            }
        }

        private void endTurnFunc(String input) {
            if(input.equals("End Turn") && isDiceThrown) {
                currentPlayerIndex = (currentPlayerIndex + 1) % clients.size();
                System.out.println("Current player index: " + currentPlayerIndex);
                broadcastTurnNotification();
                broadcastMap();
                isDiceThrown = false;
                isRobberMoved = false;
            }
        }

        private void diceThrowFunc(String input) {
            if(input.equals("Dice Throw") && !isDiceThrown) {
                diceThrow = Functionality.diceThrow();
                System.out.println(diceThrow);
                broadcastDiceThrow();
                broadcastDeck();
                isDiceThrown = true;
            }
        }

        private void sendPlayerAmount() {
            try {
                outputStream.writeObject("Players:" + clients.size());
                outputStream.reset();
            } catch (IOException e) {
                System.out.println("Could not send player amount");
            }
        }

        private void sendPoints() {
            try {
                outputStream.writeObject("Points:" + pointCounter.getPoints(currentPlayerIndex));
                outputStream.reset();
            } catch (IOException e) {
                System.out.println("Could not send points");
            }
        }

        private void sendRobberMove() {
            try {
                outputStream.writeObject(map.getRobberCell());
                outputStream.reset();
            } catch (IOException e) {
                System.out.println("Could not send robber move");
            }
        }

        private void sendVillage(VillageCell villageCell) {
            try {
                outputStream.writeObject(villageCell);
                outputStream.reset();
            } catch (IOException e) {
                System.out.println("Could not send village");
            }
        }

        private void sendRoad(RoadCell roadCell) {
            try {
                outputStream.writeObject(roadCell);
                outputStream.reset();
            } catch (IOException e) {
                System.out.println("Could not send road");
            }
        }

        private void resourcePressFunc(String input) {
            if(input.contains("Resource") && diceThrow == 7 && isDiceThrown) {
                isRobberMoved = true;
                System.out.println(input + " by user " + currentPlayerIndex);
                int resourceId = Integer.parseInt(input.split(":")[1]);
                map.moveRobber(resourceId);
                broadcastRobberMove();
            }
        }

        private void villagePressFunc(String input) {
            if(input.contains("Village") && isDiceThrown) {
                System.out.println(input + " by user " + currentPlayerIndex);
                int villageId = Integer.parseInt(input.split(":")[1]);
                Deck currentDeck = playerResources.get(currentPlayerIndex);
                if(Checkers.areVillageRequirementsMet(map.getVillageCellById(villageId), currentDeck, currentPlayerIndex)) {
                    currentDeck.removeVillageResources();
                    playerResources.put(currentPlayerIndex, currentDeck);
                    try {
                        VillageCell villageCell = map.getVillageCellById(villageId);
                        villageCell.setOwner(currentPlayerIndex);
                        villageCell.setVillagePath(villagePaths.get(currentPlayerIndex));
                        broadcastVillage(villageCell);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    pointCounter.addPoint(currentPlayerIndex);
                    sendPoints();
                    sendPlayerDeck();
                }
            }
        }

        private void RoadPressFunc(String input) {
            if(input.contains("Road") && isDiceThrown) {
                System.out.println(input + " by user " + currentPlayerIndex);
                int roadId = Integer.parseInt(input.split(":")[1]);
                Deck currentDeck = playerResources.get(currentPlayerIndex);
                if(Checkers.areRoadRequirementsMet(map.getRoadCellById(roadId), currentDeck, currentPlayerIndex)) {
                    currentDeck.removeRoadResources();
                    playerResources.put(currentPlayerIndex, currentDeck);
                    try {
                        RoadCell roadCell = map.getRoadCellById(roadId);
                        roadCell.setOwner(currentPlayerIndex);
                        roadCell.setRoadTexture(roadPaths.get(currentPlayerIndex));
                        broadcastRoad(roadCell);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    sendPlayerDeck();
                }
            }
        }

        private void sendPlayerDeck() {
            try {
                Deck deckToSend = playerResources.get(currentPlayerIndex);
                System.out.println(deckToSend.getResources());
                outputStream.writeObject(deckToSend.getResources());
                outputStream.reset();
            } catch (IOException e) {
                System.out.println("Could not send player deck");
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
                Deck tempDeck = playerResources.get(clients.indexOf(this));
                Deck acquiredResources = Functionality.getResources(diceThrow, map, tempDeck.getResources(), clients.indexOf(this));
                System.out.println(acquiredResources.getResources());
                playerResources.put(clients.indexOf(this), acquiredResources);
                outputStream.writeObject(acquiredResources.getResources());
                outputStream.reset();
            } catch (IOException e) {
                System.out.println("Could not send deck");
            }
        }
    }
}
