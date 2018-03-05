package com.cta.android.appcomercial.controladores;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.cta.android.appcomercial.R;
import com.cta.android.appcomercial.adaptadores.AdaptadorClientes;
import com.cta.android.appcomercial.adaptadores.AdaptadorContactoClienteSpinner;
import com.cta.android.appcomercial.adaptadores.AdaptadorContactoRepresentadoSpinner;
import com.cta.android.appcomercial.adaptadores.AdaptadorRepresentados;
import com.cta.android.appcomercial.model.Cargo;
import com.cta.android.appcomercial.model.Cliente;
import com.cta.android.appcomercial.model.ContactoCliente;
import com.cta.android.appcomercial.model.ContactoRepresentado;
import com.cta.android.appcomercial.model.Localidad;
import com.cta.android.appcomercial.model.Representado;
import com.cta.android.appcomercial.model.Visita;
import com.cta.android.appcomercial.util.DataBaseHelper;
import com.cta.android.appcomercial.util.Util;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by PC on 25/12/2017.
 */

public class AnadirVisitaFragment extends Fragment implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {
    private TextView tvFechaVisita;
    private Button btHoraInicio;
    private Button btHoraFinal;
    private Spinner spTipoEvento;
    private AutoCompleteTextView acEmpresa;
    private Spinner spContacto;
    private TextInputEditText etNotas;
    private ImageButton btGuardarVisita, btAtrasVisita, btBuscarEmpresa;
    private RadioButton rbCliente;
    private RadioButton rbRepresentado;
    private TimePickerDialog tpHoraInicio, tpHoraFinal;
    private Cliente cliente;
    private Representado representado;
    private int horainicio, horafinal;

