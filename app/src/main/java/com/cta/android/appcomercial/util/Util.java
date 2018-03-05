package com.cta.android.appcomercial.util;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by PC on 02/12/2017.
 */

public class Util {

    public static boolean validarCIF(String id) {
        boolean resultado = false;
        int digito_de_control;

        String letras_validas = "ABCDEFGHJPQRSUV";
        String tipo_de_letra = "PQS";
        String caracteres_de_control = "JABCDEFGHI";
        String tipo_de_nombre = "ABEH";

        try {
            /* Un CIF tiene que tener nueve dígitos */
            if (id.length() == 9) {

				/* Toma la primera letra del CIF */
                char letra_CIF = id.charAt(0);

				/* Comprueba si la primera letra del CIF es válida */
                if (letras_validas.indexOf(letra_CIF) >= 0) {

                    if (Character.isDigit(id.charAt(8))) {
                        digito_de_control = Character.getNumericValue(id
                                .charAt(8));
                        if (tipo_de_letra.indexOf(letra_CIF) >= 0)
                            digito_de_control = 100;
                    } else {
                        digito_de_control = caracteres_de_control.indexOf(id
                                .charAt(8));
                        if (tipo_de_nombre.indexOf(letra_CIF) >= 0)
                            digito_de_control = 100;
                    }

                    int a = 0, b = 0, c = 0;
                    byte[] impares = {0, 2, 4, 6, 8, 1, 3, 5, 7, 9};

					/* Calcula A y B. */
                    for (int t = 1; t <= 6; t = t + 2) {

						/* Suma los pares */
                        a = a + Character.getNumericValue(id.charAt(t + 1));
                        b = b
                                + impares[Character.getNumericValue(id
                                .charAt(t))];
                    }

                    b = b + impares[Character.getNumericValue(id.charAt(7))];
                    /* Calcula C */
                    c = 10 - ((a + b) % 10);
                    /* Compara C con los dígitos de control */
                    resultado = (c == digito_de_control);
                }
            }
        } catch (Exception e) {
            resultado = false;
        }
        return resultado;
    }

    /**
     * Validate given email with regular expression.
     *
     * @param email email for validation
     * @return true valid email, otherwise false
     */
    public static boolean validateEmail(String email) {
        String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";


        // Compiles the given regular expression into a pattern.
        Pattern pattern = Pattern.compile(PATTERN_EMAIL);

        // Match the given input against this pattern
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();

    }

    public static boolean validarTelefono(int telefono) {
        if (telefono > 600000000 && telefono < 999999999) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean validarCP(int CP) {
        if (CP > 1000 && CP < 53000) {
            return true;
        } else {
            return false;
        }
    }

    public static void tostar(String mensaje, Context context) {
        Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
    }
    /*
    public static void cerrarTeclado(Context context, Activity activity) {
        InputMethodManager inputManager = (InputMethodManager)
                context.getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

    }
    */

    public static void abrirTeclado(Context context, View v) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
    }

    public static void llamada(int telefono, Context context, Activity activity) {
        Intent intent = new Intent(Intent.ACTION_CALL);

        intent.setData(Uri.parse("tel:" + telefono));
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, 0);

        } else {
            context.startActivity(intent);
        }
    }

    public static void sendEmail(ArrayList<String> direcciones, Context context) {

        String[] TO = {direcciones.get(0)};
        direcciones.remove(0);
        String[] CC = null;
        if (direcciones.size() > 0) {
            CC = new String[direcciones.size()];
            for (int x = 0; x < CC.length; x++) {
                CC[x] = direcciones.get(x);
            }
        }
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");


        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "");

        try {
            context.startActivity(Intent.createChooser(emailIntent, "Enviar correo"));

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public static void verWeb(String url, Context context) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        context.startActivity(i);
    }

    public static void abrirGPS(String direccion, Context context) {
        String uri = String.format(Locale.ENGLISH, "geo:0,0?q=" + direccion);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        context.startActivity(intent);
    }

    public static ArrayList<String[]> readCalendarEvent(Context context, Calendar dia) {
        Calendar s = Calendar.getInstance();
        s.set(dia.get(Calendar.YEAR), dia.get(Calendar.MONTH), dia.get(Calendar.DATE), 0, 0, 0);
        s.add(Calendar.SECOND, -1);


        Calendar e = Calendar.getInstance();
        e.set(dia.get(Calendar.YEAR), dia.get(Calendar.MONTH), dia.get(Calendar.DATE), 0, 0, 0);
        e.add(Calendar.DATE, 1);
        e.add(Calendar.SECOND, -1);
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = context.getContentResolver()
                .query(
                        Uri.parse("content://com.android.calendar/events"),
                        new String[]{"calendar_id", "title", "description",
                                "dtstart", "dtend", "eventLocation", "_id"}, "dtstart >= ? AND dtstart <= ? AND deleted = ?",
                        new String[]{"" + s.getTimeInMillis(), "" + e.getTimeInMillis(), "0"}, "dtstart ASC");
        cursor.moveToFirst();

        ArrayList<String[]> listaEventos = new ArrayList<String[]>();

        for (int i = 0; i < cursor.getCount(); i++) {
            String[] array = new String[6];
            // nombre del evento
            array[0] = cursor.getString(1);
            array[1] = cursor.getString(2);
            array[2] = cursor.getString(5);
            array[3] = cursor.getString(3);
            array[4] = cursor.getString(4);
            array[5] = cursor.getString(6);
            listaEventos.add(array);
            cursor.moveToNext();

        }
        return listaEventos;
    }

    public static String getDate(long milliSeconds) {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "dd/MM/yyyy hh:mm:ss a");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}
