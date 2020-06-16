package com.dune.game.core.map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.dune.game.core.GameController;
import com.dune.game.core.GameMap;
import com.dune.game.core.units.AbstractUnit;

public class BattleMap implements GameMap {

    public static final int COLUMNS_COUNT = 64;
    public static final int ROWS_COUNT = 24;
    public static final int CELL_SIZE = 60;
    public static final int MAP_WIDTH_PX = COLUMNS_COUNT * CELL_SIZE;
    public static final int MAP_HEIGHT_PX = ROWS_COUNT * CELL_SIZE;

    @Override
    public int getSizeX() {
        return COLUMNS_COUNT;
    }

    @Override
    public int getSizeY() {
        return ROWS_COUNT;
    }

    public Building getBuildingFromCell(int cellX, int cellY) {
        if (cellX < 0 || cellY < 0 || cellX >= COLUMNS_COUNT || cellY >= ROWS_COUNT) {
            return null;
        }
        return cells[cellX][cellY].getBuildingCore();
    }

    @Override
    public boolean isCellPassable(int cellX, int cellY, boolean isFlyable) {
        if (cellX < 0 || cellY < 0 || cellX >= COLUMNS_COUNT || cellY >= ROWS_COUNT) {
            return false;
        }
        if (cells[cellX][cellY].isGroundPassable()) {
            return true;
        }
        if (cells[cellX][cellY].isAirPassable() && isFlyable) {
            return true;
        }
        return false;
    }

    @Override
    public int getCellCost(int cellX, int cellY) {
        return cells[cellX][cellY].getType().getPathCost();
    }

    private Cell[][] cells;
    private GameController gc;


    public BattleMap(GameController gc) {
        this.gc = gc;
        this.cells = new Cell[COLUMNS_COUNT][ROWS_COUNT];
    }

    public void setup() {
        for (int i = 0; i < COLUMNS_COUNT; i++) {
            for (int j = 0; j < ROWS_COUNT; j++) {

                cells[i][j] = new Cell(i, j, CellsTypes.GRASS);

                if (MathUtils.random() < 0.1f) {
                    cells[i][j] = new Cell(i, j, CellsTypes.RESOURCE);
                    cells[i][j].setResource(MathUtils.random(1, 3));

                    cells[i][j].setResourceRegenerationRate(MathUtils.random(5.0f) - 4.5f);
                    if (cells[i][j].getResourceRegenerationRate() < 0.0f) {
                        cells[i][j].setResourceRegenerationRate(0.0f);
                    } else {
                        cells[i][j] = new Cell(i, j, CellsTypes.REGENERATE_RESOURCE);
                        cells[i][j].setResourceRegenerationRate(cells[i][j].getResourceRegenerationRate() * 20.0f);
                        cells[i][j].setResourceRegenerationRate(cells[i][j].getResourceRegenerationRate() + 10.0f);
                    }
                }
            }
        }

        gc.getBuildingsController().setup(3, 3, gc.getPlayerLogic());
        gc.getBuildingsController().setup(BattleMap.COLUMNS_COUNT - 3, BattleMap.ROWS_COUNT - 3, gc.getAiLogic());
    }

    public void blockGroundCell(int cellX, int cellY) {
        cells[cellX][cellY].blockGroundPass();
    }

    public void unblockGroundCell(int cellX, int cellY) {
        cells[cellX][cellY].unblockGroundPass();
    }

    public void blockAirCell(int cellX, int cellY) {
        cells[cellX][cellY].blockAirPass();
    }

    public void unblockAirCell(int cellX, int cellY) {
        cells[cellX][cellY].unblockAirPass();
    }

    public void setupBuilding(int startX, int startY, int endX, int endY, int entranceX, int entranceY, Building
            building) {
        for (int i = startX; i <= endX; i++) {
            for (int j = startY; j <= endY; j++) {
                cells[i][j].setBuildingCore(building);
                if (building != null) {
                    blockAirCell(i, j);
                    blockGroundCell(i, j);
                } else {
                    unblockAirCell(i, j);
                    unblockGroundCell(i, j);
                }
            }
        }
        cells[entranceX][entranceY].setBuildingEntrance(building);
    }

    public boolean isCellGroundPassable(Vector2 position) {
        int cellX = (int) (position.x / BattleMap.CELL_SIZE);
        int cellY = (int) (position.y / BattleMap.CELL_SIZE);
        if (cellX < 0 || cellY < 0 || cellX >= COLUMNS_COUNT || cellY >= ROWS_COUNT) {
            return false;
        }
        return cells[cellX][cellY].isGroundPassable();
    }

    public void setApplicant(AbstractUnit applicant, int cx, int cy) {
        cells[cx][cy].setApplicant(applicant);
    }

    public AbstractUnit getApplicant(int cx, int cy) {
        return cells[cx][cy].getApplicant();
    }

    public int getResourceCount(Vector2 point) {
        int cx = (int) (point.x / CELL_SIZE);
        int cy = (int) (point.y / CELL_SIZE);
        return cells[cx][cy].getResource();
    }

    public int harvestResource(Vector2 point, int power) {
        int value = 0;
        int cx = (int) (point.x / CELL_SIZE);
        int cy = (int) (point.y / CELL_SIZE);
        if (cells[cx][cy].getResource() >= power) {
            value = power;
            cells[cx][cy].setResource(cells[cx][cy].getResource() - power);
        } else {
            value = cells[cx][cy].getResource();
            cells[cx][cy].setResource(0);
        }
        return value;
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < COLUMNS_COUNT; i++) {
            for (int j = 0; j < ROWS_COUNT; j++) {
                cells[i][j].render(batch);
            }
        }
    }

    public void update(float dt) {
        for (int i = 0; i < COLUMNS_COUNT; i++) {
            for (int j = 0; j < ROWS_COUNT; j++) {
                cells[i][j].update(dt);
            }
        }
    }

    public Building getBuildingEntrance(int cellX, int cellY) {
        return cells[cellX][cellY].getBuildingEntrance();
    }
}
