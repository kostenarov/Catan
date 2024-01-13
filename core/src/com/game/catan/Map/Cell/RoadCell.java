package com.game.catan.Map.Cell;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.game.catan.player.CatanPlayer;

import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class RoadCell extends Cell {
    private int owner = 5;
    private boolean isBuilt = false;
    private Texture roadTexture;
    private ImageButton.ImageButtonStyle roadStyle;
    private ImageButton button;
    public RoadCell(int x, int y) {
        super(x, y);
        this.texturePath = "Roads/defaultNormalRoad.png";
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
    public void buttonFunc(Stage stage, final ObjectOutputStream outputStream, final CatanPlayer player){
        this.roadTexture = new Texture(texturePath);
        this.roadStyle = new ImageButton.ImageButtonStyle();
        this.roadStyle.imageUp = new com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable(roadTexture);
        button = new ImageButton(roadStyle);

        if(!isBuilt) {
            button.addListener(new com.badlogic.gdx.scenes.scene2d.InputListener() {
                public boolean touchDown(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y, int pointer, int button) {
                    System.out.println("Button clicked");
                    try {
                        outputStream.writeObject("Clicked Road button:" + id);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    return true;
                }
            });
        }
        button.setSize(50, 50);
        button.setPosition(this.getCellCords().getX(), this.getCellCords().getY());
        stage.addActor(button);
    }

    @Override
    public void drawWithoutFunc(Stage stage) {

    }

    public void setRoadTexture(String texturePath) {
        this.texturePath = texturePath;
    }

    public boolean isBuilt() {
        return isBuilt;
    }

    public void setOwner(int owner) {
        this.owner = owner;
        isBuilt = true;
    }

    public int getOwner() {
        return owner;
    }

    public VillageCell getOtherVillage(VillageCell village) {
        for(Cell neighbour : getNeighbours()) {
            if(neighbour instanceof VillageCell && neighbour != village) {
                return (VillageCell) neighbour;
            }
        }
        return null;
    }
}
