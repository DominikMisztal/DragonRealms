package com.mygdx.dragonrealms.units;

import com.mygdx.dragonrealms.units.Unit;
import com.mygdx.dragonrealms.units.UnitType;

import java.awt.image.BufferedImage;

public class Warrior extends Unit {

    public Warrior(String unitName, UnitType unitType, BufferedImage bufferedImage, int unitCost, int attack, int hp, int range) {
        super(unitName, unitType, bufferedImage, unitCost, attack, hp, range);
    }
}
