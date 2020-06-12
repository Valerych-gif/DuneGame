package com.dune.game.core.users_logic;

import com.badlogic.gdx.math.Vector2;
import com.dune.game.core.BattleMap;
import com.dune.game.core.Building;
import com.dune.game.core.GameController;
import com.dune.game.core.units.AbstractUnit;
import com.dune.game.core.units.BattleTank;
import com.dune.game.core.units.Harvester;
import com.dune.game.core.units.types.Owner;
import com.dune.game.core.units.types.UnitType;

import java.util.ArrayList;
import java.util.List;

public class AiLogic extends BaseLogic {
    private float timer;

    private List<BattleTank> tmpAiBattleTanks;
    private List<Harvester> tmpPlayerHarvesters;
    private List<BattleTank> tmpPlayerBattleTanks;
    private List<Harvester> tmpAiHarvesters;
    private List<Building> buildings;
    private BattleMap battleMap;

    Vector2 destination;

    public AiLogic(GameController gc) {
        this.gc = gc;
        this.ownerType = Owner.AI;
        destination = new Vector2();
        this.tmpAiBattleTanks = new ArrayList<>();
        this.tmpAiHarvesters = new ArrayList<>();
        this.tmpPlayerHarvesters = new ArrayList<>();
        this.tmpPlayerBattleTanks = new ArrayList<>();
    }

    public void setup() {
        this.money = 100;
        this.unitsCount = 10;
        this.unitsMaxCount = 100;
        this.buildings = gc.getBuildingsController().getActiveList();
        this.timer = 10000.0f;
        this.battleMap = gc.getMap();
    }

    public void update(float dt) {
        timer += dt;
        if (timer > 2.0f) {
            timer = 0.0f;
            gc.getUnitsController().collectTanks(tmpAiBattleTanks, gc.getUnitsController().getAiUnits(), UnitType.BATTLE_TANK);
            gc.getUnitsController().collectTanks(tmpPlayerHarvesters, gc.getUnitsController().getPlayerUnits(), UnitType.HARVESTER);
            gc.getUnitsController().collectTanks(tmpPlayerBattleTanks, gc.getUnitsController().getPlayerUnits(), UnitType.BATTLE_TANK);
            gc.getUnitsController().collectTanks(tmpAiHarvesters, gc.getUnitsController().getAiUnits(), UnitType.HARVESTER);
            for (int i = 0; i < tmpAiBattleTanks.size(); i++) {
                BattleTank aiBattleTank = tmpAiBattleTanks.get(i);
                aiBattleTank.commandAttack(findNearestTarget(aiBattleTank, tmpPlayerHarvesters));
            }
            for (int i = 0; i < tmpAiHarvesters.size(); i++) {
                Harvester harvester = tmpAiHarvesters.get(i);
                harvesterProcessing(harvester);
            }
            this.unitsCount = gc.getUnitsController().getAiUnits().size();
            if (money >= 50 && unitsCount < unitsMaxCount) {
                gc.getUnitsController().createBattleTank(this, BattleMap.MAP_WIDTH_PX - 100, BattleMap.MAP_HEIGHT_PX - 100);
                money -= 50;

            }
        }
    }

    public <T extends AbstractUnit> T findNearestTarget(AbstractUnit currentTank, List<T> possibleTargetList) {
        T target = null;
        float minDist = 1000000.0f;
        for (int i = 0; i < possibleTargetList.size(); i++) {
            T possibleTarget = possibleTargetList.get(i);
            float currentDst = currentTank.getPosition().dst(possibleTarget.getPosition());
            if (currentDst < minDist) {
                target = possibleTarget;
                minDist = currentDst;
            }
        }
        return target;
    }

    public void harvesterProcessing(Harvester harvester) {
        if (!harvester.isOverload()) {
            destination = getNearestResourcePosition(harvester);
            if (destination != null) {
                destination.add((float) BattleMap.CELL_SIZE / 2, (float) BattleMap.CELL_SIZE / 2);

            }
        } else {
            for (int i = 0; i < buildings.size(); i++) {
                Building building = buildings.get(i);
                if (building.getOwnerLogic() == this && building.getType() == Building.Type.STOCK) {
                    float x = building.getCellX() * BattleMap.CELL_SIZE;
                    float y = building.getCellY() * BattleMap.CELL_SIZE;
                    destination = new Vector2(x + (float) BattleMap.CELL_SIZE / 2, y - (float) BattleMap.CELL_SIZE/2);
                }
            }
        }
        harvester.commandMoveTo(destination);
    }

}
