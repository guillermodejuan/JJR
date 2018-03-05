package com.cta.android.appcomercial.model;

import android.support.annotation.NonNull;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by PC on 15/11/2017.
 */
@DatabaseTable
public class Visita implements Comparable {

    @DatabaseField(generatedId = true)
    private int id; // Primary Key

    @DatabaseField
    private long fechainicio;

    @DatabaseField
    private String tipovisita;

    @DatabaseField
    private long fechafinal;

    @DatabaseField
    private String titulo;

    @DatabaseField
    private String notas;

    @DatabaseField
    private String emplazamiento;

    @DatabaseField(foreign = true)
    private ContactoCliente contactoCliente;

    @DatabaseField(foreign = true)
    private Cliente cliente;

    @DatabaseField(foreign = true)
    private Representado representado;

    @DatabaseField(foreign = true)
    private ContactoRepresentado contactoRepresentado;

    public Visita() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public ContactoCliente getContactoCliente() {
        return contactoCliente;
    }

    public void setContactoCliente(ContactoCliente contactoCliente) {
        this.contactoCliente = contactoCliente;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public ContactoRepresentado getContactoRepresentado() {
        return contactoRepresentado;
    }

    public void setContactoRepresentado(ContactoRepresentado contactoRepresentado) {
        this.contactoRepresentado = contactoRepresentado;
    }

    public Representado getRepresentado() {
        return representado;
    }

    public void setRepresentado(Representado representado) {
        this.representado = representado;
    }

    public long getFechainicio() {
        return fechainicio;
    }

    public void setFechainicio(long fechainicio) {
        this.fechainicio = fechainicio;
    }

    public long getFechafinal() {
        return fechafinal;
    }

    public void setFechafinal(long fechafinal) {
        this.fechafinal = fechafinal;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getEmplazamiento() {
        return emplazamiento;
    }

    public void setEmplazamiento(String emplazamiento) {
        this.emplazamiento = emplazamiento;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        int compare = (int) ((Visita) o).getFechainicio();
        return (int) this.getFechainicio() - compare;
    }

    public String getTipovisita() {
        return tipovisita;
    }

    public void setTipovisita(String tipovisita) {
        this.tipovisita = tipovisita;
    }


}
