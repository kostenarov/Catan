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
        VillageCell tempVillage = new VillageCell(871, 568);
        VillageCell tempVillage2 = new VillageCell(871, 640);
        VillageCell tempVillage3 = new VillageCell(1013, 568);
        RoadCell roadCell = new RoadCell(865, 600);
        roadCell.addNeighbour(centerCell);
        roadCell.addNeighbour(wheatCell);
        roadCell.addNeighbour(tempVillage);
        roadCell.addNeighbour(tempVillage2);
        stoneCell.addNeighbour(sheepCell);
        stoneCell.addNeighbour(tempVillage3);
        centerCell.addNeighbour(brickCell);
        centerCell.addNeighbour(tempVillage3);
        emptyCell.addNeighbour(brickCell);
        stoneCell.addNeighbour(brickCell);
        centerCell.addNeighbour(stoneCell);
        sheepCell.addNeighbour(centerCell);
        emptyCell.setRobber(true);
        wheatCell.addNeighbour(tempVillage);
        wheatCell.addNeighbour(tempVillage2);
        woodCell.addNeighbour(tempVillage);
        woodCell.addNeighbour(sheepCell);
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
