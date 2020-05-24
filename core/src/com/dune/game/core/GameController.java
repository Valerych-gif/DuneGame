package com.dune.game.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class GameController {
    private BattleMap map;
    private Tank tank;
    private Projectile projectile;

    public Tank getTank() {
        return tank;
    }

    public BattleMap getMap() {
        return map;
    }

    public Projectile getProjectile() { return projectile; }

    // Инициализация игровой логики
    public GameController() {
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("game.pack"));
        this.map = new BattleMap(atlas);
        this.projectile = new Projectile(atlas);
        this.tank = new Tank(atlas,200, 200, projectile);
    }

    public void update(float dt) {
        tank.update(dt);
        projectile.update(dt);
        checkCollisions(dt);
    }

    public void checkCollisions(float dt) {
        if (projectile.getPosition().x > 1280||projectile.getPosition().x<0||projectile.getPosition().y > 720||projectile.getPosition().y<0) projectile.destroy();
    }
}
