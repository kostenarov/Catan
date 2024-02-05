package com.game.catan.Map.Cell;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.game.catan.player.CatanPlayer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ResourceCell extends Cell{
    private final ResourceType type;
    private final int diceThrow;
    private boolean hasRobber = false;
    private ImageButton.ImageButtonStyle style;
    private Label resourceDice;
    private ImageButton button;
    private Texture buttonTexture;


    public ResourceCell(int x, int y, ResourceType type) {
        super(x, y);
        this.type = type;
        setTexturePath(type);
        this.diceThrow = randomDiceThrow();
    }

    public ResourceCell(int x, int y, ResourceType type, int diceThrow) {
        super(x, y);
        this.type = type;
        setTexturePath(type);
        this.diceThrow = diceThrow;
    }

    @Override
    public void addNeighbour(Cell neighbour) {
        neighbours.add(neighbour);
        if(!neighbour.getNeighbours().contains(this))
            neighbour.addNeighbour(this);
    }

    public void setTexturePath(ResourceType type){
        switch (type) {
            case WOOD:
                this.texturePath = "Resources/wood.png";
                break;
            case BRICK:
                this.texturePath = "Resources/brick.png";
                break;
            case SHEEP:
                this.texturePath = "Resources/sheep.png";
                break;
            case WHEAT:
                this.texturePath = "Resources/wheat.png";
                break;
            case STONE:
                this.texturePath = "Resources/stone.png";
                break;
            case EMPTY:
                this.texturePath = "Resources/empty.png";
                break;
        }
    }

    public ResourceType getResource() {
        return this.type;
    }

    public int getDiceThrow() {
        return this.diceThrow;
    }

    public int randomDiceThrow() {
        int temp = (int) (Math.random() * 10 + 2);
        if(temp == 7) {
            return randomDiceThrow();
        }
        return temp;
    }

    public void setRobber(boolean hasRobber) {
        this.hasRobber = hasRobber;
    }

    public boolean hasRobber() {
        return this.hasRobber;
    }

    public ArrayList<VillageCell> getVillages() {
        ArrayList<VillageCell> neighbours = new ArrayList<>();
        for(Cell neighbour : getNeighbours()) {
            if(neighbour instanceof VillageCell) {
                neighbours.add((VillageCell) neighbour);
            }
        }
        return neighbours;
    }

    @Override
    public void buttonFunc(Stage stage, final ObjectOutputStream outputStream, final CatanPlayer player) {
        if(button == null) {
            setUpResourceButtonStyle();
            button = new ImageButton(style);
            button.addListener(new com.badlogic.gdx.scenes.scene2d.InputListener() {
                public boolean touchDown(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y, int pointer, int button) {
                    System.out.println("Button clicked");
                    if (player.getIsTurn()) {
                        try {
                            outputStream.writeObject("Clicked Resource button:" + id);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    return true;
                }
            });
            button.setSize(145, 145);
            button.setPosition(this.getCellCords().getX() - 20, this.getCellCords().getY() - 20);
            stage.addActor(button);
            setUpLabel(stage);
        }
    }

    @Override
    public void drawWithoutFunc(Stage stage) {
        setUpLabel(stage);
        if(button == null) {
            button = new ImageButton(style);
        }
        button.setSize(130, 130);
        button.setPosition(this.getCellCords().getX(), this.getCellCords().getY());
        stage.addActor(button);
    }

    private void setUpLabel(Stage stage) {
        if(type != ResourceType.EMPTY) {
            resourceDice = new Label(Integer.toString(diceThrow), new Label.LabelStyle(new com.badlogic.gdx.graphics.g2d.BitmapFont(), Color.BLUE));
            resourceDice.setPosition(this.getCellCords().getX() + 50, this.getCellCords().getY() + 50);
            resourceDice.setFontScale(2);
            stage.addActor(resourceDice);
        }
    }

    private void setUpResourceButtonStyle() {
        style = new ImageButton.ImageButtonStyle();
        buttonTexture = new Texture(texturePath);
        style.imageUp = new TextureRegionDrawable(buttonTexture);
    }
}
