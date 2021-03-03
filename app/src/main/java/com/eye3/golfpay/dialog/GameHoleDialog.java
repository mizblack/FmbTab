package com.eye3.golfpay.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eye3.golfpay.R;
import com.eye3.golfpay.adapter.ClubAdapter;
import com.eye3.golfpay.adapter.ClubGuestListAdapter;
import com.eye3.golfpay.common.Global;
import com.eye3.golfpay.model.field.Hole;
import com.eye3.golfpay.model.guest.CaddieInfo;
import com.eye3.golfpay.model.guest.ClubInfo;
import com.eye3.golfpay.model.order.ReserveGameType;
import com.eye3.golfpay.model.score.NearLongScoreBoard;
import com.eye3.golfpay.net.DataInterface;
import com.eye3.golfpay.net.ResponseData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-09-22.
 */

public class GameHoleDialog extends Dialog {

    String[] hole = {"1홀", "2홀", "3홀", "4홀", "5홀", "6홀", "7홀", "8홀", "9홀"};

    public class Item {

        public Item(String h, String p) {
            hole = h; par = p;
        }

        String hole;
        String par;
        public boolean selected = false;
    }

    private TextView tv_nearest_out;
    private TextView tv_nearest_in;
    private TextView tv_longest_out;
    private TextView tv_longest_in;

    private RecyclerView rvLongestBegin;
    private RecyclerView rvLongestEnd;
    private RecyclerView rvNearestBegin;
    private RecyclerView rvNearestEnd;

    private TextView tvCancel;
    private TextView tvSave;

    private int hole_no_long = 0;
    private String course_long = "";
    private int hole_no_near = 0;
    private String course_near = "";

    public interface  IListenerDialog {
        void onSelected(int hole);
    }

    public GameHoleDialog() {
        super(null);
    }

    public GameHoleDialog(Context context)
    {
        super(context);
    }

    public GameHoleDialog(Context context, int themeResId) {
        super(context, themeResId);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.frd_game_hole);
        rvLongestBegin = findViewById(R.id.rv_longest_out);
        rvLongestEnd = findViewById(R.id.rv_longest_in);
        rvNearestBegin = findViewById(R.id.rv_nearest_out);
        rvNearestEnd = findViewById(R.id.rv_nearest_in);

        tv_nearest_out = findViewById(R.id.tv_nearest_out);
        tv_nearest_in = findViewById(R.id.tv_nearest_in);
        tv_longest_out = findViewById(R.id.tv_longest_out);
        tv_longest_in = findViewById(R.id.tv_longest_in);

        tv_nearest_out.setText(Global.courseInfoList.get(0).courseName);
        tv_nearest_in.setText(Global.courseInfoList.get(1).courseName);
        tv_longest_out.setText(Global.courseInfoList.get(0).courseName);
        tv_longest_in.setText(Global.courseInfoList.get(1).courseName);

        tvSave = findViewById(R.id.tv_save);
        tvCancel = findViewById(R.id.tv_cancel);

        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNearLongHole();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        initRecyclerViews(rvLongestBegin, 0);
        initRecyclerViews(rvLongestEnd, 1);
        initRecyclerViews(rvNearestBegin, 0);
        initRecyclerViews(rvNearestEnd, 1);

