package com.mygdx.dragonrealms;

import com.mygdx.dragonrealms.map.Map;
import com.mygdx.dragonrealms.units.Unit;

import java.util.Vector;

public class Player {
    private final String playerName;
    private final Vector<Unit> ownedUnits;
    public Unit castle;
    public int gold;
    public int goldMinesCount;
    private int moveRange;
    public int unitsDefeated;
    public int unitsBought;
    public int unitsLost;
    public int totalGoldEarned;
    public int totalGoldSpent;
    public boolean isWinner;
    public int place;

    public Player(String name){

        this.playerName = name;
        this.ownedUnits = new Vector<>();
        gold = 10;
        goldMinesCount = 0;
        unitsDefeated = 0;
        unitsBought = 0;
        unitsLost = 0;
        totalGoldEarned = 10;
        totalGoldSpent = 0;
    }

    public void addGold(){
        gold += 10  + 5*goldMinesCount;
        totalGoldEarned += 10  + 5*goldMinesCount;
    }

    public String getPlayerName(){
        return this.playerName;
    }

    public void addUnit(Unit unit){
        ownedUnits.add(unit);
    }

    public void removeUnit(Unit unit){
        ownedUnits.remove(unit);
        unitsLost++;
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

    public void removePlayer(Map map){
        for(Unit unit : ownedUnits){
            map.getTile(unit.getCoordinates()).setUnit(null);
        }
        ownedUnits.clear();
    }
}
