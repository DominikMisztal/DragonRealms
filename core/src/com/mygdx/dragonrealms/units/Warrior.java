package com.mygdx.dragonrealms.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.dragonrealms.units.Unit;
import com.mygdx.dragonrealms.units.UnitType;

import java.awt.image.BufferedImage;

public class Warrior extends Unit {

    public Warrior(int x, int y) {
        super("Warrior", new Texture(Gdx.files.internal("textures/knight.png")) , 4, 3, 6, 1, x, y);
    }
}
