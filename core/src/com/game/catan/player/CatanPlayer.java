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
    private int points = 0;
    private static final int resourceX = 200;

    private static final int SCREEN_WIDTH = 1920;
    private static final int SCREEN_HEIGHT = 1080;
    
    private static final int DEFAULT_WIDTH = 50;
    private static final int DEFAULT_HEIGHT = 50;

    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 100;

    private static final int RULES_HEIGHT = 500;
    private static final int RULES_WIDTH = 400;

    private static final int PLAYER_INDICATOR_X = 1770;
    private static final int PLAYER_INDICATOR_Y = 700;

    private static final int PLAYER_BACKGROUND_WIDTH = 150;
    private static final int PLAYER_BACKGROUND_HEIGHT = 70;

    private static final int START_MENU_BUTTON_X = 800;
    private static final int START_MENU_BUTTON_Y = 400;

    private static final int CLOSE_BUTTON_X = 1870;
    private static final int CLOSE_BUTTON_Y = 1010;

    private static final int RULES_X = 100;
    private static final int RULES_Y = 500;

    private static final int LABELS_X = 1500;
    private static final int LABELS_Y = 50;

    private static final int INCOMING_OFFER_DISPLAYS_X = 1400;
    private static final int INCOMING_OFFER_DISPLAYS_Y = 700;

    private static final int MANAGE_OFFER_X = 600;
    private static final int MANAGE_OFFER_Y = 30;

    private static final int OUTGOING_OFFER_X = 100;
    private static final int OUTGOING_OFFER_Y = 500;

    private boolean isTurn = true;
    private boolean isLoss = false;
    private boolean isWin = false;
    private boolean hasStarted = false;
    private boolean rulesOn = false;
    private boolean secondRulesOn = false;
    private boolean isDiceThrown = false;
    private boolean initialSequence = true;
    private boolean secondSequence = false;
    private boolean isOfferBeingCreated = false;
    private boolean hasOfferBeenCreated = false;
    private boolean isOfferBeingReceived = false;
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
    private Stage startMenuStage;
    private Stage rulesStage;
    private Stage secondRulesStage;
    private SpriteBatch batch;
    private SpriteBatch backgroundBatch;
    private SpriteBatch playerIndicatorBatch;
    private Texture background;
    private Texture resourceBackground;
    private Texture isTurnBackground;
    private Texture isNotTurnBackground;
    private Texture robber;
    private Image robberImage;
    private Image outgoingTradeBackground;
    private Image incomingTradeBackground;
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
        setUpStages();
        background = new Texture("Backgrounds/background.png");
        resourceBackground = new Texture("Backgrounds/resourceBackground.png");
        robber = new Texture("Textures/robber.png");
        isTurnBackground = new Texture("Textures/isTurn.png");
        isNotTurnBackground = new Texture("Textures/isNotTurn.png");
        outgoingTradeBackground = new Image(new Texture("Textures/tradeBackground.png"));
        outgoingTradeBackground.setSize(375, 600);
        incomingTradeBackground = new Image(new Texture("Textures/tradeBackground.png"));
        incomingTradeBackground.setSize(375, 300);
        initiateIndicators();
        robberImage = new Image(robber);
        robberImage.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        connectToServer();
        updateThread = new UpdateListenerThread(this, inputStream);
        updateThread.start();
        setUps();
    }

    private void setUpStages() {
        UIStage = new Stage(new ScreenViewport());
        resourceFieldStage = new Stage(new ScreenViewport());
        outgoingOfferStage = new Stage(new ScreenViewport());
        incomingOfferStage = new Stage(new ScreenViewport());
        winStage = new Stage(new ScreenViewport());
        lossStage = new Stage(new ScreenViewport());
        stage = new Stage(new ScreenViewport());
        indicatorStage = new Stage(new ScreenViewport());
        startMenuStage = new Stage(new ScreenViewport());
        rulesStage = new Stage(new ScreenViewport());
        secondRulesStage = new Stage(new ScreenViewport());
    }

    private void setUps() {
        drawButtons();
        setUpStartMenu();
        setUpInitialLabels();
        setUpEndScreens();
        setUpSendOfferButton();
        setUpResetOfferButton();
        setupConfirmOfferButton();
        setUpRejectOfferButton();
        setUpOutgoingOfferGivenButtons();
        setUpOutgoingOfferWantedIncreaseButtons();
        setUpOutgoingOfferWantedDecreaseButtons();
        setUpResourceCards();
        setUpIncomingOfferDisplays();
        displayResources();
        rulesLogic();
        secondRulesLogic();
        addRules();
        outgoingTradeBackground.setPosition(150, 100);
        incomingTradeBackground.setPosition(1600, 800);
    }

    @Override
    public void render() {
        if(!hasStarted) {
            if(rulesOn && !secondRulesOn) {
                rulesMenuLogic();
            }
            else if(secondRulesOn && rulesOn) {
                secondRulesMenuLogic();
            }
            else if(!rulesOn) {
                startMenuLogic();
                secondRulesOn = false;
            }
        }
        else if(!isLoss && !isWin) {
            mainLoopLogic();
        }
        else {
            endGameLogic();
        }
        Gdx.graphics.setContinuousRendering(false);
        Gdx.input.setInputProcessor(new InputMultiplexer(stage, startMenuStage, rulesStage, secondRulesStage, UIStage, resourceFieldStage,
                outgoingOfferStage, incomingOfferStage, indicatorStage));
    }

    private void rulesLogic(){
        ImageButton closeButton = new ImageButton(ButtonSetUps.setUpCloseButton());
        closeButton.setPosition(CLOSE_BUTTON_X, CLOSE_BUTTON_Y);
        closeButton.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        closeButton.addListener(new ClickListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                rulesOn = false;
                System.out.println("Close button clicked");
                return true;
            }
        });
        TextButton secondRulesButton = ButtonSetUps.setUpTextButton("Next page", 150);
        secondRulesButton.setPosition(1700, START_MENU_BUTTON_Y - 200);
        secondRulesButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        secondRulesButton.setStyle(textButtonStyleSetUp());
        secondRulesButton.addListener(new ClickListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(!secondRulesOn){
                    secondRulesOn = true;
                }
                return true;
            }
        });
        secondRulesStage.addActor(closeButton);
        rulesStage.addActor(closeButton);
        rulesStage.addActor(secondRulesButton);

    }
    
    private void secondRulesLogic(){
        ImageButton closeButton = new ImageButton(ButtonSetUps.setUpCloseButton());
        closeButton.setPosition(CLOSE_BUTTON_X, CLOSE_BUTTON_Y);
        closeButton.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        closeButton.addListener(new ClickListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                secondRulesOn = false;
                rulesOn = false;
                return true;
            }
        });

        TextButton rulesButton = ButtonSetUps.setUpTextButton("Previous page", 150);
        rulesButton.setPosition(200, START_MENU_BUTTON_Y - 100);
        rulesButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        rulesButton.setStyle(textButtonStyleSetUp());
        rulesButton.addListener(new ClickListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(secondRulesOn){
                    secondRulesOn = false;
                    System.out.println("Close button clicked");
                }
                System.out.println("Close button clicked");

                return true;
            }
        });
        secondRulesStage.addActor(rulesButton);
        secondRulesStage.addActor(closeButton);
    }

    private void addRules() {
        final Image buildingsRules = new Image(new Texture("Rules/Buildings.png"));
        Image resourceRules = new Image(new Texture("Rules/Resources.png"));
        Image diceRules = new Image(new Texture("Rules/Dice.png"));
        Image tradingRules = new Image(new Texture("Rules/Trading.png"));

        buildingsRules.setPosition(RULES_X, RULES_Y);
        buildingsRules.setSize(RULES_WIDTH, RULES_HEIGHT);

        resourceRules.setPosition(RULES_X + 400, RULES_Y);
        resourceRules.setSize(RULES_WIDTH, RULES_HEIGHT);

        diceRules.setPosition(RULES_X + 800, RULES_Y);
        diceRules.setSize(RULES_WIDTH, RULES_HEIGHT);
        
        tradingRules.setPosition(RULES_X, RULES_Y);
        tradingRules.setSize(RULES_WIDTH * 2 - 100, RULES_HEIGHT);
        rulesStage.addActor(resourceRules);
        rulesStage.addActor(buildingsRules);
        rulesStage.addActor(diceRules);
        secondRulesStage.addActor(tradingRules);
    }

    private void startMenuLogic(){
        backgroundBatch.begin();
        backgroundBatch.draw(background, 0,0 );
        backgroundBatch.end();
        startMenuStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        startMenuStage.draw();
    }

    private void rulesMenuLogic () {
        backgroundBatch.begin();
        backgroundBatch.draw(background, 0,0);
        backgroundBatch.end();
        rulesStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        rulesStage.draw();
    }

    private void secondRulesMenuLogic () {
        backgroundBatch.begin();
        backgroundBatch.draw(background, 0,0);
        backgroundBatch.end();
        secondRulesStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        secondRulesStage.draw();
    }

    private void mainLoopLogic() {
        drawBackgrounds();
        backgroundBatch.begin();
        backgroundBatch.draw(background, 0,0 );
        backgroundBatch.end();
        resourceFieldStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        resourceFieldStage.draw();
        UIStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        UIStage.draw();
        drawRobber();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

        renderNormalRound();
        drawPlayerIndicator();

        if(isOfferBeingCreated || hasOfferBeenCreated) {
            outgoingOfferStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
            outgoingOfferStage.draw();

            indicatorStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
            indicatorStage.draw();
            drawOutcomingOfferIndicators();
        }
        if(rulesOn && !secondRulesOn) {
            rulesMenuLogic();
        }
        else if(secondRulesOn && rulesOn) {
            secondRulesMenuLogic();
        }
        if(isOfferBeingReceived) {
            incomingOfferStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
            incomingOfferStage.draw();

            indicatorStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
            indicatorStage.draw();
            drawIncomingOfferIndicators();
        }
    }

    private void endGameLogic() {
        backgroundBatch.begin();
        backgroundBatch.draw(background, 0,0 );
        backgroundBatch.end();
        if(isLoss) {
            lossStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
            lossStage.draw();
        }
        else {
            winStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
            winStage.draw();
        }
    }

    private void initiateIndicators() {
        Texture yellowIndicator = new Texture("Indicators/yellowIndicator.png");
        Texture blueIndicator = new Texture("Indicators/blueIndicator.png");
        Texture redIndicator = new Texture("Indicators/redIndicator.png");
        Texture greenIndicator = new Texture("Indicators/greenIndicator.png");
        this.playerIndicators.put(0, yellowIndicator);
        this.playerIndicators.put(1, blueIndicator);
        this.playerIndicators.put(2, greenIndicator);
        this.playerIndicators.put(3, redIndicator);
    }

    private void setUpInitialLabels() {
        Label.LabelStyle pointsLabelStyle = new Label.LabelStyle();
        pointsLabelStyle.font = new BitmapFont();
        pointsLabelStyle.font.getData().setScale(2f);
        pointsLabelStyle.fontColor = Color.SALMON;
        pointsLabel = new Label("Points: " + points, pointsLabelStyle);
        pointsLabel.setPosition(LABELS_X, LABELS_Y);
        diceThrowLabel = new Label("Dice throw: " + diceThrow, pointsLabelStyle);
        diceThrowLabel.setPosition(LABELS_X, LABELS_Y + 50);
        UIStage.addActor(pointsLabel);
        UIStage.addActor(diceThrowLabel);
    }

    private void setUpIncomingOfferDisplays() {
        int x = INCOMING_OFFER_DISPLAYS_X;
        for(ResourceType type : ResourceType.values()) {
            if (type != ResourceType.EMPTY) {
                setUpIncomingOfferDisplay(type.toString(), incomingOffer.getGivenOfferResourceAmount(type).toString(), x, INCOMING_OFFER_DISPLAYS_Y + 200, incomingOfferGivenDisplays);
                setUpIncomingOfferDisplay(type.toString(), incomingOffer.getWantedOfferResourceAmount(type).toString(), x, INCOMING_OFFER_DISPLAYS_Y, incomingOfferWantedDisplays);
                x += 75;
            }
        }
    }

    private void setUpIncomingOfferDisplay(String type, String value, int x, int y, HashMap<ResourceType, ResourceDisplay> displays) {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = new BitmapFont();
        labelStyle.font.getData().setScale(2f);
        labelStyle.fontColor = Color.SALMON;
        Label resourceLabel = new Label(value, labelStyle);
        resourceLabel.setPosition(x, y);
        Texture texture = new Texture("Cards/" + type.toLowerCase() + "Card.png");
        Image image = new Image(texture);
        image.setPosition(x - 25, y + 50);
        ResourceDisplay resourceDisplay = new ResourceDisplay(image, resourceLabel);
        displays.put(ResourceType.valueOf(type), resourceDisplay);
        resourceDisplay.draw(incomingOfferStage);
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

    private void setUpStartMenu() {
        TextButton startButton = ButtonSetUps.setUpTextButton("Start", 50);
        TextButton rulesButton = ButtonSetUps.setUpTextButton("Rules", 150);
        startButton.addListener(new ClickListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                hasStarted = true;
                return true;
            }
        });
        startButton.setPosition(START_MENU_BUTTON_X, START_MENU_BUTTON_Y);
        startButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        startButton.setStyle(textButtonStyleSetUp());

        rulesButton.addListener(new ClickListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(!rulesOn){
                    rulesOn = true;
                }
                return true;
            }
        });
        startMenuStage.addActor(startButton);
        rulesButton.setPosition(START_MENU_BUTTON_X, START_MENU_BUTTON_Y - 100);
        rulesButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        rulesButton.setStyle(textButtonStyleSetUp());
        startMenuStage.addActor(rulesButton);
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
        TextButton rules = ButtonSetUps.setUpTextButton("Rules", 230);
        rules.addListener(new ClickListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(!rulesOn){
                    rulesOn = true;
                }
                return true;
            }
        });
        UIStage.addActor(rules);
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
                        if(outgoingOffer.getPlayerId() != id) {
                            outgoingOffer.setPlayerId(id);
                        }
                        outputStream.writeObject(outgoingOffer);
                        outputStream.reset();
                        return true;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
            }
        });
        button.setPosition(MANAGE_OFFER_X, MANAGE_OFFER_Y);
        button.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        outgoingOfferStage.addActor(button);
    }

    private void setUpResetOfferButton() {
        TextButton button = new TextButton("Reset Offer", textButtonStyleSetUp());
        button.addListener(new ClickListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                outgoingOffer = new Offer(id);
                for(ResourceType type : ResourceType.values()) {
                    if(type != ResourceType.EMPTY) {
                        offerGivenButtons.get(type).changeAmount(0);
                        offerWantedButtons.get(type).changeAmount(0);
                    }
                }
                isOfferBeingCreated = false;
                hasOfferBeenCreated = false;
                return true;
            }
        });
        button.setPosition(MANAGE_OFFER_X, MANAGE_OFFER_Y + 100);
        button.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
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
        button.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
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
        button.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
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
        playerIndicatorBatch.draw(texture, OUTGOING_OFFER_X, OUTGOING_OFFER_Y - id * 100, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        playerIndicatorBatch.end();
    }

    private void addIncomingRejection(int id) {
        playerIndicatorBatch.begin();
        String color = colorHelper(id);
        Texture texture = new Texture("Indicators/" + color + "Declined.png");
        playerIndicatorBatch.draw(texture, OUTGOING_OFFER_X, OUTGOING_OFFER_Y - id * 100, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        playerIndicatorBatch.end();
    }

    private void addIncomingOfferIndicator(int id) {
        playerIndicatorBatch.begin();
        String color = colorHelper(id);
        Texture texture = new Texture("Indicators/" + color + "Indicator.png");
        playerIndicatorBatch.draw(texture, OUTGOING_OFFER_X, OUTGOING_OFFER_Y - id * 100, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        playerIndicatorBatch.end();
    }

    private void drawOutcomingOfferIndicators() {
        for(int id : outgoingOffer.getPlayers().keySet()) {
            if(outgoingOffer.getPlayers().get(id) == 1) {
                addOutgoingAcceptance(id);
            }
            else if(outgoingOffer.getPlayers().get(id) == 2) {
                addOutgoingRejection(id);
            }
            else {
                addOutgoingIndicator(id);
            }
        }
    }

    public void addOutgoingIndicator(int id) {
        playerIndicatorBatch.begin();
        String color = colorHelper(id);
        Texture texture = new Texture("Indicators/" + color + "Indicator.png");
        playerIndicatorBatch.draw(texture, OUTGOING_OFFER_X, OUTGOING_OFFER_Y - id * 100, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        playerIndicatorBatch.end();
    }
    public void addOutgoingRejection(int id) {
        playerIndicatorBatch.begin();
        String color = colorHelper(id);
        Texture texture = new Texture("Indicators/" + color + "Declined.png");
        playerIndicatorBatch.draw(texture, OUTGOING_OFFER_X, OUTGOING_OFFER_Y - id * 100, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        playerIndicatorBatch.end();
    }

    public void addOutgoingAcceptance(final int id) {
        String color = colorHelper(id);
        Texture texture = new Texture("Indicators/" + color + "Accepted.png");
        ImageButton.ImageButtonStyle imageButtonStyle = new ImageButton.ImageButtonStyle();
        imageButtonStyle.imageUp = new TextureRegionDrawable(texture);
        ImageButton imageButton = new ImageButton(imageButtonStyle);
        imageButton.setPosition(OUTGOING_OFFER_X, OUTGOING_OFFER_Y - id * 100);
        imageButton.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
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
        int y = PLAYER_INDICATOR_Y;
        for(int i = 0; i < playersAmount; i++) {
            if(i == currentPlayerIndex) {
                isTurnBackground.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
                playerIndicatorBatch.draw(isTurnBackground, PLAYER_INDICATOR_X, y - 10, PLAYER_BACKGROUND_WIDTH, PLAYER_BACKGROUND_HEIGHT);
            }
            else {
                isNotTurnBackground.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
                playerIndicatorBatch.draw(isNotTurnBackground, PLAYER_INDICATOR_X, y - 10, PLAYER_BACKGROUND_WIDTH, PLAYER_BACKGROUND_HEIGHT);
            }
            playerIndicators.get(i).setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            playerIndicatorBatch.draw(playerIndicators.get(i), PLAYER_INDICATOR_X, y, DEFAULT_WIDTH, DEFAULT_HEIGHT);
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
        startMenuStage.dispose();
        resourceFieldStage.dispose();
        outgoingOfferStage.dispose();
        incomingOfferStage.dispose();
        rulesStage.dispose();
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
                offerWantedButtons.get(type).changeAmount(0);
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
