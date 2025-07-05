package mario.khz.musike;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class CircleProgressView extends View {
    private Paint bgPaint;
    private Paint fgPaint;
    private float progress = 0f;

    public CircleProgressView(Context context) {
        super(context);
        init();
    }

    public CircleProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPaint.setColor(0x33000000);
        bgPaint.setStyle(Paint.Style.STROKE);
        bgPaint.setStrokeWidth(8f);
        fgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fgPaint.setColor(0xFFFFFFFF);
        fgPaint.setStyle(Paint.Style.STROKE);
        fgPaint.setStrokeWidth(12f);
    }

    public void setProgress(float fraction) {
        progress = fraction;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = getWidth();
        int h = getHeight();
        float radius = Math.min(w, h) * 0.4f;
        float cx = w / 2f;
        float cy = h / 2f;
        // draw background circle
        canvas.drawCircle(cx, cy, radius, bgPaint);
        // draw arc for progress
        float sweep = progress * 360f;
        canvas.drawArc(cx - radius, cy - radius, cx + radius, cy + radius, -90f, sweep, false, fgPaint);
    }
}
