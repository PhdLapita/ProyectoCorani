package com.bearcreekmining.proyectocorani;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.SharedPreferences;

import com.bearcreekmining.proyectocorani.db.basededatos.LlavesDataBase;

public class App extends Application {
    public static App INSTANCE;
    private static final String DATABASE_NAME = "MyDatabase";
    private static final String PREFERENCES = "RoomDemo.preferences";
    private static final String KEY_FORCE_UPDATE = "force_update";

    private LlavesDataBase llavesDataBase;

    public static App get() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // create database
        llavesDataBase = Room.databaseBuilder(getApplicationContext(), LlavesDataBase.class, DATABASE_NAME)
                .build();

        INSTANCE = this;
    }

    public LlavesDataBase getDB() {
        return llavesDataBase;
    }

    public boolean isForceUpdate() {
        return getSP().getBoolean(KEY_FORCE_UPDATE, true);
    }

    public void setForceUpdate(boolean force) {
        SharedPreferences.Editor edit = getSP().edit();
        edit.putBoolean(KEY_FORCE_UPDATE, force);
        edit.apply();
    }

    private SharedPreferences getSP() {
        return getSharedPreferences(PREFERENCES, MODE_PRIVATE);
    }
}
