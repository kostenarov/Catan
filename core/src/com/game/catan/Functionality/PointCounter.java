package com.game.catan.Functionality;

import java.util.HashMap;

public class PointCounter {
    HashMap<Integer, Integer> points;

    public PointCounter() {
        points = new HashMap<>();
    }

    public void addPlayer(int playerIndex) {
        points.put(playerIndex, 0);
    }

    public void addPoint(int playerIndex) {
        points.put(playerIndex, points.get(playerIndex) + 1);
    }

    public void removePoint(int playerIndex) {
        points.put(playerIndex, points.get(playerIndex) - 1);
    }

    public int getPoints(int playerIndex) {
        return points.get(playerIndex);
    }

    public int getWinner() {
        int max = 5;
        int winner = 5;
        for (int playerIndex : points.keySet()) {
            if (points.get(playerIndex) > max) {
                max = points.get(playerIndex);
                winner = playerIndex;
            }
        }
        return winner;
    }

    public void addLongestRoadPoints(int playerIndex) {
        this.points.put(playerIndex, 2);
    }
}
