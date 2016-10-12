package ccv.checkhelzio.registrocucshbelenes.ui;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ccv.checkhelzio.registrocucshbelenes.R;
import ccv.checkhelzio.registrocucshbelenes.transitions.ChangeBoundBackground2;
import ccv.checkhelzio.registrocucshbelenes.transitions.FabTransition;

public class DialogInfoEventosHelzio extends AppCompatActivity {

    @BindView(R.id.fondo)
    RelativeLayout fondo;
    @BindView(R.id.titulo)
    TextView tv_titulo;
    @BindView(R.id.tv_auditorios)
    TextView tv_auditorios;
    @BindView(R.id.tv_tipo_actividad)
    TextView tv_tipoActividad;
    @BindView(R.id.tv_horario)
    TextView tv_horario;
    @BindView(R.id.tv_fecha)
    TextView tv_fecha;
    @BindView(R.id.tv_repeticion)
    TextView tv_repeticion;
    @BindView(R.id.tv_nombre_organizador)
    TextView tv_organizador;
    @BindView(R.id.tv_num_tel)
    TextView tv_num_tel;
    @BindView(R.id.tv_notas)
    TextView tv_nota;
    @BindView(R.id.tv_id)
    TextView tv_id;
    @BindView(R.id.marca_agua)
    TextView tv_marca_agua;
    @BindView(R.id.et_pin)
    EditText et_pin;
    @BindView(R.id.reveal)
    View reveal;
    @BindView(R.id.iv_delete)
    ImageView iv_delete;
    @BindView(R.id.snackposs)
    CoordinatorLayout snackposs;
    @BindView(R.id.fab_edit)
    ImageButton fab_edit;

