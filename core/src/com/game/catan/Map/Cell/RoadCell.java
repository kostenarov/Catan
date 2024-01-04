package com.game.catan.Map.Cell;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.game.catan.player.CatanPlayer;

import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class RoadCell extends Cell {
    private CatanPlayer owner = null;
    private boolean isBuilt = false;
    private Texture roadTexture;
    private ImageButton.ImageButtonStyle roadStyle;
    public RoadCell(int x, int y) {
        super(x, y);
        roadTexture = new Texture("Textures/road.png");
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
    public void buttonFunc(Stage stage, ObjectOutputStream outputStream, CatanPlayer player){}

    public boolean isBuilt() {
        return owner != null;
    }

    public void setOwner(CatanPlayer owner) {
        this.owner = owner;
    }

    public CatanPlayer getOwner() {
        return owner;
    }
}
