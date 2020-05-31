package com.dune.game.core.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.dune.game.core.objects.Harvester;
import com.dune.game.core.objects.Unit;
import com.dune.game.core.objects.BattleTank;

public class HarvestersController extends UnitsController {
    private Vector2 tmp;

    @Override
    protected Harvester newObject() {
        return new Harvester(gc);
    }

    public HarvestersController(GameController gc) {
        super(gc);
        this.tmp = new Vector2();
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).render(batch);
        }
    }

    public void setup(float x, float y, Unit.Owner ownerType) {
        Harvester t = (Harvester)activateObject();
        t.setup(ownerType, x, y);
    }

    public void update(float dt) {
        aiUpdate(dt);
        checkPool();
    }

    public void aiUpdate(float dt) {

    }
}
