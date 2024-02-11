package com.game.catan.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

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
    private boolean hasOfferBeenCreated = false;
    private boolean isOfferBeingReceived = false;
    private int points = 0;


    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    private Stage stage;
    private Stage UIStage;
    private Stage winStage;
    private Stage lossStage;
    private Stage resourceFieldStage;
    private Stage outgoingOfferStage;
    private Stage incomingOfferStage;
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
    private final HashMap<ResourceType, ResourceDisplay> incomingOfferGivenDisplays;
    private final HashMap<ResourceType, ResourceDisplay> incomingOfferWantedDisplays;

    private final HashMap<ResourceType, ResourceButton> resourceButtons;
    private final HashMap<ResourceType, ResourceButton> offerGivenButtons;
    private final HashMap<ResourceType, ResourceButton> offerWantedButtons;
    private final HashMap<ResourceType, ResourceButtonWithoutLabel> offerWantedIncreaseButtons;

    private Map map;
    private Offer incomingOffer;
    private Offer outgoingOffer;
    private final Deck deck;
    private UpdateListenerThread updateThread;
    private SenderThread senderThread;

    public CatanPlayer(Map map) {
        this.map = map;
        this.deck = new Deck(true);
        this.outgoingOffer = new Offer(this.id);
        this.incomingOffer = new Offer(this.id);
        this.incomingOfferGivenDisplays = new HashMap<>();
        this.incomingOfferWantedDisplays = new HashMap<>();
        this.resourceButtons = new HashMap<>();
        this.offerGivenButtons = new HashMap<>();
        this.offerWantedButtons = new HashMap<>();
        this.offerWantedIncreaseButtons = new HashMap<>();
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
        outgoingOfferStage = new Stage(new ScreenViewport());
        incomingOfferStage = new Stage(new ScreenViewport());
        winStage = new Stage(new ScreenViewport());
        lossStage = new Stage(new ScreenViewport());
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
        setUpEndScreens();
        setUpSendOfferButton();
        setupConfirmOfferButton();
        setUpOutgoingOfferGivenButtons();
        setUpOutgoingOfferWantedIncreaseButtons();
        setUpOutgoingofferWantedIncreaseButtons();
        setUpResourceCards();
        setUpincomingOfferGivenDisplays();
        setUpIncomingOfferWantedDisplays();
        displayResources();
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
            drawPlayerIndicator();
            Gdx.input.setInputProcessor(new InputMultiplexer(stage, resourceFieldStage, UIStage, outgoingOfferStage, incomingOfferStage));
            Gdx.graphics.setContinuousRendering(false);
            drawRobber();
            if(isOfferBeingCreated || hasOfferBeenCreated) {
                outgoingOfferStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
                outgoingOfferStage.draw();
            }
            if(isOfferBeingReceived) {
                incomingOfferStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
                incomingOfferStage.draw();
            }
        }
        else {
            if(isLoss) {
                lossStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
                lossStage.draw();
            }
            else if(isWin) {
                winStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
                winStage.draw();
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
    
    private void setUpincomingOfferGivenDisplays() {
        int x = 275;
        for(ResourceType type : ResourceType.values()) {
            if (type != ResourceType.EMPTY) {
                Label.LabelStyle labelStyle = new Label.LabelStyle();
                labelStyle.font = new BitmapFont();
                labelStyle.font.getData().setScale(2f);
                labelStyle.fontColor = Color.SALMON;
                Label resourceLabel = new Label(incomingOffer.getWantedOfferResourceAmount(type).toString(), labelStyle);
                resourceLabel.setPosition(x, 200);
                Texture texture = new Texture("Cards/" + type.toString().toLowerCase() + "Card.png");
                Image image = new Image(texture);
                image.setPosition(x - 25, 250);
                x += 75;
                ResourceDisplay resourceDisplay = new ResourceDisplay(image, resourceLabel);
                incomingOfferGivenDisplays.put(type, resourceDisplay);
                resourceDisplay.draw(incomingOfferStage);
            }
        }
    }

    private void setUpIncomingOfferWantedDisplays() {
        int x = 775;
        for(ResourceType type : ResourceType.values()) {
            if (type != ResourceType.EMPTY) {
                Label.LabelStyle labelStyle = new Label.LabelStyle();
                labelStyle.font = new BitmapFont();
                labelStyle.font.getData().setScale(2f);
                labelStyle.fontColor = Color.SALMON;
                Label resourceLabel = new Label(incomingOffer.getWantedOfferResourceAmount(type).toString(), labelStyle);
                resourceLabel.setPosition(x, 200);
                Texture texture = new Texture("Cards/" + type.toString().toLowerCase() + "Card.png");
                Image image = new Image(texture);
                image.setPosition(x - 25, 250);
                x += 75;
                ResourceDisplay resourceDisplay = new ResourceDisplay(image, resourceLabel);
                incomingOfferWantedDisplays.put(type, resourceDisplay);
                resourceDisplay.draw(incomingOfferStage);
            }
        }
    }

    private void setUpEndScreens() {
        Table winTable = new Table();
        Table lossTable = new Table();
        winTable.setFillParent(true);
        lossTable.setFillParent(true);
        winTable.add(new Label("You won", new Label.LabelStyle(new BitmapFont(), Color.BLACK)));
        lossTable.add(new Label("You lost", new Label.LabelStyle(new BitmapFont(), Color.BLACK)));
        winStage.addActor(winTable);
        lossStage.addActor(lossTable);
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
        resourceButtons.get(type).changeAmount(amount);
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
                resourceButtons.get(type).draw(UIStage);
            }
        }
    }
    
    private void setUpOutgoingOfferWantedIncreaseButtons() {
        int x = 775;
        for(final ResourceType type : ResourceType.values()) {
            if(type != ResourceType.EMPTY) {
                Texture texture = new Texture("Cards/" + type.toString().toLowerCase() + "Card.png");
                ImageButton.ImageButtonStyle imageButtonStyle = new ImageButton.ImageButtonStyle();
                imageButtonStyle.imageUp = new TextureRegionDrawable(texture);
                ImageButton imageButton = new ImageButton(imageButtonStyle);
                imageButton.setPosition(x - 25, 50);
                imageButton.addListener(new InputListener() {
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        if(isDiceThrown && isTurn) {
                            System.out.println("Offer Button for " + type + " clicked");
                            return increaseTradeOfferWantedLabel(type);
                        }
                        return false;
                    }
                });
                ResourceButtonWithoutLabel resourceButton = new ResourceButtonWithoutLabel(imageButton, type);
                offerWantedIncreaseButtons.put(type, resourceButton);
                offerWantedIncreaseButtons.get(type).draw(outgoingOfferStage);
                x += 75;
            }
        }
    }

    private void setUpOutgoingofferWantedIncreaseButtons() {
        int x = 775;
        for(final ResourceType type : ResourceType.values()) {
            if(type != ResourceType.EMPTY) {
                Label.LabelStyle labelStyle = new Label.LabelStyle();
                labelStyle.font = new BitmapFont();
                labelStyle.font.getData().setScale(2f);
                labelStyle.fontColor = Color.SALMON;
                Label resourceLabel = new Label(outgoingOffer.getWantedOfferResourceAmount(type).toString(), labelStyle);
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
                            return decreaseTradeOfferWantedLabel(type);
                        }
                        return false;
                    }
                });
                ResourceButton resourceButton = new ResourceButton(imageButton, resourceLabel, type);
                offerWantedButtons.put(type, resourceButton);
                offerWantedButtons.get(type).draw(outgoingOfferStage);
                x += 75;
            }
        }
    }

    private void setUpOutgoingOfferGivenButtons() {
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
                            return decreaseTradeOfferGivenLabel(type);
                        }
                        return false;
                    }
                });
                ResourceButton resourceButton = new ResourceButton(imageButton, resourceLabel, type);
                offerGivenButtons.put(type, resourceButton);
                offerGivenButtons.get(type).draw(outgoingOfferStage);
                x += 75;
            }
        }
    }

    private synchronized void setUpResourceCards() {
        int x = 275;
        for(final ResourceType type : ResourceType.values()) {
            if(type != ResourceType.EMPTY && !resourceButtons.containsKey(type)) {
                Label.LabelStyle labelStyle = new Label.LabelStyle();
                labelStyle.font = new BitmapFont();
                labelStyle.font.getData().setScale(2f);
                labelStyle.fontColor = Color.SALMON;
                Label resourceLabel = new Label(deck.getResourceAmount(type).toString(), labelStyle);
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
                            return increaseTradeOfferGivenLabel(type);
                        }
                        return false;
                    }
                });
                imageButton.setPosition(x - 25, 100);
                x += 75;
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
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    isOfferBeingCreated = false;
                    try {
                        outputStream.writeObject(outgoingOffer);
                        outputStream.reset();
                        resetOffer();
                        System.out.println("Offer sent");
                        return true;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
            }
        });
        button.setPosition(60, 0);
        button.setSize(200, 100);
        outgoingOfferStage.addActor(button);
    }

    private synchronized void setupConfirmOfferButton() {
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = new BitmapFont();
        textButtonStyle.font.getData().setScale(2f);
        textButtonStyle.fontColor = Color.BLACK;
        Texture buttonTextureUp = new Texture("Textures/buttonUp.png");
        Texture buttonTextureDown = new Texture("Textures/buttonDown.png");
        textButtonStyle.up = new TextureRegionDrawable(buttonTextureUp);
        textButtonStyle.down = new TextureRegionDrawable(buttonTextureDown);
        TextButton button = new TextButton("Confirm Offer", textButtonStyle);
        button.addListener(new ClickListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                try {
                    outputStream.writeObject("Offer confirmed by:" + id);
                    outputStream.reset();
                    System.out.println("Offer confirmed");
                    return true;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        button.setPosition(60, 0);
        button.setSize(200, 100);
        incomingOfferStage.addActor(button);
    }

    public synchronized void addOutgoingOfferAcceptance(int id) {
        outgoingOffer.addAcceptance(id);
        Gdx.graphics.requestRendering();
    }

    public synchronized void addIncomingOfferAcceptance(int id) {
        incomingOffer.addAcceptance(id);
        Gdx.graphics.requestRendering();
    }


    private synchronized boolean increaseTradeOfferGivenLabel(ResourceType type) {
        if(outgoingOffer.getGivenOfferResourceAmount(type) < resourceButtons.get(type).getLabelAmount()) {
            outgoingOffer.addResourceToGivenOffer(type);
            offerGivenButtons.get(type).changeAmount(outgoingOffer.getGivenOfferResourceAmount(type));
            Gdx.graphics.requestRendering();
            return true;
        }
        return false;
    }

    private synchronized boolean decreaseTradeOfferGivenLabel(ResourceType type) {
        if(outgoingOffer.getGivenOfferResourceAmount(type) > 0) {
            outgoingOffer.removeResourceFromGivenOffer(type);
            offerGivenButtons.get(type).changeAmount(outgoingOffer.getGivenOfferResourceAmount(type));
            Gdx.graphics.requestRendering();
            return true;
        }
        return false;
    }

    private synchronized boolean increaseTradeOfferWantedLabel(ResourceType type) {
        if(outgoingOffer.getGivenOfferResourceAmount(type) == 0) {
            System.out.println("You need to give something first");
            outgoingOffer.addResourceToWantedOffer(type);
            offerWantedButtons.get(type).changeAmount(outgoingOffer.getWantedOfferResourceAmount(type));
            Gdx.graphics.requestRendering();
            return true;
        }
        return false;
    }

    private synchronized boolean decreaseTradeOfferWantedLabel(ResourceType type) {
        if(outgoingOffer.getWantedOfferResourceAmount(type) > 0) {
            outgoingOffer.removeResourceFromWantedOffer(type);
            offerWantedButtons.get(type).changeAmount(outgoingOffer.getWantedOfferResourceAmount(type));
            Gdx.graphics.requestRendering();
            return true;
        }
        return false;
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
        for(ResourceType type : ResourceType.values()) {
            if(type != ResourceType.EMPTY) {
                incomingOfferGivenDisplays.get(type).changeAmount(offer.getWantedOfferResourceAmount(type));
            }
        }
        isOfferBeingReceived = true;
        Gdx.graphics.requestRendering();
    }

    public synchronized void setOutgoingOffer(Offer offer) {
        this.outgoingOffer = offer;
        Gdx.graphics.requestRendering();
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
                offerGivenButtons.get(type).changeAmount(0);
            }
        }
        isOfferBeingCreated = false;
        Gdx.graphics.requestRendering();
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
    public synchronized boolean isOfferBeingCreated() {
        return isOfferBeingCreated;
    }
    
    public synchronized boolean isOfferBeingReceived() {
        return isOfferBeingReceived;
    }

    public synchronized void setLoss(boolean lose) {
        isLoss = lose;
    }

    public synchronized void setWin(boolean win) {
        isWin = win;
    }

    public synchronized void resetFlags() {
        isLoss = false;
        isWin = false;
        isOfferBeingCreated = false;
        isOfferBeingReceived = false;
        isDiceThrown = false;
        Gdx.graphics.requestRendering();
    }
}
