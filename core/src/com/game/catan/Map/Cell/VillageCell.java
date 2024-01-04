package com.game.catan.Map.Cell;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import com.game.catan.player.CatanPlayer;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class VillageCell extends Cell {
    public CatanPlayer owner = null;
    private Texture villageTexture;
    private ImageButton.ImageButtonStyle style;
    private ImageButton button;
    public VillageCell(int x, int y) {
        super(x, y);
        texturePath = "Villages/defaultVillage.png";
    }

    public CatanPlayer getOwner() {
        return owner;
    }

    @Override
    public void buttonFunc(Stage stage, final ObjectOutputStream outputStream, final CatanPlayer player) {
        style = new ImageButton.ImageButtonStyle();
        if(owner != null) {
            villageTexture = new Texture(owner.getVillagePath());
        }
        else {
            villageTexture = new Texture(texturePath);
        }
        style.imageUp = new TextureRegionDrawable(villageTexture);
        button = new ImageButton(style);
        button.setPosition(this.getCellCords().getX(), this.getCellCords().getY());
        button.setSize(100, 100);
        stage.addActor(button);
        button.addListener(new com.badlogic.gdx.scenes.scene2d.InputListener() {
            public boolean touchDown(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y, int pointer, int button) {
                if(player.getIsTurn()) {
                    System.out.println("Button clicked");
                    try {
                        outputStream.writeObject("Village button clicked" + id);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                return true;
            }
        });
    }


    public void setVillagePath(String path) {
        this.texturePath = path;
    }

    public void setOwner(CatanPlayer owner) {
        if(this.owner == null) {
            this.owner = owner;
            villageTexture = new Texture(owner.getVillagePath());
            style.imageUp = new TextureRegionDrawable(villageTexture);
            button.setStyle(style);
        }
        else {
            throw new RuntimeException("Village already has an owner");
        }
    }

    public boolean hasNeighbours() {
        return !getNeighbours().isEmpty();
    }

    public ImageButton getVillageButton() {
        return button;
    }
}
