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
import com.cta.android.appcomercial.adaptadores.AdaptadorSectores;
import com.cta.android.appcomercial.model.Producto;
import com.cta.android.appcomercial.model.Representado;
import com.cta.android.appcomercial.model.Sector;
import com.cta.android.appcomercial.util.DataBaseHelper;
import com.cta.android.appcomercial.util.Util;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by PC on 19/01/2018.
 */

public class ProductoFragment extends Fragment implements View.OnClickListener {

    private ImageButton btCancelarProducto;
    private ImageButton btGuardarProducto;
    private EditText etNombreProducto;
    private EditText etReferenciaProducto;
    private EditText etPrecioProducto;
    private ImageButton ibFotoProducto;
    private Spinner spSectorProducto;
    private ImageButton ibInfoProducto;

    private DataBaseHelper helper;

    private Dialog dialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_producto, container, false);
        findViews(view);

        helper = OpenHelperManager.getHelper(getContext(), DataBaseHelper.class);
        List<Sector> sectores = null;
        try {
            Dao<Sector, Integer> daoCargo = helper.getSectorDAO();
            sectores = daoCargo.queryBuilder().orderBy("nombre", true).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Sector sector = new Sector();
        sector.setNombre("Seleccione Sector ...");
        sectores.add(0, sector);

        final AdaptadorSectores adaptadorSectores = new AdaptadorSectores(getContext(), R.layout.item_lista, sectores);
        spSectorProducto.setAdapter(adaptadorSectores);

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
        btCancelarProducto = (ImageButton) v.findViewById(R.id.btCancelarProducto);
        btGuardarProducto = (ImageButton) v.findViewById(R.id.btGuardarProducto);
        etNombreProducto = (EditText) v.findViewById(R.id.etNombreProducto);
        etReferenciaProducto = (EditText) v.findViewById(R.id.etReferenciaProducto);
        etPrecioProducto = (EditText) v.findViewById(R.id.etPrecioProducto);
        etPrecioProducto.setInputType(InputType.TYPE_CLASS_PHONE);
        ibFotoProducto = (ImageButton) v.findViewById(R.id.ibFotoProducto);
        ibInfoProducto = (ImageButton) v.findViewById(R.id.ibInfoProducto);
        spSectorProducto = (Spinner) v.findViewById(R.id.spSectorProducto);
        btCancelarProducto.setOnClickListener(this);
        btGuardarProducto.setOnClickListener(this);
        ibFotoProducto.setOnClickListener(this);
        ibInfoProducto.setOnClickListener(this);
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2017-12-03 11:09:35 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if (v == btCancelarProducto) {
            // Handle clicks for btCancelarContacto
            cerrarFragmento();

        } else if (v == btGuardarProducto) {
            // Handle clicks for btGuardarContacto
            if (validarFormulario()) {

                Representado empresa = getArguments().getParcelable("empresa");
                Producto producto = new Producto();
                producto.setDescripcion(etNombreProducto.getText().toString().toUpperCase());
                producto.setReferencia(etReferenciaProducto.getText().toString().toUpperCase());
                if (!etPrecioProducto.getText().toString().equals("")) {
                    producto.setPrecio(Float.parseFloat(etPrecioProducto.getText().toString()));
                }
                Sector sector = (Sector) spSectorProducto.getSelectedItem();
                if (!sector.getNombre().equals("Seleccione Sector ...")) {
                    producto.setSector((Sector) spSectorProducto.getSelectedItem());
                }
                if (dialog != null) {
                    producto.setInfo(((EditText) dialog.findViewById(R.id.etInfoContacto)).getText().toString());
                }
                producto.setRepresentado((Representado) getArguments().getParcelable("empresa"));

                Bitmap bitmp = ((BitmapDrawable) ibFotoProducto.getDrawable()).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                producto.setImageBytes(byteArray);

                try {
                    Dao<Producto, Integer> productoDao = helper.getProductoDao();
                    productoDao.create(producto);
                    cerrarFragmento();
                } catch (SQLException e) {
                    e.printStackTrace();
                    Util.tostar("No se ha podido guardar el producto en la Base de Datos", getContext());
                }
            }

        } else if (v == ibFotoProducto) {
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
        } else if (v == ibInfoProducto) {
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
        if (etNombreProducto.getText().toString().equals("")) {
            etNombreProducto.setError("Debe introducir un nombre para el producto");
            esValido = false;
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
                    ibFotoProducto.setImageBitmap(rotateBitmap);
                }
                break;


            default:
                super.onActivityResult(requestCode, resultCode, data);

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ListaProductosFragment listaProductosFragment = (ListaProductosFragment) this.getParentFragment();
        listaProductosFragment.cargarLista();
    }
}
