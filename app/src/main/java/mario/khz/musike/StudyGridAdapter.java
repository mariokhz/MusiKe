package mario.khz.musike;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Map;

public class StudyGridAdapter {
    private Context context;
    private String[] instrumentos;
    private Map<String, String> displayNameMap;

    public StudyGridAdapter(Context context, String[] instrumentos, Map<String, String> displayNameMap) {
        this.context = context;
        this.instrumentos = instrumentos;
        this.displayNameMap = displayNameMap;
    }

    public void populate(GridLayout grid) {
        grid.removeAllViews();
        int count = Math.min(instrumentos.length, 20);
        for (int i = 0; i < count; i++) {
            final String file = instrumentos[i];
            int drawableRes = context.getResources().getIdentifier(file, "drawable", context.getPackageName());
            int rawRes = context.getResources().getIdentifier(file, "raw", context.getPackageName());

            // Crear contenedor para cada celda
            LinearLayout cell = new LinearLayout(context);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            params.setMargins(8, 8, 8, 8);
            cell.setLayoutParams(params);
            cell.setOrientation(LinearLayout.VERTICAL);
            cell.setGravity(Gravity.CENTER);

            // Crear botón con el ícono del instrumento
            ImageButton btn = new ImageButton(context);
            // set fixed square button size (80dp) for 1:1 ratio and smaller icons
            int buttonSizePx = (int) (context.getResources().getDisplayMetrics().density * 80 + 0.5f);
            LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(buttonSizePx, buttonSizePx);
            btnParams.gravity = Gravity.CENTER;
            btn.setLayoutParams(btnParams);
            btn.setBackgroundResource(R.drawable.button_selector);
            // set button background color to light blue
            btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D681B0")));
            btn.setImageResource(drawableRes);
            btn.setAdjustViewBounds(true);
            btn.setScaleType(ImageButton.ScaleType.CENTER_INSIDE);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MediaPlayer mp = MediaPlayer.create(context, rawRes);
                    mp.start();
                }
            });

            // Crear etiqueta con el nombre del instrumento
            TextView label = new TextView(context);
            label.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            label.setText(displayNameMap.get(file));
            label.setGravity(Gravity.CENTER);
            label.setTextColor(Color.WHITE);

            cell.addView(btn);
            cell.addView(label);
            grid.addView(cell);
        }
    }
}
