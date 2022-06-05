package com.mygdx.dragonrealms.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
import com.mygdx.dragonrealms.SoundController;
import com.mygdx.dragonrealms.map.Map;
import com.mygdx.dragonrealms.map.Tile;
import com.mygdx.dragonrealms.map.TileType;
import com.mygdx.dragonrealms.screens.ScreenManager.STATE;
import com.mygdx.dragonrealms.units.*;

import java.util.Vector;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;

public class MainGameScreen extends ApplicationAdapter implements InputProcessor, Screen {

    public static final int MENU_WIDTH = 350;
    private Map map;
    private MyGame game;
    private Skin skin;
    private Stage stage;
    private ShapeRenderer shapeRenderer;
    private ShapeRenderer unitRenderer;
    private OrthographicCamera camera;
    public SoundController soundController;

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
    private boolean scrollable;
    public boolean scrolled;
    float UNIT_SHOP_X;
    float UNIT_SHOP_Y;
    float MENU_BUTTON_X;
    float MENU_BUTTON_Y;
    Vector2 PLAYER1_CASTLE = new Vector2(Map.TILESIZE,Map.TILESIZE);
    Vector2 PLAYER2_CASTLE = new Vector2(16 * Map.TILESIZE ,25 * Map.TILESIZE);
    Vector2 PLAYER3_CASTLE = new Vector2(27 * Map.TILESIZE,5 * Map.TILESIZE);

    Vector<Unit> temp;
    private Texture backgroundTexture;
    private Texture goldmineTexture;
    private Texture archerTexture;
    private Texture knightTexture;
    private Texture warriorTexture;
    SpriteBatch guiBatch;
    private boolean nextPlayer = false;
    private boolean doDrawing = false;

    public MainGameScreen(MyGame game){
        this.game = game;
        guiBatch = new SpriteBatch();
        this.stage = new Stage(new FitViewport(MyGame.WIDTH, MyGame.HEIGHT, game.camera), guiBatch);
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
        game.players = new Vector<>();
        players.add(new Player("Player 1"));
        players.add(new Player("Player 2"));
        players.add(new Player("Player 3"));
        game.players.add(players.get(0));
        game.players.add(players.get(1));
        game.players.add(players.get(2));
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
        backgroundTexture = new Texture(Gdx.files.internal("woodImage.jpg"));
        goldmineTexture = new Texture(Gdx.files.internal("textures/buildings/goldmine.png"));
        archerTexture = new Texture(Gdx.files.internal("textures/archer/archer1.png"));
        knightTexture = new Texture(Gdx.files.internal("textures/knight/knight1.png"));
        warriorTexture = new Texture(Gdx.files.internal("textures/warrior/warrior1.png"));
        soundController = new SoundController();
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

        UNIT_SHOP_X = game.camera.viewportWidth - MENU_WIDTH + 10;
        UNIT_SHOP_Y = 150;
        MENU_BUTTON_X = MyGame.WIDTH - 330;
        MENU_BUTTON_Y = MyGame.HEIGHT - 70;
        initButtons();
        gamePaused = false;
        setCurrentPlayerCamera();
    }

    private void update(float delta){
        stage.act(delta);
    }

    @Override
    public void render(float delta) {

        drawCurrentGameplayArea();
        putInMapBounds();
        drawPlayerGUI();

        update(delta);
        stage.draw();

        if(nextPlayer){
            drawNextPlayerOverlay();
        }
        if(tilesToDraw.size() > 0){
            displayCurrentTileName();
        }
    }

    private void displayCurrentTileName(){
        stage.getBatch().begin();
        GlyphLayout layout = new GlyphLayout(game.tileFont, "");
        if(tilesToDraw.size() > 0){
            Vector2 tempVec = new Vector2(tilesToDraw.get(0).coordinates);
            tempVec.scl(Map.TILESIZE);
            if(tempVec.equals(PLAYER1_CASTLE)){
                layout.setText(game.tileFont, "PLAYER 1 CASTLE");
            }
            else if(tempVec.equals(PLAYER2_CASTLE)){
                layout.setText(game.tileFont, "PLAYER 2 CASTLE");
            }
            else if(tempVec.equals(PLAYER3_CASTLE)){
                layout.setText(game.tileFont, "PLAYER 3 CASTLE");
            }
            else if(tilesToDraw.get(0).getUnit() != null){
                layout.setText(game.tileFont, tilesToDraw.get(0).getUnit().getUnitName());
            }
            else{
                layout.setText(game.tileFont, tilesToDraw.get(0).getType().toString());
            }
            float fontX = (MyGame.WIDTH - MENU_WIDTH) / 2f - layout.width / 2f;
            float fontY = MyGame.HEIGHT - 20;
            game.tileFont.draw(stage.getBatch(), layout, fontX, fontY);
        }
        stage.getBatch().end();
    }

