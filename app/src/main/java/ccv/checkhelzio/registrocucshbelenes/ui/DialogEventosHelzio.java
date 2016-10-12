package ccv.checkhelzio.registrocucshbelenes.ui;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.transition.Slide;
import android.view.View;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ccv.checkhelzio.registrocucshbelenes.R;
import ccv.checkhelzio.registrocucshbelenes.transitions.ChangeBoundBackground;

public class DialogEventosHelzio extends Activity {

    @BindView(R.id.fondo) RelativeLayout fondo;
    @BindView(R.id.recycle) RecyclerView rvEventos;
    @BindView(R.id.tv_mensaje_no_evento) TextView tv_mensaje_no_eventos;
    @BindView(R.id.tv_mensaje_con_evento) TextView tv_mensaje_con_eventos;
    @BindView(R.id.tv_num_dia) TextView tv_num_dia;
    @BindView(R.id.tv_nom_dia) TextView tv_nom_dia;
    private List<Eventos> listaEventos;
    private  EventosAdaptador adaptador;
    protected static Boolean animando = false;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_lista_eventos);
        ButterKnife.bind(this);
        postponeEnterTransition();

        handler = new Handler();
        tv_num_dia.setText(getIntent().getStringExtra("DIA_MES"));
        String nom = getIntent().getStringExtra("NOMBRE_DIA").substring(0,3) + ".";
        tv_nom_dia.setText(nom);

        //rvEventos.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvEventos.setLayoutManager(mLayoutManager);

        inicializarDatos();

        fondo.postDelayed(new Runnable() {
            @Override
            public void run() {
                final Rect endBounds = new Rect(fondo.getLeft(), fondo.getTop(), fondo.getRight(), fondo.getBottom());
                ChangeBoundBackground.setup(DialogEventosHelzio.this, fondo, true, endBounds, getViewBitmap(fondo));
                getWindow().getSharedElementEnterTransition();
                startPostponedEnterTransition();
            }
        }, 30);
    }

    private void inicializarDatos() {
        try {
            String tag = getIntent().getStringExtra("TAG");
            listaEventos = new ArrayList<>();
            for (int zz = 0; zz < tag.split("¦").length; zz++) {
                if (!tag.split("¦")[zz].contains("X~") || !tag.split("¦")[zz].trim().equals("")) {
                    listaEventos.add(
                            new Eventos(
                                    // horarios
                                    horasATetxto(Integer.valueOf(tag.split("¦")[zz].split("::")[0].trim())) + " - " + horasATetxto(Integer.valueOf(tag.split("¦")[zz].split("::")[1].trim()) + 1),
                                    // fondo
                                    fondoAuditorio(tag.split("¦")[zz].split("::")[5].trim()),
                                    // hora inicial
                                    tag.split("¦")[zz].split("::")[0].trim(),
                                    // hora final
                                    tag.split("¦")[zz].split("::")[1].trim(),
                                    // fecha inicial
                                    tag.split("¦")[zz].split("::")[2].trim(),
                                    // fecha final
                                    tag.split("¦")[zz].split("::")[3].trim(),
                                    // titulo del evento
                                    tag.split("¦")[zz].split("::")[4].trim(),
                                    // auditorio
                                    tag.split("¦")[zz].split("::")[5].trim(),
                                    // tipo de evento
                                    tag.split("¦")[zz].split("::")[6].trim(),
                                    // nombre del organizador
                                    tag.split("¦")[zz].split("::")[7].trim(),
                                    // numero de telefono
                                    tag.split("¦")[zz].split("::")[8].trim(),
                                    // quien registro
                                    tag.split("¦")[zz].split("::")[9].trim(),
                                    // cuando registro
                                    tag.split("¦")[zz].split("::")[10].trim(),
                                    // notas
                                    tag.split("¦")[zz].split("::")[11].trim(),
                                    // repeticion
                                    tag.split("¦")[zz].split("::")[12].trim(),
                                    // id
                                    tag.split("¦")[zz].split("::")[13].trim(),
                                    // tag
                                    tag.split("¦")[zz]
                                    ));
                }
            }

            if (listaEventos.size() > 0){
                tv_mensaje_no_eventos.setVisibility(View.GONE);
                if (getIntent().getBooleanExtra("REGISTRAR", false)){
                    tv_mensaje_con_eventos.setText(R.string.toca_resgitrar_evento);
                    tv_mensaje_con_eventos.setTextColor(Color.BLACK);
                    tv_num_dia.setTextColor(Color.BLACK);
                    tv_nom_dia.setTextColor(Color.BLACK);

                }else {
                    tv_mensaje_con_eventos.setVisibility(View.GONE);
                }
            }
            iniciarAdaptador();

        }catch (Exception ignored){
            tv_mensaje_con_eventos.setVisibility(View.GONE);
            tv_mensaje_no_eventos.setVisibility(View.VISIBLE);
            if (getIntent().getBooleanExtra("REGISTRAR", false)){
                tv_mensaje_no_eventos.setText(R.string.no_eventos_registra);
                tv_mensaje_no_eventos.setTextColor(Color.BLACK);
                tv_num_dia.setTextColor(Color.BLACK);
                tv_nom_dia.setTextColor(Color.BLACK);
            }else {
                tv_mensaje_no_eventos.setText("No hay eventos registrados este día.");
            }
        }

        if (getIntent().getBooleanExtra("ES_HOY", false)){
            tv_num_dia.setTextColor(getResources().getColor(R.color.colorAcento));
            tv_nom_dia.setTextColor(getResources().getColor(R.color.colorAcento));
            tv_mensaje_con_eventos.setTextColor(getResources().getColor(R.color.colorAcento));
            tv_mensaje_no_eventos.setTextColor(getResources().getColor(R.color.colorAcento));
        }
    }

    private void iniciarAdaptador() {
        adaptador = new EventosAdaptador(listaEventos, DialogEventosHelzio.this);
        rvEventos.setAdapter(adaptador);
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

    private String horasATetxto(int numero) {
        String am_pm, st_min, st_hora;

        int hora = (numero / 2) + 7;
        if (hora > 12) {
            hora = hora - 12;
            am_pm = " PM";
        } else {
            am_pm = " AM";
        }

        if (hora < 10) {
            st_hora = "0" + hora;
        } else {
            st_hora = "" + hora;
        }

        if (numero % 2 == 0) {
            st_min = "00";
        } else {
            st_min = "30";
        }

        return st_hora + ":" + st_min + am_pm;

    }

    @Override
    public void onBackPressed() {
        dismiss(null);
    }

    public void dismiss(View view) {
        if (animando){
            finish();
             overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }else {
            finishAfterTransition();
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){

            rvEventos.getChildAt(data.getIntExtra("POSITION", 0)).animate().scaleX(0).scaleY(0).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    animando = true;
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    adaptador.removeItemAtPosition(data.getIntExtra("POSITION", 0));
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Fade fade = new Fade();
                            getWindow().setSharedElementReturnTransition(null);
                            getWindow().setExitTransition(fade);
                            animando = false;
                        }
                    },500);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });


        }
    }
}
