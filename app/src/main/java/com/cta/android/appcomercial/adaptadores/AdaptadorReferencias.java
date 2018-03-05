package com.cta.android.appcomercial.adaptadores;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cta.android.appcomercial.R;
import com.cta.android.appcomercial.model.Oferta;

import java.util.List;

/**
 * Created by PC on 21/01/2018.
 */

public class AdaptadorReferencias extends ArrayAdapter<Oferta> {

    private Context context;
    private int layout;
    private List<Oferta> ofertas;

    public AdaptadorReferencias(Context context, int layout, List<Oferta> ofertas) {
        super(context, layout);
        this.context = context;
        this.layout = layout;
        this.ofertas = ofertas;
    }

    @Override
    public int getCount() {
        return ofertas.size();
    }

    @Override
    public Oferta getItem(int position) {
        return this.ofertas.get(position);
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
        Oferta oferta = getItem(position);
        TextView nametv = (TextView) convertView.findViewById(R.id.tvNombreItem);
        nametv.setText(oferta.getReferencia());


        return convertView;


    }
}
