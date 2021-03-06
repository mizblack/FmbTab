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
import com.eye3.golfpay.common.AppDef;
import com.eye3.golfpay.common.Global;

public class GpsView extends View {

    public interface IDistanceListener {
        void onDistance(double distance1, double distance2);
    }

    enum ActiveObject{
        eNone,
        eObject1,
        eObject2
    };

    Paint paint = new Paint();
    ActiveObject activeObject = ActiveObject.eNone;
    Bitmap bmObject1;
    Bitmap bmObject2;
    Bitmap bmHole;

    Rect rect;
    Rect rcObject1;
    Rect rcObject2;
    Rect rcHole;
    Rect rcDestObject1;
    Rect rcDestObject2;
    Rect rcDestHole;
    Size sizeObject1;
    Size sizeObject2;
    Size sizeHole;
    Point ptObject1;
    Point ptObject2;
    Point ptHole;
    Point ptTouchPos;
    int meter = 0;

    double ratio = 592.0 / 1417.0;
    private IDistanceListener iDistanceListener;
    public GpsView(Context context) {
        super(context);
    }

    public GpsView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint.setAlpha(255);
        paint.setStrokeWidth(3);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setPathEffect(new DashPathEffect(new float[]{10,4},50));
        paint.setAntiAlias(true);

        bmObject1 = BitmapFactory.decodeResource(getResources(), R.drawable.spot1);
        bmObject2 = BitmapFactory.decodeResource(getResources(), R.drawable.spot2);
        bmHole = BitmapFactory.decodeResource(getResources(), R.drawable.goal);

