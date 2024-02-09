package com.game.catan.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import com.game.catan.Functionality.*;
import com.game.catan.Map.Map;
import com.game.catan.Map.Cell.*;
import com.game.catan.Map.Cell.Cell;

import java.net.Socket;
import java.util.HashMap;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;


public class CatanPlayer extends ApplicationAdapter {
    private int id;
    private int playersAmount;
    private int diceThrow;
    private boolean isTurn = true;
    private boolean isLoss = false;
    private boolean isWin = false;
    private boolean isDiceThrown = false;
    private boolean isOfferBeingCreated = false;
    private int points = 0;


    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    private Stage stage;
    private Stage UIStage;
    private Stage winStage;
    private Stage lossStage;
    private Stage resourceFieldStage;
    private Stage offerStage;
    private SpriteBatch batch;
    private SpriteBatch backgroundBatch;
    private SpriteBatch playerIndicatorBatch;
    private Texture playerTexture;
    private Texture background;
    private Texture resourceBackground;
    private Texture robber;
    private Image robberImage;
    private TextButton endTurnButton;
    private TextButton diceThrowButton;

    private Label pointsLabel;
    private Label diceThrowLabel;
    private HashMap<ResourceType, ResourceDisplay> resourceLabels;
    private HashMap<ResourceType, ResourceButton> resourceButtons;
    private HashMap<ResourceType, ResourceButton> offerButtons;

    private Map map;
    private Offer incomingOffer;
    private Offer outgoingOffer;
    private final Deck deck;
    private UpdateListenerThread updateThread;
    private SenderThread senderThread;
    private VillagePair<VillageCell, VillageCell> villagePair;

    public CatanPlayer(Map map) {
        this.map = map;
        this.deck = new Deck(true);
        this.outgoingOffer = new Offer(this.id);
        this.incomingOffer = new Offer(this.id);
        this.resourceLabels = new HashMap<>();
        this.resourceButtons = new HashMap<>();
        this.offerButtons = new HashMap<>();
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
                    playerTexture = new Texture("Indicators/yellowIndicator.png");
                    break;
                case 1:
                    playerTexture = new Texture("Indicators/blueIndicator.png");
                    break;
                case 2:
                    playerTexture = new Texture("Indicators/redIndicator.png");
                    break;
                case 3:
                    playerTexture = new Texture("Indicators/greenIndicator.png");
                    break;
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void create() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch = new SpriteBatch();
        backgroundBatch = new SpriteBatch();
        playerIndicatorBatch = new SpriteBatch();
        UIStage = new Stage(new ScreenViewport());
        resourceFieldStage = new Stage(new ScreenViewport());
        offerStage = new Stage(new ScreenViewport());
        stage = new Stage(new ScreenViewport());
        background = new Texture("Backgrounds/background.png");
        resourceBackground = new Texture("Backgrounds/resourceBackground.png");
        robber = new Texture("Textures/robber.png");
        robberImage = new Image(robber);
        robberImage.setSize(50, 50);
        connectToServer();
        updateThread = new UpdateListenerThread(this, inputStream);
        updateThread.start();
        setUpInitialLabels();
        setUpResourceLabels();
        displayResources();
        setUpEndScreens();
        setUpSendOfferButton();
        setUpOutgoingOffer();
        //displayOffer();
    }

    @Override
    public void render() {
        if(!isLoss && !isWin) {
            drawBackgrounds();
            resourceFieldStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
            resourceFieldStage.draw();
            UIStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
            UIStage.draw();
            stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
            stage.draw();

            drawButtons();
            renderNormalRound();
            setUpResourceCards();
            drawPlayerIndicator();
            Gdx.input.setInputProcessor(new InputMultiplexer(stage, resourceFieldStage, UIStage, offerStage));
            Gdx.graphics.setContinuousRendering(false);
            drawRobber();
            if(isOfferBeingCreated) {
                offerStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
                offerStage.draw();
            }
        }
        else {
            if(isLoss) {
                lossStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
                lossStage.draw();
                Gdx.graphics.requestRendering();
            }
            else if(isWin) {
                winStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
                winStage.draw();
                Gdx.graphics.requestRendering();
            }
        }

    }


