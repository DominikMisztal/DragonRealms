package com.mygdx.dragonrealms;

import java.awt.*;
import java.util.HashMap;

public class Player {
    private final String playerName;
    private final HashMap<Point, Unit> ownedUnits;
    private int moveRange;
    boolean isWinner;

    public Player(String name, int moveRange, boolean isWinner){

        this.playerName = name;
        this.moveRange = moveRange;
        this.isWinner = isWinner;
        this.ownedUnits = new HashMap<>();
    }

    public String getPlayerName(){
        return this.playerName;
    }

    public void addUnit(Point coordinates, Unit unit){
        ownedUnits.put(coordinates, unit);
    }

    public void removeUnit(Point coordinates){
        ownedUnits.remove(coordinates);
    }

    public HashMap<Point, Unit> getUnits(){
        return this.ownedUnits;
    }

    public int getMoveRange(){
        return this.moveRange;
    }

    public void changeMoveRange(int moveRange){
        this.moveRange = moveRange;
    }
}
