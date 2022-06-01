package com.mygdx.dragonrealms.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.mygdx.dragonrealms.MyGame;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class HelpScreen implements Screen, InputProcessor {

    final MyGame game;
    private Skin skin;
    private Stage stage;
    private ShapeRenderer shapeRenderer;
    private Texture texture;
    private Texture texture1;
    private Texture currentTexture;
    private String nextPageText = "Next";

    public HelpScreen(final MyGame game) {
        this.game = game;
        this.texture = new Texture(Gdx.files.internal("menuHelp.png"));
        this.texture1 = new Texture(Gdx.files.internal("menuHelp1.png"));
        currentTexture = texture;
        this.stage = new Stage(new FillViewport(MyGame.WIDTH, MyGame.HEIGHT, game.camera));
        this.shapeRenderer = new ShapeRenderer();
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
        game.batch.draw(currentTexture,0,0, MyGame.WIDTH, MyGame.HEIGHT);
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
        float BUTTON_X = MyGame.WIDTH - 310f;
        float BUTTON_Y = MyGame.HEIGHT - 110f;

        TextButton exitButton = new TextButton("Exit", skin, "default");
        exitButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        exitButton.setPosition(BUTTON_X, BUTTON_Y);
        exitButton.addAction(sequence(alpha(0), parallel(fadeIn(.5f),
                moveBy(0,-20,.5f, Interpolation.pow5Out))));
        exitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                Gdx.app.exit();
            }
        });

        final TextButton nextPage = new TextButton("Next", skin, "default");
        nextPage.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        nextPage.setPosition(BUTTON_X, BUTTON_Y - 110);
        nextPage.addAction(sequence(alpha(0), parallel(fadeIn(.5f),
                moveBy(0,-20,.5f, Interpolation.pow5Out))));
        nextPage.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                if(currentTexture == texture){
                    currentTexture = texture1;
                    nextPage.setText("Back");
                }
                else{
                    currentTexture = texture;
                    nextPage.setText("Next");
                }

            }
        });

        TextButton mainMenu = new TextButton("Main Menu", skin, "default");
        mainMenu.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        mainMenu.setPosition(BUTTON_X, BUTTON_Y - 220);
        mainMenu.addAction(sequence(alpha(0), parallel(fadeIn(.5f),
                moveBy(0,-20,.5f, Interpolation.pow5Out))));
        mainMenu.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.screenManager.setScreen(ScreenManager.STATE.MAIN_MENU);
            }
        });

        stage.addActor(exitButton);
        stage.addActor(nextPage);
        stage.addActor(mainMenu);
    }


    //...Rest of class omitted for succinctness.

}