package ccv.checkhelzio.registrocucshbelenes.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ccv.checkhelzio.registrocucshbelenes.R;
import ccv.checkhelzio.registrocucshbelenes.transitions.FabTransition;

public class DateDialogHelzio extends Activity {

    @BindView(R.id.conteDialog) DatePicker datePicker;
    @BindView(R.id.conte) ViewGroup conte;
    @BindView(R.id.bt_dialog_aceptar) Button aceptar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_datepicker_transition);
        ButterKnife.bind(this);

        Calendar calendar = Calendar.getInstance();
        calendar.set(2016, 0, 1);
        datePicker.setMinDate(calendar.getTimeInMillis());
        datePicker.setFirstDayOfWeek(Calendar.MONDAY);

        FabTransition.setup(this, conte);
        getWindow().getSharedElementEnterTransition();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        dismiss();
    }

    @OnClick (R.id.bt_dialog_cancenlar)
    public void dismiss() {
        finishAfterTransition();
    }

    @OnClick (R.id.bt_dialog_aceptar)
    public void irDia(View view) {
        int mes;
        if (datePicker.getYear() == 2016) {
            mes = datePicker.getMonth();
        } else {
            mes = datePicker.getMonth();
            for (int x = 2016; x < datePicker.getYear(); x++) {
                mes += 12;
            }
        }

        Intent i = getIntent();
        i.putExtra("NUMERO_DE_MES", mes);
        setResult(RESULT_OK, i);
        dismiss();
    }
}
