package com.eye3.golfpay.fmb_tab.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eye3.golfpay.fmb_tab.R;

public class TextInputBar extends LinearLayout {
    private TextView mItemText;
    private EditText mEt ;

    public TextInputBar(Context context) {
        super(context);
        init(context);
    }

    public TextInputBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, null);
    }

//    public TextInputBar(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        init(context, null);
//
//    }

//    public TextInputBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//        init(context, null);
//    }

    @Override // inflate가 완료되는 시점에 호출된다.
    protected void onFinishInflate() {
        super.onFinishInflate();

    }

    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.textbar_layout, this, false);
        mItemText = v.findViewById(R.id.tv_item);
        mEt =  v.findViewById(R.id.et_item);
        addView(v);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.textbar_layout, this, false);
        mItemText = v.findViewById(R.id.tv_item);
        mEt =  v.findViewById(R.id.et_item);
        addView(v);
        if (attrs != null) {
            //attrs.xml에 정의한 스타일을 가져온다
//            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyCustomView);
//            mSelected = a.getInteger(0, 0);
//            a.recycle(); // 이용이 끝났으면 recycle() 호출
        }

    }

   // android:textCursorDrawable
    public void setTextInputBarTitle(String title){
        title = title +":";
        mItemText.setText(title);
    }

    public EditText getEt(){
        return mEt;
    }

    public String getEtItemInputText(){
        return  mEt.getText().toString().trim();
    }

    public void setEtItemInputText(String inputText){
        mEt.setText(inputText);
    }

//    public static void setCursorColor(EditText view, @ColorInt int color) {
//        try {
//            // Get the cursor resource id
//            Field field = TextView.class.getDeclaredField("mCursorDrawableRes");
//            field.setAccessible(true);
//            int drawableResId = field.getInt(view);
//
//            // Get the editor
//            field = TextView.class.getDeclaredField("mEditor");
//            field.setAccessible(true);
//            Object editor = field.get(view);
//
//            // Get the drawable and set a color filter
//            Drawable drawable = ContextCompat.getDrawable(view.getContext(), drawableResId);
//            drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
//            Drawable[] drawables = {drawable, drawable};
//
//            // Set the drawables
//        //    field = editor.getClass().getDeclaredField("mCursorDrawable");
//            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
//                field = editor.getClass().getDeclaredField("mDrawableForCursor"); // error
//            } else {
//                field = editor.getClass().getDeclaredField("mCursorDrawable");
//            }
//            field.setAccessible(true);
//            field.set(editor, drawables);
//        } catch (Exception ignored) {
//            ignored.printStackTrace();
//        }
//    }

}
