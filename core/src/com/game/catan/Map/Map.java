package com.game.catan.Map;

import com.game.catan.Map.Cell.ResourceType;
import com.game.catan.Map.Cell.resourceCell;

public class Map {
    private resourceCell robberCell;
    private resourceCell centerCell;
    public Map() {
        centerCell = new resourceCell(0, 0, ResourceType.BRICK);
        centerCell.addNeighbour(new resourceCell(1, 0, ResourceType.WOOD));
    }

}
