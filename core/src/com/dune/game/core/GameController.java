package com.dune.game.core;

import com.badlogic.gdx.math.Vector2;

public class GameController {
    private BattleMap map;
    private ProjectilesController projectilesController;
    private Tank tank;
    private Vector2 tmp;

    public Tank getTank() {
        return tank;
    }

    public ProjectilesController getProjectilesController() {
        return projectilesController;
    }

    public BattleMap getMap() {
        return map;
    }

    // Инициализация игровой логики
    public GameController(int screenWidth, int screenHeight) {
        Assets.getInstance().loadAssets();
        this.map = new BattleMap(screenWidth, screenHeight);
        this.projectilesController = new ProjectilesController(this);
        this.tank = new Tank(this, 200, 200);
        tmp = new Vector2();
    }

    public void update(float dt) {
        tank.update(dt);
        projectilesController.update(dt);
        map.update(dt);
        checkCollisions(dt);
    }

    public void checkCollisions(float dt) {

        map.checkCollisions(tank);

    }
}
