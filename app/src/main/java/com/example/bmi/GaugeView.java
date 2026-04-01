package com.example.bmi;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;

public class GaugeView extends View {

    private float bmi = 20.5f;

    private static final int[] ZONE_COLORS = {
            0xFF1A3A8A, 0xFF2B5AC8, 0xFF5B8FE8,
            0xFF4CAF50,
            0xFFFFC107,
            0xFFFF6D00,
            0xFFF44336,
            0xFFB71C1C
    };

    // Each zone: [bmiStart, bmiEnd, colorIndex]
    private static final float[][] ZONES = {
            {0f,    16f,   0},
            {16f,   17f,   1},
            {17f,   18.5f, 2},
            {18.5f, 25f,   3},
            {25f,   30f,   4},
            {30f,   35f,   5},
            {35f,   40f,   6},
            {40f,   50f,   7},
    };

    private static final float BMI_MIN = 0f;
    private static final float BMI_MAX = 50f;

    private Paint arcPaint   = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint needlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint dotPaint   = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint textPaint  = new Paint(Paint.ANTI_ALIAS_FLAG);
    private RectF oval = new RectF();

    public GaugeView(Context context) {
        super(context);
        init();
    }

    public GaugeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        arcPaint.setStyle(Paint.Style.STROKE);

        needlePaint.setColor(0xFF1A1A2E);
        needlePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        needlePaint.setStrokeCap(Paint.Cap.ROUND);

        dotPaint.setColor(0xFF1A1A2E);
        dotPaint.setStyle(Paint.Style.FILL);

        textPaint.setColor(0xFF888888);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    public void setBmi(float bmi) {
        this.bmi = bmi;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float w = getWidth();
        float h = getHeight();
        float cx = w / 2f;
        float cy = h * 0.85f;

        float radius = Math.min(w * 0.42f, h * 0.82f);
        float strokeW = radius * 0.20f;
        arcPaint.setStrokeWidth(strokeW);

        oval.set(cx - radius, cy - radius, cx + radius, cy + radius);

        // Draw color zones
        for (float[] zone : ZONES) {
            float startAngle = bmiToAngle(zone[0]);
            float sweepAngle = bmiToAngle(zone[1]) - startAngle;
            arcPaint.setColor(ZONE_COLORS[(int) zone[2]]);
            canvas.drawArc(oval, startAngle, sweepAngle, false, arcPaint);
        }

        // Tick labels
        textPaint.setTextSize(radius * 0.12f);
        float[] ticks = {16f, 18.5f, 25f, 30f, 40f};
        for (float t : ticks) {
            double angleRad = Math.toRadians(bmiToAngle(t));
            float lx = (float) (cx + (radius + strokeW * 0.75f) * Math.cos(angleRad));
            float ly = (float) (cy + (radius + strokeW * 0.75f) * Math.sin(angleRad));
            String label = (t == (int) t) ? String.valueOf((int) t) : String.valueOf(t);
            canvas.drawText(label, lx, ly + textPaint.getTextSize() / 3f, textPaint);
        }

        // Needle
        double needleRad = Math.toRadians(bmiToAngle(bmi));
        float needleLen = radius * 0.85f;
        float nx = (float) (cx + needleLen * Math.cos(needleRad));
        float ny = (float) (cy + needleLen * Math.sin(needleRad));
        needlePaint.setStrokeWidth(radius * 0.04f);
        canvas.drawLine(cx, cy, nx, ny, needlePaint);

        // Center dot
        canvas.drawCircle(cx, cy, radius * 0.065f, dotPaint);
    }

    private float bmiToAngle(float val) {
        float fraction = (val - BMI_MIN) / (BMI_MAX - BMI_MIN);
        fraction = Math.max(0f, Math.min(1f, fraction));
        return 180f + fraction * 180f;
    }
}
