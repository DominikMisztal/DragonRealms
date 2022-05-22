package com.mygdx.dragonrealms.units;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.dragonrealms.map.Map;


public class Unit {
    private String unitName;
    private Texture texture;
    private Sprite sprite;
    private Vector2 coordinates;
    private int unitCost;
    private int attack;
    private int max_hp;
    private int current_hp;
    private int range;



    public Unit(String unitName, Texture texture, int unitCost, int attack, int hp, int range, int x, int y){
        this.unitName = unitName;
        this.texture = texture;
        this.sprite = new Sprite(texture);
        this.unitCost = unitCost;
        this.attack = attack;
        this.max_hp = hp;
        this.current_hp = hp;
        this.range = range;
        coordinates = new Vector2(x,y);
        sprite.setPosition(coordinates.x * Map.TILESIZE + 12, coordinates.y * Map.TILESIZE + 12);


    }

    public void render(SpriteBatch spriteBatch){
        sprite.draw(spriteBatch);
    }

    public String getUnitName(){
        return this.unitName;
    }

    public Texture getTexture(){
        return this.texture;
    }

    public int getUnitCost(){
        return this.unitCost;
    }

    public int getAttack(){
        return this.attack;
    }

    public int getMax_hp() {
        return max_hp;
    }

    public int getCurrent_hp() {
        return current_hp;
    }

    public int getRange(){
        return this.range;
    }

}
