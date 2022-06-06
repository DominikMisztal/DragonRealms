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
    private Texture texture;
    public Vector2 coordinates;
    private TileType type;
    private int movementCost;
    private Sprite tileBorder;
    public int tempMovLeft;
    private int borderType;

    public Tile(){
        coordinates = new Vector2();
        tempMovLeft = 0;
    }
    public String getStatistics(){
        String out = String.format("%s\n%s",
                type.toString(), (movementCost == 99) ? "UNPASSABLE" : String.format("Move cost: %d\nPASSABLE", movementCost));

        return out;
    }
    public void setTexture(Texture texture){
        this.texture = texture;
    }
    public Texture getTexture(){
        return this.texture;
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

    public int getBorder(){
        return borderType;
    }

    public void clearBorder(){
        tileBorder = null;
        borderType = 0;
    }

    public void setBorder(int type){
        if(tileBorder == null){
            if(type == 0){
                tileBorder = null;
            }
            if(type == 1){
                tileBorder = new Sprite(new Texture(Gdx.files.internal("assets/textures/tiles_borders/GreenFrame.png")));
                tileBorder.setPosition(Map.TILESIZE*coordinates.x, Map.TILESIZE*coordinates.y);
            }
            if(type == 2){
                tileBorder = new Sprite(new Texture(Gdx.files.internal("assets/textures/tiles_borders/RedFrame.png")));
                tileBorder.setPosition(Map.TILESIZE*coordinates.x, Map.TILESIZE*coordinates.y);
            }
            if(type == 3){
                tileBorder = new Sprite(new Texture(Gdx.files.internal("assets/textures/tiles_borders/CrossFrame.png")));
                tileBorder.setPosition(Map.TILESIZE*coordinates.x, Map.TILESIZE*coordinates.y);
            }
            if(type == 4){
                tileBorder = new Sprite(new Texture(Gdx.files.internal("assets/textures/tiles_borders/WhiteFrame.png")));
                tileBorder.setPosition(Map.TILESIZE*coordinates.x, Map.TILESIZE*coordinates.y);
            }
        }
        borderType = type;
    }

    public void render(SpriteBatch sb){
        tileBorder.draw(sb);
    }

    public Vector2 getCoordinates(){
        return coordinates;
    }

    public int getTempMovLeft(){
        return tempMovLeft;
    }

    public void setTempMovLeft(int movement){
        tempMovLeft = movement;
    }
}
