package com.mygdx.dragonrealms.map;

import com.badlogic.gdx.math.Vector2;

public class Tile {
    public Vector2 coordinates;
    private TileType type;
    private int movementCost;
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
}
