package com.mygdx.dragonrealms.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Matrix4;
import com.mygdx.dragonrealms.Player;
import com.mygdx.dragonrealms.map.Tile;

public class Warrior extends Unit {

    public Warrior(){
        super("Warrior", new Texture(Gdx.files.internal("textures/warrior/warrior3.png")) , 3, 2, 6, 1, 4);
    }

    public Warrior(Tile tile, Player player, Matrix4 projectionMatrix) {
        super("Warrior", new Texture(Gdx.files.internal("textures/"+ player.getPlayerName()+"/warrior/warrior3.png")) , 3, 2, 6, 1, 4, player, (int)tile.getCoordinates().x, (int)tile.getCoordinates().y);
    }
}
