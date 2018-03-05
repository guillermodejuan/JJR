package com.cta.android.appcomercial.controladores;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;

import com.cta.android.appcomercial.R;
import com.cta.android.appcomercial.adaptadores.AdaptadorListaVisitas;
import com.cta.android.appcomercial.model.Visita;
import com.cta.android.appcomercial.util.DataBaseHelper;
import com.cta.android.appcomercial.util.Util;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Created by PC on 30/10/2017.
 */

public class VisitasFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {


    private ImageButton ibDiaSiguiente;
    private ImageButton ibDiaAnterior;
    private Button btFechaVisitas;
    private ImageButton ibAnadirVisita;
    private ListView listaVisitas;

    private String fechaString;
    private Calendar fechaDate;
    private SimpleDateFormat simpleDateFormat;

    public static VisitasFragment newInstance(String contenido) {
        VisitasFragment fragment = new VisitasFragment();
        Bundle bundle = new Bundle(1); // se puede especificar el numero de argumentos, en este caso 1
        bundle.putString("content", contenido);
        fragment.setArguments(bundle);
        return fragment;

    }

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2017-12-21 16:32:51 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews(View v) {

        ibDiaSiguiente = (ImageButton) v.findViewById(R.id.ibDiaSiguiente);
        ibDiaAnterior = (ImageButton) v.findViewById(R.id.ibDiaAnterior);
        btFechaVisitas = (Button) v.findViewById(R.id.btFechaVisitas);
        ibAnadirVisita = (ImageButton) v.findViewById(R.id.ibAnadirVisita);
        listaVisitas = (ListView) v.findViewById(R.id.listaVisitas);

        ibDiaSiguiente.setOnClickListener(this);
        ibDiaAnterior.setOnClickListener(this);
        btFechaVisitas.setOnClickListener(this);
        ibAnadirVisita.setOnClickListener(this);
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2017-12-21 16:32:51 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if (v == ibDiaSiguiente) {
            // Handle clicks for ibDiaSiguiente
            fechaDate.add(Calendar.DATE, 1);
            fechaString = simpleDateFormat.format(fechaDate.getTime());
            btFechaVisitas.setText(fechaString);
            cargarVisitas();

        } else if (v == ibDiaAnterior) {
            // Handle clicks for ibDiaAnterior
            fechaDate.add(Calendar.DATE, -1);
            fechaString = simpleDateFormat.format(fechaDate.getTime());
            btFechaVisitas.setText(fechaString);
            cargarVisitas();

        } else if (v == btFechaVisitas) {
            // Handle clicks for btFechaVisitas
            int year = fechaDate.get(Calendar.YEAR);
            int month = fechaDate.get(Calendar.MONTH);
            int day = fechaDate.get(Calendar.DAY_OF_MONTH);
            final DatePickerDialog datePickerDialog = new DatePickerDialog(
                    getContext(), this, year, month, day);
            datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.btGuardar), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DatePicker dp = datePickerDialog.getDatePicker();
                    int day = dp.getDayOfMonth();
                    int month = dp.getMonth();
                    int year = dp.getYear();
                    fechaDate = Calendar.getInstance();
                    fechaDate.set(year, month, day);
                    fechaDate.set(Calendar.HOUR_OF_DAY, 0);
                    fechaDate.set(Calendar.MINUTE, 0);
                    fechaDate.set(Calendar.SECOND, 0);
                    fechaDate.set(Calendar.MILLISECOND, 0);
                    fechaString = simpleDateFormat.format(fechaDate.getTime());
                    btFechaVisitas.setText(fechaString);
                    cargarVisitas();
                }
            });
            datePickerDialog.show();
        } else if (v == ibAnadirVisita) {
            // Handle clicks for ibAnadirVisita
            AnadirVisitaFragment anadirVisitaFragment = new AnadirVisitaFragment();
            Bundle bundle = new Bundle(1); // se puede especificar el numero de argumentos, en este caso 1
            bundle.putString("fecha", fechaString);
            anadirVisitaFragment.setArguments(bundle);
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayoutVisitas, anadirVisitaFragment, "nuevaVisita");
            transaction.commit();
            for (int x = 0; x < listaVisitas.getCount(); x++) {
                listaVisitas.getChildAt(x).setEnabled(false);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_visitas, container, false);
        findViews(view);
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        fechaDate = Calendar.getInstance();
        fechaDate.set(Calendar.HOUR_OF_DAY, 0);
        fechaDate.set(Calendar.MINUTE, 0);
        fechaDate.set(Calendar.SECOND, 0);
        fechaDate.set(Calendar.MILLISECOND, 0);
        fechaString = simpleDateFormat.format(fechaDate.getTime());
        btFechaVisitas.setText(fechaString);
        cargarVisitas();


        return view;


    }

    public void cargarVisitas() {

        ArrayList<Visita> visitas = new ArrayList<Visita>();
        ArrayList<String[]> listaCal = Util.readCalendarEvent(getContext(), fechaDate);
        for (String[] array : listaCal) {
            Visita visita = new Visita();
            visita.setId(Integer.parseInt(array[5]));
            visita.setTitulo(array[0]);
            visita.setNotas(array[1]);
            try {
                visita.setFechainicio(Long.parseLong(array[3]));
                visita.setFechafinal(Long.parseLong(array[4]));
            } catch (NumberFormatException nfe) {

            }
            visita.setEmplazamiento(array[2]);
            visitas.add(visita);
        }

        List<Visita> lista = null;
        try {
            Dao<Visita, Integer> visitasDao = OpenHelperManager.getHelper(getContext(), DataBaseHelper.class).getVisitaDao();
            Calendar nextDay = Calendar.getInstance();
            nextDay.setTimeInMillis(fechaDate.getTimeInMillis());
            nextDay.add(Calendar.DATE, 1);
            lista = visitasDao.queryBuilder().where().between("fechainicio", fechaDate.getTimeInMillis(), nextDay.getTimeInMillis()).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (lista != null) {
            for (Visita v : lista) {
                visitas.add(v);
            }

            Collections.sort(visitas);
        }
        listaVisitas.setAdapter(new AdaptadorListaVisitas(visitas, getContext(), getActivity(), R.layout.itemvisita, this));

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }

}
