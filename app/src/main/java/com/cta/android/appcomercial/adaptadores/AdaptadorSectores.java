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
import com.cta.android.appcomercial.model.Sector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC on 28/11/2017.
 */

public class AdaptadorSectores extends ArrayAdapter<Sector> {
    private final List<Sector> sectors_All;
    private final List<Sector> sectors_Suggestion;
    private Context context;
    private int layout;
    private List<Sector> sectors;


    public AdaptadorSectores(Context context, int layout, List<Sector> sectors) {
        super(context, layout);
        this.context = context;
        this.layout = layout;
        this.sectors = sectors;
        this.sectors_All = new ArrayList<>(sectors);
        this.sectors_Suggestion = new ArrayList<>();
    }


    @Override
    public int getCount() {
        return sectors.size();
    }

    @Override
    public Sector getItem(int position) {
        return this.sectors.get(position);
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
            Sector sector = getItem(position);
            TextView name = (TextView) convertView.findViewById(R.id.tvNombreItem);
            name.setText(sector.toString());
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
        String nombre = this.sectors.get(position).getNombre();
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
                return ((Sector) resultValue).getNombre();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if (constraint != null) {
                    sectors_Suggestion.clear();
                    for (Sector item : sectors_All) {
                        if (item.toString().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            sectors_Suggestion.add(item);
                        }
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = sectors_Suggestion;
                    filterResults.count = sectors_Suggestion.size();
                    return filterResults;
                } else {
                    return new FilterResults();
                }
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                sectors.clear();
                if (results != null && results.count > 0) {
                    // avoids unchecked cast warning when using mDepartments.addAll((ArrayList<Department>) results.values);
                    List<?> result = (List<?>) results.values;
                    for (Object object : result) {
                        if (object instanceof Sector) {
                            sectors.add((Sector) object);
                        }
                    }
                } else if (constraint == null) {
                    // no filter, add entire original list back in
                    sectors.addAll(sectors_All);
                }
                notifyDataSetChanged();
            }
        };
    }

    static class ViewHolder { //clase anidada y estática
        private TextView tvNombreItem;
    }
}