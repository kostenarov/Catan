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
        RoadCell roadCell16 = new RoadCell(740, 390, 0);
        RoadCell roadCell17 = new RoadCell(880, 390, 0);
        RoadCell roadCell18 = new RoadCell(1020, 390, 0);
        RoadCell roadCell19 = new RoadCell(1160, 390, 0);


        //------------Second row------------
        ResourceCell secondLevelWoodCell = new ResourceCell(700, 465, ResourceType.WOOD);
        ResourceCell secondLevelWoodCell2 = new ResourceCell(840, 465, ResourceType.WOOD);
        ResourceCell secondLevelSheepCell = new ResourceCell(980, 465, ResourceType.SHEEP);
        ResourceCell secondLevelBrickCell = new ResourceCell(1120, 465, ResourceType.BRICK);
        RoadCell roadCell11 = new RoadCell(950, 495, 0);
        RoadCell roadCell12 = new RoadCell(670, 495, 0);
        RoadCell roadCell13 = new RoadCell(810, 495, 0);
        RoadCell roadCell14 = new RoadCell(1090, 495, 0);
        RoadCell roadCell15 = new RoadCell(1230, 495, 0);


        //------------Third row------------
        ResourceCell thirdLevelSheepCell = new ResourceCell(630, 570, ResourceType.SHEEP);
        ResourceCell thirdLevelWheatCell = new ResourceCell(770, 570, ResourceType.WHEAT);
        centerCell = new ResourceCell(910, 570, ResourceType.BRICK);
        ResourceCell thirdLevelStoneCell = new ResourceCell(1050, 570, ResourceType.STONE);
        ResourceCell thirdLevelWheatCell2 = new ResourceCell(1190, 570, ResourceType.WHEAT);
        RoadCell roadCell6 = new RoadCell(600, 600, 0);
        RoadCell roadCell5 = new RoadCell(740, 600, 0);
        RoadCell roadCell = new RoadCell(880, 600, 0);
        RoadCell roadCell2 = new RoadCell(1020, 600, 0);
        RoadCell roadCell7 = new RoadCell(1160, 600, 0);
        RoadCell roadCell8 = new RoadCell(1300, 600, 0);


        //------------Fourth row------------
        ResourceCell forthLevelWheatCell = new ResourceCell(700, 675, ResourceType.WHEAT);
        ResourceCell emptyCell = new ResourceCell(840, 675, ResourceType.EMPTY);
        ResourceCell forthLevelBrickCell = new ResourceCell(980, 675, ResourceType.BRICK);
        ResourceCell forthLevelWoodCell = new ResourceCell(1120, 675, ResourceType.WOOD);
        RoadCell roadCell20 = new RoadCell(670, 705, 0);
        RoadCell roadCell21 = new RoadCell(810, 705, 0);
        RoadCell roadCell22 = new RoadCell(950, 705, 0);
        RoadCell roadCell23 = new RoadCell(1090, 705, 0);
        RoadCell roadCell24 = new RoadCell(1230, 705, 0);


        //------------Fifth row------------
        ResourceCell fifthLevelStoneCell = new ResourceCell(770, 780, ResourceType.STONE);
        ResourceCell fifthLevelSheepCell = new ResourceCell(910, 780, ResourceType.SHEEP);
        ResourceCell fifthLevelWheatCell = new ResourceCell(1050, 780, ResourceType.WHEAT);
        RoadCell roadCell25 = new RoadCell(740, 810, 0);
        RoadCell roadCell26 = new RoadCell(880, 810, 0);
        RoadCell roadCell27 = new RoadCell(1020, 810, 0);
        RoadCell roadCell28 = new RoadCell(1160, 810, 0);


        VillageCell tempVillage = new VillageCell(873, 568);
        VillageCell tempVillage2 = new VillageCell(873, 650);
        VillageCell tempVillage3 = new VillageCell(1013, 568);
        VillageCell tempVillage4 = new VillageCell(1013, 650);
        VillageCell tempVillage5 = new VillageCell(943, 540);
        VillageCell tempVillage6 = new VillageCell(733, 650);
        VillageCell tempVillage7 = new VillageCell(733, 568);
        VillageCell tempVillage8 = new VillageCell(943, 460);

        RoadCell roadCell4 = new RoadCell(940, 548, 63);
        RoadCell roadCell3 = new RoadCell(1080, 548, 63);
        RoadCell roadCell10 = new RoadCell(800, 548, 63);
        RoadCell roadCell9 = new RoadCell(970, 567, -60);

        roadCell.addNeighbour(tempVillage);
        roadCell.addNeighbour(tempVillage2);

        roadCell2.addNeighbour(tempVillage3);
        roadCell2.addNeighbour(tempVillage4);

        roadCell3.addNeighbour(tempVillage3);

        roadCell4.addNeighbour(tempVillage);
        roadCell4.addNeighbour(tempVillage5);

        roadCell5.addNeighbour(tempVillage6);
        roadCell5.addNeighbour(tempVillage7);

        roadCell9.addNeighbour(tempVillage3);
        roadCell9.addNeighbour(tempVillage5);

        roadCell10.addNeighbour(tempVillage7);

        roadCell11.addNeighbour(tempVillage8);
        roadCell11.addNeighbour(tempVillage5);

        firstLevelBrickCell.addNeighbour(roadCell16);
        firstLevelBrickCell.addNeighbour(roadCell17);
        firstLevelStoneCell.addNeighbour(roadCell17);
        firstLevelStoneCell.addNeighbour(roadCell18);
        firstLevelSheepCell.addNeighbour(roadCell18);
        firstLevelSheepCell.addNeighbour(roadCell19);
        secondLevelSheepCell.addNeighbour(roadCell3);
        secondLevelSheepCell.addNeighbour(roadCell9);
        secondLevelWoodCell2.addNeighbour(roadCell4);

        secondLevelSheepCell.addNeighbour(roadCell11);
        secondLevelWoodCell2.addNeighbour(roadCell11);
        secondLevelWoodCell.addNeighbour(roadCell12);
        secondLevelWoodCell.addNeighbour(roadCell13);
        secondLevelWoodCell2.addNeighbour(roadCell13);
        secondLevelBrickCell.addNeighbour(roadCell14);
        secondLevelSheepCell.addNeighbour(roadCell14);
        secondLevelBrickCell.addNeighbour(roadCell15);

        secondLevelSheepCell.addNeighbour(firstLevelBrickCell);
        secondLevelWoodCell2.addNeighbour(thirdLevelWheatCell);
        secondLevelWoodCell2.addNeighbour(firstLevelSheepCell);
        secondLevelWoodCell.addNeighbour(firstLevelSheepCell);
        secondLevelWoodCell.addNeighbour(firstLevelBrickCell);
        secondLevelBrickCell.addNeighbour(firstLevelStoneCell);

        centerCell.addNeighbour(roadCell4);
        centerCell.addNeighbour(roadCell9);
        centerCell.addNeighbour(roadCell);
        centerCell.addNeighbour(roadCell2);
        thirdLevelSheepCell.addNeighbour(roadCell6);
        thirdLevelSheepCell.addNeighbour(roadCell5);
        thirdLevelWheatCell.addNeighbour(roadCell5);
        thirdLevelWheatCell.addNeighbour(roadCell);
        thirdLevelWheatCell.addNeighbour(roadCell10);
        thirdLevelStoneCell.addNeighbour(roadCell2);
        thirdLevelStoneCell.addNeighbour(roadCell7);
        thirdLevelWheatCell2.addNeighbour(roadCell7);
        thirdLevelWheatCell2.addNeighbour(roadCell8);

        thirdLevelStoneCell.addNeighbour(forthLevelWoodCell);
        thirdLevelStoneCell.addNeighbour(secondLevelSheepCell);
        thirdLevelStoneCell.addNeighbour(secondLevelBrickCell);
        thirdLevelStoneCell.addNeighbour(forthLevelBrickCell);
        thirdLevelStoneCell.addNeighbour(firstLevelStoneCell);
        thirdLevelWheatCell.addNeighbour(secondLevelWoodCell);
        thirdLevelWheatCell.addNeighbour(forthLevelWheatCell);
        thirdLevelWheatCell2.addNeighbour(forthLevelWoodCell);
        thirdLevelWheatCell2.addNeighbour(secondLevelBrickCell);
        centerCell.addNeighbour(forthLevelBrickCell);
        centerCell.addNeighbour(secondLevelWoodCell2);
        centerCell.addNeighbour(emptyCell);

        forthLevelWheatCell.addNeighbour(roadCell20);
        forthLevelWheatCell.addNeighbour(roadCell21);
        emptyCell.addNeighbour(roadCell21);
        emptyCell.addNeighbour(roadCell22);
        forthLevelBrickCell.addNeighbour(roadCell22);
        forthLevelBrickCell.addNeighbour(roadCell23);
        forthLevelWoodCell.addNeighbour(roadCell23);
        forthLevelWoodCell.addNeighbour(roadCell24);

        forthLevelWoodCell.addNeighbour(fifthLevelWheatCell);
        forthLevelBrickCell.addNeighbour(fifthLevelSheepCell);
        forthLevelBrickCell.addNeighbour(fifthLevelWheatCell);
        forthLevelWheatCell.addNeighbour(thirdLevelSheepCell);
        forthLevelWheatCell.addNeighbour(secondLevelWoodCell);
        forthLevelWheatCell.addNeighbour(fifthLevelStoneCell);
        emptyCell.addNeighbour(tempVillage2);
        emptyCell.addNeighbour(thirdLevelWheatCell);
        emptyCell.addNeighbour(fifthLevelStoneCell);
        emptyCell.addNeighbour(fifthLevelSheepCell);
        emptyCell.setRobber(true);

        fifthLevelStoneCell.addNeighbour(forthLevelWheatCell);
        fifthLevelStoneCell.addNeighbour(roadCell25);
        fifthLevelStoneCell.addNeighbour(roadCell26);
        fifthLevelSheepCell.addNeighbour(roadCell26);
        fifthLevelSheepCell.addNeighbour(roadCell27);
        fifthLevelWheatCell.addNeighbour(roadCell27);
        fifthLevelWheatCell.addNeighbour(roadCell28);

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
