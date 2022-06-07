package com.mygdx.dragonrealms;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.mygdx.dragonrealms.screens.ScreenManager;

import java.util.Vector;


public class MyGame extends Game {
    public static final int WIDTH = 1800;
    public static final int HEIGHT = 900;

    public SpriteBatch batch;
    public BitmapFont font;
    public BitmapFont font1;
    public BitmapFont endgameFont;
    public BitmapFont endGameWinnerFont;
    public BitmapFont helpFont1;
    public BitmapFont helpFont2;
    public BitmapFont tileFont;
    public ScreenManager screenManager;
    public OrthographicCamera camera;
    public AssetManager assets;
    public SoundController soundController;
    public boolean isSoundActive;
    public Vector<Player> players;
    public float cameraSpeed;

    public void create() {
        assets = new AssetManager();
        batch = new SpriteBatch();
        initFonts();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WIDTH, HEIGHT);
        screenManager = new ScreenManager(this);
        isSoundActive = true;
        soundController = new SoundController();
        cameraSpeed = 400f;
    }

    public void render() {
        super.render(); // important!
    }

    public void dispose() {
        super.dispose();
        batch.dispose();
        font.dispose();
        assets.dispose();
        screenManager.dispose();
    }

    private void initFonts(){
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Arcon.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();

        params.size = 24;
        params.color = Color.WHITE;
        font = generator.generateFont(params);

        FreeTypeFontGenerator.FreeTypeFontParameter params1 = new FreeTypeFontGenerator.FreeTypeFontParameter();

        params1.size = 30;
        params1.color = Color.WHITE;
        endgameFont = generator.generateFont(params1);

        FreeTypeFontGenerator.FreeTypeFontParameter params2 = new FreeTypeFontGenerator.FreeTypeFontParameter();

        params2.size = 35;
        params2.color = Color.WHITE;
        endGameWinnerFont = generator.generateFont(params2);
        helpFont1 = generator.generateFont(params2);

        FreeTypeFontGenerator.FreeTypeFontParameter params3 = new FreeTypeFontGenerator.FreeTypeFontParameter();

        params3.size = 30;
        params3.color = Color.WHITE;
        params3.spaceY = 16;
        helpFont2 = generator.generateFont(params3);

        FreeTypeFontGenerator.FreeTypeFontParameter params4 = new FreeTypeFontGenerator.FreeTypeFontParameter();

        params4.size = 20;
        params4.color = Color.WHITE;
        font1 = generator.generateFont(params4);

        FreeTypeFontGenerator.FreeTypeFontParameter params5 = new FreeTypeFontGenerator.FreeTypeFontParameter();

        params5.size = 30;
        params5.color = Color.WHITE;
        params5.spaceY = 10;
        tileFont = generator.generateFont(params5);
    }
}