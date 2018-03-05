package com.cta.android.appcomercial.controladores;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.IdRes;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cta.android.appcomercial.R;
import com.cta.android.appcomercial.adaptadores.AdaptadorClientes;
import com.cta.android.appcomercial.adaptadores.AdaptadorComunidades;
import com.cta.android.appcomercial.adaptadores.AdaptadorLocalidades;
import com.cta.android.appcomercial.adaptadores.AdaptadorProvincias;
import com.cta.android.appcomercial.adaptadores.AdaptadorRepresentados;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC on 30/10/2017.
 */

public class EmpresasFragment extends Fragment implements View.OnLongClickListener {

    private FragmentManager fragmentManager;

    private DataBaseHelper helper;

    private AdaptadorClientes adaptadorClientes;
    private AdaptadorRepresentados adaptadorRepresentados;

    private Cliente clienteSelect;
    private Representado representadoSelect;

    private AutoCompleteTextView autoCompleteTextViewEmpresas;
    private TextView tvdRazonSocial;
    private TextView tvdCif;
    private TextView tvdTelefono;
    private TextView tvdEmail;
    private TextView tvdCoordenadas;
    private TextView tvdEmpleados;
    private TextView tvdFacturacion;
    private TextView tvdDireccion;
    private TextView tvdCp;
    private TextView tvdWeb;
    private TextView tvdNombreComercial;
    private TextView tvdLocalidad;
    private TextView tvdProvincia;

    private RadioGroup rgEmpresas;
    private RadioButton rbClientes;
    private RadioButton rbRepresentados;


    private ImageButton btBuscarEmpresa;
    private ImageButton btAnadir;
    private ImageButton btContactos;
    private ImageButton btAlternativo;
    private ImageButton btLlamada;
    private ImageButton btNavegar;
    private ImageButton btRuta;
    private ImageButton btEmail;
    private ImageButton btEliminar;

    private ConstraintLayout framEmpresas;

    public static EmpresasFragment newInstance() {
        EmpresasFragment fragment = new EmpresasFragment();
        return fragment;

    }

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2017-11-28 17:59:15 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews(View view) {
        autoCompleteTextViewEmpresas = (AutoCompleteTextView) view.findViewById(R.id.acBuscarEmpresa);
        autoCompleteTextViewEmpresas.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
        tvdRazonSocial = (TextView) view.findViewById(R.id.tvvRazonSocial);
        tvdRazonSocial.setOnLongClickListener(this);
        tvdCif = (TextView) view.findViewById(R.id.tvvCif);
        tvdCif.setOnLongClickListener(this);
        tvdTelefono = (TextView) view.findViewById(R.id.tvvTelefonoEmpresa);
        tvdTelefono.setOnLongClickListener(this);
        tvdEmail = (TextView) view.findViewById(R.id.tvvEmailEmpresa);
        tvdEmail.setOnLongClickListener(this);
        tvdCoordenadas = (TextView) view.findViewById(R.id.tvvCoordenadas);
        tvdCoordenadas.setOnLongClickListener(this);
        tvdEmpleados = (TextView) view.findViewById(R.id.tvvEmpleados);
        tvdEmpleados.setOnLongClickListener(this);
        tvdFacturacion = (TextView) view.findViewById(R.id.tvvFacturacionEmpresa);
        tvdFacturacion.setOnLongClickListener(this);
        tvdDireccion = (TextView) view.findViewById(R.id.tvvDireccion);
        tvdDireccion.setOnLongClickListener(this);
        tvdCp = (TextView) view.findViewById(R.id.tvvCP);
        tvdCp.setOnLongClickListener(this);
        tvdWeb = (TextView) view.findViewById(R.id.tvvWeb);
        tvdWeb.setOnLongClickListener(this);
        tvdNombreComercial = (TextView) view.findViewById(R.id.tvvNombreComercial);
        tvdNombreComercial.setOnLongClickListener(this);
        tvdLocalidad = (TextView) view.findViewById(R.id.tvvLocalidad);
        tvdLocalidad.setOnLongClickListener(this);
        tvdProvincia = (TextView) view.findViewById(R.id.tvvProvincia);
        tvdProvincia.setOnLongClickListener(this);

        btAnadir = (ImageButton) view.findViewById(R.id.ibAnadirEmpresa);
        btContactos = (ImageButton) view.findViewById(R.id.ibListaContactos);
        btAlternativo = (ImageButton) view.findViewById(R.id.ibConfiguracionEmpresa);
        btBuscarEmpresa = (ImageButton) view.findViewById(R.id.ibBuscarEmpresa);
        btEliminar = (ImageButton) view.findViewById(R.id.ibEliminarEmpresa);
        btEmail = (ImageButton) view.findViewById(R.id.ibEmailEmpresa);
        btNavegar = (ImageButton) view.findViewById(R.id.ibNavegarEmpresa);
        btRuta = (ImageButton) view.findViewById(R.id.ibRutaEmpresa);
        btLlamada = (ImageButton) view.findViewById(R.id.ibLlamadaEmpresa);

        rgEmpresas = (RadioGroup) view.findViewById(R.id.rgEmpresas);
        rbClientes = (RadioButton) view.findViewById(R.id.rbClientes);
        rbRepresentados = (RadioButton) view.findViewById(R.id.rbRepresentados);

        framEmpresas = (ConstraintLayout) view.findViewById(R.id.layoutEmpresas);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_empresas, container, false);
        findViews(view);
        autoCompleteTextViewEmpresas.setEnabled(false);

