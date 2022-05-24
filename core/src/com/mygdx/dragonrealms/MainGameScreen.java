package com.mygdx.dragonrealms;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.dragonrealms.map.Map;
import com.mygdx.dragonrealms.units.Unit;
import com.mygdx.dragonrealms.units.Warrior;

import java.util.Vector;

public class MainGameScreen extends ApplicationAdapter implements InputProcessor, Screen {

    Map map;
    OrthographicCamera camera;

    Vector<Unit> unitList;
    private float mapWidth;
    private float mapHeight;
    SpriteBatch sb;

    @Override
    public void show() {
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false,width,height);
        camera.update();
        map = new Map("maps/map_test/mapa_alpha.tmx");
        mapWidth = map.getWidth();
        mapHeight = map.getHeight();
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(inputMultiplexer);
        unitList = new Vector<>();
        for(int i = 0; i < 5; i += 2){
            for(int j = 0; j < 5; j += 2){
                unitList.add(new Warrior(i,j));
            }
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        camera.update();
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        map.render(camera);

        sb = new SpriteBatch();
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        for (Unit unit : unitList) {
            unit.render(sb);
        }
        sb.end();
        putInMapBounds();
    }
    
    private void putInMapBounds() {

        if (camera.position.x < camera.viewportWidth / 2f)
            camera.position.x = camera.viewportWidth / 2f;
        else if (camera.position.x > mapWidth - camera.viewportWidth / 2f)
            camera.position.x = mapHeight - camera.viewportWidth / 2f;

        if (camera.position.y < camera.viewportHeight / 2f)
            camera.position.y = camera.viewportHeight / 2f;
        else if (camera.position.y > mapHeight - camera.viewportHeight / 2f)
            camera.position.y = mapWidth - camera.viewportHeight / 2f;

        

    }
    @Override
    public boolean keyDown(int keycode) {

        if(keycode == Input.Keys.LEFT || keycode == Input.Keys.A)
            camera.translate(-64,0);
        if(keycode == Input.Keys.RIGHT || keycode == Input.Keys.D)
            camera.translate(64,0);
        if(keycode == Input.Keys.UP || keycode == Input.Keys.W)
            camera.translate(0, 64);
        if(keycode == Input.Keys.DOWN || keycode == Input.Keys.S)
            camera.translate(0,-64);
        if(keycode == Input.Keys.B){
            Gdx.app.exit();
            dispose();
        }
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
        Vector3 clickCoordinates = new Vector3(screenX, screenY, 0);
        Vector3 position = camera.unproject(clickCoordinates);
        Vector2 tile = map.convertCoordinates(position);
        System.out.println("x: " + tile.x + " y: " + tile.y);
        map.getTile(tile);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        camera.translate(-Gdx.input.getDeltaX(pointer), Gdx.input.getDeltaY(pointer));
        return true;      
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        camera.zoom += amountY/10;
        return false;
    }
    @Override
    public void hide() {

    }
}
