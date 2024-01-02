package com.game.catan.Functionality;

import com.game.catan.Map.Cell.ResourceCell;
import com.game.catan.Map.Cell.ResourceType;
import com.game.catan.Map.Cell.VillageCell;
import com.game.catan.Map.Map;

import java.util.HashMap;

public class Functionality {
    public int diceThrow() {
        return (int) (Math.random() * 10 + 2);
    }

    public int getNumberOfPlayerVillages(ResourceCell cell, int playerId) {
        int numberOfPlayerVillages = 0;
        for(VillageCell villageCell : cell.getVillages()) {
            if(villageCell.getOwner().getId() == playerId) {
                numberOfPlayerVillages++;
            }
        }
        return numberOfPlayerVillages;
    }

    public Deck getResources(int diceThrow, Map map, HashMap<ResourceType, Integer> resources, int playerId) {
        Deck deck = new Deck();
        for (ResourceCell cell : map.getResourceCells(map.getCenterCell())) {
            if (cell.getDiceThrow() == diceThrow) {
                //if (cell.getNumberOfPlayerVillages(playerId) > 0) {
                    deck.addResource(ResourceType.valueOf(cell.getResource()));
                    resources.put(ResourceType.valueOf(cell.getResource()), resources.get(ResourceType.valueOf(cell.getResource())));
                //}
            }
        }
        return deck;
    }
}
