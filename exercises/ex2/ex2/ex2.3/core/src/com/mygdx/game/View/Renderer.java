package com.mygdx.game.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.Model.Paddle;
import com.mygdx.game.Model.Ball;

import java.util.Arrays;

public class Renderer {
    private ShapeRenderer shapeRenderer;
    private SpriteBatch spriteBatch;
    private BitmapFont font;


    public Renderer() {
        shapeRenderer = new ShapeRenderer();
        spriteBatch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.BLUE);
        font.getData().setScale(10);
    }

    public void render(Paddle leftPaddle, Paddle rightPaddle, Ball ball, int leftPlayerScore, int rightPlayerScore, Button ...buttons) {
        // clear the screen
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // render ball and paddles
        renderBall(ball);
        renderPaddle(rightPaddle);
        renderPaddle(leftPaddle);

        // update ball position
        float deltaTime = Gdx.graphics.getDeltaTime();
        ball.update(deltaTime);

        // render buttons
        renderButtons(buttons);

        int marginTop = 50;
        int marginSides = 200;

        // render text
        renderText( "" + leftPlayerScore, marginSides, Gdx.graphics.getHeight() - marginTop);
        renderText("PongGame", Gdx.graphics.getWidth()/2-350, Gdx.graphics.getHeight() - marginTop);
        renderText("" + rightPlayerScore, Gdx.graphics.getWidth() - marginSides-100, Gdx.graphics.getHeight() - marginTop);
    }

    private void renderButton(Button button) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        if (button.getIsClicked()) {
            shapeRenderer.setColor(Color.BLUE);
        } else shapeRenderer.setColor(Color.LIGHT_GRAY);

        shapeRenderer.rect(button.getX(), button.getY(), button.getWidth(), button.getHeight());
        shapeRenderer.end();

        // Render text on the button
        spriteBatch.begin();
        button.getFont().draw(spriteBatch, button.getText(), button.getX()+ button.getWidth()/4, button.getHeight());
        spriteBatch.end();
    }

    private void renderButtons(Button ...buttons) {
        for (Button button : buttons) {
            renderButton(button);
        }
    }

    private void renderPaddle(Paddle paddle) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(paddle.getX(), paddle.getY(), paddle.getWidth(), paddle.getHeight());
        shapeRenderer.end();
    }

    private void renderBall(Ball ball) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.circle(ball.getX(), ball.getY(), ball.getRadius());
        shapeRenderer.end();
    }

    private void renderText(String text, float x, float y) {
        spriteBatch.begin();
        font.draw(spriteBatch, text, x, y);
        spriteBatch.end();
    }
}
