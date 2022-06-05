package com.mygdx.dragonrealms.map;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.dragonrealms.screens.MainGameScreen;
import com.mygdx.dragonrealms.screens.Mode;
import com.mygdx.dragonrealms.units.Archer;
import com.mygdx.dragonrealms.units.Castle;
import com.mygdx.dragonrealms.units.GoldMine;
import com.mygdx.dragonrealms.units.Knight;
import com.mygdx.dragonrealms.units.Warrior;

public class TileClickListener extends ClickListener {
    private Tile tile;
    private MainGameScreen mainGameScreen;
    
    public TileClickListener(Tile tile, MainGameScreen mgs){
        mainGameScreen = mgs;
        this.tile = tile;
    }

    @Override
    public void clicked(InputEvent event, float x, float y){
        if(mainGameScreen.gamePaused || mainGameScreen.scrolled){
            return;
        }
        mainGameScreen.previouslySelectedTile = mainGameScreen.currentlySelectedTile;
        mainGameScreen.currentlySelectedTile = tile;
        if(mainGameScreen.currentMode == Mode.SPAWN_UNIT){
            handleUnitSpawn();
            return;
        }
        if(tile.getBorder() == 4){
            mainGameScreen.soundController.playClick();
            return;
        }
        if(tile.getUnit() == null){
            if(mainGameScreen.tilesToDraw.contains(tile) && 
            (tile.getType() != TileType.MOUNTAIN && tile.getType() != TileType.WATER )){
                mainGameScreen.moveUnit();
                if(mainGameScreen.currentlySelectedUnit instanceof Warrior){
                    mainGameScreen.soundController.playWarriorMarch();
                }
                else if(mainGameScreen.currentlySelectedUnit instanceof Archer){
                    mainGameScreen.soundController.playArcherMarch();
                }
                else if(mainGameScreen.currentlySelectedUnit instanceof Knight){
                    mainGameScreen.soundController.playKnightMarch();
                }
                mainGameScreen.currentlySelectedUnit = null;
                return;
            }
        }
        else if(tile.getUnit() != null && mainGameScreen.tilesToDraw.contains(tile) 
                && tile.getUnit().getPlayer() != mainGameScreen.players.get(mainGameScreen.currentPlayer)
                && tile.getBorder() == 2){
                    mainGameScreen.unitAttack(mainGameScreen.currentlySelectedUnit, tile.getUnit());
                    mainGameScreen.currentlySelectedUnit = null;
                    mainGameScreen.clearMovementTiles();
                    return;
            }
        mainGameScreen.currentlySelectedUnit = null;
        mainGameScreen.clearMovementTiles();
        if(tile.getUnit() != null && tile.getUnit().getPlayer() == mainGameScreen.players.get(mainGameScreen.currentPlayer)){
            mainGameScreen.currentlySelectedUnit = tile.getUnit();
            if(mainGameScreen.currentlySelectedUnit instanceof Warrior){
                mainGameScreen.soundController.playWarriorSelect();
            }
            else if(mainGameScreen.currentlySelectedUnit instanceof Archer){
                mainGameScreen.soundController.playArcherSelect();
            }
            else if(mainGameScreen.currentlySelectedUnit instanceof Knight){
                mainGameScreen.soundController.playKnightSelect();
            }
            else if(mainGameScreen.currentlySelectedUnit instanceof GoldMine){
                mainGameScreen.soundController.playGoldMineSelect();
            }
            else if(mainGameScreen.currentlySelectedUnit instanceof Castle){
                mainGameScreen.soundController.playCastleSelect();
            }
            mainGameScreen.findUnitMovementRange(tile.getUnit(), tile);
            if(tile.getUnit() instanceof Archer && tile.getUnit().attacked == false){
                mainGameScreen.findRangedAttack(tile.getUnit());
            }
        }
        else{
            mainGameScreen.soundController.playClick();
            mainGameScreen.tilesToDraw.add(tile);
            tile.setBorder(4);
        }
        if(tile.getUnit() != null){
            System.out.println("Unit hp: " + tile.getUnit().getCurrent_hp() + "/" + tile.getUnit().getMax_hp());
        }
    }

    private void handleUnitSpawn(){
        if(mainGameScreen.tilesToDraw.contains(tile)){
            mainGameScreen.spawnUnit();
        }
        else{
            mainGameScreen.soundController.playClick();
            mainGameScreen.clearMovementTiles();
            mainGameScreen.currentMode = Mode.NONE;
        }
    }
}
