package com.mygdx.dragonrealms.units;

import java.awt.image.BufferedImage;

public class Unit {

    private final String unitName;
    private final UnitType unitType;
    private final BufferedImage bufferedImage;
    private final int unitCost;
    private final int attack;
    private final int hp;
    private final int range;



    public Unit(String unitName, UnitType unitType, BufferedImage bufferedImage, int unitCost, int attack, int hp, int range){
        this.unitName = unitName;
        this.unitType = unitType;
        this.bufferedImage = bufferedImage;
        this.unitCost = unitCost;
        this.attack = attack;
        this.hp = hp;
        this.range = range;
    }

    public String getUnitName(){
        return this.unitName;
    }

    public UnitType getUnitType(){
        return this.unitType;
    }

    public BufferedImage getBufferedImage(){
        return this.bufferedImage;
    }

    public int getUnitCost(){
        return this.unitCost;
    }

    public int getAttack(){
        return this.attack;
    }

    public int getHp(){
        return this.hp;
    }

    public int getRange(){
        return this.range;
    }

}
