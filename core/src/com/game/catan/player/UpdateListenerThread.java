package com.game.catan.player;

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
                if (input instanceof Integer) {
                    player.setDiceThrow((Integer) input);
                }
                else if(input instanceof String) {
                    if(input.equals("Your Turn" + player.getId())) {
                        player.setIsTurn(true);
                    }
                    else if(input.equals("Not your turn" + player.getId())) {
                        player.setIsTurn(false);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
