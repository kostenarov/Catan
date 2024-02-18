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
    private int currentPlayerIndex = 0;
    private int diceThrow;
    private boolean isTurn = true;
    private boolean isLoss = false;
    private boolean isWin = false;
    private boolean isDiceThrown = false;
    private boolean initialSequence = true;
    private boolean secondSequence = false;
    private boolean isOfferBeingCreated = false;
    private boolean hasOfferBeenCreated = false;
    private boolean isOfferBeingReceived = false;
    private int points = 0;
    private final int resourceX = 200;

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
    private Stage indicatorStage;
    private SpriteBatch batch;
    private SpriteBatch backgroundBatch;
    private SpriteBatch playerIndicatorBatch;
    private Texture background;
    private Texture resourceBackground;
    private Texture isTurnBackground;
    private Texture isNotTurnBackground;
    private Texture robber;
    private Image robberImage;
    private TextButton endTurnButton;
    private TextButton diceThrowButton;
    private Label pointsLabel;
    private Label diceThrowLabel;

    private final HashMap<ResourceType, ResourceDisplay> incomingOfferGivenDisplays;
    private final HashMap<ResourceType, ResourceDisplay> incomingOfferWantedDisplays;
    private final HashMap<Integer, Texture> playerIndicators;

    private final HashMap<ResourceType, ResourceButton> resourceButtons;
    private final HashMap<ResourceType, ResourceButton> offerGivenButtons;
    private final HashMap<ResourceType, ResourceButton> offerWantedButtons;
    private final HashMap<ResourceType, ResourceButtonWithoutLabel> offerWantedIncreaseButtons;
    private final HashMap<Integer, ImageButton> acceptanceIndicators;

    private Map map;
    private Offer incomingOffer;
    private Offer outgoingOffer;
    private final Deck deck;
    private UpdateListenerThread updateThread;

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
        this.acceptanceIndicators = new HashMap<>();
        this.playerIndicators = new HashMap<>();
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
        indicatorStage = new Stage(new ScreenViewport());
        background = new Texture("Backgrounds/background.png");
        resourceBackground = new Texture("Backgrounds/resourceBackground.png");
        robber = new Texture("Textures/robber.png");
        isTurnBackground = new Texture("Textures/isTurn.png");
        isNotTurnBackground = new Texture("Textures/isNotTurn.png");
        initiateIndicators();
        robberImage = new Image(robber);
        robberImage.setSize(50, 50);
        connectToServer();
        updateThread = new UpdateListenerThread(this, inputStream);
        updateThread.start();
        offerSetUps();
        drawButtons();
    }

    private void offerSetUps() {
        setUpInitialLabels();
        setUpEndScreens();
        setUpSendOfferButton();
        setupConfirmOfferButton();
        setUpRejectOfferButton();
        setUpOutgoingOfferGivenButtons();
        setUpOutgoingOfferWantedIncreaseButtons();
        setUpOutgoingOfferWantedDecreaseButtons();
        setUpResourceCards();
        setUpIncomingOfferGivenDisplays();
        setUpIncomingOfferWantedDisplays();
        displayResources();
        setUpOfferIndicators();
    }

    private void setUpOfferIndicators() {
        ImageButton.ImageButtonStyle yellowStyle = new ImageButton.ImageButtonStyle();
        yellowStyle.over = new TextureRegionDrawable(new Texture("Indicators/yellowAccepted.png"));
        yellowStyle.down = new TextureRegionDrawable(new Texture("Indicators/yellowDeclined.png"));
        yellowStyle.up = new TextureRegionDrawable(new Texture("Indicators/yellowIndicator.png"));
        ImageButton yellowIndicator = new ImageButton(yellowStyle);
        yellowIndicator.setSize(50, 50);

        ImageButton.ImageButtonStyle blueStyle = new ImageButton.ImageButtonStyle();
        blueStyle.over = new TextureRegionDrawable(new Texture("Indicators/blueAccepted.png"));
        blueStyle.down = new TextureRegionDrawable(new Texture("Indicators/blueDeclined.png"));
        blueStyle.up = new TextureRegionDrawable(new Texture("Indicators/blueIndicator.png"));
        ImageButton blueIndicator = new ImageButton(blueStyle);

        blueIndicator.setSize(50, 50);

        ImageButton.ImageButtonStyle greenStyle = new ImageButton.ImageButtonStyle();
        greenStyle.over = new TextureRegionDrawable(new Texture("Indicators/greenAccepted.png"));
        greenStyle.down = new TextureRegionDrawable(new Texture("Indicators/greenDeclined.png"));
        greenStyle.up = new TextureRegionDrawable(new Texture("Indicators/greenIndicator.png"));
        ImageButton greenIndicator = new ImageButton(greenStyle);
        greenIndicator.setSize(50, 50);

        ImageButton.ImageButtonStyle redStyle = new ImageButton.ImageButtonStyle();
        redStyle.over = new TextureRegionDrawable(new Texture("Indicators/redAccepted.png"));
        redStyle.down = new TextureRegionDrawable(new Texture("Indicators/redDeclined.png"));
        redStyle.up = new TextureRegionDrawable(new Texture("Indicators/redIndicator.png"));
        ImageButton redIndicator = new ImageButton(redStyle);
        redIndicator.setSize(50, 50);
        acceptanceIndicators.put(0, yellowIndicator);
        acceptanceIndicators.put(1, blueIndicator);
        acceptanceIndicators.put(2, greenIndicator);
        acceptanceIndicators.put(3, redIndicator);
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

            renderNormalRound();
            drawPlayerIndicator();
            Gdx.graphics.setContinuousRendering(false);
            drawRobber();
            Gdx.input.setInputProcessor(new InputMultiplexer(stage, UIStage, resourceFieldStage, outgoingOfferStage, incomingOfferStage, indicatorStage));
            if(isOfferBeingCreated || hasOfferBeenCreated) {
                outgoingOfferStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
                outgoingOfferStage.draw();

                indicatorStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
                indicatorStage.draw();
                drawOutcomingOfferIndicators();
            }
            if(isOfferBeingReceived) {
                incomingOfferStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
                incomingOfferStage.draw();

                indicatorStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
                indicatorStage.draw();
                drawIncomingOfferIndicators();
            }
        }

        else {
            drawBackgrounds();
            if(isLoss) {
                lossStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
                lossStage.draw();
            }
            else {
                winStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
                winStage.draw();
            }
        }
    }

    private void initiateIndicators() {
        Texture yellowIndicator = new Texture("Indicators/yellowIndicator.png");
        Texture blueIndicator = new Texture("Indicators/blueIndicator.png");
        Texture redIndicator = new Texture("Indicators/redIndicator.png");
        Texture greenIndicator = new Texture("Indicators/greenIndicator.png");
        this.playerIndicators.put(0, yellowIndicator);
        this.playerIndicators.put(1, blueIndicator);
        this.playerIndicators.put(2, redIndicator);
        this.playerIndicators.put(3, greenIndicator);
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
    
    private void setUpIncomingOfferGivenDisplays() {
        int x = 1400;
        for(ResourceType type : ResourceType.values()) {
            if (type != ResourceType.EMPTY) {
                Label.LabelStyle labelStyle = new Label.LabelStyle();
                labelStyle.font = new BitmapFont();
                labelStyle.font.getData().setScale(2f);
                labelStyle.fontColor = Color.SALMON;
                Label resourceLabel = new Label(incomingOffer.getGivenOfferResourceAmount(type).toString(), labelStyle);
                resourceLabel.setPosition(x, 900);
                Texture texture = new Texture("Cards/" + type.toString().toLowerCase() + "Card.png");
                Image image = new Image(texture);
                image.setPosition(x - 25, 950);
                x += 75;
                ResourceDisplay resourceDisplay = new ResourceDisplay(image, resourceLabel);
                incomingOfferGivenDisplays.put(type, resourceDisplay);
                resourceDisplay.draw(incomingOfferStage);
            }
        }
    }

    private void setUpIncomingOfferWantedDisplays() {
        int x = 1400;
        for(ResourceType type : ResourceType.values()) {
            if (type != ResourceType.EMPTY) {
                Label.LabelStyle labelStyle = new Label.LabelStyle();
                labelStyle.font = new BitmapFont();
                labelStyle.font.getData().setScale(2f);
                labelStyle.fontColor = Color.SALMON;
                Label resourceLabel = new Label(incomingOffer.getWantedOfferResourceAmount(type).toString(), labelStyle);
                resourceLabel.setPosition(x, 750);
                Texture texture = new Texture("Cards/" + type.toString().toLowerCase() + "Card.png");
                Image image = new Image(texture);
                image.setPosition(x - 25, 800);
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
        backgroundBatch.draw(background, 0,0 );
        backgroundBatch.draw(resourceBackground, 0, 0);
        backgroundBatch.end();
    }

    private void drawPlayerIndicator() {
        playerIndicatorBatch.begin();
        displayPlayers();
        playerIndicatorBatch.end();
    }

    private void drawButtons() {
        endTurnButton();
        diceThrowButton();
    }

    private void endTurnButton() {
        TextButton temp = ButtonSetUps.setUpTextButton("End Turn", 30);
        if(endTurnButton == null) {
            endTurnButton = ButtonSetUps.setUpEndTurnButtonFunc(temp, outputStream);
            UIStage.addActor(endTurnButton);
        }
    }

    private void diceThrowButton() {
        TextButton temp = ButtonSetUps.setUpTextButton("Dice Throw", 130);
        if(diceThrowButton == null) {
            diceThrowButton = ButtonSetUps.setUpDiceThrowButtonFunc(temp, outputStream);
            UIStage.addActor(diceThrowButton);
        }

    }

    private synchronized void changeLabelAmount(ResourceType type, int amount) {
        resourceButtons.get(type).changeAmount(amount);
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
        int x = resourceX;
        for(final ResourceType type : ResourceType.values()) {
            if(type != ResourceType.EMPTY) {
                Texture texture = new Texture("Cards/" + type.toString().toLowerCase() + "Card.png");
                ImageButton.ImageButtonStyle imageButtonStyle = new ImageButton.ImageButtonStyle();
                imageButtonStyle.imageUp = new TextureRegionDrawable(texture);
                ImageButton imageButton = new ImageButton(imageButtonStyle);
                imageButton.setPosition(x - 25, 550);
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

    private void setUpOutgoingOfferWantedDecreaseButtons() {
        int x = resourceX;
        for(final ResourceType type : ResourceType.values()) {
            if(type != ResourceType.EMPTY) {
                Label.LabelStyle labelStyle = new Label.LabelStyle();
                labelStyle.font = new BitmapFont();
                labelStyle.font.getData().setScale(2f);
                labelStyle.fontColor = Color.SALMON;
                Label resourceLabel = new Label(outgoingOffer.getWantedOfferResourceAmount(type).toString(), labelStyle);
                resourceLabel.setPosition(x, 350);
                Texture texture = new Texture("Cards/" + type.toString().toLowerCase() + "Card.png");
                ImageButton.ImageButtonStyle imageButtonStyle = new ImageButton.ImageButtonStyle();
                imageButtonStyle.imageUp = new TextureRegionDrawable(texture);
                ImageButton imageButton = new ImageButton(imageButtonStyle);
                imageButton.setPosition(x - 25, 400);
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
        int x = resourceX;
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
        int x = resourceX;
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
        TextButton button = new TextButton("Send Offer", textButtonStyleSetUp());
        button.addListener(new ClickListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    try {
                        for(int i = 0; i < playersAmount; i++) {
                            if(i != id) {
                                outgoingOffer.addPlayer(i);
                            }
                        }
                        outputStream.writeObject(outgoingOffer);
                        outputStream.reset();
                        System.out.println("Offer sent");
                        return true;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
            }
        });
        button.setPosition(600, 0);
        button.setSize(200, 100);
        outgoingOfferStage.addActor(button);
    }

    private synchronized void setUpRejectOfferButton() {
        TextButton button = new TextButton("Reject Offer", textButtonStyleSetUp());
        button.addListener(new ClickListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                try {
                    outputStream.writeObject("Offer rejected by:" + id);
                    outputStream.reset();
                    System.out.println("Offer rejected");
                    return true;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        button.setPosition(900, 100);
        button.setSize(200, 100);
        incomingOfferStage.addActor(button);
    }

    private synchronized void setupConfirmOfferButton() {
        TextButton button = new TextButton("Confirm Offer", textButtonStyleSetUp());
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
        button.setPosition(900, 0);
        button.setSize(200, 100);
        incomingOfferStage.addActor(button);
    }

    private TextButton.TextButtonStyle textButtonStyleSetUp() {
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = new BitmapFont();
        textButtonStyle.font.getData().setScale(2f);
        textButtonStyle.fontColor = Color.BLACK;
        Texture buttonTextureUp = new Texture("Textures/buttonUp.png");
        Texture buttonTextureDown = new Texture("Textures/buttonDown.png");
        textButtonStyle.up = new TextureRegionDrawable(buttonTextureUp);
        textButtonStyle.down = new TextureRegionDrawable(buttonTextureDown);
        return textButtonStyle;
    }

    public synchronized void addOutgoingOfferAcceptance(final int id) {
        outgoingOffer.addAcceptance(id);
        Gdx.graphics.requestRendering();
    }

    public synchronized void addOutgoingOfferRejection(final int id) {
        outgoingOffer.addRejection(id);
        Gdx.graphics.requestRendering();
    }

    private String colorHelper(int id) {
        switch (id) {
            case 0:
                return "yellow";
            case 1:
                return "blue";
            case 2:
                return "green";
            case 3:
                return "red";
        }
        return null;
    }

    public synchronized void addIncomingOfferAcceptance(int id) {
        incomingOffer.addAcceptance(id);
        Gdx.graphics.requestRendering();
    }

    public synchronized void addIncomingOfferRejection(int id) {
        incomingOffer.addRejection(id);
        Gdx.graphics.requestRendering();
    }

    private void drawIncomingOfferIndicators() {
        for(int id : incomingOffer.getPlayers().keySet()) {
            if(incomingOffer.getPlayers().get(id) == 1) {
                addIncomingAcceptance(id);
            }
            else if(incomingOffer.getPlayers().get(id) == 2) {
                addIncomingRejection(id);
            }
            else {
                addIncomingOfferIndicator(id);

            }
        }

    }

    private void addIncomingAcceptance(int id) {
        playerIndicatorBatch.begin();
        String color = colorHelper(id);
        Texture texture = new Texture("Indicators/" + color + "Accepted.png");
        playerIndicatorBatch.draw(texture, 100, 500 - id * 100, 50, 50);
        playerIndicatorBatch.end();
    }

    private void addIncomingRejection(int id) {
        playerIndicatorBatch.begin();
        String color = colorHelper(id);
        Texture texture = new Texture("Indicators/" + color + "Declined.png");
        playerIndicatorBatch.draw(texture, 100, 500 - id * 100, 50, 50);
        playerIndicatorBatch.end();
    }

    private void addIncomingOfferIndicator(int id) {
        playerIndicatorBatch.begin();
        String color = colorHelper(id);
        Texture texture = new Texture("Indicators/" + color + "Indicator.png");
        playerIndicatorBatch.draw(texture, 100, 500 - id * 100, 50, 50);
        playerIndicatorBatch.end();
    }

    private void drawOutcomingOfferIndicators() {
        for(int id : outgoingOffer.getPlayers().keySet()) {
            if(outgoingOffer.getPlayers().get(id) == 1) {
                addOutgoingAcceptance(id);
            }
            else if(outgoingOffer.getPlayers().get(id) == 2) {
                addOutcomingRejection(id);
            }
            else {
                addOutcomingIndicator(id);
            }
        }
    }


    public void addOutcomingIndicator(int id) {
        playerIndicatorBatch.begin();
        String color = colorHelper(id);
        Texture texture = new Texture("Indicators/" + color + "Indicator.png");
        playerIndicatorBatch.draw(texture, 100, 500 - id * 100, 50, 50);
        playerIndicatorBatch.end();
    }
    public void addOutcomingRejection(int id) {
        playerIndicatorBatch.begin();
        String color = colorHelper(id);
        Texture texture = new Texture("Indicators/" + color + "Declined.png");
        playerIndicatorBatch.draw(texture, 100, 500 - id * 100, 50, 50);
        playerIndicatorBatch.end();
    }

    public void addOutgoingAcceptance(final int id) {

        String color = colorHelper(id);
        Texture texture = new Texture("Indicators/" + color + "Accepted.png");
        ImageButton.ImageButtonStyle imageButtonStyle = new ImageButton.ImageButtonStyle();
        imageButtonStyle.imageUp = new TextureRegionDrawable(texture);
        ImageButton imageButton = new ImageButton(imageButtonStyle);
        imageButton.setPosition(100, 500 - id * 100);
        imageButton.setSize(50, 50);
        imageButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                try {
                    outputStream.writeObject("Accepted trade:" + id);
                    outputStream.reset();
                    System.out.println("Offer accepted");
                    return true;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        acceptanceIndicators.put(id, imageButton);
        indicatorStage.addActor(imageButton);
    }

    private synchronized boolean increaseTradeOfferGivenLabel(ResourceType type) {
        if(outgoingOffer.getGivenOfferResourceAmount(type) < deck.getResourceAmount(type)) {
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
        for(int i = 0; i < playersAmount; i++) {
            if(i == currentPlayerIndex) {
                isTurnBackground.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
                playerIndicatorBatch.draw(isTurnBackground, 1770, y - 10, 150, 70);
            }
            else {
                isNotTurnBackground.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
                playerIndicatorBatch.draw(isNotTurnBackground, 1770, y - 10, 150, 70);
            }
            playerIndicators.get(i).setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            playerIndicatorBatch.draw(playerIndicators.get(i), 1800, y, 50, 50);
            y -= 100;
        }
    }

    public void disposeIndicatorStage() {
        indicatorStage.clear();
    }

    @Override
    public void dispose() {
        batch.dispose();
        stage.dispose();
        UIStage.dispose();
        resourceFieldStage.dispose();
        outgoingOfferStage.dispose();
        incomingOfferStage.dispose();
        indicatorStage.dispose();
        backgroundBatch.dispose();
        playerIndicatorBatch.dispose();
        background.dispose();
        robber.dispose();
        isTurnBackground.dispose();
        isNotTurnBackground.dispose();
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
        if(initialSequence) {
            if(currentPlayerIndex == playersAmount - 1) {
                initialSequence = false;
                secondSequence = true;
            }
            else {
                currentPlayerIndex++;
            }
        }
        else if(secondSequence) {
            if(currentPlayerIndex == 0) {
                secondSequence = false;
            }
            else {
                currentPlayerIndex--;
            }
        }
        else {
            currentPlayerIndex = (currentPlayerIndex + 1) % playersAmount;
        }
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
            this.deck.setDeck(deck);
        }
        Gdx.graphics.requestRendering();
    }

    public synchronized void setIncomingOffer(Offer offer) {
        incomingOffer.setOffer(offer.getGivenOffer(), offer.getWantedOffer());
        for(ResourceType type : ResourceType.values()) {
            if(type != ResourceType.EMPTY) {
                incomingOfferGivenDisplays.get(type).changeAmount(incomingOffer.getGivenOfferResourceAmount(type));
                incomingOfferWantedDisplays.get(type).changeAmount(incomingOffer.getWantedOfferResourceAmount(type));
            }
        }
        incomingOffer.setPlayers(offer.getPlayers());
        System.out.println(incomingOffer.getPlayers());
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
    public synchronized void setPoints(int points) {
        this.points = points;
    }

    public synchronized void resetOffer() {
        outgoingOffer = new Offer(this.id);
        for(ResourceType type : ResourceType.values()) {
            if(type != ResourceType.EMPTY) {
                offerGivenButtons.get(type).changeAmount(0);
                offerWantedButtons.get(type).changeAmount(0);;
            }
        }
        isOfferBeingCreated = false;
        hasOfferBeenCreated = false;
        isOfferBeingReceived = false;
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

    public synchronized int getPlayersAmount() {
        return playersAmount;
    }

    public synchronized Offer getIncomingOffer() {
        return incomingOffer;
    }

    public synchronized boolean hasOfferBeenCreated() {
        return hasOfferBeenCreated;
    }
}
