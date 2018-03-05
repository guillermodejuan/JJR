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
import com.cta.android.appcomercial.adaptadores.AdaptadorListaContactosCliente;
import com.cta.android.appcomercial.adaptadores.AdaptadorListaContactosRepresentado;
import com.cta.android.appcomercial.model.Cliente;
import com.cta.android.appcomercial.model.ContactoCliente;
import com.cta.android.appcomercial.model.ContactoRepresentado;
import com.cta.android.appcomercial.model.Representado;
import com.cta.android.appcomercial.util.DataBaseHelper;
import com.cta.android.appcomercial.util.Util;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC on 02/12/2017.
 */

public class ListaContactosFragment extends Fragment {

    ImageButton btAnadirContacto;
    ImageButton btCancelarListaContactos;
    RecyclerView rvListaContactos;
    DataBaseHelper helper;
    View view;
    List<ContactoCliente> contactosCliente;
    List<ContactoRepresentado> contactosRepresentado;
    ImageButton btEmailListaContactos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_lista_contactos, container, false);
        helper = OpenHelperManager.getHelper(getContext(), DataBaseHelper.class);
        btAnadirContacto = (ImageButton) view.findViewById(R.id.ibAnadirContacto);
        btAnadirContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactoFragment contactoFragment = new ContactoFragment();
                Bundle bundle = new Bundle(1);
                bundle.putParcelable("empresa", getArguments().getParcelable("empresa"));
                contactoFragment.setArguments(bundle);
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.container_lista_contactos_fragment, contactoFragment, "listaContactos");
                transaction.commit();
            }
        });

        btCancelarListaContactos = (ImageButton) view.findViewById(R.id.ibSalirListaContactos);
        btCancelarListaContactos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarFragmento();
            }
        });

        rvListaContactos = (RecyclerView) view.findViewById(R.id.rvListaContactos);
        rvListaContactos.setLayoutManager(new LinearLayoutManager(getContext()));

        cargarLista();
        btEmailListaContactos = (ImageButton) view.findViewById(R.id.ibEmailListaContactos);
        btEmailListaContactos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Cliente empresa = getArguments().getParcelable("empresa");
                    if (contactosCliente.size() > 0) {
                        ArrayList<String> direcciones = new ArrayList<String>();
                        for (ContactoCliente c : contactosCliente) {
                            if (c.getEmail() != null && !c.getEmail().equals("")) {
                                direcciones.add(c.getEmail());
                            }
                        }
                        if (direcciones.size() > 0)
                            Util.sendEmail(direcciones, getContext());
                    }

                } catch (ClassCastException cce) {
                    if (contactosRepresentado.size() > 0) {
                        ArrayList<String> direcciones = new ArrayList<String>();
                        for (ContactoRepresentado c : contactosRepresentado) {
                            if (c.getEmail() != null && !c.getEmail().equals("")) {
                                direcciones.add(c.getEmail());
                            }
                        }
                        if (direcciones.size() > 0)
                            Util.sendEmail(direcciones, getContext());
                    }
                }

            }
        });
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
        try {
            Cliente empresa = getArguments().getParcelable("empresa");
            tipo_empresa = "Cliente";
            nombre_empresa = empresa.getNombreComercial();
            RuntimeExceptionDao<ContactoCliente, Integer> runtimeExceptionDao = helper.getContactoClienteRuntimeDAO();
            contactosCliente = runtimeExceptionDao.queryForEq("cliente_id", empresa.getId());
            if (contactosCliente.size() > 0) {
                AdaptadorListaContactosCliente adaptadorListaContactosCliente = new AdaptadorListaContactosCliente(contactosCliente, this);
                rvListaContactos.setAdapter(adaptadorListaContactosCliente);
            } else {
                rvListaContactos.setAdapter(null);
            }
        } catch (ClassCastException cce) {
            Representado empresa = getArguments().getParcelable("empresa");
            tipo_empresa = "Representado";
            nombre_empresa = empresa.getNombreComercial();
            RuntimeExceptionDao<ContactoRepresentado, Integer> runtimeExceptionDao = helper.getContactoRepresentadoRuntimeDAO();
            contactosRepresentado = runtimeExceptionDao.queryForEq("representado_id", empresa.getId());
            if (contactosRepresentado.size() > 0) {
                AdaptadorListaContactosRepresentado adaptadorListaContactosRepresentado = new AdaptadorListaContactosRepresentado(contactosRepresentado, this);
                rvListaContactos.setAdapter(adaptadorListaContactosRepresentado);
            } else {
                rvListaContactos.setAdapter(null);
            }
        }

        TextView cabecera = (TextView) view.findViewById(R.id.tvCabeceraListaContactos);
        cabecera.setText(tipo_empresa + ": " + nombre_empresa);

    }

    private void update(ContactoCliente contactoCliente) {
        try {
            Dao<ContactoCliente, Integer> ccDao = OpenHelperManager.getHelper(getContext(), DataBaseHelper.class).getContactoClienteDAO();
            ccDao.update(contactoCliente);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void update(ContactoRepresentado contactoRepresentado) {
        try {
            Dao<ContactoRepresentado, Integer> ccDao = OpenHelperManager.getHelper(getContext(), DataBaseHelper.class).getContactoRepresentadoDAO();
            ccDao.update(contactoRepresentado);
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

            if (contactosCliente != null) {
                contactosCliente.get(requestCode).setFoto(byteArray);
                update(contactosCliente.get(requestCode));
            } else if (contactosRepresentado != null) {
                contactosRepresentado.get(requestCode).setFoto(byteArray);
                update(contactosRepresentado.get(requestCode));
            }
            cargarLista();
        }

    }

}
