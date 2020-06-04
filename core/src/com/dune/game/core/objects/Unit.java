package com.dune.game.core.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.dune.game.core.Assets;
import com.dune.game.core.controllers.GameController;

public class Unit extends GameObject implements Poolable {

    protected Vector2 destination;
    protected TextureRegion[] textures;
    protected TextureRegion hpProgressbarTexture;
    protected int hp;
    protected int hpMax;
    protected float angle;
    protected float speed;
    protected float rotationSpeed;
    protected float moveTimer;
    protected float lifeTime;
    protected float timePerFrame;

    public Unit(GameController gc) {
        super(gc);
        this.hpProgressbarTexture = Assets.getInstance().getAtlas().findRegion("progressbar");
    }

    @Override
    public boolean isActive() {
        return hp > 0;
    }

    public void decreaseHP(int value){
        hp-=value;
    }

    protected int getCurrentFrameIndex() {
        return (int) (moveTimer / timePerFrame) % textures.length;
    }

    public void moveBy(Vector2 value) {
        boolean stayStill = false;
        if (position.dst(destination) < 3.0f) {
            stayStill = true;
        }
        position.add(value);
        if (stayStill) {
            destination.set(position);
        }
    }

    public void commandMoveTo(Vector2 point) {
        destination.set(point);
    }

    public float rotateTo(float srcAngle, float angleTo, float rSpeed, float dt) {
        if (Math.abs(srcAngle - angleTo) > 3.0f) {
            if ((srcAngle > angleTo && Math.abs(srcAngle - angleTo) <= 180.0f) || (srcAngle < angleTo && Math.abs(srcAngle - angleTo) > 180.0f)) {
                srcAngle -= rSpeed * dt;
            } else {
                srcAngle += rSpeed * dt;
            }
        }
        if (srcAngle < 0.0f) {
            srcAngle += 360.0f;
        }
        if (srcAngle > 360.0f) {
            srcAngle -= 360.0f;
        }
        return srcAngle;
    }

    public void playerUpdate(float dt) {
        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
            for (int i = 0; i < gc.getSelectedUnits().size(); i++) {
                Unit t = gc.getSelectedUnits().get(i);
                t.action();
            }
        }
    }


    protected void checkBounds() {
        if (position.x < 40) {
            position.x = 40;
        }
        if (position.y < 40) {
            position.y = 40;
        }
        if (position.x > 1240) {
            position.x = 1240;
        }
        if (position.y > 680) {
            position.y = 680;
        }
    }

    protected void makeBlinked(SpriteBatch batch) {
        float c = 0.7f + (float) Math.sin(lifeTime * 8.0f) * 0.3f;
        batch.setColor(c, c, c, 1.0f);
    }

    public void action(){
    }
}
