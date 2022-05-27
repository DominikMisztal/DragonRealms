package com.mygdx.dragonrealms.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.dragonrealms.MyGame;

public class EndGameScreen implements Screen {
    private MyGame game;

    public EndGameScreen(MyGame game){
        this.game = game;
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.25f, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        game.font.draw(game.batch, "You win!", Gdx.graphics.getWidth()*.25f, Gdx.graphics.getHeight() * .75f);
        game.font.draw(game.batch, "Press enter to leave.", Gdx.graphics.getWidth()*.25f, Gdx.graphics.getHeight() * .25f);
        game.batch.end();
        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
            Gdx.app.exit();
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
}
