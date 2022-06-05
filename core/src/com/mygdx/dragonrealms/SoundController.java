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

    public void playClick(){
        click.play();
    }

    public void playKnightSelect(){
        knightSelect.stop();
        knightSelect.play();
    }

    public void playWarriorSelect(){
        warriorSelect.stop();
        warriorSelect.play();
    }

    public void playArcherSelect(){
        archerSelect.stop();
        archerSelect.play();
    }

    public void playGoldMineSelect(){
        goldMineSelect.stop();
        goldMineSelect.play();
    }

    public void playCastleSelect(){
        castleSelect.stop();
        castleSelect.play();
    }

    public void playKnightAttack(){
        knightAttack.stop();
        knightAttack.play();
    }

    public void playArcherAttack(){
        archerAttack.stop();
        archerAttack.play();
    }

    public void playWarriorAttack(){
        warriorAttack.stop();
        warriorAttack.play();
    }

    public void playWarriorMarch(){
        warriorMarch.stop();
        warriorMarch.play();
    }

    public void playArcherMarch(){
        archerMarch.stop();
        archerMarch.play();
    }

    public void playKnightMarch(){
        knightMarch.stop();
        knightMarch.play();
    }
    
}
