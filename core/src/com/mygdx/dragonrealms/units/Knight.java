package com.mygdx.dragonrealms.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Knight extends Unit {

    public Knight(int x, int y) {
        super("Knight", new Texture(Gdx.files.internal("textures/knight/knight2.png")) , 4, 3, 6, 1, 6, null, x, y);
    }
}
