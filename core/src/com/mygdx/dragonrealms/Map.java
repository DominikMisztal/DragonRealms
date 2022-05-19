package com.mygdx.dragonrealms;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.sun.org.apache.bcel.internal.generic.BREAKPOINT;

import java.util.HashMap;

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
        tilesHashMap = new HashMap<>();
        TiledMapTile tiledMapTile;
        MapLayers mapLayers = tiledMap.getLayers();
        tiledMapTileLayer = (TiledMapTileLayer) mapLayers.get("objects");
        int height, width;
        height = tiledMapTileLayer.getHeight();
        width = tiledMapTileLayer.getWidth();

        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                tiledMapTile = tiledMapTileLayer.getCell(i,j).getTile();
                Tile tile = new Tile();
                tile.coordinates.x = i;
                tile.coordinates.y = j;
                String type = (String) tiledMapTile.getProperties().get("type");
                switch (type){
                    case "grass":
                        tile.type = TileType.GRASS;
                        break;
                    case "sand":
                        tile.type = TileType.SAND;
                        break;
                    case "mountain":
                        tile.type = TileType.MOUNTAIN;
                        break;
                    case "water":
                        tile.type = TileType.MOUNTAIN;
                        break;
                }
                tilesHashMap.put(tile.coordinates, tile);
            }
        }
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
