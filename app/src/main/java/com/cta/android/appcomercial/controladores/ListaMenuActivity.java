package com.cta.android.appcomercial.controladores;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cta.android.appcomercial.R;
import com.cta.android.appcomercial.adaptadores.AdaptadorCargos;
import com.cta.android.appcomercial.adaptadores.AdaptadorSectores;
import com.cta.android.appcomercial.model.Cargo;
import com.cta.android.appcomercial.model.ContactoCliente;
import com.cta.android.appcomercial.model.ContactoRepresentado;
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

public class ListaMenuActivity extends AppCompatActivity implements View.OnClickListener {

    String titulodialogo;
    ListView lista;
    String menu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        menu = getIntent().getStringExtra("menu");
        setContentView(R.layout.listanormal);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ImageButton ibatras = (ImageButton) findViewById(R.id.ibSalirListaMenu);
        ibatras.setOnClickListener(this);
        ImageButton ibanadir = (ImageButton) findViewById(R.id.ibAnadirOpcionMenu);
        ibanadir.setOnClickListener(this);
        TextView cabecera = (TextView) findViewById(R.id.tvCabeceraListaMenu);
        lista = (ListView) findViewById(R.id.listaMenu);
        cargarLista(menu);

        switch (menu) {
            case "cargos":
                cabecera.setText("Editar Cargos");
                titulodialogo = "Nuevo Cargo";
                break;
            case "sectores":
                cabecera.setText("Editar Sectores");
                titulodialogo = "Nuevo Sector";
                break;
            case "visitas":
                cabecera.setText("Editar Tipos Visita");
                titulodialogo = "Nuevo Tipo Visita";
                break;
            default:
                break;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibSalirListaMenu:
                this.finish();
                break;
            case R.id.ibAnadirOpcionMenu:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(titulodialogo);
                final EditText input = new EditText(this);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!input.getText().toString().equals("")) {
                            anadirOpcion(input.getText().toString());
                            cargarLista(menu);
                        } else {
                            input.setError("Debe introducir un valor de texto");
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
                break;
            default:
                break;
        }
    }

    private void anadirOpcion(String s) {
        DataBaseHelper helper = OpenHelperManager.getHelper(this, DataBaseHelper.class);
        switch (titulodialogo) {
            case "Nuevo Cargo":
                Dao<Cargo, Integer> cargoIntegerDao = null;
                try {
                    cargoIntegerDao = helper.getCargoDAO();
                    Cargo cargo = new Cargo();
                    cargo.setNombre(s.toUpperCase());
                    cargoIntegerDao.create(cargo);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case "Nuevo Sector":
                Dao<Sector, Integer> sectorIntegerDao = null;
                try {
                    sectorIntegerDao = helper.getSectorDAO();
                    Sector sector = new Sector();
                    sector.setNombre(s.toUpperCase());
                    sectorIntegerDao.create(sector);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case "Nuevo Tipo Visita":
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Este control se encuentra desactivado en la aplicación", Toast.LENGTH_SHORT).show();
    }

    private void cargarLista(final String menu) {
        final DataBaseHelper helper = OpenHelperManager.getHelper(this, DataBaseHelper.class);
        switch (menu) {
            case "cargos":
                final RuntimeExceptionDao<Cargo, Integer> runtimeExceptionDao = helper.getCargoRuntimeDAO();
                ArrayList<Cargo> cargos = (ArrayList<Cargo>) runtimeExceptionDao.queryForAll();
                Collections.sort(cargos);
                final AdaptadorCargos adaptadorCargos = new AdaptadorCargos(this, R.layout.item_lista, cargos);
                lista.setAdapter(adaptadorCargos);
                lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ListaMenuActivity.this);
                        builder.setTitle("¿Desea eliminar el Cargo: " + adaptadorCargos.getItem(position).getNombre() + "?");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                RuntimeExceptionDao<ContactoCliente, Integer> contactoClienteDao = helper.getContactoClienteRuntimeDAO();
                                final List<ContactoCliente> cclientes = contactoClienteDao.queryForEq("cargo_id", adaptadorCargos.getItem(position).getId());
                                RuntimeExceptionDao<ContactoRepresentado, Integer> contactoRepresentadoDao = helper.getContactoRepresentadoRuntimeDAO();
                                final List<ContactoRepresentado> crepresentado = contactoRepresentadoDao.queryForEq("cargo_id", adaptadorCargos.getItem(position).getId());
                                if (cclientes.size() == 0 && crepresentado.size() == 0) {
                                    runtimeExceptionDao.delete(adaptadorCargos.getItem(position));
                                    cargarLista(menu);
                                } else {
                                    dialog.cancel();
                                    AlertDialog.Builder builder2 = new AlertDialog.Builder(ListaMenuActivity.this);
                                    builder2.setTitle("Existen contactos con ese cargo. ¿Desea continuar?");
                                    builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog2, int which) {
                                            RuntimeExceptionDao<ContactoCliente, Integer> ccDao = helper.getContactoClienteRuntimeDAO();
                                            for (ContactoCliente contactoCliente : cclientes) {
                                                contactoCliente.setCargo(null);
                                                ccDao.update(contactoCliente);
                                            }
                                            RuntimeExceptionDao<ContactoRepresentado, Integer> crDao = helper.getContactoRepresentadoRuntimeDAO();
                                            for (ContactoRepresentado contactoRepresentado : crepresentado) {
                                                contactoRepresentado.setCargo(null);
                                                crDao.update(contactoRepresentado);
                                            }
                                            runtimeExceptionDao.delete(adaptadorCargos.getItem(position));
                                            cargarLista(menu);
                                        }
                                    });
                                    builder2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog2, int which) {
                                            dialog2.cancel();
                                        }
                                    });
                                    builder2.show();
                                }
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                        return false;
                    }
                });
                break;
            case "sectores":
                final RuntimeExceptionDao<Sector, Integer> runtimeExceptionDao1 = helper.getSectorRuntimeDAO();
                ArrayList<Sector> sectors = (ArrayList<Sector>) runtimeExceptionDao1.queryForAll();
                Collections.sort(sectors);
                final AdaptadorSectores adaptadorSectors = new AdaptadorSectores(this, R.layout.item_lista, sectors);
                lista.setAdapter(adaptadorSectors);
                lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ListaMenuActivity.this);
                        builder.setTitle("¿Desea eliminar el Sector: " + adaptadorSectors.getItem(position).getNombre() + "?");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                runtimeExceptionDao1.delete(adaptadorSectors.getItem(position));
                                cargarLista(menu);
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                        return false;
                    }
                });
                break;
            case "visitas":

                break;
            default:
                break;
        }

    }
}
