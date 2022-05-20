package com.mygdx.dragonrealms;

import java.awt.image.BufferedImage;

public class Archer extends Unit {

    public Archer(String unitName, UnitType unitType, BufferedImage bufferedImage, int unitCost, int attack, int hp, int range) {
        super(unitName, unitType, bufferedImage, unitCost, attack, hp, range);
    }
}
