package com.game.catan.Functionality;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class ResourceDisplay {
    private final Label label;
    private final Image texture;

    public ResourceDisplay(Image texture, Label label) {
        this.texture = texture;
        this.label = label;
    }

    public void draw(Stage stage) {
        stage.addActor(label);
        stage.addActor(texture);
    }

    public void draw(Table table) {
        table.add(texture);
        table.add(label);
    }

    public void changeAmount(int amount) {
        label.setText(Integer.toString(amount));
    }

    public int getLabelAmount() {
        return Integer.parseInt(label.getText().toString());
    }
}
