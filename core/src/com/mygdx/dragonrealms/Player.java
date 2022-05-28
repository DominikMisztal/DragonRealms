package com.mygdx.dragonrealms;

import com.mygdx.dragonrealms.units.Unit;

import java.util.Vector;

public class Player {
    private final String playerName;
    private final Vector<Unit> ownedUnits;
    private int moveRange;
    boolean isWinner;

    public Player(String name){

        this.playerName = name;
        this.ownedUnits = new Vector<>();
    }

    public String getPlayerName(){
        return this.playerName;
    }

    public void addUnit(Unit unit){
        ownedUnits.add(unit);
    }

    public void removeUnit(Unit unit){
        ownedUnits.remove(unit);
    }

    public Vector<Unit> getUnits(){
        return this.ownedUnits;
    }

    public int getMoveRange(){
        return this.moveRange;
    }

    public void changeMoveRange(int moveRange){
        this.moveRange = moveRange;
    }
}
