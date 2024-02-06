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
        RoadCell bottomLeftRoad = new RoadCell(800, 340, 63);
        RoadCell bottomRightRoad = new RoadCell(830, 357, -63);
        RoadCell bottomLeftRoad2 = new RoadCell(940, 340, 63);
        RoadCell bottomRightRoad2 = new RoadCell(970, 357, -63);
        RoadCell bottomLeftRoad3 = new RoadCell(1080, 340, 63);
        RoadCell bottomRightRoad3 = new RoadCell(1110, 357, -63);
        VillageCell bottomVillage = new VillageCell(733, 358);
        VillageCell bottomVillage2 = new VillageCell(803, 335);
        VillageCell bottomVillage3 = new VillageCell(873, 358);
        VillageCell bottomVillage4 = new VillageCell(943, 335);
        VillageCell bottomVillage5 = new VillageCell(1013, 358);
        VillageCell bottomVillage6 = new VillageCell(1083, 335);
        VillageCell bottomVillage7 = new VillageCell(1153, 358);

        //------------First row------------
        ResourceCell firstLevelSheepCell = new ResourceCell(770, 360, ResourceType.SHEEP);
        ResourceCell firstLevelBrickCell = new ResourceCell(910, 360, ResourceType.BRICK);
        ResourceCell firstLevelStoneCell = new ResourceCell(1050, 360, ResourceType.STONE);

        RoadCell firstLevelVerticalRoad = new RoadCell(740, 390, 0);
        RoadCell firstLevelVerticalRoad2 = new RoadCell(880, 390, 0);
        RoadCell firstLevelVerticalRoad3 = new RoadCell(1020, 390, 0);
        RoadCell firstLevelVerticalRoad4 = new RoadCell(1160, 390, 0);


        RoadCell firstLevelLeftCornerRoad = new RoadCell(730, 445, 63);
        RoadCell firstLevelRightRoad = new RoadCell(760, 462, -63);
        RoadCell firstLevelLeftRoad2 = new RoadCell(870, 445, 63);
        RoadCell firstLevelRightRoad2 = new RoadCell(900, 462, -63);
        RoadCell firstLevelLeftRoad3 = new RoadCell(1010, 445, 63);
        RoadCell firstLevelRightRoad3 = new RoadCell(1040, 462, -63);
        RoadCell firstLevelLeftRoad4 = new RoadCell(1150, 445, 63);
        RoadCell firstLevelRightCornerRoad = new RoadCell(1180, 462, -63);

        //------------Second row------------
        ResourceCell secondLevelWoodCell = new ResourceCell(700, 465, ResourceType.WOOD);
        ResourceCell secondLevelWoodCell2 = new ResourceCell(840, 465, ResourceType.WOOD);
        ResourceCell secondLevelSheepCell = new ResourceCell(980, 465, ResourceType.SHEEP);
        ResourceCell secondLevelBrickCell = new ResourceCell(1120, 465, ResourceType.BRICK);

        //Vertical roads
        RoadCell secondLevelVerticalRoad = new RoadCell(670, 495, 0);
        RoadCell secondLevelVerticalRoad2 = new RoadCell(810, 495, 0);
        RoadCell secondLevelVerticalRoad3 = new RoadCell(950, 495, 0);
        RoadCell secondLevelVerticalRoad4 = new RoadCell(1090, 495, 0);
        RoadCell secondLevelVerticalRoad5 = new RoadCell(1230, 495, 0);

        //---------Roads between second and third row
        RoadCell secondLevelLeftCornerRoad = new RoadCell(660, 550, 63);
        RoadCell secondLevelRightRoad = new RoadCell(690, 567, -63);
        RoadCell secondLevelLeftRoad2 = new RoadCell(800, 550, 63);
        RoadCell secondLevelRightRoad2 = new RoadCell(830, 567, -63);
        RoadCell secondLevelLeftRoad3 = new RoadCell(940, 550, 63);
        RoadCell secondLevelRightRoad3 = new RoadCell(970, 567, -63);
        RoadCell secondLevelLeftRoad4 = new RoadCell(1080, 550, 63);
        RoadCell secondLevelRightRoad4 = new RoadCell(1110, 567, -63);
        RoadCell secondLevelLeftRoad5 = new RoadCell(1220, 550, 63);
        RoadCell secondLevelRightCornerRoad = new RoadCell(1250, 567, -63);

        //------------Third row------------
        ResourceCell thirdLevelSheepCell = new ResourceCell(630, 570, ResourceType.SHEEP);
        ResourceCell thirdLevelWheatCell = new ResourceCell(770, 570, ResourceType.WHEAT);
        centerCell = new ResourceCell(910, 570, ResourceType.BRICK);
        ResourceCell thirdLevelStoneCell = new ResourceCell(1050, 570, ResourceType.STONE);
        ResourceCell thirdLevelWheatCell2 = new ResourceCell(1190, 570, ResourceType.WHEAT);

        RoadCell thirdLevelVerticalRoad = new RoadCell(600, 600, 0);
        RoadCell thirdLevelVerticalRoad2 = new RoadCell(740, 600, 0);
        RoadCell thirdLevelVerticalRoad3 = new RoadCell(880, 600, 0);
        RoadCell thirdLevelVerticalRoad4 = new RoadCell(1020, 600, 0);
        RoadCell thirdLevelVerticalRoad5 = new RoadCell(1160, 600, 0);
        RoadCell thirdLevelVerticalRoad6 = new RoadCell(1300, 600, 0);

        RoadCell thirdLevelCornerRightRoad = new RoadCell(620, 672, -63);
        RoadCell thirdLevelLeftRoad = new RoadCell(730, 655, 63);
        RoadCell thirdLevelRightRoad = new RoadCell(760, 672, -63);
        RoadCell thirdLevelLeftRoad2 = new RoadCell(870, 655, 63);
        RoadCell thirdLevelRightRoad2 = new RoadCell(900, 672, -63);
        RoadCell thirdLevelLeftRoad3 = new RoadCell(1010, 655, 63);
        RoadCell thirdLevelRightRoad3 = new RoadCell(1040, 672, -63);
        RoadCell thirdLevelLeftRoad4 = new RoadCell(1150, 655, 63);
        RoadCell thirdLevelRightRoad4 = new RoadCell(1180, 672, -63);
        RoadCell thirdLevelCornerLeftRoad = new RoadCell(1290, 655, 63);

        //------------Fourth row------------
        ResourceCell forthLevelWheatCell = new ResourceCell(700, 675, ResourceType.WHEAT);
        ResourceCell emptyCell = new ResourceCell(840, 675, ResourceType.EMPTY);
        ResourceCell forthLevelBrickCell = new ResourceCell(980, 675, ResourceType.BRICK);
        ResourceCell forthLevelWoodCell = new ResourceCell(1120, 675, ResourceType.WOOD);

        RoadCell forthLevelVerticalRoad = new RoadCell(670, 705, 0);
        RoadCell forthLevelVerticalRoad2 = new RoadCell(810, 705, 0);
        RoadCell forthLevelVerticalRoad3 = new RoadCell(950, 705, 0);
        RoadCell forthLevelVerticalRoad4 = new RoadCell(1090, 705, 0);
        RoadCell forthLevelVerticalRoad5 = new RoadCell(1230, 705, 0);

        RoadCell forthLevelCornerLeftRoad = new RoadCell(690, 777, -63);
        RoadCell forthLevelRightRoad = new RoadCell(800, 760, 63);
        RoadCell forthLevelLeftRoad2 = new RoadCell(830, 777, -63);
        RoadCell forthLevelRightRoad2 = new RoadCell(940, 760, 63);
        RoadCell forthLevelLeftRoad3 = new RoadCell(970, 777, -63);
        RoadCell forthLevelRightRoad3 = new RoadCell(1080, 760, 63);
        RoadCell forthLevelLeftRoad4 = new RoadCell(1110, 777, -63);
        RoadCell forthLevelCornerRightRoad = new RoadCell(1220, 760, 63);

        //------------Fifth row------------
        ResourceCell fifthLevelStoneCell = new ResourceCell(770, 780, ResourceType.STONE);
        ResourceCell fifthLevelSheepCell = new ResourceCell(910, 780, ResourceType.SHEEP);
        ResourceCell fifthLevelWheatCell = new ResourceCell(1050, 780, ResourceType.WHEAT);

        RoadCell fifthLevelRightRoad = new RoadCell(760, 882, -63);
        RoadCell fifthLevelLeftRoad = new RoadCell(870, 865, 63);
        RoadCell fifthLevelRightRoad2 = new RoadCell(900, 882, -63);
        RoadCell fifthLevelLeftRoad2 = new RoadCell(1010, 865, 63);
        RoadCell fifthLevelRightRoad3 = new RoadCell(1040, 882, -63);
        RoadCell fifthLevelLeftRoad3 = new RoadCell(1150, 865, 63);

        RoadCell fifthLevelVerticalRoad = new RoadCell(740, 810, 0);
        RoadCell fifthLevelVerticalRoad2 = new RoadCell(880, 810, 0);
        RoadCell fifthLevelVerticalRoad3 = new RoadCell(1020, 810, 0);
        RoadCell fifthLevelVerticalRoad4 = new RoadCell(1160, 810, 0);

        VillageCell tempVillage = new VillageCell(873, 568);
        VillageCell tempVillage2 = new VillageCell(873, 650);
        VillageCell tempVillage3 = new VillageCell(1013, 568);
        VillageCell tempVillage4 = new VillageCell(1013, 650);
        VillageCell tempVillage5 = new VillageCell(943, 540);
        VillageCell tempVillage6 = new VillageCell(733, 650);
        VillageCell tempVillage7 = new VillageCell(733, 568);
        VillageCell tempVillage8 = new VillageCell(943, 460);
        emptyCell.setRobber(true);

        bottomLeftRoad.addNeighbour(bottomVillage);
        bottomLeftRoad.addNeighbour(bottomVillage2);
        bottomRightRoad.addNeighbour(bottomVillage2);
        bottomRightRoad.addNeighbour(bottomVillage3);
        bottomLeftRoad2.addNeighbour(bottomVillage3);
        bottomLeftRoad2.addNeighbour(bottomVillage4);
        bottomRightRoad2.addNeighbour(bottomVillage4);
        bottomRightRoad2.addNeighbour(bottomVillage5);
        bottomLeftRoad3.addNeighbour(bottomVillage5);
        bottomLeftRoad3.addNeighbour(bottomVillage6);
        bottomRightRoad3.addNeighbour(bottomVillage6);
        bottomRightRoad3.addNeighbour(bottomVillage7);
        firstLevelVerticalRoad.addNeighbour(bottomVillage);
        firstLevelVerticalRoad2.addNeighbour(bottomVillage3);
        firstLevelVerticalRoad3.addNeighbour(bottomVillage5);
        firstLevelVerticalRoad4.addNeighbour(bottomVillage7);

        thirdLevelVerticalRoad3.addNeighbour(tempVillage);
        thirdLevelVerticalRoad3.addNeighbour(tempVillage2);

        thirdLevelVerticalRoad4.addNeighbour(tempVillage3);
        thirdLevelVerticalRoad4.addNeighbour(tempVillage4);

        secondLevelRightRoad4.addNeighbour(tempVillage3);

        secondLevelRightRoad3.addNeighbour(tempVillage);
        secondLevelRightRoad3.addNeighbour(tempVillage5);

        thirdLevelVerticalRoad2.addNeighbour(tempVillage6);
        thirdLevelVerticalRoad2.addNeighbour(tempVillage7);

        secondLevelLeftRoad3.addNeighbour(tempVillage3);
        secondLevelLeftRoad3.addNeighbour(tempVillage5);

        secondLevelRightRoad2.addNeighbour(tempVillage7);

        secondLevelVerticalRoad3.addNeighbour(tempVillage8);
        secondLevelVerticalRoad3.addNeighbour(tempVillage5);

        firstLevelSheepCell.addNeighbour(bottomLeftRoad);
        firstLevelSheepCell.addNeighbour(bottomRightRoad);
        firstLevelBrickCell.addNeighbour(bottomLeftRoad2);
        firstLevelBrickCell.addNeighbour(bottomRightRoad2);
        firstLevelStoneCell.addNeighbour(bottomLeftRoad3);
        firstLevelStoneCell.addNeighbour(bottomRightRoad3);
        firstLevelSheepCell.addNeighbour(firstLevelVerticalRoad);
        firstLevelSheepCell.addNeighbour(firstLevelVerticalRoad2);
        firstLevelBrickCell.addNeighbour(firstLevelVerticalRoad2);
        firstLevelBrickCell.addNeighbour(firstLevelVerticalRoad3);
        firstLevelStoneCell.addNeighbour(firstLevelVerticalRoad3);
        firstLevelStoneCell.addNeighbour(firstLevelVerticalRoad4);
        firstLevelSheepCell.addNeighbour(firstLevelRightRoad);
        firstLevelSheepCell.addNeighbour(firstLevelLeftRoad2);
        firstLevelBrickCell.addNeighbour(firstLevelRightRoad2);
        firstLevelBrickCell.addNeighbour(firstLevelLeftRoad3);
        firstLevelStoneCell.addNeighbour(firstLevelRightRoad3);
        firstLevelStoneCell.addNeighbour(firstLevelLeftRoad4);

        secondLevelWoodCell.addNeighbour(firstLevelLeftCornerRoad);
        secondLevelWoodCell.addNeighbour(firstLevelRightRoad);
        secondLevelWoodCell2.addNeighbour(firstLevelLeftRoad2);
        secondLevelWoodCell2.addNeighbour(firstLevelRightRoad2);
        secondLevelSheepCell.addNeighbour(firstLevelLeftRoad3);
        secondLevelSheepCell.addNeighbour(firstLevelRightRoad3);
        secondLevelBrickCell.addNeighbour(firstLevelLeftRoad4);
        secondLevelBrickCell.addNeighbour(firstLevelRightCornerRoad);
        secondLevelWoodCell.addNeighbour(secondLevelVerticalRoad);
        secondLevelWoodCell.addNeighbour(secondLevelVerticalRoad2);
        secondLevelWoodCell2.addNeighbour(secondLevelVerticalRoad2);
        secondLevelWoodCell2.addNeighbour(secondLevelVerticalRoad3);
        secondLevelSheepCell.addNeighbour(secondLevelVerticalRoad3);
        secondLevelSheepCell.addNeighbour(secondLevelVerticalRoad4);
        secondLevelBrickCell.addNeighbour(secondLevelVerticalRoad4);
        secondLevelBrickCell.addNeighbour(secondLevelVerticalRoad5);
        secondLevelWoodCell.addNeighbour(secondLevelRightRoad);
        secondLevelWoodCell.addNeighbour(secondLevelLeftRoad2);
        secondLevelWoodCell2.addNeighbour(secondLevelRightRoad2);
        secondLevelWoodCell2.addNeighbour(secondLevelLeftRoad3);
        secondLevelSheepCell.addNeighbour(secondLevelRightRoad3);
        secondLevelSheepCell.addNeighbour(secondLevelLeftRoad4);
        secondLevelBrickCell.addNeighbour(secondLevelRightRoad4);
        secondLevelBrickCell.addNeighbour(secondLevelLeftRoad5);

        thirdLevelSheepCell.addNeighbour(secondLevelLeftCornerRoad);
        thirdLevelSheepCell.addNeighbour(secondLevelRightRoad);
        thirdLevelSheepCell.addNeighbour(thirdLevelVerticalRoad);
        thirdLevelSheepCell.addNeighbour(thirdLevelVerticalRoad2);
        thirdLevelSheepCell.addNeighbour(thirdLevelCornerRightRoad);
        thirdLevelSheepCell.addNeighbour(thirdLevelLeftRoad);
        thirdLevelWheatCell.addNeighbour(thirdLevelVerticalRoad2);
        thirdLevelWheatCell.addNeighbour(thirdLevelVerticalRoad3);
        thirdLevelWheatCell.addNeighbour(thirdLevelLeftRoad2);
        thirdLevelWheatCell.addNeighbour(thirdLevelRightRoad);
        thirdLevelWheatCell.addNeighbour(secondLevelRightRoad2);
        thirdLevelWheatCell.addNeighbour(secondLevelLeftRoad2);
        centerCell.addNeighbour(thirdLevelVerticalRoad3);
        centerCell.addNeighbour(thirdLevelVerticalRoad4);
        centerCell.addNeighbour(thirdLevelRightRoad2);
        centerCell.addNeighbour(thirdLevelLeftRoad3);
        centerCell.addNeighbour(secondLevelRightRoad2);
        centerCell.addNeighbour(secondLevelLeftRoad2);
        thirdLevelStoneCell.addNeighbour(thirdLevelVerticalRoad4);
        thirdLevelStoneCell.addNeighbour(thirdLevelVerticalRoad5);
        thirdLevelStoneCell.addNeighbour(thirdLevelRightRoad3);
        thirdLevelStoneCell.addNeighbour(thirdLevelLeftRoad4);
        thirdLevelStoneCell.addNeighbour(secondLevelRightRoad3);
        thirdLevelStoneCell.addNeighbour(secondLevelLeftRoad3);
        thirdLevelWheatCell2.addNeighbour(thirdLevelVerticalRoad5);
        thirdLevelWheatCell2.addNeighbour(thirdLevelVerticalRoad6);
        thirdLevelWheatCell2.addNeighbour(secondLevelRightCornerRoad);
        thirdLevelWheatCell2.addNeighbour(secondLevelLeftRoad4);
        thirdLevelWheatCell2.addNeighbour(thirdLevelRightRoad4);
        thirdLevelWheatCell2.addNeighbour(thirdLevelCornerLeftRoad);

        forthLevelWheatCell.addNeighbour(thirdLevelLeftRoad);
        forthLevelWheatCell.addNeighbour(thirdLevelRightRoad);
        emptyCell.addNeighbour(thirdLevelLeftRoad2);
        emptyCell.addNeighbour(thirdLevelRightRoad2);
        forthLevelBrickCell.addNeighbour(thirdLevelLeftRoad3);
        forthLevelBrickCell.addNeighbour(thirdLevelRightRoad3);
        forthLevelWoodCell.addNeighbour(thirdLevelLeftRoad4);
        forthLevelWoodCell.addNeighbour(thirdLevelRightRoad4);
        forthLevelWheatCell.addNeighbour(forthLevelVerticalRoad);
        forthLevelWheatCell.addNeighbour(forthLevelVerticalRoad2);
        emptyCell.addNeighbour(forthLevelVerticalRoad2);
        emptyCell.addNeighbour(forthLevelVerticalRoad3);
        forthLevelBrickCell.addNeighbour(forthLevelVerticalRoad3);
        forthLevelBrickCell.addNeighbour(forthLevelVerticalRoad4);
        forthLevelWoodCell.addNeighbour(forthLevelVerticalRoad4);
        forthLevelWoodCell.addNeighbour(forthLevelVerticalRoad5);
        emptyCell.addNeighbour(tempVillage2);
        forthLevelWheatCell.addNeighbour(forthLevelCornerLeftRoad);
        forthLevelWheatCell.addNeighbour(forthLevelRightRoad);
        emptyCell.addNeighbour(forthLevelLeftRoad2);
        emptyCell.addNeighbour(forthLevelRightRoad2);
        forthLevelBrickCell.addNeighbour(forthLevelLeftRoad3);
        forthLevelBrickCell.addNeighbour(forthLevelRightRoad3);
        forthLevelWoodCell.addNeighbour(forthLevelLeftRoad4);
        forthLevelWoodCell.addNeighbour(forthLevelCornerRightRoad);

        fifthLevelStoneCell.addNeighbour(forthLevelLeftRoad2);
        fifthLevelStoneCell.addNeighbour(forthLevelRightRoad);
        fifthLevelSheepCell.addNeighbour(forthLevelLeftRoad3);
        fifthLevelSheepCell.addNeighbour(forthLevelRightRoad2);
        fifthLevelWheatCell.addNeighbour(forthLevelLeftRoad4);
        fifthLevelWheatCell.addNeighbour(forthLevelRightRoad3);
        fifthLevelStoneCell.addNeighbour(fifthLevelRightRoad);
        fifthLevelStoneCell.addNeighbour(fifthLevelLeftRoad);
        fifthLevelSheepCell.addNeighbour(fifthLevelRightRoad2);
        fifthLevelSheepCell.addNeighbour(fifthLevelLeftRoad2);
        fifthLevelWheatCell.addNeighbour(fifthLevelRightRoad3);
        fifthLevelWheatCell.addNeighbour(fifthLevelLeftRoad3);
        fifthLevelStoneCell.addNeighbour(fifthLevelVerticalRoad);
        fifthLevelStoneCell.addNeighbour(fifthLevelVerticalRoad2);
        fifthLevelSheepCell.addNeighbour(fifthLevelVerticalRoad2);
        fifthLevelSheepCell.addNeighbour(fifthLevelVerticalRoad3);
        fifthLevelWheatCell.addNeighbour(fifthLevelVerticalRoad3);
        fifthLevelWheatCell.addNeighbour(fifthLevelVerticalRoad4);

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
