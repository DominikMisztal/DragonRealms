package com.mygdx.dragonrealms.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.dragonrealms.Player;

public class Archer extends Unit {

    public Archer(int x, int y, Player player) {
        super("Archer", new Texture(Gdx.files.internal("textures/archer/archer3.png")) , 4, 3, 6, 2, 4, player, x, y);
    }
}
