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
    // 1. Разбор кода, в домашке задавайте вопросы что не ясно
    // 2. Выбор активного танка, управление ведется только активным
    // 3. Рядом с танком должна отрисовываться загруженность контейнера
    // 4. Ограничить размер контейнера 50 единицами
    // 5. * Реализовать столкновения, танки не могут заезжать друг в друга

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