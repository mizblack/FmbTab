package com.eye3.golfpay.dialog;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.eye3.golfpay.R;
import com.eye3.golfpay.common.Global;
import com.eye3.golfpay.model.field.Course;
import com.eye3.golfpay.model.gps.CType;
import com.eye3.golfpay.net.DataInterface;
import com.eye3.golfpay.net.ResponseData;
import com.eye3.golfpay.util.Util;

/**
 * Created by Administrator on 2017-09-22.
 */

public class ChangeCourseDialog extends Dialog {

    public interface  IListenerDialog {
        void onChangeCourse();
    }

    public ChangeCourseDialog() {
        super(null);
    }

    public ChangeCourseDialog(Context context, String beginCourse, String endCourse, IListenerDialog listener)
    {
        super(context);
        beginCourseName = beginCourse;
        endCourseName = endCourse;
        iListenerDialog = listener;
    }

    public ChangeCourseDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    private String beginCourseName, endCourseName;
    private ConstraintLayout viewMain;
    private TextView tvBeginChangeCourse;
    private TextView tvEndChangeCourse;
    private TextView tvSave;
    private TextView tvCancel;
    private TextView tvCurrentCourseName;
    private LinearLayout popup_menu;
    private IListenerDialog iListenerDialog;
    private int gameBeforeAfter = 0; //0:전반 1:후반
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.frd_change_course);
        viewMain = findViewById(R.id.view_main);
        tvBeginChangeCourse = findViewById(R.id.tv_begin_change_course);
        tvBeginChangeCourse.setText(beginCourseName);
        tvCurrentCourseName = findViewById(R.id.tv_current_course);
        tvEndChangeCourse = findViewById(R.id.tv_end_change_course);
        tvEndChangeCourse.setText(endCourseName);
        tvSave = findViewById(R.id.tv_save);
        tvCancel = findViewById(R.id.tv_cancel);

        popup_menu = findViewById(R.id.popup_menu);

        changeCourseList(getContext());

        viewMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup_menu.setVisibility(View.GONE);
            }
        });

        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                YesNoDialog dlg = new YesNoDialog(getContext(), R.style.DialogTheme);
                WindowManager.LayoutParams wmlp = dlg.getWindow().getAttributes();
                wmlp.gravity = Gravity.CENTER;

                // Set alertDialog "not focusable" so nav bar still hiding:
                dlg.getWindow().
                        setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

                dlg.setCanceledOnTouchOutside(false);
                dlg.getWindow().getDecorView().setSystemUiVisibility(Util.DlgUIFalg);
                dlg.show();
                dlg.setContentTitle("코스를 변경하면 해당 코스 스코어가 초기화 됩니다.\n변경하시겠습니까?");
                dlg.setListener(new YesNoDialog.IListenerYesNo() {
                    @Override
                    public void onConfirm() {
                        changeCourse(getContext(), tvBeginChangeCourse.getText().toString(), tvEndChangeCourse.getText().toString());
                    }
                });
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


        tvBeginChangeCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup_menu.setVisibility(View.VISIBLE);
                popup_menu.setX(tvBeginChangeCourse.getX()-1);
                popup_menu.setY(tvBeginChangeCourse.getY()-1);
                tvCurrentCourseName.setText(beginCourseName);
                gameBeforeAfter = 0; //전반
            }
        });
        tvEndChangeCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup_menu.setVisibility(View.VISIBLE);
                popup_menu.setX(tvEndChangeCourse.getX()-1);
                popup_menu.setY(tvEndChangeCourse.getY()-1);
                tvCurrentCourseName.setText(endCourseName);
                gameBeforeAfter = 1;//후반
            }
        });
    }

    private void addCourseItem(String item) {

        LinearLayout layout = popup_menu.findViewById(R.id.view_item);
        TextView tvCourseItem = new TextView(getContext(), null, 0, R.style.GlobalTextView_24SP_White_NotoSans_Medium);
        LinearLayout.LayoutParams lllp = new LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, 80);
        tvCourseItem.setLayoutParams(lllp);
        tvCourseItem.setText(item);
        tvCourseItem.setGravity(Gravity.CENTER);
        layout.addView(tvCourseItem);

        tvCourseItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = tvCourseItem.getText().toString();
                popup_menu.setVisibility(View.GONE);
                if (gameBeforeAfter == 0) //gameBeforeAfter이 0이면 전반
                    tvBeginChangeCourse.setText(text);
                else
                    tvEndChangeCourse.setText(text);

                tvCurrentCourseName.setText(text);
            }
        });
    }

    private void changeCourseList(Context context) {

        DataInterface.getInstance().changeCourseList(context, new DataInterface.ResponseCallback<ResponseData<CType>>() {
            @Override
            public void onSuccess(ResponseData<CType> response) {
                response.getList().size();

                for (CType course: response.getList())
                    addCourseItem(course.getCtype());
            }

            @Override
            public void onError(ResponseData<CType> response) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void changeCourse(Context context, String before_course, String after_course) {

        DataInterface.getInstance().changeCourse(context, before_course, after_course, new DataInterface.ResponseCallback<ResponseData<Object>>() {
            @Override
            public void onSuccess(ResponseData<Object> response) {
                if (response.getResultCode().equals("ok")) {
                    Global.CurrentCourse = null;//현재 코스 초기화
                    Toast.makeText(context, "코스가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                    iListenerDialog.onChangeCourse();
                }
                dismiss();
            }

            @Override
            public void onError(ResponseData<Object> response) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }


}