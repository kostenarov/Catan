package com.game.catan.Functionality;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.game.catan.Map.Cell.ResourceType;

public class ResourceButton {
    private final Label label;
    private final ImageButton button;
    private final ResourceType resourceType;

    public ResourceButton(ImageButton button, Label label, ResourceType resourceType) {
        this.button = button;
        this.label = label;
        this.resourceType = resourceType;
    }

    public Offer draw(Stage stage, final Offer offer, final Deck deck) {
        stage.addActor(label);
        stage.addActor(button);
        return offer;
    }

    public void changeAmount(int amount) {
        label.setText(Integer.toString(amount));
    }
}
