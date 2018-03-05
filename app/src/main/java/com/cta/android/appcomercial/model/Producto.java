package com.cta.android.appcomercial.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by PC on 15/11/2017.
 */
@DatabaseTable
public class Producto implements Serializable {

    @DatabaseField(dataType = DataType.BYTE_ARRAY)
    byte[] imageBytes;
    @DatabaseField(generatedId = true)
    private int id; // Primary Key
    @DatabaseField
    private String descripcion;
    @DatabaseField
    private String referencia;
    @DatabaseField
    private float precio;
    @DatabaseField(foreign = true)
    private Sector sector;
    @DatabaseField
    private int caducidad;
    @DatabaseField(foreign = true)
    private Representado representado;
    @DatabaseField
    private String info;

    public Producto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }

    public Sector getSector() {
        return sector;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
    }

    public Representado getRepresentado() {
        return representado;
    }

    public void setRepresentado(Representado representado) {
        this.representado = representado;
    }

    public int getCaducidad() {
        return caducidad;
    }

    public void setCaducidad(int caducidad) {
        this.caducidad = caducidad;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