    public static AnadirVisitaFragment newInstance(String contenido) {
        //1. crear el fragment
        //2. setear el contenido via argumentos
        //3. devolver el fragment creado y seteado con info

        AnadirVisitaFragment fragment = new AnadirVisitaFragment();
        Bundle bundle = new Bundle(1); // se puede especificar el numero de argumentos, en este caso 1
        bundle.putString("content", contenido);
        fragment.setArguments(bundle);
        return fragment;

    }

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2017-12-25 14:20:48 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-v.finder)
     */
    private void findViews(View v) {
        tvFechaVisita = (TextView) v.findViewById(R.id.tvFechaVisita);
        btHoraInicio = (Button) v.findViewById(R.id.btHoraInicio);
        btHoraFinal = (Button) v.findViewById(R.id.btHoraFinal);
        spTipoEvento = (Spinner) v.findViewById(R.id.spTipoEvento);
        acEmpresa = (AutoCompleteTextView) v.findViewById(R.id.acEmpresaVisita);
        spContacto = (Spinner) v.findViewById(R.id.spContacto);
        etNotas = (TextInputEditText) v.findViewById(R.id.etNotasCita);
        btGuardarVisita = (ImageButton) v.findViewById(R.id.btGuardarVisita);
        btAtrasVisita = (ImageButton) v.findViewById(R.id.btAtrasVisita);
        rbCliente = (RadioButton) v.findViewById(R.id.rbClienteVisita);
        rbRepresentado = (RadioButton) v.findViewById(R.id.rbRepresentadoVisita);
        btBuscarEmpresa = (ImageButton) v.findViewById(R.id.ibBuscaEmpresaVisita);

        btBuscarEmpresa.setOnClickListener(this);
        btHoraInicio.setOnClickListener(this);
        btHoraFinal.setOnClickListener(this);
        btGuardarVisita.setOnClickListener(this);
        btAtrasVisita.setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        String fecha = getArguments().getString("fecha");
        View view = inflater.inflate(R.layout.fragment_anadir_cita, container, false);
        findViews(view);
        tvFechaVisita.setText(fecha);


        List<String> list = new ArrayList<String>();
        list.add("VISITA");
        list.add("REUNION");
        list.add("OTROS");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                R.layout.item_lista2, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTipoEvento.setAdapter(dataAdapter);
        rbCliente.setChecked(true);
        cargarEmpresas();

        rbCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarEmpresas();
                spContacto.setAdapter(null);
                acEmpresa.setText("");
                acEmpresa.setEnabled(false);
                representado = null;
            }
        });
        rbRepresentado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarEmpresas();
                spContacto.setAdapter(null);
                acEmpresa.setText("");
                acEmpresa.setEnabled(false);
                cliente = null;
            }
        });

        acEmpresa.setEnabled(false);


        /**
         * Handle button click events<br />
         * <br />
         * Auto-created on 2017-12-25 14:20:48 by Android Layout Finder
         * (http://www.buzzingandroid.com/tools/android-layout-v.finder)
         */


        return view;
    }

    private void cargarEmpresas() {
        if (rbCliente.isChecked()) {
            List<Cliente> clientes = null;
            try {
                RuntimeExceptionDao<Cliente, Integer> runtimeDAO = OpenHelperManager.getHelper(getContext(), DataBaseHelper.class).getClienteRuntimeDAO();
                clientes = runtimeDAO.queryForAll();

            } catch (SQLException e) {
                e.printStackTrace();
            }
            AdaptadorClientes adaptadorClientes = new AdaptadorClientes(getContext(), R.layout.item_lista, clientes);
            acEmpresa.setAdapter(adaptadorClientes);
            acEmpresa.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    cliente = (Cliente) acEmpresa.getAdapter().getItem(position);
                    cargarContactos();
                    acEmpresa.setEnabled(false);
                }
            });
            representado = null;

        } else {
            List<Representado> representados = null;
            try {
                RuntimeExceptionDao<Representado, Integer> runtimeDAO = OpenHelperManager.getHelper(getContext(), DataBaseHelper.class).getRepresentadoRuntimeDAO();
                representados = runtimeDAO.queryForAll();

            } catch (SQLException e) {
                e.printStackTrace();
            }
            AdaptadorRepresentados adaptadorRepresentados = new AdaptadorRepresentados(getContext(), R.layout.item_lista, representados);
            acEmpresa.setAdapter(adaptadorRepresentados);
            acEmpresa.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    representado = (Representado) acEmpresa.getAdapter().getItem(position);
                    cargarContactos();
                    acEmpresa.setEnabled(false);
                }
            });
            cliente = null;

        }
    }

    private void cargarContactos() {
        if (rbCliente.isChecked() && cliente != null) {
            RuntimeExceptionDao<ContactoCliente, Integer> contactoClientesDao = OpenHelperManager.getHelper(getContext(), DataBaseHelper.class).getContactoClienteRuntimeDAO();
            List<ContactoCliente> contactos = contactoClientesDao.queryForEq("cliente_id", cliente.getId());
            AdaptadorContactoClienteSpinner adaptadorContactoClienteSpinner = new AdaptadorContactoClienteSpinner(getContext(), R.layout.item_spinner_contactos, contactos);
            spContacto.setAdapter(adaptadorContactoClienteSpinner);
        } else if (rbRepresentado.isChecked() && representado != null) {
            RuntimeExceptionDao<ContactoRepresentado, Integer> contactoRepresentadosDao = OpenHelperManager.getHelper(getContext(), DataBaseHelper.class).getContactoRepresentadoRuntimeDAO();
            List<ContactoRepresentado> contactos = contactoRepresentadosDao.queryForEq("representado_id", representado.getId());
            AdaptadorContactoRepresentadoSpinner adaptadorContactoRepresentadoSpinner = new AdaptadorContactoRepresentadoSpinner(getContext(), R.layout.item_spinner_contactos, contactos);
            spContacto.setAdapter(adaptadorContactoRepresentadoSpinner);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btHoraInicio) {
            // Handle clicks for btHoraInicio
            tpHoraInicio = new TimePickerDialog(getContext(), this, 9, 0, DateFormat.is24HourFormat(getContext()));
            tpHoraInicio.setTitle("Seleccione la hora de inicio:");
            tpHoraInicio.show();
        } else if (v == btHoraFinal) {
            // Handle clicks for button2
            tpHoraFinal = new TimePickerDialog(getContext(), this, 9, 0, DateFormat.is24HourFormat(getContext()));
            tpHoraFinal.setTitle("Seleccione la hora de finalización:");
            tpHoraFinal.show();
        } else if (v == btGuardarVisita) {
            // Handle clicks for btGuardarVisita
            if (validarVisita()) {

                Visita visita = new Visita();
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    cal.setTime(sdf.parse(getArguments().getString("fecha")));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                int hora = horainicio / 100;
                cal.add(Calendar.HOUR, hora);
                int minuto = (horainicio - hora * 100);
                cal.add(Calendar.MINUTE, minuto);


                visita.setFechainicio(cal.getTimeInMillis());
                String titulo = "";


                titulo = titulo + spTipoEvento.getSelectedItem().toString() + " - ";

                if (rbCliente.isChecked()) {
                    titulo = titulo + "CLIENTE: ";
                    visita.setCliente(cliente);
                } else {
                    titulo = titulo + "REPRES: ";
                    visita.setRepresentado(representado);
                }


                titulo = titulo + acEmpresa.getText().toString();
                if (spContacto.getSelectedItem() != null) {
                    try {
                        ContactoCliente contactoCliente = (ContactoCliente) spContacto.getSelectedItem();
                        visita.setContactoCliente(contactoCliente);
                        titulo = titulo + "\n" + contactoCliente.getNombre() + " " + contactoCliente.getApellidos();
                        if (contactoCliente.getCargo() != null) {
                            Cargo cargo = (Cargo) OpenHelperManager.getHelper(getContext(), DataBaseHelper.class).getCargoRuntimeDAO().queryForId(contactoCliente.getCargo().getId());
                            titulo = titulo + " - " + cargo.getNombre();
                        }
                        if (cliente.getLocalidad() != null) {
                            Localidad localidad = (Localidad) OpenHelperManager.getHelper(getContext(), DataBaseHelper.class).getLocalidadRuntimeDAO().queryForId(cliente.getLocalidad().getId());
                            visita.setEmplazamiento(cliente.getDireccion() + ", " + localidad.getNombre());
                        }

                    } catch (ClassCastException cce) {
                        ContactoRepresentado contactoRepresentado = (ContactoRepresentado) spContacto.getSelectedItem();
                        visita.setContactoRepresentado(contactoRepresentado);
                        titulo = titulo + "\n" + contactoRepresentado.getNombre() + " " + contactoRepresentado.getApellidos();
                        if (contactoRepresentado.getCargo() != null) {
                            Cargo cargo = (Cargo) OpenHelperManager.getHelper(getContext(), DataBaseHelper.class).getCargoRuntimeDAO().queryForId(contactoRepresentado.getCargo().getId());
                            titulo = titulo + " - " + cargo.getNombre();
                        }
                        if (representado.getLocalidad() != null) {
                            Localidad localidad = (Localidad) OpenHelperManager.getHelper(getContext(), DataBaseHelper.class).getLocalidadRuntimeDAO().queryForId(representado.getLocalidad().getId());
                            visita.setEmplazamiento(representado.getDireccion() + ", " + localidad.getNombre());
                        }
                    }
                }
                visita.setTitulo(titulo);
                visita.setNotas(etNotas.getText().toString());


                if (horafinal != 0) {

                    Calendar cal2 = Calendar.getInstance();
                    try {
                        cal2.setTime(sdf.parse(getArguments().getString("fecha")));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    int horaf = horafinal / 100;
                    cal2.add(Calendar.HOUR, horaf);
                    int minutof = (horafinal - horaf * 100);
                    cal2.add(Calendar.MINUTE, minutof);


                    visita.setFechafinal(cal2.getTimeInMillis());

                }

                try {
                    Dao<Visita, Integer> visitasDao = OpenHelperManager.getHelper(getContext(), DataBaseHelper.class).getVisitaDao();
                    visitasDao.create(visita);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                cerrarFragmento();

            }

        } else if (v == btBuscarEmpresa) {
            if (acEmpresa.getAdapter().getCount() > 0) {
                acEmpresa.setEnabled(true);
                acEmpresa.requestFocus();
                acEmpresa.setText("");
                Util.abrirTeclado(getContext(), acEmpresa);
            } else {
                Util.tostar("No existen empresas en la base de datos", getContext());
            }
        } else if (v == btAtrasVisita) {
            cerrarFragmento();
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String hora = "";
        String minuto = "";
        if (hourOfDay < 10) {
            hora = "0" + hourOfDay;
        } else {
            hora = "" + hourOfDay;
        }

        if (minute < 10) {
            minuto = "0" + minute;
        } else {
            minuto = "" + minute;
        }

        if (tpHoraInicio != null) {
            int inicio = hourOfDay * 100 + minute;
            if (inicio < horafinal || horafinal == 0) {
                btHoraInicio.setText(hora + ":" + minuto);
                tpHoraInicio = null;
                horainicio = inicio;
                btHoraInicio.setError(null);
            } else {
                Util.tostar("La hora inicial es posterior a la final", getContext());
            }
        } else if (tpHoraFinal != null) {
            int hfinal = hourOfDay * 100 + minute;
            if (hfinal > horainicio && horainicio != 0) {
                btHoraFinal.setText(hora + ":" + minuto);
                horafinal = hfinal;
                tpHoraFinal = null;
            } else {
                if (horainicio != 0) {
                    Util.tostar("La hora final es anterior a la inicial", getContext());
                } else {
                    Util.tostar("Debe establecer una hora de inicio", getContext());
                }
            }
        }

    }

    private void cerrarFragmento() {
        getFragmentManager().beginTransaction().remove(this).commit();
        VisitasFragment visitasFragment = (VisitasFragment) this.getParentFragment();
        visitasFragment.cargarVisitas();

    }

    private boolean validarVisita() {
        boolean valid = true;
        if (horainicio == 0) {
            btHoraInicio.setError("Debe establecer una hora de inicio");
            valid = false;
        }

        if (acEmpresa.getText().toString().equals("")) {
            acEmpresa.setError("Debe seleccionar una empresa para la visita");
            valid = false;
        }
        return valid;
    }

}
