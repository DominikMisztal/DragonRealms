package com.mygdx.dragonrealms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;


public class SplashScreen implements Screen {
    private MyGame game;
    private Stage stage;
    private Image image;

    public SplashScreen(final MyGame game){
        this.game = game;
        this.stage = new Stage(new FitViewport(MyGame.WIDTH, MyGame.HEIGHT, game.camera));
        Gdx.input.setInputProcessor(stage);

        Texture texture = new Texture(Gdx.files.internal("archery.png"));
        image = new Image(texture);
        image.setOrigin(image.getWidth() / 2, image.getHeight() / 2);

        stage.addActor(image);
    }

    @Override
    public void show() {
        image.setPosition(stage.getWidth() / 2 - 256, stage.getHeight() - 256);

        image.addAction(sequence(alpha(0), scaleTo(.1f, .1f),
                parallel(fadeIn(2f, Interpolation.pow2),
                        scaleTo(1f,1f,2.5f, Interpolation.pow5),
                        moveTo(stage.getWidth() / 2 - 256, stage.getHeight() / 2 - 256, 2f, Interpolation.swing)),
                delay(1.5f), fadeOut(1.25f)));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.25f,.25f,.25f,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        stage.draw();
    }

    public void update(float delta){
        stage.act(delta);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, false);
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
    }
}
