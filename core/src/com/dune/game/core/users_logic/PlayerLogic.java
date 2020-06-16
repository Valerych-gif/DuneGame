package com.dune.game.core.users_logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.dune.game.core.GameController;
import com.dune.game.core.interfaces.Targetable;
import com.dune.game.core.units.AbstractUnit;
import com.dune.game.core.units.Harvester;
import com.dune.game.core.units.types.Owner;
import com.dune.game.core.units.types.TargetType;
import com.dune.game.core.units.types.UnitType;

import java.util.ArrayList;
import java.util.List;

public class PlayerLogic extends BaseLogic {

    private float timer;

    public PlayerLogic(GameController gc) {
        super(gc);
        this.timer = 10000.0f;
        this.ownerType = Owner.PLAYER;
    }

    public void update(float dt) {

        timer+=dt;
        this.unitsCount = gc.getUnitsController().getPlayerUnits().size();

        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
            for (int i = 0; i < gc.getSelectedUnits().size(); i++) {
                AbstractUnit u = gc.getSelectedUnits().get(i);
                if (u.getOwnerType() == Owner.PLAYER) {
                    u.setBusy(true);
                    unitProcessing(u);
                }
            }
        } else if (timer > 2.0f){
            timer=0.0f;
            for (int i = 0; i < unitsCount; i++) {
                AbstractUnit u = gc.getUnitsController().getPlayerUnits().get(i);
                unitSelfCommand(u);
            }
        }
    }

    private void unitProcessing(AbstractUnit unit) {
        if (unit.getUnitType() == UnitType.HARVESTER) {
            unit.commandMoveTo(gc.getMouse(), true);
            return;
        }

        if (unit.getUnitType() == UnitType.BATTLE_TANK) {
            Targetable target;
            target= getAiNearestTarget(gc.getMouse());
            if (target == null) {
                unit.commandMoveTo(gc.getMouse(), true);
            } else {
                unit.commandAttack(target);
            }
        }
    }

    public Targetable getAiNearestTarget(Vector2 point) {
        List<Targetable> targets = new ArrayList<>();
        targets.addAll(collectTargets(buildings, TargetType.BUILDING));
        targets.addAll(collectTargets(aiUnits, TargetType.UNIT));
        for (int i = 0; i < targets.size(); i++) {
            Targetable t = targets.get(i);
            if (t.getPosition().dst(point) < 30) {
                return t;
            }
        }
        return null;
    }

    private void unitSelfCommand(AbstractUnit unit){
        if (unit.getUnitType()==UnitType.HARVESTER) {
            harvesterProcessing((Harvester)unit);
        }
    }
}