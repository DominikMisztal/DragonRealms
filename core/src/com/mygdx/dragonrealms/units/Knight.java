package com.mygdx.dragonrealms.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.dragonrealms.Player;
import com.mygdx.dragonrealms.map.Tile;

public class Knight extends Unit {

    public Knight(Tile tile, Player player) {
        super("Knight", new Texture(Gdx.files.internal("textures/knight/knight2.png")) , 4, 3, 6, 1, 6, player, (int)tile.getCoordinates().x, (int)tile.getCoordinates().y);
    }
}
