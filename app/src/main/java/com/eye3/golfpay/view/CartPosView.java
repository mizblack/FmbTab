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
import com.eye3.golfpay.model.gps.CartPos;
import com.eye3.golfpay.model.gps.GpsInfo;

import java.util.ArrayList;

public class CartPosView extends View {

    enum HolePosition{
        ePrevHole,
        eCurrentHole,
        eNextHole
    };


    Paint paint = new Paint();
    //ActiveObject activeObject = ActiveObject.eNone;
    Bitmap bmMyCartObject;
    Bitmap bmCartObject;
    Rect rect;
    Size sizeCartObject;
    int startMargin = 60;
    int holeY = 114;
    int cartY = 81;
    int holeLineWidth = 0;
    Rect rcCartObject;
    ArrayList<CartPos> ptPrevHoleCart = new ArrayList<>();
    ArrayList<CartPos> ptCurrentHoleCart = new ArrayList<>();
    ArrayList<CartPos> ptNextHoleCart = new ArrayList<>();

    double ratio = 592.0 / 1417.0;
    public CartPosView(Context context) {
        super(context);
    }

    public CartPosView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint.setAlpha(255);
        paint.setStrokeWidth(3);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setPathEffect(new DashPathEffect(new float[]{10,4},50));
        paint.setAntiAlias(true);

        bmMyCartObject = BitmapFactory.decodeResource(getResources(), R.drawable.mycart);
        bmCartObject = BitmapFactory.decodeResource(getResources(), R.drawable.cart);

        rcCartObject = new Rect(0, 0, bmCartObject.getWidth(), bmCartObject.getHeight());
        sizeCartObject = new Size(45, 31);
    }

    public CartPosView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

   public void addCurrentCart(CartPos cart) {
       ptCurrentHoleCart.add(cart);
   }

   public void addPrevCart(CartPos cart) {
       ptPrevHoleCart.add(cart);
   }

   public void addNextCart(CartPos cart) {
       ptNextHoleCart.add(cart);
   }

   public void clearCart() {
       ptCurrentHoleCart.clear();
       ptPrevHoleCart.clear();
       ptNextHoleCart.clear();
   }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (rect == null)
            rect = canvas.getClipBounds();

        holeLineWidth = (rect.width() - startMargin*2) / 3; //홀 양쪽 마진만 띄고 3으로 나눈 값이 각 홀 라인의 길이
        drawHoleLine(canvas);

        for (CartPos cartPos: ptPrevHoleCart) {
            String cartName = cartPos.getCart_no() + "." + cartPos.getName();
            drawCart(canvas, bmCartObject, cartPos.getPercent(), cartName, HolePosition.ePrevHole, false);
        }

        for (CartPos cartPos: ptNextHoleCart) {
            String cartName = cartPos.getCart_no() + "." + cartPos.getName();
            drawCart(canvas, bmCartObject, cartPos.getPercent(), cartName, HolePosition.eNextHole, false);
        }

        for (CartPos cartPos: ptCurrentHoleCart) {
            String cartName = cartPos.getCart_no() + "." + cartPos.getName();

            if (!cartPos.getGubun().equals("me"))
                drawCart(canvas, bmCartObject, cartPos.getPercent(), cartName, HolePosition.eCurrentHole, false);
        }

        //내꺼를 젤 위에 그리자~
        for (CartPos cartPos: ptCurrentHoleCart) {
            String cartName = cartPos.getCart_no() + "." + cartPos.getName();

            if (cartPos.getGubun().equals("me"))
                drawCart(canvas, bmMyCartObject, cartPos.getPercent(), cartName, HolePosition.eCurrentHole, true);
        }
    }

    private Rect calcCartPos(Point pt) {
        return new Rect(pt.x, pt.y, pt.x + sizeCartObject.getWidth(), pt.y + sizeCartObject.getHeight());
    }

    private void drawHoleLine(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStrokeWidth(4);
        paint.setColor(Color.parseColor("#9aabb1"));
        canvas.drawLine(startMargin, holeY, startMargin+holeLineWidth, holeY, paint);
        paint.setColor(Color.parseColor("#00abc5"));
        canvas.drawLine(startMargin+holeLineWidth, holeY, startMargin+holeLineWidth+holeLineWidth, holeY, paint);
        paint.setColor(Color.parseColor("#9aabb1"));
        canvas.drawLine(startMargin+holeLineWidth+holeLineWidth, holeY, startMargin+holeLineWidth+holeLineWidth+holeLineWidth, holeY, paint);
    }

    private void drawCart(Canvas canvas, Bitmap bmCart, int percent, String name, HolePosition holePosition, boolean me) {

        int start = getHoleStartPos(holePosition);
        start += ((float)holeLineWidth * (percent/100.0f)) - (sizeCartObject.getWidth()/2);

        Rect rc = new Rect(start, cartY, start + sizeCartObject.getWidth(), cartY + sizeCartObject.getHeight());
        canvas.drawBitmap(bmCart, rcCartObject, rc, null);

        Paint paint = new Paint();
        if (me)
            paint.setColor(Color.parseColor("#00abc5"));
        else
            paint.setColor(Color.parseColor("#9aabb1"));

        Rect textBound = new Rect();
        paint.setTextSize(20);
        paint.getTextBounds(name, 0, name.length(), textBound);
        paint.setAntiAlias(true);

        //canvas.drawLine(startMargin, holeY, startMargin+holeLineWidth, holeY, paint);

        int x = rc.left-(textBound.width()/2) + (sizeCartObject.getWidth()/2);
        canvas.drawText(name,  x,rect.bottom-10, paint);
    }

    private int getHoleStartPos(HolePosition holePosition) {

        switch (holePosition) {
            case ePrevHole:
                return startMargin;
            case eCurrentHole:
                return startMargin+holeLineWidth;
            case eNextHole:
                return startMargin+holeLineWidth+holeLineWidth;
        }

        return 0;
    }
}
