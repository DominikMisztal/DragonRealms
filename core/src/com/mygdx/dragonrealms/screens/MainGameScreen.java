package com.mygdx.dragonrealms.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
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
import com.mygdx.dragonrealms.screens.ScreenManager.STATE;
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
    private ShapeRenderer unitRenderer;
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
    private int playersCount;
    private int currentTurn;
    private int unitCost;
    public boolean gamePaused;
    public boolean drawHealthBars;
    public Mode currentMode;
    private UnitType unitToSpawn;

    Vector<Unit> temp;
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
        players.get(0).castle = new Castle(map.getTile(1,1), players.get(0)
                                                , camera.combined, 0);
        players.get(1).castle = new Castle(map.getTile(16,25), players.get(1)
                                                , camera.combined, 1);
        players.get(2).castle = new Castle(map.getTile(27,5), players.get(2)
                                                , camera.combined, 2);
        
        map.getTile(1, 1).setUnit(players.get(0).castle);     
        map.getTile(16, 25).setUnit(players.get(1).castle);  
        map.getTile(27,5).setUnit(players.get(2).castle);
        playersCount = 3;                                     
        currentPlayer = 0;
        currentTurn = 1;
        drawHealthBars = true;
        gamePaused = false;
        currentMode = Mode.NONE;
        unitRenderer = new ShapeRenderer();
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
        gamePaused = false;
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
        

        //draw units
        for(int i = 0; i < playersCount; i++){
            players.get(i).castle.render(game.batch);
            temp = players.get(i).getUnits();
            for (Unit unit : temp) {
                unit.render(game.batch);
            }
        }

        game.batch.end();
        // draw health bars
        unitRenderer.begin(ShapeType.Filled);
        unitRenderer.setProjectionMatrix(camera.combined);
        if(drawHealthBars){
            for(int i = 0; i < playersCount; i++){
                players.get(i).castle.renderHealthBar(unitRenderer);
                temp = players.get(i).getUnits();
                for (Unit unit : temp) {
                    unit.renderHealthBar(unitRenderer);
                }
            }
        }
        unitRenderer.end();

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
            unitSpawner(UnitType.WARRIOR);
        }
        if(keycode == Input.Keys.O){
            unitSpawner(UnitType.ARCHER);
        }
        if(keycode == Input.Keys.P){
            unitSpawner(UnitType.KNIGHT);
        }
        if(keycode == Input.Keys.G){
            unitSpawner(UnitType.GOLDMINE);
        }
        if(keycode == Input.Keys.SPACE){
            nextPlayer();
        }
        if(keycode == Input.Keys.ESCAPE){
            game.screenManager.setScreen(ScreenManager.STATE.MAIN_MENU);
        }
        if(keycode == Input.Keys.T){
            game.screenManager.setScreen(ScreenManager.STATE.ENDGAME);
        }

        if(keycode == Input.Keys.L){
            map.bordersOnOff();
        }
        if(keycode == Input.Keys.H){
            drawHealthBars = !drawHealthBars;
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

    public void moveUnit(Unit unit, Tile destination){
        unit.changePosition((int)destination.getCoordinates().x, (int)destination.getCoordinates().y);
        previouslySelectedTile.setUnit(null);
        destination.setUnit(currentlySelectedUnit);
        unit.setCurrentMovement(destination.getTempMovLeft());
        clearMovementTiles();
    }

    public void unitAttack(Unit attacker, Unit defender){
        if(getDistance(attacker.getCoordinates(), defender.getCoordinates()) <= attacker.getRange()){
            if(defender.damage(attacker.getAttack())){
                if(defender instanceof Castle){
                    defender.getPlayer().removePlayer(map);
                    players.remove(defender.getPlayer());
                    playersCount--;
                    if(playersCount == 1){
                        game.screenManager.setScreen(STATE.ENDGAME);
                    }
                }
                defender.getPlayer().getUnits().remove(defender);
                map.getTile(defender.getCoordinates()).setUnit(null);
            }
        }
        else{
            moveUnit(attacker, findClosestTile(defender, attacker));
            if(defender.damage(attacker.getAttack())){
                if(defender instanceof Castle){
                    defender.getPlayer().removePlayer(map);
                    players.remove(defender.getPlayer());
                    playersCount--;
                    if(playersCount == 1){
                        game.screenManager.setScreen(STATE.ENDGAME);
                    }
                }
                defender.getPlayer().getUnits().remove(defender);
                map.getTile(defender.getCoordinates()).setUnit(null);
            }
        }
        attacker.attacked = true;
    }

    public void findRangedAttack(Unit unit){
        int range = unit.getRange();

        recursiveSearchRanged(map.getTile((int)unit.getCoordinates().x+1, (int)unit.getCoordinates().y), range);
        recursiveSearchRanged(map.getTile((int)unit.getCoordinates().x-1, (int)unit.getCoordinates().y), range);
        recursiveSearchRanged(map.getTile((int)unit.getCoordinates().x, (int)unit.getCoordinates().y+1), range);
        recursiveSearchRanged(map.getTile((int)unit.getCoordinates().x, (int)unit.getCoordinates().y-1), range);
    }

    private void recursiveSearchRanged(Tile tile, int rangeLeft){
        if(tile==null || rangeLeft == 0){
            return;
        }
        if(tile.getUnit() != null && tile.getUnit().getPlayer() != players.get(currentPlayer)){
            tile.setBorder(2 );
            if(!tilesToDraw.contains(tile))
                tilesToDraw.add(tile);
            return;
        }
        
        recursiveSearchRanged(map.getTile((int)tile.coordinates.x+1, (int)tile.coordinates.y), rangeLeft-1);
        recursiveSearchRanged(map.getTile((int)tile.coordinates.x-1, (int)tile.coordinates.y), rangeLeft-1);
        recursiveSearchRanged(map.getTile((int)tile.coordinates.x, (int)tile.coordinates.y+1), rangeLeft-1);
        recursiveSearchRanged(map.getTile((int)tile.coordinates.x, (int)tile.coordinates.y-1), rangeLeft-1);
    }

    public Tile findClosestTile(Unit target, Unit toMove){
        Tile closestTile = null, temp;
        double distance = 9999;
        temp = map.getTile((int)target.getCoordinates().x + 1, (int)target.getCoordinates().y);
        if(temp != null){
            if(tilesToDraw.contains(temp) && getDistance(map.getTile(toMove.getCoordinates()), temp) < distance){
                distance = getDistance(map.getTile(toMove.getCoordinates()), temp);
                closestTile = temp;
            }
        }
        temp = map.getTile((int)target.getCoordinates().x - 1, (int)target.getCoordinates().y);
        if(temp != null){
            if(tilesToDraw.contains(temp) && getDistance(map.getTile(toMove.getCoordinates()), temp) < distance){
                distance = getDistance(map.getTile(toMove.getCoordinates()), temp);
                closestTile = temp;
            }
        }
        temp = map.getTile((int)target.getCoordinates().x, (int)target.getCoordinates().y + 1);
        if(temp != null){
            if(tilesToDraw.contains(temp) && getDistance(map.getTile(toMove.getCoordinates()), temp) < distance){
                distance = getDistance(map.getTile(toMove.getCoordinates()), temp);
                closestTile = temp;
            }
        }
        temp = map.getTile((int)target.getCoordinates().x, (int)target.getCoordinates().y - 1);
        if(temp != null){
            if(tilesToDraw.contains(temp) && getDistance(map.getTile(toMove.getCoordinates()), temp) < distance){
                distance = getDistance(map.getTile(toMove.getCoordinates()), temp);
                closestTile = temp;
            }
        }
        System.out.println("distance: " + distance);

        return closestTile;
    }

    public void nextPlayer(){
        currentPlayer++;
        if(currentPlayer >= playersCount){
            currentPlayer = 0;
            nextTurn();
        }
    }

    private void nextTurn(){
        // reset units movements
        // give gold
        Vector<Unit> temp;
        for(Player player : players){
            temp = player.getUnits();
            for(Unit unit : temp){
                unit.resetMovement();
                unit.attacked = false;
            }
            player.addGold();
        }
    }

    public double getDistance(Tile tile1, Tile tile2){
        double x = Math.abs(tile1.coordinates.x - tile2.coordinates.x);
        double y = Math.abs(tile1.coordinates.y - tile2.coordinates.y);
        return Math.sqrt(x*x + y*y);
    }

    public double getDistance(Vector2 coordinates1, Vector2 coordinates2){
        double x = Math.abs(coordinates1.x - coordinates2.x);
        double y = Math.abs(coordinates1.y - coordinates2.y);
        return Math.sqrt(x*x + y*y);
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
        if(tile.getUnit() != null 
            && tile.getUnit().getPlayer() != players.get(currentPlayer)){

            if(currentlySelectedUnit.attacked == true){
                tile.setBorder(3);
                return;
            }
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
        if(button == Input.Buttons.RIGHT){
            clearMovementTiles();
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
            if(Gdx.input.getDeltaX() < 4 && Gdx.input.getDeltaX() > -4 
                && Gdx.input.getDeltaY(pointer) < 4 && Gdx.input.getDeltaY(pointer) > -4){
                return true;
            }
            if(isInMapBounds()){
                camera.translate(-Gdx.input.getDeltaX(pointer), Gdx.input.getDeltaY(pointer));
            }
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
        if(camera.zoom > 1.412){
            camera.zoom = 1.412f;
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

    private void unitSpawner(UnitType type){
        int cost = 0;
        if(type == UnitType.ARCHER){
            cost = 4;
        }
        if(type == UnitType.KNIGHT){
            cost = 7;
        }
        if(type == UnitType.WARRIOR){
            cost = 3;
        }
        if(type == UnitType.GOLDMINE){
            cost = 10;
        }
        if(players.get(currentPlayer).gold - cost < 0){
            System.out.println("can't spawn");
            clearMovementTiles();
            return;
        }
        unitCost = cost;
        unitToSpawn = type;
        currentMode = Mode.SPAWN_UNIT;
        clearMovementTiles();
        findSpawnLocations();
    }

    private void findSpawnLocations(){
        Tile temp;
        Player player = players.get(currentPlayer);
        temp = map.getTile((int)player.castle.getCoordinates().x-1, (int)player.castle.getCoordinates().y);
        if(temp != null && temp.getUnit() == null
        && temp.getType() != TileType.WATER
        && temp.getType() != TileType.MOUNTAIN){
            temp.setBorder(1);
            tilesToDraw.add( temp);
        }
        temp = map.getTile((int)player.castle.getCoordinates().x+1, (int)player.castle.getCoordinates().y);
        if(temp != null && temp.getUnit() == null
        && temp.getType() != TileType.WATER
        && temp.getType() != TileType.MOUNTAIN){
            temp.setBorder(1);
            tilesToDraw.add( temp);
        }
        temp = map.getTile((int)player.castle.getCoordinates().x, (int)player.castle.getCoordinates().y-1);
        if(temp != null && temp.getUnit() == null
        && temp.getType() != TileType.WATER
        && temp.getType() != TileType.MOUNTAIN){
            temp.setBorder(1);
            tilesToDraw.add( temp);
        }
        temp = map.getTile((int)player.castle.getCoordinates().x, (int)player.castle.getCoordinates().y+1);
        if(temp != null && temp.getUnit() == null
        && temp.getType() != TileType.WATER
        && temp.getType() != TileType.MOUNTAIN){
            temp.setBorder(1);
            tilesToDraw.add( temp);
        }
        if(unitToSpawn == UnitType.GOLDMINE){
            for(Unit unit : player.getUnits()){
                temp = map.getTile((int)unit.getCoordinates().x-1, (int)unit.getCoordinates().y);
                if(temp != null && temp.getUnit() == null){
                    temp.setBorder(1);
                    tilesToDraw.add( temp);
                }
                temp = map.getTile((int)unit.getCoordinates().x+1, (int)unit.getCoordinates().y);
                if(temp != null && temp.getUnit() == null
                && temp.getType() != TileType.WATER
                && temp.getType() != TileType.MOUNTAIN){
                    temp.setBorder(1);
                    tilesToDraw.add( temp);
                }
                temp = map.getTile((int)unit.getCoordinates().x, (int)unit.getCoordinates().y-1);
                if(temp != null && temp.getUnit() == null
                && temp.getType() != TileType.WATER
                && temp.getType() != TileType.MOUNTAIN){
                    temp.setBorder(1);
                    tilesToDraw.add( temp);
                }
                temp = map.getTile((int)unit.getCoordinates().x, (int)unit.getCoordinates().y+1);
                if(temp != null && temp.getUnit() == null
                && temp.getType() != TileType.WATER
                && temp.getType() != TileType.MOUNTAIN){
                    temp.setBorder(1);
                    tilesToDraw.add( temp);
                }
            }
        }
        
    }

    public void spawnUnit(){
        Unit unit;

        if(currentlySelectedTile == null 
            || currentlySelectedTile.getUnit() != null 
                || currentlySelectedTile.getType() == TileType.MOUNTAIN 
                    || currentlySelectedTile.getType() == TileType.WATER){
            return;
        }

        if(unitToSpawn == UnitType.WARRIOR){
            unit = new Warrior(currentlySelectedTile, players.get(currentPlayer), camera.combined);
        }
        else if(unitToSpawn == UnitType.ARCHER){
            unit = new Archer(currentlySelectedTile, players.get(currentPlayer), camera.combined);
        }
        else if(unitToSpawn == UnitType.KNIGHT){
            unit = new Knight(currentlySelectedTile, players.get(currentPlayer), camera.combined);
        }
        else if(unitToSpawn == UnitType.GOLDMINE){
            unit = new GoldMine(currentlySelectedTile, players.get(currentPlayer), camera.combined);
            players.get(currentPlayer).goldMinesCount++;
        }
        else{
            return;
        }
        //System.out.println("spawned");

        currentlySelectedTile.setUnit(unit);
        players.get(currentPlayer).addUnit(unit);
        clearMovementTiles();
        currentlySelectedTile = null;
        currentMode = Mode.NONE;
        players.get(currentPlayer).gold -= unitCost;
        return;
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
