package com.game.catan.Functionality;

import com.game.catan.Map.Cell.ResourceType;

import java.util.HashMap;

public class Offer {
    private Deck offer;
    private final int playerId;
    private HashMap<Integer, Boolean> players;

    public Offer(Deck offer, int playerId) {
        this.offer = new Deck(true);
        this.offer = offer;
        this.playerId = playerId;
    }

    public Offer(int playerId) {
        this.offer = new Deck(true);
        this.playerId = playerId;
        players = new HashMap<>();

    }

    public void addResource(ResourceType resource) {
        offer.addResource(resource);
    }

    public void getOffer(Deck deck) {
        deck.addDeck(offer);
    }

    public void emptyOffer() {
        offer = new Deck(true);
    }

    public void addRejection(int playerId) {
        players.put(playerId, false);
    }

    public void addAcceptance(int playerId) {
        players.put(playerId, true);
    }

    public int getPlayerId() {
        return playerId;
    }

    public HashMap<Integer, Boolean> getPlayers() {
        return players;
    }

    public int getResourceAmount(ResourceType resourceType) {
        return offer.getResourceAmount(resourceType);
    }
    public boolean isBankOffer() {
        for(ResourceType resourceType : offer.getResources().keySet()) {
            if(offer.getResourceAmount(resourceType) == 4) {
                return true;
            }
        }
        return false;
    }
}
