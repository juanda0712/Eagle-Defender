package com.mygdx.interfaces;

import com.badlogic.gdx.utils.Array;

public interface IDataManager<T> {
    void create(T newObject);

    Array<T> read();

    void update(T updatedObject);

    void delete(T deletedObject);
}
