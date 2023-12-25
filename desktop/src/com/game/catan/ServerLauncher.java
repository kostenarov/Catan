package com.game.catan;

import com.game.catan.Map.Map;
import com.game.catan.server.CatanServer;

public class ServerLauncher {
    public static void main(String[] args) {
        CatanServer server = new CatanServer(new Map());
    }
}
