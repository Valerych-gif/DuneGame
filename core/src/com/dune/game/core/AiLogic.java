package com.dune.game.core;

import com.badlogic.gdx.math.Vector2;
import com.dune.game.core.units.AbstractUnit;
import com.dune.game.core.units.UnitType;

public class AiLogic {

    private GameController gc;
    private int money;
    private int unitsCount;
    private int unitsMaxCount;


    public AiLogic(GameController gc) {
        this.gc = gc;
        this.money = 1000;
        this.unitsMaxCount = 100;
    }

    public void update(float dt) {
        for (int i = 0; i < gc.getUnitsController().getAiUnits().size(); i++) {
            AbstractUnit aiUnit = gc.getUnitsController().getAiUnits().get(i);
            unitProcessing(aiUnit);
        }
        this.unitsCount=gc.getUnitsController().getAiUnits().size();
    }

    public void unitProcessing(AbstractUnit unit) {
        if (unit.getUnitType() == UnitType.HARVESTER) {
            if (!(gc.getMap().getResourceCount(unit.getPosition())>0&&unit.getContainer()<unit.getContainerCapacity())) {
                Vector2 destination = gc.getUnitsController().getNearestResourcePosition(unit);
                if (destination != null) {
                    destination.add((float) BattleMap.CELL_SIZE / 2, (float) BattleMap.CELL_SIZE / 2);
                    unit.commandMoveTo(destination);
                }
            }
            if (unit.getContainer()==unit.getContainerCapacity()){
                unit.commandMoveTo(gc.getMap().getAiBase().getPosition());
            }
            if (unit.getPosition().dst(gc.getMap().getAiBase().getPosition())< BattleMap.Base.SIZE){
                money+=unit.emptyContainer();
            }
            return;
        }
        if (unit.getUnitType() == UnitType.BATTLE_TANK) {
            AbstractUnit playerUnit = gc.getUnitsController().getNearestPlayerUnit(unit.getPosition());
            if (playerUnit != null) {
                unit.commandAttack(playerUnit);
            }
        }
    }

}