        fragmentManager = getChildFragmentManager();
        rbClientes.setChecked(true);
        helper = OpenHelperManager.getHelper(getContext(), DataBaseHelper.class);

        cargarAdaptadores();
        cargarAutoComplete();

        btBuscarEmpresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((rbClientes.isChecked() && !adaptadorClientes.isEmpty()) || (rbRepresentados.isChecked() && !adaptadorRepresentados.isEmpty())) {
                    autoCompleteTextViewEmpresas.setEnabled(true);
                    autoCompleteTextViewEmpresas.requestFocus();
                    autoCompleteTextViewEmpresas.setText("");
                    Util.abrirTeclado(getContext(), autoCompleteTextViewEmpresas);
                } else {
                    if (rbClientes.isChecked()) {
                        Util.tostar("No existen clientes en la base de datos", getContext());
                    } else {
                        Util.tostar("No existen representados en la base de datos", getContext());
                    }
                }

            }
        });

        btAnadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                autoCompleteTextViewEmpresas.setEnabled(false);

                DatosEmpresaFragment datosEmpresaFragment = new DatosEmpresaFragment();
                Bundle bundle = new Bundle(1); // se puede especificar el numero de argumentos, en este caso 1
                if (rbClientes.isChecked()) {
                    bundle.putString("empresa", "cliente");
                } else {
                    bundle.putString("empresa", "representado");
                }
                datosEmpresaFragment.setArguments(bundle);
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.framEmpresas, datosEmpresaFragment, "datosEmpresa");
                transaction.commit();


            }
        });

        rgEmpresas.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                autoCompleteTextViewEmpresas.setText("");
                cargarAutoComplete();

                if (clienteSelect != null) {
                    cargarDetalleCliente(new Cliente());
                } else if (representadoSelect != null) {
                    cargarDetalleRepresentado(new Representado());
                }

                clienteSelect = null;
                representadoSelect = null;
                autoCompleteTextViewEmpresas.setEnabled(false);

            }
        });

        autoCompleteTextViewEmpresas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                autoCompleteTextViewEmpresas.setEnabled(false);

                if (rbClientes.isChecked()) {
                    clienteSelect = (Cliente) adaptadorClientes.getItem(position);
                    cargarDetalleCliente(clienteSelect);


                } else {
                    representadoSelect = (Representado) adaptadorRepresentados.getItem(position);
                    cargarDetalleRepresentado(representadoSelect);

                }

            }
        });

        btAlternativo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (rbClientes.isChecked() && clienteSelect != null) {
                    SectoresFragment sectoresFragment = new SectoresFragment();
                    Bundle bundle = new Bundle(1);
                    bundle.putParcelable("empresa", (Parcelable) clienteSelect);
                    sectoresFragment.setArguments(bundle);
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.framEmpresas, sectoresFragment, "sectoresEmpresa");
                    transaction.commit();
                    for (int x = 0; x < framEmpresas.getChildCount(); x++) {
                        framEmpresas.getChildAt(x).setEnabled(false);
                    }
                } else if (representadoSelect != null && rbRepresentados.isChecked()){
                    ListaProductosFragment productoFragment = new ListaProductosFragment();
                    Bundle bundle = new Bundle(1);
                    bundle.putParcelable("empresa", representadoSelect);
                    productoFragment.setArguments(bundle);
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.framEmpresas, productoFragment, "productosEmpresa");
                    transaction.commit();
                    for (int x = 0; x < framEmpresas.getChildCount(); x++) {
                        framEmpresas.getChildAt(x).setEnabled(false);
                    }
                }

            }
        });

        btContactos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clienteSelect != null || representadoSelect != null) {
                    ListaContactosFragment listaContactosFragment = new ListaContactosFragment();
                    Bundle bundle = new Bundle(1);
                    if (rbClientes.isChecked()) {
                        bundle.putParcelable("empresa", (Parcelable) clienteSelect);
                    } else {
                        bundle.putParcelable("empresa", (Parcelable) representadoSelect);
                    }
                    listaContactosFragment.setArguments(bundle);
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.framEmpresas, listaContactosFragment, "listaContactos");
                    transaction.commit();
                    for (int x = 0; x < framEmpresas.getChildCount(); x++) {
                        framEmpresas.getChildAt(x).setEnabled(false);
                    }
                }


            }
        });

        btEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreEmpresa = "";
                String tipoEmpresa = "";
                boolean seleccion = false;
                if (clienteSelect != null) {
                    nombreEmpresa = clienteSelect.getNombreComercial();
                    tipoEmpresa = "cliente";
                    seleccion = true;
                } else if (representadoSelect != null) {
                    nombreEmpresa = representadoSelect.getNombreComercial();
                    tipoEmpresa = "representado";
                    seleccion = true;
                }
                if (seleccion) {
                    new AlertDialog.Builder(getContext())
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Eliminar Empresa")
                            .setMessage("¿Confirma que desea eliminar el " + tipoEmpresa + ": " + nombreEmpresa + " de la base de datos?")
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (clienteSelect != null) {
                                        try {
                                            RuntimeExceptionDao<Cliente, Integer> clienteDao = helper.getClienteRuntimeDAO();
                                            clienteDao.delete(clienteSelect);
                                            cargarAdaptadores();
                                            cargarAutoComplete();
                                            cargarDetalleCliente(new Cliente());
                                            autoCompleteTextViewEmpresas.setText("");
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        }
                                    } else if (representadoSelect != null) {
                                        try {
                                            RuntimeExceptionDao<Representado, Integer> repDao = helper.getRepresentadoRuntimeDAO();
                                            repDao.delete(representadoSelect);
                                            cargarAdaptadores();
                                            cargarAutoComplete();
                                            cargarDetalleRepresentado(new Representado());
                                            autoCompleteTextViewEmpresas.setText("");
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                }

                            })
                            .setNegativeButton("No", null)
                            .show();
                }

            }
        });

        btLlamada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clienteSelect != null) {
                    if (clienteSelect.getTelefono() != 0) {
                        Util.llamada(clienteSelect.getTelefono(), getContext(), getActivity());
                    }
                } else if (representadoSelect != null) {
                    if (representadoSelect.getTelefono() != 0) {
                        Util.llamada(representadoSelect.getTelefono(), getContext(), getActivity());
                    }
                }
            }
        });

        btEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clienteSelect != null) {
                    if (clienteSelect.getEmail() != null && !clienteSelect.getEmail().equals("")) {
                        ArrayList<String> email = new ArrayList<String>();
                        email.add(clienteSelect.getEmail());
                        Util.sendEmail(email, getContext());
                    }
                } else if (representadoSelect != null) {
                    if (representadoSelect.getEmail() != null && !representadoSelect.getEmail().equals("")) {
                        ArrayList<String> email = new ArrayList<String>();
                        email.add(representadoSelect.getEmail());
                        Util.sendEmail(email, getContext());
                    }
                }
            }
        });

        btNavegar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clienteSelect != null) {
                    if (clienteSelect.getWeb() != null) {
                        Util.verWeb(clienteSelect.getWeb(), getContext());
                    }
                } else if (representadoSelect != null) {
                    if (representadoSelect.getWeb() != null) {
                        Util.verWeb(representadoSelect.getWeb(), getContext());
                    }
                }
            }
        });

        btRuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clienteSelect != null) {
                    if ((clienteSelect.getDireccion() != null && !clienteSelect.getDireccion().equals("")) && clienteSelect.getLocalidad() != null) {
                        try {
                            String localidad = helper.getLocalidadDAO().queryForId(clienteSelect.getLocalidad().getId()).getNombre();
                            Util.abrirGPS(clienteSelect.getDireccion() + " " + localidad, getContext());
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    }
                } else if (representadoSelect != null) {
                    if ((representadoSelect.getDireccion() != null && !representadoSelect.getDireccion().equals("")) && representadoSelect.getLocalidad() != null) {
                        try {
                            String localidad = helper.getLocalidadDAO().queryForId(representadoSelect.getLocalidad().getId()).getNombre();
                            Util.abrirGPS(representadoSelect.getDireccion() + " " + localidad, getContext());
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        });

        return view;

    }


    public void cargarAutoComplete() {
        if (rbClientes.isChecked()) {
            autoCompleteTextViewEmpresas.setAdapter(adaptadorClientes);
        } else {
            autoCompleteTextViewEmpresas.setAdapter(adaptadorRepresentados);
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

        // lista de representados
        List<Representado> representados = null;
        try {
            RuntimeExceptionDao<Representado, Integer> runtimeDAO = helper.getRepresentadoRuntimeDAO();
            representados = runtimeDAO.queryForAll();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        adaptadorRepresentados = new AdaptadorRepresentados(getContext(), R.layout.item_lista, representados);

    }

    public void cargarDetalleCliente(Cliente cliente) {


        autoCompleteTextViewEmpresas.setText("");

        tvdNombreComercial.setText(cliente.getNombreComercial());
        tvdRazonSocial.setText(cliente.getRazonSocial());
        if (cliente.getCodigoPostal() != 0) {
            tvdCp.setText(String.valueOf(cliente.getCodigoPostal()));
        } else {
            tvdCp.setText("");
        }
        tvdCif.setText(cliente.getCif());
        if (cliente.getTelefono() != 0) {
            tvdTelefono.setText(String.valueOf(cliente.getTelefono()));
        } else {
            tvdTelefono.setText("");
        }
        tvdEmail.setText(cliente.getEmail());
        tvdCoordenadas.setText(cliente.getCoordenadas());
        tvdDireccion.setText(cliente.getDireccion());
        tvdWeb.setText(cliente.getWeb());
        if (cliente.getNumEmpleados() != 0) {
            tvdEmpleados.setText(String.valueOf(cliente.getNumEmpleados()));
        } else {
            tvdEmpleados.setText("");
        }
        if (cliente.getFacturacion() != 0) {
            tvdFacturacion.setText(String.valueOf(cliente.getFacturacion()));
        } else {
            tvdFacturacion.setText("");
        }
        if (cliente.getLocalidad() != null) {
            try {
                Dao<Localidad, Integer> localidadDao = helper.getLocalidadDAO();
                Localidad loc = localidadDao.queryForId(cliente.getLocalidad().getId());
                tvdLocalidad.setText(loc.getNombre());
                Dao<Provincia, Integer> provinciaDao = helper.getProvinciaDAO();
                tvdProvincia.setText(provinciaDao.queryForId(loc.getProvincia().getId()).getNombre());

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } else {
            tvdLocalidad.setText("");
            tvdProvincia.setText("");
        }

    }

    public void cargarDetalleRepresentado(Representado representado) {


        autoCompleteTextViewEmpresas.setText("");

        tvdNombreComercial.setText(representado.getNombreComercial());
        tvdRazonSocial.setText(representado.getRazonSocial());
        if (representado.getCodigoPostal() != 0) {
            tvdCp.setText(String.valueOf(representado.getCodigoPostal()));
        } else {
            tvdCp.setText("");
        }
        tvdCif.setText(representado.getCif());
        if (representado.getTelefono() != 0) {
            tvdTelefono.setText(String.valueOf(representado.getTelefono()));
        } else {
            tvdTelefono.setText("");
        }
        tvdEmail.setText(representado.getEmail());
        tvdCoordenadas.setText(representado.getCoordenadas());
        tvdDireccion.setText(representado.getDireccion());
        tvdWeb.setText(representado.getWeb());
        if (representado.getNumEmpleados() != 0) {
            tvdEmpleados.setText(String.valueOf(representado.getNumEmpleados()));
        } else {
            tvdEmpleados.setText("");
        }
        if (representado.getFacturacion() != 0) {
            tvdFacturacion.setText(String.valueOf(representado.getFacturacion()));
        } else {
            tvdFacturacion.setText("");
        }
        if (representado.getLocalidad() != null) {
            try {
                Dao<Localidad, Integer> localidadDao = helper.getLocalidadDAO();
                Localidad loc = localidadDao.queryForId(representado.getLocalidad().getId());
                tvdLocalidad.setText(loc.getNombre());
                Dao<Provincia, Integer> provinciaDao = helper.getProvinciaDAO();
                tvdProvincia.setText(provinciaDao.queryForId(loc.getProvincia().getId()).getNombre());

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } else {
            tvdLocalidad.setText("");
            tvdProvincia.setText("");
        }

    }

    @Override
    public boolean onLongClick(View v) {

        if ((clienteSelect != null || representadoSelect != null) && !(v.getId() == R.id.tvvLocalidad || v.getId() == R.id.tvvProvincia)) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            final EditText input = new EditText(builder.getContext());
            builder.setView(input);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            switch (v.getId()) {
                case R.id.tvvNombreComercial:
                    input.setHint("Nombre comercial");
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    break;
                case R.id.tvvWeb:
                    input.setHint("Dirección web");
                    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_URI);
                    break;
                case R.id.tvvCP:
                    input.setHint("Código Postal");
                    input.setInputType(InputType.TYPE_CLASS_NUMBER);
                    break;
                case R.id.tvvDireccion:
                    input.setHint("Dirección");
                    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS);
                    break;
                case R.id.tvvFacturacionEmpresa:
                    input.setHint("Facturación");
                    input.setInputType(InputType.TYPE_CLASS_NUMBER);
                    break;
                case R.id.tvvEmpleados:
                    input.setHint("Número de empleados");
                    input.setInputType(InputType.TYPE_CLASS_NUMBER);
                    break;
                case R.id.tvvCoordenadas:
                    input.setHint("Coordenadas");
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    break;
                case R.id.tvvTelefonoEmpresa:
                    input.setHint("Teléfono");
                    input.setInputType(InputType.TYPE_CLASS_NUMBER);
                    break;
                case R.id.tvvCif:
                    input.setHint("CIF");
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    break;
                case R.id.tvvRazonSocial:
                    input.setHint("Razón Social");
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    break;
                case R.id.tvvEmailEmpresa:
                    input.setHint("Email");
                    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                    break;
                default:
                    break;
            }
            final AlertDialog dialog = builder.create();
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            dialog.show();

            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {

                }
            });

            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean cierre = true;
                    if (clienteSelect != null) {
                        String mensaje = "";
                        try {
                            cierre = updateCliente(input);
                        } catch (SQLException e) {
                            Toast.makeText(getContext(), "Ha ocurrido un error al registrar los cambios en la base de datos.", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            cierre = updateRepresentado(input);
                        } catch (SQLException e) {
                            Toast.makeText(getContext(), "Ha ocurrido un error al registrar los cambios en la base de datos.", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                    if (cierre) {
                        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
                        dialog.dismiss();
                    }
                }
            });


        } else if (clienteSelect != null || representadoSelect != null) {
            createLocalidadDialogo().show();
        }
        return false;

    }

    public AlertDialog createLocalidadDialogo() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialogolocalidad, null);
        builder.setView(v);

        final RuntimeExceptionDao<ComunidadAutonoma, Integer> daoCom = helper.getComunidadAutonomaRuntimeDAO();
        List<ComunidadAutonoma> comunidades = daoCom.queryForAll();
        ComunidadAutonoma ca = new ComunidadAutonoma();
        ca.setNombre(getResources().getString(R.string.selectCom));
        comunidades.add(0, ca);

        final Spinner spinnerCom = (Spinner) v.findViewById(R.id.spinnerUpdateCom);
        final Spinner spinnerProv = (Spinner) v.findViewById(R.id.spinnerUpdateProv);
        final Spinner spinnerLoc = (Spinner) v.findViewById(R.id.spinnerUpdateLoc);

        final AdaptadorComunidades adaptadorComunidades = new AdaptadorComunidades(getContext(), R.layout.item_lista, comunidades);
        spinnerCom.setAdapter(adaptadorComunidades);

        // si la empresa seleccionada tiene una localidad cargamos esos valores
        Localidad localidadEmpresa = null;
        if (clienteSelect != null) {
            localidadEmpresa = clienteSelect.getLocalidad();
        } else if (representadoSelect != null) {
            localidadEmpresa = representadoSelect.getLocalidad();
        }
        if (localidadEmpresa != null) {
            try {
                Dao<Localidad, Integer> localidadDao = helper.getLocalidadDAO();
                Localidad loc = localidadDao.queryForId(localidadEmpresa.getId());
                Dao<Provincia, Integer> provinciaDao = helper.getProvinciaDAO();
                Provincia prov = provinciaDao.queryForId(loc.getProvincia().getId());
                Dao<ComunidadAutonoma, Integer> comunidadDao = helper.getComunidadAutonomaDAO();
                ComunidadAutonoma com = comunidadDao.queryForId(prov.getComunidadAutonoma().getId());

                List<ComunidadAutonoma> listacom = comunidadDao.queryForAll();
                int comindex = 0;
                for (int x = 0; x < listacom.size(); x++) {
                    if (listacom.get(x).getNombre().equals(com.getNombre())) {
                        comindex = x + 1;
                    }
                }
                spinnerCom.setSelection(comindex);

                List<Provincia> provincias = provinciaDao.queryForEq("comunidadAutonoma_id", com.getId());
                Provincia provincia = new Provincia();
                provincia.setNombre(getResources().getString(R.string.selectProv));
                provincias.add(0, provincia);
                AdaptadorProvincias adaptadorProvincias = new AdaptadorProvincias(getContext(), R.layout.item_lista, provincias);
                spinnerProv.setAdapter(adaptadorProvincias);
                spinnerProv.setEnabled(true);


                List<Provincia> listaprov = provinciaDao.queryForEq("comunidadAutonoma_id", com.getId());
                int provindex = 0;
                for (int x = 0; x < listaprov.size(); x++) {
                    if (listaprov.get(x).getNombre().equals(prov.getNombre())) {
                        provindex = x + 1;
                    }
                }
                spinnerProv.setSelection(provindex);

                List<Localidad> localidades = localidadDao.queryForEq("provincia_id", prov.getId());
                Localidad localidad = new Localidad();
                localidad.setNombre(getResources().getString(R.string.selectLoc));
                localidades.add(0, loc);
                AdaptadorLocalidades adaptadorLocalidades = new AdaptadorLocalidades(getContext(), R.layout.item_lista, localidades);
                spinnerLoc.setAdapter(adaptadorLocalidades);

                List<Localidad> listaloc = localidadDao.queryForEq("provincia_id", prov.getId());
                int locindex = 0;
                for (int x = 0; x < listaloc.size(); x++) {
                    if (listaloc.get(x).getNombre().equals(loc.getNombre())) {
                        locindex = x + 1;
                    }
                }
                spinnerLoc.setSelection(locindex);

                spinnerProv.setEnabled(false);
                spinnerLoc.setEnabled(false);


            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // listeners para spinners
        spinnerCom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ComunidadAutonoma comunidadAutonomaSelect = (ComunidadAutonoma) spinnerCom.getSelectedItem();
                if (comunidadAutonomaSelect.getId() != 0) {
                    if (spinnerProv.isEnabled()) {
                        RuntimeExceptionDao<Provincia, Integer> daoProv = helper.getProvinciaRuntimeDAO();
                        List<Provincia> provincias = daoProv.queryForEq("comunidadAutonoma_id", comunidadAutonomaSelect.getId());
                        Provincia provincia = new Provincia();
                        provincia.setNombre(getResources().getString(R.string.selectProv));
                        provincias.add(0, provincia);

                        AdaptadorProvincias adaptadorProvincias = new AdaptadorProvincias(getContext(), R.layout.item_lista, provincias);
                        spinnerProv.setAdapter(adaptadorProvincias);
                        spinnerProv.setEnabled(true);
                    } else {
                        spinnerProv.setEnabled(true);
                    }

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
                    if (spinnerLoc.isEnabled()) {
                        List<Localidad> localidades = daoLoc.queryForEq("provincia_id", provincia.getId());
                        Localidad loc = new Localidad();
                        loc.setNombre(getResources().getString(R.string.selectLoc));
                        localidades.add(0, loc);
                        AdaptadorLocalidades adaptadorLocalidades = new AdaptadorLocalidades(getContext(), R.layout.item_lista, localidades);
                        spinnerLoc.setAdapter(adaptadorLocalidades);
                        spinnerLoc.setEnabled(true);
                    } else {
                        spinnerLoc.setEnabled(true);
                    }

                } else {
                    spinnerLoc.setAdapter(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        final AlertDialog dialog = builder.create();
        Button guardarLoc = (Button) v.findViewById(R.id.btUpdateLoc);
        guardarLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((Localidad) spinnerLoc.getSelectedItem() != null) {
                    if (!((Localidad) spinnerLoc.getSelectedItem()).getNombre().equals(getResources().getString(R.string.selectLoc))) {
                        if (clienteSelect != null) {
                            clienteSelect.setLocalidad((Localidad) spinnerLoc.getSelectedItem());
                            try {
                                Dao<Cliente, Integer> cliDao = helper.getClienteDAO();
                                cliDao.update(clienteSelect);
                                tvdLocalidad.setText(clienteSelect.getLocalidad().getNombre());
                                tvdProvincia.setText(((Provincia) spinnerProv.getSelectedItem()).getNombre());
                                dialog.dismiss();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        } else if (representadoSelect != null) {
                            representadoSelect.setLocalidad((Localidad) spinnerLoc.getSelectedItem());
                            try {
                                Dao<Representado, Integer> cliDao = helper.getRepresentadoDAO();
                                cliDao.update(representadoSelect);
                                tvdLocalidad.setText(representadoSelect.getLocalidad().getNombre());
                                tvdProvincia.setText(((Provincia) spinnerProv.getSelectedItem()).getNombre());
                                dialog.dismiss();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }
            }
        });


        return dialog;
    }

    private boolean updateCliente(EditText input) throws SQLException {

        String campo = input.getHint().toString();
        String nuevoValor = input.getText().toString();
        boolean esValido = true;

        switch (campo) {
            case "Nombre comercial":
                if (!nuevoValor.equals("")) {
                    Dao<Cliente, Integer> clientesDao = helper.getClienteDAO();
                    List<Cliente> lista = clientesDao.queryForAll();
                    boolean nombreExiste = false;
                    for (Cliente c : lista) {
                        if (c.getNombreComercial().equals(nuevoValor.toUpperCase())) {
                            nombreExiste = true;
                        }
                    }
                    if (!nombreExiste) {
                        clienteSelect.setNombreComercial(nuevoValor.toUpperCase());
                    } else {
                        input.setError("Ya existe un cliente con ese nombre");
                        esValido = false;
                    }
                } else {
                    input.setError("Debe introducir un valor");
                    esValido = false;
                }
                break;
            case "Dirección web":
                UrlValidator urlValidator = new UrlValidator();
                if (urlValidator.isValid("http://" + nuevoValor)) {
                    clienteSelect.setWeb(("http://" + nuevoValor).toLowerCase());
                } else {
                    input.setError("La direccion web introducida no es válida.");
                    esValido = false;
                }
                break;
            case "Código Postal":
                try {
                    if (Util.validarCP(Integer.parseInt(nuevoValor))) {
                        clienteSelect.setCodigoPostal(Integer.parseInt(nuevoValor));

                    } else {
                        input.setError("El CP introducido no existe");
                        esValido = false;
                    }

                } catch (NumberFormatException nfe) {
                    input.setError("Debe introducir un valor numérico");
                    esValido = false;
                }
                break;
            case "Dirección":
                clienteSelect.setDireccion(nuevoValor);
                break;
            case "Facturación":
                try {
                    clienteSelect.setFacturacion(Integer.parseInt(nuevoValor));
                } catch (NumberFormatException nfe) {
                    input.setError("Debe introducir un valor numérico entero.");
                    esValido = false;
                }
                break;
            case "Número de empleados":
                try {
                    clienteSelect.setNumEmpleados(Integer.parseInt(nuevoValor));
                } catch (NumberFormatException nfe) {
                    input.setError("Debe introducir un valor numérico entero.");
                    esValido = false;
                }
                break;
            case "Coordenadas":
                clienteSelect.setCoordenadas(nuevoValor);
                break;
            case "Teléfono":
                try {
                    if (Util.validarTelefono(Integer.parseInt(nuevoValor))) {
                        clienteSelect.setTelefono(Integer.parseInt(nuevoValor));
                    } else {
                        input.setError("El telefono introducido no es válido");
                        esValido = false;
                    }

                } catch (NumberFormatException nfe) {
                    input.setError("Debe introducir un valor numérico entero");
                    esValido = false;
                }
                break;
            case "CIF":
                if (Util.validarCIF(nuevoValor.toUpperCase())) {
                    clienteSelect.setCif(nuevoValor.toUpperCase());
                } else {
                    input.setError("El CIF introducido no cumple las reglas de formato.");
                    esValido = false;
                }
                break;
            case "Razón Social":
                clienteSelect.setRazonSocial(nuevoValor);
                break;
            case "Email":
                if (Util.validateEmail(nuevoValor.toLowerCase())) {
                    clienteSelect.setEmail(nuevoValor.toLowerCase());
                } else {
                    input.setError("El email introducido no tiene un formato válido");
                    esValido = false;
                }
                break;
            default:
                break;
        }

        if (esValido) {
            RuntimeExceptionDao<Cliente, Integer> daoCli = helper.getClienteRuntimeDAO();
            daoCli.update(clienteSelect);
            adaptadorClientes.notifyDataSetChanged();
            cargarDetalleCliente(clienteSelect);
        }
        return esValido;

    }

    private boolean updateRepresentado(EditText input) throws SQLException {

        String campo = input.getHint().toString();
        String nuevoValor = input.getText().toString();
        boolean esValido = true;

        switch (campo) {
            case "Nombre comercial":
                if (!nuevoValor.equals("")) {
                    Dao<Representado, Integer> representadosDao = helper.getRepresentadoDAO();
                    List<Representado> lista = representadosDao.queryForAll();
                    boolean nombreExiste = false;
                    for (Representado c : lista) {
                        if (c.getNombreComercial().equals(nuevoValor.toUpperCase())) {
                            nombreExiste = true;
                        }
                    }
                    if (!nombreExiste) {
                        representadoSelect.setNombreComercial(nuevoValor.toUpperCase());
                    } else {
                        input.setError("Ya existe un representado con ese nombre");
                        esValido = false;
                    }
                } else {
                    input.setError("Debe introducir un valor");
                    esValido = false;
                }
                break;
            case "Dirección web":
                UrlValidator urlValidator = new UrlValidator();
                if (urlValidator.isValid("http://" + nuevoValor)) {
                    representadoSelect.setWeb(("http://" + nuevoValor).toLowerCase());
                } else {
                    input.setError("La direccion web introducida no es válida.");
                    esValido = false;
                }
                break;
            case "Código Postal":
                try {
                    if (Util.validarCP(Integer.parseInt(nuevoValor))) {
                        representadoSelect.setCodigoPostal(Integer.parseInt(nuevoValor));

                    } else {
                        input.setError("El CP introducido no existe");
                        esValido = false;
                    }

                } catch (NumberFormatException nfe) {
                    input.setError("Debe introducir un valor numérico");
                    esValido = false;
                }
                break;
            case "Dirección":
                representadoSelect.setDireccion(nuevoValor);
                break;
            case "Facturación":
                try {
                    representadoSelect.setFacturacion(Integer.parseInt(nuevoValor));
                } catch (NumberFormatException nfe) {
                    input.setError("Debe introducir un valor numérico entero.");
                    esValido = false;
                }
                break;
            case "Número de empleados":
                try {
                    representadoSelect.setNumEmpleados(Integer.parseInt(nuevoValor));
                } catch (NumberFormatException nfe) {
                    input.setError("Debe introducir un valor numérico entero.");
                    esValido = false;
                }
                break;
            case "Coordenadas":
                representadoSelect.setCoordenadas(nuevoValor);
                break;
            case "Teléfono":
                try {
                    if (Util.validarTelefono(Integer.parseInt(nuevoValor))) {
                        representadoSelect.setTelefono(Integer.parseInt(nuevoValor));
                    } else {
                        input.setError("El telefono introducido no es válido");
                        esValido = false;
                    }

                } catch (NumberFormatException nfe) {
                    input.setError("Debe introducir un valor numérico entero");
                    esValido = false;
                }
                break;
            case "CIF":
                if (Util.validarCIF(nuevoValor.toUpperCase())) {
                    representadoSelect.setCif(nuevoValor.toUpperCase());
                } else {
                    input.setError("El CIF introducido no cumple las reglas de formato.");
                    esValido = false;
                }
                break;
            case "Razón Social":
                representadoSelect.setRazonSocial(nuevoValor);
                break;
            case "Email":
                if (Util.validateEmail(nuevoValor.toLowerCase())) {
                    representadoSelect.setEmail(nuevoValor.toLowerCase());
                } else {
                    input.setError("El email introducido no tiene un formato válido");
                    esValido = false;
                }
                break;
            default:
                break;
        }

        if (esValido) {
            RuntimeExceptionDao<Representado, Integer> daoRep = helper.getRepresentadoRuntimeDAO();
            daoRep.update(representadoSelect);
            adaptadorRepresentados.notifyDataSetChanged();
            cargarDetalleRepresentado(representadoSelect);
        }

        return esValido;


    }

    public Cliente getClienteSelect() {
        return clienteSelect;
    }

    public void setClienteSelect(Cliente clienteSelect) {
        this.clienteSelect = clienteSelect;
    }

    public Representado getRepresentadoSelect() {
        return representadoSelect;
    }

    public void setRepresentadoSelect(Representado representadoSelect) {
        this.representadoSelect = representadoSelect;
    }

    public ConstraintLayout getFramEmpresas() {
        return framEmpresas;
    }

    public void setFramEmpresas(ConstraintLayout framEmpresas) {
        this.framEmpresas = framEmpresas;
    }
}
