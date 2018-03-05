package com.cta.android.appcomercial.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by PC on 15/11/2017.
 */
@DatabaseTable
public class ComunidadAutonoma implements Serializable {

    @DatabaseField(generatedId = true)
    private int id; // Primary Key

    @DatabaseField
    private String nombre;

    public ComunidadAutonoma() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