        rcObject1 = new Rect(0, 0, bmObject1.getWidth(), bmObject1.getHeight());
        rcObject2 = new Rect(0, 0, bmObject2.getWidth(), bmObject2.getHeight());
        rcHole = new Rect(0, 0, bmHole.getWidth(), bmHole.getHeight());
        sizeObject1 = new Size(70, 70);
        sizeObject2 = new Size(70, 70);
        sizeHole = new Size(50, 50);
        ptObject1 = new Point(0, 0);
        ptObject2 = new Point(700, 200);
        ptHole = new Point(1400, 35);
    }

    public GpsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public void setHolePos(Point posHole, int meter, int mapOrgWidth) {
        this.meter = meter;
        float ratio = (float)mapOrgWidth / (float)rect.width();
        int x = (int)((float)rect.width() * (posHole.x / 100.0f));
        int y = (int)((float)rect.height() * (posHole.y / 100.0f));
        ptHole = new Point((int)(x * ratio), (int)(y * ratio));
        //ptObject2 = new Point(rect.width()/2, rect.height()/2);
        invalidate();
    }

    public void setObject1Pos(Point pt) {
        int x = (int)((float)rect.width() * (pt.x / 100.0f));
        int y = (int)((float)rect.height() * (pt.y / 100.0f));
        ptObject1 = new Point(x,y);
        invalidate();
    }

    public void setDistanceListener(IDistanceListener listener) {
        iDistanceListener = listener;
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (rect == null)
            rect = canvas.getClipBounds();

        calcObject1(ptObject1);
        calcObject2(ptObject2);
        calcHole();
        drawLine(canvas);

        canvas.drawBitmap(bmObject1, rcObject1, rcDestObject1, null);
        canvas.drawBitmap(bmObject2, rcObject2, rcDestObject2, null);
        canvas.drawBitmap(bmHole, rcHole, rcDestHole, null);

        double distance1 = getDistance(rcDestObject1.centerX(), rcDestObject1.centerY(), rcDestObject2.centerX(), rcDestObject2.centerY());
        double distance2 = getDistance(rcDestObject2.centerX(), rcDestObject2.centerY(), rcDestHole.centerX(), rcDestHole.centerY());
        float ratio = (float)meter/(float)rect.width();

        iDistanceListener.onDistance(distance1*ratio, distance2*ratio);
        drawDistance(canvas, distance1*ratio, distance2*ratio);
        //Log.d("sangbong_log", String.format("distance1: %f, distance2: %f", distance1, distance2));
    }

    private void calcObject1(Point pt) {
        rcDestObject1 = new Rect(pt.x, pt.y, pt.x + sizeObject1.getWidth(), pt.y + sizeObject1.getHeight());
    }

    private void calcObject2(Point pt) {
        rcDestObject2 = new Rect(pt.x, pt.y, pt.x + sizeObject2.getWidth(), pt.y + sizeObject2.getHeight());
    }

    private void calcHole() {
        int x = ptHole.x;// - sizeHole.getWidth()/2;
        int y = ptHole.y - 30;//sizeHole.getHeight()/2;
        rcDestHole = new Rect(x, y, x + sizeHole.getWidth(), y + sizeHole.getHeight());
    }

    private void drawLine(Canvas canvas) {

        canvas.drawLine(rcDestObject1.centerX(), rcDestObject1.centerY(), rcDestObject2.centerX(), rcDestObject2.centerY(), paint);
        canvas.drawLine(rcDestObject2.centerX(), rcDestObject2.centerY(), rcDestHole.centerX(), rcDestHole.centerY(), paint);
    }

    private void drawDistance(Canvas canvas, double distance1, double distance2) {

        int left = rcDestObject2.left+5;
        int top = rcDestObject2.bottom;
        int right = left + 80;
        int bottom = top + 10 + 80;
        Rect rc = new Rect(left, top, right, bottom);

        String text1 = String.format("%dM", (int) distance1);
        String text2 = String.format("%dM", (int) distance2);

        if (Global.isYard) {
            int yard1 = (int)distance1;
            if (Global.isYard) {
                yard1 = AppDef.MeterToYard(yard1);
            }

            int yard2 = (int)distance2;
            if (Global.isYard) {
                yard2 = AppDef.MeterToYard(yard2);
            }

            text1 = String.format("%dYD", yard1);
            text2 = String.format("%dYD", yard2);
        }

        if (Global.isYard) {
            int yard1 = (int)distance1;
            if (Global.isYard) {
                yard1 = AppDef.MeterToYard(yard1);
            }

            int yard2 = (int)distance2;
            if (Global.isYard) {
                yard2 = AppDef.MeterToYard(yard2);
            }

            text1 = String.format("%dYD", yard1);
            text2 = String.format("%dYD", yard2);
        }

        if (rcDestObject2.right > rect.right- 50) {
            rc.offset(-20, 0);
        }

        if (rcDestObject2.bottom > rect.bottom - 100) {
            rc.offset(0, -(rcDestObject2.height() + rc.height()+10));
        }

        int marginTop = 6;
        int y = drawText(canvas, "??????", 16, rc, Color.parseColor("#ffffff"), Color.parseColor("#cd00abc5"), marginTop).height();

        rc.offset(0, y);
        y = drawText(canvas, "", 20, rc, Color.parseColor("#ffffff"), Color.parseColor("#cd000000"), 2).height();
        rc.offset(0, y);
        marginTop = 8;
        y = drawText(canvas, text1, 20, rc, Color.parseColor("#ffffff"), Color.parseColor("#cd000000"), marginTop).height();

        rc.offset(0, y);
        y = drawText(canvas, text2, 20, rc, Color.parseColor("#ffffff"), Color.parseColor("#cd000000"), marginTop).height();
        rc.offset(0, y);
        drawText(canvas, "", 20, rc, Color.parseColor("#ffffff"), Color.parseColor("#cd000000"), 2).height();
    }

    private Rect drawText(Canvas canvas, String text, int textSize, Rect rc, int textColor, int backgroundColor, int marginTop) {

        Paint Textpaint = new Paint();
        Rect textBound = new Rect();
        Textpaint.setTextSize(textSize);
        Textpaint.setColor(textColor);
        Textpaint.getTextBounds(text, 0, text.length(), textBound);
        Textpaint.setAntiAlias(true);

        Paint rectPaint = new Paint();
        rectPaint.setColor(backgroundColor);

        Rect backgroundRect = new Rect();
        backgroundRect.left = rc.left;
        backgroundRect.right = rc.right;
        backgroundRect.top = rc.top;

        int x = rc.left-(textBound.width()/2) + (rc.width()/2);
        backgroundRect.bottom = rc.top + textBound.height() + (marginTop*2);
        canvas.drawRect(backgroundRect, rectPaint);
        canvas.drawText(text, x,rc.top+textBound.height() + marginTop, Textpaint);

        return backgroundRect;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int eventX = (int)event.getX();
        int eventY = (int)event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                if (rcDestObject1.contains(eventX, eventY)) {
                    activeObject = ActiveObject.eObject1;
                    ptTouchPos = new Point(eventX-ptObject1.x, eventY-ptObject1.y);
                    //Toast.makeText(getContext(), "?????????", Toast.LENGTH_SHORT).show();
                }

                if (rcDestObject2.contains(eventX, eventY)) {
                    activeObject = ActiveObject.eObject2;
                    ptTouchPos = new Point(eventX-ptObject2.x, eventY-ptObject2.y);
                    //Toast.makeText(getContext(), "?????????", Toast.LENGTH_SHORT).show();
                }

                break;
            case MotionEvent.ACTION_MOVE :

                if (activeObject == ActiveObject.eObject1) {
                    move(ptObject1, rcDestObject1, eventX, eventY);

                } else if (activeObject == ActiveObject.eObject2) {
                    move(ptObject2, rcDestObject2, eventX, eventY);
                }

                break;

            case MotionEvent.ACTION_UP :
                activeObject = ActiveObject.eNone;
                break;
            default:
                break;
        }

        return true;
    }

    private void move(Point ptObject, Rect rcDest, int x, int y) {
        ptObject.x =  x - ptTouchPos.x;
        ptObject.y = y - ptTouchPos.y;

        if (ptObject.x < 50)
            ptObject.x = 50;
        if (ptObject.y < 0)
            ptObject.y = 0;
        if (ptObject.x + rcDest.width() > rect.width())
            ptObject.x = rect.width() - rcDest.width();
        if (ptObject.y + rcDest.height() > rect.height())
            ptObject.y = rect.height() - rcDest.height();

        invalidate();
    }

    private double getDistance(int x, int y, int x1, int y1) {
        return Math.sqrt(Math.pow(Math.abs(x1-x), 2) + Math.pow(Math.abs(y1-y), 2));
    }
}
