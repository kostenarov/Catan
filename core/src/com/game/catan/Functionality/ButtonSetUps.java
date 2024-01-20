package com.game.catan.Functionality;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class ButtonSetUps {
    public static TextButton setUpTextButton(String text, int y) {
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = new com.badlogic.gdx.graphics.g2d.BitmapFont();
        textButtonStyle.font.getData().setScale(2f);
        textButtonStyle.fontColor = com.badlogic.gdx.graphics.Color.BLACK;
        Texture buttonTexture = new Texture("Textures/button.png");
        textButtonStyle.up = new TextureRegionDrawable(buttonTexture);
        textButtonStyle.down = new TextureRegionDrawable(buttonTexture);

        TextButton button = new TextButton(text, textButtonStyle);
        button.setPosition(1720, y);
        button.setSize(200, 100);
        return button;
    }

    public static ImageButton setUpImageButton(final String path, int x, final boolean isTurn) {
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.imageUp = new TextureRegionDrawable(new Texture(path));
        ImageButton button = new ImageButton(style);
        button.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(isTurn) {
                    System.out.println("Button clicked" + path);
                }
                return true;
            }
        });

        button.setPosition(x, 100);
        button.setSize(60, 100);
        return button;
    }

    public static void setUpEndTurnButtonFunc(Stage stage, TextButton endTurnButton, final boolean isTurn,
                                              final boolean isDiceThrown, final ObjectOutputStream outputStream) {
        stage.addActor(endTurnButton);
        endTurnButton.addListener(new com.badlogic.gdx.scenes.scene2d.InputListener() {
            public boolean touchDown(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y, int pointer, int button) {
                try {
                    if(isTurn && isDiceThrown) {
                        System.out.println("End turn");
                        outputStream.writeObject("End Turn");
                        outputStream.reset();
                    }
                } catch (IOException e) {
                    System.out.println("Could not send end turn");
                }
                return true;
            }
        });
    }

    public static void setUpDiceThrowButtonFunc(Stage stage, TextButton diceThrowButton, final boolean isTurn,
                                                final boolean isDiceThrown, final ObjectOutputStream outputStream) {
        stage.addActor(diceThrowButton);
        diceThrowButton.addListener(new com.badlogic.gdx.scenes.scene2d.InputListener() {
            public boolean touchDown(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y, int pointer, int button) {
                try {
                    if(isTurn && !isDiceThrown) {
                        System.out.println("Dice throw");
                        outputStream.writeObject("Dice Throw");
                        outputStream.reset();
                    }
                } catch (IOException e) {
                    System.out.println("Could not send dice throw");
                }
                return true;
            }
        });
    }
}
