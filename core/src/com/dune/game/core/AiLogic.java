package com.dune.game.core;

import com.badlogic.gdx.math.Vector2;
import com.dune.game.core.units.AbstractUnit;
import com.dune.game.core.units.Owner;
import com.dune.game.core.units.UnitType;

public class AiLogic {

    private GameController gc;
    private int money;
    private int unitsCount;
    private int unitsMaxCount;
    Vector2 destination;


    public AiLogic(GameController gc) {
        this.gc = gc;
        this.money = 100;
        this.unitsMaxCount = 100;
        this.destination = new Vector2();
    }

    public void update(float dt) {
        for (int i = 0; i < gc.getUnitsController().getAiUnits().size(); i++) {
            AbstractUnit aiUnit = gc.getUnitsController().getAiUnits().get(i);
            unitProcessing(aiUnit);
        }
        this.unitsCount=gc.getUnitsController().getAiUnits().size();
        if (money>=100&&unitsCount<unitsMaxCount){
            gc.getUnitsController().buildUnit(Owner.AI);

        }
    }

    public void decreaseMoney(int value){
        money-=value;
    }

    public void unitProcessing(AbstractUnit unit) {
        destination = gc.getUnitsController().getNearestResourcePosition(unit);
        if (unit.getUnitType().equals(UnitType.HARVESTER)) {
            if (!(gc.getMap().getResourceCount(unit.getPosition())>0&&unit.getContainer()<unit.getContainerCapacity())) {

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
                destination.set(unit.getPosition());
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
