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
        if(tile.getBorder() == 4){
            return;
        }
        if(tile.getUnit() == null){
            if(mainGameScreen.tilesToDraw.contains(tile) && 
            (tile.getType() != TileType.MOUNTAIN && tile.getType() != TileType.WATER )){
                mainGameScreen.moveUnit();
                mainGameScreen.currentlySelectedUnit = null;
                return;
            }
        }
        else if(tile.getUnit() != null && mainGameScreen.tilesToDraw.contains(tile) 
                && tile.getUnit().getPlayer() != mainGameScreen.players.get(mainGameScreen.currentPlayer)){
                    mainGameScreen.unitAttack(mainGameScreen.currentlySelectedUnit, tile.getUnit());
                    mainGameScreen.currentlySelectedUnit = null;
                    mainGameScreen.clearMovementTiles();
                    return;
            }
        mainGameScreen.currentlySelectedUnit = null;
        mainGameScreen.clearMovementTiles();
        if(tile.getUnit() != null && tile.getUnit().getPlayer() == mainGameScreen.players.get(mainGameScreen.currentPlayer)){
            mainGameScreen.currentlySelectedUnit = tile.getUnit();
            mainGameScreen.findUnitMovementRange(tile.getUnit(), tile);
        }
        else{
            mainGameScreen.tilesToDraw.add(tile);
            tile.setBorder(4);
        }
        System.out.println("Hello from actor " + tile.getCoordinates());
        if(tile.getUnit() != null){
            System.out.println("Unit hp: " + tile.getUnit().getCurrent_hp() + "/" + tile.getUnit().getMax_hp());
        }
        //System.out.println("Currently selected tile: " + mainGameScreen.currentlySelectedTile.getCoordinates());
    }
}
