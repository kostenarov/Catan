package com.game.catan.server.Villages;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Village {
    private Texture villageTexture;
    private ImageButton.ImageButtonStyle villageStyle;


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
