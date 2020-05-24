package com.dune.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dune.game.screens.GameScreen;

public class DuneGame extends Game {
    private SpriteBatch batch;
    private GameScreen gameScreen;

    // Домашнее задание:
    // 1. Разбор кода, в домашке задавайте вопросы что не ясно
    // 2. На поле должны быть рассыпаны ресурсы, танк должен их собирать
    // когда по ним проезжает

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