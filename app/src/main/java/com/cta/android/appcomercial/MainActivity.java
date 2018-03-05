package com.cta.android.appcomercial;


import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.cta.android.appcomercial.controladores.EmpresasFragment;
import com.cta.android.appcomercial.controladores.ListaMenuActivity;
import com.cta.android.appcomercial.controladores.OfertasFragment;
import com.cta.android.appcomercial.controladores.VisitasFragment;
import com.cta.android.appcomercial.model.Cargo;
import com.cta.android.appcomercial.model.Cliente;
import com.cta.android.appcomercial.model.ComunidadAutonoma;
import com.cta.android.appcomercial.model.ContactoCliente;
import com.cta.android.appcomercial.model.Localidad;
import com.cta.android.appcomercial.model.Provincia;
import com.cta.android.appcomercial.model.Sector;
import com.cta.android.appcomercial.util.DataBaseHelper;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Adaptador adaptador = null;
    boolean permisos = false;
    boolean primeraejecucion = false;
    ViewPager vp = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            primeraejecucion = true;
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALENDAR}, 0);
        } else {
            inserts();
            setContentView(R.layout.activity_main);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            vp = (ViewPager) findViewById(R.id.vp);

            android.support.v4.app.FragmentManager fManager = getSupportFragmentManager();
            List<Fragment> fragmentos = new ArrayList<Fragment>();
            fragmentos.add(EmpresasFragment.newInstance());
            fragmentos.add(VisitasFragment.newInstance("VISITAS"));
            fragmentos.add(OfertasFragment.newInstance("OFERTAS Y PEDIDOS"));
            adaptador = new Adaptador(fManager, fragmentos);
            vp.setAdapter(adaptador);

        }

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Este control se encuentra desactivado en la aplicación", Toast.LENGTH_SHORT).show();
    }

    private void inserts() {
        DataBaseHelper helper = OpenHelperManager.getHelper(this, DataBaseHelper.class);
        try {
            Dao<ComunidadAutonoma, Integer> comunidadAutonomaDAO = helper.getComunidadAutonomaDAO();
            if (comunidadAutonomaDAO.queryForAll().size() == 0) {
                comunidadAutonomaDAO.executeRaw(IOUtils.toString(getResources().openRawResource(R.raw.insertscom), StandardCharsets.UTF_8));
                Dao<Provincia, Integer> provinciaDAO = helper.getProvinciaDAO();
                provinciaDAO.executeRaw(IOUtils.toString(getResources().openRawResource(R.raw.insertsprov), StandardCharsets.UTF_8));
                Dao<Localidad, Integer> localidadDAO = helper.getLocalidadDAO();
                localidadDAO.executeRaw(IOUtils.toString(getResources().openRawResource(R.raw.insertsloc), StandardCharsets.UTF_8));
                Dao<Cliente, Integer> clienteDAO = helper.getClienteDAO();
                clienteDAO.executeRaw(IOUtils.toString(getResources().openRawResource(R.raw.insertscli), StandardCharsets.UTF_8));
                Dao<ContactoCliente, Integer> contactoClienteDAO = helper.getContactoClienteDAO();
                contactoClienteDAO.executeRaw(IOUtils.toString(getResources().openRawResource(R.raw.insertscontactos), StandardCharsets.UTF_8));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Dao<Cargo, Integer> daoCargo = helper.getCargoDAO();
            if (daoCargo.queryForAll().size() == 0) {
                String[] cargos = {"COMPRAS", "DTOR TECNICO", "PRODUCCION", "GERENTE", "INGENIERIA", "OF TECNICA", "PROYECTOS", "COMERCIAL", "TECNICO", "DESARROLLO"
                , "DTOR COMERCIAL", "MANTENIMIENTO", "CALIDAD" };
                for (String c : cargos) {
                    Cargo cargo = new Cargo();
                    cargo.setNombre(c);
                    daoCargo.create(cargo);
                }
            }

            Dao<Sector, Integer> daoSector = helper.getSectorDAO();
            if (daoSector.queryForAll().size() == 0) {
                String[] sectores = {"AUTOMOCION","AGRICOLA","ELECTRODOMESTICO","RENOVABLES","FERROCARRIL","VALVULERIA"};
                for (String c : sectores) {
                    Sector sector = new Sector();
                    sector.setNombre(c);
                    daoSector.create(sector);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED && primeraejecucion) {
            inserts();
            setContentView(R.layout.activity_main);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            ViewPager vp = (ViewPager) findViewById(R.id.vp);

            android.support.v4.app.FragmentManager fManager = getSupportFragmentManager();
            List<Fragment> fragmentos = new ArrayList<Fragment>();
            fragmentos.add(EmpresasFragment.newInstance());
            fragmentos.add(VisitasFragment.newInstance("VISITAS"));
            fragmentos.add(OfertasFragment.newInstance("OFERTAS Y PEDIDOS"));
            adaptador = new Adaptador(fManager, fragmentos);
            vp.setAdapter(adaptador);

            primeraejecucion = false;
        } else if (primeraejecucion) {
            this.finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menumain, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(this, ListaMenuActivity.class);
        switch (item.getItemId()) {
            case R.id.itemcargos:
                myIntent.putExtra("menu", "cargos");
                break;
            case R.id.itemsectores:
                myIntent.putExtra("menu", "sectores");
                break;
            default:
                break;
        }
        startActivity(myIntent);
        return true;
    }

    public class Adaptador extends FragmentPagerAdapter {

        private List<Fragment> fragments;

        public Adaptador(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return this.fragments.get(position);
        }

        @Override
        public int getCount() {
            return this.fragments.size();
        }
    }
}
