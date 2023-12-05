package com.game.catan.Map.Cell;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.game.catan.player.CatanPlayer;

public class VillageCell extends Cell {
    public CatanPlayer owner = null;
    private Texture villageTexture;
    private ImageButton.ImageButtonStyle villageStyle;
    public VillageCell(int x, int y) {
        super(x, y);
    }

    public CatanPlayer getOwner() {
        return owner;
    }

    public void setOwner(CatanPlayer owner) {
        if(this.owner == null)
            this.owner = owner;
        else {
            throw new RuntimeException("Village already has an owner");
        }
    }

    public boolean hasNeighbours() {
        return !getNeighbours().isEmpty();
    }

    @Override
    public void draw() {
        if(owner != null) {
            villageTexture = new Texture(owner.getVillagePath());
            villageStyle.imageUp = new TextureRegionDrawable(villageTexture);
        }
        else {
            villageTexture = null;
            villageStyle = new ImageButton.ImageButtonStyle();
            villageStyle.imageUp = new TextureRegionDrawable(villageTexture);
        }
    }
}
