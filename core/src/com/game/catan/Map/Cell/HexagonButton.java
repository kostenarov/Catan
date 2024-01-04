package com.game.catan.Map.Cell;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class HexagonButton extends Actor {
    private final Polygon hexagon;
    private String texturePath;

    public HexagonButton(float x, float y, float size, String texturePath) {
        this.texturePath = texturePath;
        float[] vertices = new float[12];
        vertices[0] = x;
        vertices[1] = y + size;
        vertices[2] = x + size;
        vertices[3] = y + size / 2;
        vertices[4] = x + size;
        vertices[5] = y - size / 2;
        vertices[6] = x;
        vertices[7] = y - size;
        vertices[8] = x - size;
        vertices[9] = y - size / 2;
        vertices[10] = x - size;
        vertices[11] = y + size / 2;
        hexagon = new Polygon(vertices);
        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                onClick();
                return true;
            }
        });
    }



    public void draw(Stage stage) {
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.polygon(hexagon.getTransformedVertices());
        shapeRenderer.setColor(0, 0, 0, 1);
        shapeRenderer.end();
        this.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                onClick();
                return true;
            }
        });
        stage.addActor(this);
    }

    public void onClick() {
        System.out.println("Clicked");

    }
}
