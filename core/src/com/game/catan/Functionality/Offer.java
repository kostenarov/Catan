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
}
