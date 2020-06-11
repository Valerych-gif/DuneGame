package com.dune.game.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.dune.game.screens.ScreenManager;

public class MenuRenderer {

    private BitmapFont font14;
    private SpriteBatch batch;
    private Stage stage;
    private TextureRegion selectorTexture;

    public Stage getStage() {
        return stage;
    }

    public MenuRenderer(SpriteBatch batch) {
        this.batch=batch;
        this.font14 = Assets.getInstance().getAssetManager().get("fonts/font14.ttf");
        this.selectorTexture = Assets.getInstance().getAtlas().findRegion("selector");
        this.stage=new Stage(ScreenManager.getInstance().getViewport(), batch);
    }

    public void render() {
        Gdx.gl.glClearColor(0.0f, 1.0f, 0.0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//        batch.begin();
//        batch.end();
//        ScreenManager.getInstance().resetCamera();
        drawGui();
        stage.draw();
    }

    private void drawGui(){
        Gdx.input.setInputProcessor(stage);
        Skin skin = new Skin();
        skin.addRegions(Assets.getInstance().getAtlas());

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle(
                skin.getDrawable("smButton"), null, null, font14);

        final TextButton menuBtn = new TextButton("New Game", textButtonStyle);
        menuBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAME);
            }
        });

        final TextButton exitBtn = new TextButton("Exit", textButtonStyle);
        exitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        Group menuGroup = new Group();
        menuBtn.setPosition(0, 50);
        exitBtn.setPosition(0, 0);
        menuGroup.addActor(menuBtn);
        menuGroup.addActor(exitBtn);
        menuGroup.setPosition(0, ScreenManager.WORLD_HEIGHT-100);
        stage.addActor(menuGroup);
        skin.dispose();
    }
}
