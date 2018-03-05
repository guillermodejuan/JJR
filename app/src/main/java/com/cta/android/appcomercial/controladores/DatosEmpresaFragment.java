package com.cta.android.appcomercial.controladores;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.cta.android.appcomercial.R;
import com.cta.android.appcomercial.adaptadores.AdaptadorComunidades;
import com.cta.android.appcomercial.adaptadores.AdaptadorLocalidades;
import com.cta.android.appcomercial.adaptadores.AdaptadorProvincias;
import com.cta.android.appcomercial.model.Cliente;
import com.cta.android.appcomercial.model.ComunidadAutonoma;
import com.cta.android.appcomercial.model.Localidad;
import com.cta.android.appcomercial.model.Provincia;
import com.cta.android.appcomercial.model.Representado;
import com.cta.android.appcomercial.util.DataBaseHelper;
import com.cta.android.appcomercial.util.Util;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import org.apache.commons.validator.routines.UrlValidator;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by PC on 25/11/2017.
 */

public class DatosEmpresaFragment extends Fragment {

    DataBaseHelper helper;

    private TextView tvTitulo;
    private TextInputLayout ilNombreComercial;
    private EditText etNombreComercial;
    private TextInputLayout ilRazonSocial;
    private EditText etRazonSocial;
    private TextInputLayout ilCif;
    private EditText etCif;
    private TextInputLayout ilDireccion;
    private EditText etDireccion;
    private TextInputLayout ilCodigoPostal;
    private EditText etCodigoPostal;
    private TextInputLayout ilComunidad;
    private Spinner spinnerCom;
    private TextInputLayout ilProvincia;
    private Spinner spinnerProv;
    private TextInputLayout textView11;
    private Spinner spinnerLoc;
    private TextInputLayout ilTelefono;
    private EditText etTelefono;
    private TextInputLayout ilEmail;
    private EditText etEmail;
    private TextInputLayout ilWeb;
    private EditText etWeb;
    private TextInputLayout ilEmpleados;
    private EditText etEmpleados;
    private TextInputLayout ilFacturacion;
    private EditText etFacturacion;
    private TextInputLayout ilCoordenadas;
    private EditText etCoordenadas;
    private ImageButton btGuardarDatosEmpresa;
    private ImageButton btCancelarDatosEmpresa;

