package com.dune.game.core.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.dune.game.core.Assets;
import com.dune.game.core.Weapon;
import com.dune.game.core.controllers.GameController;

public class Harvester extends Unit {

    // Tank's parameters setup
    private static final int MAX_HP=100;
    private static final float MAX_SPEED=120.0f;
    private static final float MAX_ROTATION_SPEED=90.0f;

    // ...and weapon
    private static final float WEAPON_PERIOD=3.0f;
    private static final int WEAPON_POWER=1;

    // Settings of rendering
    private static final float TIME_PER_FRAME=0.08f;

    private int container;
    private Weapon weapon;

    private TextureRegion weaponsTextures;
    private TextureRegion progressbarTexture;

    public Weapon getWeapon() {
        return weapon;
    }


    public Owner getOwnerType() {
        return ownerType;
    }

    @Override
    public boolean isActive() {
        return hp > 0;
    }

    public Harvester(GameController gc) {
        super(gc);
        this.progressbarTexture = Assets.getInstance().getAtlas().findRegion("progressbar");
        this.weaponsTextures = new TextureRegion(Assets.getInstance().getAtlas().findRegion("harvester"));

        this.timePerFrame = TIME_PER_FRAME;
        this.rotationSpeed = MAX_ROTATION_SPEED;
    }

    public void setup(Owner ownerType, float x, float y) {
        this.textures = Assets.getInstance().getAtlas().findRegion("tankcore").split(64, 64)[0];
        this.position.set(x, y);
        this.ownerType = ownerType;
        this.speed = MAX_SPEED;
        this.hpMax = MAX_HP;
        this.hp = this.hpMax;
        this.weapon = new Weapon(Weapon.Type.HARVEST, WEAPON_PERIOD, WEAPON_POWER);
        this.destination = new Vector2(position);
    }

    public void update(float dt) {
        lifeTime+=dt;
        // Если танку необходимо доехать до какой-то точки, он работает в этом условии
        if (position.dst(destination) > 3.0f) {
            float angleTo = tmp.set(destination).sub(position).angle();
            angle = rotateTo(angle, angleTo, rotationSpeed, dt);
            moveTimer += dt;
            tmp.set(speed, 0).rotate(angle);
            position.mulAdd(tmp, dt);
            if (position.dst(destination) < 120.0f && Math.abs(angleTo - angle) > 10) {
                position.mulAdd(tmp, -dt);
            }
        }
        updateWeapon(dt);
        playerUpdate(dt);
        checkBounds();
    }

    public void commandMoveTo(Vector2 point) {
        destination.set(point);
    }

    public void updateWeapon(float dt) {

        if (weapon.getType() == Weapon.Type.HARVEST) {
            if (gc.getMap().getResourceCount(this) > 0) {
                int result = weapon.use(dt);
                if (result > -1) {
                    container += gc.getMap().harvestResource(this, result);
                }
            } else {
                weapon.reset();
            }
        }
    }

    public void render(SpriteBatch batch) {
        if (ownerType==Owner.AI){
            batch.setColor(0, 1, 0, 1);
        }
        if (gc.isUnitSelected(this)) {
            makeBlinked(batch);
        }

        batch.draw(textures[getCurrentFrameIndex()], position.x - 40, position.y - 40, 40, 40, 80, 80, 1, 1, angle);

        batch.setColor(1, 1, 1, 1);
        if (weapon.getUsageTimePercentage() > 0.0f) {
            batch.setColor(0.2f, 0.2f, 0.0f, 1.0f);
            batch.draw(progressbarTexture, position.x - 32, position.y + 30, 64, 12);
            batch.setColor(1.0f, 1.0f, 0.0f, 1.0f);
            batch.draw(progressbarTexture, position.x - 30, position.y + 32, 60 * weapon.getUsageTimePercentage(), 8);
            batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        }

        batch.setColor(0.2f, 0.2f, 0.0f, 1.0f);
        batch.draw(progressbarTexture, position.x - 32, position.y + 50, 64, 12);
        batch.setColor(1.0f, 1.0f, 0.0f, 1.0f);
        batch.draw(progressbarTexture, position.x - 30, position.y + 52, (float)(60 * hp)/hpMax, 8);
        batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    @Override
    public void action() {
        if (getOwnerType() == Owner.PLAYER && gc.getSelectedUnits().contains(this)) {
            tmp.set(Gdx.input.getX(), 720 - Gdx.input.getY());
            commandMoveTo(tmp);
        }
    }
}