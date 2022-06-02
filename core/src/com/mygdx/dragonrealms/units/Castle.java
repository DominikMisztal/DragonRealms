package com.mygdx.dragonrealms.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Matrix4;
import com.mygdx.dragonrealms.Player;
import com.mygdx.dragonrealms.map.Tile;

public class Castle extends Unit {
    public Castle(Tile tile, Player player, Matrix4 projectionMatrix, int playerNum) {
        super("Castle", new Texture(Gdx.files.internal("textures/buildings/castle"+playerNum+".png")) , 99, 0, 30, 0, 0, player, (int)tile.getCoordinates().x, (int)tile.getCoordinates().y);
    }
}