package com.game.catan.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import com.game.catan.Functionality.Deck;
import com.game.catan.Functionality.Offer;
import com.game.catan.Functionality.VillagePair;
import com.game.catan.Functionality.ButtonSetUps;
import com.game.catan.Map.Map;
import com.game.catan.Map.Cell.*;
import com.game.catan.Map.Cell.Cell;

import java.net.Socket;
import java.util.HashMap;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class CatanPlayer extends ApplicationAdapter {
    private int id;
    private int playersAmount;
    private int diceThrow;
    private boolean isTurn = true;
    private boolean isDiceThrown = false;
    private String villagePath;
    private int points = 0;


    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    private Stage stage;
    private Stage resourceStage;
    private SpriteBatch batch;
    private SpriteBatch backgroundBatch;
    private SpriteBatch playerIndicatorBatch;
    private Texture playerTexture;
    private Label pointsLabel;
    private Label diceThrowLabel;

    private Map map;
    private Offer offer;
    private final Deck deck;
    private UpdateListenerThread updateThread;
    private VillagePair<VillageCell, VillageCell> villagePair;
    private static final HashMap<ResourceType, Label> cardsAmount = new HashMap<>();

    public CatanPlayer(Map map) {
        this.map = map;
        this.deck = new Deck();
    }

    @Override
    public void create() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch = new SpriteBatch();
        backgroundBatch = new SpriteBatch();
        playerIndicatorBatch = new SpriteBatch();
        stage = new Stage(new ScreenViewport());
        resourceStage = new Stage(new ScreenViewport());
        connectToServer();
        updateThread = new UpdateListenerThread(this, inputStream);
        updateThread.start();
        setUpInitialLabels();
    }

    @Override
    public void render() {
        drawBackgrounds();
        displayResources();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
        resourceStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        resourceStage.draw();
        drawSpecificRound();
        drawButtons();
        drawPlayerIndicator();
        Gdx.input.setInputProcessor(stage);
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

    private void drawIsYourTurnLight() {
        if(isTurn) {
            playerIndicatorBatch.draw(new Texture("Villages/defaultVillage.png"), 0, 0);
        }
        else {
            playerIndicatorBatch.draw(new Texture("Villages/redVillage.png"), 0, 0);
        }
    }

    private void drawSpecificRound() {
        if(diceThrow == 7) {
            renderThiefRound();
            renderMap();
        }
        else {
            renderNormalRound();
            renderMap();
        }
    }

    private void setUpInitialLabels() {
        int x = 325;
        for(ResourceType type : ResourceType.values()) {
            if (type != ResourceType.EMPTY) {
                Label.LabelStyle labelStyle = new Label.LabelStyle();
                labelStyle.font = new BitmapFont();
                labelStyle.font.getData().setScale(2f);
                labelStyle.fontColor = Color.SALMON;
                Label resourceLabel = new Label(deck.getResourceAmount(type).toString(), labelStyle);
                resourceLabel.setPosition(x, 50);
                x += 100;
                cardsAmount.put(type, resourceLabel);
                stage.addActor(resourceLabel);
            }
        }
        Label.LabelStyle pointsLabelStyle = new Label.LabelStyle();
        pointsLabelStyle.font = new BitmapFont();
        pointsLabelStyle.font.getData().setScale(2f);
        pointsLabelStyle.fontColor = Color.SALMON;
        pointsLabel = new Label("Points: " + points, pointsLabelStyle);
        pointsLabel.setPosition(1000, 50);
        diceThrowLabel = new Label("Dice throw: " + diceThrow, pointsLabelStyle);
        diceThrowLabel.setPosition(1000, 100);
        stage.addActor(pointsLabel);
        stage.addActor(diceThrowLabel);
    }

    private void setUpOffer() {

    }

    private void drawBackgrounds() {
        backgroundBatch.begin();
        drawBackground();
        drawResourceBackground();
        backgroundBatch.end();
    }

    private void drawPlayerIndicator() {
        playerIndicatorBatch.begin();
        drawIsYourTurnLight();
        displayPlayers();
        playerIndicatorBatch.end();
    }

    private void drawButtons() {
        diceThrowButton();
        endTurnButton();
    }

    private void drawBackground() {
        Texture background = new Texture("Backgrounds/background.png");
        backgroundBatch.draw(background, 0, 0);
    }

    private void drawResourceBackground() {
        Texture background = new Texture("Backgrounds/resourceBackground.png");
        backgroundBatch.draw(background, 0, -100);
    }

    private void endTurnButton() {
        TextButton button = ButtonSetUps.setUpTextButton("End Turn", 0);
        ButtonSetUps.setUpEndTurnButtonFunc(stage, button, isTurn, isDiceThrown, outputStream);
    }

    private void diceThrowButton() {
        TextButton button = ButtonSetUps.setUpTextButton("Dice Throw", 100);
        ButtonSetUps.setUpDiceThrowButtonFunc(stage, button, isTurn, isDiceThrown, outputStream);
    }

    private void changeLabelAmount(ResourceType type, int amount) {
        for(ResourceType resourceType : ResourceType.values()) {
            if(resourceType == type) {
                cardsAmount.get(resourceType).setText(Integer.toString(amount));
            }
        }
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void displayPoints() {
        pointsLabel.setText("Points: " + points);
    }

    public void displayDiceThrow() {
        diceThrowLabel.setText("Dice throw: " + diceThrow);
    }

    private void displayResources() {
        ImageButton brickButton = ButtonSetUps.setUpImageButton("Cards/brickCard.png", 300, isTurn);
        changeLabelAmount(ResourceType.BRICK, deck.getResourceAmount(ResourceType.BRICK));
        resourceStage.addActor(brickButton);
        ImageButton wheatButton = ButtonSetUps.setUpImageButton("Cards/wheatCard.png", 400, isTurn);
        changeLabelAmount(ResourceType.WHEAT, deck.getResourceAmount(ResourceType.WHEAT));
        resourceStage.addActor(wheatButton);
        ImageButton woodButton = ButtonSetUps.setUpImageButton("Cards/woodCard.png", 500, isTurn);
        changeLabelAmount(ResourceType.WOOD, deck.getResourceAmount(ResourceType.WOOD));
        resourceStage.addActor(woodButton);
        ImageButton sheepButton = ButtonSetUps.setUpImageButton("Cards/sheepCard.png", 600, isTurn);
        changeLabelAmount(ResourceType.SHEEP, deck.getResourceAmount(ResourceType.SHEEP));
        resourceStage.addActor(sheepButton);
        ImageButton stoneButton = ButtonSetUps.setUpImageButton("Cards/stoneCard.png", 700, isTurn);
        changeLabelAmount(ResourceType.STONE, deck.getResourceAmount(ResourceType.STONE));
        resourceStage.addActor(stoneButton);
    }

    private void renderMap() {
        for(Cell cell : map.getMap()) {
            if(cell instanceof RoadCell || cell instanceof VillageCell) {
                cell.buttonFunc(stage, outputStream, this);
            }
        }
    }

    private void renderThiefRound() {
        for(Cell cell : map.getMap()) {
            if(cell instanceof ResourceCell && ((ResourceCell) cell).hasRobber()) {
                cell.buttonFunc(stage, outputStream, this);
                batch.begin();
                batch.draw(new Texture("Textures/robber.png"), cell.getCellCords().getX(), cell.getCellCords().getY());
                batch.end();
            }
            else {
                cell.buttonFunc(stage, outputStream, this);
                drawHexagon(cell.getCellCords().getX() + 50, cell.getCellCords().getY() + 50);
            }
        }
    }

    private void renderNormalRound() {
        for(Cell cell : map.getMap()) {
            if(cell instanceof ResourceCell) {
                cell.buttonFunc(stage, outputStream, this);
                drawHexagon(cell.getCellCords().getX() + 50, cell.getCellCords().getY() + 50);
            }
            if(cell instanceof ResourceCell && ((ResourceCell) cell).hasRobber()) {
                cell.buttonFunc(stage, outputStream, this);
                batch.begin();
                batch.draw(new Texture("Textures/robber.png"), cell.getCellCords().getX(), cell.getCellCords().getY());
                batch.end();
            }
        }
    }

    private void displayPlayers() {
        int y = 700;
        for(int i = 0; i < playersAmount; i++) {
            playerTexture = new Texture("Villages/defaultVillage.png");
            switch (i) {
                case 0:
                    playerTexture = new Texture("Villages/yellowVillage.png");
                    break;
                case 1:
                    playerTexture = new Texture("Villages/redVillage.png");
                    break;
                case 2:
                    playerTexture = new Texture("Villages/blueVillage.png");
                    break;
                case 3:
                    playerTexture = new Texture("Villages/greenVillage.png");
                    break;
            }
            playerTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            playerIndicatorBatch.draw(playerTexture, 1700, y+=100, 50, 50);
        }
    }

    private void drawHexagon(int x, int y) {
        Hexagon hexagon = new Hexagon(x, y, 70);
        hexagon.draw(stage);
    }

    @Override
    public void dispose() {
        batch.dispose();
        stage.dispose();
        updateThread.stopThread();
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
    public synchronized void setPlayersAmount(int playersAmount) {
        this.playersAmount = playersAmount;
    }
    public synchronized void setIsTurn(boolean isTurn) {
        this.isTurn = isTurn;
    }
    public synchronized void setDiceThrow(int diceThrow) {
        this.diceThrow = diceThrow;
        displayDiceThrow();
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
}