    private Cliente clienteCreate;
    private Representado representadoCreate;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.datosempresa, container, false);
        findViews(view);

        if (getArguments().getString("empresa").equals("cliente")) {
            tvTitulo.setText(R.string.nuevoCliente);
        } else {
            tvTitulo.setText(R.string.nuevoRepresentado);
        }

        helper = OpenHelperManager.getHelper(getContext(), DataBaseHelper.class);
        final RuntimeExceptionDao<ComunidadAutonoma, Integer> daoCom = helper.getComunidadAutonomaRuntimeDAO();
        List<ComunidadAutonoma> comunidades = daoCom.queryForAll();
        ComunidadAutonoma ca = new ComunidadAutonoma();
        ca.setNombre(getResources().getString(R.string.selectCom));
        comunidades.add(0, ca);

        spinnerProv.setEnabled(false);
        spinnerLoc.setEnabled(false);

        final AdaptadorComunidades adaptadorComunidades = new AdaptadorComunidades(getContext(), R.layout.item_lista, comunidades);
        spinnerCom.setAdapter(adaptadorComunidades);

        spinnerCom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ComunidadAutonoma comunidadAutonomaSelect = (ComunidadAutonoma) spinnerCom.getSelectedItem();
                if (comunidadAutonomaSelect.getId() != 0) {
                    Dao<Provincia, Integer> daoProv = null;
                    List<Provincia> provincias = null;
                    try {
                        daoProv = helper.getProvinciaDAO();
                        provincias = daoProv.queryForEq("comunidadAutonoma_id", comunidadAutonomaSelect.getId());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    Provincia provincia = new Provincia();
                    provincia.setNombre(getResources().getString(R.string.selectProv));
                    provincias.add(0, provincia);

                    AdaptadorProvincias adaptadorProvincias = new AdaptadorProvincias(getContext(), R.layout.item_lista, provincias);
                    spinnerProv.setAdapter(adaptadorProvincias);
                    spinnerProv.setEnabled(true);
                } else {
                    spinnerProv.setAdapter(null);
                    spinnerLoc.setAdapter(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerProv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                RuntimeExceptionDao<Localidad, Integer> daoLoc = helper.getLocalidadRuntimeDAO();
                Provincia provincia = (Provincia) spinnerProv.getSelectedItem();
                if (provincia.getId() != 0) {
                    List<Localidad> localidades = daoLoc.queryForEq("provincia_id", provincia.getId());
                    Localidad loc = new Localidad();
                    loc.setNombre(getResources().getString(R.string.selectLoc));
                    localidades.add(0, loc);
                    AdaptadorLocalidades adaptadorLocalidades = new AdaptadorLocalidades(getContext(), R.layout.item_lista, localidades);
                    spinnerLoc.setAdapter(adaptadorLocalidades);
                    spinnerLoc.setEnabled(true);
                } else {
                    spinnerLoc.setAdapter(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btGuardarDatosEmpresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validacionFormulario()) {


                    if (getArguments().getString("empresa").equals("cliente")) {
                        clienteCreate = new Cliente();
                        clienteCreate.setNombreComercial(etNombreComercial.getText().toString().toUpperCase());
                        clienteCreate.setRazonSocial(etRazonSocial.getText().toString());
                        clienteCreate.setCif(etCif.getText().toString().toUpperCase());
                        try {
                            clienteCreate.setCodigoPostal(Integer.parseInt(etCodigoPostal.getText().toString()));
                        } catch (NumberFormatException nfe) {

                        }
                        clienteCreate.setCoordenadas(etCoordenadas.getText().toString());
                        clienteCreate.setEmail(etEmail.getText().toString().toLowerCase());
                        try {
                            clienteCreate.setNumEmpleados(Integer.parseInt(etEmpleados.getText().toString()));
                        } catch (NumberFormatException nfe) {

                        }
                        clienteCreate.setDireccion(etDireccion.getText().toString());
                        try {
                            clienteCreate.setTelefono(Integer.parseInt(etTelefono.getText().toString()));
                        } catch (NumberFormatException nfe) {

                        }
                        if (!etWeb.getText().toString().equals("")) {
                            clienteCreate.setWeb("http://" + etWeb.getText().toString());
                        }
                        try {
                            clienteCreate.setFacturacion(Integer.parseInt(etFacturacion.getText().toString()));
                        } catch (NumberFormatException nfe) {

                        }
                        Localidad clienteCreateLoc = (Localidad) spinnerLoc.getSelectedItem();
                        int locindex = spinnerLoc.getSelectedItemPosition();
                        if (clienteCreateLoc != null && locindex != 0) {
                            clienteCreate.setLocalidad(clienteCreateLoc);
                        }

                        try {
                            RuntimeExceptionDao<Cliente, Integer> daoCliente = helper.getClienteRuntimeDAO();
                            daoCliente.create(clienteCreate);

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    } else {
                        representadoCreate = new Representado();
                        representadoCreate.setNombreComercial(etNombreComercial.getText().toString().toUpperCase());
                        representadoCreate.setRazonSocial(etRazonSocial.getText().toString());
                        representadoCreate.setCif(etCif.getText().toString().toUpperCase());
                        try {
                            representadoCreate.setCodigoPostal(Integer.parseInt(etCodigoPostal.getText().toString()));
                        } catch (NumberFormatException nfe) {

                        }
                        representadoCreate.setCoordenadas(etCoordenadas.getText().toString());
                        representadoCreate.setEmail(etEmail.getText().toString().toLowerCase());
                        try {
                            representadoCreate.setNumEmpleados(Integer.parseInt(etEmpleados.getText().toString()));
                        } catch (NumberFormatException nfe) {

                        }
                        try {
                            representadoCreate.setFacturacion(Integer.parseInt(etFacturacion.getText().toString()));
                        } catch (NumberFormatException nfe) {

                        }
                        representadoCreate.setDireccion(etDireccion.getText().toString());
                        if (!etWeb.getText().toString().equals("")) {
                            representadoCreate.setWeb("http://" + etWeb.getText().toString().toLowerCase());
                        }
                        try {
                            representadoCreate.setTelefono(Integer.parseInt(etTelefono.getText().toString()));
                        } catch (NumberFormatException nfe) {

                        }
                        Localidad representadoCreateLoc = (Localidad) spinnerLoc.getSelectedItem();
                        int locindex = spinnerLoc.getSelectedItemPosition();
                        if (representadoCreateLoc != null && locindex != 0) {
                            representadoCreate.setLocalidad((Localidad) spinnerLoc.getSelectedItem());
                        }
                        try {
                            RuntimeExceptionDao<Representado, Integer> daoRepresentado = helper.getRepresentadoRuntimeDAO();
                            daoRepresentado.create(representadoCreate);

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    cerrarFragmento();
                    comunicarCambios();

                }
            }
        });

        btCancelarDatosEmpresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarFragmento();
            }
        });

        return view;

    }

    private void comunicarCambios() {
        EmpresasFragment ef = (EmpresasFragment) this.getParentFragment();
        ef.cargarAdaptadores();
        ef.cargarAutoComplete();
        if (clienteCreate != null) {
            ef.cargarDetalleCliente(clienteCreate);
            ef.setClienteSelect(clienteCreate);
            ef.setRepresentadoSelect(null);

        } else if (representadoCreate != null) {
            ef.cargarDetalleRepresentado(representadoCreate);
            ef.setRepresentadoSelect(representadoCreate);
            ef.setClienteSelect(null);
        }
    }

    private void cerrarFragmento() {
        getFragmentManager().beginTransaction().remove(this).commit();
    }

    private boolean validacionFormulario() {

        boolean esValido = true;

        if (etNombreComercial.getText().toString().equals("")) {
            etNombreComercial.setError("Debe introducir un nombre para la nueva empresa.");
            esValido = false;
        } else {
            boolean nombreExiste = false;
            if (getArguments().getString("empresa").equals("cliente")) {
                Dao<Cliente, Integer> clientesDao = null;
                try {
                    clientesDao = helper.getClienteDAO();
                    List<Cliente> lista = clientesDao.queryForAll();

                    for (Cliente c : lista) {
                        if (c.getNombreComercial().equals(etNombreComercial.getText().toString().toUpperCase())) {
                            nombreExiste = true;
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if (nombreExiste) {
                    etNombreComercial.setError("Ya existe un cliente con ese nombre");
                    esValido = false;
                }
            } else {
                Dao<Representado, Integer> representadosDao = null;
                try {
                    representadosDao = helper.getRepresentadoDAO();
                    List<Representado> lista = representadosDao.queryForAll();

                    for (Representado c : lista) {
                        if (c.getNombreComercial().equals(etNombreComercial.getText().toString().toUpperCase())) {
                            nombreExiste = true;
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if (nombreExiste) {
                    etNombreComercial.setError("Ya existe un representado con ese nombre");
                    esValido = false;
                }
            }
        }

        if (!(etCif.getText().toString().equals("")) && !(Util.validarCIF(etCif.getText().toString().toUpperCase()))) {
            etCif.setError("El CIF introducido no cumple las reglas de formato.");
            esValido = false;
        }

        if (!(etEmail.getText().toString().equals("")) && !Util.validateEmail(etEmail.getText().toString().toLowerCase())) {
            etEmail.setError("El email introducido no es válido.");
            esValido = false;
        }

        if (!(etTelefono.getText().toString().equals(""))) {
            try {
                if (!(Util.validarTelefono(Integer.parseInt(etTelefono.getText().toString())))) {
                    etTelefono.setError("No ha introducido un nº de telefono valido");
                    esValido = false;
                }
            } catch (NumberFormatException nfe) {
                etTelefono.setError("El teléfono debe ser una valor numérico entero.");
                esValido = false;
            }
        }

        if (!(etCodigoPostal.getText().toString().equals(""))) {
            try {
                if (!(Util.validarCP(Integer.parseInt(etCodigoPostal.getText().toString())))) {
                    etCodigoPostal.setError("El CP introducido no existe");
                    esValido = false;
                }
            } catch (NumberFormatException nfe) {
                etCodigoPostal.setError("El CP debe ser un valor numérico entero.");
                esValido = false;
            }
        }

        if (!(etFacturacion.getText().toString().equals(""))) {
            try {
                Integer.parseInt(etFacturacion.getText().toString());
            } catch (NumberFormatException nfe) {
                etFacturacion.setError("La facturación debe ser un valor numérico entero.");
                esValido = false;
            }
        }

        if (!(etEmpleados.getText().toString().equals(""))) {
            try {
                Integer.parseInt(etEmpleados.getText().toString());
            } catch (NumberFormatException nfe) {
                etEmpleados.setError("El nº de empleados debe ser un valor numèrico entero.");
                esValido = false;
            }


        }
        UrlValidator urlValidator = new UrlValidator();
        if (!(etWeb.getText().toString().equals("")) && !urlValidator.isValid("http://" + etWeb.getText().toString().toLowerCase())) {
            etWeb.setError("No ha introducido una direccion url válida");
            esValido = false;
        }

        return esValido;

    }

    private void findViews(View view) {
        tvTitulo = (TextView) view.findViewById(R.id.tvTituloAnadirEmpresa);
        ilNombreComercial = (TextInputLayout) view.findViewById(R.id.ilNombreComercial);
        etNombreComercial = (EditText) view.findViewById(R.id.etNombreComercial);
        etNombreComercial.setInputType(InputType.TYPE_CLASS_TEXT);
        ilRazonSocial = (TextInputLayout) view.findViewById(R.id.ilRazonSocial);
        etRazonSocial = (EditText) view.findViewById(R.id.etRazonSocial);
        etRazonSocial.setInputType(InputType.TYPE_CLASS_TEXT);
        ilCif = (TextInputLayout) view.findViewById(R.id.ilCif);
        etCif = (EditText) view.findViewById(R.id.etCif);
        etCif.setInputType(InputType.TYPE_CLASS_TEXT);
        ilDireccion = (TextInputLayout) view.findViewById(R.id.ilDireccion);
        etDireccion = (EditText) view.findViewById(R.id.etDireccion);
        etDireccion.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS);
        ilCodigoPostal = (TextInputLayout) view.findViewById(R.id.ilCodigoPostal);
        etCodigoPostal = (EditText) view.findViewById(R.id.etCodigoPostal);
        etCodigoPostal.setInputType(InputType.TYPE_CLASS_NUMBER);
        spinnerCom = (Spinner) view.findViewById(R.id.spinnerCom);
        spinnerProv = (Spinner) view.findViewById(R.id.spinnerProv);
        spinnerLoc = (Spinner) view.findViewById(R.id.spinnerLoc);
        ilTelefono = (TextInputLayout) view.findViewById(R.id.ilTelefono);
        etTelefono = (EditText) view.findViewById(R.id.etTelefono);
        etTelefono.setInputType(InputType.TYPE_CLASS_NUMBER);
        ilEmail = (TextInputLayout) view.findViewById(R.id.ilEmail);
        etEmail = (EditText) view.findViewById(R.id.etEmail);
        etEmail.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        ilWeb = (TextInputLayout) view.findViewById(R.id.ilWeb);
        etWeb = (EditText) view.findViewById(R.id.etWeb);
        etWeb.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_URI);
        ilEmpleados = (TextInputLayout) view.findViewById(R.id.ilEmpleados);
        etEmpleados = (EditText) view.findViewById(R.id.etEmpleados);
        etEmpleados.setInputType(InputType.TYPE_CLASS_NUMBER);
        ilFacturacion = (TextInputLayout) view.findViewById(R.id.ilFacturacion);
        etFacturacion = (EditText) view.findViewById(R.id.etFacturacion);
        etFacturacion.setInputType(InputType.TYPE_CLASS_NUMBER);
        ilCoordenadas = (TextInputLayout) view.findViewById(R.id.ilCoordenadas);
        etCoordenadas = (EditText) view.findViewById(R.id.etCoordenadas);
        etCoordenadas.setInputType(InputType.TYPE_CLASS_TEXT);
        btGuardarDatosEmpresa = (ImageButton) view.findViewById(R.id.btGuardarDatosEmpresa);
        btCancelarDatosEmpresa = (ImageButton) view.findViewById(R.id.btCancelarDatosEmpresa);

    }

}

