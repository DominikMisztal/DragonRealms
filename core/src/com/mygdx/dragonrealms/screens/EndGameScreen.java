package com.mygdx.dragonrealms.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.dragonrealms.MyGame;

public class EndGameScreen implements Screen {
    private MyGame game;
    private Stage stage;
    private Skin skin;
    private ShapeRenderer shapeRenderer;
    private Texture archerTexture;
    private Texture knightTexture;
    private Texture warriorTexture;



    public EndGameScreen(MyGame game){
        this.game = game;
        this.stage = new Stage(new FitViewport(MyGame.WIDTH, MyGame.HEIGHT, game.camera), game.batch);
        this.shapeRenderer = new ShapeRenderer();
        archerTexture = new Texture(Gdx.files.internal("textures/archer/archer1.png"));
        knightTexture = new Texture(Gdx.files.internal("textures/knight/knight1.png"));
        warriorTexture = new Texture(Gdx.files.internal("textures/warrior/warrior1.png"));
    }

    @Override
    public void show() {
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(inputMultiplexer);
        stage.clear();

        this.skin = new Skin();
        this.skin.addRegions(game.assets.get("ui/uiskin.atlas", TextureAtlas.class));
        this.skin.add("default-font", game.font);
        this.skin.load(Gdx.files.internal("ui/uiskin.json"));

        initButtons();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        drawLayout();
        drawPlayerName();
        drawEveryPlayerUnits();
        drawPlayerPlaces();
        drawEveryPlayerStatistics();

        stage.draw();
        update(delta);
    }

    private void drawLayout(){
        game.batch.begin();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.rectLine(MyGame.WIDTH/3f, 0, MyGame.WIDTH/3f, MyGame.HEIGHT, 1);
            shapeRenderer.rectLine(2 * MyGame.WIDTH/3f, 0, 2 * MyGame.WIDTH/3f, MyGame.HEIGHT, 1);
            shapeRenderer.end();
        game.batch.end();
    }

    private void drawPlayerName(){
        game.batch.begin();
            GlyphLayout layout = new GlyphLayout(game.font, String.format("Player number %d", 1));
            float fontX = MyGame.WIDTH / 6f - layout.width / 2f;
            float fontY = MyGame.HEIGHT - 20;
            game.font.draw(game.batch, layout, fontX, fontY);
                layout.setText(game.font, String.format("Player number %d", 2));
            game.font.draw(game.batch, layout, fontX + MyGame.WIDTH / 3f, fontY);
                layout.setText(game.font, String.format("Player number %d", 3));
            game.font.draw(game.batch, layout, fontX +  2 * MyGame.WIDTH / 3f, fontY);
        game.batch.end();
    }

    private void drawEveryPlayerStatistics(){
        game.batch.begin();
            float fontY = MyGame.HEIGHT - 360;
            for(int i = 0; i < 3; i++){
                game.font.draw(game.batch, String.format("Gold earned: %d", game.players.get(i).totalGoldEarned), 20 + i * MyGame.WIDTH / 3f, fontY);
                game.font.draw(game.batch, String.format("Gold spent: %d", game.players.get(i).totalGoldSpent), 20 + i * MyGame.WIDTH / 3f, fontY - 100);
                game.font.draw(game.batch, String.format("Units bought: %d", game.players.get(i).unitsBought), 20 + i * MyGame.WIDTH / 3f, fontY - 200);
                game.font.draw(game.batch, String.format("Units defeated: %d", game.players.get(i).unitsDefeated), 20 + i * MyGame.WIDTH / 3f, fontY - 300);
                game.font.draw(game.batch, String.format("Units lost: %d", game.players.get(i).unitsLost), 20 + i * MyGame.WIDTH / 3f, fontY - 400);
            }

        game.batch.end();
    }
    private void drawEveryPlayerUnits(){
        float UNIT_Y = MyGame.HEIGHT - 380;
        game.batch.begin();
            GlyphLayout layout = new GlyphLayout(game.endgameFont, "Game statistics");
            float fontX = MyGame.WIDTH / 6f - layout.width / 2f;
            float fontY = MyGame.HEIGHT - 150;
            game.endgameFont.draw(game.batch, layout, fontX, fontY);
            game.endgameFont.draw(game.batch, layout, fontX + MyGame.WIDTH / 3f, fontY);
            game.endgameFont.draw(game.batch, layout, fontX + 2 * MyGame.WIDTH / 3f, fontY);

            for(int i = 0; i < 3; i++){
                stage.getBatch().draw(archerTexture,  i * MyGame.WIDTH / 3f, UNIT_Y, 200, 200);
                stage.getBatch().draw(knightTexture,  220 + i * MyGame.WIDTH / 3f, UNIT_Y, 200, 200);
                stage.getBatch().draw(warriorTexture, 400 + i * MyGame.WIDTH / 3f, UNIT_Y, 200, 200);
            }
        game.batch.end();
    }
    private void drawPlayerPlaces(){
        String information = "WINNER";
        game.batch.begin();
            GlyphLayout layout = new GlyphLayout();
            for(int i = 0; i < 3; i++){
//                if(game.players.get(i).place == 1){
                    information = "WINNER";
//                }
//                else if(game.players.get(i).place == 2){
//                    information = "SECOND PLACE";
//                }
//                else if(game.players.get(i).place == 3){
//                    information = "THIRD PLACE";
//                }
                layout.setText(game.endGameWinnerFont, information);
                float fontX = MyGame.WIDTH / 6f - layout.width / 2f + i * MyGame.WIDTH / 3f;
                float fontY = MyGame.HEIGHT - 80;
                game.endGameWinnerFont.draw(game.batch, layout, fontX, fontY);
            }
        game.batch.end();
    }

    private void initButtons(){

    }

    private void update(float delta){
        stage.act(delta);
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
        stage.dispose();
        shapeRenderer.dispose();
    }
}
