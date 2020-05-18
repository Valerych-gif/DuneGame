package com.dune.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class DuneGame extends ApplicationAdapter {
    private static class Tank {
        private Vector2 position;
        private Texture texture;
        private float angle;
        private float speed;
        private float dist;

        private Circle circle;

        public Tank(float x, float y, Circle circle) {
            this.position = new Vector2(x, y);
            this.texture = new Texture("tank.png");
            this.speed = 200.0f;
            this.circle=circle;
        }

        public void update(float dt) {
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                angle += 180.0f * dt;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                angle -= 180.0f * dt;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                position.x += speed * MathUtils.cosDeg(angle) * dt;
                position.y += speed * MathUtils.sinDeg(angle) * dt;
                if (position.x<40) position.x=40;
                if (position.x>1240) position.x=1240;
                if (position.y<40) position.y=40;
                if (position.y>680) position.y=680;
            }
            checkCollision();
        }
        private void checkCollision(){
            dist =(float)Math.sqrt((position.x-circle.position.x)*(position.x-circle.position.x) + (position.y-circle.position.y)*(position.y-circle.position.y));
            if (dist<50.0f) circle.setNewRandomPosition();
        }
        public void render(SpriteBatch batch) {
            batch.draw(texture, position.x - 40, position.y - 40, 40, 40, 80, 80, 1, 1, angle, 0, 0, 80, 80, false, false);
        }

        public void dispose() {
            texture.dispose();
        }
    }

    private static class Circle {
        private Vector2 position;
        private Vector2 newPosition;
        private Texture texture;
        private Random random;

        private float speed;

        public Circle(float x, float y) {
            this.position = new Vector2(x, y);
            this.texture = new Texture("circle.png");
            random = new Random();
            speed = 300.0f;
            newPosition = new Vector2();
            setNewRandomPosition();
        }

        public void update(float dt) {
            if (newPosition.x-position.x>5.0f) {
                position.x+=speed*dt;
            }
            if (newPosition.x-position.x<5.0f) {
                position.x-=speed*dt;
            }
            if (newPosition.y-position.y>5.0f) {
                position.y+=speed*dt;
            }
            if (newPosition.y-position.y<5.0f) {
                position.y-=speed*dt;
            }
        }

        public void setNewRandomPosition(){
            newPosition.x=random.nextFloat()*1230 + 25;
            newPosition.y=random.nextFloat()*670 + 25;
        }

        public void render(SpriteBatch batch) {
            batch.draw(texture, position.x - 25, position.y - 25, 25, 25);
        }

        public void dispose() {
            texture.dispose();
        }
    }

    private SpriteBatch batch;
    private Tank tank;
    private Circle circle;

    @Override
    public void create() {
        batch = new SpriteBatch();
        circle = new Circle(500, 500);
        tank = new Tank(200, 200, circle);

    }

    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();
        update(dt);
        Gdx.gl.glClearColor(0, 0.4f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        circle.render(batch);
        tank.render(batch);
        batch.end();
    }

    public void update(float dt) {
        circle.update(dt);
        tank.update(dt);
    }

    @Override
    public void dispose() {
        batch.dispose();
        tank.dispose();
        circle.dispose();
    }

    // Домашнее задание:
    // - Задать координаты точки, и нарисовать в ней круг (любой круг, радиусом пикселей 50)
    // - Если "танк" подъедет вплотную к кругу, то круг должен переместиться в случайную точку
    // - * Нельзя давать танку заезжать за экран
}