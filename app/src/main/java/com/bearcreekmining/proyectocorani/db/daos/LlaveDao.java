package com.bearcreekmining.proyectocorani.db.daos;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.bearcreekmining.proyectocorani.db.entidades.LlaveEntidad;

import java.util.List;

@Dao
public interface LlaveDao {
    @Query("SELECT * FROM mis_llaves")
    List<LlaveEntidad> getAll();

    @Query("SELECT * FROM mis_llaves WHERE uid IN (:llaveEntidadIds)")
    List<LlaveEntidad> loadAllByIds(int[] llaveEntidadIds);

    @Query("SELECT * FROM mis_llaves WHERE descripcion LIKE :first AND "
            +"ble_uuid LIKE :last LIMIT 1")
    LlaveEntidad findByName(String first ,String last);

    @Insert
    void insertAll(LlaveEntidad... llaveEntidads);

    @Delete
    void delete(LlaveEntidad llaveEntidad);
}
