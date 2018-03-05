package com.cta.android.appcomercial.adaptadores;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cta.android.appcomercial.R;
import com.cta.android.appcomercial.model.Cargo;
import com.cta.android.appcomercial.model.ContactoCliente;
import com.cta.android.appcomercial.model.LineaOferta;
import com.cta.android.appcomercial.model.Producto;
import com.cta.android.appcomercial.util.DataBaseHelper;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.util.List;

/**
 * Created by PC on 21/01/2018.
 */

public class AdaptadorLineaOferta extends BaseAdapter {
        private Context context;
        private int layout;
        private List<LineaOferta> contactos;

        public AdaptadorLineaOferta(Context context, int layout, List<LineaOferta> contactos) {
            this.context = context;
            this.layout = layout;
            this.contactos = contactos;
        }

        @Override
        public int getCount() {
            return contactos.size();
        }

        @Override
        public LineaOferta getItem(int position) {
            return this.contactos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return this.contactos.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            try {
                if (convertView == null) {
                    LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                    convertView = inflater.inflate(layout, parent, false);
                }
                LineaOferta lineaOferta = getItem(position);
                TextView nametv = (TextView) convertView.findViewById(R.id.tvNombreContactoSpinner);
                RuntimeExceptionDao<Producto, Integer> runtimeDAO = OpenHelperManager.getHelper(context, DataBaseHelper.class).getProductoRuntimeExceptionDao();
                Producto producto = runtimeDAO.queryForId(lineaOferta.getProducto().getId());
                nametv.setText(producto.getDescripcion());
                TextView cantidad = (TextView) convertView.findViewById(R.id.tvCargoContactoSpinner);
                cantidad.setText("" + lineaOferta.getCantidad());


            } catch (Exception e) {
                e.printStackTrace();
            }
            return convertView;


        }
}
