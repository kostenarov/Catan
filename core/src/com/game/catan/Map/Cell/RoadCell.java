package com.game.catan.Map.Cell;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.game.catan.player.CatanPlayer;

import java.util.ArrayList;

public class RoadCell extends Cell {
    private CatanPlayer owner = null;
    private boolean isBuilt = false;
    private Texture roadTexture;
    private ImageButton.ImageButtonStyle roadStyle;
    public RoadCell(int x, int y) {
        super(x, y);
    }

    public void setRoad(String path) {
        if(owner == null) {
            roadTexture = new Texture(path);
            roadStyle.imageUp = new TextureRegionDrawable(roadTexture);
        }
        else {
            roadTexture = null;
            roadStyle = new ImageButton.ImageButtonStyle();
            roadStyle.imageUp = new TextureRegionDrawable(roadTexture);
        }
    }

    public ArrayList<VillageCell> getVillages() {
        ArrayList<VillageCell> neighbours = new ArrayList<>();
        for(Cell neighbour : getNeighbours()) {
            if(neighbour instanceof VillageCell) {
                neighbours.add((VillageCell) neighbour);
            }
        }
        return neighbours;
    }

    @Override
    public void buttonFunc(Stage stage){}

    public boolean isBuilt() {
        if(owner == null) {
            return false;
        }
        return true;
    }

    public void setOwner(CatanPlayer owner) {
        this.owner = owner;
    }

    public CatanPlayer getOwner() {
        return owner;
    }
}
