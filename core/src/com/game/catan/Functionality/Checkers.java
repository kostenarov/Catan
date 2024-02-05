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
                for(Cell iteratorCell2 : iteratorCell.getNeighbours()) {
                    if(iteratorCell2 instanceof RoadCell) {
                        if(((RoadCell) iteratorCell2).getOwner() == currentPlayerIndex) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }


    public static boolean areInitialRoadRequirementsMet(RoadCell cell, int currentPlayerIndex) {
        return checkRoadRequirements(cell, currentPlayerIndex) && !cell.isBuilt();
    }

    private static boolean checkVillageRequirements(VillageCell cell, int currentPlayerIndex) {
        int counter = 0;
        int roadHelper = 0;
        boolean ownerHelper = false;
        for(Cell iteratorCell : cell.getNeighbours()) {
            if(iteratorCell instanceof RoadCell) {
                roadHelper++;
                if(((RoadCell) iteratorCell).getOtherVillage(cell).getOwner() == 5)
                    counter++;
                if(((RoadCell) iteratorCell).getOwner() == currentPlayerIndex) {
                    ownerHelper = true;
                }
            }
        }
        System.out.println("Counter: " + counter + " RoadHelper: " + roadHelper + " OwnerHelper: " + ownerHelper);
        return cell.getId() == 5;
    }

    private static boolean checkInitialVillageRequirements(VillageCell cell) {
        if(cell.getOwner() != 5) {
            System.out.println("Owner is not 5");
            return false;
        }
        int counter = 0;
        int roadHelper = 0;
        for(Cell iteratorCell : cell.getNeighbours()) {
            if(iteratorCell instanceof RoadCell) {
                roadHelper++;
                if(((RoadCell) iteratorCell).getOtherVillage(cell).getOwner() == 5)
                    counter++;
            }
        }
        return counter == roadHelper;
    }

    public static boolean areVillageRequirementsMet(VillageCell cell, Deck deck, int currentPlayerIndex) {
        System.out.println("Village requirements met: " + isVillageBuildable(deck) + " " + cell.getOwner() + " " + checkVillageRequirements(cell, currentPlayerIndex));
        return isVillageBuildable(deck) && cell.getOwner() == 5 && checkVillageRequirements(cell, currentPlayerIndex);
        //return isVillageBuildable(deck) && cell.getOwner() == 5;
    }

    public static boolean areInitialVillageRequirementsMet(VillageCell cell) {
        return checkInitialVillageRequirements(cell);
    }

    private static boolean isVillageBuildable(Deck deck) {
        return deck.getResourceAmount(ResourceType.WOOD) >= 1 &&
                deck.getResourceAmount(ResourceType.STONE) >= 1 &&
                deck.getResourceAmount(ResourceType.SHEEP) >= 1 &&
                deck.getResourceAmount(ResourceType.WHEAT) >= 1;
    }
}
