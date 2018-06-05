package com.bearcreekmining.proyectocorani.db.daos;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.bearcreekmining.proyectocorani.db.entidades.LlaveEntidad;

import java.util.List;

@Dao
public interface LlaveDao {
    @Query("SELECT * FROM mis_llaves")
    List<LlaveEntidad> getAll();
/*
    @Query("SELECT * FROM mis_llaves WHERE uid IN (:llaveEntidadIds)")
    List<LlaveEntidad> loadAllByIds(int[] llaveEntidadIds);
*/
    @Query("SELECT * FROM mis_llaves WHERE descripcion LIKE :llave LIMIT 1")
    LlaveEntidad findByName(String llave);

    @Insert
    void insertAll(List<LlaveEntidad> llaveEntidads);

    @Delete
    void delete(LlaveEntidad llaveEntidad);

    @Update
    void update(LlaveEntidad llaveEntidad);

}
