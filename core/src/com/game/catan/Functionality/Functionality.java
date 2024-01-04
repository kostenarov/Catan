package com.game.catan.Functionality;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.game.catan.Map.Cell.ResourceCell;
import com.game.catan.Map.Cell.ResourceType;
import com.game.catan.Map.Cell.VillageCell;
import com.game.catan.Map.Map;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class Functionality {
    public static int diceThrow() {
        return (int) (Math.random() * 10 + 2);
    }

    public int getNumberOfPlayerVillages(ResourceCell cell, int playerId) {
        int numberOfPlayerVillages = 0;
        for(VillageCell villageCell : cell.getVillages()) {
            if(villageCell.getOwner().getId() == playerId) {
                numberOfPlayerVillages++;
            }
        }
        return numberOfPlayerVillages;
    }

    public static Deck getResources(int diceThrow, Map map, HashMap<ResourceType, Integer> resources, int playerId) {
        Deck deck = new Deck();
        for (ResourceCell cell : map.getResourceCells(map.getCenterCell())) {
            if (cell.getDiceThrow() == diceThrow && !cell.hasRobber() && cell.getResource() != ResourceType.EMPTY) {
                //if (cell.getNumberOfPlayerVillages(playerId) > 0) {
                    deck.addResource(cell.getResource());
                    resources.put(cell.getResource(), resources.get(cell.getResource()) + 1);
                //}
            }
        }
        return deck;
    }

    public static void setUpButtonFunc(Stage stage, TextButton endTurnButton, final boolean isTurn, final ObjectOutputStream outputStream) {
        stage.addActor(endTurnButton);
        endTurnButton.addListener(new com.badlogic.gdx.scenes.scene2d.InputListener() {
            public boolean touchDown(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y, int pointer, int button) {
                try {
                    if(isTurn) {
                        System.out.println("End turn");
                        outputStream.writeObject("End Turn");
                        outputStream.reset();
                    }
                } catch (IOException e) {
                    System.out.println("Could not send end turn");
                }
                return true;
            }
        });
    }
}
