package com.dune.game.core.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.dune.game.core.GameController;
import com.dune.game.screens.ScreenManager;
import com.dune.game.screens.utils.Assets;

public class GameUserInterface{
    private Skin skin;
    private Stage stage;
    private GameController gc;

    public GameUserInterface(GameController gc) {
        this.gc = gc;
    }

    public void setup() {

        this.stage=gc.getStage();
        this.skin = new Skin(Assets.getInstance().getAtlas());
        BitmapFont font14 = Assets.getInstance().getAssetManager().get("fonts/font14.ttf");
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle(
                skin.getDrawable("smButton"), null, null, font14);
        final TextButton menuBtn = new TextButton("Menu", textButtonStyle);
        menuBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.MENU);
            }
        });

        Group menuGroup = new Group();
        menuBtn.setPosition(0, 0);
        menuGroup.addActor(menuBtn);
        menuGroup.setPosition(900, 680);

        Label.LabelStyle labelStyle = new Label.LabelStyle(font14, Color.WHITE);
        skin.add("simpleLabel", labelStyle);
        stage.addActor(menuGroup);
    }

    public void update(float dt) {
    }
}
