package com.dune.game.core.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.dune.game.core.BattleMap;
import com.dune.game.core.controllers.GameController;

public abstract class GameObject implements Poolable {
    protected Owner ownerType;
    protected GameController gc;
    protected Vector2 position;
    protected Vector2 tmp;


    public enum Owner {
        PLAYER, AI
    }


    public Owner getOwnerType() {
        return ownerType;
    }

    public Vector2 getPosition() {
        return position;
    }

    public int getCellX() {
        return (int) (position.x / BattleMap.CELL_SIZE);
    }

    public int getCellY() {
        return (int) (position.y / BattleMap.CELL_SIZE);
    }

    public void moveBy(Vector2 value) {
        position.add(value);
    }

    public GameObject(GameController gc) {
        this.gc = gc;
        this.position = new Vector2();
        this.tmp = new Vector2();
    }

    public void render(SpriteBatch batch){

    }

    public void update(float dt){

    }

    public boolean isActive(){
        return false;
    }

}