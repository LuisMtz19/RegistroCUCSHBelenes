package ccv.checkhelzio.registrocucshbelenes.ui;

import android.app.Activity;
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
import ccv.checkhelzio.registrocucshbelenes.transitions.FabTransform;

/**
 * Created by check on 09/09/2016.
 */

public class DateDialogHelzio extends Activity {

    @BindView(R.id.conteDialog)
    DatePicker datePicker;
    @BindView(R.id.conte)
    ViewGroup conte;
    @BindView(R.id.bt_dialog_aceptar)
    Button aceptar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dialog_no_eventos);
        ButterKnife.bind(this);

        Calendar calendar = Calendar.getInstance();
        calendar.set(2016, 0, 1);
        datePicker.setMinDate(calendar.getTimeInMillis());
        datePicker.setFirstDayOfWeek(Calendar.MONDAY);

        FabTransform.setup(this, conte);
        getWindow().getSharedElementEnterTransition();
    }
    
    @Override
    public void onBackPressed() {
        dismiss(null);
    }

    @OnClick (R.id.bt_dialog_cancenlar)
    public void dismiss(View view) {
        finishAfterTransition();
    }
}
