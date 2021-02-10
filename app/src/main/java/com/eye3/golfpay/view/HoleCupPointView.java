package com.eye3.golfpay.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Size;
import android.view.MotionEvent;
import android.view.View;

import com.eye3.golfpay.R;

public class HoleCupPointView extends View {

    Paint paint = new Paint();
    Rect rect;
    Point ptHole;

    public HoleCupPointView(Context context) {
        super(context);
    }
    public HoleCupPointView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint.setAlpha(255);
        paint.setStrokeWidth(4);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setPathEffect(new DashPathEffect(new float[]{10,4},50));
        paint.setAntiAlias(true);
        ptHole = new Point(0,0);
    }

    public HoleCupPointView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public void setHolePos (Point posHole, int meter, int mapOrgWidth) {
        float ratio = (float)mapOrgWidth / (float)rect.width();
        int x = (int)((float)rect.width() * (posHole.x / 100.0f));
        int y = (int)((float)rect.height() * (posHole.y / 100.0f));
        ptHole = new Point((int)(x * ratio), (int)(y * ratio));
        invalidate();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (rect == null)
            rect = canvas.getClipBounds();

        paint.setColor(Color.parseColor("#d63232"));
        canvas.drawCircle(ptHole.x, ptHole.y, 12, paint);
    }
}
