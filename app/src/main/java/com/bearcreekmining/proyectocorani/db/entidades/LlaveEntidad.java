package com.bearcreekmining.proyectocorani.db.entidades;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = LlaveEntidad.TABLE_NAME)
public class LlaveEntidad {

    /** The name of the Cheese table. */
    public static final String TABLE_NAME = "mis_llaves";

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "descripcion")
    private String descripcion;

    @ColumnInfo(name = "imagen")
    private String imagen;

    @ColumnInfo(name = "name_ble")
    private String nameBle;

    @ColumnInfo(name = "ble_uuid")
    private String bleUuid;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getNameBle() {
        return nameBle;
    }

    public void setNameBle(String nameBle) {
        this.nameBle = nameBle;
    }

    public String getBleUuid() {
        return bleUuid;
    }

    public void setBleUuid(String bleUuid) {
        this.bleUuid = bleUuid;
    }
}
