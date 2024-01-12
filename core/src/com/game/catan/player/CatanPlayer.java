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
import com.game.catan.Functionality.Functionality;
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
    private final Deck deck;
    private Map map;
    private UpdateListenerThread updateThread;
    private Variables variables;
    private VillagePair<VillageCell, VillageCell> villagePair;
    private final HashMap<ResourceType, Label> cardsAmount;
    private int points = 0;
    private Label pointsLabel;

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
        drawSpecificRound();
        drawButtons();
        batch.begin();
        drawIsYourTurnLight();
        batch.end();
        Gdx.input.setInputProcessor(stage);
    }

    private void drawIsYourTurnLight() {
        if(isTurn) {
            batch.draw(new Texture("Villages/defaultVillage.png"), 0, 0);
        }
        else {
            batch.draw(new Texture("Villages/redVillage.png"), 0, 0);
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
        Label.LabelStyle pointsLabelStyle = new Label.LabelStyle();
        pointsLabelStyle.font = new com.badlogic.gdx.graphics.g2d.BitmapFont();
        pointsLabelStyle.font.getData().setScale(2f);
        pointsLabelStyle.fontColor = Color.SALMON;
        pointsLabel = new Label("Points: " + points, pointsLabelStyle);
        pointsLabel.setPosition(1000, 50);
        stage.addActor(pointsLabel);
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

    private TextButton setUpTextButton(String text, int y) {
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

    private void endTurnButton() {
        TextButton button = setUpTextButton("End Turn", 0);
        Functionality.setUpButtonFunc(stage, button, isTurn, outputStream);
    }

    private void diceThrowButton(Stage stage) {
        TextButton button = setUpTextButton("Throw Dice", 100);
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

    private ImageButton setUpImageButton(final String path, int x, ResourceType type) {
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

        button.setPosition(x, 100);
        button.setSize(60, 100);
        changeLabelAmount(type, deck.getResourceAmount(type));
        return button;
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

    private void displayResources(Stage stage) {
        ImageButton brickButton = setUpImageButton("Cards/brickCard.png", 500, ResourceType.BRICK);
        stage.addActor(brickButton);
        ImageButton wheatButton = setUpImageButton("Cards/wheatCard.png", 600, ResourceType.WHEAT);
        stage.addActor(wheatButton);
        ImageButton woodButton = setUpImageButton("Cards/woodCard.png", 700, ResourceType.WOOD);
        stage.addActor(woodButton);
        ImageButton sheepButton = setUpImageButton("Cards/sheepCard.png", 800, ResourceType.SHEEP);
        stage.addActor(sheepButton);
        ImageButton stoneButton = setUpImageButton("Cards/stoneCard.png", 900, ResourceType.STONE);
        stage.addActor(stoneButton);
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
}
