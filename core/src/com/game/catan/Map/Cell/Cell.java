package com.game.catan.Map.Cell;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.game.catan.player.CatanPlayer;

import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Cell implements Serializable {
    private static int idCounter = 0;
    protected final int id = idCounter++;
    private final Point cellCords;
    private final List<Cell> neighbours = new ArrayList<>();
    protected String texturePath;

    public Cell(int x, int y) {
        cellCords = new Point(x, y);
    }

    public Point getCellCords() {
        return cellCords;
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
    public abstract void buttonFunc(Stage stage, ObjectOutputStream outputStream, CatanPlayer player);
    public int getId() {
        return id;
    }
}
