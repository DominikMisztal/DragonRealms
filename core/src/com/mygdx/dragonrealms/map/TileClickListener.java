package com.mygdx.dragonrealms.map;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.dragonrealms.screens.MainGameScreen;

public class TileClickListener extends ClickListener {
    private Tile tile;
    private MainGameScreen mainGameScreen;
    
    public TileClickListener(Tile tile, MainGameScreen mgs){
        mainGameScreen = mgs;
        this.tile = tile;
    }

    @Override
    public void clicked(InputEvent event, float x, float y){
        mainGameScreen.previouslySelectedTile = mainGameScreen.currentlySelectedTile;
        mainGameScreen.currentlySelectedTile = tile;
        if(mainGameScreen.tilesToDraw.contains(tile)){
            mainGameScreen.moveUnit();
            mainGameScreen.currentlySelectedUnit = null;
            return;
        }
        mainGameScreen.currentlySelectedUnit = null;
        mainGameScreen.tilesToDraw.clear();
        if(tile.getUnit() != null){
            mainGameScreen.currentlySelectedUnit = tile.getUnit();
            mainGameScreen.findUnitMovementRange(tile.getUnit(), tile);
        }
        System.out.println("Hello from actor " + tile.getCoordinates());
        System.out.println("Currently selected tile: " + mainGameScreen.currentlySelectedTile.getCoordinates());
    }
}
