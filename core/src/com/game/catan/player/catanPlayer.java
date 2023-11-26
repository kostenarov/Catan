package com.game.catan.player;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class catanPlayer extends ApplicationAdapter {
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private int currentPlayerIndex;
    private SpriteBatch batch;
    private Texture buttonTexture;

    @Override
    public void create() {
        batch = new SpriteBatch();
        buttonTexture = new Texture("button.png"); // Replace with your button texture

        try {
            socket = new Socket("localhost", 12345);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());

            // Do not use a separate thread for listening in libGDX
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    @Override
    public void dispose() {
        batch.dispose();
        buttonTexture.dispose();
    }
}
