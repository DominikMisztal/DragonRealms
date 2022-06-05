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
import com.mygdx.dragonrealms.units.*;

import java.util.Vector;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class HelpScreen implements Screen, InputProcessor {

    final MyGame game;
    private Skin skin;
    private Stage stage;
    private ShapeRenderer shapeRenderer;
    private Texture texture;
    private Texture goldmineTexture;
    private Texture archerTexture;
    private Texture knightTexture;
    private Texture warriorTexture;
    private String nextPageText;
    private Vector<Unit> units;
    private GoldMine goldMine;
    private int page;

    public HelpScreen(final MyGame game) {
        this.game = game;
        this.texture = new Texture(Gdx.files.internal("menuHelp1.png"));
        this.stage = new Stage(new FillViewport(MyGame.WIDTH, MyGame.HEIGHT, game.camera), game.batch);
        this.shapeRenderer = new ShapeRenderer();
        goldmineTexture = new Texture(Gdx.files.internal("textures/buildings/goldmine.png"));
        archerTexture = new Texture(Gdx.files.internal("textures/archer/archer1.png"));
        knightTexture = new Texture(Gdx.files.internal("textures/knight/knight1.png"));
        warriorTexture = new Texture(Gdx.files.internal("textures/warrior/warrior1.png"));
        units = new Vector<>();
        units.add(new Archer());
        units.add(new Warrior());
        units.add(new Knight());
        goldMine = new GoldMine();
        page = 1;
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

        if(page == 1){
            displayPage1();
        }
        if(page == 2){
            displayPage2();
        }
        if(page == 3){
            displayPage3();
        }

        update(delta);
        stage.draw();
    }

    private void displayPage1(){
        drawUnitsInfo();
        drawCommandsHelp();
    }
    private void displayPage2(){
        game.batch.begin();
            GlyphLayout layout = new GlyphLayout(game.helpFont1, "Earning money");
            float fontX = 300;
            float fontY = MyGame.HEIGHT - 200;
            game.helpFont1.draw(game.batch, layout, fontX, fontY + 80);
            GlyphLayout layout1 = new GlyphLayout(game.helpFont2,
                    "Main goal in the game is to defeat other\n" +
                    "opponents. The only way to attack opponents\n" +
                    "is to buy attacking units. You can obtain\n" +
                    "them by gold. In this case you are going to\n" +
                    "earn gold. Your starting gold income per round\n" +
                    "is 10 gold. You can increase this amount by\n" +
                    "buying goldmines. Be careful, other\n" +
                    "opponents can destroy your goldmines.\n" +
                    "that's why you need to protect them at all costs.\n" +
                    "Beside there is description about goldmines.\n");
            game.helpFont2.draw(game.batch, layout1, fontX - 200, fontY - 100);
        game.batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.rectLine(fontX + layout1.width - 50, 50, fontX + layout1.width - 50, MyGame.HEIGHT - 50, 1);
        shapeRenderer.rectLine(50, MyGame.HEIGHT - 250, fontX + layout1.width + 400, MyGame.HEIGHT - 250, 1);
        shapeRenderer.end();

        game.batch.begin();
            game.batch.draw(goldmineTexture, MyGame.WIDTH / 2f + 130, fontY + 10, 100, 100);
            layout1.setText(game.helpFont2, String.format("Unit cost: %d", goldMine.getUnitCost()));
            game.helpFont2.draw(game.batch, layout1, MyGame.WIDTH / 2f + 30, fontY - 90);

            layout1.setText(game.helpFont2, String.format("Attack: %d", goldMine.getAttack()));
            game.helpFont2.draw(game.batch, layout1, MyGame.WIDTH / 2f + 30, fontY - 190);

            layout1.setText(game.helpFont2, String.format("Hp: %d", goldMine.getMax_hp()));
            game.helpFont2.draw(game.batch, layout1, MyGame.WIDTH / 2f + 30, fontY - 290);

            layout1.setText(game.helpFont2, String.format("Attack range: %d", goldMine.getAttack()));
            game.helpFont2.draw(game.batch, layout1, MyGame.WIDTH / 2f + 30, fontY - 390);

            layout1.setText(game.helpFont2, String.format("Movement range: %d", goldMine.getMaxMovement()));
            game.helpFont2.draw(game.batch, layout1, MyGame.WIDTH / 2f + 30, fontY - 490);

            layout1.setText(game.helpFont2, String.format("Passive money: %d", goldMine.getPassiveMoney()));
            game.helpFont2.draw(game.batch, layout1, MyGame.WIDTH / 2f + 30, fontY - 590);
        game.batch.end();
    }
    private void displayPage3(){
        drawHelpGameRules();
    }

    private void drawHelpGameRules(){
        game.batch.begin();
            GlyphLayout layout = new GlyphLayout(game.helpFont1, "Game rules");
            float fontX = MyGame.WIDTH / 2f - layout.width / 2f;
            float fontY = MyGame.HEIGHT - 300;
            game.helpFont1.draw(game.batch, layout, fontX, fontY);

            GlyphLayout layout1 = new GlyphLayout(game.helpFont2, "The main goal of the game is to defeat your opponents.\n" +
                    "The winner is the person whose castle survives\nthe longest on the battlefield. " +
                    "Each player can make\na certain number of moves during one round, depending\non the units he has. " +
                    "The main currency is gold, which\nwe can get from gold mines. " +
                    "You can use gold to buy\nunits that can be used to attack your opponents.");
            fontX = MyGame.WIDTH / 3f - 50;
            fontY = MyGame.HEIGHT - 400;
            game.helpFont2.draw(game.batch, layout1, fontX, fontY);
        game.batch.end();
    }

    private void drawUnitsInfo(){
        game.batch.begin();
            GlyphLayout layout = new GlyphLayout(game.helpFont1, "Available attacking units");
            float fontX = MyGame.WIDTH / 4f - layout.width / 2f;
            float fontY = MyGame.HEIGHT - 100;
            game.helpFont1.draw(game.batch, layout, fontX + 80, fontY);
        game.batch.end();

        fontY = MyGame.HEIGHT - 150;
        game.batch.begin();
            game.batch.draw(archerTexture, fontX / 4f + 20, fontY - 200, 200, 200);
            game.batch.draw(knightTexture, fontX / 4f + fontX + 100, fontY - 200, 200, 200);
            game.batch.draw(warriorTexture, fontX / 4f + 2 * fontX + 150, fontY - 200, 200, 200);
        game.batch.end();

        game.batch.begin();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.rectLine(fontX + 70, fontY - 200, fontX + 70, fontY - 700, 1);
            shapeRenderer.rectLine((fontX + 70) * 2, fontY - 200, (fontX + 70) * 2, fontY - 700, 1);
            shapeRenderer.end();
        game.batch.end();

        game.batch.begin();
        for(int i = 0; i < 3; i++){
            layout.setText(game.helpFont2, String.format("Unit cost: %d", units.get(i).getUnitCost()));
            game.helpFont2.draw(game.batch, layout, 50 + i * (fontX + 60), fontY - 230);

            layout.setText(game.helpFont2, String.format("Attack: %d", units.get(i).getAttack()));
            game.helpFont2.draw(game.batch, layout, 50 + i * (fontX + 60), fontY - 330);

            layout.setText(game.helpFont2, String.format("Hp: %d", units.get(i).getMax_hp()));
            game.helpFont2.draw(game.batch, layout, 50 + i * (fontX + 60), fontY - 430);

            layout.setText(game.helpFont2, String.format("Attack range: %d", units.get(i).getAttack()));
            game.helpFont2.draw(game.batch, layout, 50 + i * (fontX + 60), fontY - 530);

            layout.setText(game.helpFont2, String.format("Movement range: %d", units.get(i).getMaxMovement()));
            game.helpFont2.draw(game.batch, layout, 50 + i * (fontX + 60), fontY - 630);
        }
        game.batch.end();
    }

    private void drawCommandsHelp(){
        game.batch.begin();
            GlyphLayout layout = new GlyphLayout(game.helpFont1, "All commands");
            float fontX = MyGame.WIDTH * 2/3f - layout.width / 2f + 40;
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
        final int BUTTON_WIDTH = 300;
        int BUTTON_HEIGHT = 100;
        final float BUTTON_X = MyGame.WIDTH - 310f;
        float BUTTON_Y = MyGame.HEIGHT - 110f;

        TextButton exitButton = new TextButton("Exit", skin, "default");
        exitButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        exitButton.setPosition(BUTTON_X, BUTTON_Y);
        exitButton.addAction(parallel(fadeIn(.5f),
                moveBy(0,-20,.5f, Interpolation.pow5Out)));
        exitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.soundController.playClick();
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
                game.soundController.playClick();
                if(page == 1){
                    page = 2;
                    nextPage.setText("Back / Next");
                }
                else if(page == 2){
                    if(Gdx.input.getX() > BUTTON_X + BUTTON_WIDTH / 2f){
                        page = 3;
                        nextPage.setText("Back");
                    }
                    else{
                        page = 1;
                        nextPage.setText("Next");
                    }
                }
                else if(page == 3){
                    page = 2;
                    nextPage.setText("Back / Next");
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
                game.soundController.playClick();
                page = 1;
                nextPage.setText("Next");
                game.screenManager.setScreen(ScreenManager.STATE.SETTINGS);
            }
        });

        stage.addActor(exitButton);
        stage.addActor(nextPage);
        stage.addActor(mainMenu);
    }

}