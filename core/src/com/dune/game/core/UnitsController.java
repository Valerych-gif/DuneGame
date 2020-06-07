package com.dune.game.core;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.dune.game.core.units.AbstractUnit;
import com.dune.game.core.units.BattleTank;
import com.dune.game.core.units.Owner;
import com.dune.game.screens.ScreenManager;

import java.util.ArrayList;
import java.util.List;

public class UnitsController {
    private GameController gc;
    private BattleTanksController battleTanksController;
    private HarvestersController harvestersController;
    private BattleMap battleMap;
    private List<AbstractUnit> units;
    private List<AbstractUnit> playerUnits;
    private List<AbstractUnit> aiUnits;

    Vector2 nearestResourcePosition;
    Vector2 tmp;

    public List<AbstractUnit> getUnits() {
        return units;
    }

    public List<AbstractUnit> getPlayerUnits() {
        return playerUnits;
    }

    public List<AbstractUnit> getAiUnits() {
        return aiUnits;
    }

    public UnitsController(GameController gc) {
        this.gc = gc;
        this.battleTanksController = new BattleTanksController(gc);
        this.harvestersController = new HarvestersController(gc);
        this.battleMap = gc.getMap();
        this.units = new ArrayList<>();
        this.playerUnits = new ArrayList<>();
        this.aiUnits = new ArrayList<>();
        nearestResourcePosition = new Vector2();
        tmp = new Vector2();
        setup();
    }

    private void setup() {
        for (int i = 0; i < 5; i++) {
            createHarvester(
                    Owner.PLAYER,
                    MathUtils.random(BattleMap.CELL_SIZE, BattleMap.CELL_SIZE*4),
                    MathUtils.random(BattleMap.CELL_SIZE, BattleMap.CELL_SIZE*4)
            );
        }

        for (int i = 0; i < 5; i++) {
            createHarvester(
                    Owner.AI,
                    MathUtils.random(BattleMap.MAP_WIDTH_PX-BattleMap.CELL_SIZE*4, BattleMap.MAP_WIDTH_PX-BattleMap.CELL_SIZE),
                    MathUtils.random(BattleMap.MAP_HEIGHT_PX-BattleMap.CELL_SIZE*4, BattleMap.MAP_HEIGHT_PX-BattleMap.CELL_SIZE)
            );
        }

    }

    public void buildUnit(Owner owner){
       if (MathUtils.random()<0.8f){
           gc.getUnitsController().createBattleTank(owner, MathUtils.random(80, 1200), MathUtils.random(80, 640));
       } else {
           gc.getUnitsController().createHarvester(owner, MathUtils.random(80, 1200), MathUtils.random(80, 640));
       }
    }

    public void createBattleTank(Owner owner, float x, float y) {
        battleTanksController.setup(x, y, owner);
    }

    public void createHarvester(Owner owner, float x, float y) {
        harvestersController.setup(x, y, owner);
    }

    public void update(float dt) {
        battleTanksController.update(dt);
        harvestersController.update(dt);
        units.clear();
        aiUnits.clear();
        playerUnits.clear();
        units.addAll(battleTanksController.getActiveList());
        units.addAll(harvestersController.getActiveList());
        for (int i = 0; i < units.size(); i++) {
            if (units.get(i).getOwnerType() == Owner.AI) {
                aiUnits.add(units.get(i));
            }
            if (units.get(i).getOwnerType() == Owner.PLAYER) {
                playerUnits.add(units.get(i));
            }
        }
    }

    public void render(SpriteBatch batch) {
        battleTanksController.render(batch);
        harvestersController.render(batch);
    }

    public AbstractUnit getNearestAiUnit(Vector2 point) {
        for (int i = 0; i < aiUnits.size(); i++) {
            AbstractUnit u = aiUnits.get(i);
            if (u.getPosition().dst(point) < 30) {
                return u;
            }
        }
        return null;
    }

    public AbstractUnit getNearestPlayerUnit(Vector2 point) {
        AbstractUnit nearestPlayerUnit = null;
        float dstToNearestPlayerUnit = 1_000_000f;
        for (int i = 0; i < playerUnits.size(); i++) {
            AbstractUnit u = playerUnits.get(i);
            float dstToCurrentUnit = u.getPosition().dst(point);
            if (dstToCurrentUnit < dstToNearestPlayerUnit) {
                dstToNearestPlayerUnit=dstToCurrentUnit;
                nearestPlayerUnit=u;
            }
        }
        return nearestPlayerUnit;
    }

    public Vector2 getNearestResourcePosition(AbstractUnit unit){
        nearestResourcePosition.set(BattleMap.MAP_WIDTH_PX*2, BattleMap.MAP_HEIGHT_PX*2);
        boolean isResourceFound =false;
        int blockX=BattleMap.COLUMNS_COUNT;
        int blockY=BattleMap.ROWS_COUNT;
        for (int cellY = 0; cellY < BattleMap.ROWS_COUNT; cellY++) {
            for (int cellX = 0; cellX < BattleMap.COLUMNS_COUNT; cellX++) {
                tmp.set(cellX*BattleMap.CELL_SIZE, cellY*BattleMap.CELL_SIZE);
                if (battleMap.getResourceCount(tmp)>0){
                    if (!checkApplicant(unit, cellX, cellY)) {
                        float dstToCurrentResource = unit.getPosition().dst(tmp);
                        if (unit.isActive() && dstToCurrentResource < unit.getPosition().dst(nearestResourcePosition)) {
                            nearestResourcePosition.set(tmp);
                            blockX = cellX;
                            blockY = cellY;
                            isResourceFound = true;
                        }
                    }
                }
            }
        }
        if (isResourceFound) {
            battleMap.setApplicant(unit, blockX, blockY);
            return nearestResourcePosition;
        }
        return null;
    }

    private boolean checkApplicant(AbstractUnit unit, int cellX, int cellY){
        return (
                battleMap.getApplicant(cellX, cellY)!=null
                &&unit.getOwnerType().equals(battleMap.getApplicant(cellX, cellY).getOwnerType()))
                &&!unit.equals(battleMap.getApplicant(cellX, cellY)
        );
    }
}