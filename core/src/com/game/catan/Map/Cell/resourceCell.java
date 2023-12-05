package com.game.catan.Map.Cell;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.util.ArrayList;

public class resourceCell extends Cell {
    private final ResourceType type;
    private final int diceThrow;
    private boolean hasRobber = false;


    public resourceCell(int x, int y, ResourceType type) {
        super(x, y);
        this.type = type;
        setTextureAndStyle(type);
        this.diceThrow = randomDiceThrow();
    }

    public resourceCell(int x, int y, ResourceType type, int diceThrow) {
        super(x, y);
        this.type = type;
        setTextureAndStyle(type);
        this.diceThrow = diceThrow;
    }

    public void setTextureAndStyle(ResourceType type){
        switch (type) {
            case WOOD:
                this.texture = new Texture("wood.png");
                break;
            case BRICK:
                this.texture = new Texture("brick.png");
                break;
            case SHEEP:
                this.texture = new Texture("sheep.png");
                break;
            case WHEAT:
                this.texture = new Texture("wheat.png");
                break;
            case STONE:
                this.texture = new Texture("stone.png");
                break;
            case EMPTY:
                this.texture = new Texture("empty.png");
                break;
        }
        this.style = new ImageButton.ImageButtonStyle();
        this.style.imageUp = new TextureRegionDrawable(this.texture);
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
        if (cell instanceof resourceCell) {
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
    public void draw() {
        if(hasRobber) {
            texture = new Texture("robber.png");
            this.style.imageUp = new TextureRegionDrawable(texture);
        }
        else {
            texture = new Texture("empty.png");
            this.style.imageUp = new TextureRegionDrawable(texture);
        }
    }
}
