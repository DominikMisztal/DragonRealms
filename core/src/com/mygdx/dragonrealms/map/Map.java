package com.mygdx.dragonrealms.map;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.io.*;
import java.util.HashMap;

public class Map {
    TiledMap tiledMap;
    public final static int TILESIZE = 64;
    TiledMapRenderer tiledMapRenderer;
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
                        switch (type){
                            case "2":
                                tile.setType(TileType.GRASS);
                                tile.setMovementCost(1);
                                break;
                            case "1":
                                tile.setType(TileType.SAND);
                                tile.setMovementCost(2);

                                break;
                            case "4":
                                tile.setType(TileType.MOUNTAIN);
                                tile.setMovementCost(99);
                                break;
                            case "3":
                                tile.setType(TileType.WATER);
                                tile.setMovementCost(99);
                                break;
                            case "5":
                                tile.setType(TileType.FOREST);
                                tile.setMovementCost(4);
                                break;
                            case "6":
                                tile.setType(TileType.SNOW);
                                tile.setMovementCost(5);
                                break;
                            case "7":
                                tile.setType(TileType.SHALLOW_WATER);
                                tile.setMovementCost(3);
                                break;
                        }
                        tile.coordinates.x = i;
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
            switch (tile.getType()){
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