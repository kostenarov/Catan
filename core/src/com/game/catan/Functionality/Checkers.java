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


    private static boolean checkInitialRoadRequirements(RoadCell cell, int currentPlayerIndex) {
        VillageCell village1 = (VillageCell) cell.getVillages().get(0);
        VillageCell village2 = (VillageCell) cell.getVillages().get(1);
        if(cell.getOwner() != 5)
            return false;
        if(village1.getOwner() != currentPlayerIndex && village2.getOwner() != currentPlayerIndex)
            return false;
        for(Cell iteratorCell : cell.getNeighbours()) {
            if(iteratorCell instanceof VillageCell && ((VillageCell) iteratorCell).getOwner() == currentPlayerIndex) {
                for(Cell iteratorCell2 : iteratorCell.getNeighbours()) {
                    if(iteratorCell2 instanceof RoadCell) {
                        if(((RoadCell) iteratorCell2).getOwner() != 5) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
    public static boolean areInitialRoadRequirementsMet(RoadCell cell, int currentPlayerIndex) {
        return checkInitialRoadRequirements(cell, currentPlayerIndex) && !cell.isBuilt();
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
        System.out.println("Owner: " + cell.getOwner() + " CurrentPlayerIndex: " + currentPlayerIndex);
        return cell.getOwner() == 5 && counter == roadHelper && ownerHelper;
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

    public static boolean isInitialRoadBuilt(VillageCell cell) {
        for(Cell iteratorCell : cell.getNeighbours()) {
            if(iteratorCell instanceof RoadCell) {
                if(((RoadCell) iteratorCell).getOwner() == cell.getOwner()) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean areVillageRequirementsMet(VillageCell cell, Deck deck, int currentPlayerIndex) {
        System.out.println("Village requirements met: " + isVillageBuildable(deck) + " " + cell.getOwner() + " " + checkVillageRequirements(cell, currentPlayerIndex));
        return isVillageBuildable(deck) && checkVillageRequirements(cell, currentPlayerIndex);
    }

    public static boolean areInitialVillageRequirementsMet(VillageCell cell) {
        return checkInitialVillageRequirements(cell);
    }

    private static boolean isVillageBuildable(Deck deck) {
        return deck.getResourceAmount(ResourceType.WOOD) >= 1 &&
                deck.getResourceAmount(ResourceType.BRICK) >= 1 &&
                deck.getResourceAmount(ResourceType.SHEEP) >= 1 &&
                deck.getResourceAmount(ResourceType.WHEAT) >= 1;
    }
}
