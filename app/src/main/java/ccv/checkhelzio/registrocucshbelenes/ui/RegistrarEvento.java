package ccv.checkhelzio.registrocucshbelenes.ui;

import android.animation.Animator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ccv.checkhelzio.registrocucshbelenes.R;
import ccv.checkhelzio.registrocucshbelenes.util.AnimUtils;

public class RegistrarEvento extends AppCompatActivity {

    @BindView(R.id.fondo)
    LinearLayout fondo;
    @BindView(R.id.color_reveal)
    View color_reveal;
    @BindView(R.id.atv_tituto_evento)
    AutoCompleteTextView atv_titulo_evento;
    @BindView(R.id.atv_tipo_evento)
    AutoCompleteTextView atv_tipo_evento;
    @BindView(R.id.atv_nombre_org)
    AutoCompleteTextView atv_nombre_org;
    @BindView(R.id.toolbar_dialog)
    RelativeLayout full_header;
    @BindView(R.id.sp_auditorios)
    Spinner sp_auditorios;
    @BindView(R.id.tv_repeticion)
    TextView tv_repeticion;
    @BindView(R.id.tv_titulo_label)
    TextView tv_titulo_label;
    @BindView(R.id.tv_tipo_evento_label)
    TextView tv_tipo_evento_label;
    @BindView(R.id.tv_nom_org_label)
    TextView tv_nom_org_label;
    @BindView(R.id.tv_contraseña_label)
    TextView tv_contraseña_label;
    @BindView(R.id.et_numero_tel)
    EditText et_num_tel;
    @BindView(R.id.et_contraseña)
    EditText et_contraseña;
    @BindView(R.id.et_nota)
    EditText et_nota;
    @BindView(R.id.snackposs) CoordinatorLayout snackposs;
    @BindView(R.id.rv_fechas)
    RecyclerView rv_fechas;

    private Handler handler;
    private boolean cerrar = false;
    private boolean pin_correcto_eliminar = false;

    private final String auditorio1 = "Auditorio Salvador Allende";
    private final String auditorio2 = "Auditorio Silvano Barba";
    private final String auditorio3 = "Auditorio Carlos Ramírez";
    private final String auditorio4 = "Auditorio Adalberto Navarro";
    private final String auditorio5 = "Sala de Juicios Orales Mariano Otero";
    private String st_quien;
    private String data;
    private boolean registroCorrecto;
    private String st_eventos_guardados;
    private String stComprobarEv;
    private String AD;
    private int int_fecha;
    List<int[]> listaFechasHoras = new ArrayList<int[]>();
    private boolean hora_correcta;
    private boolean unaFecha = true;
    private Snackbar snackHora;
    private String tagHora = "";
    private final static int INICIAL = 333;
    private final static int AGREGAR = 334;
    private String[] horas = new String[]{
            "07:00 AM", //0
            "07:30 AM", //1
            "08:00 AM", //2
            "08:30 AM", //3
            "09:00 AM", //4
            "09:30 AM", //5
            "10:00 AM", //6
            "10:30 AM", //7
            "11:00 AM", //8
            "11:30 AM", //9
            "12:00 PM", //10
            "12:30 PM", //11
            "01:00 PM", //12
            "01:30 PM", //13
            "02:00 PM", //14
            "02:30 PM", //15
            "03:00 PM", //16
            "03:30 PM", //17
            "04:00 PM", //18
            "04:30 PM", //19
            "05:00 PM", //20
            "05:30 PM", //21
            "06:00 PM", //22
            "06:30 PM", //23
            "07:00 PM", //24
            "07:30 PM", //25
            "08:00 PM", //26
            "08:30 PM", //27
            "09:00 PM", //28
            "09:30 PM", //29
            "10:00 PM", //30
    };

    protected ArrayAdapter<String> adapterHoras;
    protected ArrayList<Fecha> listaFechas = new ArrayList<>();
    protected ArrayList<Eventos> listaFechasE = new ArrayList<>();

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //PONEMOS LAYOT QUE VAMOS A USAR EN ESTA ACTIVITY
        setContentView(R.layout.dialog_editar_evento);

