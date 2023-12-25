package com.game.catan.Map.Cell;

import java.io.Serializable;

public class Point implements Serializable {
    private final int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() { return x; }

    public int getY() { return y; }
}
