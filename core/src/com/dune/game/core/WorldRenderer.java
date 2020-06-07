package com.dune.game.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.dune.game.screens.ScreenManager;

public class WorldRenderer {
    private SpriteBatch batch;
    private BitmapFont font32;
    private GameController gc;
    private TextureRegion selectorTexture;

    public WorldRenderer(SpriteBatch batch, GameController gc) {
        this.batch = batch;
        this.font32 = Assets.getInstance().getAssetManager().get("fonts/font32.ttf");
        this.selectorTexture = Assets.getInstance().getAtlas().findRegion("selector");
        this.gc = gc;
    }

    public void render() {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        ScreenManager.getInstance().pointCameraTo(gc.getPointOfView());
        batch.begin();
        gc.getMap().render(batch);
        gc.getUnitsController().render(batch);
        gc.getProjectilesController().render(batch);
        gc.getParticleController().render(batch);
        drawSelectionFrame();
        batch.end();
        ScreenManager.getInstance().resetCamera();
        gc.getStage().draw();
    }

    public void drawSelectionFrame() {
        if (gc.getSelectionStart().x > 0 && gc.getSelectionStart().y > 0) {
            batch.draw(selectorTexture, gc.getMouse().x - 8, gc.getMouse().y - 8);
            batch.draw(selectorTexture, gc.getMouse().x - 8, gc.getSelectionStart().y - 8);
            batch.draw(selectorTexture, gc.getSelectionStart().x - 8, gc.getSelectionStart().y - 8);
            batch.draw(selectorTexture, gc.getSelectionStart().x - 8, gc.getMouse().y - 8);
            float minX = Math.min(gc.getSelectionStart().x, gc.getMouse().x);
            float maxX = Math.max(gc.getSelectionStart().x, gc.getMouse().x);
            float minY = Math.min(gc.getSelectionStart().y, gc.getMouse().y);
            float maxY = Math.max(gc.getSelectionStart().y, gc.getMouse().y);
            for (float i = minX; i < maxX; i += 30.0f) {
                batch.draw(selectorTexture, i - 4, minY - 4, 8, 8);
                batch.draw(selectorTexture, i - 4, maxY - 4, 8, 8);
            }
            for (float i = minY; i < maxY; i += 30.0f) {
                batch.draw(selectorTexture, minX - 4, i - 4, 8, 8);
                batch.draw(selectorTexture, maxX - 4, i - 4, 8, 8);
            }
        }
    }
}
