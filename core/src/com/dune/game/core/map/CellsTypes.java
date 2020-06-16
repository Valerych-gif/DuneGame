package com.dune.game.core.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.dune.game.screens.utils.Assets;

public enum CellsTypes {
    GRASS("grass", 1),
    RESOURCE("resource", 2),
    REGENERATE_RESOURCE("resource", 2);

    private TextureRegion texture;
    private int pathCost;

    public TextureRegion getTexture() {
        return texture;
    }

    public int getPathCost() {
        return pathCost;
    }

    CellsTypes(String textureName, int pathCost){
        this.texture=Assets.getInstance().getAtlas().findRegion(textureName);
        this.pathCost=pathCost;
    }
}

