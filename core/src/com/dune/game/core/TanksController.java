package com.dune.game.core;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TanksController extends ObjectPool<Tank> {
    private GameController gc;

    @Override
    protected Tank newObject() {
        return new Tank(gc);
    }

    public TanksController(GameController gc) {
        this.gc = gc;
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).render(batch);
        }
    }

    public void setup(float x, float y, Tank.Owner ownerType) {
        Tank t = getActiveElement();
        t.setup(ownerType, x, y);
    }

    public void update(float dt) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkCollisions(dt);
        checkPool();
    }

    public void select(int x, int y) {
        for (int i = 0; i < activeList.size(); i++) {
            if (activeList.get(i).position.dst(x, y) < 40.0f) {
                activeList.get(i).startControl();
            } else {
                activeList.get(i).stopControl();
            }
        }
    }

    private void checkCollisions(float dt) {
        Tank thisTank, otherTank;
        float dstToObstacle;
        for (int i = 0; i < activeList.size(); i++) {
            thisTank = activeList.get(i);
            for (int j = 0; j < activeList.size(); j++) {
                if (i != j) {
                    otherTank = activeList.get(j);
                    dstToObstacle = thisTank.position.dst(otherTank.position);
                    if (dstToObstacle < 100.0f) {
                        thisTank.tryToAvoidObstacle(otherTank, dstToObstacle, dt);
                    }
                }
            }
        }
    }
}
