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

public class BaseLogic {
    protected Owner ownerType;
    protected GameController gc;
    protected int money;
    protected int unitsCount;
    protected int unitsMaxCount;
    private BattleMap map;
    private Vector2 nearestResourcePosition;
    private Vector2 tmp = new Vector2();
    protected Vector2 destination;

    protected List<BattleTank> tmpAiBattleTanks;
    protected List<Harvester> tmpPlayerHarvesters;
    protected List<BattleTank> tmpPlayerBattleTanks;
    protected List<Harvester> tmpAiHarvesters;
    protected List<Building> buildings;
    protected BattleMap battleMap;


    public void addMoney(int amount) {
        this.money += amount;
    }

    public int getMoney() {
        return money;
    }

    public int getUnitsCount() {
        return unitsCount;
    }

    public int getUnitsMaxCount() {
        return unitsMaxCount;
    }

    public Owner getOwnerType() {
        return ownerType;
    }

    public BaseLogic(GameController gc) {
        this.gc = gc;
        this.ownerType = Owner.AI;
        this.tmpAiBattleTanks = new ArrayList<>();
        this.tmpAiHarvesters = new ArrayList<>();
        this.tmpPlayerHarvesters = new ArrayList<>();
        this.tmpPlayerBattleTanks = new ArrayList<>();
        this.buildings = new ArrayList<>();
    }

    public void setup() {
        this.money = 100;
        this.unitsCount = 10;
        this.unitsMaxCount = 100;
        this.buildings = gc.getBuildingsController().getActiveList();
        this.battleMap = gc.getMap();
    }

    public void decreaseMoney(int value){
        money-=value;
    }

    public Vector2 getNearestResourcePosition(AbstractUnit unit) {
        nearestResourcePosition = new Vector2(BattleMap.MAP_WIDTH_PX * 2, BattleMap.MAP_HEIGHT_PX * 2);
        int blockX = BattleMap.COLUMNS_COUNT;
        int blockY = BattleMap.ROWS_COUNT;
        map = gc.getMap();
        for (int cellY = 0; cellY < BattleMap.ROWS_COUNT; cellY++) {
            for (int cellX = 0; cellX < BattleMap.COLUMNS_COUNT; cellX++) {
                tmp.set(cellX * BattleMap.CELL_SIZE, cellY * BattleMap.CELL_SIZE);
                if (map.getResourceCount(tmp) > 0) {
                    if (map.getApplicant(cellX, cellY) == null || (map.getApplicant(cellX, cellY).getBaseLogic() != unit.getBaseLogic() || unit == map.getApplicant(cellX, cellY))) {
                        float dstToCurrentResource = unit.getPosition().dst(tmp);
                        if (dstToCurrentResource < unit.getPosition().dst(nearestResourcePosition)) {
                            nearestResourcePosition.set(tmp);
                            blockX = cellX;
                            blockY = cellY;
                        }
                    }
                }
            }
        }
        if (blockX<BattleMap.COLUMNS_COUNT) {
            map.setApplicant(unit, blockX, blockY);
            return nearestResourcePosition;
        } else if (unit.getOwnerType()==Owner.PLAYER) System.out.println(destination);
        return null;
    }


    public void harvesterProcessing(Harvester harvester) {
        if (!harvester.isBusy()) {
            if (!harvester.isOverload()) {
                destination = getNearestResourcePosition(harvester);
                if (destination!=null) {
                    destination.add((float) BattleMap.CELL_SIZE / 2, (float) BattleMap.CELL_SIZE / 2);
                    harvester.setBusy(true);
                    harvester.commandMoveTo(destination);
                }
            } else {
                for (int i = 0; i < buildings.size(); i++) {
                    Building building = buildings.get(i);
                    if (building.getOwnerLogic() == this && building.getType() == Building.Type.STOCK) {
                        float x = building.getCellX() * BattleMap.CELL_SIZE;
                        float y = building.getCellY() * BattleMap.CELL_SIZE;
                        destination.set(x + (float) BattleMap.CELL_SIZE / 2, y - (float) BattleMap.CELL_SIZE / 2);
                        harvester.setBusy(true);
                        harvester.commandMoveTo(destination);
                    }
                }
            }
        }
    }

    public void update(float dt) {
        gc.getUnitsController().collectTanks(tmpAiBattleTanks, gc.getUnitsController().getAiUnits(), UnitType.BATTLE_TANK);
        gc.getUnitsController().collectTanks(tmpPlayerHarvesters, gc.getUnitsController().getPlayerUnits(), UnitType.HARVESTER);
        gc.getUnitsController().collectTanks(tmpPlayerBattleTanks, gc.getUnitsController().getPlayerUnits(), UnitType.BATTLE_TANK);
        gc.getUnitsController().collectTanks(tmpAiHarvesters, gc.getUnitsController().getAiUnits(), UnitType.HARVESTER);
    }
}
