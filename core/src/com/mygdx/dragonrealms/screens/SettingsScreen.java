package com.mygdx.dragonrealms.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.mygdx.dragonrealms.MyGame;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class SettingsScreen implements Screen, InputProcessor {

    final MyGame game;
    private Skin skin;
    private Stage stage;
    private ShapeRenderer shapeRenderer;

    TextButton playButton;
    TextButton helpButton;
    TextButton musicButton;
    TextButton speedButton;
    TextButton backButton;
    Slider slider;
    private Texture texture;
    float FIRST_BUTTON_X = MyGame.WIDTH / 2f - 150f;
    float FIRST_BUTTON_Y = MyGame.HEIGHT/ 2f + 60f;

    public SettingsScreen(final MyGame game) {
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
        initSlider();
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
    private void initSlider(){
        slider = new Slider(0, 100, 1, false, skin);
        slider.setSize(280, 10);
        slider.setPosition(FIRST_BUTTON_X + 10,FIRST_BUTTON_Y - 215);

        stage.addActor(slider);
        slider.setVisible(false);
    }

    private void initButtons(){
        int BUTTON_WIDTH = 300;
        int BUTTON_HEIGHT = 100;

        helpButton = new TextButton("Help", skin, "default");
        helpButton.setSize(BUTTON_WIDTH,BUTTON_HEIGHT);
        helpButton.setPosition(FIRST_BUTTON_X,FIRST_BUTTON_Y);
        helpButton.addAction(parallel(fadeIn(.5f),
                moveBy(0,-20,.5f, Interpolation.pow5Out)));
        helpButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.soundController.playClick();
                game.screenManager.setScreen(ScreenManager.STATE.HELP);
            }
        });

        musicButton = new TextButton("Music ON", skin, "default");
        musicButton.setSize(BUTTON_WIDTH,BUTTON_HEIGHT);
        musicButton.setPosition(FIRST_BUTTON_X,FIRST_BUTTON_Y - 120);
        musicButton.addAction(parallel(fadeIn(.5f),
                moveBy(0,-20,.5f, Interpolation.pow5Out)));
        musicButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.soundController.playClick();
                if(game.isSoundActive){
                    game.isSoundActive = false;
                    musicButton.setText("Music OFF");
                }
                else{
                    game.isSoundActive = true;
                    musicButton.setText("Music ON");
                }
            }
        });

        speedButton = new TextButton("Adjust camera speed", skin, "default");
        speedButton.setSize(BUTTON_WIDTH,BUTTON_HEIGHT);
        speedButton.setPosition(FIRST_BUTTON_X,FIRST_BUTTON_Y - 240);
        speedButton.addAction(parallel(fadeIn(.5f),
                moveBy(0,-20,.5f, Interpolation.pow5Out)));
        speedButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.soundController.playClick();
                if(speedButton.getText().length() > 0){
                    speedButton.setText("");
                    slider.setVisible(true);
                }
                else{
                    speedButton.setText("Adjust camera speed");
                    slider.setVisible(false);
                }
            }
        });

        backButton = new TextButton("Back", skin, "default");
        backButton.setSize(BUTTON_WIDTH,BUTTON_HEIGHT);
        backButton.setPosition(FIRST_BUTTON_X,FIRST_BUTTON_Y - 360);
        backButton.addAction(parallel(fadeIn(.5f),
                moveBy(0,-20,.5f, Interpolation.pow5Out)));
        backButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.soundController.playClick();
                game.screenManager.setScreen(ScreenManager.STATE.MAIN_MENU);
            }
        });

        stage.addActor(helpButton);
        stage.addActor(musicButton);
        stage.addActor(speedButton);
        stage.addActor(backButton);
    }

}