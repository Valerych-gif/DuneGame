package com.dune.game.core.utils;

import com.badlogic.gdx.math.Vector2;
import com.dune.game.core.GameObject;
import com.dune.game.core.interfaces.Poolable;
import com.dune.game.core.interfaces.Targetable;
import com.dune.game.core.units.AbstractUnit;
import com.dune.game.core.units.types.TargetType;
import com.dune.game.core.units.types.UnitType;

import java.util.ArrayList;
import java.util.List;

public abstract class ObjectPool<T extends Poolable> {
    protected List<T> activeList;
    protected List<T> freeList;

    public List<T> getActiveList() {
        return activeList;
    }

    public int activeSize() {
        return activeList.size();
    }

    protected abstract T newObject();

    public void free(int index) {
        freeList.add(activeList.remove(index));
    }

    public ObjectPool() {
        this.activeList = new ArrayList<>();
        this.freeList = new ArrayList<>();
    }

    public T activateObject() {
        if (freeList.size() == 0) {
            freeList.add(newObject());
        }
        T tempObject = freeList.remove(freeList.size() - 1);
        activeList.add(tempObject);
        return tempObject;
    }

    public void checkPool() {
        for (int i = activeList.size() - 1; i >= 0; i--) {
            if (!activeList.get(i).isActive()) {
                free(i);
            }
        }
    }
}