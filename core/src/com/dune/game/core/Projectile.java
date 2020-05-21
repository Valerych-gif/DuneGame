package com.dune.game.core;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Projectile {
    private Vector2 position;
    private Vector2 velocity;
    private boolean isActive;
    private TextureRegion texture;
    private float angle;
    private float speed = 200f;

    public Vector2 getPosition() {
        return position;
    }

    public boolean isActive() {
        return isActive;
    }

    public Projectile(TextureAtlas atlas) {
        this.texture = atlas.findRegion("bullet");
        this.position = new Vector2(0,0);
        this.velocity = new Vector2(0,0);
        this.isActive=false;
    }

    public void setup(Vector2 startPosition, float angle) {
        if (!isActive) {
            isActive = true;
            this.position.set(startPosition.x+25 * MathUtils.cosDeg(angle), startPosition.y+25 * MathUtils.sinDeg(angle));
            velocity.set(speed * MathUtils.cosDeg(angle), speed * MathUtils.sinDeg(angle));
        }
    }

    public void destroy(){
        isActive = false;
        position.set(0,0);
        velocity.set(0,0);
    }

    public void update(float dt) {
        // position.x += velocity.x * dt;
        // position.y += velocity.y * dt;
        position.mulAdd(velocity, dt);
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 8, position.y - 8, 8, 8, 16, 16, 1, 1, angle);
    }
}
