package com.dune.game.core.users_logic;

import com.badlogic.gdx.math.Vector2;
import com.dune.game.core.BattleMap;
import com.dune.game.core.GameController;
import com.dune.game.core.units.AbstractUnit;
import com.dune.game.core.units.types.Owner;

public class BaseLogic {
    protected Owner ownerType;
    protected GameController gc;
    protected int money;
    protected int unitsCount;
    protected int unitsMaxCount;
    private Vector2 nearestResourcePosition;
    private Vector2 tmp = new Vector2();

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

    public Vector2 getNearestResourcePosition(AbstractUnit unit){
        nearestResourcePosition=new Vector2(BattleMap.MAP_WIDTH_PX*2, BattleMap.MAP_HEIGHT_PX*2);
        boolean isResourceFound =false;
        int blockX=BattleMap.COLUMNS_COUNT;
        int blockY=BattleMap.ROWS_COUNT;
        BattleMap map = gc.getMap();
        for (int cellY = 0; cellY < BattleMap.ROWS_COUNT; cellY++) {
            for (int cellX = 0; cellX < BattleMap.COLUMNS_COUNT; cellX++) {
                tmp.set(cellX*BattleMap.CELL_SIZE, cellY*BattleMap.CELL_SIZE);
                if (map.getResourceCount(tmp)>0){
//                    if (!checkApplicant(unit, cellX, cellY)) {
                        float dstToCurrentResource = unit.getPosition().dst(tmp);
                        if (dstToCurrentResource < unit.getPosition().dst(nearestResourcePosition)) {
                            nearestResourcePosition.set(tmp);
                            blockX = cellX;
                            blockY = cellY;
                            isResourceFound = true;
                        }
//                    }
                }
            }
        }
        if (isResourceFound) {
            map.setApplicant(unit.getBaseLogic(), blockX, blockY);
            return nearestResourcePosition;
        }
        return null;
    }

//    private boolean checkApplicant(AbstractUnit unit, int cellX, int cellY){
//        return (
//                map.getApplicant(cellX, cellY)!=null
//                        &&unit.getOwnerType().equals(map.getApplicant(cellX, cellY).getOwnerType()))
//                &&!unit.equals(map.getApplicant(cellX, cellY)
//        );
//    }
}
