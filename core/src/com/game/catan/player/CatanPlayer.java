package com.game.catan.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import com.game.catan.Functionality.Deck;
import com.game.catan.Functionality.VillagePair;
import com.game.catan.Map.Cell.Cell;
import com.game.catan.Map.Map;
import com.game.catan.Map.Cell.*;
import com.game.catan.Functionality.Functionality;

import java.net.Socket;
import java.util.HashMap;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;


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
    private Deck deck;
    private Map map;
    private UpdateListenerThread updateThread;
    private Variables variables;
    private VillagePair<VillageCell, VillageCell> villagePair;
    private HashMap<ResourceType, Label> cardsAmount;

    public CatanPlayer(Map map) {
        this.map = map;
        this.deck = new Deck();
        this.cardsAmount = new HashMap<>();
    }

    @Override
    public void create() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch = new SpriteBatch();
        stage = new Stage(new ScreenViewport());
        connectToServer();
        updateThread = new UpdateListenerThread(this, inputStream);
        updateThread.start();
        setUpInitialLabels();
    }

    private void connectToServer() {
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
            switch (id) {
                case 0:
                    villagePath = "Villages/yellowVillage.png";
                    break;
                case 1:
                    villagePath = "Villages/redVillage.png";
                    break;
                case 2:
                    villagePath = "Villages/blueVillage.png";
                    break;
                case 3:
                    villagePath = "Villages/greenVillage.png";
                    break;
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void render() {
        //setUpInitialLabels();
        drawBackgrounds();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
        renderMap();
        drawButtons();
        drawHex();
        batch.begin();
        if(isTurn) {
            batch.draw(new Texture("Villages/defaultVillage.png"), 0, 0);
        }
        else {
            batch.draw(new Texture("Villages/redVillage.png"), 0, 0);
        }
        batch.end();
        Gdx.input.setInputProcessor(stage);
    }

    private void setUpInitialLabels() {
        int x = 525;
        for(ResourceType type : ResourceType.values()) {
            if (type != ResourceType.EMPTY) {
                Label.LabelStyle labelStyle = new Label.LabelStyle();
                labelStyle.font = new com.badlogic.gdx.graphics.g2d.BitmapFont();
                labelStyle.font.getData().setScale(2f);
                labelStyle.fontColor = Color.SALMON;
                Label resourceLabel = new Label(deck.getResourceAmount(type).toString(), labelStyle);
                resourceLabel.setPosition(x, 50);
                x += 100;
                cardsAmount.put(type, resourceLabel);
                stage.addActor(resourceLabel);
            }
        }
    }

    private void drawBackgrounds() {
        drawBackground();
        drawResourceBackground();
        displayResources(stage);
    }

    private void drawButtons() {
        diceThrowButton(stage);
        endTurnButton();
    }

    private void drawBackground() {
        Texture background = new Texture("Backgrounds/background.png");
        batch.begin();
        batch.draw(background, 0, 0);
        batch.end();
    }

    private void drawResourceBackground() {
        Texture background = new Texture("Backgrounds/resourceBackground.png");
        batch.begin();
        batch.draw(background, 0, -100);
        batch.end();
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

    private void endTurnButton() {
        TextButton button = setUpTextButton("End Turn", 1720, 0);
        Functionality.setUpButtonFunc(stage, button, isTurn, outputStream);
    }

    private void drawHex() {
        HexagonButton hexagonButton = new HexagonButton(100, 100, 100, "Textures/brick.png");
        hexagonButton.draw(stage);
    }

    private void diceThrowButton(Stage stage) {
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
    }

    private ImageButton setUpImageButton(final String path, int x, int y, int amount, ResourceType type) {
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
        changeLabelAmount(type, amount, x, y - 50);
        return button;
    }
    private void changeLabelAmount(ResourceType type, int amount, int x, int y) {
        for(ResourceType resourceType : ResourceType.values()) {
            if(resourceType == type) {
                cardsAmount.get(resourceType).setText(Integer.toString(amount));
            }
        }
    }

    private void displayResources(Stage stage) {
        ImageButton brickButton = setUpImageButton("Cards/brickCard.png", 500, 100, deck.getResourceAmount(ResourceType.BRICK), ResourceType.BRICK);
        stage.addActor(brickButton);
        ImageButton wheatButton = setUpImageButton("Cards/wheatCard.png", 600, 100, deck.getResourceAmount(ResourceType.WHEAT), ResourceType.WHEAT);
        stage.addActor(wheatButton);
        ImageButton woodButton = setUpImageButton("Cards/woodCard.png", 700, 100, deck.getResourceAmount(ResourceType.WOOD), ResourceType.WOOD);
        stage.addActor(woodButton);
        ImageButton sheepButton = setUpImageButton("Cards/sheepCard.png", 800, 100, deck.getResourceAmount(ResourceType.SHEEP), ResourceType.SHEEP);
        stage.addActor(sheepButton);
        ImageButton stoneButton = setUpImageButton("Cards/stoneCard.png", 900, 100, deck.getResourceAmount(ResourceType.STONE), ResourceType.STONE);
        stage.addActor(stoneButton);
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
        HashSet<Cell> cells = new HashSet<>(map.getMap());
        for(Cell cell : map.getMap()) {
            cell.buttonFunc(stage, outputStream, this);
        }
    }

    /*private void renderMapWithStream() {
        Stream<Cell> stream = map.getMap().stream();
        stream.forEach(cell -> cell.buttonFunc(stage, outputStream, this));
    }*/

    @Override
    public void dispose() {
        batch.dispose();
        stage.dispose();
        updateThread.interrupt();
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
    public synchronized void setDeck(HashMap<ResourceType, Integer> deck) {
        this.deck.setDeck(deck);
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
