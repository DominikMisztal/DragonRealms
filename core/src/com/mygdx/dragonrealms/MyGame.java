package com.mygdx.dragonrealms;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class MyGame extends Game {

    public SpriteBatch batch;
    public BitmapFont font;
    public ScreenManager screenManager;

    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont(); // use libGDX's default Arial font
        screenManager = new ScreenManager(this);
    }

    public void render() {
        super.render(); // important!
    }

    public void dispose() {
        super.dispose();
        batch.dispose();
        font.dispose();
        screenManager.dispose();
    }

}