package com.mygdx.dragonrealms.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.dragonrealms.Player;

public class Warrior extends Unit {

    public Warrior(int x, int y, Player player) {
        super("Warrior", new Texture(Gdx.files.internal("textures/warrior/warrior3.png")) , 4, 3, 6, 1, 5, player, x, y);
    }
}
