package com.game.catan.player;

import com.game.catan.Map.Cell.ResourceType;
import com.game.catan.Map.Map;
import java.io.ObjectInputStream;
import java.util.HashMap;

public class UpdateListenerThread extends Thread{
    private final CatanPlayer player;
    private final ObjectInputStream in;
    private boolean isWorking = true;

    public UpdateListenerThread(CatanPlayer player, ObjectInputStream in) {
        this.player = player;
        this.in = in;
    }

    @Override
    public void run() {
        while(isWorking) {
            try {
                Object input = in.readObject();
                System.out.println(input);
                if (input instanceof Integer) {
                    if((int) input < 13) {
                        player.setDiceThrow((Integer) input);
                        player.setDiceThrown(true);
                    }
                    else if((int) input == 100) {
                        System.out.println("Your turn");
                        player.setIsTurn(true);
                        player.setDiceThrown(false);
                    }
                    else if((int) input == 200) {
                        System.out.println("Not your turn");
                        player.setIsTurn(false);
                    }
                }
                else if(input instanceof String) {
                    if(((String) input).contains("Points")) {
                        System.out.println("Points received");
                        player.setPoints(Integer.parseInt(((String) input).split(":")[1]));
                        player.displayPoints();
                    }
                    else if(((String) input).contains("Players")) {
                        System.out.println("Players received");
                        player.setPlayersAmount(Integer.parseInt(((String) input).split(":")[1]));
                    }
                }
                else if(input instanceof Map) {
                    System.out.println("Map received");
                    player.setMap((Map) input);
                }
                else if(input instanceof HashMap) {
                    System.out.println("Deck received");
                    player.setDeck((HashMap<ResourceType, Integer>) input);
                }
            } catch (Exception e) {
                System.out.println("Could not read input");
            }
        }
    }

    public void stopThread() {
        isWorking = false;
    }
}
