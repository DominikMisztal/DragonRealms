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
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.mygdx.dragonrealms.MyGame;
import com.mygdx.dragonrealms.units.Archer;
import com.mygdx.dragonrealms.units.Knight;
import com.mygdx.dragonrealms.units.Unit;
import com.mygdx.dragonrealms.units.Warrior;

import java.util.Vector;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class HelpScreen implements Screen, InputProcessor {

    final MyGame game;
    private Skin skin;
    private Stage stage;
    private ShapeRenderer shapeRenderer;
    private Texture texture;
    private Texture texture1;
    private Texture currentTexture;

    private Texture archerTexture;
    private Texture knightTexture;
    private Texture warriorTexture;
    private String nextPageText;
    private Vector<Unit> units;

    public HelpScreen(final MyGame game) {
        this.game = game;
        this.texture = new Texture(Gdx.files.internal("menuHelp.png"));
        this.texture1 = new Texture(Gdx.files.internal("menuHelp1.png"));
        currentTexture = texture;
        this.stage = new Stage(new FillViewport(MyGame.WIDTH, MyGame.HEIGHT, game.camera), game.batch);
        this.shapeRenderer = new ShapeRenderer();
        archerTexture = new Texture(Gdx.files.internal("textures/archer/archer1.png"));
        knightTexture = new Texture(Gdx.files.internal("textures/knight/knight1.png"));
        warriorTexture = new Texture(Gdx.files.internal("textures/warrior/warrior1.png"));
        units = new Vector<>();
        units.add(new Archer());
        units.add(new Warrior());
        units.add(new Knight());
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
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
        Gdx.gl.glClearColor(0,0,0,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        drawUnitsInfo();
        drawCommandsHelp();

        update(delta);
        stage.draw();
    }

    private void drawUnitsInfo(){
        game.batch.begin();
            GlyphLayout layout = new GlyphLayout(game.helpFont1, "Available units");
            float fontX = MyGame.WIDTH / 4f - layout.width / 2f;
            float fontY = MyGame.HEIGHT - 100;
            game.helpFont1.draw(game.batch, layout, fontX, fontY);
        game.batch.end();

        fontY = MyGame.HEIGHT - 150;
        game.batch.begin();
            game.batch.draw(archerTexture, fontX / 4f, fontY - 200, 200, 200);
            game.batch.draw(knightTexture, fontX / 4f + fontX, fontY - 200, 200, 200);
            game.batch.draw(warriorTexture, fontX / 4f + 2 * fontX - 30, fontY - 200, 200, 200);
        game.batch.end();

        game.batch.begin();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.rectLine(fontX, fontY - 200, fontX, fontY - 700, 1);
            shapeRenderer.rectLine(fontX * 2, fontY - 200, fontX * 2, fontY - 700, 1);
            shapeRenderer.end();
        game.batch.end();

        game.batch.begin();
        for(int i = 0; i < 3; i++){
            layout.setText(game.helpFont2, String.format("Unit cost: %d", units.get(i).getUnitCost()));
            game.helpFont2.draw(game.batch, layout, i * fontX + 30, fontY - 230);

            layout.setText(game.helpFont2, String.format("Attack: %d", units.get(i).getAttack()));
            game.helpFont2.draw(game.batch, layout, i * fontX + 30, fontY - 330);

            layout.setText(game.helpFont2, String.format("Hp: %d", units.get(i).getMax_hp()));
            game.helpFont2.draw(game.batch, layout, i * fontX + 30, fontY - 430);

            layout.setText(game.helpFont2, String.format("Attack range: %d", units.get(i).getAttack()));
            game.helpFont2.draw(game.batch, layout, i * fontX + 30, fontY - 530);

            layout.setText(game.helpFont2, String.format("Movement range: %d", units.get(i).getMaxMovement()));
            game.helpFont2.draw(game.batch, layout, i * fontX + 30, fontY - 630);
        }
        game.batch.end();
    }

    private void drawCommandsHelp(){
        game.batch.begin();
            GlyphLayout layout = new GlyphLayout(game.helpFont1, "All commands");
            float fontX = MyGame.WIDTH * 2/3f - layout.width / 2f;
            float fontY = MyGame.HEIGHT - 100;
            game.helpFont1.draw(game.batch, layout, fontX, fontY);

            fontX -= 80;
            GlyphLayout layout1 = new GlyphLayout(game.helpFont2, "");
            layout1.setText(game.helpFont2, "I - Spawn Warrior");
            game.helpFont2.draw(game.batch, layout1, fontX, fontY - 100);
            layout1.setText(game.helpFont2, "O - spawn Archer");
            game.helpFont2.draw(game.batch, layout1, fontX, fontY - 200);
            layout1.setText(game.helpFont2, "P - spawn Knight");
            game.helpFont2.draw(game.batch, layout1, fontX, fontY - 300);
            layout1.setText(game.helpFont2, "G - spawn gold mine");
            game.helpFont2.draw(game.batch, layout1, fontX, fontY - 400);
            layout1.setText(game.helpFont2, "H - enable / disable health bars");
            game.helpFont2.draw(game.batch, layout1, fontX, fontY - 500);
            layout1.setText(game.helpFont2, "L - enable / disable grid lines");
            game.helpFont2.draw(game.batch, layout1, fontX, fontY - 600);
            layout1.setText(game.helpFont2, "SPACE - next player");
            game.helpFont2.draw(game.batch, layout1, fontX, fontY - 700);
        game.batch.end();

        game.batch.begin();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.rectLine(fontX - 20, 50, fontX - 20, MyGame.HEIGHT - 90, 1f);
        shapeRenderer.end();
        game.batch.end();
    }


    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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

    @Override
    public boolean keyDown(int keycode) {
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

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    private void initButtons(){
        int BUTTON_WIDTH = 300;
        int BUTTON_HEIGHT = 100;
        float BUTTON_X = MyGame.WIDTH - 310f;
        float BUTTON_Y = MyGame.HEIGHT - 110f;

        TextButton exitButton = new TextButton("Exit", skin, "default");
        exitButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        exitButton.setPosition(BUTTON_X, BUTTON_Y);
        exitButton.addAction(parallel(fadeIn(.5f),
                moveBy(0,-20,.5f, Interpolation.pow5Out)));
        exitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                Gdx.app.exit();
            }
        });

        final TextButton nextPage = new TextButton("Next", skin, "default");
        nextPage.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        nextPage.setPosition(BUTTON_X, BUTTON_Y - 110);
        nextPage.addAction(parallel(fadeIn(.5f),
                moveBy(0,-20,.5f, Interpolation.pow5Out)));
        nextPage.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                if(currentTexture == texture){
                    currentTexture = texture1;
                    nextPage.setText("Back");
                }
                else{
                    currentTexture = texture;
                    nextPage.setText("Next");
                }

            }
        });

        TextButton mainMenu = new TextButton("Back to settings", skin, "default");
        mainMenu.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        mainMenu.setPosition(BUTTON_X, BUTTON_Y - 220);
        mainMenu.addAction(parallel(fadeIn(.5f),
                moveBy(0,-20,.5f, Interpolation.pow5Out)));
        mainMenu.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                currentTexture = texture;
                nextPage.setText("Next");
                game.screenManager.setScreen(ScreenManager.STATE.SETTINGS);
            }
        });

        stage.addActor(exitButton);
        stage.addActor(nextPage);
        stage.addActor(mainMenu);
    }

}