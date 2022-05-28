package com.mygdx.dragonrealms.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.dragonrealms.units.Unit;

public class Tile extends Actor{

    private Unit unit;
    public Vector2 coordinates;
    private TileType type;
    private int movementCost;
    private Sprite tileBorder;

    public Tile(){
        coordinates = new Vector2();
    }

    public int getMovementCost() {
        return movementCost;
    }

    public void setMovementCost(int movementCost) {
        this.movementCost = movementCost;
    }

    public void setType(TileType type) {
        this.type = type;
    }

    public TileType getType() {
        return type;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public void setBorder(int type){
        if(type == 1){
            tileBorder = new Sprite(new Texture(Gdx.files.internal("assets/textures/tiles_borders/GreenFrame.png")));
            tileBorder.setPosition(Map.TILESIZE*coordinates.x, Map.TILESIZE*coordinates.y);
        }
    }

    public void render(SpriteBatch sb){
        tileBorder.draw(sb);
    }

    public Vector2 getCoordinates(){
        return coordinates;
    }
}
