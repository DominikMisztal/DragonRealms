package com.mygdx.dragonrealms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SoundController {
    private Sound warriorSelect;
    private Sound archerSelect;
    private Sound knightSelect;
    private Sound goldMineSelect;
    private Sound castleSelect;

    private Sound warriorAttack;
    private Sound archerAttack;
    private Sound knightAttack;

    private Sound warriorMarch;
    private Sound archerMarch;
    private Sound knightMarch;

    private Sound click;

    public SoundController(){
        warriorSelect = Gdx.audio.newSound(Gdx.files.internal("sounds/warrior_select.mp3"));
        archerSelect = Gdx.audio.newSound(Gdx.files.internal("sounds/archer_select.mp3"));
        knightSelect = Gdx.audio.newSound(Gdx.files.internal("sounds/knight_select.mp3"));
        goldMineSelect = Gdx.audio.newSound(Gdx.files.internal("sounds/goldmine_select.mp3"));
        castleSelect = Gdx.audio.newSound(Gdx.files.internal("sounds/castle_select.mp3"));

        warriorAttack = Gdx.audio.newSound(Gdx.files.internal("sounds/warrior_attack.mp3"));
        archerAttack = Gdx.audio.newSound(Gdx.files.internal("sounds/archer_attack.mp3"));
        knightAttack = Gdx.audio.newSound(Gdx.files.internal("sounds/knight_attack.mp3"));

        warriorMarch = Gdx.audio.newSound(Gdx.files.internal("sounds/warrior_march.mp3"));
        archerMarch = Gdx.audio.newSound(Gdx.files.internal("sounds/archer_march.mp3"));
        knightMarch = Gdx.audio.newSound(Gdx.files.internal("sounds/knight_march.mp3"));

        click = Gdx.audio.newSound(Gdx.files.internal("sounds/click.wav"));
    }

    private void stopAllSounds(){
        knightSelect.stop();
        warriorSelect.stop();
        archerSelect.stop();
        goldMineSelect.stop();
        castleSelect.stop();
        knightAttack.stop();
        archerAttack.stop();
        warriorAttack.stop();
        warriorMarch.stop();
        archerMarch.stop();
        knightMarch.stop();
    }

    public void playClick(){
        click.play();
    }

    public void playKnightSelect(){
        stopAllSounds();
        knightSelect.play();
    }

    public void playWarriorSelect(){
        stopAllSounds();
        warriorSelect.play();
    }

    public void playArcherSelect(){
        stopAllSounds();
        archerSelect.play();
    }

    public void playGoldMineSelect(){
        stopAllSounds();
        goldMineSelect.play();
    }

    public void playCastleSelect(){
        stopAllSounds();
        castleSelect.play();
    }

    public void playKnightAttack(){
        stopAllSounds();
        knightAttack.play();
    }

    public void playArcherAttack(){
        stopAllSounds();
        archerAttack.play();
    }

    public void playWarriorAttack(){
        stopAllSounds();
        warriorAttack.play();
    }

    public void playWarriorMarch(){
        stopAllSounds();
        warriorMarch.play();
    }

    public void playArcherMarch(){
        stopAllSounds();
        archerMarch.play();
    }

    public void playKnightMarch(){
        stopAllSounds();
        knightMarch.play();
    }
    
}
