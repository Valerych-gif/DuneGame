package com.dune.game.map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.dune.game.core.Building;
import com.dune.game.core.units.AbstractUnit;

public class Cell {
    private AbstractUnit applicant;
    private Building buildingCore;
    private Building buildingEntrance;
    private int cellX, cellY;
    private int resource;
    private float resourceRegenerationRate;
    private float resourceRegenerationTime;
    private boolean groundPassable;
    private boolean airPassable;
    private TextureRegion texture;
    private CellTypes type;

    public AbstractUnit getApplicant() {
        return applicant;
    }

    public Building getBuildingCore() {
        return buildingCore;
    }

    public Building getBuildingEntrance() {
        return buildingEntrance;
    }

    public void setApplicant(AbstractUnit applicant) {
        this.applicant = applicant;
    }

    public void setBuildingCore(Building buildingCore) {
        this.buildingCore = buildingCore;
    }

    public void setBuildingEntrance(Building buildingEntrance) {
        this.buildingEntrance = buildingEntrance;
    }

    public void setGroundPassable(boolean groundPassable) {
        this.groundPassable = groundPassable;
    }

    public void setAirPassable(boolean airPassable) {
        this.airPassable = airPassable;
    }

    public void setType(CellTypes type) {
        this.type = type;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }

    public int getCellX() {
        return cellX;
    }

    public int getCellY() {
        return cellY;
    }

    public int getResource() {
        return resource;
    }

    public boolean isGroundPassable() {
        return groundPassable;
    }

    public boolean isAirPassable() {
        return airPassable;
    }

    public CellTypes getType() {
        return type;
    }

    public Cell(int cellX, int cellY, CellTypes type) {
        this.cellX = cellX;
        this.cellY = cellY;
        this.type = type;
        this.texture = type.getTexture();
        this.groundPassable = true;
        this.airPassable = true;
    }

    public void update(float dt) {
//        if (resourceRegenerationRate > 0.01f) {
//            resourceRegenerationTime += dt;
//            if (resourceRegenerationTime > resourceRegenerationRate) {
//                resourceRegenerationTime = 0.0f;
//                resource++;
//                if (resource > 5) {
//                    resource = 5;
//                }
//            }
//        }
        if (resource<=0){
            this.type=CellTypes.GRASS;
            this.texture=type.getTexture();
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, cellX * BattleMap.CELL_SIZE, cellY * BattleMap.CELL_SIZE, BattleMap.CELL_SIZE / 2, BattleMap.CELL_SIZE / 2, BattleMap.CELL_SIZE, BattleMap.CELL_SIZE, 1, 1, 0.0f);
    }

    public void blockGroundPass() {
        groundPassable = false;
        resourceRegenerationRate = 0.0f;
        resource = 0;
    }

    public void blockAirPass() {
        airPassable = false;
    }

    public void unblockGroundPass() {
        groundPassable = true;
    }

    public void unblockAirPass() {
        airPassable = true;
    }
}
