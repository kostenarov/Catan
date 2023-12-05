package com.game.catan.Map;

import com.game.catan.Map.Cell.*;

import java.util.ArrayList;
import java.util.HashSet;

public class Map {
    private resourceCell robberCell;
    private final resourceCell centerCell;
    public Map() {
        centerCell = new resourceCell(0, 0, ResourceType.BRICK);
        centerCell.addNeighbour(new resourceCell(1, 0, ResourceType.WOOD));
    }

    public VillageCell getSpecificVillage(int x, int y) {
        centerCell.getVillages().get(0);
        for(VillageCell villageCell : getVillageNeighbours(centerCell.getVillages().get(0))) {
            if(villageCell.getX() == x && villageCell.getY() == y) {
                return villageCell;
            }
        }
        return null;
    }

    public HashSet<VillageCell> getVillageNeighbours(VillageCell villageCell) {
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

    public HashSet<Cell> getMapNeighbours(Cell cell) {
        HashSet<Cell> neighbours = new HashSet<>();
        for(Cell neighbour : cell.getNeighbours()) {
            neighbours.add(neighbour);
            neighbours.addAll(getMapNeighbours(neighbour));
        }
        return neighbours;
    }

    public void drawMap() {
        for(Cell cell : getMap()) {
            cell.draw();
        }
    }

    public void dispose() {
        for(Cell cell : getMap()) {
            cell.dispose();
        }
    }
}
