package com.dune.game.core.controllers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.dune.game.core.objects.BattleTank;
import com.dune.game.core.objects.GameObject;
import com.dune.game.core.objects.Unit;

public class BattleTanksController extends UnitsController {
    private Vector2 tmp;

    @Override
    protected BattleTank newObject() {
        return new BattleTank(gc);
    }

    public BattleTanksController(GameController gc) {
        super(gc);
        this.tmp = new Vector2();
    }

    public void setup(float x, float y, GameObject.Owner ownerType) {
        BattleTank t = (BattleTank)activateObject();
        t.setup(ownerType, x, y);
    }

    public void update(float dt) {

        aiUpdate(dt);
        checkPool();
    }



    public void aiUpdate(float dt) {

    }
}
