package ccv.checkhelzio.registrocucshbelenes.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import ccv.checkhelzio.registrocucshbelenes.R;
import ccv.checkhelzio.registrocucshbelenes.transitions.ChangeBoundBackground;

public class DialogEventosHelzio extends Activity {

    @BindView(R.id.fondo)
    RelativeLayout fondo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_no_eventos);
        ButterKnife.bind(this);
        postponeEnterTransition();

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
