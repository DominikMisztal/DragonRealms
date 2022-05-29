package com.mygdx.dragonrealms.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.dragonrealms.MyGame;
import com.mygdx.dragonrealms.Player;
import com.mygdx.dragonrealms.map.Map;
import com.mygdx.dragonrealms.map.Tile;
import com.mygdx.dragonrealms.map.TileType;
import com.mygdx.dragonrealms.units.*;

import java.util.Vector;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;

public class MainGameScreen extends ApplicationAdapter implements InputProcessor, Screen {
    private static final int MENU_WIDTH = 350;
    private Map map;
    private MyGame game;
    private Skin skin;
    private Stage stage;
    private ShapeRenderer shapeRenderer;
    private TextButton playButton;
    private TextButton exitButton;
    private OrthographicCamera camera;
    
    public Vector<Tile> tilesToDraw;
    private float mapWidth;
    private float mapHeight;
    public Tile previouslySelectedTile;
    public Tile currentlySelectedTile;
    public Vector2 lastClickTile;
    public Unit currentlySelectedUnit;
    public Vector<Player> players;
    public int currentPlayer;
    boolean doDrawing = true;

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
        map = new Map("maps/map_test/mapa_alpha.tmx", this);
        map.getViewport().setCamera(camera);
        mapWidth = map.getMapWidth();
        mapHeight = map.getMapHeight();
        players = new Vector<>();
        players.add(new Player("Player 1"));
        players.add(new Player("Player 2"));
        players.add(new Player("Player 3"));
        currentPlayer = 0;
    }

    @Override
    public void create(){
    }

    @Override
    public void show() {
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(this);
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(map);
        Gdx.input.setInputProcessor(inputMultiplexer);
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

        camera.update();
        map.render(camera);

        game.batch.begin();
        game.batch.setProjectionMatrix(camera.combined);
        for(Tile tile : tilesToDraw){
            tile.render(game.batch);
        }
        Vector<Unit> temp = players.get(0).getUnits();
        for (Unit unit : temp) {
            unit.render(game.batch);
        }
        temp = players.get(1).getUnits();
        for (Unit unit : temp) {
            unit.render(game.batch);
        }
        temp = players.get(2).getUnits();
        for (Unit unit : temp) {
            unit.render(game.batch);
        }
        game.batch.end();
        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            camera.translate(Gdx.graphics.getDeltaTime() * -200,0);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)){
            camera.translate(0,Gdx.graphics.getDeltaTime() * -200);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            camera.translate(0,Gdx.graphics.getDeltaTime() * 200);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)){
            camera.translate(Gdx.graphics.getDeltaTime() * 200,0);
        }
        putInMapBounds();
        

        if(doDrawing){
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(new Color(0, 1, 0, 1f));
            shapeRenderer.rect(game.camera.viewportWidth - MENU_WIDTH, 0, MENU_WIDTH, game.camera.viewportHeight);
            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
            update(delta);
            stage.draw();
        }

    }

    private void putInMapBounds() {

        if (camera.position.x < camera.viewportWidth * camera.zoom / 2f)
            camera.position.x = camera.viewportWidth * camera.zoom / 2f;
        else if (camera.position.x > mapWidth + MENU_WIDTH * camera.zoom  - camera.viewportWidth * camera.zoom / 2f)
            camera.position.x = mapHeight + MENU_WIDTH * camera.zoom - camera.viewportWidth * camera.zoom / 2f;

        if (camera.position.y < camera.viewportHeight * camera.zoom / 2f)
            camera.position.y = camera.viewportHeight * camera.zoom / 2f;
        else if (camera.position.y > mapHeight - camera.viewportHeight * camera.zoom / 2f)
            camera.position.y = mapWidth - camera.viewportHeight * camera.zoom / 2f;

    }
    
    private boolean isInMapBounds() {

        return camera.position.x >= camera.viewportWidth * camera.zoom / 2f
                && camera.position.x <= mapWidth + MENU_WIDTH * camera.zoom - camera.viewportWidth  * camera.zoom / 2f
                && camera.position.y >= camera.viewportHeight * camera.zoom / 2f
                && camera.position.y <= mapHeight - camera.viewportHeight * camera.zoom / 2f;

    }
    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.I){
            spawnUnit(1);
            //System.out.println("Spawning warrior at X: " + (int)lastClickTile.x + " Y: " + (int)lastClickTile.y);
        }
        if(keycode == Input.Keys.O){
            spawnUnit(2);
            //System.out.println("Spawning archer at X: " + (int)lastClickTile.x + " Y: " + (int)lastClickTile.y);
        }
        if(keycode == Input.Keys.P){
            spawnUnit(3);
            //System.out.println("Spawning assassin at X: " + (int)lastClickTile.x + " Y: " + (int)lastClickTile.y);
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
        //TODO: REMOVE AFTER FINISHING
        if(keycode == Input.Keys.NUM_1){
            currentPlayer = 0;
        }
        if(keycode == Input.Keys.NUM_2){
            currentPlayer = 1;
        }
        if(keycode == Input.Keys.NUM_3){
            currentPlayer = 2;
        }
        if(keycode == Input.Keys.ESCAPE){
            doDrawing = (doDrawing == false) ? true : false;
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
        return false;
    }

    public void moveUnit(){
        currentlySelectedUnit.changePosition((int)currentlySelectedTile.getCoordinates().x, (int)currentlySelectedTile.getCoordinates().y);
        previouslySelectedTile.setUnit(null);
        currentlySelectedTile.setUnit(currentlySelectedUnit);
        currentlySelectedUnit.setCurrentMovement(currentlySelectedTile.getTempMovLeft());
        clearMovementTiles();
    }

    public void findUnitMovementRange(Unit unit, Tile tile){
        tile.setBorder(1);
        tilesToDraw.add(tile);
        int movementPoints = unit.getCurrentMovement();
        recursiveSearch(movementPoints, map.getTile((int)tile.coordinates.x+1, (int)tile.coordinates.y));
        recursiveSearch(movementPoints, map.getTile((int)tile.coordinates.x-1, (int)tile.coordinates.y));
        recursiveSearch(movementPoints, map.getTile((int)tile.coordinates.x, (int)tile.coordinates.y+1));
        recursiveSearch(movementPoints, map.getTile((int)tile.coordinates.x, (int)tile.coordinates.y-1));
    }

    private void recursiveSearch(int movementPoints, Tile tile){
        if(tile==null || movementPoints == 0){
            return;
        }
        if(tile.getMovementCost() == 99){
            tile.setBorder(3);
            tilesToDraw.add(tile);
            return;
        }
        if(tile.getUnit() != null && tile.getUnit().getPlayer() != players.get(currentPlayer)){
            tile.setBorder(2 );
            tilesToDraw.add(tile);
            return;
        }
        if(movementPoints - tile.getMovementCost() < 0){
           return;
        }
        movementPoints -= tile.getMovementCost();
        if(tile.getTempMovLeft() < movementPoints){
            tile.setTempMovLeft(movementPoints);
        }
        tilesToDraw.add(tile);
        tile.setBorder(1);
        recursiveSearch(movementPoints, map.getTile((int)tile.coordinates.x+1, (int)tile.coordinates.y));
        recursiveSearch(movementPoints, map.getTile((int)tile.coordinates.x-1, (int)tile.coordinates.y));
        recursiveSearch(movementPoints, map.getTile((int)tile.coordinates.x, (int)tile.coordinates.y+1));
        recursiveSearch(movementPoints, map.getTile((int)tile.coordinates.x, (int)tile.coordinates.y-1));
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
        Unit unit;
        if(currentlySelectedTile == null 
            || currentlySelectedTile.getUnit() != null 
                || currentlySelectedTile.getType() == TileType.MOUNTAIN 
                    || currentlySelectedTile.getType() == TileType.WATER){
            return false;
        }

        if(type == 1){
            unit = new Warrior(currentlySelectedTile, players.get(currentPlayer));
        }
        else if(type == 2){
            unit = new Archer(currentlySelectedTile, players.get(currentPlayer));
        }
        else if(type == 3){
            unit = new Knight(currentlySelectedTile, players.get(currentPlayer));
        }
        else{
            return false;
        }

        currentlySelectedTile.setUnit(unit);
        players.get(currentPlayer).addUnit(unit);
        return true;
    }

    public void clearMovementTiles(){
        for(Tile tile : tilesToDraw){
            tile.setBorder(0);
            tile.setTempMovLeft(0);
        }
        tilesToDraw.clear();
    }

    private void initButtons(){
        playButton = new TextButton("Menu", skin, "default");
        playButton.setSize(300,100);
        playButton.setPosition(MyGame.WIDTH - 325,MyGame.HEIGHT - 100);
        playButton.addAction(sequence(alpha(0), parallel(fadeIn(.5f),
                moveBy(0,-20,.5f, Interpolation.pow5Out))));
        playButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.screenManager.setScreen(ScreenManager.STATE.MAIN_MENU);
            }
        });

        exitButton = new TextButton("Exit", skin, "default");
        exitButton.setSize(300,100);
        exitButton.setPosition(MyGame.WIDTH - 325,MyGame.HEIGHT - 250);
        exitButton.addAction(sequence(alpha(0), parallel(fadeIn(.5f),
                moveBy(0,-20,.5f, Interpolation.pow5Out))));
        exitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                Gdx.app.exit();
            }
        });

        stage.addActor(playButton);
        stage.addActor(exitButton);
    }
}
