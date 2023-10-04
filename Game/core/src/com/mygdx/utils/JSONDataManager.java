package com.mygdx.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.mygdx.interfaces.IDataManager;
import com.badlogic.gdx.utils.Array;

public class JSONDataManager<T> implements IDataManager<T> {
    private final Json json = new Json();
    private final Array<T> data;
    private String filePath;
    private FileHandle fileHandle;

    public JSONDataManager(String filePath, Class<T> dataClass) {
        this.filePath = filePath;
        Array<T> tempData = null;
        try {
            FileHandle fileHandle = Gdx.files.local(filePath);
            tempData = json.fromJson(Array.class, dataClass, fileHandle);
        } catch (Exception e) {
            Gdx.app.error("JSONDataManager", "Error al cargar archivo: " + e.getMessage());
        }
        data = tempData;

        //TEST
        for (T object : data) {
            System.out.println(object);
        }
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

    private void savejsonFile() {
        System.out.println(data);
        json.toJson(data, Gdx.files.local(filePath));
    }
}
