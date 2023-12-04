package com.game.catan.Map.Cell;
import com.game.catan.player.CatanPlayer;

public class VillageCell extends Cell {
    public CatanPlayer owner = null;
    public VillageCell(int x, int y) {
        super(x, y);
    }

    public CatanPlayer getOwner() {
        return owner;
    }

    public void setOwner(CatanPlayer owner) {
        if(this.owner == null)
            this.owner = owner;
        else {
            throw new RuntimeException("Village already has an owner");
        }
    }

    public boolean hasNeighbours() {
        return !getNeighbours().isEmpty();
    }
}
