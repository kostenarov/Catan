package com.game.catan.Map;

import com.game.catan.Map.Cell.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Map implements Serializable {
    private ResourceCell robberCell;
    private final ResourceCell centerCell;
    public Map() {
        centerCell = new ResourceCell(910, 570, ResourceType.BRICK);
        ResourceCell woodCell = new ResourceCell(840, 465, ResourceType.WOOD);
        ResourceCell wheatCell = new ResourceCell(770, 570, ResourceType.WHEAT);
        ResourceCell emptyCell = new ResourceCell(840, 675, ResourceType.EMPTY);
        ResourceCell sheepCell = new ResourceCell(980, 465, ResourceType.SHEEP);
        ResourceCell stoneCell = new ResourceCell(1050, 570, ResourceType.STONE);
        ResourceCell brickCell = new ResourceCell(980, 675, ResourceType.BRICK);
        ResourceCell woodCell2 = new ResourceCell(700, 465, ResourceType.WOOD);
        VillageCell tempVillage = new VillageCell(871, 568);
        VillageCell tempVillage2 = new VillageCell(871, 640);
        VillageCell tempVillage3 = new VillageCell(1013, 568);
        VillageCell tempVillage4 = new VillageCell(1013, 640);
        RoadCell roadCell = new RoadCell(865, 600, 0);
        RoadCell roadCell2 = new RoadCell(1007, 600, 0);
        RoadCell roadCell3 = new RoadCell(1073, 535, 60);

        roadCell.addNeighbour(tempVillage);
        roadCell.addNeighbour(tempVillage2);

        roadCell2.addNeighbour(tempVillage3);
        roadCell2.addNeighbour(tempVillage4);

        roadCell3.addNeighbour(tempVillage3);

        stoneCell.addNeighbour(roadCell2);
        stoneCell.addNeighbour(roadCell3);
        stoneCell.addNeighbour(sheepCell);
        stoneCell.addNeighbour(brickCell);

        wheatCell.addNeighbour(woodCell2);
        wheatCell.addNeighbour(roadCell);

        emptyCell.addNeighbour(tempVillage2);
        emptyCell.addNeighbour(brickCell);
        emptyCell.addNeighbour(wheatCell);
        emptyCell.setRobber(true);

        centerCell.addNeighbour(roadCell);
        centerCell.addNeighbour(roadCell2);
        centerCell.addNeighbour(brickCell);
        centerCell.addNeighbour(woodCell);
        centerCell.addNeighbour(emptyCell);

        sheepCell.addNeighbour(roadCell3);

        woodCell.addNeighbour(tempVillage);
        woodCell.addNeighbour(sheepCell);
        woodCell.addNeighbour(wheatCell);
        woodCell.addNeighbour(woodCell2);

        System.out.println(centerCell.getDiceThrow());
        robberCell = findRobber();
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

    public HashSet<Cell> getMap(Cell start, HashSet<Cell> visited) {

        visited.add(start);
        for (Cell cell : start.getNeighbours()) {
            if(!visited.contains(cell)) {
                visited.add(cell);
                getMap(cell, visited);
            }
        }
        return visited;
    }

    private HashSet<Cell> getCellNeighbours(Cell cell) {
        return new HashSet<>(cell.getNeighbours());
    }

    private ResourceCell findRobber() {
        for(ResourceCell resourceCell : getResourceNeighbours(centerCell)) {
            if(resourceCell.hasRobber()) {
                robberCell = resourceCell;
                break;
            }
        }
        return robberCell;
    }

    private int findLongestRoad() {
        HashSet<RoadCell> takenRoads = new HashSet<>();
        for(Cell cell : getMap(centerCell, new HashSet<Cell>())) {
            if(cell instanceof RoadCell) {
                if(((RoadCell) cell).isBuilt()) {
                    takenRoads.add((RoadCell) cell);
                }
            }
        }
        if(takenRoads.size() < 5) {
            return 5;
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

    public HashSet<Cell> getResources() {
        HashSet<Cell> map = new HashSet<>();
        map.add(centerCell);
        for (Cell cell : map) {
            if(cell instanceof ResourceCell)
                map.addAll(getResourceNeighbours((ResourceCell) cell));
        }
        return map;
    }

    public HashSet<ResourceCell> getResourceNeighbours(ResourceCell resourceCell) {
        HashSet<ResourceCell> resourceCells = new HashSet<>();
        for(Cell cell : resourceCell.getNeighbours()) {
            if(cell instanceof ResourceCell) {
                resourceCells.add((ResourceCell) cell);
            }
        }
        return resourceCells;
    }

    public Cell getCellById(int id) {
        for(Cell cell : getMap(centerCell, new HashSet<Cell>())) {
            if(cell.getId() == id) {
                return cell;
            }
        }
        return null;
    }

    public VillageCell getVillageCellById(int id) {
        for(Cell cell : getMap(centerCell, new HashSet<Cell>())) {
            if(cell instanceof VillageCell) {
                if(cell.getId() == id) {
                    return (VillageCell) cell;
                }
            }
        }
        return null;
    }

    public RoadCell getRoadCellById(int id) {
        for(Cell cell : getMap(centerCell, new HashSet<Cell>())) {
            if(cell instanceof RoadCell) {
                if(cell.getId() == id) {
                    return (RoadCell) cell;
                }
            }
        }
        return null;
    }

    public ResourceCell getCenterCell() {
        return centerCell;
    }
}
