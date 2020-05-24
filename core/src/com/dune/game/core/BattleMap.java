package com.dune.game.core;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class BattleMap {

    private TextureRegion grassTexture, coinTexture;
    private int screenWidth, screenHeight;
    private int[][] groundType;
    private int x, y;

    public int[][] getGroundType() {
        return groundType;
    }

    public BattleMap(int screenWidth, int screenHeight) {
        this.screenWidth=screenWidth;
        this.screenHeight=screenHeight;
        groundType = new int[screenWidth][screenHeight];
        this.grassTexture = Assets.getInstance().getAtlas().findRegion("grass");
        this.coinTexture = Assets.getInstance().getAtlas().findRegion("coin");
        generate();
    }

    private void generate() {
        for (y = 0; y < screenHeight; y++) {
            for (x = 0; x < screenWidth; x++) {
                if (MathUtils.random(10) < 9) {
                    groundType[x][y] = 0;
                } else {
                    groundType[x][y] = 1;
                }
            }
        }
    }

    public void update (float dt){

    }

    public void render(SpriteBatch batch) {
        for (y = 0; y < screenHeight; y++) {
            for (x = 0; x < screenWidth; x++) {
                if (groundType[x][y] == 0) {
                    batch.draw(grassTexture, x * 80, y * 80);
                } else {
                    batch.draw(coinTexture, x * 80, y * 80);
                }
            }
        }
    }

    public void pickUp (int blockX, int blockY){
        groundType [blockX][blockY] = 0;
    }
}
