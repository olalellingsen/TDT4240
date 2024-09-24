package com.mygdx.game.Model;

// singleton class
public class PongManager {
    public enum GameLevel {
        EASY,
        MEDIUM,
        HARD
    }
    private float ballSpeed;
    private int paddleHeight;
    private GameLevel currentLevel;

    public static PongManager getInstance() {
        return InstanceHolder.INSTANCE;
    }
    private static class InstanceHolder {
        private static final PongManager INSTANCE = new PongManager();
    }

    private void adjustGameParameters() {
        // switching between different levels
        switch (currentLevel) {
            case EASY:
                ballSpeed = 600;
                paddleHeight = 400;
                break;
            case MEDIUM:
                ballSpeed = 800;
                paddleHeight = 200;
                break;
            case HARD:
                ballSpeed = 1000;
                paddleHeight = 100;
                break;
            default:
                break;
        }
    }

    public float getBallSpeed() {
        return ballSpeed;
    }

    public int getPaddleHeight() {
        return paddleHeight;
    }

    public void setCurrentLevel(GameLevel level) {
        currentLevel = level;
        adjustGameParameters(); // Adjust game parameters based on the selected level
    }

    public GameLevel getCurrentLevel() {
        return currentLevel;
    }
}

