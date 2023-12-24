package com.game.catan.player;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.game.catan.Map.Cell.Cell;
import com.game.catan.Map.Cell.VillageCell;
import com.game.catan.Map.Map;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class CatanPlayer extends ApplicationAdapter {
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private int currentPlayerIndex;
    private SpriteBatch batch;
    private Stage stage;
    private Texture buttonTexture;
    private Map map;
    private VillageCell startVillage;
    private String villagePath;
    private String roadPath;

    public CatanPlayer(Map map) {
        this.map = map;
    }

    public CatanPlayer() {
        this.map = null;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        stage = new Stage(new ScreenViewport());

        try {
            socket = new Socket("localhost", 12345);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
            getMapFromServer();

            for(Cell cell : map.getMap()) {
                cell.buttonFunc(stage);
            }

            Gdx.input.setInputProcessor(stage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
        //renderMap();
        handleUpdates();
    }

    public void getMapFromServer() {
        try {
            // Assuming outputStream and inputStream are instances of ObjectOutputStream and ObjectInputStream
            Object temp = inputStream.readObject();
            if(temp instanceof Map) {
                map = (Map) temp;
            }
            else {
                System.out.println("Error: Map not received");
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            // Close resources properly
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void endTurnButton () {
        //create a button that when pressed does something
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = new com.badlogic.gdx.graphics.g2d.BitmapFont();
        textButtonStyle.font.getData().setScale(2f);
        textButtonStyle.fontColor = com.badlogic.gdx.graphics.Color.BLACK;
        buttonTexture = new Texture("sheep.png");
        textButtonStyle.up = new TextureRegionDrawable(buttonTexture);
        textButtonStyle.down = new TextureRegionDrawable(buttonTexture);

        TextButton button = new TextButton("End Turn", textButtonStyle);
        button.setPosition(600, 600);
        button.setSize(200, 100);
        SpriteBatch batch = new SpriteBatch();
        batch.begin();
        button.draw(batch, 1);
        button.addListener(new com.badlogic.gdx.scenes.scene2d.InputListener() {
            public boolean touchDown(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y, int pointer, int button) {
                try {
                    System.out.println("End turn");
                    endTurn();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
        });

    }

    private void endTurn() throws IOException {
        // Send a message to the server indicating the end of the turn
        outputStream.writeObject(new Object()); // Send any object to the server
        Gdx.gl.glClearColor(200, 1, 1, 1);
        outputStream.reset(); // Ensure the object is resent
    }

    private void handleUpdates() {

    }

    private void renderMap() {
        for(Cell cell : map.getMap()) {
            Texture tempTexture = new Texture(cell.getTexturePath());
            batch.begin();
            batch.draw(tempTexture, cell.getCellCords().getX(), cell.getCellCords().getY());
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
