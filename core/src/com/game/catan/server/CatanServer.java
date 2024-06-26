package com.game.catan.server;

import java.net.InetAddress;
import java.util.*;
import java.net.Socket;
import java.util.HashMap;
import java.io.IOException;
import java.net.ServerSocket;

import com.game.catan.Functionality.*;
import com.game.catan.Map.Cell.ResourceType;
import com.game.catan.Map.Cell.RoadCell;
import com.game.catan.Map.Cell.VillageCell;
import com.game.catan.Map.Map;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class CatanServer {
    private static final int ROBBER_THROW = 7;
    private static final int REQUIRED_POINTS = 5;
    private static final int MAX_PLAYERS = 4;
    private static final int YOUR_TURN = 100;
    private static final int NOT_YOUR_TURN = 200;
    private static final int SOCKET_PORT = 12345;
    private ServerSocket serverSocket;
    private final List<ClientHandler> clients;
    private int currentPlayerIndex = 0;
    private Map map;
    private final HashMap<Integer, Deck> playerResources;
    private final ArrayList<String> villagePaths;
    private final ArrayList<String> roadPaths;
    private boolean isWorking = true;
    private boolean isInitialVillagePhase = true;
    private boolean isSecondVillagePhase = false;
    private int diceThrow;
    private boolean isDiceThrown = false;
    private boolean isRobberMoved = true;
    private final HashMap<Integer, VillagePair> initialVillages;
    private final PointCounter pointCounter;
    private Offer currentOffer;

    public CatanServer(Map map) {
        this.map = map;
        clients = new ArrayList<>();
        roadPaths = new ArrayList<>();
        villagePaths = new ArrayList<>();
        pointCounter = new PointCounter();
        playerResources = new HashMap<>();
        initialVillages = new HashMap<>();
        villagePaths.add("Villages/yellowVillage.png");
        villagePaths.add("Villages/blueVillage.png");
        villagePaths.add("Villages/greenVillage.png");
        villagePaths.add("Villages/redVillage.png");
        roadPaths.add("Roads/yellowNormalRoad.png");
        roadPaths.add("Roads/blueNormalRoad.png");
        roadPaths.add("Roads/greenNormalRoad.png");
        roadPaths.add("Roads/redNormalRoad.png");
        try {
            serverSocket = new ServerSocket(SOCKET_PORT);
            InetAddress serverAddress = InetAddress.getLocalHost();
            System.out.println("Server address: " + serverAddress.getHostAddress());
            System.out.println("Server started. Waiting for clients...");

            while (isWorking) {
                if(clients.size() < MAX_PLAYERS) {
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
        playerResources.put(clients.size(), new Deck(true));
        pointCounter.addPlayer(clients.size());
        initialVillages.put(clients.size(), new VillagePair());
        ClientHandler clientHandler = new ClientHandler(clientSocket);
        clients.add(clientHandler);
        broadcastPlayersAmount();
        new Thread(clientHandler).start();
    }

    public void setMap(Map map) {
        this.map = map;
    }

    private void broadcastTurnNotification() {
        sendTurnNotifications();

    }

    private void sendTurnNotifications() {
        for(ClientHandler client : clients) {
            client.sendTurnNotification(currentPlayerIndex);
        }
    }

    private void broadcastOffer() {
        for(ClientHandler client : clients) {
            client.sendOffer();
        }
    }

    private void broadcastMap() {
        for (ClientHandler client : clients) {
            client.sendMap(map);
        }
    }

    private void broadcastDiceThrow() {
        for (ClientHandler client : clients) {
            if(diceThrow == ROBBER_THROW) {
                for(int playerId = 0; playerId < clients.size(); playerId++) {
                    playerResources.get(playerId).robberMove();
                }
                broadcastRobberDeck();
            }
            client.sendDiceThrow(diceThrow);
        }
    }

    private void broadcastDeck() {
        for (ClientHandler client : clients) {
            client.sendDeck(diceThrow);
        }
    }

    private void broadcastTradeFinished() {
        for (ClientHandler client : clients) {
            client.sendTrade();
        }
    }

    private void broadcastRobberDeck() {
        for (ClientHandler client : clients) {
            client.sendPlayerDeck();
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

    private void broadcastOfferConfirmation(int id) {
        for (ClientHandler client : clients) {
            client.sendOfferConfirmation(id);
        }
    }

    private void broadcastOfferRejection(int id) {
        if(checkAllRejected()) {
            for (ClientHandler client : clients) {
                client.sendAllRejected();
            }
        }
        else {
            for (ClientHandler client : clients) {
                client.sendOfferRejection(id);
            }
        }
    }

    private boolean checkAllRejected() {
        HashMap<Integer, Integer> players = currentOffer.getPlayers();
        for(Integer playerId : players.keySet()) {
            if(players.get(playerId) != 2) {
                return false;
            }
        }
        return true;
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
                    if(isInitialVillagePhase || isSecondVillagePhase) {
                        initialVillagePhaseChecker(input);
                    }
                    else {
                        endTurnAndDiceThrowChecker(input);
                    }
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
                manageOfferConfirmation((String) input);
                manageOfferRejection((String) input);
                manageTrade((String) input);
            }
            else if(input instanceof Offer) {
                OfferPressFunc(input);
            }
        }

        private void initialVillagePhaseChecker(Object input) {
            if(input instanceof String) {
                initialVillagePressFunc((String) input);
                secondInitialVillagePressFunc((String) input);
                initialRoadPressFunc((String) input);
                initialEndTurnFunc((String) input);
            }
        }

        private void endTurnFunc(String input) {
            if(input.equals("End Turn") && isDiceThrown &&
                    currentPlayerIndex == clients.indexOf(this) && isRobberMoved){
                currentPlayerIndex = (currentPlayerIndex + 1) % clients.size();
                broadcastTurnNotification();
                broadcastMap();
                isDiceThrown = false;
                isRobberMoved = true;
            }
        }

        private void diceThrowFunc(String input) {
            if(input.equals("Dice Throw") && !isDiceThrown &&
                    currentPlayerIndex == clients.indexOf(this)) {
                diceThrow = Functionality.diceThrow();
                System.out.println(diceThrow);
                broadcastDiceThrow();
                broadcastDeck();
                isDiceThrown = true;
                if(diceThrow == 7)
                    isRobberMoved = false;
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

        private void sendOfferConfirmation(int id) {
            try {
                outputStream.writeObject("Offer accepted:" + id);
                outputStream.reset();
            }
            catch (IOException e) {
                System.out.println("Could not send offer confirmation");
            }
        }

        private void sendOfferRejection(int id) {
            try {
                    outputStream.writeObject("Offer rejected:" + id);
                    outputStream.reset();
            }
            catch (IOException e) {
                System.out.println("Could not send offer rejection");
            }
        }
        
        private void manageTrade(String input) {
            if(input.contains("Accepted trade") && currentPlayerIndex == clients.indexOf(this) && currentOffer != null) {
                int id = Integer.parseInt(input.split(":")[1]);
                if(currentOffer.getPlayers().get(id) == 1) {
                    swapCards(currentOffer.getPlayerId(), id);
                }
                broadcastDeck();
                broadcastTradeFinished();
            }

        }

        private void sendTrade() {
            try {
                outputStream.writeObject("Trade");
                outputStream.reset();
            }
            catch (IOException e) {
                System.out.println("Could not send trade");
            }
        }
        
        private void swapCards(int id1, int id2) {
            Deck deckSender = playerResources.get(id1);
            Deck deckReciever = playerResources.get(id2);
            System.out.println("Swapping cards");
            for(ResourceType resource : ResourceType.values()) {
                if(resource != ResourceType.EMPTY) {
                    int temp = currentOffer.getGivenOfferResourceAmount(resource);
                    deckSender.removeResources(resource, temp);
                    deckReciever.addResources(resource, temp);
                    int temp2 = currentOffer.getWantedOfferResourceAmount(resource);
                    deckReciever.removeResources(resource, temp2);
                    deckSender.addResources(resource, temp2);
                }
            }
            playerResources.put(id1, deckSender);
            playerResources.put(id2, deckReciever);
        }

        private void sendAllRejected() {
            try {
                outputStream.writeObject("All rejected");
                outputStream.reset();
            }
            catch (IOException e) {
                System.out.println("Could not send all rejected");
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
            if(input.contains("Resource") && diceThrow == 7 && isDiceThrown && !isRobberMoved && currentPlayerIndex == clients.indexOf(this)) {
                isRobberMoved = true;
                int resourceId = Integer.parseInt(input.split(":")[1]);
                map.moveRobber(resourceId);
                isRobberMoved = true;
                broadcastMap();
            }
        }

        private void villagePressFunc(String input) {
            if(input.contains("Village") && isDiceThrown && isRobberMoved && !isInitialVillagePhase &&
                    !isSecondVillagePhase && currentPlayerIndex == clients.indexOf(this)){
                int villageId = Integer.parseInt(input.split(":")[1]);
                VillageCell villageCell = map.getVillageCellById(villageId);
                Deck currentDeck = playerResources.get(currentPlayerIndex);
                if(Checkers.areVillageRequirementsMet(villageCell, currentDeck, currentPlayerIndex)) {
                    try {
                        villageCell.setOwner(currentPlayerIndex);
                        villageCell.setVillagePath(villagePaths.get(currentPlayerIndex));
                        broadcastMap();
                        currentDeck.removeVillageResources();
                        playerResources.put(currentPlayerIndex, currentDeck);
                        sendPlayerDeck();
                        pointCounter.addPoint(currentPlayerIndex);
                        sendPoints();
                        endGame();

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        private void endGame() {
            if(pointCounter.getPoints(currentPlayerIndex) >= REQUIRED_POINTS) {
                for(ClientHandler client : clients) {
                    if(clients.indexOf(client) == currentPlayerIndex) {
                        try {
                            client.outputStream.writeObject("Win");
                            client.outputStream.reset();
                        } catch (IOException e) {
                            System.out.println("Could not send win");
                        }
                    }
                    else {
                        try {
                            client.outputStream.writeObject("Loss");
                            client.outputStream.reset();
                        } catch (IOException e) {
                            System.out.println("Could not send loss");
                        }
                    }
                }
                System.out.println("Game ended");
            }
        }

        private void initialVillagePressFunc(String input) {

            if(input.contains("Village") && initialVillages.get(currentPlayerIndex).hasNone()
                    && isInitialVillagePhase && currentPlayerIndex == clients.indexOf(this)) {

                int villageId = Integer.parseInt(input.split(":")[1]);
                VillageCell villageCell = map.getVillageCellById(villageId);
                if(Checkers.areInitialVillageRequirementsMet(villageCell)) {
                    System.out.println("Player " + currentPlayerIndex + " clicked village " + villageId + " in initial phase");
                    try {
                        villageCell.setOwner(currentPlayerIndex);
                        villageCell.setVillagePath(villagePaths.get(currentPlayerIndex));
                        if(initialVillages.get(currentPlayerIndex) == null) {
                            initialVillages.put(currentPlayerIndex, new VillagePair(villageCell));
                        }
                        else {
                            initialVillages.get(currentPlayerIndex).setFirst(villageCell);
                        }
                        System.out.println("Initial village set");
                        System.out.println(initialVillages.get(currentPlayerIndex).getFirst().getId());
                        broadcastMap();
                        pointCounter.addPoint(currentPlayerIndex);
                        sendPoints();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        private void OfferPressFunc(Object input) {
            if(input instanceof Offer && currentPlayerIndex == clients.indexOf(this)) {
                System.out.println("Offer received");
                currentOffer = (Offer) input;
                broadcastOffer();
            }
        }

        private void sendOffer() {
            try {
                outputStream.writeObject(currentOffer);
                outputStream.reset();
            } catch (IOException e) {
                System.out.println("Could not send offer");
            }
        }

        private void initialEndTurnFunc(String input) {
            if(input.equals("End Turn") && currentPlayerIndex == clients.indexOf(this)) {
                broadcastMap();
                if(isInitialVillagePhase && initialVillages.get(currentPlayerIndex).hasOne() &&
                        Checkers.isInitialRoadBuilt(initialVillages.get(currentPlayerIndex).getFirst())) {
                    if(currentPlayerIndex == clients.size() - 1) {
                        isInitialVillagePhase = false;
                        isSecondVillagePhase = true;
                    }
                    else {
                        currentPlayerIndex++;
                    }
                    broadcastTurnNotification();
                }
                else if(isSecondVillagePhase && initialVillages.get(currentPlayerIndex).hasBoth() &&
                        Checkers.isInitialRoadBuilt(initialVillages.get(currentPlayerIndex).getSecond())) {
                    if(currentPlayerIndex == 0) {
                        isSecondVillagePhase = false;
                    }
                    else {
                        currentPlayerIndex--;
                    }
                    broadcastTurnNotification();
                }
            }
        }

        private void secondInitialVillagePressFunc(String input) {

            if(input.contains("Village") && initialVillages.get(currentPlayerIndex).hasOne()
                    && isSecondVillagePhase && currentPlayerIndex == clients.indexOf(this)) {
                int villageId = Integer.parseInt(input.split(":")[1]);
                VillageCell villageCell = map.getVillageCellById(villageId);
                if(Checkers.areInitialVillageRequirementsMet(villageCell)) {
                    try {
                        villageCell.setOwner(currentPlayerIndex);
                        villageCell.setVillagePath(villagePaths.get(currentPlayerIndex));
                        initialVillages.get(currentPlayerIndex).setSecond(villageCell);
                        broadcastMap();
                        playerResources.put(currentPlayerIndex, Functionality.getResourcesFromInitialVillages(villageCell, playerResources.get(currentPlayerIndex).getResources()));
                        sendPlayerDeck();
                        pointCounter.addPoint(currentPlayerIndex);
                        sendPoints();
                        System.out.println("Initial villages set");
                        System.out.println(initialVillages.get(currentPlayerIndex).getSecond().getId());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        }

        private void initialRoadPressFunc(String input) {
            if(input.contains("Road") && currentPlayerIndex == clients.indexOf(this) &&
                    ((initialVillages.get(currentPlayerIndex).hasOne()) ||
                            initialVillages.get(currentPlayerIndex).hasBoth())) {

                int roadId = Integer.parseInt(input.split(":")[1]);
                if(Checkers.areInitialRoadRequirementsMet(map.getRoadCellById(roadId), currentPlayerIndex)) {
                    try {
                        RoadCell roadCell = map.getRoadCellById(roadId);
                        roadCell.setOwner(currentPlayerIndex);
                        roadCell.setRoadTexture(roadPaths.get(currentPlayerIndex));
                        broadcastMap();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        private void RoadPressFunc(String input) {
            if(input.contains("Road") && isDiceThrown && isRobberMoved && !isInitialVillagePhase &&
                    !isSecondVillagePhase && currentPlayerIndex == clients.indexOf(this)) {

                int roadId = Integer.parseInt(input.split(":")[1]);
                Deck currentDeck = playerResources.get(currentPlayerIndex);
                if(Checkers.areRoadRequirementsMet(map.getRoadCellById(roadId), currentDeck, currentPlayerIndex)) {
                    try {
                        RoadCell roadCell = map.getRoadCellById(roadId);
                        roadCell.setOwner(currentPlayerIndex);
                        roadCell.setRoadTexture(roadPaths.get(currentPlayerIndex));
                        broadcastMap();
                        currentDeck.removeRoadResources();
                        playerResources.put(currentPlayerIndex, currentDeck);
                        sendPlayerDeck();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
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
                    outputStream.writeObject(YOUR_TURN);
                    outputStream.reset();
                }
                else {
                    outputStream.writeObject(NOT_YOUR_TURN);
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

        private void manageOfferConfirmation(String message) {
            if(message.contains("Offer confirmed")) {
                int id = Integer.parseInt(message.split(":")[1]);
                if(canAfford(id)) {
                    currentOffer.addAcceptance(id);
                    broadcastOfferConfirmation(id);
                    System.out.println("Offer confirmation received by client " + id);
                }
            }
        }

        private void manageOfferRejection(String message) {
            if(message.contains("Offer rejected")) {
                int id = Integer.parseInt(message.split(":")[1]);
                currentOffer.addRejection(id);
                broadcastOfferRejection(id);
                System.out.println("Offer rejection received by client " + id);
            }
        }

        private boolean canAfford(int id) {
            for(ResourceType resource : ResourceType.values()) {
                if(resource != ResourceType.EMPTY && playerResources.get(id).getResources().get(resource) < currentOffer.getWantedOfferResourceAmount(resource)){
                    return false;
                }
            }
            return true;
        }

        private void sendDeck(int diceThrow) {
            try {
                Deck tempDeck = playerResources.get(clients.indexOf(this));
                Deck acquiredResources = Functionality.getResources(diceThrow, map, tempDeck.getResources(), clients.indexOf(this), true);
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
