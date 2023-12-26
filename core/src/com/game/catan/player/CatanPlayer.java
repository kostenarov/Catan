package com.game.catan.player;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.game.catan.Map.Map;
import com.game.catan.Map.Cell.*;
import com.game.catan.Functionality.Functionality;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

public class CatanPlayer extends ApplicationAdapter {
    private Socket socket;
    private int id;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private int currentPlayerId;
    private SpriteBatch batch;
    private Stage stage;
    private Map map;
    private VillageCell startVillage;
    private String villagePath;
    private String roadPath;
    private HashMap<ResourceType, Integer> resources;
    private final Functionality functionality = new Functionality();
    private boolean isTurn = true;

    public CatanPlayer(Map map) {
        this.map = map;
        resources = new HashMap<>();
        resources.put(ResourceType.BRICK, 0);
        resources.put(ResourceType.WOOD, 0);
        resources.put(ResourceType.STONE, 0);
        resources.put(ResourceType.SHEEP, 0);
        resources.put(ResourceType.WHEAT, 0);
    }

    public CatanPlayer() {
        this.map = null;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        stage = new Stage(new ScreenViewport());

        try {
            socket = new Socket("localhost", 12345);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
            Map receivedMap = (Map) inputStream.readObject();
            System.out.println(receivedMap.getCenterCell().getDiceThrow());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
        if (isTurn) {
            renderMap();
            int diceThrow = diceThrowButton(stage);
            resources = functionality.getResources(diceThrow, map, resources, this);
            try {
                endTurnButton();
                isTurn = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        handleUpdates();
        Gdx.input.setInputProcessor(stage);

    }

    private TextButton setUpTextButton(String text, int x, int y, int width, int height) {
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = new com.badlogic.gdx.graphics.g2d.BitmapFont();
        textButtonStyle.font.getData().setScale(2f);
        textButtonStyle.fontColor = com.badlogic.gdx.graphics.Color.BLACK;
        Texture buttonTexture = new Texture("sheep.png");
        textButtonStyle.up = new TextureRegionDrawable(buttonTexture);
        textButtonStyle.down = new TextureRegionDrawable(buttonTexture);

        TextButton button = new TextButton(text, textButtonStyle);
        button.setPosition(x, y);
        button.setSize(width, height);
        return button;
    }

    private void endTurnButton() throws IOException {
        TextButton button = setUpTextButton("End Turn", 1720, 0, 200, 100);
        if(isTurn) {
        stage.addActor(button);
        button.addListener(new com.badlogic.gdx.scenes.scene2d.InputListener() {
                public boolean touchDown(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y, int pointer, int button) {
                    try {
                        isTurn = false;
                        System.out.println("End turn");
                        outputStream.writeObject("End Turn");
                        outputStream.reset();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
            });
        }
    }

    public int diceThrowButton(Stage stage) {
        final int[] diceThrow = new int[1];
        TextButton button = setUpTextButton("Throw Dice", 1720, 100, 200, 100);
        stage.addActor(button);
        button.addListener(new com.badlogic.gdx.scenes.scene2d.InputListener() {
            public boolean touchDown(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y, int pointer, int button) {
                try {
                    System.out.println("Dice thrown");
                    outputStream.writeObject("Dice Throw");
                    outputStream.reset();
                    Object input = inputStream.readObject();
                    if(input instanceof Integer) {
                        diceThrow[0] = (int) input;
                        System.out.println(diceThrow[0]);
                    }
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                return true;
            }
        });
        return diceThrow[0];
    }

    private void handleUpdates() {

    }

    private void renderMap() {
        for(Cell cell : map.getMap()) {
            cell.buttonFunc(stage);
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        stage.dispose();
    }
    public String getVillagePath() {
        return villagePath;
    }

    public String getRoadPath() {
        return roadPath;
    }
    public void setMap(Map map) {
        this.map = map;
    }
}
