package com.mygdx.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

// Ball class
public class Ball {
    private float x, y;
    private float radius;
    private float speedX, speedY;
    private Paddle leftPaddle, rightPaddle;

    public Ball(float x, float y, float radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public void setSpeed(float speed) {
        this.speedX = speed;
        this.speedY = speed;
    }

    public void setLeftPaddle(Paddle leftPaddle) {
        this.leftPaddle = leftPaddle;
    }

    public void setRightPaddle(Paddle rightPaddle) {
        this.rightPaddle = rightPaddle;
    }

    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.circle(x, y, radius);
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void update(float deltaTime) {
        // Update ball position based on speed and time
        x += speedX * deltaTime;
        y += speedY * deltaTime;

        // Check for collisions with paddles
        if (leftPaddle != null && overlaps(leftPaddle)) {
            // Reverse the horizontal direction
            speedX = Math.abs(speedX);
        }

        if (rightPaddle != null && overlaps(rightPaddle)) {
            // Reverse the horizontal direction
            speedX = -Math.abs(speedX);
        }

        // Check for collisions with top and bottom edges
        if (y - radius < 0 || y + radius > Gdx.graphics.getHeight()) {
            // Reverse the vertical direction
            speedY = -speedY;
        }
    }

    private boolean overlaps(Paddle paddle) {
        // Check if the ball overlaps with the given paddle
        return x - radius < paddle.getX() + paddle.getWidth() &&
                x + radius > paddle.getX() &&
                y - radius < paddle.getY() + paddle.getHeight() &&
                y + radius > paddle.getY();
    }

    public float getRadius() {
        return radius;
    }
}
