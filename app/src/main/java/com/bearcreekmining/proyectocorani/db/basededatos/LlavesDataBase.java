package com.bearcreekmining.proyectocorani.db.basededatos;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.VisibleForTesting;

import com.bearcreekmining.proyectocorani.db.daos.LlaveDao;
import com.bearcreekmining.proyectocorani.db.entidades.LlaveEntidad;

@Database(entities = {LlaveEntidad.class}, version = 1)
public abstract class LlavesDataBase extends RoomDatabase{
    /**
     * @return The DAO for the LlaveDao table.
     */
    public abstract LlaveDao llaveDao();
    /** The only instance */
    private static LlavesDataBase sInstance;
    /**
     * Gets the singleton instance of LlavesDataBase.
     *
     * @param context The context.
     * @return The singleton instance of LlavesDataBase.
     */
    public static synchronized LlavesDataBase getInstance(Context context) {
        if (sInstance == null) {
            sInstance = Room
                    .databaseBuilder(context.getApplicationContext(), LlavesDataBase.class, "llave-database")
                    .build();
            //sInstance.populateInitialData();
        }
        return sInstance;
    }

    /**
     * Switches the internal implementation with an empty in-memory database.
     *
     * @param context The context.
     */
    @VisibleForTesting
    public static void switchToInMemory(Context context) {
        sInstance = Room.inMemoryDatabaseBuilder(context.getApplicationContext(),
                LlavesDataBase.class).build();
    }

}
