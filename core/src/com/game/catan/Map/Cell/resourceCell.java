package com.game.catan.Map.Cell;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class resourceCell extends Cell {
    private final ResourceType type;
    private final int diceThrow;
    private boolean hasRobber = false;
    private Texture texture;
    private ImageButton.ImageButtonStyle resourceStyle;


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
        this.resourceStyle = new ImageButton.ImageButtonStyle();
        this.resourceStyle.imageUp = new TextureRegionDrawable(this.texture);
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

    public void setHasRobber(boolean hasRobber) {
        this.hasRobber = hasRobber;
    }

    public boolean getHasRobber() {
        return this.hasRobber;
    }

    public Texture getTexture() {
        return this.texture;
    }

    public void dispose() {
        this.texture.dispose();
    }

    public ImageButton.ImageButtonStyle getResourceStyle() {
        return this.resourceStyle;
    }
}
