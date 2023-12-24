package com.game.catan.Map.Cell;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.util.ArrayList;

public class ResourceCell extends Cell{
    private final ResourceType type;
    private final int diceThrow;
    private boolean hasRobber = false;


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

    public void setTexturePath(ResourceType type){
        switch (type) {
            case WOOD:
                this.texturePath = "wood.png";
                break;
            case BRICK:
                this.texturePath = "brick.png";
                break;
            case SHEEP:
                this.texturePath = "sheep.png";
                break;
            case WHEAT:
                this.texturePath = "wheat.png";
                break;
            case STONE:
                this.texturePath = "stone.png";
                break;
            case EMPTY:
                this.texturePath = "empty.png";
                break;
        }
    }

    public String getResource() {
        return this.type.toString();
    }

    public int getDiceThrow() {
        return this.diceThrow;
    }

    public int randomDiceThrow() {
        return (int) (Math.random() * 10) + 2;
    }

    public void setRobber(boolean hasRobber) {
        this.hasRobber = hasRobber;
    }

    public boolean HasRobber() {
        return this.hasRobber;
    }

    public void addNeighbour(Cell cell) {
        if (cell instanceof ResourceCell) {
            super.addNeighbour(cell);
        }
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
    public void buttonFunc(Stage stage) {
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.imageUp = new TextureRegionDrawable(new Texture(texturePath));
        ImageButton button = new ImageButton(style);
        button.setPosition(this.getCellCords().getX(), this.getCellCords().getY());
        stage.addActor(button);
        button.addListener(new com.badlogic.gdx.scenes.scene2d.InputListener() {
            public boolean touchDown(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Button clicked");
                return true;
            }
        });
    }
}
