package com.game.catan.Functionality;

import com.game.catan.Map.Cell.ResourceType;

import java.io.Serializable;
import java.util.HashMap;

public class Offer implements Serializable {
    private boolean isBankOffer = false;
    private Deck givenOffer;
    private Deck wantedOffer;
    private int playerId;
    private HashMap<Integer, Integer> players;

    public Offer(Deck offer, int playerId) {
        this.givenOffer = offer;
        this.wantedOffer = new Deck(true);
        this.playerId = playerId;
    }

    public Offer(int playerId) {
        this.givenOffer = new Deck(true);
        this.wantedOffer = new Deck(true);
        this.playerId = playerId;
        players = new HashMap<>();
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public synchronized void addResourceToWantedOffer(ResourceType resource) {
        wantedOffer.addResource(resource);
    }

    public synchronized void removeResourceFromWantedOffer(ResourceType resource) {
        wantedOffer.removeResource(resource);
    }

    public synchronized void addResourceToGivenOffer(ResourceType resource) {
        givenOffer.addResource(resource);
    }

    public synchronized void removeResourceFromGivenOffer(ResourceType resource) {
        givenOffer.removeResource(resource);
    }

    public Deck getWantedOffer() {
        return wantedOffer;
    }

    public Deck getGivenOffer() {
        return givenOffer;
    }

    public void addPlayer(int playerId) {
        players.put(playerId, 0);
    }
    public void addAcceptance(int playerId) {
        players.put(playerId, 1);
    }
    public void addRejection(int playerId) {
        players.put(playerId, 2);
    }

    public int getPlayerId() {
        return playerId;
    }

    public HashMap<Integer, Integer> getPlayers() {
        return players;
    }

    public Integer getWantedOfferResourceAmount(ResourceType resourceType) {
        return wantedOffer.getResourceAmount(resourceType);
    }

    public Integer getGivenOfferResourceAmount(ResourceType resourceType) {
        return givenOffer.getResourceAmount(resourceType);
    }

    public Integer getPlayerAnswer(int playerId) {
        return players.get(playerId);
    }

    public void setOffer(Deck givenOffer, Deck wantedOffer) {
        this.givenOffer = givenOffer;
        this.wantedOffer = wantedOffer;
    }

    public void setPlayers(HashMap<Integer, Integer> players) {
        this.players = players;
    }

    public boolean isBankOffer() {
        for(ResourceType resourceType : givenOffer.getResources().keySet()) {
            if(givenOffer.getResourceAmount(resourceType) == 4 && isBankOffer) {
                return true;
            }
        }
        return false;
    }
}
