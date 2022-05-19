package com.mygdx.dragonrealms;

import java.awt.image.BufferedImage;

public class Warrior extends Unit{

    public Warrior(String unitName, UnitType unitType, BufferedImage bufferedImage, int unitCost, int attack, int hp, int range) {
        super(unitName, unitType, bufferedImage, unitCost, attack, hp, range);
    }
}
