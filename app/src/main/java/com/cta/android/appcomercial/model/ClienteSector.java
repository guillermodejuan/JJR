package com.cta.android.appcomercial.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by PC on 15/11/2017.
 */
@DatabaseTable
public class ClienteSector implements Serializable {

    @DatabaseField(generatedId = true)
    private int id; // Primary Key

    @DatabaseField(uniqueCombo = true, foreign = true)
    private Cliente cliente;

    @DatabaseField(uniqueCombo = true, foreign = true, columnDefinition = "INTEGER CONSTRAINT FK_CLIENTESECTOR_SECTOR REFERENCES sector(id) ON DELETE CASCADE")
    private Sector sector;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Sector getSector() {
        return sector;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
    }
}
