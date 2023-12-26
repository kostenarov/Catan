package com.game.catan.player;

import com.game.catan.Map.Map;

import java.io.ObjectInputStream;

public class UpdateListenerThread extends Thread{
    private CatanPlayer player;
    private ObjectInputStream in;

    public UpdateListenerThread(CatanPlayer player, ObjectInputStream in) {
        this.player = player;
        this.in = in;
    }

    @Override
    public void run() {
        while(true) {
            try {
                Object input = in.readObject();
                System.out.println(input);
                if (input instanceof Integer) {
                    player.setDiceThrow((Integer) input);
                }
                else if(input instanceof String) {
                    if(input.equals("Your Turn:" + player.getId())) {
                        player.setIsTurn(true);
                        player.setDiceThrown(false);
                    }
                    else if(input.equals("Not your turn:" + player.getId())) {
                        player.setIsTurn(false);
                        player.sendMap();
                    }
                }
                else if(input instanceof Map) {
                    player.setMap((Map) input);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
