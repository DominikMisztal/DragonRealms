package com.mygdx.dragonrealms;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import org.w3c.dom.Text;

public class MainMenuScreen implements Screen, InputProcessor {

    final MyGame game;
    private Skin skin;
    String text;

    OrthographicCamera camera;
    private Texture texture;

    Texture playButtonActive;
    Texture playButtonInactive;
    Texture exitButtonActive;
    Texture exitButtonInactive;

    public MainMenuScreen(final MyGame game) {
        this.game = game;
        this.skin = new Skin();
//        this.skin.addRegions();

//        game.font = new BitmapFont(Gdx.files.internal("textures/font1.fnt"));
        text = "Welcome to Dragon Realms!\n" + "Tap anywhere to begin!";
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
//        texture = new Texture(Gdx.files.internal("mainMenu.jpg"));
//        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();



//        game.batch.draw(texture,0,0, 800,500);
        game.font.draw(game.batch, text, 200, 250);
        game.batch.end();

        if (Gdx.input.isTouched()) {
            game.screenManager.setScreen(ScreenManager.STATE.PLAY);
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }


    //...Rest of class omitted for succinctness.

}