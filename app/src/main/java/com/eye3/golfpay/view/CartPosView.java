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
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Size;
import android.view.MotionEvent;
import android.view.View;

import com.eye3.golfpay.R;
import com.eye3.golfpay.common.Global;
import com.eye3.golfpay.model.field.Hole;
import com.eye3.golfpay.model.gps.CartPos;
import com.eye3.golfpay.model.gps.GpsInfo;
import com.eye3.golfpay.util.BitmapUtils;

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
    Bitmap bmCartNameBox;
    Bitmap bmMyCartNameBox;

    Rect rect;
    Size sizeCartObject;
    Size sizeCartNameBox;
    int startMargin = 60;
    int holeY = 114;
    int cartY = 120;
    int holeLinePrevLength = 0;
    int holeLineCurrentLength = 0;
    int holeLineNextLength = 0;

    int prevHoleStartPos = 0;
    int currentHoleStartPos = 0;
    int nextHoleStartPos = 0;

    Hole currentHole = null, prevHole = null, nextHole = null;
    Rect rcCartObject, rcCartNameObject;
    ArrayList<CartPos> ptPrevHoleCart = new ArrayList<>();
    ArrayList<CartPos> ptCurrentHoleCart = new ArrayList<>();
    ArrayList<CartPos> ptNextHoleCart = new ArrayList<>();

    final float distance_to_pixel_ratio = 274.0f / 610.0f; //그냥 내가 정함.. 실제 미터를 픽셀로 잡는 기준

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

        bmCartNameBox = BitmapFactory.decodeResource(getResources(), R.drawable.cart_name_box);
        bmMyCartNameBox = BitmapFactory.decodeResource(getResources(), R.drawable.cart_name_box_me);

        rcCartObject = new Rect(0, 0, bmCartObject.getWidth(), bmCartObject.getHeight());
        rcCartNameObject = new Rect(0, 0, bmCartNameBox.getWidth(), bmCartNameBox.getHeight());
        sizeCartObject = new Size(20, 14);
        sizeCartNameBox = new Size(110, 32);
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

        drawHoleLine(canvas);

        int index = 0;
        for (CartPos cartPos: ptPrevHoleCart) {
            String cartName = cartPos.getCart_no() + "." + cartPos.getName();
            drawCart(canvas, bmCartObject, cartPos.getPercent(), cartName, HolePosition.ePrevHole, false, index++);
        }

        index = 0;
        for (CartPos cartPos: ptNextHoleCart) {
            String cartName = cartPos.getCart_no() + "." + cartPos.getName();
            drawCart(canvas, bmCartObject, cartPos.getPercent(), cartName, HolePosition.eNextHole, false, index++);
        }

        index = 0;
        for (CartPos cartPos: ptCurrentHoleCart) {
            String cartName = cartPos.getCart_no() + "." + cartPos.getName();

            if (!cartPos.getGubun().equals("me"))
                drawCart(canvas, bmCartObject, cartPos.getPercent(), cartName, HolePosition.eCurrentHole, false, index++);
        }

        //내꺼를 젤 위에 그리자~
        for (CartPos cartPos: ptCurrentHoleCart) {
            String cartName = cartPos.getCart_no() + "." + cartPos.getName();

            if (cartPos.getGubun().equals("me"))
                drawCart(canvas, bmMyCartObject, cartPos.getPercent(), cartName, HolePosition.eCurrentHole, true, index++);
        }
    }

    private Rect calcCartPos(Point pt) {
        return new Rect(pt.x, pt.y, pt.x + sizeCartObject.getWidth(), pt.y + sizeCartObject.getHeight());
    }

    public void setCurrentHoleInfo(Hole currentHole, Hole prevHole, Hole nextHole) {
        this.currentHole = currentHole;
        this.prevHole = prevHole;
        this.nextHole = nextHole;
    }

    private void drawHoleLine(Canvas canvas) {

        if (currentHole == null) {
            return;
        }

        ////////////////////////////////////////////////////////////////////////////////////////////
        //* Prev Hole Line
        if (prevHole == null) {
            holeLinePrevLength = (int)(350 * distance_to_pixel_ratio);
        } else {
            holeLinePrevLength = (int)(prevHole.hole_length * distance_to_pixel_ratio);
        }

        Paint paint = new Paint();
        paint.setStrokeWidth(4);
        paint.setColor(Color.parseColor("#9aabb1"));
        prevHoleStartPos = startMargin;
        canvas.drawLine(prevHoleStartPos, holeY, startMargin+holeLinePrevLength, holeY, paint);

        ////////////////////////////////////////////////////////////////////////////////////////////
        //* Current Hole Line
        holeLineCurrentLength = (int)(currentHole.hole_length * distance_to_pixel_ratio);

        int distance_between_hole = (int)(currentHole.distance_between_hole * distance_to_pixel_ratio);
        currentHoleStartPos = prevHoleStartPos+distance_between_hole+holeLinePrevLength;

        paint.setColor(Color.parseColor("#00abc5"));
        canvas.drawLine(currentHoleStartPos, holeY, currentHoleStartPos+holeLineCurrentLength, holeY, paint);

        ////////////////////////////////////////////////////////////////////////////////////////////
        //* Next Hole Line
        if (nextHole == null) {
            holeLineNextLength = (int)(350 * distance_to_pixel_ratio);
        } else {
            holeLineNextLength = (int)(nextHole.hole_length * distance_to_pixel_ratio);
        }

        distance_between_hole = (int)(nextHole.distance_between_hole * distance_to_pixel_ratio);
        nextHoleStartPos = currentHoleStartPos+distance_between_hole+holeLineCurrentLength;
        paint.setColor(Color.parseColor("#9aabb1"));
        canvas.drawLine(nextHoleStartPos, holeY, nextHoleStartPos+holeLineNextLength, holeY, paint);
    }

    private void drawCart(Canvas canvas, Bitmap bmCart, int percent, String name, HolePosition holePosition, boolean me, int index) {

        boolean bTopMode = index % 2 == 0;

        cartY = holeY-sizeCartObject.getHeight()-3;
        int start = getHoleStartPos(holePosition);
        int holeLineLength = getHoleLineLength(holePosition);
        start += ((float)holeLineLength * (percent/100.0f)) - (sizeCartObject.getWidth()/2);

        Rect rc = new Rect(start, cartY, start + sizeCartObject.getWidth(), cartY + sizeCartObject.getHeight());

        if (bTopMode)
            canvas.drawBitmap(bmCart, rcCartObject, rc, null);
        else {
            int y = cartY+sizeCartObject.getHeight()+5;
            rc = new Rect(start, y,
                    start + sizeCartObject.getWidth(), y + sizeCartObject.getHeight());
            Bitmap inverseCartBitmap = BitmapUtils.inversionBitmap(bmCart, 3);
            canvas.drawBitmap(inverseCartBitmap, rcCartObject, rc, null);
        }

        /////////////////////////////////////////////////////////////////////////////////
        // * draw Cart Name

        int x = rc.centerX() - (sizeCartNameBox.getWidth()/2);
        int y = cartY-40;
        Rect rcName = new Rect(x, y, x + sizeCartNameBox.getWidth(), y + sizeCartNameBox.getHeight());

        if (bTopMode) {
            if (me)
                canvas.drawBitmap(bmMyCartNameBox, rcCartNameObject, rcName, null);
            else
                canvas.drawBitmap(bmCartNameBox, rcCartNameObject, rcName, null);
        }
        else {
            y = cartY+sizeCartObject.getHeight()+30;
            rcName = new Rect(x, y, x + sizeCartNameBox.getWidth(), y + sizeCartNameBox.getHeight());

            if (me)
                canvas.drawBitmap(bmMyCartNameBox, rcCartNameObject, rcName, null);
            else
                canvas.drawBitmap(bmCartNameBox, rcCartNameObject, rcName, null);
        }

        Paint paint = new Paint();
        if (me)
            paint.setColor(Color.parseColor("#00abc5"));
        else
            paint.setColor(Color.parseColor("#9aabb1"));

        Rect textBound = new Rect();
        paint.setTextSize(16);
        paint.getTextBounds(name, 0, name.length(), textBound);
        paint.setAntiAlias(true);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        paint.setColor(Color.WHITE);

        //canvas.drawLine(startMargin, holeY, startMargin+holeLineWidth, holeY, paint);

        x = (rcName.centerX()) - (textBound.width()/2);
        canvas.drawText(name,  x,rcName.bottom-12, paint);
    }

    private int getHoleStartPos(HolePosition holePosition) {

        switch (holePosition) {
            case ePrevHole:
                return prevHoleStartPos;
            case eCurrentHole:
                return currentHoleStartPos;
            case eNextHole:
                return nextHoleStartPos;
        }

        return 0;
    }

    private int getHoleLineLength(HolePosition holePosition) {
        switch (holePosition) {
            case ePrevHole:
                return holeLinePrevLength;
            case eCurrentHole:
                return holeLineCurrentLength;
            case eNextHole:
                return holeLineNextLength;
        }

        return 0;
    }
}
