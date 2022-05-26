package com.mygdx.dragonrealms.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Warrior extends Unit {

    public Warrior(int x, int y) {
        super("Warrior", new Texture(Gdx.files.internal("textures/warrior/warrior3.png")) , 4, 3, 6, 1, x, y);
    }
}
