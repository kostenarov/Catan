package com.game.catan.player;

import java.io.ObjectOutputStream;

public class SenderThread  extends Thread{
    private final CatanPlayer player;
    private final ObjectOutputStream outputStream;

    public SenderThread(CatanPlayer player, ObjectOutputStream outputStream){
        this.player = player;
        this.outputStream = outputStream;
    }

    @Override
    public void run() {

    }
}
