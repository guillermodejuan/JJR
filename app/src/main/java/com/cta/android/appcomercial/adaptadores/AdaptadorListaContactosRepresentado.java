package com.cta.android.appcomercial.adaptadores;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.cta.android.appcomercial.R;
import com.cta.android.appcomercial.controladores.ListaContactosFragment;
import com.cta.android.appcomercial.model.Cargo;
import com.cta.android.appcomercial.model.ContactoRepresentado;
import com.cta.android.appcomercial.util.DataBaseHelper;
import com.cta.android.appcomercial.util.Util;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC on 15/12/2017.
 */

public class AdaptadorListaContactosRepresentado extends RecyclerView.Adapter<AdaptadorListaContactosRepresentado.ContactosHolder> implements View.OnLongClickListener, View.OnClickListener {

    Context context;
    Activity activity;
    ListaContactosFragment listaContactosFragment;
    private List<ContactoRepresentado> contactos;

    public AdaptadorListaContactosRepresentado(List<ContactoRepresentado> contactos, ListaContactosFragment listaContactosFragment) {
        this.contactos = contactos;
        this.context = listaContactosFragment.getContext();
        this.activity = listaContactosFragment.getActivity();
        this.listaContactosFragment = listaContactosFragment;
    }

    @Override
    public ContactosHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final ViewGroup p = parent;
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_contactos, parent, false);
        ContactosHolder holder = new ContactosHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(ContactosHolder holder, int position) {
        final ContactoRepresentado contactoRepresentado = contactos.get(position);

        DataBaseHelper helper = OpenHelperManager.getHelper(context, DataBaseHelper.class);
        holder.ibFoto.setImageResource(R.drawable.tvborder);
        holder.ibFoto.setTag(position);
        holder.ibFoto.setOnLongClickListener(this);
        holder.tvNombre.setText(contactoRepresentado.getNombre() + " " + contactoRepresentado.getApellidos());
        holder.tvNombre.setTag(position);
        holder.tvNombre.setOnLongClickListener(this);
        activity.registerForContextMenu(holder.tvNombre);

        if (contactoRepresentado.getCargo() != null) {
            RuntimeExceptionDao<Cargo, Integer> cargoIntegerRuntimeExceptionDao = helper.getCargoRuntimeDAO();
            Cargo cargo = cargoIntegerRuntimeExceptionDao.queryForId(contactoRepresentado.getCargo().getId());
            holder.tvCargo.setText(cargo.getNombre());
        } else {
            holder.tvCargo.setText("Cargo ...");
        }
        holder.tvCargo.setOnLongClickListener(this);
        holder.tvCargo.setTag(position);

        if (contactoRepresentado.getTelefono() != 0) {
            holder.tvFijo.setText(String.valueOf(contactoRepresentado.getTelefono()));
            holder.tvFijo.setOnClickListener(this);
        } else
            holder.tvFijo.setText("Teléfono ...");
        holder.tvFijo.setOnLongClickListener(this);
        holder.tvFijo.setTag(position);
        if (contactoRepresentado.getMovil() != 0) {
            holder.tvMovil.setText(String.valueOf(contactoRepresentado.getMovil()));
            holder.tvMovil.setOnClickListener(this);
        } else
            holder.tvMovil.setText("Móvil ...");
        holder.tvMovil.setOnLongClickListener(this);
        holder.tvMovil.setTag(position);
        if (!contactoRepresentado.getEmail().equals("")) {
            holder.tvEmail.setText(contactoRepresentado.getEmail());
            holder.tvEmail.setOnClickListener(this);
        } else
            holder.tvEmail.setText("Email ...");
        holder.tvEmail.setOnLongClickListener(this);
        holder.tvEmail.setTag(position);

        Bitmap bitmap = BitmapFactory.decodeByteArray(contactoRepresentado.getFoto(), 0, contactoRepresentado.getFoto().length);
        holder.ibFoto.setImageBitmap(bitmap);

        holder.ibEliminar.setTag(position);
        holder.ibEliminar.setOnClickListener(this);
        holder.ibCita.setTag(position);
        holder.ibCita.setOnClickListener(this);

    }

    @Override
    public int getItemCount() {
        return contactos.size();
    }

    @Override
    public boolean onLongClick(View v) {
        final ContactoRepresentado contactoRepresentado = (ContactoRepresentado) contactos.get(Integer.parseInt(v.getTag().toString()));

        final EditText input = new EditText(context);
        switch (v.getId()) {
            case R.id.tvNombreProductoLista:
                crearDialogoNombreCompleto(contactoRepresentado);
                break;
            case R.id.tvReferenciaProductoLista:
                crearDialogoCargo(contactoRepresentado);
                break;
            case R.id.tvCaducidadProductoLista:
                input.setHint(context.getResources().getString(R.string.telefonoContacto));
                input.setInputType(InputType.TYPE_CLASS_PHONE);
                crearDialogo(input, contactoRepresentado);
                break;
            case R.id.tvPrecioProductoLista:
                input.setHint(context.getResources().getString(R.string.movilContacto));
                input.setInputType(InputType.TYPE_CLASS_PHONE);
                crearDialogo(input, contactoRepresentado);
                break;
            case R.id.tvSectorProductoLista:
                input.setHint(context.getResources().getString(R.string.emailContacto));
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                crearDialogo(input, contactoRepresentado);
                break;
            case R.id.ibFotoProductoLista:
                tomarFoto(v.getTag().toString());
                break;
            default:
                break;
        }
        return false;
    }

    private void crearDialogoCargo(final ContactoRepresentado contactoRepresentado) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View v = inflater.inflate(R.layout.dialogocargo, null);
        builder.setView(v);

        final Spinner spCargo = (Spinner) v.findViewById(R.id.spUpdateSector);
        DataBaseHelper helper = OpenHelperManager.getHelper(context, DataBaseHelper.class);
        List<Cargo> cargos = null;
        try {
            Dao<Cargo, Integer> daoCargo = helper.getCargoDAO();
            cargos = daoCargo.queryBuilder().orderBy("nombre", true).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Cargo cargo = new Cargo();
        cargo.setNombre(context.getResources().getString(R.string.selectCargo));
        cargos.add(0, cargo);

        final AdaptadorCargos adaptadorCargos = new AdaptadorCargos(context, R.layout.item_lista, cargos);
        spCargo.setAdapter(adaptadorCargos);

        final AlertDialog dialog = builder.create();

        Button bt = (Button) v.findViewById(R.id.btGuardarUpdateCargo);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cargo cargo = (Cargo) spCargo.getSelectedItem();
                if (!cargo.getNombre().equals(context.getResources().getString(R.string.selectCargo))) {
                    contactoRepresentado.setCargo((Cargo) spCargo.getSelectedItem());
                    update(contactoRepresentado);
                    dialog.dismiss();
                    listaContactosFragment.cargarLista();
                }
            }
        });
        dialog.show();
    }

    private void crearDialogoNombreCompleto(final ContactoRepresentado contactoRepresentado) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View v = inflater.inflate(R.layout.dialogonombrecompleto, null);
        builder.setView(v);

        final EditText etNombre = (EditText) v.findViewById(R.id.etUpdateNombreContacto);
        final EditText etApellidos = (EditText) v.findViewById(R.id.etUpdateApellidosContacto);
        Button btGuardar = (Button) v.findViewById(R.id.btUpdateNombreContacto);
        etNombre.setText(contactoRepresentado.getNombre());
        etApellidos.setText(contactoRepresentado.getApellidos());
        final AlertDialog dialog = builder.create();

        btGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etNombre.getText().toString().equals("")) {
                    etNombre.setError("Debe introducir al menos un nombre para el contacto");
                } else {
                    contactoRepresentado.setNombre(etNombre.getText().toString().toUpperCase());
                    contactoRepresentado.setApellidos(etApellidos.getText().toString().toUpperCase());
                    update(contactoRepresentado);
                    dialog.dismiss();
                    listaContactosFragment.cargarLista();
                }
            }
        });
        dialog.show();
        Util.abrirTeclado(context, etNombre);
    }

    private void crearDialogo(final EditText input, final ContactoRepresentado contactoRepresentado) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        final AlertDialog dialog = builder.create();
        dialog.setTitle(contactoRepresentado.getNombre() + " " + contactoRepresentado.getApellidos());
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (input.getHint().equals(context.getResources().getString(R.string.telefonoContacto))) {
                        if (Integer.parseInt(input.getText().toString()) > 799999999 && Integer.parseInt(input.getText().toString()) < 999999999) {
                            contactoRepresentado.setTelefono(Integer.parseInt(input.getText().toString()));
                            update(contactoRepresentado);
                            dialog.dismiss();
                            listaContactosFragment.cargarLista();
                        } else {
                            input.setError("El nº introducido no se corresponde con un nº fijo");
                        }
                    } else if (input.getHint().equals(context.getResources().getString(R.string.movilContacto))) {
                        if (Integer.parseInt(input.getText().toString()) > 599999999 && Integer.parseInt(input.getText().toString()) < 799999999) {
                            contactoRepresentado.setMovil(Integer.parseInt(input.getText().toString()));
                            update(contactoRepresentado);
                            dialog.dismiss();
                            listaContactosFragment.cargarLista();
                        } else {
                            input.setError("El nº introducido no se corresponde con un nº de movil");
                        }
                    } else if (input.getHint().equals(context.getResources().getString(R.string.emailContacto))) {
                        if (Util.validateEmail(input.getText().toString())) {
                            contactoRepresentado.setEmail(input.getText().toString());
                            update(contactoRepresentado);
                            dialog.dismiss();
                            listaContactosFragment.cargarLista();
                        } else {
                            input.setError("El email introducido no es válido");
                        }
                    }
                } catch (NumberFormatException nfe) {
                    input.setError("Debe introducir un número entero");
                }
            }
        });
    }

    private void tomarFoto(String position) {

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, 0);
        } else {
            Intent intent = new Intent();
            if (intent != null) {
                intent.setAction("android.media.action.IMAGE_CAPTURE");
                try {
                    listaContactosFragment.startActivityForResult(intent, Integer.parseInt(position));
                } catch (Exception e) {

                }
            }
        }
    }

    private void update(ContactoRepresentado contactoRepresentado) {
        try {
            Dao<ContactoRepresentado, Integer> ccDao = OpenHelperManager.getHelper(context, DataBaseHelper.class).getContactoRepresentadoDAO();
            ccDao.update(contactoRepresentado);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        final TextView tv;
        switch (v.getId()) {
            case R.id.tvCaducidadProductoLista:
                tv = (TextView) v;
                Util.llamada(Integer.parseInt(tv.getText().toString()), context, activity);
                break;
            case R.id.tvPrecioProductoLista:
                tv = (TextView) v;
                Util.llamada(Integer.parseInt(tv.getText().toString()), context, activity);
                break;
            case R.id.tvSectorProductoLista:
                tv = (TextView) v;
                ArrayList<String> email = new ArrayList<String>();
                email.add(tv.getText().toString());
                Util.sendEmail(email, context);
                break;
            case R.id.ibInfoProductoLista:
                ContactoRepresentado contactoRepresentado1 = contactos.get(Integer.parseInt(v.getTag().toString()));
                Util.tostar(contactoRepresentado1.getOtros(), context);
                break;
            case R.id.ibEliminarContacto:
                final ContactoRepresentado contactoRepresentado = contactos.get(Integer.parseInt(v.getTag().toString()));
                new AlertDialog.Builder(context)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Eliminar Contacto")
                        .setMessage("¿Confirma que desea eliminar el contacto : " + contactoRepresentado.getNombre() + " " + contactoRepresentado.getApellidos() + " de la base de datos?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                DataBaseHelper helper = OpenHelperManager.getHelper(context, DataBaseHelper.class);
                                try {
                                    helper.getContactoRepresentadoDAO().delete(contactoRepresentado);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                listaContactosFragment.cargarLista();
                            }
                        }).setNegativeButton("No", null)
                        .show();
                ;
                break;
            default:
                break;
        }
    }


    public static class ContactosHolder extends RecyclerView.ViewHolder {
        //definir views
        ImageButton ibFoto, ibEliminar, ibCita;
        TextView tvNombre, tvCargo, tvMovil, tvFijo, tvEmail;
        int width, heigth;

        public ContactosHolder(View itemView) {
            super(itemView);
            //instanciar views
            ibFoto = (ImageButton) itemView.findViewById(R.id.ibFotoProductoLista);
            tvNombre = (TextView) itemView.findViewById(R.id.tvNombreProductoLista);
            tvCargo = (TextView) itemView.findViewById(R.id.tvReferenciaProductoLista);
            tvFijo = (TextView) itemView.findViewById(R.id.tvCaducidadProductoLista);
            tvMovil = (TextView) itemView.findViewById(R.id.tvPrecioProductoLista);
            tvEmail = (TextView) itemView.findViewById(R.id.tvSectorProductoLista);
            ibCita = (ImageButton) itemView.findViewById(R.id.ibInfoProductoLista);
            ibEliminar = (ImageButton) itemView.findViewById(R.id.ibEliminarContacto);
            width = ibFoto.getWidth();
            heigth = ibFoto.getHeight();


        }
    }


}

