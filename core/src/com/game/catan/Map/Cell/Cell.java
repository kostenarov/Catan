package com.game.catan.Map.Cell;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;

import java.util.ArrayList;
import java.util.List;

public abstract class Cell{
    private final int x;
    private final int y;
    private final List<Cell> neighbours = new ArrayList<>();
    protected String texturePath;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void addNeighbour(Cell neighbour) {
        neighbours.add(neighbour);
    }

    public List<Cell> getNeighbours() {
        return neighbours;
    }

    public String getTexturePath() {
        return texturePath;
    }
}
