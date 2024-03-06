package com.game.catan.Map.Cell;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.game.catan.player.CatanPlayer;

import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class RoadCell extends Cell {
    private int owner = 5;
    private boolean isBuilt = false;
    private ImageButton button;
    private final int rotation;
    public RoadCell(int x, int y, int rotation) {
        super(x, y);
        this.texturePath = "Roads/defaultNormalRoad.png";
        this.rotation = rotation;
    }

    @Override
    public void addNeighbour(Cell neighbour) {
        neighbours.add(neighbour);
        if(!neighbour.getNeighbours().contains(this))
            neighbour.addNeighbour(this);
        if(neighbour instanceof ResourceCell && getVillages().size() == 2) {
            VillageCell firstVillage = getVillages().get(0);
            neighbour.addNeighbour(firstVillage);
            neighbour.addNeighbour(getOtherVillage(firstVillage));
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
    public void buttonFunc(Stage stage, final ObjectOutputStream outputStream, final CatanPlayer player){
        if(button == null) {
            Texture roadTexture = new Texture(texturePath);
            ImageButton.ImageButtonStyle roadStyle = new ImageButton.ImageButtonStyle();
            roadStyle.imageUp = new TextureRegionDrawable(roadTexture);
            button = new ImageButton(roadStyle);

            if (!isBuilt) {
                button.addListener(new InputListener() {
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
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
            button.setPosition(this.getCellCords().getX(), this.getCellCords().getY());
            button.setTransform(true);
            button.setRotation(rotation);
            stage.addActor(button);
        }
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

    public String getRoadTexture() {
        return texturePath;
    }
}
