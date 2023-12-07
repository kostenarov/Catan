package com.game.catan.player;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.game.catan.Map.Cell.Cell;
import com.game.catan.Map.Cell.VillageCell;
import com.game.catan.Map.Map;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashSet;

public class CatanPlayer extends ApplicationAdapter {
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private int currentPlayerIndex;
    private SpriteBatch batch;
    private Texture buttonTexture;
    private Map map;
    private VillageCell startVillage;
    private String villagePath;
    private String roadPath;

    public CatanPlayer(Map map) {
        this.map = map;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        buttonTexture = new Texture("button.png"); // Replace with your button texture

        try {
            socket = new Socket("172.23.32.1", 12345);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());

            // Do not use a separate thread for listening in libGDX
        } catch (IOException e) {
            e.printStackTrace();
        }
        drawField();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(buttonTexture, Gdx.graphics.getWidth() / 2 - buttonTexture.getWidth() / 2,
                Gdx.graphics.getHeight() / 2 - buttonTexture.getHeight() / 2);
        batch.end();

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            // Simulate pressing the "End Turn" button
            try {
                endTurn();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        renderMap();
        // Handle updates on the main thread
        handleUpdates();
    }

    private void endTurn() throws IOException {
        // Send a message to the server indicating the end of the turn
        outputStream.writeObject(new Object()); // Send any object to the server
        Gdx.gl.glClearColor(200, 1, 1, 1);
        outputStream.reset(); // Ensure the object is resent
    }

    private void handleUpdates() {
        try {
            // Check for updates from the server
            if (inputStream.available() > 0) {
                // Receive updates from the server
                currentPlayerIndex = (int) inputStream.readObject();

                // Handle the update on the main thread
                System.out.println("Current player index: " + currentPlayerIndex);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void drawField() {
        for(Cell cell : map.getMap()) {
            Texture tempTexture = new Texture(cell.getTexturePath());
        }
    }

    private void renderMap() {
        for(Cell cell : map.getMap()) {
            Texture tempTexture = new Texture(cell.getTexturePath());
            batch.begin();
            batch.draw(tempTexture, cell.getX(), cell.getY());
            batch.end();
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        buttonTexture.dispose();
    }

    public String getVillagePath() {
        return villagePath;
    }

    public String getRoadPath() {
        return roadPath;
    }
    public void setMap(Map map) {
        this.map = map;
    }
}
