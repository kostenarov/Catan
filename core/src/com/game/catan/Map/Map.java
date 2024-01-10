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
        centerCell = new ResourceCell(960, 540, ResourceType.BRICK);
        ResourceCell woodCell = new ResourceCell(890, 435, ResourceType.WOOD);
        ResourceCell wheatCell = new ResourceCell(820, 540, ResourceType.WHEAT);
        ResourceCell emptyCell = new ResourceCell(890, 645, ResourceType.EMPTY);
        VillageCell tempVillage = new VillageCell(921, 538);
        VillageCell tempVillage2 = new VillageCell(921, 610);
        RoadCell roadCell = new RoadCell(915, 570);
        roadCell.addNeighbour(centerCell);
        roadCell.addNeighbour(wheatCell);
        roadCell.addNeighbour(tempVillage);
        roadCell.addNeighbour(tempVillage2);
        emptyCell.setRobber(true);
        wheatCell.addNeighbour(tempVillage);
        wheatCell.addNeighbour(tempVillage2);
        woodCell.addNeighbour(tempVillage);
        emptyCell.addNeighbour(tempVillage2);
        centerCell.addNeighbour(tempVillage);
        centerCell.addNeighbour(tempVillage2);
        centerCell.addNeighbour(wheatCell);
        centerCell.addNeighbour(woodCell);
        centerCell.addNeighbour(emptyCell);
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
        for(Cell cell : getMap()) {
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

    public RoadCell getRoadCellById(int id) {
        for(Cell cell : getMap()) {
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
