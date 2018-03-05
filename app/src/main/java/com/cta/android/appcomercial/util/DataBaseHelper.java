package com.cta.android.appcomercial.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cta.android.appcomercial.R;
import com.cta.android.appcomercial.model.Cargo;
import com.cta.android.appcomercial.model.Cliente;
import com.cta.android.appcomercial.model.ClienteSector;
import com.cta.android.appcomercial.model.ComunidadAutonoma;
import com.cta.android.appcomercial.model.ContactoCliente;
import com.cta.android.appcomercial.model.ContactoRepresentado;
import com.cta.android.appcomercial.model.LineaOferta;
import com.cta.android.appcomercial.model.Localidad;
import com.cta.android.appcomercial.model.Oferta;
import com.cta.android.appcomercial.model.Producto;
import com.cta.android.appcomercial.model.Provincia;
import com.cta.android.appcomercial.model.Representado;
import com.cta.android.appcomercial.model.Sector;
import com.cta.android.appcomercial.model.Visita;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by PC on 14/11/2017.
 */

public class DataBaseHelper extends OrmLiteSqliteOpenHelper {


    private static final String DATABASE_NAME = "appcomercial.db";
    private static final int DATABASE_VERSION = 1;

    private Dao<Sector, Integer> sectorDAO = null;
    private RuntimeExceptionDao<Sector, Integer> sectorRuntimeDAO = null;
    private Dao<ComunidadAutonoma, Integer> comunidadAutonomaDAO = null;
    private RuntimeExceptionDao<ComunidadAutonoma, Integer> comunidadAutonomaRuntimeDAO = null;
    private Dao<Provincia, Integer> provinciaDAO = null;
    private RuntimeExceptionDao<Provincia, Integer> provinciaRuntimeDAO = null;
    private Dao<Localidad, Integer> localidadDAO = null;
    private RuntimeExceptionDao<Localidad, Integer> localidadRuntimeDAO = null;
    private Dao<Cliente, Integer> clienteDAO = null;
    private RuntimeExceptionDao<Cliente, Integer> clienteRuntimeDAO = null;
    private Dao<Representado, Integer> representadoDAO = null;
    private RuntimeExceptionDao<Representado, Integer> representadoRuntimeDAO = null;
    private Dao<ContactoRepresentado, Integer> contactoRepresentadoDAO = null;
    private RuntimeExceptionDao<ContactoRepresentado, Integer> contactoRepresentadoRuntimeDAO = null;
    private Dao<ContactoCliente, Integer> contactoClienteDAO = null;
    private RuntimeExceptionDao<ContactoCliente, Integer> contactoClienteRuntimeDAO = null;
    private Dao<Cargo, Integer> cargoDAO = null;
    private RuntimeExceptionDao<Cargo, Integer> cargoRuntimeDAO = null;
    private Dao<Visita, Integer> visitaDao = null;
    private RuntimeExceptionDao<Visita, Integer> visitaRuntimeDao = null;
    private Dao<ClienteSector, Integer> clienteSectorDao = null;
    private RuntimeExceptionDao<ClienteSector, Integer> clienteSectorRuntimeDao = null;
    private Dao<Producto,Integer> productoDao = null;
    private RuntimeExceptionDao<Producto, Integer> productoRuntimeExceptionDao;
    private Dao<Oferta,Integer> ofertaDao = null;
    private RuntimeExceptionDao<Oferta, Integer> ofertaRuntimeExceptionDao = null;
    private Dao<LineaOferta,Integer> lineaOfertaIntegerDao = null;
    private RuntimeExceptionDao<LineaOferta, Integer> lineaOfertaIntegerRuntimeExceptionDao = null;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
    }

    /**
     * @param sqLiteDatabase
     * @param connectionSource
     */
    @Override

    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Sector.class);
            TableUtils.createTable(connectionSource, Cargo.class);
            TableUtils.createTable(connectionSource, ComunidadAutonoma.class);
            TableUtils.createTable(connectionSource, Provincia.class);
            TableUtils.createTable(connectionSource, Localidad.class);
            TableUtils.createTable(connectionSource, Cliente.class);
            TableUtils.createTable(connectionSource, ClienteSector.class);
            TableUtils.createTable(connectionSource, ContactoCliente.class);
            TableUtils.createTable(connectionSource, Representado.class);
            TableUtils.createTable(connectionSource, ContactoRepresentado.class);
            TableUtils.createTable(connectionSource, Producto.class);
            TableUtils.createTable(connectionSource, Oferta.class);
            TableUtils.createTable(connectionSource, LineaOferta.class);
            TableUtils.createTable(connectionSource, Visita.class);

        } catch (SQLException e) {

            Log.e(DataBaseHelper.class.getSimpleName(), "Imposible crear la base de datos");
            throw new RuntimeException(e);
        }
    }

    /**
     * @param sqLiteDatabase
     * @param connectionSource
     * @param i
     * @param i1
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {
        Log.i(DataBaseHelper.class.getSimpleName(), "onUpgrade()");
        try {
            TableUtils.dropTable(connectionSource, Sector.class, true);
            onCreate(sqLiteDatabase, connectionSource);
        } catch (SQLException e) {
            Log.e(DataBaseHelper.class.getSimpleName(), "Imposible eliminar la base de datos");
            throw new RuntimeException(e);
        }
    }

    public void close() {
        super.close();
        sectorDAO = null;
        sectorRuntimeDAO = null;
        comunidadAutonomaRuntimeDAO = null;
        comunidadAutonomaDAO = null;
        provinciaDAO = null;
        provinciaRuntimeDAO = null;
        localidadDAO = null;
        localidadRuntimeDAO = null;
        clienteDAO = null;
        clienteRuntimeDAO = null;
        representadoDAO = null;
        representadoRuntimeDAO = null;
        contactoClienteDAO = null;
        contactoClienteRuntimeDAO = null;
        contactoRepresentadoDAO = null;
        contactoRepresentadoRuntimeDAO = null;
        cargoDAO = null;
        cargoRuntimeDAO = null;
        clienteSectorDao = null;
        clienteSectorRuntimeDao = null;
    }

    public Dao<Sector, Integer> getSectorDAO() throws SQLException {
        if (sectorDAO == null) {
            sectorDAO = getDao(Sector.class);
        }
        return sectorDAO;
    }

    public RuntimeExceptionDao<Sector, Integer> getSectorRuntimeDAO() {
        if (sectorRuntimeDAO == null) sectorRuntimeDAO = getRuntimeExceptionDao(Sector.class);
        return sectorRuntimeDAO;
    }


    public Dao<ComunidadAutonoma, Integer> getComunidadAutonomaDAO() throws SQLException {
        if (comunidadAutonomaDAO == null) {
            comunidadAutonomaDAO = getDao(ComunidadAutonoma.class);
        }
        return comunidadAutonomaDAO;
    }

    public RuntimeExceptionDao<ComunidadAutonoma, Integer> getComunidadAutonomaRuntimeDAO() {
        if (comunidadAutonomaRuntimeDAO == null)
            comunidadAutonomaRuntimeDAO = getRuntimeExceptionDao(ComunidadAutonoma.class);
        return comunidadAutonomaRuntimeDAO;
    }

    public Dao<Provincia, Integer> getProvinciaDAO() throws SQLException {
        if (provinciaDAO == null) {
            provinciaDAO = getDao(Provincia.class);
        }
        return provinciaDAO;
    }

    public RuntimeExceptionDao<Provincia, Integer> getProvinciaRuntimeDAO() {
        if (provinciaRuntimeDAO == null)
            provinciaRuntimeDAO = getRuntimeExceptionDao(Provincia.class);
        return provinciaRuntimeDAO;
    }

    public Dao<Localidad, Integer> getLocalidadDAO() throws SQLException {
        if (localidadDAO == null) {
            localidadDAO = getDao(Localidad.class);
        }
        return localidadDAO;
    }

    public RuntimeExceptionDao<Localidad, Integer> getLocalidadRuntimeDAO() {
        if (localidadRuntimeDAO == null)
            localidadRuntimeDAO = getRuntimeExceptionDao(Localidad.class);
        return localidadRuntimeDAO;
    }

    public Dao<Cliente, Integer> getClienteDAO() throws SQLException {
        if (clienteDAO == null) {
            clienteDAO = getDao(Cliente.class);
        }
        return clienteDAO;
    }

    public RuntimeExceptionDao<Cliente, Integer> getClienteRuntimeDAO() throws SQLException {
        if (clienteRuntimeDAO == null) clienteRuntimeDAO = getRuntimeExceptionDao(Cliente.class);
        return clienteRuntimeDAO;
    }

    public Dao<Representado, Integer> getRepresentadoDAO() throws SQLException {
        if (representadoDAO == null) {
            representadoDAO = getDao(Representado.class);
        }
        return representadoDAO;
    }

    public RuntimeExceptionDao<Representado, Integer> getRepresentadoRuntimeDAO() throws SQLException {
        if (representadoRuntimeDAO == null) {
            representadoRuntimeDAO = getRuntimeExceptionDao(Representado.class);
        }
        return representadoRuntimeDAO;
    }

    public Dao<ContactoRepresentado, Integer> getContactoRepresentadoDAO() throws SQLException {
        if (contactoRepresentadoDAO == null) {
            contactoRepresentadoDAO = getDao(ContactoRepresentado.class);
        }
        return contactoRepresentadoDAO;
    }

    public RuntimeExceptionDao<ContactoRepresentado, Integer> getContactoRepresentadoRuntimeDAO() {
        if (contactoRepresentadoRuntimeDAO == null) {
            contactoRepresentadoRuntimeDAO = getRuntimeExceptionDao(ContactoRepresentado.class);
        }
        return contactoRepresentadoRuntimeDAO;
    }

    public Dao<ContactoCliente, Integer> getContactoClienteDAO() throws SQLException {
        if (contactoClienteDAO == null) {
            contactoClienteDAO = getDao(ContactoCliente.class);
        }
        return contactoClienteDAO;
    }

    public RuntimeExceptionDao<ContactoCliente, Integer> getContactoClienteRuntimeDAO() {
        if (contactoClienteRuntimeDAO == null) {
            contactoClienteRuntimeDAO = getRuntimeExceptionDao(ContactoCliente.class);
        }
        return contactoClienteRuntimeDAO;
    }

    public Dao<Cargo, Integer> getCargoDAO() throws SQLException {
        if (cargoDAO == null) {
            cargoDAO = getDao(Cargo.class);
        }
        return cargoDAO;
    }

    public RuntimeExceptionDao<Cargo, Integer> getCargoRuntimeDAO() {
        if (cargoRuntimeDAO == null) {
            cargoRuntimeDAO = getRuntimeExceptionDao(Cargo.class);
        }
        return cargoRuntimeDAO;
    }

    public Dao<Visita, Integer> getVisitaDao() throws SQLException {
        if (visitaDao == null) {
            visitaDao = getDao(Visita.class);
        }
        return visitaDao;
    }

    public RuntimeExceptionDao<Visita, Integer> getVisitaRuntimeDao() {
        if (visitaRuntimeDao == null) {
            visitaRuntimeDao = getRuntimeExceptionDao(Visita.class);
        }
        return visitaRuntimeDao;
    }

    public Dao<ClienteSector, Integer> getClienteSectorDao() throws SQLException {
        if (clienteSectorDao == null) {
            clienteSectorDao = getDao(ClienteSector.class);
        }
        return clienteSectorDao;
    }

    public RuntimeExceptionDao<ClienteSector, Integer> getClienteSectorRuntimeDao() {
        if (clienteSectorRuntimeDao == null) {
            clienteSectorRuntimeDao = getRuntimeExceptionDao(ClienteSector.class);
        }
        return clienteSectorRuntimeDao;
    }

    public Dao<Producto, Integer> getProductoDao() throws SQLException {
        if (productoDao == null) {
            productoDao = getDao(Producto.class);
        }
        return productoDao;
    }

    public RuntimeExceptionDao<Producto, Integer> getProductoRuntimeExceptionDao() {
        if (productoRuntimeExceptionDao == null) {
            productoRuntimeExceptionDao = getRuntimeExceptionDao(Producto.class);
        }
        return productoRuntimeExceptionDao;
    }

    public Dao<Oferta, Integer> getOfertaDao() throws SQLException {
        if (ofertaDao == null) {
            ofertaDao = getDao(Oferta.class);
        }
        return ofertaDao;
    }

    public RuntimeExceptionDao<Oferta, Integer> getOfertaRuntimeExceptionDao() {
        if (ofertaRuntimeExceptionDao == null) {
            ofertaRuntimeExceptionDao = getRuntimeExceptionDao(Oferta.class);
        }
        return ofertaRuntimeExceptionDao;
    }

    public Dao<LineaOferta, Integer> getLineaOfertaIntegerDao() throws SQLException {
        if (lineaOfertaIntegerDao == null) {
            lineaOfertaIntegerDao = getDao(LineaOferta.class);
        }
        return lineaOfertaIntegerDao;
    }

    public RuntimeExceptionDao<LineaOferta, Integer> getLineaOfertaIntegerRuntimeExceptionDao() {
        if (lineaOfertaIntegerRuntimeExceptionDao == null) {
            lineaOfertaIntegerRuntimeExceptionDao = getRuntimeExceptionDao(LineaOferta.class);
        }
        return lineaOfertaIntegerRuntimeExceptionDao;
    }
}


