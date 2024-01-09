package com.game.catan.Map.Cell;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Hexagon {
    private final Polygon hexagon;

    public Hexagon(float x, float y, float size) {
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
    }



    public void draw(Stage stage) {
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.polygon(hexagon.getTransformedVertices());
        shapeRenderer.setColor(0, 0, 0, 1);
        shapeRenderer.end();
    }

    public void onClick() {
        System.out.println("Clicked");

    }
}
