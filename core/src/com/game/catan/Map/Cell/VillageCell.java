package com.game.catan.Map.Cell;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import com.game.catan.player.CatanPlayer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashSet;

public class VillageCell extends Cell {
    private int owner = 5;
    private Texture villageTexture;
    private ImageButton.ImageButtonStyle style;
    private ImageButton button;
    public VillageCell(int x, int y) {
        super(x, y);
        this.texturePath = "Villages/defaultVillage.png";
    }

    public int getOwner() {
        return owner;
    }

    @Override
    public void addNeighbour(Cell neighbour) {
        neighbours.add(neighbour);
        if(!neighbour.getNeighbours().contains(this))
            neighbour.addNeighbour(this);
    }

    @Override
    public void buttonFunc(Stage stage, final ObjectOutputStream outputStream, final CatanPlayer player) {
        if(button == null) {
            this.villageTexture = new Texture(texturePath);
            this.style = new ImageButton.ImageButtonStyle();
            this.style.imageUp = new TextureRegionDrawable(villageTexture);
            button = new ImageButton(style);
            button.addListener(new com.badlogic.gdx.scenes.scene2d.InputListener() {
                public boolean touchDown(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y, int pointer, int button) {
                    System.out.println("Button clicked");
                    if (player.getIsTurn() && owner == 5) {
                        try {
                            outputStream.writeObject("Clicked Village button:" + id);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    return true;
                }
            });
            button.setSize(35, 35);
            button.setPosition(this.getCellCords().getX(), this.getCellCords().getY());
            stage.addActor(button);
        }

    }
    @Override
    public void drawWithoutFunc(Stage stage) {
        this.villageTexture = new Texture(texturePath);
        this.style = new ImageButton.ImageButtonStyle();
        this.style.imageUp = new TextureRegionDrawable(villageTexture);
        if(button == null) {
            button = new ImageButton(style);
        }
        else {
            button.setStyle(style);
        }
        button.setSize(50, 50);
        button.setPosition(this.getCellCords().getX(), this.getCellCords().getY());
        stage.addActor(button);
    }

    public void setVillagePath(String path) {
        this.texturePath = path;
    }

    public void setOwner(int owner) {
        if(this.owner == 5) {
            this.owner = owner;
        }
        else {
            throw new RuntimeException("Village already has an owner");
        }
    }

    public boolean hasNeighbours() {
        return !getNeighbours().isEmpty();
    }

    public HashSet<ResourceCell> getResourceNeighbours() {
        HashSet<ResourceCell> resourceCells = new HashSet<>();
        for(Cell cell : getNeighbours()) {
            if(cell instanceof ResourceCell) {
                resourceCells.add((ResourceCell) cell);
            }
        }
        return resourceCells;
    }

    public ImageButton getVillageButton() {
        return button;
    }

    public void setVillage(Texture texture) {
        this.villageTexture = texture;
    }

    public String getVillagePath() {
        return texturePath;
    }
}