    private void drawIsYourTurnLight() {
        if(isTurn) {
            playerIndicatorBatch.draw(new Texture("Indicators/greenIndicator.png"), 0, 50, 50, 50);
        }
        else {
            playerIndicatorBatch.draw(new Texture("Indicators/redIndicator.png"), 0, 50, 50, 50);
        }
    }

    private void setUpInitialLabels() {
        Label.LabelStyle pointsLabelStyle = new Label.LabelStyle();
        pointsLabelStyle.font = new BitmapFont();
        pointsLabelStyle.font.getData().setScale(2f);
        pointsLabelStyle.fontColor = Color.SALMON;
        pointsLabel = new Label("Points: " + points, pointsLabelStyle);
        pointsLabel.setPosition(1500, 50);
        diceThrowLabel = new Label("Dice throw: " + diceThrow, pointsLabelStyle);
        diceThrowLabel.setPosition(1500, 100);
        UIStage.addActor(pointsLabel);
        UIStage.addActor(diceThrowLabel);
    }

    private void setUpOffer() {

    }

    private void setUpEndScreens() {
        winStage = new Stage(new ScreenViewport());
        lossStage = new Stage(new ScreenViewport());
        Table winTable = new Table();
        Table lossTable = new Table();
        winTable.setFillParent(true);
        lossTable.setFillParent(true);
        winTable.add(new Label("You won", new Label.LabelStyle(new BitmapFont(), Color.BLACK)));
        lossTable.add(new Label("You lost", new Label.LabelStyle(new BitmapFont(), Color.BLACK)));
        winStage.addActor(winTable);
        lossStage.addActor(lossTable);
        Gdx.graphics.requestRendering();
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
        endTurnButton();
        diceThrowButton();
    }

    private void drawBackground() {
        backgroundBatch.draw(background, 0,0 );
    }

    private void drawResourceBackground() {
        backgroundBatch.draw(resourceBackground, 0, 0);
    }

    private void endTurnButton() {
        TextButton temp = ButtonSetUps.setUpTextButton("End Turn", 30, this);
        if(endTurnButton == null) {
            endTurnButton = ButtonSetUps.setUpEndTurnButtonFunc(temp, outputStream);
            UIStage.addActor(endTurnButton);
        }
    }

    private void diceThrowButton() {
        TextButton temp = ButtonSetUps.setUpTextButton("Dice Throw", 130, this);
        if(diceThrowButton == null) {
            diceThrowButton = ButtonSetUps.setUpDiceThrowButtonFunc(temp, outputStream);
            UIStage.addActor(diceThrowButton);
        }

    }

    private synchronized void changeLabelAmount(ResourceType type, int amount) {
        resourceLabels.get(type).changeAmount(amount);
    }

    public synchronized void setPoints(int points) {
        this.points = points;
    }

    public void displayPoints() {
        pointsLabel.setText("Points: " + points);
    }

    public void displayDiceThrow() {
        diceThrowLabel.setText("Dice throw: " + diceThrow);
    }

    private void displayResources() {
        for(ResourceType type : ResourceType.values()) {
            if(type != ResourceType.EMPTY) {
                resourceLabels.get(type).draw(UIStage);
            }
        }
    }

    private void displayOffer() {
        for(ResourceType type : ResourceType.values()) {
            if(type != ResourceType.EMPTY) {
                outgoingOffer = resourceButtons.get(type).draw(UIStage, outgoingOffer, deck);
            }
        }
    }

