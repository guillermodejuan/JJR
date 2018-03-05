package com.cta.android.appcomercial.controladores;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.cta.android.appcomercial.R;
import com.cta.android.appcomercial.adaptadores.AdaptadorClientes;
import com.cta.android.appcomercial.adaptadores.AdaptadorLineaOferta;
import com.cta.android.appcomercial.adaptadores.AdaptadorRepresentados;
import com.cta.android.appcomercial.adaptadores.AdaptadorSpinnerProductos;
import com.cta.android.appcomercial.model.Cliente;
import com.cta.android.appcomercial.model.LineaOferta;
import com.cta.android.appcomercial.model.Oferta;
import com.cta.android.appcomercial.model.Producto;
import com.cta.android.appcomercial.model.Representado;
import com.cta.android.appcomercial.util.DataBaseHelper;
import com.cta.android.appcomercial.util.Util;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by PC on 21/01/2018.
 */

public class AnadirOfertaFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private ImageButton ibSalirAnadirOferta;
    private ImageButton ibGuardarOferta;
    private AutoCompleteTextView acClienteOferta;
    private AutoCompleteTextView acRepresentadoOferta;
    private Button btFechaOferta;
    private Button btExpiracionOferta;
    private Spinner spProducto;
    private TextInputEditText etCantidadAnual;
    private TextInputEditText etReferencia;
    private ImageButton ibAnadirLineaOferta;
    private ListView lvLineasOferta;

    private DataBaseHelper helper;
    private AdaptadorClientes adaptadorClientes;
    private AdaptadorRepresentados adaptadorRepresentados;
    private Representado representadoSelect;
    private Cliente clienteSelect;

    private long fechaOferta;
    private long expiracionOferta;

    private ArrayList<LineaOferta> lineas = new ArrayList<LineaOferta>();

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2018-01-21 11:53:09 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews(View v) {
        ibSalirAnadirOferta = (ImageButton)v.findViewById( R.id.ibSalirAnadirOferta );
        ibGuardarOferta = (ImageButton)v.findViewById( R.id.ibGuardarOferta );
        acClienteOferta = (AutoCompleteTextView)v.findViewById( R.id.acClienteOferta );
        acRepresentadoOferta = (AutoCompleteTextView)v.findViewById( R.id.acRepresentadoOferta);
        btFechaOferta = (Button)v.findViewById( R.id.btFechaOferta );
        btExpiracionOferta = (Button)v.findViewById( R.id.btFechaExpiracion);
        spProducto = (Spinner)v.findViewById( R.id.spProducto );
        etCantidadAnual = (TextInputEditText)v.findViewById( R.id.etCantidadAnual );
        ibAnadirLineaOferta = (ImageButton)v.findViewById( R.id.ibAnadirLineaOferta );
        lvLineasOferta = (ListView)v.findViewById(R.id.lvLineasOferta);
        etReferencia = (TextInputEditText) v.findViewById(R.id.etReferenciaOferta);

        ibSalirAnadirOferta.setOnClickListener( this );
        ibGuardarOferta.setOnClickListener( this );
        btFechaOferta.setOnClickListener( this );
        btExpiracionOferta.setOnClickListener( this );
        ibAnadirLineaOferta.setOnClickListener( this );

        lvLineasOferta.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(getContext())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Eliminar Linea de Oferta")
                        .setMessage("¿Confirma que desea la linea de oferta seleccionada?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                lineas.remove(lvLineasOferta.getItemAtPosition(position));
                                AdaptadorLineaOferta adaptadorLineaOferta = new AdaptadorLineaOferta(getContext(),R.layout.item_spinner_contactos,lineas);
                                lvLineasOferta.setAdapter(adaptadorLineaOferta);

                            }
                        }).setNegativeButton("No", null)
                        .show();

                return false;
            }
        });


    }



    public static OfertasFragment newInstance(String contenido) {
        //1. crear el fragment
        //2. setear el contenido via argumentos
        //3. devolver el fragment creado y seteado con info

        OfertasFragment fragment = new OfertasFragment();
        Bundle bundle = new Bundle(1); // se puede especificar el numero de argumentos, en este caso 1
        bundle.putString("content", contenido);
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.datosoferta, container, false);
        helper = OpenHelperManager.getHelper(getContext(), DataBaseHelper.class);
        findViews(view);
        cargarAdaptadores();
        acRepresentadoOferta.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Representado representado = (Representado) acRepresentadoOferta.getAdapter().getItem(position);
                RuntimeExceptionDao<Producto, Integer> runtimeExceptionDao = helper.getProductoRuntimeExceptionDao();
                List<Producto> productos = runtimeExceptionDao.queryForEq("representado_id",representado.getId());
                AdaptadorSpinnerProductos adaptadorSpinnerProductos = new AdaptadorSpinnerProductos(getContext(),R.layout.item_lista,productos);
                spProducto.setAdapter(adaptadorSpinnerProductos);
                representadoSelect = representado;

            }
        });
        acClienteOferta.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clienteSelect = (Cliente) acClienteOferta.getAdapter().getItem(position);
            }
        });
        etCantidadAnual.setInputType(InputType.TYPE_CLASS_NUMBER);
        return view;
    }

    @Override
    public void onClick(View v) {
        if ( v == ibSalirAnadirOferta ) {
            // Handle clicks for ibSalirAnadirOferta
            cerrarFragmento();
        } else if ( v == ibGuardarOferta ) {
            // Handle clicks for ibGuardarOferta
            boolean valid = validarOferta();
            if (valid && lineas.size() > 0) {
                Oferta oferta = new Oferta();
                oferta.setCliente(clienteSelect);
                oferta.setRepresentado(representadoSelect);
                oferta.setFecha(fechaOferta);
                if (expiracionOferta != 0) {
                    oferta.setFechaLimite(expiracionOferta);
                }
                if (!etReferencia.getText().toString().equals("")) {
                    oferta.setReferencia(etReferencia.getText().toString());
                }
                RuntimeExceptionDao<Oferta, Integer > ofertaIntegerRuntimeExceptionDao = helper.getOfertaRuntimeExceptionDao();
                ofertaIntegerRuntimeExceptionDao.create(oferta);

                RuntimeExceptionDao<LineaOferta, Integer> lineaOfertaIntegerRuntimeExceptionDao = helper.getLineaOfertaIntegerRuntimeExceptionDao();
                for (LineaOferta lineaOferta : lineas) {
                    lineaOferta.setOferta(oferta);
                    lineaOfertaIntegerRuntimeExceptionDao.create(lineaOferta);
                }
                cerrarFragmento();
            } else if (lineas.size() == 0) {
                Util.tostar("Debe introducir al menos una línea de oferta", getContext());
            } else if (!valid) {
                Util.tostar("Debe rellenar los campos indicados correctamente", getContext());
            }
        } else if ( v == btFechaOferta ) {
            // Handle clicks for btFechaOferta
            final Calendar cal = Calendar.getInstance();
            final DatePickerDialog datePickerDialog = new DatePickerDialog(
                    getContext(), this, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
            datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Fecha de Oferta", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DatePicker dp = datePickerDialog.getDatePicker();
                    int day = dp.getDayOfMonth();
                    int month = dp.getMonth();
                    int year = dp.getYear();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    cal.set(year, month, day);
                    cal.set(Calendar.HOUR_OF_DAY, 0);
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND, 0);
                    cal.set(Calendar.MILLISECOND, 0);
                    fechaOferta = cal.getTimeInMillis();
                    btFechaOferta.setText(simpleDateFormat.format(cal.getTime()));
                }
            });
            datePickerDialog.show();
        } else if ( v == btExpiracionOferta ) {
            // Handle clicks for button2
            final Calendar cal = Calendar.getInstance();
            final DatePickerDialog datePickerDialog = new DatePickerDialog(
                    getContext(), this, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
            datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Fecha de Expiración", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DatePicker dp = datePickerDialog.getDatePicker();
                    int day = dp.getDayOfMonth();
                    int month = dp.getMonth();
                    int year = dp.getYear();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    cal.set(year, month, day);
                    cal.set(Calendar.HOUR_OF_DAY, 0);
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND, 0);
                    cal.set(Calendar.MILLISECOND, 0);
                    expiracionOferta = cal.getTimeInMillis();
                    btExpiracionOferta.setText(simpleDateFormat.format(cal.getTime()));
                }
            });
            datePickerDialog.show();
        } else if ( v == ibAnadirLineaOferta ) {
            // Handle clicks for ibAnadirLineaOferta
            if (validarLineaOferta()) {
                LineaOferta lineaOferta = new LineaOferta();
                lineaOferta.setCantidad(Integer.parseInt(etCantidadAnual.getText().toString()));
                lineaOferta.setProducto((Producto) spProducto.getSelectedItem());
                lineas.add(lineaOferta);
                AdaptadorLineaOferta adaptadorLineaOferta = new AdaptadorLineaOferta(getContext(),R.layout.item_spinner_contactos,lineas);
                lvLineasOferta.setAdapter(adaptadorLineaOferta);

            }
        }
    }

    public void cargarAdaptadores() {
        List<Cliente> clientes = null;
        try {
            RuntimeExceptionDao<Cliente, Integer> runtimeDAO = helper.getClienteRuntimeDAO();
            clientes = runtimeDAO.queryForAll();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        adaptadorClientes = new AdaptadorClientes(getContext(), R.layout.item_lista, clientes);
        acClienteOferta.setAdapter(adaptadorClientes);

        // lista de representados
        List<Representado> representados = null;
        try {
            RuntimeExceptionDao<Representado, Integer> runtimeDAO = helper.getRepresentadoRuntimeDAO();
            representados = runtimeDAO.queryForAll();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        adaptadorRepresentados = new AdaptadorRepresentados(getContext(), R.layout.item_lista, representados);
        acRepresentadoOferta.setAdapter(adaptadorRepresentados);

    }

    private boolean validarLineaOferta(){
        boolean valido = true;



        if (acRepresentadoOferta.getText().toString().equals("")) {
            acRepresentadoOferta.setError("Debe introducir un representado");
            valido = false;
        } else {
            if (spProducto.getAdapter().getCount() == 0) {
                acRepresentadoOferta.setError("El representado no tiene ningun producto para ofertar");
                valido = false;
            }
        }



        if (etCantidadAnual.getText().toString().equals("")) {
            etCantidadAnual.setError("Debe introducir una cantidad anual");
            valido = false;
        }

        return valido;
    }

    private boolean validarOferta() {
        boolean valido = true;

        if (acClienteOferta.getText().toString().equals("")) {
            acClienteOferta.setError("Debe introducir un cliente");
            valido = false;
        }

        if (btFechaOferta.getText().toString().equals("FECHA")) {
            btFechaOferta.setError("Debe introducir una fecha para la oferta");
            valido = false;
        }

        if (acRepresentadoOferta.getText().toString().equals("")) {
            acRepresentadoOferta.setError("Debe introducir un representado");
            valido = false;
        }

        return valido;

    }

    private void cerrarFragmento() {
        getFragmentManager().beginTransaction().remove(this).commit();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }
}
