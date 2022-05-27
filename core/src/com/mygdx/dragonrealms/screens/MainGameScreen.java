package com.mygdx.dragonrealms.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.dragonrealms.MyGame;
import com.mygdx.dragonrealms.map.Map;
import com.mygdx.dragonrealms.map.Tile;
import com.mygdx.dragonrealms.map.TileType;
import com.mygdx.dragonrealms.units.*;

import java.util.Vector;

public class MainGameScreen extends ApplicationAdapter implements InputProcessor, Screen {

    private Map map;
    private MyGame game;
    private Skin skin;
    private Stage stage;
    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;

    private Vector<Unit> unitList;
    private Vector<Tile> tilesToDraw;
    private float mapWidth;
    private float mapHeight;
    private Vector2 lastClickTile;
    private Unit currentlySelectedUnit;

    public MainGameScreen(MyGame game){
        this.game = game;
        this.stage = new Stage(new FitViewport(MyGame.WIDTH, MyGame.HEIGHT, game.camera));
        this.shapeRenderer = new ShapeRenderer();

        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();
        tilesToDraw = new Vector<>();
        camera = new OrthographicCamera();
        camera.setToOrtho(false,width,height);
        camera.update();
        map = new Map("maps/map_test/mapa_alpha.tmx");
        mapWidth = map.getWidth();
        mapHeight = map.getHeight();
        unitList = new Vector<>();
    }

    @Override
    public void create(){
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
        stage.clear();

        this.skin = new Skin();
        this.skin.addRegions(game.assets.get("ui/uiskin.atlas", TextureAtlas.class));
        this.skin.add("default-font", game.font);
        this.skin.load(Gdx.files.internal("ui/uiskin.json"));
    }

    private void update(float delta){
        stage.act(delta);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f,1f,1f,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        map.render(camera);

        update(delta);

        stage.draw();

        game.batch.begin();
        game.batch.setProjectionMatrix(camera.combined);
        for(Tile tile : tilesToDraw){
            tile.render(game.batch);
        }
        for (Unit unit : unitList) {
            unit.render(game.batch);
        }
        game.batch.end();
        putInMapBounds();
    }

    private void putInMapBounds() {

        if (camera.position.x < camera.viewportWidth * camera.zoom / 2f)
            camera.position.x = camera.viewportWidth * camera.zoom / 2f;
        else if (camera.position.x > mapWidth - camera.viewportWidth * camera.zoom / 2f)
            camera.position.x = mapHeight - camera.viewportWidth * camera.zoom / 2f;

        if (camera.position.y < camera.viewportHeight * camera.zoom / 2f)
            camera.position.y = camera.viewportHeight * camera.zoom / 2f;
        else if (camera.position.y > mapHeight - camera.viewportHeight * camera.zoom / 2f)
            camera.position.y = mapWidth - camera.viewportHeight * camera.zoom / 2f;

    }
    private boolean isInMapBounds() {

        return camera.position.x >= camera.viewportWidth * camera.zoom / 2f
                && camera.position.x <= mapWidth - camera.viewportWidth * camera.zoom / 2f
                && camera.position.y >= camera.viewportHeight * camera.zoom / 2f
                && camera.position.y <= mapHeight - camera.viewportHeight * camera.zoom / 2f;

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
        if(keycode == Input.Keys.SPACE){
            game.screenManager.setScreen(ScreenManager.STATE.MAIN_MENU);
        }
        if(keycode == Input.Keys.T){
            game.screenManager.setScreen(ScreenManager.STATE.ENDGAME);
        }

        if(keycode == Input.Keys.L){
            map.bordersOnOff();
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
        Vector2 temp = lastClickTile;
        Vector3 clickCoordinates = new Vector3(screenX, screenY, 0);
        
        Vector3 position = camera.unproject(clickCoordinates);
        System.out.println("click: " + position);
        lastClickTile = map.convertCoordinates(position);
        
        Tile tile = map.getTile(lastClickTile);
        System.out.println("tile: " + tile.getType());
        if(tilesToDraw.contains(tile) && tile.getUnit() == null){
            System.out.println("moving unit");
            tilesToDraw.clear();
            currentlySelectedUnit.changePosition((int)lastClickTile.x, (int)lastClickTile.y);
            map.getTile(temp).setUnit(null);
            map.getTile(lastClickTile).setUnit(currentlySelectedUnit);
        }
        else{
            tilesToDraw.clear();
            currentlySelectedUnit = tile.getUnit();
            if(currentlySelectedUnit != null){
                setBordersOnTiles(lastClickTile);
            }
        }
        return false;
    }

    private void setBordersOnTiles(Vector2 lastClickTile){
        Vector2 temp = new Vector2();
        Tile tile;
        temp.x = lastClickTile.x; temp.y = lastClickTile.y;
        tile = map.getTile(temp);
        if(tile != null){
            tile.setBorder(1);
            tilesToDraw.add(tile);
        }
        temp.x = lastClickTile.x -1; temp.y = lastClickTile.y;
        tile = map.getTile(temp);
        if(tile != null){
            tile.setBorder(1);
            tilesToDraw.add(tile);
        }
        temp.x = lastClickTile.x + 1; temp.y = lastClickTile.y;
        tile = map.getTile(temp);
        if(tile != null){
            tile.setBorder(1);
            tilesToDraw.add(tile);
        }
        temp.x = lastClickTile.x; temp.y = lastClickTile.y-1;
        tile = map.getTile(temp);
        if(tile != null){
            tile.setBorder(1);
            tilesToDraw.add(tile);
        }
        temp.x = lastClickTile.x; temp.y = lastClickTile.y+1;
        tile = map.getTile(temp);
        if(tile != null){
            tile.setBorder(1);
            tilesToDraw.add(tile);
        }
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if(Gdx.input.getDeltaX() < 4 && Gdx.input.getDeltaX() > -4 
            && Gdx.input.getDeltaY(pointer) < 4 && Gdx.input.getDeltaY(pointer) > -4){
            return true;
        }
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
        camera.zoom += amountY/50;
        if(camera.zoom > 1.1375){
            camera.zoom = 1.1375f;
        }
        if(camera.zoom < 0.6){
            camera.zoom = 0.6f;
        }
        System.out.println("zoom: " + camera.zoom);
        putInMapBounds();
        return false;
    }
    @Override
    public void hide() {

    }
    @Override
    public void dispose(){
        stage.dispose();
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
            unit = new Knight((int)lastClickTile.x,(int)lastClickTile.y);
        }
        else{
            return false;
        }

        tile.setUnit(unit);
        unitList.add(unit);
        return true;
    }
}
