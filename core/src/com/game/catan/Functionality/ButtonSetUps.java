package com.game.catan.Functionality;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class ButtonSetUps {
    public static TextButton setUpTextButton(String text, int y) {
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = new com.badlogic.gdx.graphics.g2d.BitmapFont();
        textButtonStyle.font.getData().setScale(2f);
        textButtonStyle.fontColor = com.badlogic.gdx.graphics.Color.BLACK;
        Texture buttonTextureUp = new Texture("Textures/buttonUp.png");
        Texture buttonTextureDown = new Texture("Textures/buttonDown.png");
        textButtonStyle.up = new TextureRegionDrawable(buttonTextureUp);
        textButtonStyle.down = new TextureRegionDrawable(buttonTextureDown);

        TextButton button = new TextButton(text, textButtonStyle);
        button.setPosition(1720, y);
        button.setSize(200, 100);
        return button;
    }

    public static ImageButton.ImageButtonStyle setUpCloseButton() {
        ImageButton.ImageButtonStyle imageButtonStyle = new ImageButton.ImageButtonStyle();
        TextureRegionDrawable textureRegionDrawable = new TextureRegionDrawable(new Texture("Textures/xButtonUnclicked.png"));
        TextureRegionDrawable textureRegionDrawableClicked = new TextureRegionDrawable(new Texture("Textures/xButtonClicked.png"));
        imageButtonStyle.imageUp = textureRegionDrawable;
        imageButtonStyle.imageDown = textureRegionDrawableClicked;

        return imageButtonStyle;
    }

    public static TextButton setUpEndTurnButtonFunc(TextButton endTurnButton, final ObjectOutputStream outputStream) {
        endTurnButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                try {
                    outputStream.writeObject("End Turn");
                    outputStream.reset();
                } catch (IOException e) {
                    System.out.println("Could not send end turn");
                }
                return true;
            }
        });
        return endTurnButton;
    }

    public static TextButton setUpDiceThrowButtonFunc(TextButton diceThrowButton, final ObjectOutputStream outputStream) {
        diceThrowButton.addListener(new com.badlogic.gdx.scenes.scene2d.InputListener() {
            public boolean touchDown(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y, int pointer, int button) {
                try {
                    outputStream.writeObject("Dice Throw");
                    outputStream.reset();
                } catch (IOException e) {
                    System.out.println("Could not send dice throw");
                }
                return true;
            }
        });
        return diceThrowButton;
    }
}
