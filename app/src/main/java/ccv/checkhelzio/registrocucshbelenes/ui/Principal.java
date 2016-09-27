package ccv.checkhelzio.registrocucshbelenes.ui;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ccv.checkhelzio.registrocucshbelenes.R;
import ccv.checkhelzio.registrocucshbelenes.transitions.ChangeBoundBackground;
import ccv.checkhelzio.registrocucshbelenes.transitions.FabTransition;
import ccv.checkhelzio.registrocucshbelenes.util.DescargarBD;

public class Principal extends AppCompatActivity {

    @BindView(R.id.fab)
    ImageButton fab;
    @BindView(R.id.principal_coordinatorlayout)
    CoordinatorLayout coordinator;

    private ArrayList<String> lista_eventos = new ArrayList<>();
    private final String SHARED_TEXT_SIZE = "texto_compartido";
    private static boolean wifiConnected = false;
    private static boolean mobileConnected = false;
    protected static Calendar calendarioActualizarDiasMes;
    private Calendar calendarioMinimo;
    private Calendar calendarioIrHoy;

    private Handler handler;

    static final int HELZIO_DATE_DIALOG = 13;
    protected static int irHoyMes;
    protected static int irHoyDiaSemana;
    protected static int irHoyNumeroDiaMes;
    protected static int irHoyAño;
    private int irHoyNumeroMesAño;

    protected static String[] eventos2016;
    private String st_eventos_guardados = "";
    private String stNuevoId = "";

    private TextView tv_header2;
    private TextView tv_conexion;

