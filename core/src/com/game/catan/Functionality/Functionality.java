package com.game.catan.Functionality;

import com.game.catan.Map.Cell.ResourceCell;
import com.game.catan.Map.Cell.ResourceType;
import com.game.catan.Map.Map;
import com.game.catan.player.CatanPlayer;

import java.util.HashMap;

public class Functionality {
    public int diceThrow() {
        return (int) (Math.random() * 10 + 2);
    }

    public HashMap<ResourceType, Integer> getResources(int diceThrow, Map map, HashMap<ResourceType, Integer> resources, CatanPlayer player) {
        for(ResourceCell cell : map.getResourceCells(map.getCenterCell())) {
            if(!cell.HasRobber()) {
                System.out.println(cell.getResource());
                resources.put(ResourceType.valueOf(cell.getResource()), resources.get(ResourceType.valueOf(cell.getResource())) + cell.getNumberOfPlayerVillages(player));
            }
        }
        return resources;
    }
}
