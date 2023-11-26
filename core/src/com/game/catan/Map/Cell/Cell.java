package com.game.catan.Map.Cell;

import java.util.ArrayList;
import java.util.List;

public abstract class Cell {
    private final int x;
    private final int y;
    private final List<Cell> neighbours = new ArrayList<>();

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
}
