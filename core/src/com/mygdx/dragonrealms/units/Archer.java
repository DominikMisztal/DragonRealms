package com.mygdx.dragonrealms.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Archer extends Unit {

    public Archer(int x, int y) {
        super("Archer", new Texture(Gdx.files.internal("textures/archer/archer3.png")) , 4, 3, 6, 2, x, y);
    }
}