    private void drawCurrentGameplayArea(){
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
        
        game.batch.begin();
        for(Tile tile : tilesToDraw){
            tile.render(game.batch);
        }
        game.batch.end();

        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            camera.translate( 0,Gdx.graphics.getDeltaTime() * 400);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)){
            camera.translate(Gdx.graphics.getDeltaTime() * 400,0);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)){
            camera.translate(0, Gdx.graphics.getDeltaTime() * -400);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            camera.translate(Gdx.graphics.getDeltaTime() * -400,0);
        }

    }

    private void drawPlayerGUI(){
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        guiBatch.begin();
            stage.getBatch().draw(backgroundTexture,MyGame.WIDTH - MENU_WIDTH,0, MENU_WIDTH, MyGame.HEIGHT);
            GlyphLayout layout = new GlyphLayout(game.font, String.format("Current player number: %d", currentPlayer + 1));
            float fontX = MyGame.WIDTH - MENU_WIDTH / 2f - (layout.width) / 2f;
            float fontY = MENU_BUTTON_Y - 40;
            game.font.draw(guiBatch, layout, fontX, fontY);
            stage.getBatch().draw(goldmineTexture, UNIT_SHOP_X + 10, UNIT_SHOP_Y + 160, 100, 100);
            stage.getBatch().draw(archerTexture, UNIT_SHOP_X - 40, UNIT_SHOP_Y, 200, 200);
            stage.getBatch().draw(knightTexture,UNIT_SHOP_X - 30, UNIT_SHOP_Y - 110, 200, 200);
            stage.getBatch().draw(warriorTexture,UNIT_SHOP_X - 40, UNIT_SHOP_Y - 200, 200, 200);
            game.font.draw(guiBatch, String.format("Your gold: %d", players.get(currentPlayer).gold), UNIT_SHOP_X + 170, UNIT_SHOP_Y + 300);
        guiBatch.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
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
        if(nextPlayer){
            nextPlayer = false;
            return false;
        }
        if(keycode == Input.Keys.SPACE){
            nextPlayer();
        }
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
        if(screenX >= 1800 - 350){
            scrollable = false;
        }
        else {
            scrollable = true;
            scrolled = false;
        }
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
                    if(playersCount == 2){
                        defender.getPlayer().setPlace(3);
                    }
                    if(playersCount == 1){
                        defender.getPlayer().setPlace(2);
                    }
                    if(playersCount == 0){
                        players.get(0).isWinner = true;
                        defender.getPlayer().setPlace(1);
                        game.screenManager.setScreen(STATE.ENDGAME);
                    }
                }
                defender.getPlayer().getUnits().remove(defender);
                defender.getPlayer().unitsLost++;
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
                        players.get(0).isWinner = true;
                        game.screenManager.setScreen(STATE.ENDGAME);
                    }
                }
                defender.getPlayer().getUnits().remove(defender);
                defender.getPlayer().unitsLost++;
                map.getTile(defender.getCoordinates()).setUnit(null);
            }
        }
        attacker.attacked = true;
        if(attacker instanceof Warrior){
            soundController.playWarriorAttack();
        }
        if(attacker instanceof Knight){
            soundController.playKnightAttack();
        }
        if(attacker instanceof Archer){
            soundController.playArcherAttack();
        }
        changeAttackTiles();
    }

    private void changeAttackTiles(){
        for(Tile tile : tilesToDraw){
            if(tile.getBorder() == 2){
                tile.clearBorder();
                tile.setBorder(4);
            }
        }
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
            tile.setBorder(2);
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
        if(temp != null && temp.getUnit() == null){
            if(tilesToDraw.contains(temp) && getDistance(map.getTile(toMove.getCoordinates()), temp) < distance){
                distance = getDistance(map.getTile(toMove.getCoordinates()), temp);
                closestTile = temp;
            }
        }
        temp = map.getTile((int)target.getCoordinates().x - 1, (int)target.getCoordinates().y);
        if(temp != null && temp.getUnit() == null){
            if(tilesToDraw.contains(temp) && getDistance(map.getTile(toMove.getCoordinates()), temp) < distance){
                distance = getDistance(map.getTile(toMove.getCoordinates()), temp);
                closestTile = temp;
            }
        }
        temp = map.getTile((int)target.getCoordinates().x, (int)target.getCoordinates().y + 1);
        if(temp != null && temp.getUnit() == null){
            if(tilesToDraw.contains(temp) && getDistance(map.getTile(toMove.getCoordinates()), temp) < distance){
                distance = getDistance(map.getTile(toMove.getCoordinates()), temp);
                closestTile = temp;
            }
        }
        temp = map.getTile((int)target.getCoordinates().x, (int)target.getCoordinates().y - 1);
        if(temp != null && temp.getUnit() == null){
            if(tilesToDraw.contains(temp) && getDistance(map.getTile(toMove.getCoordinates()), temp) < distance){
                distance = getDistance(map.getTile(toMove.getCoordinates()), temp);
                closestTile = temp;
            }
        }
        System.out.println("distance: " + distance);

        return closestTile;
    }
    private void setCurrentPlayerCamera(){
        switch (currentPlayer){
            case 0:
                camera.position.set(PLAYER1_CASTLE, 0);
                break;
            case 1:
                camera.position.set(PLAYER2_CASTLE, 0);
                break;
            case 2:
                camera.position.set(PLAYER3_CASTLE, 0);
                break;
        }
        putInMapBounds();
    }
    public void drawNextPlayerOverlay(){
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        guiBatch.begin();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.rect(0,0,MyGame.WIDTH, MyGame.HEIGHT);
        shapeRenderer.setColor(0,0,0,0.8f);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        guiBatch.end();
        guiBatch.begin();
        GlyphLayout playerLayout = new GlyphLayout(game.font, String.format("Now is playing player number %d\n" +
                "     Type any key to begin...", currentPlayer + 1));
        float fontX = MyGame.WIDTH / 2f - (playerLayout.width) / 2f;
        float fontY = MyGame.HEIGHT / 2f;
        game.font.draw(guiBatch, playerLayout, fontX, fontY);
        guiBatch.end();
    }

    public void nextPlayer(){
        currentPlayer++;
        if(currentPlayer >= playersCount){
            currentPlayer = 0;
            nextTurn();
        }
        nextPlayer = true;
        setCurrentPlayerCamera();
        clearMovementTiles();
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
        if(movementPoints == 0){
            lookForEnemyMelee(tile);
        }
        recursiveSearchUp(movementPoints, map.getTile((int)tile.coordinates.x, (int)tile.coordinates.y+1));
        recursiveSearchDown(movementPoints, map.getTile((int)tile.coordinates.x, (int)tile.coordinates.y-1));
        recursiveSearchLeft(movementPoints, map.getTile((int)tile.coordinates.x-1, (int)tile.coordinates.y));
        recursiveSearchRight(movementPoints, map.getTile((int)tile.coordinates.x+1, (int)tile.coordinates.y));
    }

    private void recursiveSearchUp(int movementPoints, Tile tile){
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

            if(currentlySelectedUnit.attacked){
                tile.setBorder(3);
                tilesToDraw.add(tile);
                return;
            }
            tile.setBorder(2 );
            tilesToDraw.add(tile);
            return;
        }
        if(movementPoints - tile.getMovementCost() < 0){
            lookForEnemyMelee(tile);
           return;
        }
        movementPoints -= tile.getMovementCost();
        if(tile.getTempMovLeft() < movementPoints){
            tile.setTempMovLeft(movementPoints);
        }
        tilesToDraw.add(tile);
        tile.setBorder(1);
        if(movementPoints == 0){
            lookForEnemyMelee(tile);
            return;
        }
        recursiveSearchUp(movementPoints, map.getTile((int)tile.coordinates.x, (int)tile.coordinates.y+1));
        recursiveSearchLeft(movementPoints, map.getTile((int)tile.coordinates.x-1, (int)tile.coordinates.y));
        recursiveSearchRight(movementPoints, map.getTile((int)tile.coordinates.x+1, (int)tile.coordinates.y));
    }

    private void recursiveSearchRight(int movementPoints, Tile tile){
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

            if(currentlySelectedUnit.attacked){
                tile.setBorder(3);
                tilesToDraw.add(tile);
                return;
            }
            tile.setBorder(2 );
            tilesToDraw.add(tile);
            return;
        }
        if(movementPoints - tile.getMovementCost() < 0){
            lookForEnemyMelee(tile);
           return;
        }
        movementPoints -= tile.getMovementCost();
        if(tile.getTempMovLeft() < movementPoints){
            tile.setTempMovLeft(movementPoints);
        }
        tilesToDraw.add(tile);
        tile.setBorder(1);
        if(movementPoints == 0){
            lookForEnemyMelee(tile);
            return;
        }
        recursiveSearchRight(movementPoints, map.getTile((int)tile.coordinates.x+1, (int)tile.coordinates.y));
        recursiveSearchUp(movementPoints, map.getTile((int)tile.coordinates.x, (int)tile.coordinates.y+1));
        recursiveSearchDown(movementPoints, map.getTile((int)tile.coordinates.x, (int)tile.coordinates.y-1));
    }
    
    private void recursiveSearchDown(int movementPoints, Tile tile){
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

            if(currentlySelectedUnit.attacked){
                tile.setBorder(3);
                tilesToDraw.add(tile);
                return;
            }
            tile.setBorder(2 );
            tilesToDraw.add(tile);
            return;
        }
        if(movementPoints - tile.getMovementCost() < 0){
            lookForEnemyMelee(tile);
           return;
        }
        movementPoints -= tile.getMovementCost();
        if(tile.getTempMovLeft() < movementPoints){
            tile.setTempMovLeft(movementPoints);
        }
        tilesToDraw.add(tile);
        tile.setBorder(1);
        if(movementPoints == 0){
            lookForEnemyMelee(tile);
            return;
        }
        recursiveSearchDown(movementPoints, map.getTile((int)tile.coordinates.x, (int)tile.coordinates.y-1));
        recursiveSearchLeft(movementPoints, map.getTile((int)tile.coordinates.x-1, (int)tile.coordinates.y));
        recursiveSearchRight(movementPoints, map.getTile((int)tile.coordinates.x+1, (int)tile.coordinates.y));
    }
    
    private void recursiveSearchLeft(int movementPoints, Tile tile){
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

            if(currentlySelectedUnit.attacked){
                tile.setBorder(3);
                tilesToDraw.add(tile);
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
        if(movementPoints == 0){
            lookForEnemyMelee(tile);
            return;
        }
        recursiveSearchUp(movementPoints, map.getTile((int)tile.coordinates.x, (int)tile.coordinates.y+1));
        recursiveSearchLeft(movementPoints, map.getTile((int)tile.coordinates.x-1, (int)tile.coordinates.y));
        recursiveSearchDown(movementPoints, map.getTile((int)tile.coordinates.x, (int)tile.coordinates.y-1));
    }

    private void lookForEnemyMelee(Tile tile){
        if(tile == null){
            return;
        }
        Tile temp = map.getTile((int)tile.getCoordinates().x+1, (int)tile.getCoordinates().y );
        if(temp != null 
            && temp.getUnit() != null 
            && temp.getUnit().getPlayer() != players.get(currentPlayer)){
            temp.setBorder(2 );
            tilesToDraw.add(temp);
        }
        temp = map.getTile((int)tile.getCoordinates().x-1, (int)tile.getCoordinates().y );
        if(temp != null && temp.getUnit() != null && temp.getUnit().getPlayer() != players.get(currentPlayer) ){
            temp.setBorder(2 );
            tilesToDraw.add(temp);
        }
        temp = map.getTile((int)tile.getCoordinates().x, (int)tile.getCoordinates().y-1 );
        if(temp != null && temp.getUnit() != null && temp.getUnit().getPlayer() != players.get(currentPlayer) ){
            temp.setBorder(2 );
            tilesToDraw.add(temp);
        }
        temp = map.getTile((int)tile.getCoordinates().x, (int)tile.getCoordinates().y-1 );
        if(temp != null && temp.getUnit() != null && temp.getUnit().getPlayer() != players.get(currentPlayer) ){
            temp.setBorder(2 );
            tilesToDraw.add(temp);
        }
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        nextPlayer = false;
        if(button == Input.Buttons.RIGHT){
            clearMovementTiles();
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        nextPlayer = false;
        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && scrollable){
            if(Gdx.input.getDeltaX() < 4 && Gdx.input.getDeltaX() > -4 
                && Gdx.input.getDeltaY(pointer) < 4 && Gdx.input.getDeltaY(pointer) > -4){
                return true;
            }
            if(isInMapBounds()){
                scrolled = true;
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
        nextPlayer = false;
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
            tilesToDraw.add(temp);
        }
        temp = map.getTile((int)player.castle.getCoordinates().x+1, (int)player.castle.getCoordinates().y);
        if(temp != null && temp.getUnit() == null
        && temp.getType() != TileType.WATER
        && temp.getType() != TileType.MOUNTAIN){
            temp.setBorder(1);
            tilesToDraw.add(temp);
        }
        temp = map.getTile((int)player.castle.getCoordinates().x, (int)player.castle.getCoordinates().y-1);
        if(temp != null && temp.getUnit() == null
        && temp.getType() != TileType.WATER
        && temp.getType() != TileType.MOUNTAIN){
            temp.setBorder(1);
            tilesToDraw.add(temp);
        }
        temp = map.getTile((int)player.castle.getCoordinates().x, (int)player.castle.getCoordinates().y+1);
        if(temp != null && temp.getUnit() == null
        && temp.getType() != TileType.WATER
        && temp.getType() != TileType.MOUNTAIN){
            temp.setBorder(1);
            tilesToDraw.add(temp);
        }
        if(unitToSpawn == UnitType.GOLDMINE){
            for(Unit unit : player.getUnits()){
                temp = map.getTile((int)unit.getCoordinates().x-1, (int)unit.getCoordinates().y);
                if(temp != null && temp.getUnit() == null){
                    temp.setBorder(1);
                    tilesToDraw.add(temp);
                }
                temp = map.getTile((int)unit.getCoordinates().x+1, (int)unit.getCoordinates().y);
                if(temp != null && temp.getUnit() == null
                && temp.getType() != TileType.WATER
                && temp.getType() != TileType.MOUNTAIN){
                    temp.setBorder(1);
                    tilesToDraw.add(temp);
                }
                temp = map.getTile((int)unit.getCoordinates().x, (int)unit.getCoordinates().y-1);
                if(temp != null && temp.getUnit() == null
                && temp.getType() != TileType.WATER
                && temp.getType() != TileType.MOUNTAIN){
                    temp.setBorder(1);
                    tilesToDraw.add(temp);
                }
                temp = map.getTile((int)unit.getCoordinates().x, (int)unit.getCoordinates().y+1);
                if(temp != null && temp.getUnit() == null
                && temp.getType() != TileType.WATER
                && temp.getType() != TileType.MOUNTAIN){
                    temp.setBorder(1);
                    tilesToDraw.add(temp);
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
            soundController.playWarriorSelect();
        }
        else if(unitToSpawn == UnitType.ARCHER){
            unit = new Archer(currentlySelectedTile, players.get(currentPlayer), camera.combined);
            soundController.playArcherSelect();
        }
        else if(unitToSpawn == UnitType.KNIGHT){
            unit = new Knight(currentlySelectedTile, players.get(currentPlayer), camera.combined);
            soundController.playKnightSelect();
        }
        else if(unitToSpawn == UnitType.GOLDMINE){
            unit = new GoldMine(currentlySelectedTile, players.get(currentPlayer), camera.combined);
            soundController.playGoldMineSelect();
            players.get(currentPlayer).goldMinesCount++;
        }
        else{
            return;
        }

        currentlySelectedTile.setUnit(unit);
        players.get(currentPlayer).addUnit(unit);
        clearMovementTiles();
        currentlySelectedTile = null;
        currentMode = Mode.NONE;
        players.get(currentPlayer).gold -= unitCost;
        players.get(currentPlayer).totalGoldSpent += unitCost;
        players.get(currentPlayer).unitsBought++;
    }

    public void clearMovementTiles(){
        for(Tile tile : tilesToDraw){
            tile.clearBorder();
            tile.setTempMovLeft(0);
        }
        tilesToDraw.clear();
        doDrawing = false;
    }

    private void initButtons(){
        TextButton menuButton = new TextButton("Menu", skin, "default");
        menuButton.setSize(150,70);
        menuButton.setPosition(MENU_BUTTON_X, MENU_BUTTON_Y);
        menuButton.addAction(parallel(fadeIn(.5f),
                moveBy(0,-20,.5f, Interpolation.pow5Out)));
        menuButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.soundController.playClick();
                game.screenManager.setScreen(ScreenManager.STATE.MAIN_MENU);
            }
        });

        TextButton endTurnButton = new TextButton("End turn", skin, "default");
        endTurnButton.setSize(150,70);
        endTurnButton.setPosition(MENU_BUTTON_X + 160, MENU_BUTTON_Y);
        endTurnButton.addAction(parallel(fadeIn(.5f),
                moveBy(0,-20,.5f, Interpolation.pow5Out)));
        endTurnButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.soundController.playClick();
                nextPlayer();
            }
        });

        Skin skin1 = new Skin();
        skin1.addRegions(game.assets.get("ui/uiskin.atlas", TextureAtlas.class));
        skin1.add("default-font", game.font1);
        skin1.load(Gdx.files.internal("ui/uiskin.json"));

        TextButton goldmineButton = new TextButton("Buy goldmine / 10gp", skin1, "default");
        goldmineButton.setSize(185,64);
        goldmineButton.setPosition(UNIT_SHOP_X + 140,UNIT_SHOP_Y + 200);
        goldmineButton.addAction(parallel(fadeIn(.5f),
                moveBy(0,-20,.5f, Interpolation.pow5Out)));
        goldmineButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.soundController.playClick();
                unitSpawner(UnitType.GOLDMINE);
            }
        });

        TextButton archerButton = new TextButton("Buy archer / 4gp", skin, "default");
        archerButton.setSize(185,64);
        archerButton.setPosition(UNIT_SHOP_X + 140,UNIT_SHOP_Y + 100);
        archerButton.addAction(parallel(fadeIn(.5f),
                moveBy(0,-20,.5f, Interpolation.pow5Out)));
        archerButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.soundController.playClick();
                setCurrentPlayerCamera();
                unitSpawner(UnitType.ARCHER);
            }
        });

        TextButton knightButton = new TextButton("Buy knight / 7gp", skin, "default");
        knightButton.setSize(185,64);
        knightButton.setPosition(UNIT_SHOP_X + 140,UNIT_SHOP_Y);
        knightButton.addAction(parallel(fadeIn(.5f),
                moveBy(0,-20,.5f, Interpolation.pow5Out)));
        knightButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.soundController.playClick();
                setCurrentPlayerCamera();
                unitSpawner(UnitType.KNIGHT);
            }
        });

        TextButton warriorButton = new TextButton("Buy warrior / 3gp", skin, "default");
        warriorButton.setSize(185,64);
        warriorButton.setPosition(UNIT_SHOP_X + 140,UNIT_SHOP_Y - 100);
        warriorButton.addAction(sequence(alpha(0), parallel(fadeIn(.5f),
                moveBy(0,-20,.5f, Interpolation.pow5Out))));
        warriorButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.soundController.playClick();
                setCurrentPlayerCamera();
                unitSpawner(UnitType.WARRIOR);
            }
        });

        stage.addActor(goldmineButton);
        stage.addActor(menuButton);
        stage.addActor(endTurnButton);
        stage.addActor(archerButton);
        stage.addActor(warriorButton);
        stage.addActor(knightButton);
    }
}

