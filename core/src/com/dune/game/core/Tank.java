package com.dune.game.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Tank extends GameObject implements Poolable {

    public enum Owner {
        PLAYER, AI
    }

    private static final int CONTAINERS_CAPACITY=50;
    private static final int MAX_HP=100;
    private static final float MAX_SPEED=120.0f;

    private Owner ownerType;
    private Weapon weapon;
    private Vector2 destination;
    private TextureRegion[] textures;
    private TextureRegion progressbarTexture;
    private BitmapFont font32;
    StringBuilder load;
    private int hp;
    private float angle;
    private float speed;
    private float rotationSpeed;

    private float moveTimer;
    private float timePerFrame;
    private int container;
    private boolean isControlled;

    @Override
    public boolean isActive() {
        return hp > 0;
    }

    public Tank(GameController gc) {
        super(gc);
        this.progressbarTexture = Assets.getInstance().getAtlas().findRegion("progressbar");
        this.timePerFrame = 0.08f;
        this.rotationSpeed = 90.0f;
    }

    public void setup(Owner ownerType, float x, float y) {
        this.textures = Assets.getInstance().getAtlas().findRegion("tankanim").split(64,64)[0];
        this.font32 = Assets.getInstance().getAssetManager().get("fonts/font32.ttf");
        load = new StringBuilder();
        this.position.set(x, y);
        this.ownerType = ownerType;
        this.speed = MAX_SPEED;
        this.hp = MAX_HP;
        this.weapon = new Weapon(Weapon.Type.HARVEST, 3.0f, 1);
        this.destination = new Vector2(position);
    }

    private int getCurrentFrameIndex() {
        return (int) (moveTimer / timePerFrame) % textures.length;
    }

    public void startControl() {
        isControlled=true;
    }

    public void stopControl() {
        isControlled=false;
    }

    public void update(float dt) {
        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)&&isControlled) {
            destination.set(Gdx.input.getX(), 720 - Gdx.input.getY());
        }


        if (position.dst(destination) > 3.0f) {
            float angleTo = tmp.set(destination).sub(position).angle();
            if (Math.abs(angle - angleTo) > 3.0f) {
                if (angle > angleTo) {
                    if (Math.abs(angle - angleTo) <= 180.0f) {
                        angle -= rotationSpeed * dt;
                    } else {
                        angle += rotationSpeed * dt;
                    }
                } else {
                    if (Math.abs(angle - angleTo) <= 180.0f) {
                        angle += rotationSpeed * dt;
                    } else {
                        angle -= rotationSpeed * dt;
                    }
                }
            }
            if (angle < 0.0f) {
                angle += 360.0f;
            }
            if (angle > 360.0f) {
                angle -= 360.0f;
            }

            moveTimer += dt;
            tmp.set(speed, 0).rotate(angle);
            position.mulAdd(tmp, dt);
            if (position.dst(destination) < 120.0f && Math.abs(angleTo - angle) > 10) {
                position.mulAdd(tmp, -dt);
            }
        }
        updateWeapon(dt);
        checkBounds();
    }

    public void updateWeapon(float dt) {
        if (weapon.getType() == Weapon.Type.HARVEST) {
            if (gc.getMap().getResourceCount(this) > 0 && container<CONTAINERS_CAPACITY) {
                int result = weapon.use(dt);
                if (result > -1) {
                    container += gc.getMap().harvestResource(this, result);
                }
            } else {
                weapon.reset();
            }
        }
    }

    public void checkBounds() {
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

    public void tryToAvoidObstacle(GameObject obstacle, float dst, float dt){
        tmp.set(obstacle.position);
        float obstacleAngle = tmp.sub(position).angle();
        float angleToRotate = angle - obstacleAngle;
        if (dst>80&&dst<100){
            rotateFromObstacle(angleToRotate, dt);
        }
        if (dst<80){
            if (!rotateFromObstacle(angleToRotate, dt)){
                this.speed=0;
            } else {
                this.speed=MAX_SPEED;
            }
        }

        if (destination.dst(obstacle.position)<80){
            destination.set(position);
        }
    }

    private boolean rotateFromObstacle(float angleToRotate, float dt) {
        if (angleToRotate>0&&angleToRotate<90){
            angle+=rotationSpeed*dt*2;
            return false;
        } else if (angleToRotate<=0&&angleToRotate>-90){
            angle-=rotationSpeed*dt*2;
            return false;
        }
        return true;
    }

    public void render(SpriteBatch batch) {
        if (isControlled) batch.setColor(1.0f, 0.7f, 0.7f, 1.0f);
        batch.draw(textures[getCurrentFrameIndex()], position.x - 40, position.y - 40, 40, 40, 80, 80, 1, 1, angle);
        batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);

        if (weapon.getType() == Weapon.Type.HARVEST && weapon.getUsageTimePercentage() > 0.0f) {
            batch.setColor(0.2f, 0.2f, 0.0f, 1.0f);
            batch.draw(progressbarTexture, position.x - 32, position.y + 30, 64, 12);
            batch.setColor(1.0f, 1.0f, 0.0f, 1.0f);
            batch.draw(progressbarTexture, position.x - 30, position.y + 32, 60 * weapon.getUsageTimePercentage(), 8);
            batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
            load.append(container).append("/").append(CONTAINERS_CAPACITY);
            font32.draw(batch, load, position.x, position.y+70, 0, 1, false);
            load.setLength(0);
        }

        if (container==CONTAINERS_CAPACITY){
            font32.draw(batch, "Overload", position.x, position.y+70, 0, 1, false);
        }
    }
}
