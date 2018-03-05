package com.cta.android.appcomercial.controladores;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.cta.android.appcomercial.R;
import com.cta.android.appcomercial.adaptadores.AdaptadorSectoresCheckBox;
import com.cta.android.appcomercial.model.Cliente;
import com.cta.android.appcomercial.model.ClienteSector;
import com.cta.android.appcomercial.model.Sector;
import com.cta.android.appcomercial.util.DataBaseHelper;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by PC on 13/01/2018.
 */

public class SectoresFragment extends Fragment {

    ListView lista;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sectores, container, false);
        lista = (ListView) view.findViewById(R.id.listaSectores);
        DataBaseHelper helper = OpenHelperManager.getHelper(getContext(), DataBaseHelper.class);
        final RuntimeExceptionDao<Sector, Integer> runtimeExceptionDao = helper.getSectorRuntimeDAO();
        ArrayList<Sector> sectores = (ArrayList<Sector>) runtimeExceptionDao.queryForAll();
        Collections.sort(sectores);
        final AdaptadorSectoresCheckBox adaptador = new AdaptadorSectoresCheckBox(getContext(), R.layout.itemsector, sectores, (Cliente) getArguments().getParcelable("empresa"));
        lista.setAdapter(adaptador);

        TextView tv = (TextView) view.findViewById(R.id.tvCabeceraSectores);
        tv.setText("SECTORES: " + ((Cliente) getArguments().getParcelable("empresa")).getNombreComercial());
        ImageButton ibAtras = (ImageButton) view.findViewById(R.id.ibSalirSectores);
        ImageButton ibGuardar = (ImageButton) view.findViewById(R.id.ibGuardarSectores);

        ibAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarFragmento();
            }
        });
        ibGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dao<ClienteSector, Integer> dao = null;
                try {
                    dao = OpenHelperManager.getHelper(getContext(), DataBaseHelper.class).getClienteSectorDao();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                for (int x = 0; x < adaptador.getCount(); x++) {
                    CheckBox cb = (CheckBox) lista.getChildAt(x);
                    try {
                        if (cb.isChecked()) {
                            ClienteSector clienteSector = new ClienteSector();
                            clienteSector.setCliente((Cliente) getArguments().getParcelable("empresa"));
                            clienteSector.setSector(adaptador.getItem(x));
                            dao.create(clienteSector);
                        } else {
                            List<ClienteSector> lista = dao.queryBuilder().where().eq("cliente_id", ((Cliente) getArguments().getParcelable("empresa")).getId()).and().eq("sector_id", adaptador.getItem(x).getId()).query();
                            if (lista.size() > 0) {
                                dao.delete(lista.get(0));
                            }
                        }
                    } catch (SQLException sqle) {
                        sqle.printStackTrace();
                    }
                }

                cerrarFragmento();

            }
        });
        return view;
    }

    private void cerrarFragmento() {
        getFragmentManager().beginTransaction().remove(this).commit();
        EmpresasFragment empresasFragment = (EmpresasFragment) this.getParentFragment();
        for (int x = 0; x < empresasFragment.getFramEmpresas().getChildCount(); x++) {
            empresasFragment.getFramEmpresas().getChildAt(x).setEnabled(true);
        }
    }
}
