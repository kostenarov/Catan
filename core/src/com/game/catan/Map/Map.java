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

        VillageCell firstLevelVillage = new VillageCell(663, 460);
        VillageCell firstLevelVillage2 = new VillageCell(733, 437);
        VillageCell firstLevelVillage3 = new VillageCell(803, 460);
        VillageCell firstLevelVillage4 = new VillageCell(873, 437);
        VillageCell firstLevelVillage5 = new VillageCell(943, 460);
        VillageCell firstLevelVillage6 = new VillageCell(1013, 437);
        VillageCell firstLevelVillage7 = new VillageCell(1083, 460);
        VillageCell firstLevelVillage8 = new VillageCell(1153, 437);
        VillageCell firstLevelVillage9 = new VillageCell(1223, 460);

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
        RoadCell secondLevelLeftCornerRoad = new RoadCell(660, 550, 63); //12
        RoadCell secondLevelRightRoad = new RoadCell(690, 567, -63);//23
        RoadCell secondLevelLeftRoad2 = new RoadCell(800, 550, 63);//34
        RoadCell secondLevelRightRoad2 = new RoadCell(830, 567, -63);//45
        RoadCell secondLevelLeftRoad3 = new RoadCell(940, 550, 63);//56
        RoadCell secondLevelRightRoad3 = new RoadCell(970, 567, -63);//67
        RoadCell secondLevelLeftRoad4 = new RoadCell(1080, 550, 63);//78
        RoadCell secondLevelRightRoad4 = new RoadCell(1110, 567, -63);//89
        RoadCell secondLevelLeftRoad5 = new RoadCell(1220, 550, 63);//9
        RoadCell secondLevelRightCornerRoad = new RoadCell(1250, 567, -63);

        VillageCell secondLevelVillage = new VillageCell(593, 568);
        VillageCell secondLevelVillage2 = new VillageCell(663, 545);
        VillageCell secondLevelVillage3 = new VillageCell(733, 568);
        VillageCell secondLevelVillage4 = new VillageCell(803, 545);
        VillageCell secondLevelVillage5 = new VillageCell(873, 568);
        VillageCell secondLevelVillage6 = new VillageCell(943, 545);
        VillageCell secondLevelVillage7 = new VillageCell(1013, 568);
        VillageCell secondLevelVillage8 = new VillageCell(1083, 545);
        VillageCell secondLevelVillage9 = new VillageCell(1153, 568);
        VillageCell secondLevelVillage10 = new VillageCell(1223, 545);
        VillageCell secondLevelVillage11 = new VillageCell(1293, 568);

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

        VillageCell thirdLevelVillage = new VillageCell(593, 647);
        VillageCell thirdLevelVillage2 = new VillageCell(663, 670);
        VillageCell thirdLevelVillage3 = new VillageCell(733, 647);
        VillageCell thirdLevelVillage4 = new VillageCell(803, 670);
        VillageCell thirdLevelVillage5 = new VillageCell(873, 647);
        VillageCell thirdLevelVillage6 = new VillageCell(943, 670);
        VillageCell thirdLevelVillage7 = new VillageCell(1013, 647);
        VillageCell thirdLevelVillage8 = new VillageCell(1083, 670);
        VillageCell thirdLevelVillage9 = new VillageCell(1153, 647);
        VillageCell thirdLevelVillage10 = new VillageCell(1223, 670);
        VillageCell thirdLevelVillage11 = new VillageCell(1293, 647);


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
        VillageCell forthLevelVillage = new VillageCell(663, 749);
        VillageCell forthLevelVillage2 = new VillageCell(733, 772);
        VillageCell forthLevelVillage3 = new VillageCell(803, 749);
        VillageCell forthLevelVillage4 = new VillageCell(873, 772);
        VillageCell forthLevelVillage5 = new VillageCell(943, 749);
        VillageCell forthLevelVillage6 = new VillageCell(1013, 772);
        VillageCell forthLevelVillage7 = new VillageCell(1083, 749);
        VillageCell forthLevelVillage8 = new VillageCell(1153, 772);
        VillageCell forthLevelVillage9 = new VillageCell(1223, 749);


        //------------Fifth row------------
        ResourceCell fifthLevelStoneCell = new ResourceCell(770, 780, ResourceType.STONE);
        ResourceCell fifthLevelSheepCell = new ResourceCell(910, 780, ResourceType.SHEEP);
        ResourceCell fifthLevelWheatCell = new ResourceCell(1050, 780, ResourceType.WHEAT);

        RoadCell fifthLevelRightCornerRoad = new RoadCell(760, 882, -63);
        RoadCell fifthLevelLeftRoad = new RoadCell(870, 865, 63);
        RoadCell fifthLevelRightRoad2 = new RoadCell(900, 882, -63);
        RoadCell fifthLevelLeftRoad2 = new RoadCell(1010, 865, 63);
        RoadCell fifthLevelRightRoad3 = new RoadCell(1040, 882, -63);
        RoadCell fifthLevelLeftCornerRoad = new RoadCell(1150, 865, 63);

        RoadCell fifthLevelVerticalRoad = new RoadCell(740, 810, 0);
        RoadCell fifthLevelVerticalRoad2 = new RoadCell(880, 810, 0);
        RoadCell fifthLevelVerticalRoad3 = new RoadCell(1020, 810, 0);
        RoadCell fifthLevelVerticalRoad4 = new RoadCell(1160, 810, 0);

        VillageCell fifthLevelVillage = new VillageCell(733, 851);
        VillageCell fifthLevelVillage2 = new VillageCell(803, 874);
        VillageCell fifthLevelVillage3 = new VillageCell(873, 851);
        VillageCell fifthLevelVillage4 = new VillageCell(943, 874);
        VillageCell fifthLevelVillage5 = new VillageCell(1013, 851);
        VillageCell fifthLevelVillage6 = new VillageCell(1083, 874);
        VillageCell fifthLevelVillage7 = new VillageCell(1153, 851);

        emptyCell.setRobber(true);
        //-------Bottom row Villages--------
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

        //-------First row Villages--------
        secondLevelVerticalRoad.addNeighbour(firstLevelVillage);
        firstLevelLeftCornerRoad.addNeighbour(firstLevelVillage);
        firstLevelLeftCornerRoad.addNeighbour(firstLevelVillage2);
        firstLevelVerticalRoad.addNeighbour(firstLevelVillage2);
        firstLevelRightRoad.addNeighbour(firstLevelVillage2);
        firstLevelRightRoad.addNeighbour(firstLevelVillage3);
        secondLevelVerticalRoad2.addNeighbour(firstLevelVillage3);
        firstLevelLeftRoad2.addNeighbour(firstLevelVillage3);
        firstLevelLeftRoad2.addNeighbour(firstLevelVillage4);
        firstLevelVerticalRoad2.addNeighbour(firstLevelVillage4);
        firstLevelRightRoad2.addNeighbour(firstLevelVillage4);
        firstLevelRightRoad2.addNeighbour(firstLevelVillage5);
        secondLevelVerticalRoad3.addNeighbour(firstLevelVillage5);
        firstLevelLeftRoad3.addNeighbour(firstLevelVillage5);
        firstLevelLeftRoad3.addNeighbour(firstLevelVillage6);
        firstLevelVerticalRoad3.addNeighbour(firstLevelVillage6);
        firstLevelRightRoad3.addNeighbour(firstLevelVillage6);
        firstLevelRightRoad3.addNeighbour(firstLevelVillage7);
        secondLevelVerticalRoad4.addNeighbour(firstLevelVillage7);
        firstLevelLeftRoad4.addNeighbour(firstLevelVillage7);
        firstLevelLeftRoad4.addNeighbour(firstLevelVillage8);
        firstLevelVerticalRoad4.addNeighbour(firstLevelVillage8);
        firstLevelRightCornerRoad.addNeighbour(firstLevelVillage8);
        firstLevelRightCornerRoad.addNeighbour(firstLevelVillage9);
        secondLevelVerticalRoad5.addNeighbour(firstLevelVillage9);

        //-------Second row Villages--------
        thirdLevelVerticalRoad.addNeighbour(secondLevelVillage);
        secondLevelLeftCornerRoad.addNeighbour(secondLevelVillage);
        secondLevelLeftCornerRoad.addNeighbour(secondLevelVillage2);
        secondLevelVerticalRoad.addNeighbour(secondLevelVillage2);
        secondLevelRightRoad.addNeighbour(secondLevelVillage2);
        secondLevelRightRoad.addNeighbour(secondLevelVillage3);
        thirdLevelVerticalRoad2.addNeighbour(secondLevelVillage3);
        secondLevelLeftRoad2.addNeighbour(secondLevelVillage3);
        secondLevelLeftRoad2.addNeighbour(secondLevelVillage4);
        secondLevelVerticalRoad2.addNeighbour(secondLevelVillage4);
        secondLevelRightRoad2.addNeighbour(secondLevelVillage4);
        secondLevelRightRoad2.addNeighbour(secondLevelVillage5);
        thirdLevelVerticalRoad3.addNeighbour(secondLevelVillage5);
        secondLevelLeftRoad3.addNeighbour(secondLevelVillage5);
        secondLevelLeftRoad3.addNeighbour(secondLevelVillage6);
        secondLevelVerticalRoad3.addNeighbour(secondLevelVillage6);
        secondLevelRightRoad3.addNeighbour(secondLevelVillage6);
        secondLevelRightRoad3.addNeighbour(secondLevelVillage7);
        thirdLevelVerticalRoad4.addNeighbour(secondLevelVillage7);
        secondLevelLeftRoad4.addNeighbour(secondLevelVillage7);
        secondLevelLeftRoad4.addNeighbour(secondLevelVillage8);
        secondLevelVerticalRoad4.addNeighbour(secondLevelVillage8);
        secondLevelLeftRoad4.addNeighbour(secondLevelVillage8);
        secondLevelLeftRoad4.addNeighbour(secondLevelVillage9);
        thirdLevelVerticalRoad5.addNeighbour(secondLevelVillage9);
        secondLevelLeftRoad5.addNeighbour(secondLevelVillage9);
        secondLevelLeftRoad5.addNeighbour(secondLevelVillage10);
        secondLevelVerticalRoad5.addNeighbour(secondLevelVillage10);
        secondLevelRightCornerRoad.addNeighbour(secondLevelVillage10);
        secondLevelRightCornerRoad.addNeighbour(secondLevelVillage11);
        thirdLevelVerticalRoad6.addNeighbour(secondLevelVillage11);

        //-------Third row Villages--------
        thirdLevelVerticalRoad.addNeighbour(thirdLevelVillage);
        thirdLevelCornerRightRoad.addNeighbour(thirdLevelVillage);
        thirdLevelCornerRightRoad.addNeighbour(thirdLevelVillage2);
        forthLevelVerticalRoad.addNeighbour(thirdLevelVillage2);
        thirdLevelLeftRoad.addNeighbour(thirdLevelVillage2);
        thirdLevelLeftRoad.addNeighbour(thirdLevelVillage3);
        thirdLevelVerticalRoad2.addNeighbour(thirdLevelVillage3);
        thirdLevelRightRoad.addNeighbour(thirdLevelVillage3);
        thirdLevelRightRoad.addNeighbour(thirdLevelVillage4);
        forthLevelVerticalRoad2.addNeighbour(thirdLevelVillage4);
        thirdLevelLeftRoad2.addNeighbour(thirdLevelVillage4);
        thirdLevelLeftRoad2.addNeighbour(thirdLevelVillage5);
        thirdLevelVerticalRoad3.addNeighbour(thirdLevelVillage5);
        thirdLevelRightRoad2.addNeighbour(thirdLevelVillage5);
        thirdLevelRightRoad2.addNeighbour(thirdLevelVillage6);
        forthLevelVerticalRoad3.addNeighbour(thirdLevelVillage6);
        thirdLevelLeftRoad3.addNeighbour(thirdLevelVillage6);
        thirdLevelLeftRoad3.addNeighbour(thirdLevelVillage7);
        thirdLevelVerticalRoad4.addNeighbour(thirdLevelVillage7);
        thirdLevelRightRoad3.addNeighbour(thirdLevelVillage7);
        thirdLevelRightRoad3.addNeighbour(thirdLevelVillage8);
        forthLevelVerticalRoad4.addNeighbour(thirdLevelVillage8);
        thirdLevelLeftRoad4.addNeighbour(thirdLevelVillage8);
        thirdLevelLeftRoad4.addNeighbour(thirdLevelVillage9);
        thirdLevelVerticalRoad5.addNeighbour(thirdLevelVillage9);
        thirdLevelRightRoad4.addNeighbour(thirdLevelVillage9);
        thirdLevelRightRoad4.addNeighbour(thirdLevelVillage10);
        forthLevelVerticalRoad5.addNeighbour(thirdLevelVillage10);
        thirdLevelCornerLeftRoad.addNeighbour(thirdLevelVillage10);
        thirdLevelCornerLeftRoad.addNeighbour(thirdLevelVillage11);
        thirdLevelVerticalRoad6.addNeighbour(thirdLevelVillage11);

        //-------Fourth row Villages--------
        forthLevelVerticalRoad.addNeighbour(forthLevelVillage);
        forthLevelCornerLeftRoad.addNeighbour(forthLevelVillage);
        forthLevelCornerLeftRoad.addNeighbour(forthLevelVillage2);
        fifthLevelVerticalRoad.addNeighbour(forthLevelVillage2);
        forthLevelRightRoad.addNeighbour(forthLevelVillage2);
        forthLevelRightRoad.addNeighbour(forthLevelVillage3);
        forthLevelVerticalRoad2.addNeighbour(forthLevelVillage3);
        forthLevelLeftRoad2.addNeighbour(forthLevelVillage3);
        forthLevelLeftRoad2.addNeighbour(forthLevelVillage4);
        fifthLevelVerticalRoad2.addNeighbour(forthLevelVillage4);
        forthLevelRightRoad2.addNeighbour(forthLevelVillage4);
        forthLevelRightRoad2.addNeighbour(forthLevelVillage5);
        forthLevelVerticalRoad3.addNeighbour(forthLevelVillage5);
        forthLevelLeftRoad3.addNeighbour(forthLevelVillage5);
        forthLevelLeftRoad3.addNeighbour(forthLevelVillage6);
        fifthLevelVerticalRoad3.addNeighbour(forthLevelVillage6);
        forthLevelRightRoad3.addNeighbour(forthLevelVillage6);
        forthLevelRightRoad3.addNeighbour(forthLevelVillage7);
        forthLevelVerticalRoad4.addNeighbour(forthLevelVillage7);
        forthLevelLeftRoad4.addNeighbour(forthLevelVillage7);
        forthLevelLeftRoad4.addNeighbour(forthLevelVillage8);
        fifthLevelVerticalRoad4.addNeighbour(forthLevelVillage8);
        forthLevelCornerRightRoad.addNeighbour(forthLevelVillage8);
        forthLevelCornerRightRoad.addNeighbour(forthLevelVillage9);
        forthLevelVerticalRoad5.addNeighbour(forthLevelVillage9);
        fifthLevelVerticalRoad.addNeighbour(fifthLevelVillage);

        //-------Fifth row Villages--------
        fifthLevelRightCornerRoad.addNeighbour(fifthLevelVillage);
        fifthLevelRightCornerRoad.addNeighbour(fifthLevelVillage2);
        fifthLevelLeftRoad.addNeighbour(fifthLevelVillage2);
        fifthLevelLeftRoad.addNeighbour(fifthLevelVillage3);
        fifthLevelVerticalRoad2.addNeighbour(fifthLevelVillage3);
        fifthLevelRightRoad2.addNeighbour(fifthLevelVillage3);
        fifthLevelRightRoad2.addNeighbour(fifthLevelVillage4);
        fifthLevelLeftRoad2.addNeighbour(fifthLevelVillage4);
        fifthLevelLeftRoad2.addNeighbour(fifthLevelVillage5);
        fifthLevelVerticalRoad3.addNeighbour(fifthLevelVillage5);
        fifthLevelRightRoad3.addNeighbour(fifthLevelVillage5);
        fifthLevelRightRoad3.addNeighbour(fifthLevelVillage6);
        fifthLevelLeftCornerRoad.addNeighbour(fifthLevelVillage6);
        fifthLevelLeftCornerRoad.addNeighbour(fifthLevelVillage7);
        fifthLevelVerticalRoad4.addNeighbour(fifthLevelVillage7);

        //-------First row resources--------
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

        //-------Second row resources--------
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

        //-------Third row resources--------
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

        //-------Fourth row resources--------
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
        forthLevelWheatCell.addNeighbour(forthLevelCornerLeftRoad);
        forthLevelWheatCell.addNeighbour(forthLevelRightRoad);
        emptyCell.addNeighbour(forthLevelLeftRoad2);
        emptyCell.addNeighbour(forthLevelRightRoad2);
        forthLevelBrickCell.addNeighbour(forthLevelLeftRoad3);
        forthLevelBrickCell.addNeighbour(forthLevelRightRoad3);
        forthLevelWoodCell.addNeighbour(forthLevelLeftRoad4);
        forthLevelWoodCell.addNeighbour(forthLevelCornerRightRoad);

        //-------Fifth row resources--------
        fifthLevelStoneCell.addNeighbour(forthLevelLeftRoad2);
        fifthLevelStoneCell.addNeighbour(forthLevelRightRoad);
        fifthLevelSheepCell.addNeighbour(forthLevelLeftRoad3);
        fifthLevelSheepCell.addNeighbour(forthLevelRightRoad2);
        fifthLevelWheatCell.addNeighbour(forthLevelLeftRoad4);
        fifthLevelWheatCell.addNeighbour(forthLevelRightRoad3);
        fifthLevelStoneCell.addNeighbour(fifthLevelRightCornerRoad);
        fifthLevelStoneCell.addNeighbour(fifthLevelLeftRoad);
        fifthLevelSheepCell.addNeighbour(fifthLevelRightRoad2);
        fifthLevelSheepCell.addNeighbour(fifthLevelLeftRoad2);
        fifthLevelWheatCell.addNeighbour(fifthLevelRightRoad3);
        fifthLevelWheatCell.addNeighbour(fifthLevelLeftCornerRoad);
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
        HashSet<Cell> map = getMap(centerCell, new HashSet<Cell>());
        for(Cell cell : map) {
            if(cell instanceof ResourceCell) {
                if(((ResourceCell) cell).hasRobber()) {
                    return (ResourceCell) cell;
                }
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
        robberCell = getResourceCellById(resourceCellId);
        System.out.println("Robber moved to " + getResourceCellById(resourceCellId).getResourceType());
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

    public HashSet<VillageCell> getVillagesByPlayerId(int playerId) {
        HashSet<VillageCell> villages = new HashSet<>();
        for(Cell cell : getMap(centerCell, new HashSet<Cell>())) {
            if(cell instanceof VillageCell) {
                if(((VillageCell) cell).getOwner() == playerId) {
                    villages.add((VillageCell) cell);
                }
            }
        }
        return villages;
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
