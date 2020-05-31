package com.dune.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dune.game.screens.GameScreen;

public class DuneGame extends Game {
    private SpriteBatch batch;
    private GameScreen gameScreen;

    // Список идей:
    // - Добавить управления группами танков
    // - Добавить столкновения
    // - Добавить боевые танки и стрельбу

    // Домашнее задание:
    // 1. Разобраться с кодом
    // 2. Снаряды должны попадать по танкам
    // 3. Танк получает урон от снаряда, если хп упало до 0,
    // то танк уничтожается

    @Override
    public void create() {
        this.batch = new SpriteBatch();
        this.gameScreen = new GameScreen(batch);
        this.setScreen(gameScreen);
    }

    @Override
    public void render() {
        getScreen().render(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}