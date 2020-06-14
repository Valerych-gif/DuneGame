package com.dune.game.core.users_logic;

import com.dune.game.map.BattleMap;
import com.dune.game.core.GameController;
import com.dune.game.core.units.AbstractUnit;
import com.dune.game.core.units.BattleTank;
import com.dune.game.core.units.Harvester;
import com.dune.game.core.units.types.Owner;

import java.util.ArrayList;
import java.util.List;

public class AiLogic extends BaseLogic {
    private float timer;

    private List<BattleTank> tmpAiBattleTanks;
    private List<Harvester> tmpPlayerHarvesters;
    private List<BattleTank> tmpPlayerBattleTanks;
    private AbstractUnit tmpUnit;

    public AiLogic(GameController gc){
        super(gc);
        this.gc = gc;
        this.money = 1000;
        this.unitsCount = 10;
        this.unitsMaxCount = 100;
        this.ownerType = Owner.AI;
        this.tmpAiBattleTanks = new ArrayList<>();
        this.tmpPlayerHarvesters = new ArrayList<>();
        this.tmpPlayerBattleTanks = new ArrayList<>();
        this.timer = 10000.0f;
    }

    public void update(float dt) {
        timer+=dt;
        if (timer > 2.0f) {

            timer = 0.0f;
            super.update(dt);
            for (int i = 0; i < tmpAiBattleTanks.size(); i++) {
                BattleTank aiBattleTank = tmpAiBattleTanks.get(i);
                tmpUnit = findNearestTarget(aiBattleTank, tmpPlayerBattleTanks);
                if (tmpUnit==null){
                    tmpUnit = findNearestTarget(aiBattleTank, tmpPlayerHarvesters);
                }
                aiBattleTank.commandAttack(tmpUnit);
            }
            for (int i = 0; i < tmpAiHarvesters.size(); i++) {
                Harvester harvester = tmpAiHarvesters.get(i);
                harvesterProcessing(harvester);
            }
            this.unitsCount = gc.getUnitsController().getAiUnits().size();
            if (tmpAiBattleTanks.size()==0||tmpAiHarvesters.size()/tmpAiBattleTanks.size()>2) {
                if (money >= 50 && unitsCount < unitsMaxCount) {
                    gc.getUnitsController().createBattleTank(this, BattleMap.MAP_WIDTH_PX - 100, BattleMap.MAP_HEIGHT_PX - 100);
                }
            } else {
                if (money >= 100 && unitsCount < unitsMaxCount) {
                    gc.getUnitsController().createHarvester(this, BattleMap.MAP_WIDTH_PX - 100, BattleMap.MAP_HEIGHT_PX - 100);
                }
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
}
