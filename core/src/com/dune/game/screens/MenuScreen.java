package com.dune.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.dune.game.core.MenuRenderer;

public class MenuScreen extends AbstractScreen {

    MenuRenderer menuRenderer;

    public MenuScreen(SpriteBatch batch) { super(batch); }

    @Override
    public void show() {
        this.menuRenderer = new MenuRenderer(batch);
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0.4f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        menuRenderer.render();
    }

    public void update(float dt) {
        menuRenderer.getStage().act(dt);
    }

    @Override
    public void dispose() {
    }
}