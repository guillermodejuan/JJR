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
import com.cta.android.appcomercial.model.ContactoCliente;
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

public class AdaptadorListaContactosCliente extends RecyclerView.Adapter<AdaptadorListaContactosCliente.ContactosHolder> implements View.OnLongClickListener, View.OnClickListener {

    Context context;
    Activity activity;
    ListaContactosFragment listaContactosFragment;
    private List<ContactoCliente> contactos;

    public AdaptadorListaContactosCliente(List<ContactoCliente> contactos, ListaContactosFragment listaContactosFragment) {
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
        final ContactoCliente contactoCliente = contactos.get(position);

        DataBaseHelper helper = OpenHelperManager.getHelper(context, DataBaseHelper.class);
        holder.ibFoto.setImageResource(R.drawable.tvborder);
        holder.ibFoto.setTag(position);
        holder.ibFoto.setOnLongClickListener(this);
        holder.tvNombre.setText(contactoCliente.getNombre() + " " + contactoCliente.getApellidos());
        holder.tvNombre.setTag(position);
        holder.tvNombre.setOnLongClickListener(this);
        activity.registerForContextMenu(holder.tvNombre);

        if (contactoCliente.getCargo() != null) {
            RuntimeExceptionDao<Cargo, Integer> cargoIntegerRuntimeExceptionDao = helper.getCargoRuntimeDAO();
            Cargo cargo = cargoIntegerRuntimeExceptionDao.queryForId(contactoCliente.getCargo().getId());
            holder.tvCargo.setText(cargo.getNombre());
        } else {
            holder.tvCargo.setText("Cargo ...");
        }
        holder.tvCargo.setOnLongClickListener(this);
        holder.tvCargo.setTag(position);

        if (contactoCliente.getTelefono() != 0) {
            holder.tvFijo.setText(String.valueOf(contactoCliente.getTelefono()));
            holder.tvFijo.setOnClickListener(this);
        } else
            holder.tvFijo.setText("Teléfono ...");
        holder.tvFijo.setOnLongClickListener(this);
        holder.tvFijo.setTag(position);
        if (contactoCliente.getMovil() != 0) {
            holder.tvMovil.setText(String.valueOf(contactoCliente.getMovil()));
            holder.tvMovil.setOnClickListener(this);
        } else
            holder.tvMovil.setText("Móvil ...");
        holder.tvMovil.setOnLongClickListener(this);
        holder.tvMovil.setTag(position);
        if (!contactoCliente.getEmail().equals("")) {
            holder.tvEmail.setText(contactoCliente.getEmail());
            holder.tvEmail.setOnClickListener(this);
        } else
            holder.tvEmail.setText("Email ...");
        holder.tvEmail.setOnLongClickListener(this);
        holder.tvEmail.setTag(position);
        if (contactoCliente.getFoto() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(contactoCliente.getFoto(), 0, contactoCliente.getFoto().length);
            holder.ibFoto.setImageBitmap(bitmap);
        }

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
        final ContactoCliente contactoCliente = (ContactoCliente) contactos.get(Integer.parseInt(v.getTag().toString()));

        final EditText input = new EditText(context);
        switch (v.getId()) {
            case R.id.tvNombreProductoLista:
                crearDialogoNombreCompleto(contactoCliente);
                break;
            case R.id.tvReferenciaProductoLista:
                crearDialogoCargo(contactoCliente);
                break;
            case R.id.tvCaducidadProductoLista:
                input.setHint(context.getResources().getString(R.string.telefonoContacto));
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                crearDialogo(input, contactoCliente);
                break;
            case R.id.tvPrecioProductoLista:
                input.setHint(context.getResources().getString(R.string.movilContacto));
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                crearDialogo(input, contactoCliente);
                break;
            case R.id.tvSectorProductoLista:
                input.setHint(context.getResources().getString(R.string.emailContacto));
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                crearDialogo(input, contactoCliente);
                break;
            case R.id.ibFotoProductoLista:
                tomarFoto(v.getTag().toString());
                break;
            default:
                break;
        }
        return false;
    }

