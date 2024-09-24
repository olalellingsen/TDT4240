package com.mygdx.game.View;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Button {
    private String text;
    private BitmapFont font;
    private float x, y, width, height;
    private Boolean isClicked;

    public Button(String text, float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        this.font = new BitmapFont();
        this.font.setColor(Color.RED);
        this.font.getData().setScale(5);
        isClicked = false;
    }

    public BitmapFont getFont() {
        return font;
    }

    public void setIsClicked(Boolean clicked) {
        isClicked = clicked;
    }

    public Boolean getIsClicked() {
        return isClicked;
    }

    public String getText() {
        return text;
    }

    public float getY() {
        return y;
    }
    public float getX() {
        return x;
    }
    public float getWidth() {
        return width;
    }
    public float getHeight() {
        return height;
    }

    public boolean contains(float testX, float testY) {
        return testX >= x && testX <= x + width && testY >= y && testY <= y + height;
    }
}
