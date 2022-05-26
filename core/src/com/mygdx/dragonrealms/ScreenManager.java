package com.mygdx.dragonrealms;

import com.badlogic.gdx.Screen;

import java.util.HashMap;

public class ScreenManager {
    private final MyGame game;
    private HashMap<STATE, Screen> gameScreens;

    public enum STATE{
        MAIN_MENU,
        PLAY,
        ENDGAME
    }

    public ScreenManager(final MyGame game){
        this.game = game;
        initGameScreens();
        setScreen(STATE.MAIN_MENU);
    }

    private void initGameScreens(){
        this.gameScreens = new HashMap<>();
        this.gameScreens.put(STATE.MAIN_MENU, new MainMenuScreen(game));
        this.gameScreens.put(STATE.PLAY, new MainGameScreen(game));
        this.gameScreens.put(STATE.ENDGAME, new EndGameScreen(game));
    }

    public void setScreen(STATE nextScreen){
        game.setScreen(gameScreens.get(nextScreen));
    }

    public void dispose(){
        for(Screen screen : gameScreens.values()){
            if(screen != null){
                screen.dispose();
            }
        }
    }

}
