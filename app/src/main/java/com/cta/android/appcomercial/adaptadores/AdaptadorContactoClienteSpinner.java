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
import com.cta.android.appcomercial.util.DataBaseHelper;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.util.List;

/**
 * Created by PC on 02/01/2018.
 */

public class AdaptadorContactoClienteSpinner extends BaseAdapter {

    private Context context;
    private int layout;
    private List<ContactoCliente> contactos;

    public AdaptadorContactoClienteSpinner(Context context, int layout, List<ContactoCliente> contactos) {
        this.context = context;
        this.layout = layout;
        this.contactos = contactos;
    }

    @Override
    public int getCount() {
        return contactos.size();
    }

    @Override
    public ContactoCliente getItem(int position) {
        return this.contactos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        try {
            if (convertView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                convertView = inflater.inflate(layout, parent, false);
            }
            ContactoCliente contactoCliente = getItem(position);
            TextView nametv = (TextView) convertView.findViewById(R.id.tvNombreContactoSpinner);
            nametv.setText(contactoCliente.getNombre() + " " + contactoCliente.getApellidos());
            TextView cargotv = (TextView) convertView.findViewById(R.id.tvCargoContactoSpinner);

            RuntimeExceptionDao<Cargo, Integer> runtimeDAO = OpenHelperManager.getHelper(context, DataBaseHelper.class).getCargoRuntimeDAO();
            Cargo cargo = runtimeDAO.queryForId(contactoCliente.getCargo().getId());
            cargotv.setText(cargo.getNombre());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;


    }
}