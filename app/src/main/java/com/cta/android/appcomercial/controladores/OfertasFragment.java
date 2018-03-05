package com.cta.android.appcomercial.controladores;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.cta.android.appcomercial.R;
import com.cta.android.appcomercial.adaptadores.AdaptadorClientes;
import com.cta.android.appcomercial.adaptadores.AdaptadorListaOfertas;
import com.cta.android.appcomercial.adaptadores.AdaptadorReferencias;
import com.cta.android.appcomercial.adaptadores.AdaptadorRepresentados;
import com.cta.android.appcomercial.adaptadores.AdaptadorSpinnerProductos;
import com.cta.android.appcomercial.model.Cliente;
import com.cta.android.appcomercial.model.Oferta;
import com.cta.android.appcomercial.model.Producto;
import com.cta.android.appcomercial.model.Representado;
import com.cta.android.appcomercial.util.DataBaseHelper;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC on 30/10/2017.
 */

public class OfertasFragment extends Fragment implements View.OnClickListener{

    private AutoCompleteTextView acProducto;
    private AutoCompleteTextView acReferencia;
    private AutoCompleteTextView acRepresentado;
    private AutoCompleteTextView acCliente;
    private TextView ofertasPedidostv;
    private ImageButton ibBuscarOferta;
    private ImageButton ibAnadirOferta;
    private ListView lvOfertas;

    private DataBaseHelper helper;
    private AdaptadorClientes adaptadorClientes;
    private AdaptadorRepresentados adaptadorRepresentados;

    private Cliente clienteSelect;
    private Representado representadoSelect;
    

    
    
    @Override
    public void onClick(View v) {
        if ( v == ibBuscarOferta ) {
            // Handle clicks for ibBuscarOferta
            RuntimeExceptionDao<Oferta, Integer> runtimeExceptionDao = helper.getOfertaRuntimeExceptionDao();
            List<Oferta> ofertas = runtimeExceptionDao.queryForAll();
            ArrayList<Oferta> ofertas1 = new ArrayList<Oferta>();
            for (Oferta oferta : ofertas) {
                ofertas1.add(oferta);
            }
            AdaptadorListaOfertas adaptadorListaOfertas = new AdaptadorListaOfertas(ofertas1,getContext(),getActivity(),R.layout.item_lista_oferta);
            lvOfertas.setAdapter(adaptadorListaOfertas);

        } else if ( v == ibAnadirOferta ) {
            // Handle clicks for ibAnadirOferta
            AnadirOfertaFragment anadirOfertaFragment = new AnadirOfertaFragment();
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.framOfertas, anadirOfertaFragment , "anadirOferta");
            transaction.commit();

        }
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ofertas, container, false);
        helper = OpenHelperManager.getHelper(getContext(), DataBaseHelper.class);
        acProducto = (AutoCompleteTextView)view.findViewById( R.id.acProducto );
        acReferencia = (AutoCompleteTextView)view.findViewById( R.id.acReferencia );
        acRepresentado = (AutoCompleteTextView)view.findViewById( R.id.acRepresentado );
        acCliente = (AutoCompleteTextView)view.findViewById( R.id.acCliente );
        ofertasPedidostv = (TextView)view.findViewById( R.id.ofertas_pedidostv );
        ibBuscarOferta = (ImageButton)view.findViewById( R.id.ibBuscarOferta );
        ibAnadirOferta = (ImageButton)view.findViewById( R.id.ibAnadirOferta );
        lvOfertas = (ListView)view.findViewById( R.id.lvOfertas );
        cargarAdaptadores();
        try {
            cargarAdaptadorReferencias(null, null);
        } catch (SQLException sqle) {

        }
        cargarAdaptadorProductos(null);

        acCliente.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clienteSelect = (Cliente) acCliente.getAdapter().getItem(position);
                try {
                    cargarAdaptadorReferencias(clienteSelect,representadoSelect);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        acRepresentado.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                representadoSelect = (Representado) acRepresentado.getAdapter().getItem(position);
                cargarAdaptadorProductos(representadoSelect);
                try {
                    cargarAdaptadorReferencias(clienteSelect,representadoSelect);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });



        ibBuscarOferta.setOnClickListener( this );
        ibAnadirOferta.setOnClickListener( this );
        return view;

    }

    private void cargarAdaptadorProductos(Representado representadoSelect) {
        RuntimeExceptionDao<Producto, Integer> productoIntegerRuntimeExceptionDao = helper.getProductoRuntimeExceptionDao();
        List productos = null;
        if (!acRepresentado.getText().toString().equals("")) {
            productos = productoIntegerRuntimeExceptionDao.queryForEq("representado_id", representadoSelect.getId());
        } else {
            productos = productoIntegerRuntimeExceptionDao.queryForAll();
        }
        AdaptadorSpinnerProductos adaptadorSpinnerProductos = new AdaptadorSpinnerProductos(getContext(),R.layout.item_lista,productos);
        acProducto.setAdapter(adaptadorSpinnerProductos);
    }

    private void cargarAdaptadorReferencias(Cliente cliente, Representado representado) throws SQLException {
        RuntimeExceptionDao<Oferta,Integer> runtimeExceptionDao = helper.getOfertaRuntimeExceptionDao();
        List<Oferta> ofertas = null;
        if (cliente != null && representado != null) {
            ofertas = runtimeExceptionDao.queryBuilder().where().eq("representado_id",representado.getId()).and().eq("cliente_id",cliente.getId()).query();
        } else if (cliente == null && representado != null) {
            ofertas = runtimeExceptionDao.queryBuilder().where().eq("representado_id",representado.getId()).query();
        } else if (cliente != null && representado == null) {
            ofertas = runtimeExceptionDao.queryBuilder().where().eq("cliente_id",cliente.getId()).query();
        } else {
            ofertas = runtimeExceptionDao.queryForAll();
        }
        AdaptadorReferencias adaptadorReferencias = new AdaptadorReferencias(getContext(),R.layout.item_lista,ofertas);
        acReferencia.setAdapter(adaptadorReferencias);
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
        acCliente.setAdapter(adaptadorClientes);

        // lista de representados
        List<Representado> representados = null;
        try {
            RuntimeExceptionDao<Representado, Integer> runtimeDAO = helper.getRepresentadoRuntimeDAO();
            representados = runtimeDAO.queryForAll();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        adaptadorRepresentados = new AdaptadorRepresentados(getContext(), R.layout.item_lista, representados);
        acRepresentado.setAdapter(adaptadorRepresentados);


    }
}
