package com.game.catan.player;

public class SenderThread implements Runnable{
    private final CatanPlayer player;
    private final String message;

    public SenderThread(CatanPlayer player, String message) {
        this.player = player;
        this.message = message;
    }

    @Override
    public void run() {

    }
}
