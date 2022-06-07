package com.mygdx.dragonrealms.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.mygdx.dragonrealms.MyGame;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class MainMenuScreen implements Screen, InputProcessor {

    final MyGame game;
    private Skin skin;
    private Stage stage;
    private ShapeRenderer shapeRenderer;
    Texture texture;

    TextButton playButton;
    TextButton settingsButton;
    TextButton exitButton;

    public MainMenuScreen(final MyGame game) {
        this.game = game;
        this.stage = new Stage(new FillViewport(MyGame.WIDTH, MyGame.HEIGHT, game.camera), game.batch);
        this.shapeRenderer = new ShapeRenderer();
        this.texture = new Texture(Gdx.files.internal("mainMenu.jpg"));
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        stage.clear();

        this.skin = new Skin();
        this.skin.addRegions(game.assets.get("ui/uiskin.atlas", TextureAtlas.class));
        this.skin.add("default-font", game.font);
        this.skin.load(Gdx.files.internal("ui/uiskin.json"));
        
        initButtons();
    }

    private void update(float delta){
        stage.act(delta);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f,1f,1f,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        stage.getBatch().draw(texture,0,0, MyGame.WIDTH, MyGame.HEIGHT);
        game.batch.end();

        update(delta);
        stage.draw();
    }


    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        stage.dispose();
        shapeRenderer.dispose();
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

    private void initButtons(){
        int BUTTON_WIDTH = 300;
        int BUTTON_HEIGHT = 100;
        float FIRST_BUTTON_X = MyGame.WIDTH / 2f - 150f;
        float FIRST_BUTTON_Y = MyGame.HEIGHT/ 2f + 60f;
        playButton = new TextButton("Play", skin, "default");
        playButton.setSize(BUTTON_WIDTH,BUTTON_HEIGHT);
        playButton.setPosition(FIRST_BUTTON_X,FIRST_BUTTON_Y);
        playButton.addAction(parallel(fadeIn(.5f),
                moveBy(0,-20,.5f, Interpolation.pow5Out)));
        playButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.soundController.playClick();
                game.screenManager.setScreen(ScreenManager.STATE.PLAY);
            }
        });

        settingsButton = new TextButton("Settings", skin, "default");
        settingsButton.setSize(BUTTON_WIDTH,BUTTON_HEIGHT);
        settingsButton.setPosition(FIRST_BUTTON_X,FIRST_BUTTON_Y - 120);
        settingsButton.addAction(parallel(fadeIn(.5f),
                moveBy(0,-20,.5f, Interpolation.pow5Out)));
        settingsButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.soundController.playClick();
                game.screenManager.setScreen(ScreenManager.STATE.SETTINGS);
            }
        });

        exitButton = new TextButton("Exit", skin, "default");
        exitButton.setSize(BUTTON_WIDTH,BUTTON_HEIGHT);
        exitButton.setPosition(FIRST_BUTTON_X,FIRST_BUTTON_Y - 240);
        exitButton.addAction(parallel(fadeIn(.5f),
                moveBy(0,-20,.5f, Interpolation.pow5Out)));
        exitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.soundController.playClick();
                Gdx.app.exit();
            }
        });

        stage.addActor(playButton);
        stage.addActor(settingsButton);
        stage.addActor(exitButton);
    }

}