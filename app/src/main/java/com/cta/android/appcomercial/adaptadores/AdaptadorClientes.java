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
import com.cta.android.appcomercial.model.Cliente;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC on 28/11/2017.
 */

public class AdaptadorClientes extends ArrayAdapter<Cliente> {
    private final List<Cliente> clientes_All;
    private final List<Cliente> clientes_Suggestion;
    private Context context;
    private int layout;
    private List<Cliente> clientes;


    public AdaptadorClientes(Context context, int layout, List<Cliente> clientes) {
        super(context, layout);
        this.context = context;
        this.layout = layout;
        this.clientes = clientes;
        this.clientes_All = new ArrayList<>(clientes);
        this.clientes_Suggestion = new ArrayList<>();
    }


    @Override
    public int getCount() {
        return clientes.size();
    }

    @Override
    public Cliente getItem(int position) {
        return this.clientes.get(position);
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
            Cliente cliente = getItem(position);
            TextView name = (TextView) convertView.findViewById(R.id.tvNombreItem);
            name.setText(cliente.toString());
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
                return ((Cliente) resultValue).getNombreComercial();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if (constraint != null) {
                    clientes_Suggestion.clear();
                    for (Cliente item : clientes_All) {
                        if (item.toString().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            clientes_Suggestion.add(item);
                        }
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = clientes_Suggestion;
                    filterResults.count = clientes_Suggestion.size();
                    return filterResults;
                } else {
                    return new FilterResults();
                }
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                clientes.clear();
                if (results != null && results.count > 0) {
                    // avoids unchecked cast warning when using mDepartments.addAll((ArrayList<Department>) results.values);
                    List<?> result = (List<?>) results.values;
                    for (Object object : result) {
                        if (object instanceof Cliente) {
                            clientes.add((Cliente) object);
                        }
                    }
                } else if (constraint == null) {
                    // no filter, add entire original list back in
                    clientes.addAll(clientes_All);
                }
                notifyDataSetChanged();
            }
        };
    }

    static class ViewHolder { //clase anidada y estática
        private TextView tvNombreItem;

    }
}