package com.eye3.golfpay.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eye3.golfpay.R;
import com.eye3.golfpay.adapter.ClubAdapter;
import com.eye3.golfpay.adapter.ClubGuestListAdapter;
import com.eye3.golfpay.common.Global;
import com.eye3.golfpay.model.guest.CaddieInfo;
import com.eye3.golfpay.model.guest.ClubInfo;
import com.eye3.golfpay.model.order.ReserveGameType;
import com.eye3.golfpay.model.score.NearLongScoreBoard;
import com.eye3.golfpay.net.DataInterface;
import com.eye3.golfpay.net.ResponseData;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017-09-22.
 */

public class GameHoleDialog extends Dialog {

    String[] hole = {"1홀", "2홀", "3홀", "4홀", "5홀", "6홀", "7홀", "8홀", "9홀"};
    String[] par = {"Par3", "Par4", "Par2", "Par3", "Par3", "Par5", "Par4", "Par2", "Par3"};

    public class Item {

        public Item(String h, String p) {
            hole = h; par = p;
        }

        String hole;
        String par;
        public boolean selected = false;
    }


    private RecyclerView rvLongestOut;
    private RecyclerView rvLongestIn;
    private RecyclerView rvNearestOut;
    private RecyclerView rvNearestIn;

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
        rvLongestOut = findViewById(R.id.rv_longest_out);
        rvLongestIn = findViewById(R.id.rv_longest_in);

        rvNearestOut = findViewById(R.id.rv_nearest_out);
        rvNearestIn = findViewById(R.id.rv_nearest_in);

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

        initRecyclerViews(rvLongestOut);
        initRecyclerViews(rvLongestIn);
        initRecyclerViews(rvNearestOut);
        initRecyclerViews(rvNearestIn);

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

            }

            @Override
            public void onError(ResponseData<Object> response) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

        dismiss();
    }

    private void getNearLongHole() {

        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).getReserveGameType(getContext(), new DataInterface.ResponseCallback<ReserveGameType>() {
            @Override
            public void onSuccess(ReserveGameType response) {
                if (response.ret_code.equals("ok")) {

                    allUnSelect(rvNearestOut);
                    allUnSelect(rvNearestIn);
                    allUnSelect(rvLongestOut);
                    allUnSelect(rvLongestIn);

                    course_near = response.course_near = response.course_near.toLowerCase();
                    course_long = response.course_long = response.course_long.toLowerCase();
                    hole_no_near = response.hole_no_near;
                    hole_no_long = response.hole_no_long;

                    if (response.course_near.equals("in")) {
                        ((GameHoleAdapter) rvNearestIn.getAdapter()).select(response.hole_no_near);
                        rvNearestIn.getAdapter().notifyDataSetChanged();
                    } else if (response.course_near.equals("out")) {
                        ((GameHoleAdapter) rvNearestOut.getAdapter()).select(response.hole_no_near);
                        rvNearestIn.getAdapter().notifyDataSetChanged();
                    }

                    if (response.course_long.equals("in")) {
                        ((GameHoleAdapter) rvLongestIn.getAdapter()).select(response.hole_no_long);
                        rvNearestIn.getAdapter().notifyDataSetChanged();
                    } else if (response.course_long.equals("out")) {
                        ((GameHoleAdapter) rvLongestOut.getAdapter()).select(response.hole_no_long);
                        rvNearestIn.getAdapter().notifyDataSetChanged();
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

    private void initRecyclerViews(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        GameHoleAdapter adapter = new GameHoleAdapter(getContext());
        adapter.setIListenerDialog(new IListenerDialog() {
            @Override
            public void onSelected(int hole) {
                if (recyclerView == rvNearestIn) {
                    course_near = "IN";
                    hole_no_near = hole;
                    allUnSelect(rvNearestOut);
                }
                else if (recyclerView == rvNearestOut) {
                    course_near = "OUT";
                    hole_no_near = hole;
                    allUnSelect(rvNearestIn);
                }
                else if (recyclerView == rvLongestIn) {
                    course_long = "IN";
                    hole_no_long = hole;
                    allUnSelect(rvLongestOut);
                }
                else if (recyclerView == rvLongestOut) {
                    course_long = "OUT";
                    hole_no_long = hole;
                    allUnSelect(rvLongestIn);
                }
                adapter.notifyDataSetChanged();
            }
        });

        recyclerView.setAdapter(adapter);
        for (int i = 0; i < 9; i++) {
            Item item = new Item(hole[i], par[i]);
            adapter.items.add(item);
        }

        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        linearLayoutManager.scrollToPositionWithOffset(0, 0);
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
        private Context context;
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
                if (items.get(position).selected == true) {
                    holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.irisBlue));
                    holder.tv_hole.setTextAppearance(context, R.style.GlobalTextView_20SP_White_NotoSans_Medium);
                    holder.tv_par.setTextAppearance(context, R.style.GlobalTextView_12SP_White_NotoSans_Medium);
                } else {
                    holder.itemView.setBackgroundColor(Color.WHITE);
                    holder.tv_hole.setTextAppearance(context, R.style.GlobalTextView_20SP_ebonyBlack_NotoSans_Medium);
                    holder.tv_par.setTextAppearance(context, R.style.GlobalTextView_12SP_ebonyBlack_NotoSans_Medium);
                }

                holder.tv_hole.setText(items.get(position).hole);
                holder.tv_par.setText(items.get(position).par);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //allSelected(false);
                        allSelected(false);
                        iListenerDialog.onSelected(position+1);
                        items.get(position).selected = true;
                        notifyDataSetChanged();
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