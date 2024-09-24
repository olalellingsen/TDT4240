package com.mygdx.game.Controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.Model.Ball;
import com.mygdx.game.Model.Paddle;
import com.mygdx.game.Model.PongManager;
import com.mygdx.game.MyPongGame;

public class GameController {
    private Paddle leftPaddle;
    private Paddle rightPaddle;
    private Ball ball;
    private PongManager pongManager;
    private MyPongGame game;


    public GameController(MyPongGame game, Paddle leftPaddle, Paddle rightPaddle, Ball ball, PongManager pongManager) {
        this.leftPaddle = leftPaddle;
        this.rightPaddle = rightPaddle;
        this.ball = ball;
        this.pongManager = pongManager;
        this.game = game;
    }

    private void updateScore() {
        if (ball.getX() - ball.getRadius() < 0) {
            // Right player scores a point
            resetGame();
            game.increaseRightScore();
        }
        if (ball.getX() + ball.getRadius() > Gdx.graphics.getWidth()) {
            // Left player scores a point
            resetGame();
            game.increaseLeftScore();
        }
    }


    private void resetGame() {
        updateLevel();

        // Reset the ball position to the center
        ball.setX(Gdx.graphics.getWidth() / 2);
        ball.setY(Gdx.graphics.getHeight() / 2);

        // Reset paddles to initial positions
        leftPaddle.setY(Gdx.graphics.getHeight() / 2 - leftPaddle.getHeight() / 2);
        rightPaddle.setY(Gdx.graphics.getHeight() / 2 - rightPaddle.getHeight() / 2);
    }

    private void updateLevel(){
        // update speed and paddles according to the level provided by the pongManager
        game.getBall().setSpeed(pongManager.getBallSpeed());
        game.getLeftPaddle().setHeight(pongManager.getPaddleHeight());
        game.getRightPaddle().setHeight(pongManager.getPaddleHeight());
    }

    private void updateButtons(float screenX, float screenY) {
        // Check if the click position intersects with any button and update clicked state
        if (game.getEasyButton().contains(screenX, screenY)) {
            pongManager.setCurrentLevel(PongManager.GameLevel.EASY);
            resetGame();
            game.getEasyButton().setIsClicked(true);
            game.getMediumButton().setIsClicked(false);
            game.getHardButton().setIsClicked(false);
        }

        else if (game.getMediumButton().contains(screenX, screenY)) {
            pongManager.setCurrentLevel(PongManager.GameLevel.MEDIUM);
            resetGame();
            game.getEasyButton().setIsClicked(false);
            game.getMediumButton().setIsClicked(true);
            game.getHardButton().setIsClicked(false);
        }

        else if (game.getHardButton().contains(screenX, screenY)) {
            pongManager.setCurrentLevel(PongManager.GameLevel.HARD);
            resetGame();
            game.getEasyButton().setIsClicked(false);
            game.getMediumButton().setIsClicked(false);
            game.getHardButton().setIsClicked(true);
        }
    }


    public void handleInput() {
        if (Gdx.input.isTouched()) {
            float screenX = Gdx.input.getX();
            float screenY = Gdx.graphics.getHeight() - Gdx.input.getY();
            float oldLeftHeight = leftPaddle.getY();
            float oldRightHeight = rightPaddle.getY();
            float newY = MathUtils.clamp(screenY - leftPaddle.getHeight() / 2, 0, Gdx.graphics.getHeight() - leftPaddle.getHeight());

            if (screenX < Gdx.graphics.getWidth() / 2) {
                leftPaddle.setY(newY);
                rightPaddle.setY(oldRightHeight);
            }
            if (screenX > Gdx.graphics.getWidth() / 2) {
                rightPaddle.setY(newY);
                leftPaddle.setY(oldLeftHeight);
            }

            updateButtons(screenX, screenY);
        }
        // updates scores
        updateScore();
    }


}