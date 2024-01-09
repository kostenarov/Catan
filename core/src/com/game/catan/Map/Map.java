package com.game.catan.Map;

import com.game.catan.Map.Cell.*;
import com.game.catan.player.CatanPlayer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Map implements Serializable {
    private ResourceCell robberCell;
    private final ResourceCell centerCell;
    public Map() {
        centerCell = new ResourceCell(960, 540, ResourceType.BRICK);
        centerCell.addNeighbour(new ResourceCell(890, 435, ResourceType.WOOD));
        ResourceCell tempCell = new ResourceCell(820, 540, ResourceType.WHEAT);
        ResourceCell emptyCell = new ResourceCell(760, 840, ResourceType.EMPTY);
        centerCell.addNeighbour(tempCell);
        emptyCell.setRobber(true);
        centerCell.addNeighbour(emptyCell);
        VillageCell tempVillage = new VillageCell(915, 530);
        tempVillage.addNeighbour(centerCell);
        tempVillage.addNeighbour(tempCell);
        tempCell.addVillageNeighbour(tempVillage);
        centerCell.addVillageNeighbour(tempVillage);
        System.out.println(centerCell.getDiceThrow());
        findRobber();
    }

    private HashSet<VillageCell> getVillageNeighbours(VillageCell villageCell) {
        HashSet<VillageCell> neighbours = new HashSet<>();
        for(Cell cell : villageCell.getNeighbours()) {
            if(cell instanceof RoadCell) {
                ArrayList<VillageCell> tempNeighbours = ((RoadCell) cell).getVillages();
                neighbours.addAll(tempNeighbours);
                for(VillageCell tempVillage : tempNeighbours) {
                    neighbours.addAll(getVillageNeighbours(tempVillage));
                }
            }
        }
        return neighbours;
    }

    public HashSet<Cell> getMap() {
        HashSet<Cell> map = new HashSet<>();
        map.add(centerCell);
        for (Cell cell : map) {
            map.addAll(getCellNeighbours(cell));
        }
        return map;

    }

    private HashSet<Cell> getCellNeighbours(Cell cell) {
        return new HashSet<>(cell.getNeighbours());
    }

    private void findRobber() {
        for(ResourceCell resourceCell : getResourceCells(centerCell)) {
            if(resourceCell.hasRobber()) {
                robberCell = resourceCell;
                break;
            }
        }
    }

    private CatanPlayer findLongestRoad() {
        HashSet<RoadCell> takenRoads = new HashSet<>();
        for(Cell cell : getMap()) {
            if(cell instanceof RoadCell) {
                if(((RoadCell) cell).isBuilt()) {
                    takenRoads.add((RoadCell) cell);
                }
            }
        }
        if(takenRoads.size() < 5) {
            return null;
        }
        HashSet<RoadCell> longestRoad = new HashSet<>();
        for(RoadCell roadCell : takenRoads) {
            HashSet<RoadCell> tempRoad = connectRoads(roadCell);
            if(tempRoad.size() > longestRoad.size()) {
                longestRoad = tempRoad;
            }
        }
        return longestRoad.iterator().next().getOwner();
    }

    private HashSet<RoadCell> connectRoads(RoadCell roadCell) {
        HashSet<RoadCell> roadCells = new HashSet<>();
        for(Cell cell : roadCell.getNeighbours()) {
            if(cell instanceof VillageCell) {
                List<Cell> tempNeighbours = cell.getNeighbours();
                for(Cell tempCell : tempNeighbours) {
                    if(tempCell instanceof RoadCell) {
                        if(((RoadCell) tempCell).isBuilt() && ((RoadCell) tempCell).getOwner() == roadCell.getOwner()) {
                            roadCells.add((RoadCell) tempCell);
                            roadCells.addAll(connectRoads((RoadCell) tempCell));
                        }
                    }
                }
            }
        }
        return roadCells;
    }

    public HashSet<ResourceCell> getResourceCells(ResourceCell resourceCell) {
        HashSet<ResourceCell> resourceCells = new HashSet<>();
        for(Cell cell : resourceCell.getNeighbours()) {
            if(cell instanceof ResourceCell) {
                resourceCells.add((ResourceCell) cell);
                resourceCells.addAll(getResourceCells((ResourceCell) cell));
            }
        }
        return resourceCells;
    }

    public Cell getCellById(int id) {
        for(Cell cell : getMap()) {
            if(cell.getId() == id) {
                return cell;
            }
        }
        return null;
    }

    public VillageCell getVillageCellById(int id) {
        for(Cell cell : getMap()) {
            if(cell instanceof VillageCell) {
                if(cell.getId() == id) {
                    return (VillageCell) cell;
                }
            }
        }
        return null;
    }

    public ResourceCell getCenterCell() {
        return centerCell;
    }
}
