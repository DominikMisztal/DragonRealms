package com.mygdx.dragonrealms.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.dragonrealms.Player;

public class Knight extends Unit {

    public Knight(int x, int y, Player player) {
        super("Knight", new Texture(Gdx.files.internal("textures/knight/knight2.png")) , 4, 3, 6, 1, 6, player, x, y);
    }
}
