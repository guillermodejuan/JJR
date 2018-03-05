package com.cta.android.appcomercial.adaptadores;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.cta.android.appcomercial.R;
import com.cta.android.appcomercial.model.Cargo;
import com.cta.android.appcomercial.model.ContactoCliente;
import com.cta.android.appcomercial.model.Producto;
import com.cta.android.appcomercial.util.DataBaseHelper;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC on 21/01/2018.
 */

public class AdaptadorSpinnerProductos extends ArrayAdapter<Producto> {


    private Context context;
    private int layout;
    private List<Producto> productos;
    private final List<Producto> productos_All;
    private final List<Producto> productos_Suggestion;

    public AdaptadorSpinnerProductos(Context context, int layout, List<Producto> productos) {
        super(context, layout);
        this.context = context;
        this.layout = layout;
        this.productos = productos;
        this.productos_All = new ArrayList<>(productos);
        this.productos_Suggestion = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return productos.size();
    }

    @Override
    public Producto getItem(int position) {
        return this.productos.get(position);
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
            Producto producto = getItem(position);
            TextView nametv = (TextView) convertView.findViewById(R.id.tvNombreItem);
            nametv.setText(producto.getDescripcion());

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
                return ((Producto) resultValue).getDescripcion();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if (constraint != null) {
                    productos_Suggestion.clear();
                    for (Producto item : productos_All) {
                        if (item.toString().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            productos_Suggestion.add(item);
                        }
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = productos_Suggestion;
                    filterResults.count = productos_Suggestion.size();
                    return filterResults;
                } else {
                    return new FilterResults();
                }
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                productos.clear();
                if (results != null && results.count > 0) {
                    // avoids unchecked cast warning when using mDepartments.addAll((ArrayList<Department>) results.values);
                    List<?> result = (List<?>) results.values;
                    for (Object object : result) {
                        if (object instanceof Producto) {
                            productos.add((Producto) object);
                        }
                    }
                } else if (constraint == null) {
                    // no filter, add entire original list back in
                    productos.addAll(productos_All);
                }
                notifyDataSetChanged();
            }
        };
    }

    static class ViewHolder { //clase anidada y estática
        private TextView tvNombreItem;

    }
}
