package com.cta.android.appcomercial.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by PC on 15/11/2017.
 */
@DatabaseTable
public class Representado implements Parcelable {

    public static final Parcelable.Creator<Representado> CREATOR = new Parcelable.Creator<Representado>() {
        @Override
        public Representado createFromParcel(Parcel in) {
            return new Representado(in);
        }

        @Override
        public Representado[] newArray(int size) {
            return new Representado[size];
        }
    };
    @DatabaseField(generatedId = true)
    private int id; // Primary Key
    @DatabaseField
    private String nombreComercial;
    @DatabaseField
    private String razonSocial;
    @DatabaseField
    private String cif;
    @DatabaseField
    private String direccion;
    @DatabaseField
    private int codigoPostal;
    @DatabaseField
    private String web;
    @DatabaseField
    private String email;
    @DatabaseField
    private int telefono;
    @DatabaseField
    private String coordenadas;
    @DatabaseField
    private int numEmpleados;
    @DatabaseField
    private int facturacion;
    @DatabaseField(foreign = true)
    private Localidad localidad;

    public Representado() {
    }

    public Representado(Parcel in) {
        id = in.readInt();
        nombreComercial = in.readString();
        razonSocial = in.readString();
        cif = in.readString();
        direccion = in.readString();
        codigoPostal = in.readInt();
        web = in.readString();
        email = in.readString();
        telefono = in.readInt();
        coordenadas = in.readString();
        numEmpleados = in.readInt();
        facturacion = in.readInt();
        //localidad = (Localidad) in.readParcelable(Localidad.class.getClassLoader());

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreComercial() {
        return nombreComercial;
    }

    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(int codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public String getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(String coordenadas) {
        this.coordenadas = coordenadas;
    }

    public int getNumEmpleados() {
        return numEmpleados;
    }

    public void setNumEmpleados(int numEmpleados) {
        this.numEmpleados = numEmpleados;
    }

    public int getFacturacion() {
        return facturacion;
    }

    public void setFacturacion(int facturacion) {
        this.facturacion = facturacion;
    }

    public Localidad getLocalidad() {
        return localidad;
    }

    public void setLocalidad(Localidad localidad) {
        this.localidad = localidad;
    }

    @Override
    public String toString() {
        return nombreComercial;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nombreComercial);
        dest.writeString(razonSocial);
        dest.writeString(cif);
        dest.writeString(direccion);
        dest.writeInt(codigoPostal);
        dest.writeString(web);
        dest.writeString(email);
        dest.writeInt(telefono);
        dest.writeString(coordenadas);
        dest.writeInt(numEmpleados);
        dest.writeInt(facturacion);
    }
}
