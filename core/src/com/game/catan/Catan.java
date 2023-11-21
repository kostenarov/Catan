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
import com.game.catan.server.Villages.Village;

import java.util.ArrayList;

public class Catan extends ApplicationAdapter {
	private Stage stage;
	private ArrayList<ImageButton> buttons = new ArrayList<>();

	private FitViewport window;
	private Texture background = new Texture(Gdx.files.internal("dirt.png"));

	private SpriteBatch batch;

	private Village redVillageObj;
	private Village blueVillageObj;
	private Village greenVillageObj;
	private Village yellowVillageObj;

	@Override
	public void create() {
		redVillageObj = new Village("redVillage.png");
		blueVillageObj = new Village("blueVillage.png");
		greenVillageObj = new Village("greenVillage.png");
		yellowVillageObj = new Village("yellowVillage.png");
		window = new FitViewport(1920, 1080);
		stage = new Stage(window);
		Gdx.input.setInputProcessor(stage);
		ImageButton button = new ImageButton(redVillageObj.getVillageStyle());
		ImageButton button2 = new ImageButton(blueVillageObj.getVillageStyle());
		button.setPosition(100, 100);
		button2.setPosition(500, 500);
		buttons.add(button);
		buttons.add(button2);
		stage.addActor(button);
		stage.addActor(button2);
	}


	public void disposeVillages() {
		redVillageObj.dispose();
		blueVillageObj.dispose();
		greenVillageObj.dispose();
		yellowVillageObj.dispose();
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