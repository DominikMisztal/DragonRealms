package com.mygdx.dragonrealms.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.dragonrealms.Player;
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
    private int maxMovement;
    private int currentMovement;
    private Player player;
    private Matrix4 projectionMatrix;
    private float hpWidth;

    private ShapeRenderer sr;

    public Unit(String unitName, Texture texture, int unitCost, int attack, int hp, int range, int movementRange, Player player, int x, int y, Matrix4 projectionMatrix){
        this.unitName = unitName;
        this.texture = texture;
        this.sprite = new Sprite(texture);
        this.unitCost = unitCost;
        this.attack = attack;
        this.max_hp = hp;
        this.current_hp = hp;
        this.range = range;
        this.maxMovement = movementRange;
        this.currentMovement = movementRange;
        this.player = player;
        hpWidth = Map.TILESIZE-20;
        coordinates = new Vector2(x,y);
        sprite.setPosition(coordinates.x * Map.TILESIZE, coordinates.y * Map.TILESIZE);
        sr = new ShapeRenderer();
        //sr.setProjectionMatrix(projectionMatrix);
        this.projectionMatrix = projectionMatrix;
    }

    public void render(SpriteBatch spriteBatch){
        sprite.draw(spriteBatch);
    }

    public void renderHealthBar(){
        sr.begin(ShapeType.Filled);
        sr.setProjectionMatrix(projectionMatrix);
        sr.setColor(Color.GREEN);
        sr.rect(coordinates.x * Map.TILESIZE+10, coordinates.y * Map.TILESIZE + 10, hpWidth, 5);
        sr.end();
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

    public int getCurrentMovement(){
        return currentMovement;
    }

    public void setCurrentMovement(int movement){
        currentMovement = movement;
    }

    public void resetMovement(){
        currentMovement = maxMovement;
    }

    public Player getPlayer(){
        return player;
    }
    public Vector2 getCoordinates(){
        return coordinates;
    }

    public void changePosition(int x, int y){
            coordinates.x = x; coordinates.y = y;
            sprite.setPosition(coordinates.x * Map.TILESIZE, coordinates.y * Map.TILESIZE);
    }

    public boolean damage(int damage){
        current_hp -= damage;
        changeSprite();
        hpWidth = (float)current_hp / (float)max_hp * 44;
        if(current_hp <= 0){
            return true;
        }
        return false;
    }

    private void changeSprite(){
        if(this.unitName == "Warrior"){
            if(current_hp < Math.floor(max_hp/3)){
                sprite.setTexture(new Texture(Gdx.files.internal("textures/warrior/warrior1.png")));
            }
            else if(current_hp < Math.floor(2*max_hp/3)){
                sprite.setTexture(new Texture(Gdx.files.internal("textures/warrior/warrior2.png")));
            }
        }
        else if(this.unitName == "Archer"){
            if(current_hp <= Math.floor(max_hp/3)){
                sprite.setTexture(new Texture(Gdx.files.internal("textures/archer/archer1.png")));
            }
            else if(current_hp <= Math.floor(2*max_hp/3)){
                sprite.setTexture(new Texture(Gdx.files.internal("textures/archer/archer2.png")));
            }
        }
        else{
            if(current_hp <= Math.floor(max_hp/2)){
                sprite.setTexture(new Texture(Gdx.files.internal("textures/knight/knight1.png")));
            }
        }
    }

}
