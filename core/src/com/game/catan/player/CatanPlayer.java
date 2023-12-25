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
    private Texture buttonTexture;
    private Map map;
    private VillageCell startVillage;
    private String villagePath;
    private String roadPath;
    private HashMap<ResourceType, Integer> resources;

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
        for(Cell cell : map.getMap()) {
            cell.buttonFunc(stage);
        }
        int diceThrow = diceThrowButton(stage);
        getResources(diceThrow);
        try {
            endTurn();
        } catch (IOException e) {
            e.printStackTrace();
        }
        handleUpdates();
        Gdx.input.setInputProcessor(stage);

    }

    public void getMapFromServer() {
        try {
            Object temp = inputStream.readObject();
            if(temp instanceof Map) {
                map = (Map) temp;
            }
            else {
                System.out.println("Error: Map not received");
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            // Close resources properly
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void endTurn() throws IOException {
        // Send a message to the server indicating the end of the turn
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = new com.badlogic.gdx.graphics.g2d.BitmapFont();
        textButtonStyle.font.getData().setScale(2f);
        textButtonStyle.fontColor = com.badlogic.gdx.graphics.Color.BLACK;
        buttonTexture = new Texture("sheep.png");
        textButtonStyle.up = new TextureRegionDrawable(buttonTexture);
        textButtonStyle.down = new TextureRegionDrawable(buttonTexture);

        TextButton button = new TextButton("End Turn", textButtonStyle);
        button.setPosition(600, 600);
        button.setSize(200, 100);
        stage.addActor(button);
        button.addListener(new com.badlogic.gdx.scenes.scene2d.InputListener() {
            public boolean touchDown(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y, int pointer, int button) {
                try {
                    System.out.println("End turn");
                    outputStream.writeObject("End Turn");
                    outputStream.reset(); // Ensure the object is resent
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
        });
    }

    private void handleUpdates() {

    }

    private void renderMap() {
        for(Cell cell : map.getMap()) {
            Texture tempTexture = new Texture(cell.getTexturePath());
            batch.begin();
            batch.draw(tempTexture, cell.getCellCords().getX(), cell.getCellCords().getY());
            batch.end();
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        buttonTexture.dispose();
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

    public void getResources(int diceThrow) {
        for(ResourceCell cell : map.getResourceCells(map.getCenterCell())) {
            if(cell.getDiceThrow() == diceThrow && !cell.HasRobber()) {
                System.out.println(cell.getResource());
                resources.put(ResourceType.valueOf(cell.getResource()), resources.get(ResourceType.valueOf(cell.getResource())) + cell.getNumberOfPlayerVillages(this));
            }
        }
    }

    public int diceThrowButton(Stage stage) {
        final int[] diceThrow = new int[1];
        //add button at bottom right that when clicked generates a random number between 2 and 12
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = new com.badlogic.gdx.graphics.g2d.BitmapFont();
        textButtonStyle.font.getData().setScale(2f);
        textButtonStyle.fontColor = com.badlogic.gdx.graphics.Color.BLACK;
        buttonTexture = new Texture("sheep.png");
        textButtonStyle.up = new TextureRegionDrawable(buttonTexture);
        textButtonStyle.down = new TextureRegionDrawable(buttonTexture);

        TextButton button = new TextButton("Throw Dice", textButtonStyle);
        button.setPosition(1720, 980);
        button.setSize(200, 100);
        stage.addActor(button);
        button.addListener(new com.badlogic.gdx.scenes.scene2d.InputListener() {
            public boolean touchDown(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y, int pointer, int button) {
                diceThrow[0] = (int) (Math.random() * 10) + 2;
                System.out.println(diceThrow[0]);
                return true;
            }
        });
        return diceThrow[0];
    }
}
