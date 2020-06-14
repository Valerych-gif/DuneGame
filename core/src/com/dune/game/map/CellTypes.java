package com.dune.game.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.dune.game.screens.utils.Assets;

public enum CellTypes {
    GRASS("grass", 1),
    RESOURCE("resource", 2);

    TextureRegion texture;
    int pathCost;

    public TextureRegion getTexture() {
        return texture;
    }

    public int getPathCost() {
        return pathCost;
    }

    CellTypes (String textureName, int pathCost){
        this.texture=Assets.getInstance().getAtlas().findRegion(textureName);
        this.pathCost=pathCost;
    }
}

