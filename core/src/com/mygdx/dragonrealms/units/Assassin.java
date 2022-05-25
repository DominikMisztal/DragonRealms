package com.mygdx.dragonrealms.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Assassin extends Unit {

    public Assassin(int x, int y) {
        super("Assassin", new Texture(Gdx.files.internal("textures/assassin.png")) , 4, 3, 6, 1, x, y);
    }
}