    private void crearDialogoCargo(final ContactoCliente contactoCliente) {
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
                    contactoCliente.setCargo((Cargo) spCargo.getSelectedItem());
                    update(contactoCliente);
                    dialog.dismiss();
                    listaContactosFragment.cargarLista();
                }
            }
        });
        dialog.show();
    }

    private void crearDialogoNombreCompleto(final ContactoCliente contactoCliente) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View v = inflater.inflate(R.layout.dialogonombrecompleto, null);
        builder.setView(v);

        final EditText etNombre = (EditText) v.findViewById(R.id.etUpdateNombreContacto);
        final EditText etApellidos = (EditText) v.findViewById(R.id.etUpdateApellidosContacto);
        Button btGuardar = (Button) v.findViewById(R.id.btUpdateNombreContacto);
        etNombre.setText(contactoCliente.getNombre());
        etApellidos.setText(contactoCliente.getApellidos());
        final AlertDialog dialog = builder.create();

        btGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etNombre.getText().toString().equals("")) {
                    etNombre.setError("Debe introducir al menos un nombre para el contacto");
                } else {
                    contactoCliente.setNombre(etNombre.getText().toString().toUpperCase());
                    contactoCliente.setApellidos(etApellidos.getText().toString().toUpperCase());
                    update(contactoCliente);
                    dialog.dismiss();
                    listaContactosFragment.cargarLista();
                }
            }
        });
        dialog.show();
        Util.abrirTeclado(context, etNombre);
    }

    private void crearDialogo(final EditText input, final ContactoCliente contactoCliente) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        final AlertDialog dialog = builder.create();
        dialog.setTitle(contactoCliente.getNombre() + " " + contactoCliente.getApellidos());
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (input.getHint().equals(context.getResources().getString(R.string.telefonoContacto))) {
                        if (Integer.parseInt(input.getText().toString()) > 799999999 && Integer.parseInt(input.getText().toString()) < 999999999) {
                            contactoCliente.setTelefono(Integer.parseInt(input.getText().toString()));
                            update(contactoCliente);
                            dialog.dismiss();
                            listaContactosFragment.cargarLista();

                        } else {
                            input.setError("El nº introducido no se corresponde con un nº fijo");
                        }
                    } else if (input.getHint().equals(context.getResources().getString(R.string.movilContacto))) {
                        if (Integer.parseInt(input.getText().toString()) > 599999999 && Integer.parseInt(input.getText().toString()) < 799999999) {
                            contactoCliente.setMovil(Integer.parseInt(input.getText().toString()));
                            update(contactoCliente);
                            dialog.dismiss();
                            listaContactosFragment.cargarLista();
                        } else {
                            input.setError("El nº introducido no se corresponde con un nº de movil");
                        }
                    } else if (input.getHint().equals(context.getResources().getString(R.string.emailContacto))) {
                        if (Util.validateEmail(input.getText().toString())) {
                            contactoCliente.setEmail(input.getText().toString());
                            update(contactoCliente);
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

    private void update(ContactoCliente contactoCliente) {
        try {
            Dao<ContactoCliente, Integer> ccDao = OpenHelperManager.getHelper(context, DataBaseHelper.class).getContactoClienteDAO();
            ccDao.update(contactoCliente);
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
                ContactoCliente contactoCliente1 = contactos.get(Integer.parseInt(v.getTag().toString()));
                Util.tostar(contactoCliente1.getOtros(), context);
                break;
            case R.id.ibEliminarContacto:
                final ContactoCliente contactoCliente = contactos.get(Integer.parseInt(v.getTag().toString()));
                new AlertDialog.Builder(context)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Eliminar Contacto")
                        .setMessage("¿Confirma que desea eliminar el contacto : " + contactoCliente.getNombre() + " " + contactoCliente.getApellidos() + " de la base de datos?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                DataBaseHelper helper = OpenHelperManager.getHelper(context, DataBaseHelper.class);
                                try {
                                    helper.getContactoClienteDAO().delete(contactoCliente);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                listaContactosFragment.cargarLista();
                            }
                        }).setNegativeButton("No", null)
                        .show();

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
