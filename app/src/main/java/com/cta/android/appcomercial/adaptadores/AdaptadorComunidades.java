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
import com.cta.android.appcomercial.model.ComunidadAutonoma;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC on 26/11/2017.
 */

public class AdaptadorComunidades extends ArrayAdapter<ComunidadAutonoma> {
    private final List<ComunidadAutonoma> comunidades_All;
    private final List<ComunidadAutonoma> comunidades_Suggestion;
    private Context context;
    private int layout;
    private List<ComunidadAutonoma> comunidades;


    public AdaptadorComunidades(Context context, int layout, List<ComunidadAutonoma> comunidades) {
        super(context, layout);
        this.context = context;
        this.layout = layout;
        this.comunidades = comunidades;
        this.comunidades_All = new ArrayList<>(comunidades);
        this.comunidades_Suggestion = new ArrayList<>();
    }


    @Override
    public int getCount() {
        return comunidades.size();
    }

    @Override
    public ComunidadAutonoma getItem(int position) {
        return this.comunidades.get(position);
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
            ComunidadAutonoma comunidadAutonoma = getItem(position);
            TextView name = (TextView) convertView.findViewById(R.id.tvNombreItem);
            name.setText(comunidadAutonoma.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;

    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            public String convertResultToString(Object resultValue) {
                return ((ComunidadAutonoma) resultValue).getNombre();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if (constraint != null) {
                    comunidades_Suggestion.clear();
                    for (ComunidadAutonoma item : comunidades_All) {
                        if (item.toString().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            comunidades_Suggestion.add(item);
                        }
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = comunidades_Suggestion;
                    filterResults.count = comunidades_Suggestion.size();
                    return filterResults;
                } else {
                    return new FilterResults();
                }
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                comunidades.clear();
                if (results != null && results.count > 0) {
                    // avoids unchecked cast warning when using mDepartments.addAll((ArrayList<Department>) results.values);
                    List<?> result = (List<?>) results.values;
                    for (Object object : result) {
                        if (object instanceof ComunidadAutonoma) {
                            comunidades.add((ComunidadAutonoma) object);
                        }
                    }
                } else if (constraint == null) {
                    // no filter, add entire original list back in
                    comunidades.addAll(comunidades_All);
                }
                notifyDataSetChanged();
            }
        };
    }

    static class ViewHolder { //clase anidada y estática
        private TextView tvNombreItem;

    }
}

