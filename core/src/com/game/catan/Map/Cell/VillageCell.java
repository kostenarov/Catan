package com.game.catan.Map.Cell;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import com.game.catan.player.CatanPlayer;

import java.io.IOException;
import java.io.ObjectOutputStream;

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
    public void buttonFunc(Stage stage, final ObjectOutputStream outputStream, final CatanPlayer player) {
        this.villageTexture = new Texture(texturePath);
        this.style = new ImageButton.ImageButtonStyle();
        this.style.imageUp = new TextureRegionDrawable(villageTexture);
        if(button == null) {
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

    public ImageButton getVillageButton() {
        return button;
    }
}
