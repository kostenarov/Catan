package com.game.catan.Map.Cell;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import com.game.catan.player.CatanPlayer;

import java.io.ObjectOutputStream;

public class VillageCell extends Cell {
    public CatanPlayer owner = null;
    private Texture villageTexture;
    private TextureRegion villageRegion;
    private ImageButton.ImageButtonStyle villageStyle;
    private ImageButton villageButton;
    public VillageCell(int x, int y) {
        super(x, y);
    }

    public CatanPlayer getOwner() {
        return owner;
    }

    @Override
    public void buttonFunc(Stage stage, ObjectOutputStream outputStream) {
        villageTexture = new Texture("button.png"); // Replace with your button texture
        TextureRegion buttonTextureRegion = new TextureRegion(villageTexture);

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.imageUp = new TextureRegionDrawable(buttonTextureRegion);

        ImageButton button = new ImageButton(style);
        button.setPosition(100, 100);
        button.setSize(200, 100);

        stage.addActor(button);
        button.addListener(new com.badlogic.gdx.scenes.scene2d.InputListener() {
            public boolean touchDown(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Button clicked");
                return true;
            }
        });
    }

    public void drawVillage(Batch batch) {
        batch.draw(villageTexture, this.getCellCords().getX(), this.getCellCords().getY());
    }

    public void setOwner(CatanPlayer owner) {
        if(this.owner == null) {
            this.owner = owner;
            villageTexture = new Texture(owner.getVillagePath());
            villageStyle.imageUp = new TextureRegionDrawable(villageTexture);
            villageButton.setStyle(villageStyle);
        }
        else {
            throw new RuntimeException("Village already has an owner");
        }
    }

    public boolean hasNeighbours() {
        return !getNeighbours().isEmpty();
    }

    public ImageButton getVillageButton() {
        return villageButton;
    }
}
