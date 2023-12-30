package com.game.catan.Functionality;

import com.game.catan.Map.Cell.ResourceCell;
import com.game.catan.Map.Cell.ResourceType;
import com.game.catan.Map.Map;
import com.game.catan.player.CatanPlayer;

import java.util.HashMap;

public class Functionality {
    public int diceThrow() {
        return (int) (Math.random() * 10 + 2);
    }

    public Deck getResources(int diceThrow, Map map, HashMap<ResourceType, Integer> resources, CatanPlayer player) {
        Deck deck = player.getDeck();
        for (ResourceCell cell : map.getResourceCells(map.getCenterCell())) {
            if (cell.getDiceThrow() == diceThrow) {
                if (cell.hasRobber()) {
                    continue;
                }
                ResourceType resource = ResourceType.valueOf(cell.getResource());
                deck.addResource(resource);
            }
        }
        return deck;
    }
}
