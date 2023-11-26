package com.game.catan.Villages;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.game.catan.player.catanPlayer;

public class Village {
    private Texture villageTexture;
    private ImageButton.ImageButtonStyle villageStyle;
    private catanPlayer owner;


    public Village(String path) {
        setVillage(path);
    }
    public void setVillage(String path) {
        villageTexture = new Texture(path);
        villageStyle = new ImageButton.ImageButtonStyle();
        villageStyle.imageUp = new TextureRegionDrawable(villageTexture);
    }


    public void dispose() {
        villageTexture.dispose();
    }

    public ImageButton.ImageButtonStyle getVillageStyle() {
        return villageStyle;
    }

    public Texture getVillageTexture() {
        return villageTexture;
    }
}
