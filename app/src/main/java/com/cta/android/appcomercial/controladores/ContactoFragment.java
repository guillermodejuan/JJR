package com.cta.android.appcomercial.controladores;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.cta.android.appcomercial.R;
import com.cta.android.appcomercial.adaptadores.AdaptadorCargos;
import com.cta.android.appcomercial.model.Cargo;
import com.cta.android.appcomercial.model.Cliente;
import com.cta.android.appcomercial.model.ContactoCliente;
import com.cta.android.appcomercial.model.ContactoRepresentado;
import com.cta.android.appcomercial.model.Representado;
import com.cta.android.appcomercial.util.DataBaseHelper;
import com.cta.android.appcomercial.util.Util;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by PC on 02/12/2017.
 */

public class ContactoFragment extends Fragment implements View.OnClickListener {

    private ImageButton btCancelarContacto;
    private ImageButton btGuardarContacto;
    private TextInputLayout tilNombreContacto;
    private EditText etNombreContacto;
    private TextInputLayout tilApellidosContacto;
    private EditText etApellidosContacto;
    private TextInputLayout tilTelefonoContacto;
    private EditText etTelefonoContacto;
    private TextInputLayout tilMovilContacto;
    private EditText etMovilContacto;
    private TextInputLayout tilEmailContacto;
    private EditText etEmailContacto;
    private ImageButton ibFotoContacto;
    private Spinner spCargoContacto;
    private ImageButton ibInfoContacto;

    private DataBaseHelper helper;

