package com.mygdx.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.mygdx.interfaces.IDataManager;
import com.badlogic.gdx.utils.Array;

public class JSONDataManager<T> implements IDataManager<T> {
    private final Json json = new Json();
    private final Array<T> data;
    private String filePath;

    public JSONDataManager(String filePath, Class<T> dataClass) {
        this.filePath = filePath;
        data = json.fromJson(Array.class, dataClass, Gdx.files.local(filePath));
    }

    @Override
    public void create(T newObject) {
        data.add(newObject);
        savejsonFile();
    }

    @Override
    public Array<T> read() {
        return data;
    }

    @Override
    public void update(T updatedObject) {
        int index = data.indexOf(updatedObject, true);
        if (index >= 0) {
            data.set(index, updatedObject);
            savejsonFile();
        }
    }

    @Override
    public void delete(T deletedObject) {
        data.removeValue(deletedObject, true);
        savejsonFile();
    }

    @Override
    private void savejsonFile() {
        System.out.println(data);
        json.toJson(data, Gdx.files.local(filePath));
    }
}
