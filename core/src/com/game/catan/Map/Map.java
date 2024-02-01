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
        //------------First row------------
        ResourceCell firstLevelBrickCell = new ResourceCell(910, 360, ResourceType.BRICK);
        ResourceCell firstLevelStoneCell = new ResourceCell(1050, 360, ResourceType.STONE);
        ResourceCell firstLevelSheepCell = new ResourceCell(770, 360, ResourceType.SHEEP);

        //------------Second row------------
        ResourceCell secondLevelWoodCell = new ResourceCell(700, 465, ResourceType.WOOD);
        ResourceCell secondLevelWoodCell2 = new ResourceCell(840, 465, ResourceType.WOOD);
        ResourceCell secondLevelSheepCell = new ResourceCell(980, 465, ResourceType.SHEEP);
        ResourceCell secondLevelBrickCell = new ResourceCell(1120, 465, ResourceType.BRICK);

        //------------Third row------------
        centerCell = new ResourceCell(910, 570, ResourceType.BRICK);
        ResourceCell thirdLevelSheepCell = new ResourceCell(630, 570, ResourceType.SHEEP);
        ResourceCell thirdLevelWheatCell = new ResourceCell(770, 570, ResourceType.WHEAT);
        ResourceCell thirdLevelStoneCell = new ResourceCell(1050, 570, ResourceType.STONE);
        ResourceCell thirdLevelWheatCell2 = new ResourceCell(1190, 570, ResourceType.WHEAT);
        RoadCell roadCell6 = new RoadCell(585, 600, 0);
        RoadCell roadCell5 = new RoadCell(725, 600, 0);
        RoadCell roadCell = new RoadCell(865, 600, 0);
        RoadCell roadCell2 = new RoadCell(1005, 600, 0);
        RoadCell roadCell7 = new RoadCell(1145, 600, 0);
        RoadCell roadCell8 = new RoadCell(1285, 600, 0);


        //------------Fourth row------------
        ResourceCell emptyCell = new ResourceCell(840, 675, ResourceType.EMPTY);
        ResourceCell forthLevelBrickCell = new ResourceCell(980, 675, ResourceType.BRICK);
        ResourceCell forthLevelWoodCell = new ResourceCell(1120, 675, ResourceType.WOOD);
        ResourceCell forthLevelWheatCell = new ResourceCell(700, 675, ResourceType.WHEAT);
        
        //------------Fifth row------------
        ResourceCell fifthLevelStoneCell = new ResourceCell(770, 780, ResourceType.STONE);
        ResourceCell fifthLevelSheepCell = new ResourceCell(910, 780, ResourceType.SHEEP);
        ResourceCell fifthLevelWheatCell = new ResourceCell(1050, 780, ResourceType.WHEAT);

        VillageCell tempVillage = new VillageCell(871, 568);
        VillageCell tempVillage2 = new VillageCell(871, 640);
        VillageCell tempVillage3 = new VillageCell(1013, 568);
        VillageCell tempVillage4 = new VillageCell(1013, 640);
        VillageCell tempVillage5 = new VillageCell(942, 532);

        RoadCell roadCell3 = new RoadCell(1073, 535, 60);
        RoadCell roadCell4 = new RoadCell(932, 535, 60);


        roadCell.addNeighbour(tempVillage);
        roadCell.addNeighbour(tempVillage2);

        roadCell2.addNeighbour(tempVillage3);
        roadCell2.addNeighbour(tempVillage4);

        roadCell3.addNeighbour(tempVillage3);

        roadCell4.addNeighbour(tempVillage);
        roadCell4.addNeighbour(tempVillage5);

        firstLevelSheepCell.addNeighbour(firstLevelBrickCell);
        firstLevelBrickCell.addNeighbour(firstLevelStoneCell);

        secondLevelSheepCell.addNeighbour(roadCell3);
        secondLevelSheepCell.addNeighbour(secondLevelBrickCell);
        secondLevelSheepCell.addNeighbour(firstLevelBrickCell);
        secondLevelWoodCell2.addNeighbour(secondLevelSheepCell);
        secondLevelWoodCell2.addNeighbour(thirdLevelWheatCell);
        secondLevelWoodCell2.addNeighbour(secondLevelWoodCell);
        secondLevelWoodCell2.addNeighbour(roadCell4);
        secondLevelWoodCell2.addNeighbour(firstLevelSheepCell);
        secondLevelWoodCell.addNeighbour(firstLevelSheepCell);
        secondLevelWoodCell.addNeighbour(firstLevelBrickCell);
        secondLevelBrickCell.addNeighbour(firstLevelStoneCell);

        thirdLevelWheatCell.addNeighbour(roadCell5);
        thirdLevelSheepCell.addNeighbour(roadCell5);
        thirdLevelSheepCell.addNeighbour(roadCell6);
        thirdLevelStoneCell.addNeighbour(forthLevelWoodCell);
        thirdLevelStoneCell.addNeighbour(secondLevelSheepCell);
        thirdLevelStoneCell.addNeighbour(thirdLevelWheatCell2);
        thirdLevelStoneCell.addNeighbour(secondLevelBrickCell);
        thirdLevelStoneCell.addNeighbour(forthLevelBrickCell);
        thirdLevelStoneCell.addNeighbour(firstLevelStoneCell);
        thirdLevelWheatCell.addNeighbour(secondLevelWoodCell);
        thirdLevelWheatCell.addNeighbour(forthLevelWheatCell);
        thirdLevelWheatCell2.addNeighbour(forthLevelWoodCell);
        thirdLevelWheatCell2.addNeighbour(secondLevelBrickCell);
        centerCell.addNeighbour(roadCell4);
        centerCell.addNeighbour(forthLevelBrickCell);
        centerCell.addNeighbour(secondLevelWoodCell2);
        centerCell.addNeighbour(emptyCell);
        centerCell.addNeighbour(roadCell);
        centerCell.addNeighbour(roadCell2);

        fifthLevelStoneCell.addNeighbour(forthLevelWheatCell);
        fifthLevelStoneCell.addNeighbour(fifthLevelSheepCell);
        fifthLevelSheepCell.addNeighbour(fifthLevelStoneCell);
        fifthLevelSheepCell.addNeighbour(fifthLevelWheatCell);

        forthLevelWoodCell.addNeighbour(fifthLevelWheatCell);
        forthLevelBrickCell.addNeighbour(forthLevelWoodCell);
        forthLevelBrickCell.addNeighbour(fifthLevelSheepCell);
        forthLevelBrickCell.addNeighbour(fifthLevelWheatCell);
        forthLevelWheatCell.addNeighbour(thirdLevelSheepCell);
        forthLevelWheatCell.addNeighbour(secondLevelWoodCell);
        forthLevelWheatCell.addNeighbour(fifthLevelStoneCell);
        forthLevelWheatCell.addNeighbour(roadCell7);
        forthLevelWheatCell.addNeighbour(roadCell8);
        emptyCell.addNeighbour(tempVillage2);
        emptyCell.addNeighbour(forthLevelBrickCell);
        emptyCell.addNeighbour(thirdLevelWheatCell);
        emptyCell.addNeighbour(forthLevelWheatCell);
        emptyCell.addNeighbour(fifthLevelStoneCell);
        emptyCell.addNeighbour(fifthLevelSheepCell);
        emptyCell.setRobber(true);

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

    public void setRobberCell(ResourceCell newRobber) {
        robberCell.setRobber(false);
        robberCell = newRobber;
        robberCell.setRobber(true);
    }

    public Cell getCellById(int id) {
        for(Cell cell : getMap(centerCell, new HashSet<Cell>())) {
            if(cell.getId() == id) {
                return cell;
            }
        }
        return null;
    }

    public ResourceCell getRobberCell() {
        return robberCell;
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
    public void setVillageCell(VillageCell cell){
        getVillageCellById(cell.getId()).setOwner(cell.getOwner());
        getVillageCellById(cell.getId()).setVillagePath(cell.getVillagePath());
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

    public void setRoadCell(RoadCell cell){
        getRoadCellById(cell.getId()).setOwner(cell.getOwner());
        getRoadCellById(cell.getId()).setRoadTexture(cell.getRoadTexture());
    }

    public ResourceCell getCenterCell() {
        return centerCell;
    }
}
