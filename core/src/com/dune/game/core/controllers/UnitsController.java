package com.dune.game.core.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.dune.game.core.objects.*;

import java.util.ArrayList;
import java.util.List;

public class UnitsController extends ObjectPool<Unit> {
    protected GameController gc;
    protected Vector2 tmp;

    private BattleTanksController btc;
    private HarvestersController hc;

    List<Unit> unitsActiveList;

    public List<Unit> getUnitsActiveList() {
        return unitsActiveList;
    }

    public UnitsController(GameController gc) {
        this.gc = gc;
        unitsActiveList = new ArrayList<>();
        tmp = new Vector2();
    }

    public void setup() {
        for (int i = 0; i < 5; i++) {
            if (MathUtils.random() < 0.5f) {
                gc.getBtc().setup(MathUtils.random(80, 1200), MathUtils.random(80, 640), Unit.Owner.PLAYER);
            } else {
                gc.getHc().setup(MathUtils.random(80, 1200), MathUtils.random(80, 640), Unit.Owner.PLAYER);
            }
        }
        for (int i = 0; i < 2; i++) {
            if (MathUtils.random() < 0.5f) {
                gc.getBtc().setup(MathUtils.random(80, 1200), MathUtils.random(80, 640), Unit.Owner.AI);
            } else {
                gc.getHc().setup(MathUtils.random(80, 1200), MathUtils.random(80, 640), Unit.Owner.AI);
            }
        }
        unitsActiveList.addAll(gc.getBtc().getActiveList());
        unitsActiveList.addAll(gc.getHc().getActiveList());
    }

    public Unit getNearestAiUnit(Vector2 point) {
        for (int i = 0; i < unitsActiveList.size(); i++) {
            Unit t = unitsActiveList.get(i);
            if (t.getOwnerType() == BattleTank.Owner.AI && t.getPosition().dst(point) < 30) {
                return t;
            }
        }
        return null;
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < unitsActiveList.size(); i++) {
            unitsActiveList.get(i).render(batch);
        }
    }

    public void update(float dt) {
        for (int i = 0; i < unitsActiveList.size(); i++) {
            Unit u=unitsActiveList.get(i);
            if (u.isActive()) {
                u.update(dt);
            } else {
                unitsActiveList.remove(i);
            }
        }
        playerUpdate(dt);
        checkCollisions(dt);
        checkPool();
    }


    public void playerUpdate(float dt) {
        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
            for (int i = 0; i < gc.getSelectedUnits().size(); i++) {
                Unit t = gc.getSelectedUnits().get(i);
                if (t.getOwnerType() == Unit.Owner.PLAYER && gc.getSelectedUnits().contains(t)) {
                    t.action();
                }
            }
        }
    }

    public void checkCollisions(float dt) {
        for (int i = 0; i < unitsActiveList.size() - 1; i++) {
            Unit t1 = unitsActiveList.get(i);
            for (int j = i + 1; j < unitsActiveList.size(); j++) {
                Unit t2 = unitsActiveList.get(j);
                float dst = t1.getPosition().dst(t2.getPosition());
                if (dst < 30 + 30) {
                    float colLengthD2 = (60 - dst) / 2;
                    tmp.set(t2.getPosition()).sub(t1.getPosition()).nor().scl(colLengthD2);
                    t2.moveBy(tmp);
                    tmp.scl(-1);
                    t1.moveBy(tmp);
                }
            }
        }
    }

    @Override
    protected Unit newObject() {
        return null;
    }
}
