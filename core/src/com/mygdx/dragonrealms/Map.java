package com.mygdx.dragonrealms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.sun.org.apache.bcel.internal.generic.BREAKPOINT;

import java.io.*;
import java.util.HashMap;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Map {
    TiledMap tiledMap;
    Texture img;
    TiledMapRenderer tiledMapRenderer;
    TiledMapTileLayer tiledMapTileLayer;
    HashMap<Vector2, Tile> tilesHashMap;

    public Map(String map_file){
        tiledMap = new TmxMapLoader().load(map_file);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        createTilesMap();
    }

    private void createTilesMap(){
        try{
            tilesHashMap = new HashMap<>();
            FileHandle fileHandle = new FileHandle("assets/maps/map1.txt");
            BufferedReader reader = fileHandle.reader(1000);
            for(int i = 0; i < 10; i++){

                    String typeLine;
                    typeLine = reader.readLine();
                    String[] typeArray = typeLine.split(",");
                    int j = 0;
                    for (String type: typeArray) {
                        Tile tile = new Tile();
                        tile.coordinates.x = i;
                        switch (type){
                            case "2":
                                tile.type = TileType.GRASS;
                                break;
                            case "1":
                                tile.type = TileType.SAND;
                                break;
                            case "4":
                                tile.type = TileType.MOUNTAIN;
                                break;
                            case "3":
                                tile.type = TileType.WATER;
                                break;
                        }
                        tile.coordinates.y = j;
                        tilesHashMap.put(new Vector2(j,i), tile);
                        j++;
                    }


            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return;
    }

    public void getTile(Vector2 coordinates){
        Tile tile = tilesHashMap.get(coordinates);
        switch (tile.type){
            case SAND:
                System.out.println("Tile is sand");
                break;
            case GRASS:
                System.out.println("Tile is grass");
                break;
            case WATER:
                System.out.println("Tile is water");
                break;
            case MOUNTAIN:
                System.out.println("Tile is mountain");
                break;
        }
    }

    public void render(OrthographicCamera camera){
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
    }

    public Vector2 convertCoordinates(Vector3 coordinates){
        Vector2 vector = new Vector2();
        vector.x = (float) Math.floor(coordinates.x/16);
        vector.y = (float) Math.floor(coordinates.y/16);

        return vector;
    }
}
