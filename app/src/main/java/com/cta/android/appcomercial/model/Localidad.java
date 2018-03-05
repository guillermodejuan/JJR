package com.cta.android.appcomercial.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by PC on 15/11/2017.
 */
@DatabaseTable
public class Localidad {

    @DatabaseField(generatedId = true)
    private int id; // Primary Key

    @DatabaseField
    private String nombre;

    @DatabaseField
    private int codigo;

    @DatabaseField(foreign = true)
    private Provincia provincia;

    public Localidad() {
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

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public Provincia getProvincia() {
        return provincia;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }

    @Override
    public String toString() {
        return nombre;
    }


}
