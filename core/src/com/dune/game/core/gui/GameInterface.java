package com.dune.game.core.gui;

import com.dune.game.core.GameController;

public class GameInterface {
    private GameController gc;

    private GameUserInterface gameUserInterface;
    private GuiPlayerInfo guiPlayerInfo;
    private UnitsPanel unitsPanel;

    public GameInterface(GameController gc) {

        this.gameUserInterface = new GameUserInterface(gc);
        this.guiPlayerInfo = new GuiPlayerInfo(gc);
        this.unitsPanel = new UnitsPanel(gc);
    }

    public void setup(){
        gameUserInterface.setup();
        guiPlayerInfo.setup();
        unitsPanel.setup();
    }

    public void update (float dt){
        gameUserInterface.update(dt);
        guiPlayerInfo.update(dt);
        unitsPanel.update(dt);
    }
}
