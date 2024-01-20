package com.game.catan.Functionality;

import com.game.catan.Map.Cell.ResourceType;

public class Offer {
    Deck offer;

    public Offer(Deck offer) {
        this.offer = new Deck(true);
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
}
