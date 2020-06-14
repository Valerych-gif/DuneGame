package com.dune.game.core.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.dune.game.core.GameController;
import com.dune.game.core.controllers.UnitsController;
import com.dune.game.core.users_logic.BaseLogic;
import com.dune.game.screens.ScreenManager;
import com.dune.game.screens.utils.Assets;

import java.util.HashMap;
import java.util.Map;

public class UnitsPanel{

    private Map<Integer, ImageButton> buttons = new HashMap();
    private Stage stage;
    private GameController gc;
    private BaseLogic playerLogic;
    private UnitsController unitsController;
    private Skin skin;

    public Stage getStage() {
        return stage;
    }

    public UnitsPanel(GameController gc) {
        this.gc=gc;
        this.playerLogic = gc.getPlayerLogic();
        this.unitsController = gc.getUnitsController();
    }

    public void setup() {
        this.stage=gc.getStage();
        this.skin = new Skin(Assets.getInstance().getAtlas());

        ImageButton.ImageButtonStyle harvesterBtnStyle = new ImageButton.ImageButtonStyle(
                skin.getDrawable("harvesterBtn"), null, null, null, null, null);
        final ImageButton harvesterBtn = new ImageButton(harvesterBtnStyle);
        harvesterBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (playerLogic.getMoney() >= 100) {
                    unitsController.createHarvester(playerLogic, 2, 2);
                }
            }
        });


        ImageButton.ImageButtonStyle tankBtnStyle = new ImageButton.ImageButtonStyle(
                skin.getDrawable("tankBtn"), null, null, null, null, null);
        final ImageButton tankBtn = new ImageButton(tankBtnStyle);
        tankBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (playerLogic.getMoney() >= 50) {
                    unitsController.createBattleTank(playerLogic, 2, 2);
                }
            }
        });

        buttons.put(0, harvesterBtn);
        buttons.put(1, tankBtn);
    }

    public void update (float dt){
        Group unitsToBuy = new Group();
        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).setPosition(i*35, 0);
            unitsToBuy.addActor(buttons.get(i));
        }
        unitsToBuy.setPosition(20,650);
        stage.addActor(unitsToBuy);
    }
}
