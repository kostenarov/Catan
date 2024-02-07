package com.game.catan.Functionality;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class ResourceDisplay {
    private final Label label;
    private final Image texture;

    public ResourceDisplay(Image texture, Label label) {
        this.texture = texture;
        this.label = label;
    }

    public void draw(Stage stage, Batch batch) {
        stage.addActor(label);
        stage.addActor(texture);
    }

    public void changeAmount(int amount) {
        label.setText(Integer.toString(amount));
    }
}
