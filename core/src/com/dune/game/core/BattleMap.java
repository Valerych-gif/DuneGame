package com.dune.game.core;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.dune.game.core.units.AbstractUnit;
import com.dune.game.core.units.BattleTank;
import com.dune.game.core.units.Harvester;
import com.dune.game.core.units.Owner;

public class BattleMap {
    private class Cell {
        private int cellX, cellY;
        private int resource;
        private float resourceRegenerationRate;
        private float resourceRegenerationTime;
        private AbstractUnit applicant;

        public int getResource() {
            return resource;
        }

        private Cell(int cellX, int cellY, int resource, float resourceRegenerationRate) {
            this.cellX = cellX;
            this.cellY = cellY;
            this.applicant = null;
            this.resource=resource;
            this.resourceRegenerationRate=resourceRegenerationRate;
        }

        private void update(float dt) {
            if (resourceRegenerationRate > 0.01f) {
                resourceRegenerationTime += dt;
                if (resourceRegenerationTime > resourceRegenerationRate) {
                    resourceRegenerationTime = 0.0f;
                    resource++;
                    if (resource > 5) {
                        resource = 5;
                    }
                }
            }
        }

        private void render(SpriteBatch batch) {
            if (resource > 0) {
                float scale = 0.5f + resource * 0.2f;
                batch.draw(resourceTexture, cellX * 80, cellY * 80, 40, 40, 80, 80, scale, scale, 0.0f);
            } else {
                if (resourceRegenerationRate > 0.01f) {
                    batch.draw(resourceTexture, cellX * 80, cellY * 80, 40, 40, 80, 80, 0.1f, 0.1f, 0.0f);
                }
            }
        }
    }

    public class Base{
        public static final float SIZE=120.0f;

        Vector2 position;
        Owner owner;

        public Vector2 getPosition() {
            return position;
        }

        public Owner getOwner() {
            return owner;
        }

        public Base (Vector2 position, Owner owner){
            this.position=position;
            this.owner=owner;
        }

        public void render(SpriteBatch batch){
            batch.setColor(0.2f, 0.2f, 0.0f, 0.5f);
            int cx = (int)position.x/CELL_SIZE;
            int cy = (int)position.y/CELL_SIZE;
            for (int i = cy-1; i < cy+2; i++) {
                for (int j = cx-1; j < cx+2; j++) {
                    batch.draw(grassTexture, j * 80, i * 80);
                }
            }
            batch.setColor(1, 1, 1, 1);
        }
    }

    public static final int COLUMNS_COUNT = 32;
    public static final int ROWS_COUNT = 18;
    public static final int CELL_SIZE = 80;
    public static final int MAP_HEIGHT_PX = ROWS_COUNT * CELL_SIZE;
    public static final int MAP_WIDTH_PX = COLUMNS_COUNT * CELL_SIZE;

    private TextureRegion grassTexture;
    private TextureRegion resourceTexture;

    private Base aiBase;
    private Base playerBase;

    public Cell[][] getCells() {
        return cells;
    }

    public Base getAiBase() {
        return aiBase;
    }

    public Base getPlayerBase() {
        return playerBase;
    }

    private Cell[][] cells;

    public BattleMap() {
        this.grassTexture = Assets.getInstance().getAtlas().findRegion("grass");
        this.resourceTexture = Assets.getInstance().getAtlas().findRegion("resource");
        this.playerBase = new Base(new Vector2(200.0f, 200.0f), Owner.PLAYER);
        this.aiBase = new Base(new Vector2(MAP_WIDTH_PX-200.0f, MAP_HEIGHT_PX-200.0f), Owner.AI);
        this.cells = new Cell[COLUMNS_COUNT][ROWS_COUNT];
        for (int i = 0; i < COLUMNS_COUNT; i++) {
            for (int j = 0; j < ROWS_COUNT; j++) {
                putResource(i, j);
            }
        }
    }

    private void putResource(int i, int j) {
        float resourceRegenerationRate = 0.0f;
        int resource = 0;

        if (i>2&&j>2&&i<COLUMNS_COUNT-2&&j<ROWS_COUNT-2) {
            if (MathUtils.random() < 0.1f) {
                resource = MathUtils.random(1, 3);
            }
        }

        if (i>5&&j>5&&i<COLUMNS_COUNT-5&&j<ROWS_COUNT-5) {
            resourceRegenerationRate = MathUtils.random(5.0f) - 4.5f;
            if (resourceRegenerationRate < 0.0f) {
                resourceRegenerationRate = 0.0f;
            } else {
                resourceRegenerationRate *= 20.0f;
                resourceRegenerationRate += 10.0f;
            }
        }
        cells[i][j] = new Cell(i, j, resource, resourceRegenerationRate);
        
    }

    public int getResourceCount(Vector2 point) {
        int cx = (int) (point.x / CELL_SIZE);
        int cy = (int) (point.y / CELL_SIZE);
        return cells[cx][cy].resource;
    }

    public AbstractUnit getApplicant(int cx, int cy){
        return cells[cx][cy].applicant;
    }

    public void setApplicant(AbstractUnit applicant, int cx, int cy){
        cells[cx][cy].applicant=applicant;
    }

    public int harvestResource(Vector2 point, int power) {
        int value = 0;
        int cx = (int) (point.x / CELL_SIZE);
        int cy = (int) (point.y / CELL_SIZE);
        if (cells[cx][cy].resource >= power) {
            value = power;
            cells[cx][cy].resource -= power;
        } else {
            value = cells[cx][cy].resource;
            cells[cx][cy].resource = 0;
        }
        return value;
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < COLUMNS_COUNT; i++) {
            for (int j = 0; j < ROWS_COUNT; j++) {
                batch.draw(grassTexture, i * 80, j * 80);
                cells[i][j].render(batch);
            }
        }
        aiBase.render(batch);
        playerBase.render(batch);
    }

    public void update(float dt) {
        for (int i = 0; i < COLUMNS_COUNT; i++) {
            for (int j = 0; j < ROWS_COUNT; j++) {
                cells[i][j].update(dt);
            }
        }
    }
}
