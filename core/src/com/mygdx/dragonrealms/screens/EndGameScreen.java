package com.mygdx.dragonrealms.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.dragonrealms.MyGame;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class EndGameScreen implements Screen {
    private MyGame game;
    private Stage stage;
    private Skin skin;
    private ShapeRenderer shapeRenderer;

    public EndGameScreen(MyGame game){
        this.game = game;
        this.stage = new Stage(new FitViewport(MyGame.WIDTH, MyGame.HEIGHT, game.camera), game.batch);
        this.shapeRenderer = new ShapeRenderer();
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
                game.font.draw(game.batch, String.format("Gold spent: %d", game.players.get(i).totalGoldSpent), 20 + i * MyGame.WIDTH / 3f, fontY - 90);
                game.font.draw(game.batch, String.format("Gold mines bought: %d", game.players.get(i).goldMinesCount), 20 + i * MyGame.WIDTH / 3f, fontY - 180);
                game.font.draw(game.batch, String.format("Units bought: %d", game.players.get(i).unitsBought), 20 + i * MyGame.WIDTH / 3f, fontY - 270);
                game.font.draw(game.batch, String.format("Units defeated: %d", game.players.get(i).unitsDefeated), 20 + i * MyGame.WIDTH / 3f, fontY - 360);
                game.font.draw(game.batch, String.format("Units lost: %d", game.players.get(i).unitsLost), 20 + i * MyGame.WIDTH / 3f, fontY - 450);
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

            float knightY = 0;
            float knightX = 0;
            for(int i = 0; i < 3; i++){
                if(i == 1){
                    knightY = 30; knightX = -20;
                }
                if(i == 2){
                    knightY = 40;
                }
                Texture archerTexture = new Texture(Gdx.files.internal("textures/Player "+(i + 1)+"/archer/archer1.png"));
                Texture knightTexture = new Texture(Gdx.files.internal("textures/Player "+(i + 1)+"/knight/knight1.png"));
                Texture warriorTexture = new Texture(Gdx.files.internal("textures/Player "+(i + 1)+"/warrior/warrior1.png"));
                stage.getBatch().draw(archerTexture,  i * MyGame.WIDTH / 3f, UNIT_Y, 200, 200);
                stage.getBatch().draw(knightTexture,  220 + i * MyGame.WIDTH / 3f + knightX, UNIT_Y + knightY, 200, 200);
                stage.getBatch().draw(warriorTexture, 400 + i * MyGame.WIDTH / 3f, UNIT_Y, 200, 200);
            }
        game.batch.end();
    }
    private void drawPlayerPlaces(){
        String information = "WINNER";
        game.batch.begin();
            GlyphLayout layout = new GlyphLayout();
            for(int i = 0; i < 3; i++){
                if(game.players.get(i).place == 1){
                    information = "WINNER";
                }
                else if(game.players.get(i).place == 2){
                    information = "2nd PLACE";
                }
                else if(game.players.get(i).place == 3){
                    information = "3rd PLACE";
                }
                layout.setText(game.endGameWinnerFont, information);
                float fontX = MyGame.WIDTH / 6f - layout.width / 2f + i * MyGame.WIDTH / 3f;
                float fontY = MyGame.HEIGHT - 80;
                game.endGameWinnerFont.draw(game.batch, layout, fontX, fontY);
            }
        game.batch.end();
    }

    private void initButtons(){
        TextButton restartButton = new TextButton("Restart game", skin);
        restartButton.setSize(180,50);
        restartButton.setPosition(MyGame.WIDTH - 190, MyGame.HEIGHT - 60);
        restartButton.addAction(parallel(fadeIn(.5f),
                moveBy(0,-20,.5f, Interpolation.pow5Out)));
        restartButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.soundController.playClick();
                game.setScreen(new MainGameScreen(game));
            }
        });

        TextButton exitButton = new TextButton("Exit game", skin);
        exitButton.setSize(180,50);
        exitButton.setPosition(MyGame.WIDTH - 190, MyGame.HEIGHT - 120);
        exitButton.addAction(parallel(fadeIn(.5f),
                moveBy(0,-20,.5f, Interpolation.pow5Out)));
        exitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.soundController.playClick();
                Gdx.app.exit();
            }
        });

        stage.addActor(restartButton);
        stage.addActor(exitButton);
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
