package ccv.checkhelzio.registrocucshbelenes.ui;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayout;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;

import ccv.checkhelzio.registrocucshbelenes.R;

import static ccv.checkhelzio.registrocucshbelenes.ui.Principal.calendarioActualizarDiasMes;
import static ccv.checkhelzio.registrocucshbelenes.ui.Principal.eventos2016;
import static ccv.checkhelzio.registrocucshbelenes.ui.Principal.irHoyDiaSemana;
import static ccv.checkhelzio.registrocucshbelenes.ui.Principal.irHoyMes;
import static ccv.checkhelzio.registrocucshbelenes.ui.Principal.irHoyNumeroDiaMes;

/**
 * Created by check on 09/09/2016.
 */

public class CalendarFragment extends Fragment {
    int fragVal;
    static CalendarFragment init(int val) {
        CalendarFragment truitonFrag = new CalendarFragment();
        // Supply val input as an argument.
        Bundle args = new Bundle();
        args.putInt("val", val);
        truitonFrag.setArguments(args);
        return truitonFrag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragVal = getArguments() != null ? getArguments().getInt("val") : 1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.aba_a, container,
                false);

        calendarioActualizarDiasMes.set(2016, 0, 1);
        calendarioActualizarDiasMes.set(Calendar.MONTH, fragVal);
        actualizarDiasDelMes((GridLayout) layout.findViewById(R.id.grid), (LinearLayout) layout.findViewById(R.id.aba_b));
        return layout;
    }


    private void actualizarDiasDelMes(GridLayout grid_layout, LinearLayout aba_b) {
        int diasemana = obtenerDiaSemana(calendarioActualizarDiasMes.get(Calendar.DAY_OF_WEEK));
        int actuDias_Año = calendarioActualizarDiasMes.get(Calendar.YEAR);
        int dia_inicial_del_mes;

        if (actuDias_Año == 2016) {
            dia_inicial_del_mes = calendarioActualizarDiasMes.get(Calendar.DAY_OF_YEAR);
        } else {
            dia_inicial_del_mes = calendarioActualizarDiasMes.get(Calendar.DAY_OF_YEAR);
            for (int x = 2016; x < actuDias_Año; x++) {
                Calendar cr = Calendar.getInstance();
                cr.set(x, 11, 31);
                dia_inicial_del_mes += cr.get(Calendar.DAY_OF_YEAR);
            }
        }

        for (int y = 0; y < diasemana; y++) {
            String st_nd = "";
            ((TextView) ((RelativeLayout) ((LinearLayout) grid_layout.getChildAt(y)).getChildAt(0)).getChildAt(0)).setText(st_nd);
            ((TextView) ((LinearLayout) grid_layout.getChildAt(y)).getChildAt(1)).setText(st_nd);
            ((TextView) ((LinearLayout) grid_layout.getChildAt(y)).getChildAt(2)).setText(st_nd);
            ((TextView) ((LinearLayout) grid_layout.getChildAt(y)).getChildAt(3)).setText(st_nd);
            ((LinearLayout) grid_layout.getChildAt(y)).getChildAt(1).setBackgroundColor(Color.TRANSPARENT);
            ((LinearLayout) grid_layout.getChildAt(y)).getChildAt(2).setBackgroundColor(Color.TRANSPARENT);
            ((LinearLayout) grid_layout.getChildAt(y)).getChildAt(3).setBackgroundColor(Color.TRANSPARENT);
        }

        for (int x = 1; x <= calendarioActualizarDiasMes.getActualMaximum(Calendar.DAY_OF_MONTH); x++) {
            String st_nd = "" + x;
            ((TextView) ((RelativeLayout) ((LinearLayout) grid_layout.getChildAt(x - 1 + diasemana)).getChildAt(0)).getChildAt(0)).setText(st_nd);

            String j = "";
            try {
                final String s = eventos2016[dia_inicial_del_mes - 1 + x];

                if (s != null) {
                    for (String ev_suelto : s.split("¦")) {
                        if (!ev_suelto.contains("X~")) {
                            if (!ev_suelto.equals("")) {
                                j += ev_suelto + "¦";
                            }
                        }
                    }
                }

                if (!j.trim().equals("")) {
                    int i = 1;
                    for (final String eve : j.split("¦")) {
                        if (i == 1 || i ==2) {
                            ((TextView) ((LinearLayout) grid_layout.getChildAt(x - 1 + diasemana)).getChildAt(i)).setText(eve.split("::")[4].trim());
                            ((LinearLayout) grid_layout.getChildAt(x - 1 + diasemana)).getChildAt(i).setBackgroundResource(getFondo(eve.split("::")[5].trim()));

                        } else if (i == 3) {
                            int cuantos = j.split("¦").length - 2;
                            String cu = "" + cuantos + " más";
                            ((TextView) ((LinearLayout) grid_layout.getChildAt(x - 1 + diasemana)).getChildAt(i)).setText(cu);
                            break;
                        }
                        i++;
                    }
                    grid_layout.getChildAt(x - 1 + diasemana).setTag(j);
                }
            } catch (Exception ignored) {

            }
        }

        for (int x = calendarioActualizarDiasMes.getActualMaximum(Calendar.DAY_OF_MONTH) + diasemana; x <= 35; x++) {
            String st_nd = "";
            ((TextView) ((RelativeLayout) ((LinearLayout) grid_layout.getChildAt(x)).getChildAt(0)).getChildAt(0)).setText(st_nd);

            ((TextView) ((LinearLayout) grid_layout.getChildAt(x)).getChildAt(1)).setText(st_nd);
            ((TextView) ((LinearLayout) grid_layout.getChildAt(x)).getChildAt(2)).setText(st_nd);
            ((TextView) ((LinearLayout) grid_layout.getChildAt(x)).getChildAt(3)).setText(st_nd);
            ((LinearLayout) grid_layout.getChildAt(x)).getChildAt(1).setBackgroundColor(Color.TRANSPARENT);
            ((LinearLayout) grid_layout.getChildAt(x)).getChildAt(2).setBackgroundColor(Color.TRANSPARENT);
            ((LinearLayout) grid_layout.getChildAt(x)).getChildAt(3).setBackgroundColor(Color.TRANSPARENT);
        }

        if (calendarioActualizarDiasMes.get(Calendar.MONTH) == irHoyMes) {
            TextView tv_fondo_hoy = ((TextView) ((RelativeLayout) ((LinearLayout) grid_layout.getChildAt(diasemana - 1 + irHoyNumeroDiaMes)).getChildAt(0)).getChildAt(0));
            tv_fondo_hoy.setBackgroundResource(R.drawable.fondo_hoy);
            tv_fondo_hoy.setTextColor(Color.WHITE);
            ((TextView) aba_b.getChildAt(obtenerDiaSemana(irHoyDiaSemana))).setTextColor((Integer) getAcentColor());
        }

        int z = diasemana + calendarioActualizarDiasMes.getActualMaximum(Calendar.DAY_OF_MONTH);
        if (z >= 36) {
            grid_layout.getChildAt(35).setVisibility(View.VISIBLE);
            grid_layout.getChildAt(36).setVisibility(View.VISIBLE);
            grid_layout.getChildAt(37).setVisibility(View.VISIBLE);
            grid_layout.getChildAt(38).setVisibility(View.VISIBLE);
            grid_layout.getChildAt(39).setVisibility(View.VISIBLE);
            grid_layout.getChildAt(40).setVisibility(View.VISIBLE);
            grid_layout.getChildAt(41).setVisibility(View.VISIBLE);
        } else {
            grid_layout.getChildAt(35).setVisibility(View.GONE);
            grid_layout.getChildAt(36).setVisibility(View.GONE);
            grid_layout.getChildAt(37).setVisibility(View.GONE);
            grid_layout.getChildAt(38).setVisibility(View.GONE);
            grid_layout.getChildAt(39).setVisibility(View.GONE);
            grid_layout.getChildAt(40).setVisibility(View.GONE);
            grid_layout.getChildAt(41).setVisibility(View.GONE);
        }
    }

    private int getFondo(String trim) {
        int a = 0;
        switch (trim) {
            case "1":
                a = (R.drawable.fondo1);
                break;
            case "2":
                a = (R.drawable.fondo2);
                break;
            case "3":
                a = (R.drawable.fondo3);
                break;
            case "4":
                a = (R.drawable.fondo1);
                break;
            case "5":
                a = (R.drawable.fondo2);
                break;
        }
        return a;
    }

    private Integer obtenerDiaSemana(int i) {
        switch (i) {
            //domingo
            case 1:
                i = 6;
                break;
            //lunes
            case 2:
                i = 0;
                break;
            //martes
            case 3:
                i = 1;
                break;
            //miercoles
            case 4:
                i = 2;
                break;
            //jueves
            case 5:
                i = 3;
                break;
            //viernes
            case 6:
                i = 4;
                break;
            //sabado
            case 7:
                i = 5;
                break;
        }
        return i;
    }

    public Object getAcentColor() {
        int colorAttr;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            colorAttr = android.R.attr.colorAccent;
        } else {
            //Get colorAccent defined for AppCompat
            colorAttr = getResources().getIdentifier("colorAccent", "attr", getActivity().getPackageName());
        }
        TypedValue outValue = new TypedValue();
        getActivity().getTheme().resolveAttribute(colorAttr, outValue, true);
        return outValue.data;
    }
}