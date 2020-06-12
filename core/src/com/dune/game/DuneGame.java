package com.dune.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dune.game.screens.GameScreen;
import com.dune.game.screens.ScreenManager;

public class DuneGame extends Game {
    private SpriteBatch batch;

    // Домашнее задание:
    // 1. Разобраться с кодом +
    // 2. Харвестеры AI должны собирать ресурсы и отвозить на базу +
    // 3. Игроки могут покупать юнитов. Ai - только боевые танки, игрок - и танки и харвестеры

    @Override
    public void create() {
        batch = new SpriteBatch();
        ScreenManager.getInstance().init(this, batch);
        ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAME);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        float dt = Gdx.graphics.getDeltaTime();
        getScreen().render(dt);
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}