    private final String auditorio1 = "Auditorio Salvador Allende";
    private final String auditorio2 = "Auditorio Silvano Barba";
    private final String auditorio3 = "Auditorio Carlos Ramírez";
    private final String auditorio4 = "Auditorio Adalberto Navarro";
    private final String auditorio5 = "Sala de Juicios Orales Mariano Otero";
    private Boolean pin_correcto_eliminar = false;
    private String st_quien = "";
    private String alerta = "";
    private Eventos evento;
    private String st_evento_para_guardar = "";
    private String st_eventos_guardados = "";
    private String datos = "";
    private Handler handler;
    private boolean registroCorrecto = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_info_evento);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ButterKnife.bind(this);
        postponeEnterTransition();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        evento = (Eventos) getIntent().getSerializableExtra("EVENTO");
        tv_titulo.setText(evento.getTitulo_evento());
        tv_auditorios.setText(nombreAuditorio(evento.getAuditorio()));
        tv_tipoActividad.setText(evento.getTipo_evento());
        tv_horario.setText(evento.getHorario());
        tv_fecha.setText(fecha(evento.getFecha_inicial(), evento.getFecha_final()));
        tv_repeticion.setText(stringRepeticion(evento.getRepeticion()));
        tv_organizador.setText(evento.getNombre_organizador());
        tv_num_tel.setText(evento.getNumero_tel().equals("") ? "Sin número telefónico" : evento.getNumero_tel());
        tv_nota.setText(evento.getNotas());
        tv_id.setText(evento.getId());
        tv_marca_agua.setText(REX(evento.getQuien_registro() + " - " + evento.getCuando_registro().split(getString(R.string.separador_repeticion))[0] + " a las " + evento.getCuando_registro().split(getString(R.string.separador_repeticion))[1]));
        fondo.setBackgroundColor(evento.getFondo());
        handler = new Handler();

        et_pin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                switch (et_pin.getText().toString()) {
                    case "1308":
                        pin_correcto_eliminar = true;
                        et_pin.setTextColor(Color.WHITE);
                        st_quien = "Susy";
                        break;
                    case "2886":
                        pin_correcto_eliminar = true;
                        et_pin.setTextColor(Color.WHITE);
                        st_quien = "Tere";
                        break;
                    case "4343":
                        pin_correcto_eliminar = true;
                        et_pin.setTextColor(Color.WHITE);
                        st_quien = "CTA";
                        break;
                    default:
                        pin_correcto_eliminar = false;
                        et_pin.setTextColor(Color.RED);
                        st_quien = "";
                        break;
                }
            }
        });

        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pin_correcto_eliminar) {
                    if (Eliminar(evento.getFecha_inicial(), evento.getFecha_final(), evento.getHora_inicial(), evento.getHora_final(), evento.getRepeticion())) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DialogInfoEventosHelzio.this);
                        alertDialogBuilder.setMessage("\n" + (alerta.equals("") ? "¿Deseas eliminar este evento?" : alerta));
                        alertDialogBuilder.setPositiveButton("ACEPTAR",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        new EliminarEventoBaseDatos().execute();
                                    }
                                });
                        alertDialogBuilder.setNegativeButton("CANCELAR",
                                null);

                        alertDialogBuilder.create().show();
                    } else {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DialogInfoEventosHelzio.this);
                        alertDialogBuilder.setMessage("\n" + (alerta.equals("") ? "Este evento ya ha finalizado, no se puede eliminar" : alerta));
                        alertDialogBuilder.setPositiveButton("ACEPTAR", null);

                        alertDialogBuilder.create().show();
                    }
                } else {
                    Snackbar snack = Snackbar.make(snackposs, "La contraseña que ingresaste es incorrecta", Snackbar.LENGTH_LONG);
                    snack.show();
                }
            }
        });

        fondo.postDelayed(new Runnable() {
                              @Override
                              public void run() {
                                  final Rect endBounds = new Rect(fondo.getLeft(), fondo.getTop(), fondo.getRight(), fondo.getBottom());
                                  ChangeBoundBackground2.setup(DialogInfoEventosHelzio.this, fondo, true, endBounds, getViewBitmap(fondo));
                                  getWindow().getSharedElementEnterTransition();
                                  startPostponedEnterTransition();
                              }
                          }

                , 30);

        Log.v("TAG", evento.getTag());
    }

    private boolean Eliminar(String fecha_inicial, String fecha_final, String hora_inicial, String hora_final, String repeticion) {
        Boolean eliminar = false;
        //CALENDARIO CON LA FECHA DE HOY
        Calendar c = Calendar.getInstance();

        //CALENDARIO CON LA FECHA FINAL Y HORA FINAL DEL EVENTO
        Calendar e = Calendar.getInstance();
        e.set(2016, 0, 1);
        e.set(Calendar.DAY_OF_YEAR, Integer.parseInt(fecha_final));
        horasaCalendario(e, hora_final);

        //SI LA FECHA FINAL Y HORA FINAL DEL EVENTO EN ANTERIOR A LA FECHA DE HOY EL EVENTO NO SE PUEDE ELIMINAR PORQUE YA TERMINO
        if (e.getTimeInMillis() < c.getTimeInMillis()) {
            //PERO EL EVENTO PUEDE TENER FECHA DE REPETICION. HAY QUE VERIFICAR SI EL EVENTO SE REPITE
            //SI EL EVENTO NO SE REPITE QUIERE DECIR QUE EL EVENTO YA TERMINO Y NO SE PUEDE ELIMINAR
            if (repeticion.equals("NSR~~")) {
                eliminar = false;
            }

            // SI EL EVENTO SE REPITE HAY QUE CHECAR HASTA CUANDO SE REPITE EL EVENTO
            else {
                e.set(2016, 0, 1);
                e.set(Calendar.DAY_OF_YEAR, Integer.parseInt(repeticion.split("~")[2]));
                horasaCalendario(e, hora_final);

                // SI EL EVENTO SE REPITE EN TIEMPO INFERIOR AL DIA DE HOY QUIERE DECIR QUE YA TERMINO
                if (e.getTimeInMillis() < c.getTimeInMillis()) {
                    eliminar = false;
                }
                // SI EL EVENTO SE REPITE EN TIEMPO POSTERIOR AL DIA DE HOY HAY QUE CHECAR SI EL EVENTO NO ESTA TRANSCURRIENDO
                else if (e.getTimeInMillis() > c.getTimeInMillis()) {
                    horasaCalendario(e, hora_inicial);
                    if (e.getTimeInMillis() < c.getTimeInMillis()) {
                        eliminar = false;
                        alerta = "Una sesión de este evento esta ocurriendo ahora mismo, no se puede eliminar";
                    } else {
                        eliminar = true;
                        alerta = "Este evento " + stringRepeticion(repeticion).toLowerCase() + ".\n" + "¿Deseas eliminarlo?";
                    }
                }
            }
        } else if (e.getTimeInMillis() > c.getTimeInMillis()) {
            //LA HORA FINAL ES MAYOR, HAY QUE CHECAR LA INICIAL PARA SABER SI EL EVENTO ESTA AHORITA O NO
            horasaCalendario(e, hora_inicial);

            //SI LA HORA INICIAL ES MAYOR TAMBIEN QUIERE DECIR QUE EL EVENTO AUN NO COMIENZA
            if (e.getTimeInMillis() > c.getTimeInMillis()) {
                eliminar = true;
            } else {
                //SI LA HORA INICIAL ES MENOR ENTONCES EL EVENTO YA COMENZO
                eliminar = false;
                alerta = "Una sesión de este evento esta ocurriendo ahora mismo, no se puede eliminar";
            }
        }
        return eliminar;
    }

    private void horasaCalendario(Calendar e, String hora_inicial) {

        int hora = (Integer.parseInt(hora_inicial) / 2) + 7;
        e.set(Calendar.HOUR_OF_DAY, hora);

        if (Integer.parseInt(hora_inicial) % 2 != 0) {
            e.set(Calendar.MINUTE, 30);
        }
    }

    @OnClick(R.id.fab_edit)
    public void EditarEnvento() {
        if (Eliminar(evento.getFecha_inicial(), evento.getFecha_final(), evento.getHora_inicial(), evento.getHora_final(), evento.getRepeticion())) {
            Intent intent = new Intent(this, EditarEvento.class);
            intent.putExtra("EVENTO", evento);
            FabTransition.setCustomDuration(500);
            FabTransition.addExtras(intent, (Integer) getAcentColor(), R.drawable.ic_edit);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, fab_edit,
                    getString(R.string.transition_date_dialog_helzio));
            startActivity(intent, options.toBundle());
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DialogInfoEventosHelzio.this);
            alertDialogBuilder.setMessage("\n" + (alerta.equals("") ? "Este evento ya ha finalizado, no se puede editar" : alerta.replace("eliminar", "editar")));
            alertDialogBuilder.setPositiveButton("ACEPTAR", null);

            alertDialogBuilder.create().show();
        }

    }

    public Object getAcentColor() {
        int colorAttr;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            colorAttr = android.R.attr.colorAccent;
        } else {
            //Get colorAccent defined for AppCompat
            colorAttr = getResources().getIdentifier("colorAccent", "attr", getPackageName());
        }
        TypedValue outValue = new TypedValue();
        getTheme().resolveAttribute(colorAttr, outValue, true);
        return outValue.data;
    }

    private String fecha(String fecha_inicial, String fecha_final) {
        if (fecha_inicial.equals(fecha_final)) {
            Calendar fecha = Calendar.getInstance();
            fecha.set(2016, 0, 1);
            fecha.add(Calendar.DAY_OF_YEAR, Integer.valueOf(fecha_inicial) - 1);
            SimpleDateFormat format = new SimpleDateFormat("cccc',' d 'de' MMMM 'del' yyyy");
            fecha_inicial = (format.format(fecha.getTime()));
        } else {
            Calendar fecha = Calendar.getInstance();
            fecha.set(2016, 0, 1);
            fecha.add(Calendar.DAY_OF_YEAR, Integer.valueOf(fecha_inicial) - 1);
            SimpleDateFormat format = new SimpleDateFormat("cccc',' d 'de' MMMM 'del' yyyy");
            fecha_inicial = (format.format(fecha.getTime()));

            fecha.set(2016, 0, 1);
            fecha.add(Calendar.DAY_OF_YEAR, Integer.valueOf(fecha_final) - 1);
            SimpleDateFormat format2 = new SimpleDateFormat("cccc',' d 'de' MMMM 'del' yyyy");

            if (fecha_inicial.split(" ")[2].equals(format2.format(fecha.getTime()).split(" ")[2])) {
                fecha_inicial = ("Del " + fecha_inicial.split(" ")[0].toLowerCase().replace(",", "") + " " + fecha_inicial.split(" ")[1] + " al " + format2.format(fecha.getTime()).toLowerCase().replaceAll(",", ""));
            } else {
                fecha_inicial = (fecha_inicial + "  -  " + format2.format(fecha.getTime()));
            }
        }
        return fecha_inicial;
    }

    private String stringRepeticion(String repeticion) {
        if (!repeticion.equals("NSR~~")) {
            if (repeticion.split(getString(R.string.separador_repeticion))[0].contains("S")) {
                Calendar repe_info = Calendar.getInstance();
                repe_info.set(2016, 0, 1);
                repe_info.set(Calendar.DAY_OF_YEAR, Integer.valueOf(repeticion.split(getString(R.string.separador_repeticion))[2]));
                SimpleDateFormat format = new SimpleDateFormat("d 'de' MMMM 'del' yyyy");
                if (repeticion.split(getString(R.string.separador_repeticion))[0].substring(0, 1).equals("1")) {
                    repeticion = ("Se repite cada semana hasta el " + format.format(repe_info.getTime()));
                } else {
                    repeticion = ("Se repite cada " + repeticion.split(getString(R.string.separador_repeticion))[0].substring(0, 1) + " semanas hasta el " + format.format(repe_info.getTime()));
                }
            }
        } else {
            repeticion = "Éste evento no se repite.";
        }
        return repeticion;
    }

    private String REX(String s) {
        String rex = "";
        if (s.split(getString(R.string.separador_repeticion))[0].equals("E")) {
            rex = "Editado por " + s.split(getString(R.string.separador_repeticion))[1];
        }
        if (s.split(getString(R.string.separador_repeticion))[0].equals("R")) {
            rex = "Registrado por " + s.split(getString(R.string.separador_repeticion))[1];
        }
        if (s.split(getString(R.string.separador_repeticion))[0].equals("X")) {
            rex = "Eliminado por " + s.split(getString(R.string.separador_repeticion))[1];
        }

        return rex;
    }

    private String nombreAuditorio(String numero) {
        String st = "";
        switch (numero) {
            case "1":
                st = auditorio1;
                break;
            case "2":
                st = auditorio2;
                break;
            case "3":
                st = auditorio3;
                break;
            case "4":
                st = auditorio4;
                break;
            case "5":
                st = auditorio5;
                break;
        }
        return st;
    }

    @Override
    public void onBackPressed() {
        dismiss(null);
    }

    public void dismiss(View view) {
        finishAfterTransition();
    }

    private Bitmap getViewBitmap(View v) {
        v.clearFocus();
        v.setPressed(false);

        boolean willNotCache = v.willNotCacheDrawing();
        v.setWillNotCacheDrawing(false);

        // Reset the drawing cache background color to fully transparent
        // for the duration of this operation
        int color = v.getDrawingCacheBackgroundColor();
        v.setDrawingCacheBackgroundColor(0);

        if (color != 0) {
            v.destroyDrawingCache();
        }
        v.buildDrawingCache();
        Bitmap cacheBitmap = v.getDrawingCache();
        if (cacheBitmap == null) {
            return null;
        }

        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

        // Restore the view
        v.destroyDrawingCache();
        v.setWillNotCacheDrawing(willNotCache);
        v.setDrawingCacheBackgroundColor(color);

        return bitmap;
    }

    class EliminarEventoBaseDatos extends AsyncTask<String, String, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Calendar calendarioRegistro = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("d/MM/yyyy'~'h:mm a");

            Intent inte = getIntent();
            int pos = inte.getIntExtra("POSITION", 333);

            int i = 0;
            st_evento_para_guardar = evento.getTag().replace(evento.getTag().split("::")[9], "X~" + st_quien).replace(evento.getTag().split("::")[10], format.format(calendarioRegistro.getTime()));
            for (String eev : Principal.lista_eventos) {
                if (eev.equals(evento.getTag()+ "¦")) {
                    Principal.lista_eventos.set(i, st_evento_para_guardar + "¦");
                }
                i++;
            }

            Collections.sort(Principal.lista_eventos, new Comparator<String>() {
                @Override
                public int compare(String s1, String s2) {
                    s1 = s1.replace("\uFEFF", "") + "";
                    s2 = s2.replace("\uFEFF", "") + "";
                    Integer i1 = Integer.parseInt("" + s1.split("::")[0].trim());
                    Integer i2 = Integer.parseInt("" + s2.split("::")[0].trim());
                    if (i1 == i2) {
                        Integer i3 = Integer.parseInt("" + s1.split("::")[1].trim());
                        Integer i4 = Integer.parseInt("" + s2.split("::")[1].trim());
                        if (i3 == i4) {
                            Integer i5 = Integer.parseInt("" + s1.split("::")[2].trim());
                            Integer i6 = Integer.parseInt("" + s2.split("::")[2].trim());
                            if (i5 == i6) {
                                Integer i7 = Integer.parseInt("" + s1.split("::")[3].trim());
                                Integer i8 = Integer.parseInt("" + s2.split("::")[3].trim());

                                return i7.compareTo(i8);
                            } else {
                                return i5.compareTo(i6);
                            }
                        } else {
                            return i3.compareTo(i4);
                        }
                    } else {
                        return i1.compareTo(i2);
                    }
                }
            });

            st_eventos_guardados = "";
            for (String item : Principal.lista_eventos) {
                st_eventos_guardados += item;
            }
            datos = "";
            registroCorrecto = false;
        }

        @Override
        protected Void doInBackground(String... aa12) {
            if (st_eventos_guardados.length() > 333) {
                try {
                    URL url = new URL("http://148.202.6.72/aplicacion/datos2.php");
                    HttpURLConnection aaaaa = (HttpURLConnection) url.openConnection();
                    aaaaa.setReadTimeout(0);
                    aaaaa.setConnectTimeout(0);
                    aaaaa.setRequestMethod("POST");
                    aaaaa.setDoInput(true);
                    aaaaa.setDoOutput(true);

                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("comentarios", st_eventos_guardados);
                    String query = builder.build().getEncodedQuery();

                    OutputStream os = aaaaa.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(query);
                    writer.flush();
                    writer.close();
                    os.close();

                    aaaaa.connect();

                    int aaaaaaa = aaaaa.getResponseCode();
                    if (aaaaaaa == HttpsURLConnection.HTTP_OK) {
                        registroCorrecto = true;
                        String aaaaaaaa;
                        BufferedReader br = new BufferedReader(new InputStreamReader(aaaaa.getInputStream(), "UTF-8"));
                        while ((aaaaaaaa = br.readLine()) != null) {
                            datos += aaaaaaaa;
                        }
                    } else {
                        datos = "error code: " + aaaaaaa;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (datos.contains("error code: ") || !registroCorrecto) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new EliminarEventoBaseDatos().execute();
                    }
                }, 1000);
            } else {
                Toast.makeText(DialogInfoEventosHelzio.this, "El evento con el ID " + evento.getId() + " ha sido eliminado", Toast.LENGTH_LONG).show();
                SharedPreferences prefs = getSharedPreferences("EVENTOS CUCSH", Context.MODE_PRIVATE);
                prefs.edit().putString("EVENTOS GUARDADOS", st_eventos_guardados).apply();
                Intent i = getIntent();
                i.putExtra("POSITION", i.getIntExtra("POSITION", 0));
                setResult(RESULT_OK, i);
                finishAfterTransition();
            }
        }
    }
}
