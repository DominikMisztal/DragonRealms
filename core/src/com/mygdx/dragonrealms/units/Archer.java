package com.mygdx.dragonrealms.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.dragonrealms.Player;
import com.mygdx.dragonrealms.map.Tile;

public class Archer extends Unit {

    public Archer(Tile tile, Player player) {
        super("Archer", new Texture(Gdx.files.internal("textures/archer/archer3.png")) , 4, 3, 6, 2, 4, player, (int)tile.getCoordinates().x, (int)tile.getCoordinates().y);
    }
}
