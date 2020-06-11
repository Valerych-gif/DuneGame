package com.dune.game.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.dune.game.core.units.AbstractUnit;
import com.dune.game.core.units.Owner;
import com.dune.game.core.units.UnitType;

public class PlayerLogic {
    private GameController gc;
    private int money;
    private int unitsCount;
    private int unitsMaxCount;
    Vector2 destination;

    public int getMoney() {
        return money;
    }

    public int getUnitsCount() {
        return unitsCount;
    }

    public int getUnitsMaxCount() {
        return unitsMaxCount;
    }

    public PlayerLogic(GameController gc) {
        this.gc = gc;
        this.destination= new Vector2();
    }

    public void setup() {
        this.money = 100;
        this.unitsMaxCount = 100;
    }

    public void update(float dt) {

        this.unitsCount = gc.getUnitsController().getPlayerUnits().size();

        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
            for (int i = 0; i < gc.getSelectedUnits().size(); i++) {
                AbstractUnit u = gc.getSelectedUnits().get(i);
                if (u.getOwnerType() == Owner.PLAYER) {
                    unitProcessing(u);
                }
            }
        } else {
            for (int i = 0; i < unitsCount; i++) {
                AbstractUnit u = gc.getUnitsController().getPlayerUnits().get(i);
                if (u.getPosition().dst(u.getDestination())<5.0f) {
                    unitSelfCommand(u);
                }
            }
        }
    }

    private void unitProcessing(AbstractUnit unit) {
        if (unit.getUnitType() == UnitType.HARVESTER) {
            unit.commandMoveTo(gc.getMouse());
            return;
        }

        if (unit.getUnitType() == UnitType.BATTLE_TANK) {
            AbstractUnit aiUnit = gc.getUnitsController().getNearestAiUnit(gc.getMouse());
            if (aiUnit == null) {
                unit.commandMoveTo(gc.getMouse());
            } else {
                unit.commandAttack(aiUnit);
            }
        }
    }

    private void unitSelfCommand(AbstractUnit unit){
        if (unit.getUnitType().equals(UnitType.HARVESTER)) {
            if (!(gc.getMap().getResourceCount(unit.getPosition()) > 0 && unit.getContainer() < unit.getContainerCapacity())) {
                Vector2 destination = gc.getUnitsController().getNearestResourcePosition(unit);
                if (destination!=null) {
                    destination.add((float) BattleMap.CELL_SIZE / 2, (float) BattleMap.CELL_SIZE / 2);
                    unit.commandMoveTo(destination);
                }
            }
            if (unit.getContainer() == unit.getContainerCapacity()) {
                destination.set(gc.getMap().getPlayerBase().getPosition());
                unit.commandMoveTo(destination);
            }
            if (unit.getPosition().dst(gc.getMap().getPlayerBase().getPosition()) < BattleMap.Base.SIZE) {
                money += unit.emptyContainer();
            }
        }
    }
}
