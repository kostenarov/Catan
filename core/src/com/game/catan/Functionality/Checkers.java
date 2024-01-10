package com.game.catan.Functionality;

import com.game.catan.Map.Cell.Cell;
import com.game.catan.Map.Cell.ResourceType;
import com.game.catan.Map.Cell.RoadCell;
import com.game.catan.Map.Cell.VillageCell;

import java.util.HashSet;

public class Checkers {

    private boolean isRoadBuildable(Deck deck) {
        return deck.getResourceAmount(ResourceType.WOOD) >= 1 &&
                deck.getResourceAmount(ResourceType.BRICK) >= 1;
    }

    public static boolean areRoadRequirementsMet(RoadCell cell, Deck deck, int currentPlayerIndex) {
        Checkers checkers = new Checkers();
        return checkers.isRoadBuildable(deck) && cell.getOwner() == 5 && checkRoadRequirements(cell, currentPlayerIndex);
    }

    private static boolean checkRoadRequirements(RoadCell cell, int currentPlayerIndex) {
        for(Cell iteratorCell : cell.getNeighbours()) {
            if(iteratorCell instanceof VillageCell) {
                if(((VillageCell) iteratorCell).getOwner() == currentPlayerIndex) {
                    return true;
                }
            }
        }
        return false;
    }
}
