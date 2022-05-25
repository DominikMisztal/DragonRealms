package com.mygdx.dragonrealms;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.dragonrealms.map.Map;
import com.mygdx.dragonrealms.map.Tile;
import com.mygdx.dragonrealms.map.TileType;
import com.mygdx.dragonrealms.units.Archer;
import com.mygdx.dragonrealms.units.Assassin;
import com.mygdx.dragonrealms.units.Unit;
import com.mygdx.dragonrealms.units.Warrior;

import java.util.Vector;

public class MainGameScreen extends ApplicationAdapter implements InputProcessor, Screen {

    private Map map;
    private OrthographicCamera camera;

    private Vector<Unit> unitList;
    private float mapWidth;
    private float mapHeight;
    private SpriteBatch sb;
    private Vector2 lastClickTile;

    @Override
    public void create(){
        
    }

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

        if (camera.position.x < camera.viewportWidth*camera.zoom / 2f)
            camera.position.x = camera.viewportWidth*camera.zoom / 2f;
        else if (camera.position.x > mapWidth - camera.viewportWidth*camera.zoom / 2f)
            camera.position.x = mapHeight - camera.viewportWidth*camera.zoom / 2f;

        if (camera.position.y < camera.viewportHeight*camera.zoom / 2f)
            camera.position.y = camera.viewportHeight*camera.zoom / 2f;
        else if (camera.position.y > mapHeight - camera.viewportHeight*camera.zoom / 2f)
            camera.position.y = mapWidth - camera.viewportHeight*camera.zoom / 2f;

    }
    private boolean isInMapBounds() {

        return camera.position.x >= camera.viewportWidth*camera.zoom / 2f
                && camera.position.x <= mapWidth - camera.viewportWidth*camera.zoom / 2f
                && camera.position.y >= camera.viewportHeight*camera.zoom / 2f
                && camera.position.y <= mapHeight - camera.viewportHeight*camera.zoom / 2f;

    }
    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.LEFT || keycode == Input.Keys.A && isInMapBounds())
            camera.translate(-64,0);
        if(keycode == Input.Keys.RIGHT || keycode == Input.Keys.D && isInMapBounds())
            camera.translate(64,0);
        if(keycode == Input.Keys.UP || keycode == Input.Keys.W && isInMapBounds())
            camera.translate(0, 64);
        if(keycode == Input.Keys.DOWN || keycode == Input.Keys.S && isInMapBounds())
            camera.translate(0,-64);
        if(keycode == Input.Keys.B){
            Gdx.app.exit();
            dispose();
        }

        if(keycode == Input.Keys.I){
            spawnUnit(1);
            System.out.println("Spawning warrior at X: " + (int)lastClickTile.x + " Y: " + (int)lastClickTile.y);
        }
        if(keycode == Input.Keys.O){
            spawnUnit(2);
            System.out.println("Spawning archer at X: " + (int)lastClickTile.x + " Y: " + (int)lastClickTile.y);
        }
        if(keycode == Input.Keys.P){
            spawnUnit(3);
            System.out.println("Spawning assassin at X: " + (int)lastClickTile.x + " Y: " + (int)lastClickTile.y);
        }
        putInMapBounds();
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
        lastClickTile = map.convertCoordinates(position);
        // System.out.println("x: " + tile.x + " y: " + tile.y);
        // map.getTile(tile);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if(isInMapBounds()){
            camera.translate(-Gdx.input.getDeltaX(pointer), Gdx.input.getDeltaY(pointer));
        }
        putInMapBounds();
        return true;      
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        camera.zoom += amountY/100;
        if(camera.zoom > 1.1375){
            camera.zoom = 1.1375f;
        }
        if(camera.zoom < 0.6){
            camera.zoom = 0.6f;
        }
        System.out.println("zoom: " + camera.zoom);
        return false;
    }
    @Override
    public void hide() {

    }

    private boolean spawnUnit(int type){
        Tile tile;
        Unit unit;
        tile = map.getTile(new Vector2(lastClickTile.x,lastClickTile.y));
        if(tile == null || tile.getUnit() != null || tile.getType() == TileType.MOUNTAIN || tile.getType() == TileType.WATER){
            return false;
        }

        if(type == 1){
            unit = new Warrior((int)lastClickTile.x,(int)lastClickTile.y);
        }
        else if(type == 2){
            unit = new Archer((int)lastClickTile.x,(int)lastClickTile.y);
        }
        else if(type == 3){
            unit = new Assassin((int)lastClickTile.x,(int)lastClickTile.y);
        }
        else{
            return false;
        }

        tile.setUnit(unit);
        unitList.add(unit);
        return true;
    }
}
