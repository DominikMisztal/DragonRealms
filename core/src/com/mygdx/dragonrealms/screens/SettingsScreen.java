package com.mygdx.dragonrealms.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.dragonrealms.MyGame;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;

public class SettingsScreen implements Screen {
    private MyGame game;
    private Table table;
    private TextButton exitButton;
    private Stage stage;
    private ShapeRenderer shapeRenderer;
    private Skin skin;

    public SettingsScreen(MyGame game){
        this.game = game;
        this.table = new Table();
        this.stage = new Stage(new FitViewport(MyGame.WIDTH, MyGame.HEIGHT, game.camera));
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

    private void update(float delta){
        stage.act(delta);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor( 0, 0, 0, 0 );
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.rect(0,0,MyGame.WIDTH,MyGame.HEIGHT);
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.end();

        Table table = new Table();
        table.defaults().pad(10F);
        table.setFillParent(true);

        Label label = new Label("PLAYER NAME",skin);
        label.setAlignment(Align.center);

        Table first_table = new Table(skin);
        first_table.setDebug(true);
        Label firstLabel = new Label("FIRST TABLE",skin);
        firstLabel.setAlignment(Align.center);
        first_table.add(firstLabel);

        Table second_table = new Table(skin);
        Label secondLabel = new Label("SECOND TABLE",skin);
        secondLabel.setAlignment(Align.center);
        second_table.add(secondLabel);

        table.add(label).colspan(2).fillX();
        table.row();
        table.add(first_table).expand();
        table.add(second_table).expand();

        stage.addActor(table);
        stage.setDebugAll(true);

        update(delta);
        stage.draw();
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

    private void initButtons(){

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

        stage.addActor(exitButton);
    }
}
