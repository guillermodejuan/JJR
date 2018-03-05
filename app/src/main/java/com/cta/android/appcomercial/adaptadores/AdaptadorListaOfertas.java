package com.cta.android.appcomercial.adaptadores;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cta.android.appcomercial.R;
import com.cta.android.appcomercial.model.Cliente;
import com.cta.android.appcomercial.model.LineaOferta;
import com.cta.android.appcomercial.model.Oferta;
import com.cta.android.appcomercial.model.Producto;
import com.cta.android.appcomercial.model.Representado;
import com.cta.android.appcomercial.util.DataBaseHelper;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC on 21/01/2018.
 */

public class AdaptadorListaOfertas extends BaseAdapter {

    ArrayList<Oferta> listaOfertas;
    Context context;
    Activity activity;
    int layout;

    public AdaptadorListaOfertas(ArrayList<Oferta> listaOfertas, Context context, Activity activity, int layout) {
        this.listaOfertas = listaOfertas;
        this.context = context;
        this.activity = activity;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return listaOfertas.size();
    }

    @Override
    public Oferta getItem(int position) {
        return listaOfertas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(layout, parent, false);
        }

        TextView empresas = (TextView) convertView.findViewById(R.id.tvEmpresasOferta);
        TextView fecha = (TextView) convertView.findViewById(R.id.tvFechaOferta);
        TextView expiracion = (TextView) convertView.findViewById(R.id.tvFechaExpiracion);
        TextView cantidad = (TextView) convertView.findViewById(R.id.tvCantidadAnual);
        TextView volumen = (TextView) convertView.findViewById(R.id.tvProductoItemOferta);
        TextView referencia = (TextView) convertView.findViewById(R.id.tvReferenciaOferta);
        Oferta oferta = listaOfertas.get(position);
        try {
            RuntimeExceptionDao<Cliente, Integer> runtimeExceptionDao = OpenHelperManager.getHelper(context, DataBaseHelper.class).getClienteRuntimeDAO();
            Cliente cliente = runtimeExceptionDao.queryForId(oferta.getCliente().getId());
            RuntimeExceptionDao<Representado, Integer> runtimeExceptionDao1 = OpenHelperManager.getHelper(context, DataBaseHelper.class).getRepresentadoRuntimeDAO();
            Representado representado = runtimeExceptionDao1.queryForId(oferta.getRepresentado().getId());
            referencia.setText(representado.getNombreComercial() + " oferta a " + cliente.getNombreComercial());

            RuntimeExceptionDao<LineaOferta, Integer> lineaOfertaRuntimeExceptionDao = OpenHelperManager.getHelper(context, DataBaseHelper.class).getLineaOfertaIntegerRuntimeExceptionDao();
            List<LineaOferta> lineaOfertaList = lineaOfertaRuntimeExceptionDao.queryForEq("oferta_id",oferta.getId());
            int piezas = 0;
            float totalfacturacion = 0;
            for (LineaOferta lineaOferta : lineaOfertaList) {
                piezas = piezas + lineaOferta.getCantidad();
                RuntimeExceptionDao<Producto, Integer> productoIntegerRuntimeExceptionDao = OpenHelperManager.getHelper(context, DataBaseHelper.class).getProductoRuntimeExceptionDao();
                Producto productolinea = productoIntegerRuntimeExceptionDao.queryForId(lineaOferta.getProducto().getId());
                totalfacturacion = totalfacturacion + (lineaOferta.getCantidad()*productolinea.getPrecio());
                cantidad.setText("Cantidad Anual: " + piezas);
                volumen.setText("Volumen Facturación: " + totalfacturacion);
                if (oferta.getReferencia() != null) {
                    empresas.setText("ref: " + oferta.getReferencia());
                } else {
                    empresas.setText("");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM7yyyy");
        fecha.setText("Ofertado: " + simpleDateFormat.format(oferta.getFecha()));
        if (oferta.getFechaLimite() != 0) {
            expiracion.setText("Expira: " + simpleDateFormat.format(oferta.getFechaLimite()));
        }


        return convertView;
    }
}
