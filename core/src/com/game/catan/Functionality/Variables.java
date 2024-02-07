package com.game.catan.Functionality;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import com.game.catan.Map.Cell.ResourceType;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class Variables {
    private TextButton endTurnButton;
    private TextButton diceThrowButton;
    private List<ImageButton> cards;
    private List<Label> cardsAmount;
    ObjectInputStream inputStream;
    ObjectOutputStream outputStream;
    boolean isTurn = false;
    boolean isDiceThrown = false;

    public Variables(ObjectInputStream inputStream, ObjectOutputStream outputStream) {
    }


    private Label setUpLabel(String path, int x, int y, int amount, Stage stage) {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = new com.badlogic.gdx.graphics.g2d.BitmapFont();
        labelStyle.font.getData().setScale(2f);
        labelStyle.fontColor = Color.SALMON;
        Label resourceLabel = new Label(Integer.toString(amount), labelStyle);
        resourceLabel.setPosition(x + 25, y - 50);
        return resourceLabel;
    }

    public void setupLabels(Stage stage, Deck deck) {
        cardsAmount.add(setUpLabel("Cards/brickCard.png", 500, 100, deck.getResourceAmount(ResourceType.BRICK), stage));
        cardsAmount.add(setUpLabel("Cards/wheatCard.png", 600, 100, deck.getResourceAmount(ResourceType.WHEAT), stage));
        cardsAmount.add(setUpLabel("Cards/woodCard.png", 700, 100, deck.getResourceAmount(ResourceType.WOOD), stage));
        cardsAmount.add(setUpLabel("Cards/sheepCard.png", 800, 100, deck.getResourceAmount(ResourceType.SHEEP), stage));
        cardsAmount.add(setUpLabel("Cards/stoneCard.png", 900, 100, deck.getResourceAmount(ResourceType.STONE), stage));
        for (Label label : cardsAmount) {
            stage.addActor(label);
        }
    }

}
