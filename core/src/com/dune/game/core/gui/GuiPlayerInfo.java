package com.dune.game.core.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.StringBuilder;
import com.dune.game.core.GameController;
import com.dune.game.core.users_logic.PlayerLogic;
import com.dune.game.screens.utils.Assets;

public class GuiPlayerInfo {

    private GameController gc;
    private Stage stage;
    private PlayerLogic playerLogic;
    private Label money;
    private Label unitsCount;
    private StringBuilder tmpSB;
    private Skin skin;
    private Group playerInfoGroup;
    private BitmapFont font14;

    public GuiPlayerInfo(GameController gc) {
        this.gc = gc;
        this.skin = new Skin(Assets.getInstance().getAtlas());
        this.font14 = Assets.getInstance().getAssetManager().get("fonts/font14.ttf");
        this.playerLogic = gc.getPlayerLogic();
        this.playerInfoGroup = new Group();
        this.tmpSB = new StringBuilder();
    }

    public void setup() {
        this.stage=gc.getStage();
        Label.LabelStyle labelStyle = new Label.LabelStyle(font14, Color.WHITE);
        skin.add("simpleLabel", labelStyle);
        this.money = new Label("", skin, "simpleLabel");
        this.unitsCount = new Label("", skin, "simpleLabel");
        this.money.setPosition(10, 10);
        this.unitsCount.setPosition(210, 10);
        playerInfoGroup.addActor(money);
        playerInfoGroup.addActor(unitsCount);
        playerInfoGroup.setPosition(0, 700);
        stage.addActor(playerInfoGroup);
    }

    public void update(float dt) {
        tmpSB.clear();
        tmpSB.append("MINERALS: ").append(playerLogic.getMoney());
        money.setText(tmpSB);
        tmpSB.clear();
        tmpSB.append("UNITS: ").append(playerLogic.getUnitsCount()).append(" / ").append(playerLogic.getUnitsMaxCount());
        unitsCount.setText(tmpSB);
    }
}
