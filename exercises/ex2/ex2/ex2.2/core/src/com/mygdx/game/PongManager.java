package com.mygdx.game;

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
    // Inner class to hold the singleton instance
    private static class InstanceHolder {
        private static final PongManager INSTANCE = new PongManager();
    }

    private void adjustGameParameters() {
        switch (currentLevel) {
            case EASY:
                ballSpeed = 400;
                paddleHeight = 600;
                break;
            case MEDIUM:
                ballSpeed = 600;
                paddleHeight = 300;
                break;
            case HARD:
                ballSpeed = 900;
                paddleHeight = 150;
                break;
            default:
                break;
        }
    }

    // Getters
    public float getBallSpeed() {
        return ballSpeed;
    }
    public int getPaddleHeight() {
        return paddleHeight;
    }

    // Setters
    public void setCurrentLevel(GameLevel level) {
        currentLevel = level;
        adjustGameParameters(); // Adjust game parameters based on the selected level
    }
    public GameLevel getCurrentLevel() {
        return currentLevel;
    }

    // Increase/decrease level
    public void increaseGameLevel() {

    }
    public void decreaseGameLevel() {

    }
}

