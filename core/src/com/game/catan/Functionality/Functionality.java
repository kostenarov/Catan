package com.game.catan.Functionality;

import com.game.catan.Map.Cell.Cell;
import com.game.catan.Map.Cell.ResourceCell;
import com.game.catan.Map.Cell.ResourceType;
import com.game.catan.Map.Cell.VillageCell;
import com.game.catan.Map.Map;

import java.util.HashMap;
import java.util.HashSet;

public class Functionality {
    public static int diceThrow() {
        return (int) (Math.random() * 10 + 2);
    }

    public static int getNumberOfPlayerVillages(ResourceCell cell, int playerId) {
        int numberOfPlayerVillages = 0;
        for(VillageCell villageCell : cell.getVillages()) {
            if(villageCell.getOwner() == playerId) {
                numberOfPlayerVillages++;
            }
        }
        return numberOfPlayerVillages;
    }

    public static Deck getResources(int diceThrow, Map map, HashMap<ResourceType, Integer> resources, int playerId) {
        for (ResourceCell cell : map.getResourceNeighbours(map.getCenterCell())) {
            if (cell.getDiceThrow() == diceThrow && !cell.hasRobber() && cell.getResource() != ResourceType.EMPTY) {
                if (getNumberOfPlayerVillages(cell, playerId) > 0) {
                    resources.put(cell.getResource(), resources.get(cell.getResource()) + 1);
                }
            }
        }
        Deck deck = new Deck(true);
        deck.setDeck(resources);
        return deck;
    }

    public static Deck getResources(int diceThrow, Map map, HashMap<ResourceType, Integer> resources, int playerId, boolean Helper) {
        HashSet<VillageCell> villages = new HashSet<>();
        villages = map.getVillagesByPlayerId(playerId);

        for (VillageCell villageCell : villages) {
            for (ResourceCell cell : villageCell.getResourceNeighbours()) {
                if (cell.getDiceThrow() == diceThrow && !cell.hasRobber() && cell.getResource() != ResourceType.EMPTY) {
                    resources.put(cell.getResource(), resources.get(cell.getResource()) + 1);
                }
            }
        }

        Deck deck = new Deck(true);
        deck.setDeck(resources);
        return deck;
    }

    public static Deck getResourcesFromInitialVillages(VillageCell villageCell, HashMap<ResourceType, Integer> resources) {
        for (ResourceCell cell : villageCell.getResourceNeighbours())
            if(cell.getResource() != ResourceType.EMPTY)
                resources.put(cell.getResource(), resources.get(cell.getResource()) + 1);
        Deck deck = new Deck(true);
        deck.setDeck(resources);
        return deck;
    }
}
