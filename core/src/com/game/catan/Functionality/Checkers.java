package com.game.catan.Functionality;

import com.game.catan.Map.Cell.Cell;
import com.game.catan.Map.Cell.ResourceType;
import com.game.catan.Map.Cell.RoadCell;
import com.game.catan.Map.Cell.VillageCell;

public class Checkers {

    private boolean isRoadBuildable(Deck deck) {
        return deck.getResourceAmount(ResourceType.WOOD) >= 1 &&
                deck.getResourceAmount(ResourceType.BRICK) >= 1;
    }

    public static boolean areRoadRequirementsMet(RoadCell cell, Deck deck, int currentPlayerIndex) {
        Checkers checkers = new Checkers();
        return checkers.isRoadBuildable(deck) && !cell.isBuilt() && checkRoadRequirements(cell, currentPlayerIndex);
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

    private static boolean checkVillageRequirements(VillageCell cell, int currentPlayerIndex) {
        for(Cell iteratorCell : cell.getNeighbours()) {
            if(iteratorCell instanceof RoadCell) {
                if(((RoadCell) iteratorCell).getOwner() == currentPlayerIndex && ((RoadCell) iteratorCell).getOtherVillage(cell).getOwner() == currentPlayerIndex) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean areVillageRequirementsMet(VillageCell cell, Deck deck, int currentPlayerIndex) {
        //return isVillageBuildable(deck) && cell.getOwner() == 5 && checkVillageRequirements(cell, currentPlayerIndex);
        return isVillageBuildable(deck) && cell.getOwner() == 5;
    }

    private static boolean isVillageBuildable(Deck deck) {
        return deck.getResourceAmount(ResourceType.WOOD) >= 1 &&
                deck.getResourceAmount(ResourceType.STONE) >= 1 &&
                deck.getResourceAmount(ResourceType.SHEEP) >= 1 &&
                deck.getResourceAmount(ResourceType.WHEAT) >= 1;
    }
}
