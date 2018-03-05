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
import com.cta.android.appcomercial.controladores.ListaProductosFragment;
import com.cta.android.appcomercial.model.ContactoCliente;
import com.cta.android.appcomercial.model.LineaOferta;
import com.cta.android.appcomercial.model.Producto;
import com.cta.android.appcomercial.model.Sector;
import com.cta.android.appcomercial.util.DataBaseHelper;
import com.cta.android.appcomercial.util.Util;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC on 19/01/2018.
 */

public class AdaptadorListaProductos extends RecyclerView.Adapter<AdaptadorListaProductos.ProductosHolder> implements View.OnLongClickListener, View.OnClickListener {

    Context context;
    Activity activity;
    ListaProductosFragment listaProductosFragment;
    private List<Producto> productos;

    public AdaptadorListaProductos(List<Producto> productos, ListaProductosFragment listaProductosFragment) {
        this.productos = productos;
        this.context = listaProductosFragment.getContext();
        this.activity = listaProductosFragment.getActivity();
        this.listaProductosFragment = listaProductosFragment;
    }

    @Override
    public AdaptadorListaProductos.ProductosHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final ViewGroup p = parent;
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_prouctos, parent, false);
        AdaptadorListaProductos.ProductosHolder holder = new AdaptadorListaProductos.ProductosHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(AdaptadorListaProductos.ProductosHolder holder, int position) {
        final Producto producto = productos.get(position);

        DataBaseHelper helper = OpenHelperManager.getHelper(context, DataBaseHelper.class);
        holder.ibFoto.setImageResource(R.drawable.tvborder);
        holder.ibFoto.setTag(position);
        holder.ibFoto.setOnLongClickListener(this);
        holder.tvNombre.setText(producto.getDescripcion());
        holder.tvNombre.setTag(position);
        holder.tvNombre.setOnLongClickListener(this);
        activity.registerForContextMenu(holder.tvNombre);

        if (producto.getSector() != null) {
            RuntimeExceptionDao<Sector, Integer> runtimeExceptionDao = helper.getSectorRuntimeDAO();
            Sector sector = runtimeExceptionDao.queryForId(producto.getSector().getId());
            holder.tvSector.setText(sector.getNombre());
        } else {
            holder.tvSector.setText("Sector ...");
        }
        holder.tvSector.setOnLongClickListener(this);
        holder.tvSector.setTag(position);

        if (producto.getPrecio() != 0) {
            holder.tvPrecio.setText(String.valueOf(producto.getPrecio()));
            holder.tvPrecio.setOnClickListener(this);
        } else
            holder.tvPrecio.setText("Precio ...");
        holder.tvPrecio.setOnLongClickListener(this);
        holder.tvPrecio.setTag(position);

        if (!producto.getReferencia().equals("")) {
            holder.tvReferencia.setText(producto.getReferencia());
            holder.tvReferencia.setOnClickListener(this);
        } else
            holder.tvReferencia.setText("Referencia ...");
        holder.tvReferencia.setOnLongClickListener(this);
        holder.tvReferencia.setTag(position);

        Bitmap bitmap = BitmapFactory.decodeByteArray(producto.getImageBytes(), 0, producto.getImageBytes().length);
        holder.ibFoto.setImageBitmap(bitmap);

        holder.ibEliminar.setTag(position);
        holder.ibEliminar.setOnClickListener(this);
        holder.ibInfo.setTag(position);
        holder.ibInfo.setOnClickListener(this);

    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    @Override
    public boolean onLongClick(View v) {
        final Producto producto = (Producto) productos.get(Integer.parseInt(v.getTag().toString()));

        final EditText input = new EditText(context);
        switch (v.getId()) {
            case R.id.tvNombreProductoLista:
                input.setHint("Nombre ...");
                crearDialogo(input, producto);
                break;
            case R.id.tvReferenciaProductoLista:
                input.setHint("Referencia ...");
                crearDialogo(input, producto);
                break;
            case R.id.tvPrecioProductoLista:
                input.setHint("Precio ...");
                input.setInputType(InputType.TYPE_CLASS_PHONE);
                crearDialogo(input, producto);
                break;
            case R.id.tvSectorProductoLista:
                crearDialogoSector(producto);
                break;
            case R.id.ibFotoProductoLista:
                tomarFoto(v.getTag().toString());
                break;
            default:
                break;
        }
        return false;
    }

    private void crearDialogoSector(final Producto producto) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View v = inflater.inflate(R.layout.dialogosector, null);
        builder.setView(v);

        final Spinner spSector = (Spinner) v.findViewById(R.id.spUpdateSector);
        DataBaseHelper helper = OpenHelperManager.getHelper(context, DataBaseHelper.class);
        List<Sector> sectores = null;
        try {
            Dao<Sector, Integer> daoSector = helper.getSectorDAO();
            sectores = daoSector.queryBuilder().orderBy("nombre", true).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Sector sector = new Sector();
        sector.setNombre("Seleccione Sector ...");
        sectores.add(0, sector);

        final AdaptadorSectores adaptadorSectores = new AdaptadorSectores(context, R.layout.item_lista, sectores);
        spSector.setAdapter(adaptadorSectores);

        final AlertDialog dialog = builder.create();

        Button bt = (Button) v.findViewById(R.id.btGuardarUpdateSector);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sector sector = (Sector) spSector.getSelectedItem();
                if (!sector.getNombre().equals("Seleccione Sector ...")) {
                    producto.setSector((Sector) spSector.getSelectedItem());
                    update(producto);
                    dialog.dismiss();
                    listaProductosFragment.cargarLista();
                }
            }
        });
        dialog.show();
    }


    private void crearDialogo(final EditText input, final Producto producto) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        final AlertDialog dialog = builder.create();
        dialog.setTitle(producto.getDescripcion());
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (input.getHint().equals("Nombre ...")) {
                        if (!input.getText().equals("")) {
                            producto.setDescripcion(input.getText().toString());
                            update(producto);
                            dialog.dismiss();
                            listaProductosFragment.cargarLista();

                        }

                    } else if (input.getHint().equals("Referencia ...")) {
                        if (!input.getText().equals("")) {
                            producto.setReferencia(input.getText().toString());
                            update(producto);
                            dialog.dismiss();
                            listaProductosFragment.cargarLista();

                        }

                    } else if (input.getHint().equals("Precio ...")) {
                        if (!input.getText().equals("")) {
                            try {
                                producto.setPrecio(Float.parseFloat(input.getText().toString()));
                                update(producto);
                                dialog.dismiss();
                                listaProductosFragment.cargarLista();
                            } catch (NumberFormatException nfe) {
                                input.setError("Debe introducir un valor numérico entero o decimal");
                            }

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
                    listaProductosFragment.startActivityForResult(intent, Integer.parseInt(position));
                } catch (Exception e) {

                }
            }
        }
    }

    private void update(Producto producto) {
        try {
            Dao<Producto, Integer> dao = OpenHelperManager.getHelper(context, DataBaseHelper.class).getProductoDao();
            dao.update(producto);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        final TextView tv;
        switch (v.getId()) {
            case R.id.ibInfoProductoLista:
                Producto producto1 = productos.get(Integer.parseInt(v.getTag().toString()));
                Util.tostar(producto1.getInfo(), context);
                break;
            case R.id.ibEliminarProducto:
                final Producto producto2 = productos.get(Integer.parseInt(v.getTag().toString()));
                new AlertDialog.Builder(context)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Eliminar Producto")
                        .setMessage("¿Confirma que desea eliminar el producto : " + producto2.getDescripcion() + " de la base de datos?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                DataBaseHelper helper = OpenHelperManager.getHelper(context, DataBaseHelper.class);
                                try {
                                    RuntimeExceptionDao<LineaOferta,Integer> lineaOfertaIntegerRuntimeExceptionDao = helper.getLineaOfertaIntegerRuntimeExceptionDao();
                                    List<LineaOferta> lineas = lineaOfertaIntegerRuntimeExceptionDao.queryForEq("producto_id", producto2.getId());
                                    if (lineas.size() == 0) {
                                        helper.getProductoDao().delete(producto2);
                                    } else {
                                        Util.tostar("No se ha podido eliminar el producto, existen ofertas asociadas a él",context);
                                    }
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                listaProductosFragment.cargarLista();
                            }
                        }).setNegativeButton("No", null)
                        .show();

                break;
            default:
                break;
        }
    }


    public static class ProductosHolder extends RecyclerView.ViewHolder {
        //definir views
        ImageButton ibFoto, ibEliminar, ibInfo;
        TextView tvNombre, tvReferencia, tvPrecio, tvSector;
        int width, heigth;

        public ProductosHolder(View itemView) {
            super(itemView);
            //instanciar views
            ibFoto = (ImageButton) itemView.findViewById(R.id.ibFotoProductoLista);
            tvNombre = (TextView) itemView.findViewById(R.id.tvNombreProductoLista);
            tvSector = (TextView) itemView.findViewById(R.id.tvSectorProductoLista);
            tvPrecio = (TextView) itemView.findViewById(R.id.tvPrecioProductoLista);
            tvReferencia = (TextView) itemView.findViewById(R.id.tvReferenciaProductoLista);
            ibInfo = (ImageButton) itemView.findViewById(R.id.ibInfoProductoLista);
            ibEliminar = (ImageButton) itemView.findViewById(R.id.ibEliminarProducto);
            width = ibFoto.getWidth();
            heigth = ibFoto.getHeight();


        }
    }
}
