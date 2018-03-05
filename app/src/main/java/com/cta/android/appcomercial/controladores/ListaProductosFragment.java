package com.cta.android.appcomercial.controladores;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cta.android.appcomercial.R;
import com.cta.android.appcomercial.adaptadores.AdaptadorListaContactosRepresentado;
import com.cta.android.appcomercial.adaptadores.AdaptadorListaProductos;
import com.cta.android.appcomercial.model.ContactoCliente;
import com.cta.android.appcomercial.model.ContactoRepresentado;
import com.cta.android.appcomercial.model.Producto;
import com.cta.android.appcomercial.model.Representado;
import com.cta.android.appcomercial.util.DataBaseHelper;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by PC on 19/01/2018.
 */

public class ListaProductosFragment extends Fragment {

    ImageButton btAnadirProducto;
    ImageButton btCancelarListaProductos;
    RecyclerView rvListaProductos;
    DataBaseHelper helper;
    View view;
    List<Producto> producto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_lista_productos, container, false);
        helper = OpenHelperManager.getHelper(getContext(), DataBaseHelper.class);
        btAnadirProducto = (ImageButton) view.findViewById(R.id.ibAnadirProducto);
        btAnadirProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductoFragment productoFragment = new ProductoFragment();
                Bundle bundle = new Bundle(1);
                bundle.putParcelable("empresa", getArguments().getParcelable("empresa"));
                productoFragment.setArguments(bundle);
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.container_lista_productos, productoFragment, "listaProductos");
                transaction.commit();
            }
        });

        btCancelarListaProductos = (ImageButton) view.findViewById(R.id.ibSalirListaProductos);
        btCancelarListaProductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarFragmento();
            }
        });

        rvListaProductos = (RecyclerView) view.findViewById(R.id.rvListaProductos);
        rvListaProductos.setLayoutManager(new LinearLayoutManager(getContext()));

        cargarLista();

        return view;

    }

    private void cerrarFragmento() {
        getFragmentManager().beginTransaction().remove(this).commit();
        EmpresasFragment empresasFragment = (EmpresasFragment) this.getParentFragment();
        for (int x = 0; x < empresasFragment.getFramEmpresas().getChildCount(); x++) {
            empresasFragment.getFramEmpresas().getChildAt(x).setEnabled(true);
        }

    }

    public void cargarLista() {

        String nombre_empresa = "";
        String tipo_empresa = "";

        Representado empresa = getArguments().getParcelable("empresa");
        tipo_empresa = "Representado";
        nombre_empresa = empresa.getNombreComercial();
        RuntimeExceptionDao<Producto, Integer> runtimeExceptionDao = helper.getProductoRuntimeExceptionDao();
        producto = runtimeExceptionDao.queryForEq("representado_id", empresa.getId());
        if (producto.size() > 0) {
            AdaptadorListaProductos adaptadorListaProductos = new AdaptadorListaProductos(producto, this);
            rvListaProductos.setAdapter(adaptadorListaProductos);
        } else {
            rvListaProductos.setAdapter(null);
        }


        TextView cabecera = (TextView) view.findViewById(R.id.tvCabeceraListaProductos);
        cabecera.setText(tipo_empresa + ": " + nombre_empresa);

    }

    private void update(Producto producto) {
        try {
            Dao<Producto, Integer> productosDao = OpenHelperManager.getHelper(getContext(), DataBaseHelper.class).getProductoDao();
            productosDao.update(producto);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap rotateBitmap = null;


        if (resultCode == Activity.RESULT_OK) {

            int rotate = 90;
            Uri image = data.getData();
            String result = data.toUri(0);
            Bundle extras = data.getExtras();
            Bitmap imageBmp = (Bitmap) extras.get("data");
            Matrix matrix = new Matrix();
            matrix.postRotate(rotate);
            rotateBitmap = Bitmap.createBitmap(imageBmp, 0, 0, imageBmp.getWidth(), imageBmp.getHeight(), matrix, true);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            rotateBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            if (producto != null) {
                producto.get(requestCode).setImageBytes(byteArray);
                update(producto.get(requestCode));
            }
            cargarLista();
        }

    }
}
