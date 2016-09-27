package ccv.checkhelzio.registrocucshbelenes.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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

    private final String auditorio1 = "Auditorio Salvador Allende";
    private final String auditorio2 = "Auditorio Silvano Barba";
    private final String auditorio3 = "Auditorio Carlos Ramírez";
    private final String auditorio4 = "Auditorio Adalberto Navarro";
    private final String auditorio5 = "Sala de Juicios Orales Mariano Otero";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_lista_eventos);
        ButterKnife.bind(this);
        postponeEnterTransition();

        tv_num_dia.setText(getIntent().getStringExtra("DIA_MES"));
        String nom = getIntent().getStringExtra("NOMBRE_DIA").substring(0,3) + ".";
        tv_nom_dia.setText(nom);

        rvEventos.setHasFixedSize(true);
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
                    listaEventos.add(new Eventos(tag.split("¦")[zz].split("::")[4].trim(), tag.split("¦")[zz].split("::")[7].trim(), nombreAuditorio(tag.split("¦")[zz].split("::")[5].trim()),
                            horasATetxto(Integer.valueOf(tag.split("¦")[zz].split("::")[0].trim())) + " - " + horasATetxto(Integer.valueOf(tag.split("¦")[zz].split("::")[1].trim()) + 1),
                            fondoAuditorio(tag.split("¦")[zz].split("::")[5].trim())));
                }
            }

            if (listaEventos.size() > 0){
                tv_mensaje_no_eventos.setVisibility(View.GONE);
                if (getIntent().getBooleanExtra("REGISTRAR", false)){
                    tv_mensaje_con_eventos.setText("Presiona aquí para registrar otro evento.");
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
                tv_mensaje_no_eventos.setText("No hay eventos registrados este día. Presiona para registrar uno.");
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
        }
    }

    private void iniciarAdaptador() {
        EventosAdaptador adaptador = new EventosAdaptador(listaEventos);
        rvEventos.setAdapter(adaptador);
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
}
