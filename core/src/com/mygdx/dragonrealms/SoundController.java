package com.mygdx.dragonrealms;

import javax.swing.plaf.multi.MultiInternalFrameUI;

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

    private Music music;

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

        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
        music.setLooping(true);
    }

    public void stopAllSounds(){
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

    public void playMusic(){
        music.setVolume(0.1f);
        music.play();
    }

    public void pauseMusic(){
        music.pause();
    }

    public void stopMusic(){
        music.stop();
    }

    public void playClick(){
        if(MyGame.isSpecialSoundsActive){
            click.play();
        }
    }

    public void playKnightSelect(){
        stopAllSounds();
        if(MyGame.isSpecialSoundsActive){
            knightSelect.play();
        }
    }

    public void playWarriorSelect(){
        stopAllSounds();
        if(MyGame.isSpecialSoundsActive){
            warriorSelect.play();
        }
    }

    public void playArcherSelect(){
        stopAllSounds();
        if(MyGame.isSpecialSoundsActive){
            archerSelect.play();
        }
    }

    public void playGoldMineSelect(){
        stopAllSounds();
        if(MyGame.isSpecialSoundsActive){
            goldMineSelect.play();
        }
    }

    public void playCastleSelect(){
        stopAllSounds();
        if(MyGame.isSpecialSoundsActive){
            castleSelect.play();
        }
    }

    public void playKnightAttack(){
        stopAllSounds();
        if(MyGame.isSpecialSoundsActive){
            knightAttack.play();
        }
    }

    public void playArcherAttack(){
        stopAllSounds();
        if(MyGame.isSpecialSoundsActive){
            archerAttack.play();
        }
    }

    public void playWarriorAttack(){
        stopAllSounds();
        if(MyGame.isSpecialSoundsActive){
            warriorAttack.play();
        }
    }

    public void playWarriorMarch(){
        stopAllSounds();
        if(MyGame.isSpecialSoundsActive){
            warriorMarch.play();
        }
    }

    public void playArcherMarch(){
        stopAllSounds();
        if(MyGame.isSpecialSoundsActive){
            archerMarch.play();
        }
    }

    public void playKnightMarch(){
        stopAllSounds();
        if(MyGame.isSpecialSoundsActive){
            knightMarch.play();
        }
    }
    
}