    private Dialog dialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contacto, container, false);
        findViews(view);

        helper = OpenHelperManager.getHelper(getContext(), DataBaseHelper.class);
        List<Cargo> cargos = null;
        try {
            Dao<Cargo, Integer> daoCargo = helper.getCargoDAO();
            cargos = daoCargo.queryBuilder().orderBy("nombre", true).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Cargo cargo = new Cargo();
        cargo.setNombre(getResources().getString(R.string.selectCargo));
        cargos.add(0, cargo);

        final AdaptadorCargos adaptadorCargos = new AdaptadorCargos(getContext(), R.layout.item_lista, cargos);
        spCargoContacto.setAdapter(adaptadorCargos);

        return view;
    }

    private void cerrarFragmento() {
        getFragmentManager().beginTransaction().remove(this).commit();
    }

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2017-12-03 11:09:35 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews(View v) {
        btCancelarContacto = (ImageButton) v.findViewById(R.id.btCancelarContacto);
        btGuardarContacto = (ImageButton) v.findViewById(R.id.btGuardarContacto);
        tilNombreContacto = (TextInputLayout) v.findViewById(R.id.tilNombreProducto);
        etNombreContacto = (EditText) v.findViewById(R.id.etNombreContacto);
        tilApellidosContacto = (TextInputLayout) v.findViewById(R.id.tilReferenciaProducto);
        etApellidosContacto = (EditText) v.findViewById(R.id.etApellidosContacto);
        tilTelefonoContacto = (TextInputLayout) v.findViewById(R.id.tilPrecioProducto);
        etTelefonoContacto = (EditText) v.findViewById(R.id.etTelefonoContacto);
        etTelefonoContacto.setInputType(InputType.TYPE_CLASS_NUMBER);
        tilMovilContacto = (TextInputLayout) v.findViewById(R.id.tilPlazoCaducidad);
        etMovilContacto = (EditText) v.findViewById(R.id.etMovilContacto);
        etMovilContacto.setInputType(InputType.TYPE_CLASS_NUMBER);
        tilEmailContacto = (TextInputLayout) v.findViewById(R.id.tilEmailContacto);
        etEmailContacto = (EditText) v.findViewById(R.id.etEmailContacto);
        etEmailContacto.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        ibFotoContacto = (ImageButton) v.findViewById(R.id.ibFotoProducto);
        ibInfoContacto = (ImageButton) v.findViewById(R.id.ibInfoContacto);
        spCargoContacto = (Spinner) v.findViewById(R.id.spSectorProducto);

        btCancelarContacto.setOnClickListener(this);
        btGuardarContacto.setOnClickListener(this);
        ibFotoContacto.setOnClickListener(this);
        ibInfoContacto.setOnClickListener(this);
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2017-12-03 11:09:35 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if (v == btCancelarContacto) {
            // Handle clicks for btCancelarContacto
            cerrarFragmento();

        } else if (v == btGuardarContacto) {
            // Handle clicks for btGuardarContacto
            if (validarFormulario()) {
                try {
                    Cliente empresa = getArguments().getParcelable("empresa");
                    ContactoCliente cliente = new ContactoCliente();
                    cliente.setNombre(etNombreContacto.getText().toString().toUpperCase());
                    cliente.setApellidos(etApellidosContacto.getText().toString().toUpperCase());
                    if (!etTelefonoContacto.getText().toString().equals("")) {
                        cliente.setTelefono(Integer.parseInt(etTelefonoContacto.getText().toString()));
                    }
                    if (!etMovilContacto.getText().toString().equals("")) {
                        cliente.setMovil(Integer.parseInt(etMovilContacto.getText().toString()));
                    }
                    Cargo cargo = (Cargo) spCargoContacto.getSelectedItem();
                    if (!cargo.getNombre().equals(getResources().getString(R.string.selectCargo))) {
                        cliente.setCargo((Cargo) spCargoContacto.getSelectedItem());
                    }
                    if (dialog != null) {
                        cliente.setOtros(((EditText) dialog.findViewById(R.id.etInfoContacto)).getText().toString());
                    }
                    cliente.setEmail(etEmailContacto.getText().toString().toLowerCase());
                    cliente.setCliente((Cliente) getArguments().getParcelable("empresa"));

                    Bitmap bitmp = ((BitmapDrawable) ibFotoContacto.getDrawable()).getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    cliente.setFoto(byteArray);

                    try {
                        Dao<ContactoCliente, Integer> contactoClienteDao = helper.getContactoClienteDAO();
                        contactoClienteDao.create(cliente);

                        cerrarFragmento();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        Util.tostar("No se ha podido guardar el contacto en la Base de Datos", getContext());
                    }


                } catch (ClassCastException cce) {
                    Representado empresa = getArguments().getParcelable("empresa");
                    ContactoRepresentado representado = new ContactoRepresentado();
                    representado.setNombre(etNombreContacto.getText().toString().toUpperCase());
                    representado.setApellidos(etApellidosContacto.getText().toString().toUpperCase());
                    if (!etTelefonoContacto.getText().toString().equals("")) {
                        representado.setTelefono(Integer.parseInt(etTelefonoContacto.getText().toString()));
                    }
                    if (!etMovilContacto.getText().toString().equals("")) {
                        representado.setMovil(Integer.parseInt(etMovilContacto.getText().toString()));
                    }
                    Cargo cargo = (Cargo) spCargoContacto.getSelectedItem();
                    if (!cargo.getNombre().equals(getResources().getString(R.string.selectCargo))) {
                        representado.setCargo((Cargo) spCargoContacto.getSelectedItem());
                    }
                    if (dialog != null) {
                        representado.setOtros(((EditText) dialog.findViewById(R.id.etInfoContacto)).getText().toString());
                    }
                    representado.setEmail(etEmailContacto.getText().toString().toLowerCase());
                    representado.setRepresentado((Representado) getArguments().getParcelable("empresa"));

                    Bitmap bitmp = ((BitmapDrawable) ibFotoContacto.getDrawable()).getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    representado.setFoto(byteArray);

                    try {
                        Dao<ContactoRepresentado, Integer> contactoRepresentadoDao = helper.getContactoRepresentadoDAO();
                        contactoRepresentadoDao.create(representado);
                        cerrarFragmento();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        Util.tostar("No se ha podido guardar el contacto en la Base de Datos", getContext());
                    }
                }
            }
        } else if (v == ibFotoContacto) {
            // Handle clicks for ibFotoContacto
            //Util.cerrarTeclado(getContext(), getActivity());

            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 0);
            } else {
                Intent intent = new Intent();
                if (intent != null) {
                    intent.setAction("android.media.action.IMAGE_CAPTURE");
                    try {
                        startActivityForResult(intent, 10);
                    } catch (Exception e) {

                    }
                }
            }
        } else if (v == ibInfoContacto) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.otrosdatos, null);
            builder.setView(view);
            dialog = builder.create();
            Button btGuardar = (Button) view.findViewById(R.id.btGuardarInfoContacto);
            btGuardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();

                }
            });
            dialog.show();
        }
    }

    private boolean validarFormulario() {
        boolean esValido = true;
        if (etNombreContacto.getText().toString().equals("")) {
            etNombreContacto.setError("Debe introducir un nombre para el contacto");
            esValido = false;
        }

        if (!etEmailContacto.getText().toString().equals("")) {
            if (!Util.validateEmail(etEmailContacto.getText().toString())) {
                etEmailContacto.setError("El email introducido no tiene un formato válido");
                esValido = false;
            }
        }

        if (!etMovilContacto.getText().toString().equals("")) {
            try {
                int movil = Integer.parseInt(etMovilContacto.getText().toString());
                if (movil < 600000000 || movil > 799999999) {
                    etMovilContacto.setError("El número no se corresponde con un teléfono móvil");
                    esValido = false;
                }
            } catch (NumberFormatException nfe) {
                etMovilContacto.setError("El número no se corresponde con un teléfono móvil");
                esValido = false;
            }
        }


        if (!etTelefonoContacto.getText().toString().equals("")) {
            try {
                int telefono = Integer.parseInt(etTelefonoContacto.getText().toString());
                if (telefono < 800000000 || telefono > 999999999) {
                    etTelefonoContacto.setError("El número no se corresponde con un teléfono fijo");
                    esValido = false;
                }
            } catch (NumberFormatException nfe) {
                etTelefonoContacto.setError("El número no se corresponde con un teléfono fijo");
                esValido = false;
            }
        }
        return esValido;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case 10:

                if (resultCode == Activity.RESULT_OK) {
                    int rotate = 0;
                    Uri image = data.getData();
                    rotate = 90;
                    String result = data.toUri(0);
                    Bundle extras = data.getExtras();
                    Bitmap imageBmp = (Bitmap) extras.get("data");
                    Matrix matrix = new Matrix();
                    matrix.postRotate(rotate);
                    Bitmap rotateBitmap = Bitmap.createBitmap(imageBmp, 0, 0, imageBmp.getWidth(), imageBmp.getHeight(), matrix, true);
                    ibFotoContacto.setImageBitmap(rotateBitmap);
                }
                break;


            default:
                super.onActivityResult(requestCode, resultCode, data);

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ListaContactosFragment listaContactosFragment = (ListaContactosFragment) this.getParentFragment();
        listaContactosFragment.cargarLista();
    }


}
