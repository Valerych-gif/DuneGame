package com.dune.game.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dune.game.core.GameController;
import com.dune.game.core.WorldRenderer;

public class GameScreen extends AbstractScreen {
    public final static int WIDTH = 16;
    public final static int HEIGHT = 9;

    private SpriteBatch batch;
    private GameController gameController;
    private WorldRenderer worldRenderer;

    public GameScreen(SpriteBatch batch) {
        this.batch = batch;
    }

    @Override
    public void show() {
        this.gameController = new GameController(WIDTH, HEIGHT);
        this.worldRenderer = new WorldRenderer(batch, gameController);
    }

    @Override
    public void render(float delta) {
        gameController.update(delta);
        worldRenderer.render();
    }

    @Override
    public void dispose() {
    }
}
