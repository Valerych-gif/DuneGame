package com.dune.game.core.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.dune.game.core.Assets;
import com.dune.game.core.BattleMap;
import com.dune.game.core.objects.GameObject;
import com.dune.game.core.objects.Projectile;
import com.dune.game.core.objects.Unit;

import java.util.ArrayList;
import java.util.List;

public class GameController {
    private BattleMap map;
    private ProjectilesController pc;
    private UnitsController uc;
    private BattleTanksController btc;
    private HarvestersController hc;
    private Vector2 tmp;
    private Vector2 selectionStart;

    private List<Unit> selectedUnits;

    public UnitsController getUc() {
        return uc;
    }

    public BattleTanksController getBtc() {
        return btc;
    }

    public HarvestersController getHc() {
        return hc;
    }

    public List<Unit> getSelectedUnits() {
        return selectedUnits;
    }

    public ProjectilesController getPc() {
        return pc;
    }

    public BattleMap getMap() {
        return map;
    }

    // Инициализация игровой логики
    public GameController() {
        Assets.getInstance().loadAssets();
        this.tmp = new Vector2();
        this.selectionStart = new Vector2();
        this.selectedUnits = new ArrayList<>();
        this.map = new BattleMap();
        this.pc = new ProjectilesController(this);
        this.uc = new UnitsController(this);
        this.btc = new BattleTanksController(this);
        this.hc = new HarvestersController(this);
        uc.setup();

        prepareInput();
    }

    public void update(float dt) {
        uc.update(dt);
        pc.update(dt);
        map.update(dt);
        checkCollisions(dt);
    }

    public boolean isUnitSelected(Unit unit) {
        return selectedUnits.contains(unit);
    }

    public void checkCollisions(float dt) {
        uc.checkCollisions(dt);
        for (int i = 0; i < uc.getUnitsActiveList().size(); i++) {
            Unit t = uc.getUnitsActiveList().get(i);
            for (int j = 0; j < pc.getActiveList().size(); j++) {
                Projectile p = pc.getActiveList().get(j);
                if (t.getPosition().dst(p.getPosition())<30){
                    t.decreaseHP(2);
                    p.deactivate();
                }
            }
        }
    }

    public void prepareInput() {
        InputProcessor ip = new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                if (button == Input.Buttons.LEFT) {
                    selectionStart.set(screenX, 720 - screenY);
                }
                return true;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                if (button == Input.Buttons.LEFT) {
                    tmp.set(Gdx.input.getX(), 720 - Gdx.input.getY());

                    if (tmp.x < selectionStart.x) {
                        float t = tmp.x;
                        tmp.x = selectionStart.x;
                        selectionStart.x = t;
                    }
                    if (tmp.y > selectionStart.y) {
                        float t = tmp.y;
                        tmp.y = selectionStart.y;
                        selectionStart.y = t;
                    }

                    selectedUnits.clear();
                    if (Math.abs(tmp.x - selectionStart.x) > 20 & Math.abs(tmp.y - selectionStart.y) > 20) {
                        for (int i = 0; i < uc.getUnitsActiveList().size(); i++) {
                            Unit t = uc.getUnitsActiveList().get(i);
                            if (t.getOwnerType() == GameObject.Owner.PLAYER && t.getPosition().x > selectionStart.x && t.getPosition().x < tmp.x
                                    && t.getPosition().y > tmp.y && t.getPosition().y < selectionStart.y
                            ) {
                                selectedUnits.add(t);
                            }
                        }
                    } else {
                        for (int i = 0; i < uc.getUnitsActiveList().size(); i++) {
                            Unit t = uc.getUnitsActiveList().get(i);
                            if (t.getPosition().dst(tmp) < 30.0f) {
                                selectedUnits.add(t);
                            }
                        }
                    }
                }
                return true;
            }
        };
        Gdx.input.setInputProcessor(ip);
    }
}