    private void setUpOutgoingOffer() {
        int x = 275;
        for(final ResourceType type : ResourceType.values()) {
            if(type != ResourceType.EMPTY) {
                Label.LabelStyle labelStyle = new Label.LabelStyle();
                labelStyle.font = new BitmapFont();
                labelStyle.font.getData().setScale(2f);
                labelStyle.fontColor = Color.SALMON;
                Label resourceLabel = new Label(deck.getResourceAmount(type).toString(), labelStyle);
                resourceLabel.setPosition(x, 200);
                Texture texture = new Texture("Cards/" + type.toString().toLowerCase() + "Card.png");
                ImageButton.ImageButtonStyle imageButtonStyle = new ImageButton.ImageButtonStyle();
                imageButtonStyle.imageUp = new TextureRegionDrawable(texture);
                ImageButton imageButton = new ImageButton(imageButtonStyle);
                imageButton.setPosition(x - 25, 250);
                imageButton.addListener(new InputListener() {
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        if(isDiceThrown && isTurn) {
                            System.out.println("Offer Button for " + type + " clicked");
                            return decreaseTradeOfferLabel(type);
                        }
                        return false;
                    }
                });
                ResourceButton resourceButton = new ResourceButton(imageButton, resourceLabel, type);
                offerButtons.put(type, resourceButton);
                x += 100;
            }
        }
        for(ResourceType type : ResourceType.values()) {
            if(type != ResourceType.EMPTY) {
                offerButtons.get(type).draw(offerStage);
            }
        }
    }

    private synchronized void setUpResourceCards() {
        int x = 825;
        for(final ResourceType type : ResourceType.values()) {
            if(type != ResourceType.EMPTY && !resourceButtons.containsKey(type)) {
                Label.LabelStyle labelStyle = new Label.LabelStyle();
                labelStyle.font = new BitmapFont();
                labelStyle.font.getData().setScale(2f);
                labelStyle.fontColor = Color.SALMON;
                final Label resourceLabel = new Label("0", labelStyle);
                resourceLabel.setPosition(x, 50);
                Texture texture = new Texture("Cards/" + type.toString().toLowerCase() + "Card.png");
                ImageButton.ImageButtonStyle imageButtonStyle = new ImageButton.ImageButtonStyle();
                imageButtonStyle.imageUp = new TextureRegionDrawable(texture);
                ImageButton imageButton = new ImageButton(imageButtonStyle);
                imageButton.addListener(new InputListener() {
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        if(isDiceThrown && isTurn) {
                            System.out.println("Offer Button for " + type + " clicked");
                            if(!isOfferBeingCreated)
                                isOfferBeingCreated = true;
                            return increaseTradeOfferLabel(type);
                        }
                        return false;
                    }
                });
                imageButton.setPosition(x - 25, 100);
                x += 100;
                ResourceButton resourceButton = new ResourceButton(imageButton, resourceLabel, type);
                outgoingOffer = resourceButton.draw(UIStage, outgoingOffer, deck);
                resourceButtons.put(type, resourceButton);
            }
        }
    }

    private void setUpSendOfferButton() {
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = new BitmapFont();
        textButtonStyle.font.getData().setScale(2f);
        textButtonStyle.fontColor = Color.BLACK;
        Texture buttonTextureUp = new Texture("Textures/buttonUp.png");
        Texture buttonTextureDown = new Texture("Textures/buttonDown.png");
        textButtonStyle.up = new TextureRegionDrawable(buttonTextureUp);
        textButtonStyle.down = new TextureRegionDrawable(buttonTextureDown);
        TextButton button = new TextButton("Send Offer", textButtonStyle);
        button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                    isOfferBeingCreated = false;
                    try {
                        outputStream.writeObject(outgoingOffer);
                        outputStream.reset();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("Offer sent");
                    resetOffer();
            }
        });
        button.setPosition(60, 0);
        button.setSize(200, 100);
        offerStage.addActor(button);
    }


    private synchronized boolean increaseTradeOfferLabel(ResourceType type) {
        if(outgoingOffer.getResourceAmount(type) < resourceLabels.get(type).getLabelAmount()) {
            outgoingOffer.addResource(type);
            offerButtons.get(type).changeAmount(outgoingOffer.getResourceAmount(type));
            Gdx.graphics.requestRendering();
            return true;
        }
        return false;
    }

    private synchronized boolean decreaseTradeOfferLabel(ResourceType type) {
        if(outgoingOffer.getResourceAmount(type) > 0) {
            outgoingOffer.removeResource(type);
            offerButtons.get(type).changeAmount(outgoingOffer.getResourceAmount(type));
            Gdx.graphics.requestRendering();
            return true;
        }
        return false;
    }

    private void setUpResourceLabels() {
        int x = 275;
        for(ResourceType type : ResourceType.values()) {
            if (type != ResourceType.EMPTY) {
                Label.LabelStyle labelStyle = new Label.LabelStyle();
                labelStyle.font = new BitmapFont();
                labelStyle.font.getData().setScale(2f);
                labelStyle.fontColor = Color.SALMON;
                Label resourceLabel = new Label(deck.getResourceAmount(type).toString(), labelStyle);
                resourceLabel.setPosition(x, 50);
                Texture texture = new Texture("Cards/" + type.toString().toLowerCase() + "Card.png");
                Image image = new Image(texture);
                image.setPosition(x - 25, 100);
                x += 100;
                ResourceDisplay resourceDisplay = new ResourceDisplay(image, resourceLabel);
                resourceLabels.put(type, resourceDisplay);
            }
        }
    }

    private void renderNormalRound() {
        HashSet<Cell> cells = new HashSet<>();
        cells = map.getMap(map.getCenterCell(), cells);
        for(Cell cell : cells) {
            if(cell instanceof ResourceCell) {
                cell.buttonFunc(resourceFieldStage, outputStream, this);
            }
            else {
                cell.buttonFunc(stage, outputStream, this);
            }
        }
    }

    private void drawRobber() {
        ResourceCell cell = map.getRobberCell();
        batch.begin();
        batch.draw(robber, cell.getCellCords().getX(), cell.getCellCords().getY());
        batch.end();
    }

    private void displayPlayers() {
        int y = 700;
        playerTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        playerIndicatorBatch.draw(playerTexture, 1700, y+=100, 50, 50);
    }

    @Override
    public void dispose() {
        batch.dispose();
        stage.dispose();
        UIStage.dispose();
        resourceFieldStage.dispose();
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
        Gdx.graphics.requestRendering();
    }
    public synchronized void setPlayersAmount(int playersAmount) {
        this.playersAmount = playersAmount;
        Gdx.graphics.requestRendering();
    }
    public synchronized void setIsTurn(boolean isTurn) {
        this.isTurn = isTurn;
        Gdx.graphics.requestRendering();
    }
    public synchronized void setDiceThrow(int diceThrow) {
        this.diceThrow = diceThrow;
        displayDiceThrow();
        Gdx.graphics.requestRendering();
    }
    public synchronized void setDeck(HashMap<ResourceType, Integer> deck) {
        if(!this.deck.equals(new Deck(deck))) {
            for (ResourceType type : ResourceType.values()) {
                if (type != ResourceType.EMPTY) {
                    changeLabelAmount(type, deck.get(type));
                }
            }
        }
        Gdx.graphics.requestRendering();
    }

    public synchronized void setIncomingOffer(Offer offer) {
        this.incomingOffer = offer;
    }

    public synchronized void setOutgoingOffer(Offer offer) {
        this.outgoingOffer = offer;
    }

    public synchronized void setDiceThrown(boolean isDiceThrown) {
        this.isDiceThrown = isDiceThrown;
        Gdx.graphics.requestRendering();
    }

    public synchronized void setRobberCell(ResourceCell cell) {
        map.setRobberCell(cell);
        Gdx.graphics.requestRendering();
    }

    public synchronized void setVillageCell(VillageCell cell) {
        map.setVillageCell(cell);
        Gdx.graphics.requestRendering();
    }

    public synchronized void setRoadCell(RoadCell cell) {
        map.setRoadCell(cell);
        Gdx.graphics.requestRendering();
    }

    public synchronized void resetOffer() {
        outgoingOffer = new Offer(this.id);
        for(ResourceType type : ResourceType.values()) {
            if(type != ResourceType.EMPTY) {
                resourceButtons.get(type).changeAmount(0);
            }
        }
    }

    //**********GETTERS**********//
    public synchronized boolean getIsTurn() {
        return isTurn;
    }
    public synchronized int getId() {
        return id;
    }
    public synchronized Deck getDeck() {
        return deck;
    }

    public synchronized boolean getIsDiceThrown() {
        return isDiceThrown;
    }

    public synchronized void setLoss(boolean lose) {
        isLoss = lose;
    }

    public synchronized void setWin(boolean win) {
        isWin = win;
    }
}
