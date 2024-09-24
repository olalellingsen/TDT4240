package com.mygdx.game.Model;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

public class Paddle {
    private float x, y;
    private float width, height;
    private float speed;

    public Paddle(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void updatePositionByTouch(float deltaY) {
        // Update the paddle position based on touch input
        y += deltaY;

        // Ensure the paddle stays within the screen bounds
        y = MathUtils.clamp(y, 0, Gdx.graphics.getHeight() - height);
    }

    public void update(float deltaTime) {
        y += speed * Gdx.input.getDeltaY() * deltaTime;
    }

    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(x, y, width, height);
    }
    public float getWidth() {
        return width;
    }
    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }
}
