package com.game.catan;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ApplicationAdapter;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.OrthographicCamera;

import com.badlogic.gdx.math.Rectangle;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;

public class Catan extends ApplicationAdapter {
	private Stage stage;
	private ArrayList<ImageButton> buttons = new ArrayList<>();

	private FitViewport window;

	private Texture redVillage;
	private Texture blueVillage;
	private Texture greenVillage;
	private Texture yellowVillage;
	private Texture background;

	private SpriteBatch batch;

	private ImageButton.ImageButtonStyle redStyle;
	private ImageButton.ImageButtonStyle blueStyle;
	private ImageButton.ImageButtonStyle greenStyle;
	private ImageButton.ImageButtonStyle yellowStyle;

	@Override
	public void create() {
		setVillages();

		window = new FitViewport(1920, 1080);
		stage = new Stage(window);
		Gdx.input.setInputProcessor(stage);
		setStyles();
		ImageButton button = new ImageButton(redStyle);
		ImageButton button2 = new ImageButton(blueStyle);
		button.setPosition(100, 100);
		button2.setPosition(500, 500);
		buttons.add(button);
		buttons.add(button2);
		for (final ImageButton imageButton : buttons) {
			imageButton.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					Gdx.app.log("Button Clicked", "Button was clicked!");
					imageButton.setStyle(generateRandomStyle(imageButton.getStyle()));
				}
			});
			stage.addActor(imageButton);
		}
	}

	ImageButton.ImageButtonStyle generateRandomStyle(ImageButton.ImageButtonStyle style) {
		int random = (int) (Math.random() * 4);
		switch (random) {
			case 0: {
				if (style == redStyle)
					return generateRandomStyle(blueStyle);
				else
					return redStyle;
			}
			case 1: {
				if (style == blueStyle)
					return generateRandomStyle(greenStyle);
				else
					return greenStyle;
			}
			case 2: {
				if (style == greenStyle)
					return generateRandomStyle(yellowStyle);
				else
					return yellowStyle;
			}
			case 3: {
				if (style == yellowStyle)
					return generateRandomStyle(redStyle);
				else
					return blueStyle;
			}
			default: {
				return redStyle;
			}
		}
	}

	public void setVillages() {
		redVillage = new Texture(Gdx.files.internal("redVillage.png"));
		blueVillage = new Texture(Gdx.files.internal("blueVillage.png"));
		greenVillage = new Texture(Gdx.files.internal("greenVillage.png"));
		yellowVillage = new Texture(Gdx.files.internal("yellowVillage.png"));
		background = new Texture(Gdx.files.internal("dirt.png"));
	}

	public void disposeVillages() {
		redVillage.dispose();
		blueVillage.dispose();
		greenVillage.dispose();
		yellowVillage.dispose();
	}

	public void setStyles() {
		redStyle = new ImageButton.ImageButtonStyle();
		redStyle.imageUp = new TextureRegionDrawable(new TextureRegion(redVillage));
		blueStyle = new ImageButton.ImageButtonStyle();
		blueStyle.imageUp = new TextureRegionDrawable(new TextureRegion(blueVillage));
		greenStyle = new ImageButton.ImageButtonStyle();
		greenStyle.imageUp = new TextureRegionDrawable(new TextureRegion(greenVillage));
		yellowStyle = new ImageButton.ImageButtonStyle();
		yellowStyle.imageUp = new TextureRegionDrawable(new TextureRegion(yellowVillage));
	}

	@Override
	public void render() {
		//set background
		batch = new SpriteBatch();
		batch.begin();
		batch.draw(background, 0, 0, 800, 800);
		Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);
		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();
	}

	@Override
	public void dispose() {
		stage.dispose();

		disposeVillages();

	}
}