package com.mygdx.dragonrealms;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
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
    public BitmapFont endgameFont;
    public BitmapFont endGameWinnerFont;
    public ScreenManager screenManager;
    public OrthographicCamera camera;
    public AssetManager assets;
    public Sound sound;
    public boolean isSoundActive;
    public Vector<Player> players;

    public void create() {
        assets = new AssetManager();
        batch = new SpriteBatch();
        initFonts();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WIDTH, HEIGHT);
        screenManager = new ScreenManager(this);
        sound = new Sound() {
            @Override
            public long play() {
                return 0;
            }

            @Override
            public long play(float volume) {
                return 0;
            }

            @Override
            public long play(float volume, float pitch, float pan) {
                return 0;
            }

            @Override
            public long loop() {
                return 0;
            }

            @Override
            public long loop(float volume) {
                return 0;
            }

            @Override
            public long loop(float volume, float pitch, float pan) {
                return 0;
            }

            @Override
            public void stop() {

            }

            @Override
            public void pause() {

            }

            @Override
            public void resume() {

            }

            @Override
            public void dispose() {

            }

            @Override
            public void stop(long soundId) {

            }

            @Override
            public void pause(long soundId) {

            }

            @Override
            public void resume(long soundId) {

            }

            @Override
            public void setLooping(long soundId, boolean looping) {

            }

            @Override
            public void setPitch(long soundId, float pitch) {

            }

            @Override
            public void setVolume(long soundId, float volume) {

            }

            @Override
            public void setPan(long soundId, float pan, float volume) {

            }
        };
        isSoundActive = true;
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
        sound.dispose();
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
    }
}