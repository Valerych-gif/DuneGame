package com.dune.game.core.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.dune.game.core.Assets;
import com.dune.game.core.Weapon;
import com.dune.game.core.controllers.GameController;

public class BattleTank extends Unit {

    // Tank's parameters setup
    private static final int MAX_HP = 100;
    private static final float MAX_SPEED = 120.0f;
    private static final float MAX_ROTATION_SPEED = 90.0f;

    // ...and weapon
    private static final float WEAPON_PERIOD = 1.5f;
    private static final int WEAPON_POWER = 1;

    // Settings of rendering
    private static final float TIME_PER_FRAME = 0.08f;

    private Weapon weapon;
    private TextureRegion weaponsTextures;
    private GameObject target;

    public Weapon getWeapon() {
        return weapon;
    }

    public BattleTank(GameController gc) {
        super(gc);
        this.weaponsTextures = new TextureRegion(Assets.getInstance().getAtlas().findRegion("turret"));

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
        this.weapon = new Weapon(Weapon.Type.GROUND, WEAPON_PERIOD, WEAPON_POWER);
        this.destination = new Vector2(position);
    }

    public void update(float dt) {
        lifeTime += dt;
        // Если у танка есть цель, он пытается ее атаковать
        if (target != null) {
            destination.set(target.position);
            if (position.dst(target.position) < 240.0f) {
                destination.set(position);
            }
        }
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

    public void action() {
        if (getOwnerType() == Owner.PLAYER && gc.getSelectedUnits().contains(this)) {
            tmp.set(Gdx.input.getX(), 720 - Gdx.input.getY());
            GameObject aiTank = gc.getUc().getNearestAiUnit(tmp);
            if (aiTank == null) {
                commandMoveTo(tmp);
                commandAttack(null);
            } else {
                commandAttack(aiTank);
            }

        }
    }

    public void commandAttack(GameObject target) {
        this.target = target;
    }

    public void updateWeapon(float dt) {
        if (weapon.getType() == Weapon.Type.GROUND && target != null) {
            float angleTo = tmp.set(target.position).sub(position).angle();
            weapon.setAngle(rotateTo(weapon.getAngle(), angleTo, 180.0f, dt));
            int power = weapon.use(dt);
            if (power > -1) {
                tmp.set(position);
                tmp.add(35 * MathUtils.cosDeg(weapon.getAngle()), 35 * MathUtils.sinDeg(weapon.getAngle()));
                gc.getPc().setup(tmp, weapon.getAngle());
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
        batch.draw(weaponsTextures, position.x - 40, position.y - 40, 40, 40, 80, 80, 1, 1, weapon.getAngle());

        batch.setColor(1, 1, 1, 1);


    }
}