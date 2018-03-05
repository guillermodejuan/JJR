package com.cta.android.appcomercial.model;

import android.support.annotation.NonNull;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by PC on 14/11/2017.
 */

@DatabaseTable
public class Sector implements Comparable<Sector> {

    @DatabaseField(generatedId = true)
    private int id; // Primary Key

    @DatabaseField
    private String nombre;

    public Sector() {
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
        return this.nombre;
    }

    @Override
    public int compareTo(@NonNull Sector o) {
        return this.getNombre().compareTo(o.getNombre());
    }
}
