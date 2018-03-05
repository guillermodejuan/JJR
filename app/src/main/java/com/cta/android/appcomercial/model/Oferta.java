package com.cta.android.appcomercial.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by PC on 15/11/2017.
 */
@DatabaseTable
public class Oferta implements Serializable {

    @DatabaseField(generatedId = true)
    private int id; // Primary Key

    @DatabaseField
    private String referencia;

    @DatabaseField
    private long fecha;

    @DatabaseField
    private long fechaLimite;

    @DatabaseField(foreign = true)
    private Cliente cliente;

    @DatabaseField(foreign = true)
    private Representado representado;

    public Oferta() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public long getFecha() {
        return fecha;
    }

    public void setFecha(long fecha) {
        this.fecha = fecha;
    }

    public long getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(long fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Representado getRepresentado() {
        return representado;
    }

    public void setRepresentado(Representado representado) {
        this.representado = representado;
    }
}
