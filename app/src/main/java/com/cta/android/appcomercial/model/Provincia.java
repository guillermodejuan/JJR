package com.cta.android.appcomercial.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by PC on 15/11/2017.
 */
@DatabaseTable
public class Provincia implements Serializable {

    @DatabaseField(generatedId = true)
    private int id; // Primary Key

    @DatabaseField
    private String nombre;

    @DatabaseField(foreign = true)
    private ComunidadAutonoma comunidadAutonoma;

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

    public ComunidadAutonoma getComunidadAutonoma() {
        return comunidadAutonoma;
    }

    public void setComunidadAutonoma(ComunidadAutonoma comunidadAutonoma) {
        this.comunidadAutonoma = comunidadAutonoma;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
