package com.cta.android.appcomercial.adaptadores;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.cta.android.appcomercial.R;
import com.cta.android.appcomercial.controladores.VisitasFragment;
import com.cta.android.appcomercial.model.Cliente;
import com.cta.android.appcomercial.model.ContactoCliente;
import com.cta.android.appcomercial.model.ContactoRepresentado;
import com.cta.android.appcomercial.model.Localidad;
import com.cta.android.appcomercial.model.Representado;
import com.cta.android.appcomercial.model.Visita;
import com.cta.android.appcomercial.util.DataBaseHelper;
import com.cta.android.appcomercial.util.Util;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by PC on 02/01/2018.
 */

public class AdaptadorListaVisitas extends BaseAdapter implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    ArrayList<Visita> listaVisitas;
    Context context;
    Activity activity;
    int layout;
    VisitasFragment visitasFragment;

    //variables para menu contextual
    int telefonoempresa = 0;
    int fijocontacto = 0;
    int movilcontacto = 0;
    String emailempresa = null;
    String emailcontacto = null;

    public AdaptadorListaVisitas(ArrayList<Visita> listaVisitas, Context context, Activity activity, int layout, VisitasFragment visitasFragment) {
        this.listaVisitas = listaVisitas;
        this.context = context;
        this.activity = activity;
        this.layout = layout;
        this.visitasFragment = visitasFragment;
    }

    @Override
    public int getCount() {
        return listaVisitas.size();
    }

    @Override
    public Visita getItem(int position) {
        return listaVisitas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            if (convertView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                convertView = inflater.inflate(layout, parent, false);
            }

            TextView titulotv = (TextView) convertView.findViewById(R.id.tvTituloVisita);
            titulotv.setText(listaVisitas.get(position).getTitulo());

            TextView ubicaciontv = (TextView) convertView.findViewById(R.id.tvLugarItemVisita);
            if (listaVisitas.get(position).getCliente() != null) {
                Cliente cliente = OpenHelperManager.getHelper(context, DataBaseHelper.class).getClienteRuntimeDAO().queryForId(listaVisitas.get(position).getCliente().getId());
                if (cliente.getDireccion() != null && !cliente.getDireccion().equals("") &&
                        cliente.getLocalidad() != null) {
                    Localidad localidad = (Localidad) OpenHelperManager.getHelper(context, DataBaseHelper.class).getLocalidadRuntimeDAO().queryForId(cliente.getLocalidad().getId());
                    ubicaciontv.setText((cliente.getDireccion() + ", " + localidad.getNombre()));
                }
            } else if (listaVisitas.get(position).getRepresentado() != null) {
                Representado representado = OpenHelperManager.getHelper(context, DataBaseHelper.class).getRepresentadoRuntimeDAO().queryForId(listaVisitas.get(position).getRepresentado().getId());
                if (representado.getDireccion() != null && !representado.getDireccion().equals("") &&
                        representado.getLocalidad() != null) {
                    Localidad localidad = (Localidad) OpenHelperManager.getHelper(context, DataBaseHelper.class).getLocalidadRuntimeDAO().queryForId(representado.getLocalidad().getId());
                    ubicaciontv.setText(representado.getDireccion() + ", " + localidad.getNombre());
                }
            } else {
                ubicaciontv.setText(listaVisitas.get(position).getEmplazamiento());
            }

            //Hora de inicio
            TextView horainiciotv = (TextView) convertView.findViewById(R.id.tvHoraInicioItemVisita);
            Calendar calinicio = Calendar.getInstance();
            calinicio.setTimeInMillis(listaVisitas.get(position).getFechainicio());
            String hora, minuto;
            if (calinicio.get(Calendar.HOUR_OF_DAY) < 10) {
                hora = "0" + calinicio.get(Calendar.HOUR_OF_DAY);
            } else {
                hora = "" + calinicio.get(Calendar.HOUR_OF_DAY);
            }

            if (calinicio.get(Calendar.MINUTE) < 10) {
                minuto = "0" + calinicio.get(Calendar.MINUTE);
            } else {
                minuto = "" + calinicio.get(Calendar.MINUTE);
            }
            horainiciotv.setText(hora + ":" + minuto);
            //Hora final
            TextView horafinaltv = (TextView) convertView.findViewById(R.id.tvHoraFinItemVisita);
            if (listaVisitas.get(position).getFechafinal() != 0) {
                Calendar calfinal = Calendar.getInstance();
                calfinal.setTimeInMillis(listaVisitas.get(position).getFechafinal());
                String horaf, minutof;
                if (calfinal.get(Calendar.HOUR_OF_DAY) < 10) {
                    horaf = "0" + calfinal.get(Calendar.HOUR_OF_DAY);
                } else {
                    horaf = "" + calfinal.get(Calendar.HOUR_OF_DAY);
                }

                if (calfinal.get(Calendar.MINUTE) < 10) {
                    minutof = "0" + calfinal.get(Calendar.MINUTE);
                } else {
                    minutof = "" + calfinal.get(Calendar.MINUTE);
                }

                horafinaltv.setText(horaf + ":" + minutof);
            } else {
                horafinaltv.setText("         ");
            }

            ImageButton ibLlamada = (ImageButton) convertView.findViewById(R.id.ibLlamadaItemVisita);
            ImageButton ibEmail = (ImageButton) convertView.findViewById(R.id.ibEmailItemVisita);
            ImageButton ibRuta = (ImageButton) convertView.findViewById(R.id.ibNavegarItemVisita);
            ImageButton ibNotas = (ImageButton) convertView.findViewById(R.id.ibInfoItemVisita);
            ImageButton ibEliminar = (ImageButton) convertView.findViewById(R.id.ibBorrarItemVisita);

            ibLlamada.setTag(position);
            ibEmail.setTag(position);
            ibRuta.setTag(position);
            ibNotas.setTag(position);
            ibEliminar.setTag(position);

            ibLlamada.setOnClickListener(this);
            ibEmail.setOnClickListener(this);
            ibRuta.setOnClickListener(this);
            ibNotas.setOnClickListener(this);
            ibEliminar.setOnClickListener(this);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }


    @Override
    public void onClick(final View v) {

        switch (v.getId()) {
            case R.id.ibLlamadaItemVisita:
                PopupMenu popup = new PopupMenu(context, v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.menullamadavisita, popup.getMenu());
                popup.setOnMenuItemClickListener(this);
                if (listaVisitas.get(Integer.parseInt(v.getTag().toString())).getCliente() != null) {
                    Cliente cliente = null;
                    try {
                        cliente = OpenHelperManager.getHelper(context, DataBaseHelper.class).getClienteRuntimeDAO().queryForId(listaVisitas.get(Integer.parseInt(v.getTag().toString())).getCliente().getId());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    if (cliente.getTelefono() == 0) {
                        popup.getMenu().getItem(0).setEnabled(false);
                    } else {
                        telefonoempresa = cliente.getTelefono();
                    }
                    if (listaVisitas.get(Integer.parseInt(v.getTag().toString())).getContactoCliente() != null) {
                        ContactoCliente contactoCliente = OpenHelperManager.getHelper(context, DataBaseHelper.class).getContactoClienteRuntimeDAO().queryForId(listaVisitas.get(Integer.parseInt(v.getTag().toString())).getContactoCliente().getId());
                        if (contactoCliente.getTelefono() == 0) {
                            popup.getMenu().getItem(1).setEnabled(false);
                        } else {
                            fijocontacto = contactoCliente.getTelefono();
                        }
                        if (contactoCliente.getMovil() == 0) {
                            popup.getMenu().getItem(2).setEnabled(false);
                        } else {
                            movilcontacto = contactoCliente.getMovil();
                        }
                    } else {
                        popup.getMenu().getItem(1).setEnabled(false);
                        popup.getMenu().getItem(2).setEnabled(false);
                    }
                    popup.show();
                } else {
                    if (listaVisitas.get(Integer.parseInt(v.getTag().toString())).getRepresentado() != null) {
                        Representado representado = null;
                        try {
                            representado = OpenHelperManager.getHelper(context, DataBaseHelper.class).getRepresentadoRuntimeDAO().queryForId(listaVisitas.get(Integer.parseInt(v.getTag().toString())).getRepresentado().getId());
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        if (representado.getTelefono() == 0) {
                            popup.getMenu().getItem(0).setEnabled(false);
                        } else {
                            telefonoempresa = representado.getTelefono();
                        }
                        if (listaVisitas.get(Integer.parseInt(v.getTag().toString())).getContactoRepresentado() != null) {
                            ContactoRepresentado contactoRepresentado = OpenHelperManager.getHelper(context, DataBaseHelper.class).getContactoRepresentadoRuntimeDAO().queryForId(listaVisitas.get(Integer.parseInt(v.getTag().toString())).getContactoRepresentado().getId());
                            if (contactoRepresentado.getTelefono() == 0) {
                                popup.getMenu().getItem(1).setEnabled(false);
                            } else {
                                fijocontacto = contactoRepresentado.getTelefono();
                            }
                            if (contactoRepresentado.getMovil() == 0) {
                                popup.getMenu().getItem(2).setEnabled(false);
                            } else {
                                movilcontacto = contactoRepresentado.getMovil();
                            }
                        } else {
                            popup.getMenu().getItem(1).setEnabled(false);
                            popup.getMenu().getItem(2).setEnabled(false);
                        }
                        popup.show();
                    }
                }

                break;
            case R.id.ibEmailItemVisita:

                PopupMenu popup2 = new PopupMenu(context, v);
                MenuInflater inflater2 = popup2.getMenuInflater();
                inflater2.inflate(R.menu.menuemailvisita, popup2.getMenu());
                popup2.setOnMenuItemClickListener(this);
                if (listaVisitas.get(Integer.parseInt(v.getTag().toString())).getCliente() != null) {
                    Cliente cliente = null;
                    try {
                        cliente = OpenHelperManager.getHelper(context, DataBaseHelper.class).getClienteRuntimeDAO().queryForId(listaVisitas.get(Integer.parseInt(v.getTag().toString())).getCliente().getId());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    if (cliente.getEmail() == null || cliente.getEmail().equals("")) {
                        popup2.getMenu().getItem(0).setEnabled(false);
                        popup2.getMenu().getItem(2).setEnabled(false);
                    } else {
                        emailempresa = cliente.getEmail();
                    }
                    if (listaVisitas.get(Integer.parseInt(v.getTag().toString())).getContactoCliente() != null) {
                        ContactoCliente contactoCliente = OpenHelperManager.getHelper(context, DataBaseHelper.class).getContactoClienteRuntimeDAO().queryForId(listaVisitas.get(Integer.parseInt(v.getTag().toString())).getContactoCliente().getId());
                        if (contactoCliente.getEmail() == null || contactoCliente.getEmail().equals("")) {
                            popup2.getMenu().getItem(1).setEnabled(false);
                            popup2.getMenu().getItem(2).setEnabled(false);
                        } else {
                            emailcontacto = contactoCliente.getEmail();
                        }

                    } else {
                        popup2.getMenu().getItem(1).setEnabled(false);
                        popup2.getMenu().getItem(2).setEnabled(false);
                    }
                    popup2.show();
                } else {
                    if (listaVisitas.get(Integer.parseInt(v.getTag().toString())).getRepresentado() != null) {
                        Representado representado = null;
                        try {
                            representado = OpenHelperManager.getHelper(context, DataBaseHelper.class).getRepresentadoRuntimeDAO().queryForId(listaVisitas.get(Integer.parseInt(v.getTag().toString())).getRepresentado().getId());
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        if (representado.getEmail() == null || representado.getEmail().equals("")) {
                            popup2.getMenu().getItem(0).setEnabled(false);
                            popup2.getMenu().getItem(2).setEnabled(false);
                        } else {
                            emailempresa = representado.getEmail();
                        }
                        if (listaVisitas.get(Integer.parseInt(v.getTag().toString())).getContactoRepresentado() != null) {
                            ContactoRepresentado contactoRepresentado = OpenHelperManager.getHelper(context, DataBaseHelper.class).getContactoRepresentadoRuntimeDAO().queryForId(listaVisitas.get(Integer.parseInt(v.getTag().toString())).getContactoRepresentado().getId());
                            if (contactoRepresentado.getEmail() == null || contactoRepresentado.getEmail().equals("")) {
                                popup2.getMenu().getItem(1).setEnabled(false);
                                popup2.getMenu().getItem(2).setEnabled(false);
                            } else {
                                emailcontacto = contactoRepresentado.getEmail();
                            }

                        } else {
                            popup2.getMenu().getItem(1).setEnabled(false);
                            popup2.getMenu().getItem(2).setEnabled(false);
                        }
                        popup2.show();
                    }
                }


                break;
            case R.id.ibNavegarItemVisita:
                if (listaVisitas.get(Integer.parseInt(v.getTag().toString())).getEmplazamiento() != null && !listaVisitas.get(Integer.parseInt(v.getTag().toString())).getEmplazamiento().equals("")) {
                    Util.abrirGPS(listaVisitas.get(Integer.parseInt(v.getTag().toString())).getEmplazamiento(), context);
                } else {
                    if (listaVisitas.get(Integer.parseInt(v.getTag().toString())).getCliente() != null) {
                        Cliente cliente = null;
                        try {
                            cliente = OpenHelperManager.getHelper(context, DataBaseHelper.class).getClienteRuntimeDAO().queryForId(listaVisitas.get(Integer.parseInt(v.getTag().toString())).getCliente().getId());
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        if (cliente.getDireccion() != null && !cliente.getDireccion().equals("") &&
                                cliente.getLocalidad() != null) {
                            Localidad localidad = (Localidad) OpenHelperManager.getHelper(context, DataBaseHelper.class).getLocalidadRuntimeDAO().queryForId(cliente.getLocalidad().getId());
                            Util.abrirGPS(cliente.getDireccion() + ", " + localidad.getNombre(), context);
                        }
                    } else if (listaVisitas.get(Integer.parseInt(v.getTag().toString())).getRepresentado() != null) {
                        Representado representado = null;
                        try {
                            representado = OpenHelperManager.getHelper(context, DataBaseHelper.class).getRepresentadoRuntimeDAO().queryForId(listaVisitas.get(Integer.parseInt(v.getTag().toString())).getRepresentado().getId());
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        if (representado.getDireccion() != null && !representado.getDireccion().equals("") &&
                                representado.getLocalidad() != null) {
                            Localidad localidad = (Localidad) OpenHelperManager.getHelper(context, DataBaseHelper.class).getLocalidadRuntimeDAO().queryForId(representado.getLocalidad().getId());
                            Util.abrirGPS(representado.getDireccion() + ", " + localidad.getNombre(), context);
                        }
                    }
                }
                break;
            case R.id.ibInfoItemVisita:
                if (listaVisitas.get(Integer.parseInt(v.getTag().toString())).getNotas() != null && !listaVisitas.get(Integer.parseInt(v.getTag().toString())).getNotas().equals("")) {
                    Util.tostar(listaVisitas.get(Integer.parseInt(v.getTag().toString())).getNotas(), context);
                }
                break;
            case R.id.ibBorrarItemVisita:
                new AlertDialog.Builder(context)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Eliminar Visita")
                        .setMessage("¿Confirma que desea eliminar la visita seleccionada?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Uri uri = null;
                                if (listaVisitas.get(Integer.parseInt(v.getTag().toString())).getCliente() == null && listaVisitas.get(Integer.parseInt(v.getTag().toString())).getRepresentado() == null) {
                                    uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, listaVisitas.get(Integer.parseInt(v.getTag().toString())).getId());
                                    activity.getContentResolver().delete(uri, null, null);
                                } else {
                                    Visita visita = listaVisitas.get(Integer.parseInt(v.getTag().toString()));
                                    try {
                                        Dao<Visita, Integer> visitasDao = OpenHelperManager.getHelper(context, DataBaseHelper.class).getVisitaDao();
                                        visitasDao.delete(visita);
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                }


                                visitasFragment.cargarVisitas();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

                break;
            default:
                break;
        }


    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemempresa:
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, 0);
                } else {
                    Util.llamada(telefonoempresa, context, activity);
                }
                break;
            case R.id.itemfijocontacto:
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, 0);
                } else {
                    Util.llamada(fijocontacto, context, activity);
                }
                break;
            case R.id.itemmovilcontacto:
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, 0);
                } else {
                    Util.llamada(movilcontacto, context, activity);
                }
                break;
            case R.id.itememailempresa:
                ArrayList<String> direcciones = new ArrayList<String>();
                direcciones.add(emailempresa);
                Util.sendEmail(direcciones, context);
                break;
            case R.id.itememailcontacto:
                ArrayList<String> direcciones2 = new ArrayList<String>();
                direcciones2.add(emailcontacto);
                Util.sendEmail(direcciones2, context);
                break;
            case R.id.itemlistaemail:
                ArrayList<String> direcciones3 = new ArrayList<String>();
                direcciones3.add(emailempresa);
                direcciones3.add(emailcontacto);
                Util.sendEmail(direcciones3, context);
                break;
            default:
                break;
        }
        return false;
    }
}
