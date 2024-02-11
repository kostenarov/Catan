package com.game.catan.Functionality;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.game.catan.Map.Cell.ResourceType;

public class ResourceButtonWithoutLabel {
    private final ImageButton button;
    private final ResourceType resourceType;

    public ResourceButtonWithoutLabel(ImageButton button, ResourceType resourceType) {
        this.button = button;
        this.resourceType = resourceType;
    }

    public void draw(Stage stage) {
        stage.addActor(button);
    }
}
