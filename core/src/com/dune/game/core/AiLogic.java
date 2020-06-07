package com.dune.game.core;

import com.badlogic.gdx.math.Vector2;
import com.dune.game.core.units.AbstractUnit;
import com.dune.game.core.units.UnitType;

public class AiLogic {


    private GameController gc;

    public AiLogic(GameController gc) {

        this.gc = gc;
    }

    public void update(float dt) {
        for (int i = 0; i < gc.getUnitsController().getAiUnits().size(); i++) {
            AbstractUnit aiUnit = gc.getUnitsController().getAiUnits().get(i);
            unitProcessing(aiUnit);
        }
    }

    public void unitProcessing(AbstractUnit unit) {
        if (unit.getUnitType() == UnitType.HARVESTER) {
            Vector2 destination = gc.getUnitsController().getNearestResourcePosition(unit);
            if (destination!=null) {
                destination.add((float) BattleMap.CELL_SIZE / 2, (float) BattleMap.CELL_SIZE / 2);
                unit.commandMoveTo(destination);
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
