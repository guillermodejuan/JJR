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
import com.cta.android.appcomercial.model.Cargo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC on 28/11/2017.
 */

public class AdaptadorCargos extends ArrayAdapter<Cargo> {
    private final List<Cargo> cargos_All;
    private final List<Cargo> cargos_Suggestion;
    private Context context;
    private int layout;
    private List<Cargo> cargos;


    public AdaptadorCargos(Context context, int layout, List<Cargo> cargos) {
        super(context, layout);
        this.context = context;
        this.layout = layout;
        this.cargos = cargos;
        this.cargos_All = new ArrayList<>(cargos);
        this.cargos_Suggestion = new ArrayList<>();
    }


    @Override
    public int getCount() {
        return cargos.size();
    }

    @Override
    public Cargo getItem(int position) {
        return this.cargos.get(position);
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
            Cargo cargo = getItem(position);
            TextView name = (TextView) convertView.findViewById(R.id.tvNombreItem);
            name.setText(cargo.toString());
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
                return ((Cargo) resultValue).getNombre();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if (constraint != null) {
                    cargos_Suggestion.clear();
                    for (Cargo item : cargos_All) {
                        if (item.toString().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            cargos_Suggestion.add(item);
                        }
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = cargos_Suggestion;
                    filterResults.count = cargos_Suggestion.size();
                    return filterResults;
                } else {
                    return new FilterResults();
                }
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                cargos.clear();
                if (results != null && results.count > 0) {
                    // avoids unchecked cast warning when using mDepartments.addAll((ArrayList<Department>) results.values);
                    List<?> result = (List<?>) results.values;
                    for (Object object : result) {
                        if (object instanceof Cargo) {
                            cargos.add((Cargo) object);
                        }
                    }
                } else if (constraint == null) {
                    // no filter, add entire original list back in
                    cargos.addAll(cargos_All);
                }
                notifyDataSetChanged();
            }
        };
    }

    static class ViewHolder { //clase anidada y estática
        private TextView tvNombreItem;
    }
}