package com.mygdx.dragonrealms.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.dragonrealms.Player;
import com.mygdx.dragonrealms.map.Tile;

public class Warrior extends Unit {

    public Warrior(Tile tile, Player player) {
        super("Warrior", new Texture(Gdx.files.internal("textures/warrior/warrior3.png")) , 4, 3, 6, 1, 5, player, (int)tile.getCoordinates().x, (int)tile.getCoordinates().y);
    }
}
