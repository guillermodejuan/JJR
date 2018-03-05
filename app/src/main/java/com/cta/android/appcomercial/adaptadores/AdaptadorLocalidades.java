package com.cta.android.appcomercial.adaptadores;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.cta.android.appcomercial.R;
import com.cta.android.appcomercial.model.Localidad;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC on 27/11/2017.
 */

public class AdaptadorLocalidades extends ArrayAdapter<Localidad> {

    private final List<Localidad> localidades_All;
    private final List<Localidad> localidades_Suggestion;
    private Context context;
    private int layout;
    private List<Localidad> localidades;


    public AdaptadorLocalidades(Context context, int layout, List<Localidad> localidades) {
        super(context, layout);
        this.context = context;
        this.layout = layout;
        this.localidades = localidades;
        this.localidades_All = new ArrayList<>(localidades);
        this.localidades_Suggestion = new ArrayList<>();
    }


    @Override
    public int getCount() {
        return localidades.size();
    }

    @Override
    public Localidad getItem(int position) {
        return this.localidades.get(position);
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
            Localidad localidad = getItem(position);
            TextView name = (TextView) convertView.findViewById(R.id.tvNombreItem);
            name.setText(localidad.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;

        /*
        ViewHolder holder;
        //hay que saber si es la primera carga de la fila o no
        if (convertView == null) { //si se carga por primera vez
            //cargar layout (similar a setContentView)

            //necesito Layoutinflater
            LayoutInflater inflater = LayoutInflater.from(this.context);
            convertView = inflater.inflate(this.layout, null); // null porque no tiene parent

            //creacion holder
            holder = new ViewHolder();
            holder.tvNombreItem = (TextView) convertView.findViewById(R.id.tvNombreItem);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //recuperar valor marca
        String nombre = this.localidades.get(position).getNombre();
        holder.tvNombreItem = (TextView) convertView.findViewById(R.id.tvNombreItem);
        holder.tvNombreItem.setText(nombre);
        return convertView;
        */
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            public String convertResultToString(Object resultValue) {
                return ((Localidad) resultValue).getNombre();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if (constraint != null) {
                    localidades_Suggestion.clear();
                    for (Localidad item : localidades_All) {
                        if (item.toString().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            localidades_Suggestion.add(item);
                        }
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = localidades_Suggestion;
                    filterResults.count = localidades_Suggestion.size();
                    return filterResults;
                } else {
                    return new FilterResults();
                }
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                localidades.clear();
                if (results != null && results.count > 0) {
                    // avoids unchecked cast warning when using mDepartments.addAll((ArrayList<Department>) results.values);
                    List<?> result = (List<?>) results.values;
                    for (Object object : result) {
                        if (object instanceof Localidad) {
                            localidades.add((Localidad) object);
                        }
                    }
                } else if (constraint == null) {
                    // no filter, add entire original list back in
                    localidades.addAll(localidades_All);
                }
                notifyDataSetChanged();
            }
        };
    }

    static class ViewHolder { //clase anidada y estática
        private TextView tvNombreItem;

    }
}
