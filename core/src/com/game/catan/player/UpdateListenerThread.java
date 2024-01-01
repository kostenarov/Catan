package com.game.catan.player;

import com.game.catan.Map.Map;
import java.io.ObjectInputStream;
import java.util.HashMap;

public class UpdateListenerThread extends Thread{
    private CatanPlayer player;
    private ObjectInputStream in;
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
                    if((int) input < 20) {
                        player.setDiceThrow((Integer) input);
                    }
                    else if((int) input == 100) {
                        System.out.println("Your turn");
                        player.setIsTurn(true);
                        player.setDiceThrown(false);
                    }
                    else if((int) input == 200) {
                        System.out.println("Not your turn");
                        player.setIsTurn(false);
                        player.sendMap();
                    }
                }
                else if(input instanceof Map) {
                    System.out.println("Map received");
                    player.setMap((Map) input);
                }
                else if(input instanceof HashMap) {
                    System.out.println("Deck received");

                    player.setDeck((HashMap<?, ?>) input);
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