    protected static ViewPager viewPager;
    private SharedPreferences prefs;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a);
        ButterKnife.bind(this);

        if (isScreenLarge()) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        iniciarObjetos();
        irHoy();
        actualizarFecha();
        checkNetworkConnection();
        new LlenarArrays().execute();
    }

    private void iniciarObjetos() {
        prefs = getSharedPreferences("EVENTOS CUCSH BELENES", Context.MODE_PRIVATE);
        st_eventos_guardados = prefs.getString("EVENTOS GUARDADOS", "");

        tv_header2 = (TextView) findViewById(R.id.header2);
        tv_conexion = (TextView) findViewById(R.id.conexion);

        calendarioMinimo = Calendar.getInstance();
        calendarioMinimo.set(2016, 0, 1);

        calendarioIrHoy = Calendar.getInstance();
        irHoyNumeroDiaMes = calendarioIrHoy.get(Calendar.DAY_OF_MONTH);
        irHoyDiaSemana = calendarioIrHoy.get(Calendar.DAY_OF_WEEK);
        irHoyAño = calendarioIrHoy.get(Calendar.YEAR);
        irHoyMes = calendarioIrHoy.get(Calendar.MONTH);

        calendarioActualizarDiasMes = Calendar.getInstance();
        calendarioActualizarDiasMes.set(Calendar.DAY_OF_MONTH, 1);

        handler = new Handler();
    }

    private void irHoy() {
        if (irHoyAño == 2016) {
            irHoyNumeroMesAño = calendarioIrHoy.get(Calendar.MONTH);
        } else {
            irHoyNumeroMesAño = calendarioIrHoy.get(Calendar.MONTH);
            for (int x = 2016; x < irHoyAño; x++) {
                irHoyNumeroMesAño += 12;
            }
        }
    }

    private void setListenners() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                calendarioActualizarDiasMes.set(2016, viewPager.getCurrentItem(), 1);
                actualizarFecha();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void actualizarFecha() {
        SimpleDateFormat format = new SimpleDateFormat("MMMM 'del' yyyy", Locale.forLanguageTag("es-MX"));
        String f = format.format(calendarioActualizarDiasMes.getTime());
        tv_header2.setText(capitalize(f));
    }

    private void checkNetworkConnection() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        if (activeInfo != null && activeInfo.isConnected()) {
            wifiConnected = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
            mobileConnected = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;
            if (wifiConnected) {
                new DescargarBD().execute("http://148.202.6.72/aplicacion/datos2.txt", Principal.this);
            } /*else if (mobileConnected) {
                Log.i(TAG, getString(R.string.mobile_connection));
            }*/
        }/* else {
            DESAPARECER FAB BUTTON CALENDARIO Y APARECER FAB BUTON ACTUALIZAR
            Log.i(TAG, getString(R.string.no_wifi_or_mobile));
        }*/
    }

    private String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }

    private boolean isScreenLarge() {
        final int screenSize = getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK;
        return screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE
                || screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    public void postDescargar(String s) {
        calendarioIrHoy = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("'Actualizado el 'd 'de' MMMM 'del' yyyy 'a las' h:mm a", Locale.forLanguageTag("es-MX"));
        tv_conexion.setText(format.format(calendarioIrHoy.getTime()));
        prefs.edit().putString("ACTUALIZACION", format.format(calendarioIrHoy.getTime())).apply();

        try {
            s = s.split("</form>")[1].trim();
        } catch (Exception ignored) {
        }
        if (!s.trim().equals(st_eventos_guardados.trim())) {
            st_eventos_guardados = s;
            prefs.edit().putString("EVENTOS GUARDADOS", st_eventos_guardados).apply();
            new LlenarArrays().execute();
        }
    }

    @OnClick(R.id.fab)
    public void fabClick() {
        Intent intent = new Intent(this, DateDialogHelzio.class);
        FabTransition.addExtras(intent, (Integer) getAcentColor(), R.drawable.ic_fecha);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, fab,
                getString(R.string.transition_date_dialog_helzio));
        startActivityForResult(intent, HELZIO_DATE_DIALOG, options.toBundle());
    }

    public void clickbotones(final View view) {

        if (!((TextView)((ViewGroup)((ViewGroup)view).getChildAt(0)).getChildAt(0)).getText().toString().equals("") ){
            Intent intent = new Intent(Principal.this, DialogEventosHelzio.class);
            intent.putExtra("NOMBRE_DIA", getResources().getResourceEntryName(view.getId()));
            intent.putExtra("DIA_MES", ((TextView)((ViewGroup)((ViewGroup)view).getChildAt(0)).getChildAt(0)).getText().toString());

            //BOLEAN PARA SABER SI ESTAMOS CLICKEANDO EL DIA DE HOY Y COLOREAR EL TEXTO EN EL DIALOG
            if (viewPager.getCurrentItem() == irHoyMes && Integer.parseInt(((TextView)((ViewGroup)((ViewGroup)view).getChildAt(0)).getChildAt(0)).getText().toString()) == irHoyNumeroDiaMes){
                intent.putExtra("ES_HOY", true);
            }else {
                intent.putExtra("ES_HOY", false);
            }

            //BOOLEAN PARA SABER SI SE PUEDE REGISTRAR O NO
            //SI ESTAMOS EN UN MES ANTERIOR AL ACTUAL NO SE PUEDE REGISTRAR
            if (viewPager.getCurrentItem() < irHoyMes){
                intent.putExtra("REGISTRAR", false);
            }
            //SI ESTAMOS EN EL MISMO MES
            else if (viewPager.getCurrentItem() == irHoyMes){
                //SI EL DIA DEL MES ES ANTERIOR AL DIA DE HOY NO PODEMOS REGISTRAR
                if (Integer.parseInt(((TextView)((ViewGroup)((ViewGroup)view).getChildAt(0)).getChildAt(0)).getText().toString()) < irHoyNumeroDiaMes){
                    intent.putExtra("REGISTRAR", false);
                }
                //SI EL DIA DEL MES ES HOY NO SE PUEDE AGENDAR DESPUES DE LAS 6PM
                else if (Integer.parseInt(((TextView)((ViewGroup)((ViewGroup)view).getChildAt(0)).getChildAt(0)).getText().toString()) == irHoyNumeroDiaMes){
                    calendarioIrHoy = Calendar.getInstance();
                    if (calendarioIrHoy.get(Calendar.HOUR) > 5){
                        intent.putExtra("REGISTRAR", false);
                    }else{
                        intent.putExtra("REGISTRAR", true);
                    }
                }
                //SI ES DESPUES DE HOY SI SE PEUDE REGISTRAR
                else{
                    intent.putExtra("REGISTRAR", true);
                }
            }
            //SI ES EN UN MES FUTURO SE PUEDE REGISTRAR
            else {
                intent.putExtra("REGISTRAR", true);
            }

            try {
                intent.putExtra("TAG", view.getTag().toString().replaceFirst("null", ""));
                intent.putExtra("DIA_SEMANA", ((TextView)((ViewGroup)((ViewGroup)view).getChildAt(0)).getChildAt(0)).getText().toString());
            }catch (Exception ignored){}
            final Rect startBounds = new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
            ChangeBoundBackground.addExtras(intent, getViewBitmap(view), startBounds);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Principal.this, view, "fondo");
            startActivity(intent, options.toBundle());
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

    class LlenarArrays extends AsyncTask<String, String, Void> {

        @Override
        protected Void doInBackground(String... aa12) {
            if (st_eventos_guardados.contains("</form>")) {
                st_eventos_guardados = st_eventos_guardados.split("</form>")[1].trim();
            }

            eventos2016 = new String[3660];
            lista_eventos = new ArrayList<>();

            if (st_eventos_guardados.trim().length() > 0) {
                for (String eventos_suelto : st_eventos_guardados.trim().split("¦")) {
                    if (!eventos_suelto.trim().equals("")) {
                        lista_eventos.add(eventos_suelto.trim() + "¦");
                    }

                }

                stNuevoId = "" + (lista_eventos.size() + 1);
                if (stNuevoId.length() == 1) {
                    stNuevoId = "000" + stNuevoId;
                } else if (stNuevoId.length() == 2) {
                    stNuevoId = "00" + stNuevoId;
                } else if (stNuevoId.length() == 3) {
                    stNuevoId = "0" + stNuevoId;
                }

                if (lista_eventos.size() > 0) {
                    for (int x = 0; x < lista_eventos.size(); x++) {
                        for (int y = Integer.valueOf(lista_eventos.get(x).trim().split("::")[2].trim()); y <= Integer.valueOf(lista_eventos.get(x).trim().split("::")[3].trim()); y++) {
                            eventos2016[y] += lista_eventos.get(x);
                        }

                        if (!lista_eventos.get(x).trim().split("::")[12].equals("NSR~~")) {
                            if (lista_eventos.get(x).trim().split("::")[12].split("~")[1].equals("")) {
                                int intervalo = Integer.valueOf(lista_eventos.get(x).trim().split("::")[3]) - Integer.valueOf(lista_eventos.get(x).trim().split("::")[2]);
                                if (lista_eventos.get(x).trim().split("::")[12].split("~")[0].contains("S")) {
                                    for (int wz = (Integer.valueOf(lista_eventos.get(x).trim().split("::")[2]) + (7 * Integer.valueOf(lista_eventos.get(x).trim().split("::")[12].split("~")[0].substring(0, 1)))); wz <= Integer.valueOf(lista_eventos.get(x).trim().split("::")[12].split("~")[2].split("¦")[0]); wz += (7 * Integer.valueOf(lista_eventos.get(x).trim().split("::")[12].split("~")[0].substring(0, 1)))) {
                                        eventos2016[wz] += lista_eventos.get(x);
                                        if (intervalo != 0) {
                                            for (int wzz = 1; wzz < intervalo + 1; wzz++) {
                                                if (wz + wzz <= Integer.valueOf(lista_eventos.get(x).trim().split("::")[12].split("~")[2].split("¦")[0])) {
                                                    eventos2016[wz + wzz] += lista_eventos.get(x);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            iniciarPager();
            setListenners();
        }
    }

    private void iniciarPager() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(new HelzioAdapter(getSupportFragmentManager()));
        viewPager.setPageMargin((int) getResources().getDimension(R.dimen.fab_size));
        viewPager.setCurrentItem(irHoyNumeroMesAño);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case HELZIO_DATE_DIALOG:
                if (resultCode == RESULT_OK) {
                    viewPager.setCurrentItem(data.getExtras().getInt("NUMERO_DE_MES"), true);
                }
                break;
        }
    }
}
