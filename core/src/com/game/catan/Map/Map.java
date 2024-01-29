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
        ResourceCell emptyCell = new ResourceCell(840, 675, ResourceType.EMPTY);
        centerCell = new ResourceCell(910, 570, ResourceType.BRICK);
        ResourceCell brickCell = new ResourceCell(980, 675, ResourceType.BRICK);
        ResourceCell brickCell2 = new ResourceCell(1120, 465, ResourceType.BRICK);
        ResourceCell brickCell3 = new ResourceCell(910, 360, ResourceType.BRICK);
        ResourceCell woodCell = new ResourceCell(840, 465, ResourceType.WOOD);
        ResourceCell woodCell2 = new ResourceCell(700, 465, ResourceType.WOOD);
        ResourceCell woodCell3 = new ResourceCell(1120, 675, ResourceType.WOOD);
        ResourceCell wheatCell = new ResourceCell(770, 570, ResourceType.WHEAT);
        ResourceCell wheatCell2 = new ResourceCell(700, 675, ResourceType.WHEAT);
        ResourceCell wheatCell3 = new ResourceCell(1190, 570, ResourceType.WHEAT);
        ResourceCell wheatCell4 = new ResourceCell(1050, 780, ResourceType.WHEAT);
        ResourceCell sheepCell = new ResourceCell(980, 465, ResourceType.SHEEP);
        ResourceCell sheepCell2 = new ResourceCell(630, 570, ResourceType.SHEEP);
        ResourceCell sheepCell3 = new ResourceCell(910, 780, ResourceType.SHEEP);
        ResourceCell sheepCell4 = new ResourceCell(770, 360, ResourceType.SHEEP);
        ResourceCell stoneCell = new ResourceCell(1050, 570, ResourceType.STONE);
        ResourceCell stoneCell2 = new ResourceCell(770, 780, ResourceType.STONE);
        ResourceCell stoneCell3 = new ResourceCell(1050, 360, ResourceType.STONE);
        VillageCell tempVillage = new VillageCell(871, 568);
        VillageCell tempVillage2 = new VillageCell(871, 640);
        VillageCell tempVillage3 = new VillageCell(1013, 568);
        VillageCell tempVillage4 = new VillageCell(1013, 640);
        VillageCell tempVillage5 = new VillageCell(942, 532);
        RoadCell roadCell = new RoadCell(865, 600, 0);
        RoadCell roadCell2 = new RoadCell(1007, 600, 0);
        RoadCell roadCell3 = new RoadCell(1073, 535, 60);
        RoadCell roadCell4 = new RoadCell(932, 535, 60);

        roadCell.addNeighbour(tempVillage);
        roadCell.addNeighbour(tempVillage2);

        roadCell2.addNeighbour(tempVillage3);
        roadCell2.addNeighbour(tempVillage4);

        roadCell3.addNeighbour(tempVillage3);

        roadCell4.addNeighbour(tempVillage);
        roadCell4.addNeighbour(tempVillage5);

        stoneCell2.addNeighbour(wheatCell2);
        stoneCell2.addNeighbour(sheepCell3);

        sheepCell3.addNeighbour(stoneCell2);
        sheepCell3.addNeighbour(wheatCell4);

        woodCell3.addNeighbour(wheatCell4);

        stoneCell.addNeighbour(woodCell3);
        stoneCell.addNeighbour(roadCell2);
        stoneCell.addNeighbour(roadCell3);
        stoneCell.addNeighbour(sheepCell);
        stoneCell.addNeighbour(wheatCell3);
        stoneCell.addNeighbour(brickCell2);
        stoneCell.addNeighbour(brickCell);

        brickCell.addNeighbour(woodCell3);
        brickCell.addNeighbour(sheepCell3);
        brickCell.addNeighbour(wheatCell4);

        brickCell2.addNeighbour(stoneCell3);

        wheatCell.addNeighbour(woodCell2);
        wheatCell.addNeighbour(wheatCell2);
        wheatCell.addNeighbour(roadCell);
        wheatCell.addNeighbour(sheepCell2);

        emptyCell.addNeighbour(tempVillage2);
        emptyCell.addNeighbour(brickCell);
        emptyCell.addNeighbour(wheatCell);
        emptyCell.addNeighbour(wheatCell2);
        emptyCell.addNeighbour(stoneCell2);
        emptyCell.addNeighbour(sheepCell3);
        emptyCell.setRobber(true);

        wheatCell2.addNeighbour(sheepCell2);
        wheatCell2.addNeighbour(woodCell2);
        wheatCell2.addNeighbour(stoneCell2);

        wheatCell3.addNeighbour(woodCell3);
        wheatCell3.addNeighbour(brickCell2);

        centerCell.addNeighbour(roadCell);
        centerCell.addNeighbour(roadCell2);
        centerCell.addNeighbour(roadCell4);
        centerCell.addNeighbour(brickCell);
        centerCell.addNeighbour(woodCell);
        centerCell.addNeighbour(emptyCell);

        sheepCell.addNeighbour(roadCell3);
        sheepCell.addNeighbour(brickCell2);
        sheepCell.addNeighbour(brickCell3);
        stoneCell.addNeighbour(stoneCell3);

        woodCell.addNeighbour(sheepCell);
        woodCell.addNeighbour(wheatCell);
        woodCell.addNeighbour(woodCell2);
        woodCell.addNeighbour(roadCell4);
        woodCell.addNeighbour(sheepCell4);

        sheepCell4.addNeighbour(brickCell3);
        brickCell3.addNeighbour(stoneCell3);

        woodCell2.addNeighbour(sheepCell4);
        woodCell2.addNeighbour(brickCell3);

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

    public void moveRobber(int resourceCellId) {
        robberCell.setRobber(false);
        getResourceCellById(resourceCellId).setRobber(true);
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

    public ResourceCell getResourceCellById(int id) {
        for(Cell cell : getMap(centerCell, new HashSet<Cell>())) {
            if(cell instanceof ResourceCell) {
                if(cell.getId() == id) {
                    return (ResourceCell) cell;
                }
            }
        }
        return null;
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
