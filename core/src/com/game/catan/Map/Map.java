package com.game.catan.Map;

import com.game.catan.Map.Cell.*;

import java.util.ArrayList;
import java.util.HashSet;

public class Map {
    private ResourceCell robberCell;
    private final ResourceCell centerCell;
    public Map() {
        centerCell = new ResourceCell(960, 540, ResourceType.BRICK);
        centerCell.addNeighbour(new ResourceCell(960, 200, ResourceType.WOOD));
        ResourceCell tempCell = new ResourceCell(760, 540, ResourceType.WHEAT);
        tempCell.setRobber(true);
        centerCell.addNeighbour(tempCell);
        VillageCell tempVillage = new VillageCell(760, 840);
        tempVillage.addNeighbour(centerCell);
        tempVillage.addNeighbour(tempCell);
        tempCell.addNeighbour(tempVillage);
        centerCell.addNeighbour(tempVillage);
        findRobber();
    }

    public VillageCell getSpecificVillage(int x, int y) {
        centerCell.getVillages().get(0);
        for(VillageCell villageCell : getVillageNeighbours(centerCell.getVillages().get(0))) {
            if(villageCell.getCellCords().getX() == x && villageCell.getCellCords().getY() == y) {
                return villageCell;
            }
        }
        return null;
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
        for(Cell cell : centerCell.getNeighbours()) {
            map.add(cell);
            map.addAll(getMapNeighbours(cell));
        }
        return map;
    }

    private HashSet<Cell> getMapNeighbours(Cell cell) {
        HashSet<Cell> neighbours = new HashSet<>();
        for(Cell neighbour : cell.getNeighbours()) {
            neighbours.add(neighbour);
            neighbours.addAll(getMapNeighbours(neighbour));
        }
        return neighbours;
    }

    private void findRobber() {
        for(ResourceCell resourceCell : getResourceCells(centerCell)) {
            if(resourceCell.HasRobber()) {
                robberCell = resourceCell;
                break;
            }
        }
    }

    private HashSet<ResourceCell> getResourceCells(ResourceCell resourceCell) {
        HashSet<ResourceCell> resourceCells = new HashSet<>();
        for(Cell cell : resourceCell.getNeighbours()) {
            if(cell instanceof ResourceCell) {
                resourceCells.add((ResourceCell) cell);
                resourceCells.addAll(getResourceCells((ResourceCell) cell));
            }
        }
        return resourceCells;
    }

    private Cell getCellById(int id) {
        for(Cell cell : getMap()) {
            if(cell.getId() == id) {
                return cell;
            }
        }
        return null;
    }
}
