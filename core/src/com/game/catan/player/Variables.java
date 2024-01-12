package com.game.catan.player;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.game.catan.Functionality.Deck;
import com.game.catan.Functionality.Functionality;
import com.game.catan.Map.Cell.ResourceType;

import java.io.IOException;
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
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        endTurnButton = setUpTextButton("End Turn", 1720, 0);
        diceThrowButton = setUpTextButton("Throw Dice", 1720, 100);

    }

    private TextButton setUpTextButton(String text, int x, int y) {
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = new com.badlogic.gdx.graphics.g2d.BitmapFont();
        textButtonStyle.font.getData().setScale(2f);
        textButtonStyle.fontColor = com.badlogic.gdx.graphics.Color.BLACK;
        Texture buttonTexture = new Texture("Textures/button.png");
        textButtonStyle.up = new TextureRegionDrawable(buttonTexture);
        textButtonStyle.down = new TextureRegionDrawable(buttonTexture);

        TextButton button = new TextButton(text, textButtonStyle);
        button.setPosition(x, y);
        button.setSize(200, 100);
        return button;
    }

    public void endTurnButton(Stage stage) {
        endTurnButton = setUpTextButton("End Turn", 1720, 0);
        Functionality.setUpButtonFunc(stage, endTurnButton, isTurn, outputStream);
    }

    public void diceThrowButton(Stage stage, final CatanPlayer player) {
        diceThrowButton = setUpTextButton("Throw Dice", 1720, 100);
        stage.addActor(diceThrowButton);
        diceThrowButton.addListener(new com.badlogic.gdx.scenes.scene2d.InputListener() {
            public boolean touchDown(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y, int pointer, int button) {
                try {
                    if(isTurn && !isDiceThrown) {
                        System.out.println("Dice thrown");
                        outputStream.writeObject("Dice Throw");
                        outputStream.reset();
                        player.setDiceThrown(true);
                    }
                } catch (IOException e) {
                    System.out.println("Could not send dice throw");
                }
                return true;
            }
        });
    }

    static ImageButton setUpImageButton(final String path, int x, int y, int amount, ResourceType type, Stage stage, final boolean isTurn) {
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.imageUp = new TextureRegionDrawable(new Texture(path));
        ImageButton button = new ImageButton(style);
        button.addListener(new com.badlogic.gdx.scenes.scene2d.InputListener() {
            public boolean touchDown(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y, int pointer, int button) {
                if(isTurn) {
                    System.out.println("Button clicked" + path);
                }
                return true;
            }
        });

        button.setPosition(x, y);
        button.setSize(60, 100);
        return button;
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

    public void displayResources(Stage stage, Deck deck, boolean isTurn) {
        cards.add(setUpImageButton("Cards/brickCard.png", 500, 100, deck.getResourceAmount(ResourceType.BRICK), ResourceType.BRICK, stage, isTurn));
        cards.add(setUpImageButton("Cards/wheatCard.png", 600, 100, deck.getResourceAmount(ResourceType.WHEAT), ResourceType.BRICK, stage, isTurn));
        cards.add(setUpImageButton("Cards/woodCard.png", 700, 100, deck.getResourceAmount(ResourceType.WOOD), ResourceType.BRICK, stage, isTurn));
        cards.add(setUpImageButton("Cards/sheepCard.png", 800, 100, deck.getResourceAmount(ResourceType.SHEEP), ResourceType.BRICK, stage, isTurn));
        cards.add(setUpImageButton("Cards/stoneCard.png", 900, 100, deck.getResourceAmount(ResourceType.STONE), ResourceType.BRICK, stage, isTurn));
        for (ImageButton card : cards) {
            stage.addActor(card);
        }
    }
}
