package com.game.catan.server;

import com.badlogic.gdx.net.Socket;
import com.game.catan.player.catanPlayer;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

//socket server
class catanServer {
    private static class catanServerHolder {
        private static final catanServer INSTANCE = new catanServer();

        private Socket clientSocket;

        private catanServerHolder() {
            try {
                clientSocket = new Socket() {
                    @Override
                    public void dispose() {

                    }

                    @Override
                    public boolean isConnected() {
                        return false;
                    }

                    @Override
                    public InputStream getInputStream() {
                        return null;
                    }

                    @Override
                    public OutputStream getOutputStream() {
                        return null;
                    }

                    @Override
                    public String getRemoteAddress() {
                        return null;
                    }
                };
            } catch (Exception e) {
                System.out.println("Error: " + e);
            }
        }
    }
    private static final int PORT = 1234;
    List<catanPlayer> players = new ArrayList<catanPlayer>();

    catanPlayer currentPlayer;
    public static void main(String[] args) {


    }

    public void addPlayer(catanPlayer player) {
        players.add(player);
    }

    public void removePlayer(catanPlayer player) {
        players.remove(player);
    }
}