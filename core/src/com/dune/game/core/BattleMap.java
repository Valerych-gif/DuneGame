package com.dune.game.core;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.dune.game.screens.GameScreen;

public class BattleMap {

    private TextureRegion grassTexture, coinTexture;
    private int[][] groundType;
    private int blockX, blockY;

    public BattleMap(int screenWidth, int screenHeight) {
        groundType = new int[screenWidth][screenHeight];
        this.grassTexture = Assets.getInstance().getAtlas().findRegion("grass");
        this.coinTexture = Assets.getInstance().getAtlas().findRegion("coin");
        generate();
    }

    private void generate() {
        for (blockY = 0; blockY < GameScreen.HEIGHT; blockY++) {
            for (blockX = 0; blockX < GameScreen.WIDTH; blockX++) {
                if (MathUtils.random(10) < 9) {
                    groundType[blockX][blockY] = 0;
                } else {
                    groundType[blockX][blockY] = 1;
                }
            }
        }
    }

    public void update (float dt){

    }

    public void render(SpriteBatch batch) {
        for (blockY = 0; blockY < GameScreen.HEIGHT; blockY++) {
            for (blockX = 0; blockX < GameScreen.WIDTH; blockX++) {
                if (groundType[blockX][blockY] == 0) {
                    batch.draw(grassTexture, blockX * 80, blockY * 80);
                } else {
                    batch.draw(coinTexture, blockX * 80, blockY * 80);
                }
            }
        }
    }

    public void checkCollisions (GameObject object){

        for (blockY = 0; blockY < GameScreen.HEIGHT; blockY++) {
            for (blockX = 0; blockX < GameScreen.WIDTH; blockX++) {
                if (groundType[blockX][blockY]==1){
                    checkCollision(object);
                }
            }
        }
    }

    private void checkCollision(GameObject object){
        int objectX = (int)object.position.x/80;
        int objectY = (int)object.position.y/80;
        if ((objectX==blockX)&&(objectY==blockY)) pickUp();
    }

    public void pickUp (){
        groundType [blockX][blockY] = 0;
    }
}
