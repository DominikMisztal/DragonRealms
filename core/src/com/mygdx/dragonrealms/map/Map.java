package com.mygdx.dragonrealms.map;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.io.*;
import java.util.HashMap;

public class Map {
    TiledMap tiledMap;
    final static int TILESIZE = 64;
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
            FileHandle fileHandle = new FileHandle("assets/maps/map_test/map_alpha_tiles.txt");
            BufferedReader reader = fileHandle.reader(4096);
            for(int i = 0; i < 32; i++){

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
                            case "5":
                                tile.type = TileType.FOREST;
                                break;
                            case "6":
                                tile.type = TileType.SNOW;
                                break;
                            case "7":
                                tile.type = TileType.SHALLOW_WATER;
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
        if(tile != null){
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
                case FOREST:
                    System.out.println("Tile is forest");
                    break;
                case SNOW:
                    System.out.println("Tile is snow");
                    break;
                case SHALLOW_WATER:
                    System.out.println("Tile is shallow water");
                    break;
            }
        }
    }

    public void render(OrthographicCamera camera){
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
    }

    public Vector2 convertCoordinates(Vector3 coordinates){
        Vector2 vector = new Vector2();
        vector.x = (float) Math.floor(coordinates.x/TILESIZE);
        vector.y = (float) Math.floor(coordinates.y/TILESIZE);

        return vector;
    }
}
