package com.game.catan.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import com.game.catan.Functionality.Deck;
import com.game.catan.Map.Map;
import com.game.catan.Map.Cell.*;
import com.game.catan.Functionality.Functionality;

import java.net.Socket;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class CatanPlayer extends ApplicationAdapter {
    private int id;
    private int diceThrow;
    private boolean isTurn = true;
    private boolean isDiceThrown = false;
    private String roadPath;
    private String villagePath;
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private Stage stage;
    private SpriteBatch batch;
    private Deck deck = new Deck();
    private Map map;
    private VillageCell startVillage;
    private UpdateListenerThread updateThread;
    private final Functionality functionality = new Functionality();

    public CatanPlayer(Map map) {
        this.map = map;
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
            setMap(receivedMap);
            this.id = (int) inputStream.readObject();
            System.out.println("My id: " + id);
            if(id != 0) {
                isTurn = false;
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        updateThread = new UpdateListenerThread(this, inputStream);
        updateThread.start();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
        renderMap();
        this.diceThrow = diceThrowButton(stage);
        try {
            endTurnButton();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        Gdx.input.setInputProcessor(stage);
    }

    private TextButton setUpTextButton(String text, int x, int y) {
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = new com.badlogic.gdx.graphics.g2d.BitmapFont();
        textButtonStyle.font.getData().setScale(2f);
        textButtonStyle.fontColor = com.badlogic.gdx.graphics.Color.BLACK;
        Texture buttonTexture = new Texture("sheep.png");
        textButtonStyle.up = new TextureRegionDrawable(buttonTexture);
        textButtonStyle.down = new TextureRegionDrawable(buttonTexture);

        TextButton button = new TextButton(text, textButtonStyle);
        button.setPosition(x, y);
        button.setSize(200, 100);
        return button;
    }

    private void endTurnButton() throws IOException, ClassNotFoundException {
        TextButton button = setUpTextButton("End Turn", 1720, 0);
        stage.addActor(button);
        button.addListener(new com.badlogic.gdx.scenes.scene2d.InputListener() {
                public boolean touchDown(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y, int pointer, int button) {
                    try {
                        if(isTurn) {
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

    public int diceThrowButton(Stage stage) {
        final int[] diceThrow = new int[1];
        TextButton button = setUpTextButton("Throw Dice", 1720, 100);
        stage.addActor(button);
        button.addListener(new com.badlogic.gdx.scenes.scene2d.InputListener() {
            public boolean touchDown(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y, int pointer, int button) {
                try {
                    if(isTurn && !isDiceThrown) {
                        System.out.println("Dice thrown");
                        outputStream.writeObject("Dice Throw");
                        outputStream.reset();
                        setDiceThrown(true);
                    }
                } catch (IOException e) {
                    System.out.println("Could not send dice throw");
                }
                return true;
            }
        });
        return diceThrow[0];
    }

    public void sendMap() {
        try {
            outputStream.writeObject(map);
            outputStream.reset();
        } catch (IOException e) {
            System.out.println("Could not send map");
        }
    }

    private void renderMap() {
        for(Cell cell : map.getMap()) {
            cell.buttonFunc(stage, outputStream);
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        stage.dispose();
    }

    //**********SETTERS**********//
    public synchronized void setMap(Map map) {
        this.map = map;
    }
    public synchronized void setIsTurn(boolean isTurn) {
        this.isTurn = isTurn;
    }
    public synchronized void setDiceThrow(int diceThrow) {
        this.diceThrow = diceThrow;
    }
    public synchronized void setDeck(Deck deck) {
        this.deck = deck;
    }
    public synchronized void setDiceThrown(boolean isDiceThrown) {
        this.isDiceThrown = isDiceThrown;
    }


    //**********GETTERS**********//
    public synchronized boolean getIsTurn() {
        return isTurn;
    }
    public synchronized int getDiceThrow() {
        return diceThrow;
    }
    public synchronized int getId() {
        return id;
    }
    public synchronized Deck getDeck() {
        return deck;
    }
    public String getVillagePath() {
        return villagePath;
    }
    public String getRoadPath() {
        return roadPath;
    }
}
