package com.mygdx.dragonrealms.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Matrix4;
import com.mygdx.dragonrealms.Player;
import com.mygdx.dragonrealms.map.Tile;

public class Knight extends Unit {

    public Knight(){
        super("Knight", new Texture(Gdx.files.internal("textures/knight/knight2.png")) , 7, 5, 8, 1, 6);
    }

    public Knight(Tile tile, Player player, Matrix4 projectionMatrix) {
        super("Knight", new Texture(Gdx.files.internal("textures/"+ player.getPlayerName()+"/knight/knight2.png")) , 7, 5, 8, 1, 6, player, (int)tile.getCoordinates().x, (int)tile.getCoordinates().y);
    }
}