        //POSPONEMOS LA ANIMACION DE TRANSICION PARA AGREGAR UNA PERSONALIZADA
        postponeEnterTransition();

        //OCULTAR EL TECLADO PARA QUE NO SE ABRA AL INICIAR LA ACTIVITY
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //INICIAR EL PLUGIN QUE PERMITE HACER LAS DECLARACIONES MAS RAPIDO
        ButterKnife.bind(this);

        // CREAMOS LA TRANSICION DE ENTRADA Y LA INICIAMOS
        Slide slide = new Slide(Gravity.BOTTOM);
        slide.setInterpolator(AnimUtils.getLinearOutSlowInInterpolator(RegistrarEvento.this));
        slide.excludeTarget(android.R.id.statusBarBackground, true);
        slide.excludeTarget(android.R.id.navigationBarBackground, true);
        getWindow().setEnterTransition(slide);
        full_header.setBackgroundColor(fondoAuditorio("1"));

        startPostponedEnterTransition();

        //INICIAMOS TODOS LOS VIEWS QUE VAMOS A UTILIZAR
        iniciarObjetos();
        iniciarDatos();
    }

    private void iniciarObjetos() {
        // CREAMOS UN HANDLER PARA TAREAS CON TIEMPO DE RETRASO
        handler = new Handler();
    }

    private void iniciarDatos() {

        //TITULO DEL EVENTO
        atv_titulo_evento.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String result = s.toString().replaceAll("::", "").replace("¦", "");
                if (!s.toString().equals(result)) {
                    atv_titulo_evento.setText(result);
                    atv_titulo_evento.setSelection(result.length());
                }

                if (atv_titulo_evento.getText().toString().trim().length() == 0) {
                    tv_titulo_label.setTextColor(Color.RED);
                } else {
                    tv_titulo_label.setTextColor(Color.WHITE);
                }
            }
        });

        // DESPUES DE PONER EL TITULO INICIAMOS EL AUTOCOMPLETAR PARA EL NOMBRE DEL EVENTO PARA QUE NO SALGA LA LISTA DE EVENTOS
        ArrayAdapter<String> nombresEAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Principal.titulos.split("¦"));
        atv_titulo_evento.setAdapter(nombresEAdapter);

        // CONFIGURAR SPINER PARA SELECCIONAR EL AUDITORIO
        String[] items = new String[]{
                auditorio1, auditorio2, auditorio3, auditorio4, auditorio5};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        sp_auditorios.setAdapter(adapter);
        sp_auditorios.setSelection(0);
        sp_auditorios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                //CONFIGURAR EL COLOR DEL HEADER SEGUN EL AUDITORIO SELECCIOANDO
                colorReveal(fondoAuditorio((i + 1) + ""));
                AD = "" + (i + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //TIPO DE EVENTO
        tv_tipo_evento_label.setTextColor(Color.RED);

        // INICIAMOS EL AUTOCOMPLETAR DE TIPOS DE EVENTO Y COLOCAMOS EL TIPO DE EVENTO CORRESPONDIENTE
        ArrayAdapter<String> tiposEventoAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Principal.tiposDeEvento.split("¦"));
        atv_tipo_evento.setAdapter(tiposEventoAdapter);
        atv_tipo_evento.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String result = s.toString().replaceAll("::", "").replace("¦", "");
                if (!s.toString().equals(result)) {
                    atv_tipo_evento.setText(result);
                    atv_tipo_evento.setSelection(result.length());
                }

                if (atv_tipo_evento.getText().toString().trim().length() == 0) {
                    tv_tipo_evento_label.setTextColor(Color.RED);
                } else {
                    tv_tipo_evento_label.setTextColor(Color.parseColor("#121212"));
                }
            }
        });

        // COLOCAMOS COMO FECHA INICIAL LA FECHA DEL DIA DEL EVENTO
        int_fecha = getIntent().getIntExtra("DIA_AÑO", 0);

        //CONFIGUAR NOMBRE DEL ORGANIZADOR
        tv_nom_org_label.setTextColor(Color.RED);
        atv_nombre_org.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String result = s.toString().replaceAll("::", "").replace("¦", "");
                if (!s.toString().equals(result)) {
                    atv_nombre_org.setText(result);
                    atv_nombre_org.setSelection(result.length());
                }

                if (atv_nombre_org.getText().toString().trim().length() == 0) {
                    tv_nom_org_label.setTextColor(Color.RED);
                } else {
                    tv_nom_org_label.setTextColor(Color.parseColor("#121212"));
                }
            }
        });

        //CONFIGURAR AUTOCOMPLETAR PARA EL NOMBRE DEL ORGANIZADOR
        ArrayAdapter<String> nombresOrganizadorAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Principal.nombresOrganizador.split("¦"));
        atv_nombre_org.setAdapter(nombresOrganizadorAdapter);

        // CONFIGURAMOS EL EDIT TEXT DE LA CONTRASEÑA
        tv_contraseña_label.setTextColor(Color.RED);
        et_contraseña.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (et_contraseña.getText().toString().trim().length() == 0) {
                    tv_contraseña_label.setTextColor(Color.RED);
                } else {
                    tv_contraseña_label.setTextColor(Color.parseColor("#121212"));
                }

                switch (et_contraseña.getText().toString()) {
                    case "1308":
                        pin_correcto_eliminar = true;
                        et_contraseña.setTextColor(Color.parseColor("#121212"));
                        st_quien = "Susy";
                        break;
                    case "2886":
                        pin_correcto_eliminar = true;
                        et_contraseña.setTextColor(Color.parseColor("#121212"));
                        st_quien = "Tere";
                        break;
                    case "4343":
                        pin_correcto_eliminar = true;
                        et_contraseña.setTextColor(Color.parseColor("#121212"));
                        st_quien = "CTA";
                        break;
                    default:
                        pin_correcto_eliminar = false;
                        et_contraseña.setTextColor(Color.RED);
                        st_quien = "";
                        break;
                }
            }
        });

        // CONFIGURAR LA PRIMER FECHA EN LA LISTA DE FECHAS
        listaFechas.add(new Fecha(int_fecha, 0,2));


        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        rv_fechas.setLayoutManager(mLayoutManager);
        FechasAdaptador adaptador = new FechasAdaptador(listaFechas, RegistrarEvento.this);
        rv_fechas.setAdapter(adaptador);

        ItemTouchHelper.Callback callback = new SwipeHelper(adaptador);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(rv_fechas);

        rv_fechas.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                loopComprobarhoras();
            }
        });
    }

    private void loopComprobarhoras() {

        if (!cerrar){
            int x = 0;
            for (Fecha f : listaFechas){
                comprobarFecha(f, x,Principal.lista_eventos);
                x++;
            }
        }

        if (!cerrar) {
            int n = 0;
            for (Fecha f : listaFechas){
                comprobarFecha(f,n,listaFechasE);
                n++;
            }
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loopComprobarhoras();
                }
            }, 200);
        }
    }
    //SE AÑADIO listafechasC PARA HACER QUE FUNCIONE CON CUALQUIER LISTA
    private void comprobarFecha(Fecha f, int n,ArrayList<Eventos> listaFechasC) {
        // COMPROBAR PRIMERO SI SE ESTA INTENTANDO REGISTRAR UN EVENTO A LAS 9:00 PM O MAS TARDE
        if (f.getHoraInicial() > 27) {
            hora_correcta = false;
            f.getLabel_inicial().setTextColor(Color.RED);
            f.getLabel_final().setTextColor(Color.RED);

            if (!tagHora.equals("1")) {
                tagHora = "1";
                mostrarSnack("Fecha del evento No. " + n + "\nLos eventos no pueden comenzar a después de las 8:30 PM");
            }
        }
        // SI EL EVENTO SE ESTA INTENTANDO REGISTRAR EN UNA HORA VALIDA COMPROBAMOS EL CUPO CON LA BASE DE DATOS
        else {
            // SEPARAMOS EN EVENTOS INDIVIDUALES
            for (Eventos e : listaFechasC) {
                if (Integer.parseInt(e.getFecha()) == f.getDia() && e.getAuditorio().equals(AD) && !e.getStatusEvento().equals("X")){

                    // EL EVENTO COMIENZA 30 MINUTOS ANTES DE QUE COMIENCE OTRO
                    if (f.getHoraInicial() == Integer.valueOf(e.getHoraInicial()) - 1) {
                        hora_correcta = false;
                        f.getLabel_inicial().setTextColor(Color.RED);
                        f.getLabel_final().setTextColor(Color.parseColor("#121212"));

                        if (!tagHora.equals("4")) {
                            tagHora = "4";
                            mostrarSnack("Fecha del evento No. " + n + "\nEl evento que quieres editar comienza 30 minutos antes de que comience el evento (" + e.getId() + ").");
                        }
                        break;
                    }

                    //LOS EVENTOS COMIENZAN A LA MISMA HORA
                    else if (f.getHoraInicial() == Integer.valueOf(e.getHoraInicial())) {
                        hora_correcta = false;
                        f.getLabel_inicial().setTextColor(Color.RED);
                        f.getLabel_final().setTextColor(Color.parseColor("#121212"));

                        if (!tagHora.equals("2")) {
                            tagHora = "2";
                            mostrarSnack("Fecha del evento No. " + n + "\nEl evento que quieres editar comienza a la misma hora que el evento (" + e.getId() + ").");
                        }
                        break;
                    }

                    // EL EVENTO COMIENZA 30 MINUTOS ANTES DE QUE TERMINE EL ANTERIOR
                    else if (f.getHoraInicial() == Integer.valueOf(e.getHoraFinal())) {
                        hora_correcta = false;
                        f.getLabel_inicial().setTextColor(Color.RED);
                        f.getLabel_final().setTextColor(Color.parseColor("#121212"));

                        if (!tagHora.equals("3")) {
                            tagHora = "3";
                            mostrarSnack("Fecha del evento No. " + n + "\nEl evento que quieres editar comienza 30 minutos antes de que finalice el evento (" + e.getId() + ").");
                        }
                        break;
                    }

                    // LA HORA INICIAL DEL EVENTO ESTA JUSTO EN MEDIO DEL HORARIO DE OTRO
                    else if (f.getHoraInicial() > Integer.valueOf(e.getHoraInicial()) && f.getHoraInicial() < Integer.valueOf(e.getHoraFinal())) {
                        hora_correcta = false;
                        f.getLabel_inicial().setTextColor(Color.RED);
                        f.getLabel_final().setTextColor(Color.parseColor("#121212"));

                        if (!tagHora.equals("5")) {
                            tagHora = "5";
                            mostrarSnack("Fecha del evento No. " + n + "\nEl evento que quieres editar comienza dentro del horario del evento (" + e.getId() + ").");
                        }
                        break;

                    }

                    // LA HORA INICIAL DEL EVENTO ES MENOR A LA HORA INICIAL DEL PROX EVENTO
                    else if (f.getHoraInicial() < Integer.valueOf(e.getHoraInicial())) {

                        // EL EVENTO FINALIZA DENTRO DEL HORARIO DEL PROXIMO
                        if (f.getHoraFinal() - 1 == Integer.valueOf(e.getHoraInicial()) || f.getHoraFinal() - 1 == Integer.valueOf(e.getHoraFinal())) {
                            hora_correcta = false;

                            if (!tagHora.equals("6")) {
                                tagHora = "6";
                                mostrarSnack("Fecha del evento No. " + n + "\nEl evento que quieres editar termina dentro del horario del evento (" + e.getId() + ").");
                            }

                            f.getLabel_final().setTextColor(Color.RED);
                            f.getLabel_inicial().setTextColor(Color.parseColor("#121212"));
                            break;
                        }
                        // EL EVENTO FINALIZA DENTRO DEL HORARIO DEL PROXIMO
                        else if (f.getHoraFinal() - 1 > Integer.valueOf(e.getHoraInicial()) && f.getHoraFinal() - 1 < Integer.valueOf(e.getHoraFinal())) {
                            hora_correcta = false;
                            f.getLabel_final().setTextColor(Color.RED);
                            f.getLabel_inicial().setTextColor(Color.parseColor("#121212"));

                            if (!tagHora.equals("6")) {
                                tagHora = "6";
                                mostrarSnack("Fecha del evento No. " + n + "\nEl evento que quieres editar termina dentro del horario del evento (" + e.getId() + ").");
                            }

                            break;
                        }
                        // EL EVENTO COMIENZA ANTES Y TERMINA DESPUES DEL PROXIMO
                        else if (f.getHoraFinal() - 1 > Integer.valueOf(e.getHoraFinal())) {
                            hora_correcta = false;
                            f.getLabel_final().setTextColor(Color.RED);
                            f.getLabel_inicial().setTextColor(Color.parseColor("#121212"));

                            if (!tagHora.equals("7")) {
                                tagHora = "7";
                                mostrarSnack("Fecha del evento No. " + n + "\nHay un evento registrado en un horario intermedio al que has elegido (" + e.getId() + ").");
                            }
                            break;
                        } else if (f.getHoraFinal() - 1 < f.getHoraInicial()) {
                            hora_correcta = false;
                            f.getLabel_final().setTextColor(Color.RED);
                            f.getLabel_inicial().setTextColor(Color.parseColor("#121212"));

                            if (!tagHora.equals("8")) {
                                tagHora = "8";
                                mostrarSnack("La hora final del evento no puede ser igual o menor a la inicial.");
                            }
                        } else if (f.getHoraFinal() - 1 == f.getHoraInicial()) {
                            hora_correcta = false;
                            f.getLabel_final().setTextColor(Color.RED);
                            f.getLabel_inicial().setTextColor(Color.parseColor("#121212"));

                            if (!tagHora.equals("9")) {
                                tagHora = "9";
                                mostrarSnack("El evento debe durar más de 30 minutos.");
                            }
                        }

                        // SI LA LISTA TIENE MAS DE UN FECHA
                        else if(n > 0){

                            Log.v("NINGUNA", "NINGUNA DE LAS ANTERIORES");
                            for (int x = 0; x < n; x++ ){
                                if (listaFechas.get(x).getDia() == listaFechas.get(n).getDia()){
                                    if (listaFechas.get(x).getDia() == listaFechas.get(n).getDia()){

                                        if (listaFechas.get(x).getHoraInicial() == listaFechas.get(n).getHoraInicial()){
                                            hora_correcta = false;
                                            listaFechas.get(n).getLabel_inicial().setTextColor(Color.RED);

                                            if (!tagHora.equals("10")) {
                                                tagHora = "10";
                                                mostrarSnack("La hora inicial del evento No. " + n + " es igual a la hora inicial del evento No." + x + ".");
                                            }
                                        }
                                    }
                                }else {
                                    hora_correcta = true;
                                    f.getLabel_final().setTextColor(Color.parseColor("#121212"));
                                    f.getLabel_inicial().setTextColor(Color.parseColor("#121212"));

                                    // QUITAMOS LOS DIALOGS SOLO SI ES EL ULTIMO EVENTO QUE COMPROBAMOS
                                    if (f == listaFechas.get(listaFechas.size() - 1)) {
                                        try {
                                            snackHora.dismiss();
                                            tagHora = "";
                                        } catch (Exception ignored) {
                                        }
                                    }
                                }
                            }
                        }

                        else {
                            hora_correcta = true;
                            f.getLabel_final().setTextColor(Color.parseColor("#121212"));
                            f.getLabel_inicial().setTextColor(Color.parseColor("#121212"));

                            // QUITAMOS LOS DIALOGS SOLO SI ES EL ULTIMO EVENTO QUE COMPROBAMOS
                            if (f == listaFechas.get(listaFechas.size() - 1)) {
                                try {
                                    snackHora.dismiss();
                                    tagHora = "";
                                } catch (Exception ignored) {
                                }
                            }
                        }
                    }

                    //LA HORA INICIAL ES MAYOR AL EVENTO ANTERIOR POR LO TANTO ESTA BIEN
                    else if (f.getHoraInicial() > Integer.valueOf(e.getHoraInicial()) && f.getHoraInicial() > Integer.valueOf(e.getHoraFinal())) {
                        if (f.getHoraFinal() - 1 < f.getHoraInicial()) {
                            hora_correcta = false;
                            f.getLabel_final().setTextColor(Color.RED);
                            f.getLabel_inicial().setTextColor(Color.parseColor("#121212"));

                            if (!tagHora.equals("8")) {
                                tagHora = "8";
                                mostrarSnack("La hora final del evento no puede ser igual o menor a la inicial.");
                            }
                        } else if (f.getHoraFinal() - 1 == f.getHoraInicial()) {
                            hora_correcta = false;
                            f.getLabel_final().setTextColor(Color.RED);
                            f.getLabel_inicial().setTextColor(Color.parseColor("#121212"));

                            if (!tagHora.equals("9")) {
                                tagHora = "9";
                                mostrarSnack("El evento debe durar más de 30 minutos.");
                            }
                        } else {
                            hora_correcta = true;
                            f.getLabel_final().setTextColor(Color.parseColor("#121212"));
                            f.getLabel_inicial().setTextColor(Color.parseColor("#121212"));
                            if (f == listaFechas.get(listaFechas.size() - 1)) {
                                try {
                                    snackHora.dismiss();
                                    tagHora = "";
                                } catch (Exception ignored) {
                                }
                            }
                        }
                    }
                }else {
                    if (f.getHoraFinal() - 1 < f.getHoraInicial()) {
                        hora_correcta = false;
                        f.getLabel_final().setTextColor(Color.RED);
                        f.getLabel_inicial().setTextColor(Color.parseColor("#121212"));

                        if (!tagHora.equals("8")) {
                            tagHora = "8";
                            mostrarSnack("La hora final del evento no puede ser igual o menor a la inicial.");
                        }
                    } else if (f.getHoraFinal() - 1 == f.getHoraInicial()) {
                        hora_correcta = false;
                        f.getLabel_final().setTextColor(Color.RED);
                        f.getLabel_inicial().setTextColor(Color.parseColor("#121212"));

                        if (!tagHora.equals("9")) {
                            tagHora = "9";
                            mostrarSnack("El evento debe durar más de 30 minutos.");
                        }
                    } else {

                        for (int x = 0; x < n; x++ ){
                            if (listaFechas.get(x).getDia() == listaFechas.get(n).getDia()){
                                if (listaFechas.get(x).getHoraInicial() == listaFechas.get(n).getHoraInicial()){
                                    hora_correcta = false;
                                    listaFechas.get(n).getLabel_inicial().setTextColor(Color.RED);

                                    if (!tagHora.equals("10")) {
                                        tagHora = "10";
                                        mostrarSnack("La hora inicial No. " + n + " es igual a la hora inicial No. " + (x + 1) + ".");
                                    }
                                }else if (listaFechas.get(x).getHoraInicial() == listaFechas.get(n).getHoraInicial() - 1){
                                    hora_correcta = false;
                                    listaFechas.get(n).getLabel_inicial().setTextColor(Color.RED);

                                    if (!tagHora.equals("11")) {
                                        tagHora = "11";
                                        mostrarSnack("La hora inicial No. " + n + " comienza 30 min después que la hora inicial No. " + (x + 1) + ".");
                                    }
                                }else if (listaFechas.get(x).getHoraInicial()- 1 == listaFechas.get(n).getHoraInicial() ){
                                    hora_correcta = false;
                                    listaFechas.get(n).getLabel_inicial().setTextColor(Color.RED);

                                    if (!tagHora.equals("11")) {
                                        tagHora = "11";
                                        mostrarSnack("La hora inicial No. " + n + " comienza 30 min antes que la hora inicial No. " + (x + 1) + ".");
                                    }
                                }else if (listaFechas.get(n).getHoraInicial() > listaFechas.get(x).getHoraInicial() && listaFechas.get(n).getHoraInicial() < listaFechas.get(x).getHoraFinal()){
                                    hora_correcta = false;
                                    listaFechas.get(n).getLabel_inicial().setTextColor(Color.RED);

                                    if (!tagHora.equals("12")) {
                                        tagHora = "12";
                                        mostrarSnack("La hora inicial No. " + n + " esta dentro del horario No. " + (x + 1) + ".");
                                    }
                                }else if (listaFechas.get(n).getHoraInicial() > listaFechas.get(x).getHoraInicial() && listaFechas.get(n).getHoraInicial() < listaFechas.get(x).getHoraFinal()){
                                    hora_correcta = false;
                                    listaFechas.get(n).getLabel_inicial().setTextColor(Color.RED);

                                    if (!tagHora.equals("12")) {
                                        tagHora = "12";
                                        mostrarSnack("La hora inicial No. " + n + " esta dentro del horario No. " + (x + 1) + ".");
                                    }
                                }else {
                                    hora_correcta = true;
                                    f.getLabel_final().setTextColor(Color.parseColor("#121212"));
                                    f.getLabel_inicial().setTextColor(Color.parseColor("#121212"));

                                    // QUITAMOS LOS DIALOGS SOLO SI ES EL ULTIMO EVENTO QUE COMPROBAMOS
                                    if (f == listaFechas.get(listaFechas.size() - 1)) {
                                        try {
                                            snackHora.dismiss();
                                            tagHora = "";
                                        } catch (Exception ignored) {
                                        }
                                    }
                                }
                            }else {
                                hora_correcta = true;
                                f.getLabel_final().setTextColor(Color.parseColor("#121212"));
                                f.getLabel_inicial().setTextColor(Color.parseColor("#121212"));

                                // QUITAMOS LOS DIALOGS SOLO SI ES EL ULTIMO EVENTO QUE COMPROBAMOS
                                if (f == listaFechas.get(listaFechas.size() - 1)) {
                                    try {
                                        snackHora.dismiss();
                                        tagHora = "";
                                    } catch (Exception ignored) {
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void mostrarSnack(String s) {
        snackHora = Snackbar.make(snackposs, s, Snackbar.LENGTH_INDEFINITE);
        final View view = snackHora.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setMaxLines(3);
        snackHora.show();
    }

    private void colorReveal(final int fondo) {

        //COLOREAMOS DEL COLOR DEL AUDITORIO EL FONDO INVISIBLE
        color_reveal.setBackgroundColor(fondo);

        //OBTENEMOS EL CENTRO DEL FONDO INVISIBLE, SERA EL ORIGEN DEL EFECTO REVELAR
        int cx = (color_reveal.getLeft() + color_reveal.getRight()) / 2;
        int cy = (color_reveal.getTop());

        //OBTENEMOS EL RADIO FINAL PARA EL CIRCULO DEL EFECTO REVELAR
        int finalRadius = Math.max(color_reveal.getWidth(), color_reveal.getHeight());

        //Creamos un animator para la view, el radio del efecto comienza en cero;
        Animator anim = ViewAnimationUtils.createCircularReveal(color_reveal, cx, cy, 0, finalRadius);
        anim.setDuration(500L);

        //AGREGAMOS UN LISTENER PARA CUANDO TERMINE LA ANIMACION ESCONDER DE NUEVO EL VIEW
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                //CUANDO LA ANIMACION PONEMOS DEL MISMO COLOR EL FONDO INICIAL
                full_header.setBackgroundColor(fondo);

                //DESPUES OCULTAMOS NUEVAMENTE EL VIEW PARA EL EFECTO REVELAR
                color_reveal.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        //HACEMOS VISIBLE EL FONDO INVISIBLE Y COMENZAMOS LA ANIMACION
        color_reveal.setVisibility(View.VISIBLE);
        anim.start();

    }

    @OnClick(R.id.iv_cerrar_dialog)
    public void cerrar(View view) {
        cerrar = true;
        finishAfterTransition();
    }

    private int fondoAuditorio(String numero) {
        int st = 0;
        switch (numero) {
            case "1":
                st = getResources().getColor(R.color.ed_a);
                break;
            case "2":
                st = getResources().getColor(R.color.ed_b);
                break;
            case "3":
                st = getResources().getColor(R.color.ed_c);
                break;
            case "4":
                st = getResources().getColor(R.color.ed_d);
                break;
            case "5":
                st = getResources().getColor(R.color.ed_e);
                break;
        }
        return st;
    }

    private String fecha(String st_fecha) {
        Calendar fecha = Calendar.getInstance();
        fecha.set(2016, 0, 1);
        fecha.add(Calendar.DAY_OF_YEAR, Integer.valueOf(st_fecha) - 1);
        SimpleDateFormat format = new SimpleDateFormat("d 'de' MMMM 'del' yyyy");
        return format.format(fecha.getTime());
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

    @OnClick(R.id.tv_repeticion)
    public void AbrirDialogAgregarFechas() {
        Intent intent = new Intent(this, DialogAgregarFechas.class);
        intent.putExtra("DIA", int_fecha);
        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        startActivityForResult(intent, AGREGAR, bundle);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case INICIAL:
                if (resultCode == RESULT_OK) {
                    int_fecha = data.getIntExtra("DIA_DEL_AÑO", 0);
                    String st_fecha = "" + data.getIntExtra("DIA_DEL_AÑO", 0);
                }
                break;
            case AGREGAR:
                if (resultCode == RESULT_OK) {

                    // TRAEMOS LA LISTA DE LAS FECHAS SELECCIONADAS
                    ArrayList<Integer> listaFechas2 = data.getIntegerArrayListExtra("LISTA_FECHAS");


                    for (Integer f : listaFechas2){
                        try {
                            listaFechas.add(new Fecha(f, listaFechas.get(0).getHoraInicial(),listaFechas.get(0).getHoraFinal()));
                            //SE ESTÁ AÑADIENDO A LA LISTA LA MISMA FECHA PERO COMO UNA LISTA DE EVENTOS
                            listaFechasE.add(new Eventos(String.valueOf(listaFechas.get(0).getHoraInicial()),String.valueOf(listaFechas.get(0).getHoraFinal())));
                        }catch (Exception ignored0){
                            listaFechas.add(new Fecha(f, 0, 0));
                        }
                    }

                    // LA ORDENAMOS EN ORDEN ASCENDENTE PARA NO TENER DIAS SALTEADOS
                    Collections.sort(listaFechas, new Comparator<Fecha>() {
                        @Override
                        public int compare(Fecha f1, Fecha f2) {
                            Integer i1 = f1.getDia();
                            Integer i2 = f2.getDia();

                            if (i1 == i2){

                                Integer i3 = f1.getHoraInicial();
                                Integer i4 = f2.getHoraInicial();
                                if (i3 == i4){
                                    Integer i5 = f1.getHoraFinal();
                                    Integer i6 = f2.getHoraFinal();
                                    return i5.compareTo(i6);
                                }else {
                                    return i3.compareTo(i4);
                                }
                            }else {
                                return i1.compareTo(i2);
                            }

                        }
                    });

                    rv_fechas.getAdapter().notifyDataSetChanged();
                }
                break;
        }
    }
}

