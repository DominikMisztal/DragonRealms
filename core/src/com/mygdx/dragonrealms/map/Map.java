package com.mygdx.dragonrealms.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.dragonrealms.screens.MainGameScreen;

import java.io.*;
import java.util.HashMap;

public class Map extends Stage {
    private TiledMap tiledMap;
    public final static int TILESIZE = 64;
    private TiledMapRenderer tiledMapRenderer;
    private HashMap<Vector2, Tile> tilesHashMap;
    private boolean borders;
    private ShapeRenderer sRenderer;
    private MainGameScreen mainGameScreen;


    public Map(String map_file, MainGameScreen mgs){
        mainGameScreen = mgs;
        tiledMap = new TmxMapLoader().load(map_file);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        createTilesMap();
        borders = false;
        sRenderer = new ShapeRenderer();
    }

    private void createTilesMap(){
        try{
            tilesHashMap = new HashMap<>();
            FileHandle fileHandle = new FileHandle("assets/maps/map_test/map_alpha_tiles.txt");
            BufferedReader reader = fileHandle.reader(4096);
            for(int i = 0; i < 32; i++){
                    String typeLine;
                    typeLine = reader.readLine();
                    String[] typeArray = typeLine.split(",");
                    int j = 0;
                    for (String type: typeArray) {
                        Tile tile = new Tile();
                        switch (type){
                            case "1":
                                tile.setType(TileType.SAND);
                                tile.setTexture(new Texture(Gdx.files.internal("textures/tiles/SAND.png")));
                                tile.setMovementCost(1);
                                break;
                            case "2":
                                tile.setType(TileType.GRASS);
                                tile.setTexture(new Texture(Gdx.files.internal("textures/tiles/GRASS.png")));
                                tile.setMovementCost(1);
                                break;
                            case "3":
                                tile.setType(TileType.WATER);
                                tile.setTexture(new Texture(Gdx.files.internal("textures/tiles/WATER.png")));
                                tile.setMovementCost(99);
                                break;
                            case "4":
                                tile.setType(TileType.MOUNTAIN);
                                tile.setTexture(new Texture(Gdx.files.internal("textures/tiles/MOUNTAIN.png")));
                                tile.setMovementCost(99);
                                break;
                            case "5":
                                tile.setType(TileType.FOREST);
                                tile.setTexture(new Texture(Gdx.files.internal("textures/tiles/FOREST.png")));
                                tile.setMovementCost(3);
                                break;
                            case "6":
                                tile.setType(TileType.SNOW);
                                tile.setTexture(new Texture(Gdx.files.internal("textures/tiles/SNOW.png")));
                                tile.setMovementCost(4);
                                break;
                            case "7":
                                tile.setType(TileType.SHALLOW_WATER);
                                tile.setTexture(new Texture(Gdx.files.internal("textures/tiles/SHALLOW_WATER.png")));
                                tile.setMovementCost(2);
                                break;
                        }
                        tile.coordinates.x = j;
                        tile.coordinates.y = i;
                        tile.setBounds(TILESIZE*j, TILESIZE *  i, TILESIZE, TILESIZE);
                        addActor(tile);
                        EventListener eventListener = new TileClickListener(tile, mainGameScreen);
                        tile.addListener(eventListener);
                        tilesHashMap.put(new Vector2(j,i), tile);
                        j++;
                    }


            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return;
    }

    public Tile getTile(Vector2 coordinates){
        Tile tile = tilesHashMap.get(coordinates);
        return tile;
    }

    public Tile getTile(int x, int y){
        Tile tile = tilesHashMap.get(new Vector2(x, y));
        return tile;
    }


    public int getMapWidth(){
        TiledMapTileLayer layer = (TiledMapTileLayer)tiledMap.getLayers().get("layer1");
        return layer.getWidth() * TILESIZE;
    }

    public int getMapHeight(){
        TiledMapTileLayer layer = (TiledMapTileLayer)tiledMap.getLayers().get("layer1");
        return layer.getHeight() * TILESIZE;
    }

    public void render(OrthographicCamera camera){
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
        sRenderer.setProjectionMatrix(camera.combined);
        if(borders == true){
            for(int i = 0; i< 32; i++){
                for (int j = 0; j < 32; j++){
                    sRenderer.begin(ShapeType.Line);
                    sRenderer.identity();
                    sRenderer.setColor(Color.BLACK);
                    sRenderer.rect(i * TILESIZE , j * TILESIZE , TILESIZE, TILESIZE );
                    sRenderer.end();
                }
            }
        }
    }

    public Vector2 convertCoordinates(Vector3 coordinates){
        Vector2 vector = new Vector2();
        vector.x = (float) Math.floor(coordinates.x/TILESIZE);
        vector.y = (float) Math.floor(coordinates.y/TILESIZE);

        return vector;
    }

    public void bordersOnOff(){
        borders = !borders;
    }
}