        getNearLongHole();
    }

    private void saveNearLongHole() {

        if (hole_no_long == 0 || course_long.isEmpty() || hole_no_near == 0 || course_near.isEmpty()) {
            Toast.makeText(getContext(), "홀을 선택해 주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        ReserveGameType reserveGameType = new ReserveGameType();
        reserveGameType.res_id = Global.teeUpTime.getTodayReserveList().get(Global.selectedTeeUpIndex).getId();
        reserveGameType.hole_no_long = hole_no_long;
        reserveGameType.course_long = course_long;
        reserveGameType.hole_no_near = hole_no_near;
        reserveGameType.course_near = course_near;

        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).setReserveGameType(getContext(), reserveGameType, new DataInterface.ResponseCallback<ResponseData<Object>>() {
            @Override
            public void onSuccess(ResponseData<Object> response) {
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

    private void getNearLongHole() {

        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).getReserveGameType(getContext(), new DataInterface.ResponseCallback<ReserveGameType>() {
            @Override
            public void onSuccess(ReserveGameType response) {
                if (response.ret_code.equals("ok")) {

                    allUnSelect(rvNearestBegin);
                    allUnSelect(rvNearestEnd);
                    allUnSelect(rvLongestBegin);
                    allUnSelect(rvLongestEnd);

                    course_near = response.course_near;
                    course_long = response.course_long;
                    hole_no_near = response.hole_no_near;
                    hole_no_long = response.hole_no_long;

                    if (response.course_near.equals(Global.courseInfoList.get(0).courseName)) {
                        ((GameHoleAdapter) rvNearestBegin.getAdapter()).select(response.hole_no_near);
                        rvNearestBegin.getAdapter().notifyDataSetChanged();
                    } else if (response.course_near.equals(Global.courseInfoList.get(1).courseName)) {
                        ((GameHoleAdapter) rvNearestEnd.getAdapter()).select(response.hole_no_near);
                        rvNearestEnd.getAdapter().notifyDataSetChanged();
                    }

                    if (response.course_long.equals(Global.courseInfoList.get(0).courseName)) {
                        ((GameHoleAdapter) rvLongestBegin.getAdapter()).select(response.hole_no_long);
                        rvLongestBegin.getAdapter().notifyDataSetChanged();
                    } else if (response.course_long.equals(Global.courseInfoList.get(1).courseName)) {
                        ((GameHoleAdapter) rvLongestEnd.getAdapter()).select(response.hole_no_long);
                        rvLongestEnd.getAdapter().notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onError(ReserveGameType response) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void initRecyclerViews(RecyclerView recyclerView, int type) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        GameHoleAdapter gameHoleAdapter = new GameHoleAdapter(getContext());
        gameHoleAdapter.setIListenerDialog(new IListenerDialog() {
            @Override
            public void onSelected(int hole) {
                if (recyclerView == rvNearestEnd) {
                    GameHoleAdapter adapter = (GameHoleAdapter)rvLongestEnd.getAdapter();
                    if (sameCourseHole(adapter, hole)) {
                        return;
                    }

                    course_near = Global.courseInfoList.get(type).courseName;
                    hole_no_near = hole;
                    allUnSelect(rvNearestBegin);
                    updateUI(gameHoleAdapter, hole);
                }
                else if (recyclerView == rvNearestBegin) {
                    GameHoleAdapter adapter = (GameHoleAdapter)rvLongestBegin.getAdapter();
                    if (sameCourseHole(adapter, hole)) {
                        return;
                    }

                    course_near = Global.courseInfoList.get(type).courseName;
                    hole_no_near = hole;
                    allUnSelect(rvNearestEnd);
                    updateUI(gameHoleAdapter, hole);
                }
                else if (recyclerView == rvLongestEnd) {
                    GameHoleAdapter adapter = (GameHoleAdapter)rvNearestEnd.getAdapter();
                    if (sameCourseHole(adapter, hole)) {
                        return;
                    }

                    course_long = Global.courseInfoList.get(type).courseName;
                    hole_no_long = hole;
                    allUnSelect(rvLongestBegin);
                    updateUI(gameHoleAdapter, hole);
                }
                else if (recyclerView == rvLongestBegin) {
                    GameHoleAdapter adapter = (GameHoleAdapter)rvNearestBegin.getAdapter();
                    if (sameCourseHole(adapter, hole)) {
                        return;
                    }

                    course_long = Global.courseInfoList.get(type).courseName;
                    hole_no_long = hole;
                    allUnSelect(rvLongestEnd);
                    updateUI(gameHoleAdapter, hole);
                }
            }
        });

        recyclerView.setAdapter(gameHoleAdapter);

        List<Hole> holes = Global.courseInfoList.get(type).holes;
        for (int i = 0; i < 9; i++) {
            Item item = new Item(hole[i], "Par" + holes.get(i).par);
            gameHoleAdapter.items.add(item);
        }

        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        linearLayoutManager.scrollToPositionWithOffset(0, 0);
    }

    private void updateUI(@NonNull GameHoleAdapter adapter, int hole) {
        adapter.select(hole);
        adapter.notifyDataSetChanged();
    }

    //같은코스 같은홀에 니어/롱을 동시에 설정할 수 없다.
    private boolean sameCourseHole(@NonNull GameHoleAdapter adapter, int hole) {
        int select = adapter.getSelectItem();

        if (select == hole) {
            Toast.makeText(getContext(), "같은홀에 설정할 수 없습니다.", Toast.LENGTH_SHORT).show();
            return true;
        }

        return false;
    }

    private void allUnSelect(RecyclerView recyclerView) {

        try {
            ((GameHoleAdapter) recyclerView.getAdapter()).allSelected(false);
            recyclerView.getAdapter().notifyDataSetChanged();
        } catch(NullPointerException e) {
            e.printStackTrace();
        }

    }

    public class GameHoleAdapter extends RecyclerView.Adapter<GameHoleAdapter.GameHoleHolder> {

        private IListenerDialog iListenerDialog;
        ArrayList<Item> items = new ArrayList<>();

        public void setIListenerDialog(IListenerDialog iListenerDialog) {
            this.iListenerDialog = iListenerDialog;
        }

        public class GameHoleHolder extends RecyclerView.ViewHolder {

            public TextView tv_hole;
            public TextView tv_par;

            public GameHoleHolder(View itemView) {
                super(itemView);

                tv_hole = itemView.findViewById(R.id.tv_hole);
                tv_par = itemView.findViewById(R.id.tv_par);
            }
        }


        protected static final String TAG = "ClubAdapter";
        private final Context context;
        public GameHoleAdapter(Context context) {
            this.context = context;
        }

        @Override
        public GameHoleHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_game_hole_item, parent, false);
            GameHoleHolder rcv = new GameHoleHolder(view);

            return rcv;
        }

        @Override
        public void onBindViewHolder(GameHoleHolder holder, final int position) {

            try {
                if (items.get(position).selected) {
                    holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.irisBlue));
                    holder.tv_hole.setTextAppearance(R.style.GlobalTextView_20SP_White_NotoSans_Medium);
                    holder.tv_par.setTextAppearance(R.style.GlobalTextView_12SP_White_NotoSans_Medium);
                } else {
                    holder.itemView.setBackgroundColor(Color.WHITE);
                    holder.tv_hole.setTextAppearance(R.style.GlobalTextView_20SP_ebonyBlack_NotoSans_Medium);
                    holder.tv_par.setTextAppearance(R.style.GlobalTextView_12SP_ebonyBlack_NotoSans_Medium);
                }

                holder.tv_hole.setText(items.get(position).hole);
                holder.tv_par.setText(items.get(position).par);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //allSelected(false);
                        allSelected(false);
                        iListenerDialog.onSelected(position+1);
                    }
                });
            } catch (NullPointerException e) {
                Log.e(TAG, "NullPointerException : " + e);
            } catch (Exception e) {
                Log.e(TAG, "error : " + e);
            }
        }

        private void allSelected(boolean select) {
            for (Item item : items) {
                item.selected = select;
            }
        }

        public int getSelectItem() {
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).selected)
                    return i+1;
            }
            return -1;
        }

        public void select(int index) {
            try {
                items.get(index-1).selected = true;
            } catch(IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }
}