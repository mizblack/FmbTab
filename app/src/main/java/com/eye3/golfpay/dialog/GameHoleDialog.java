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

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eye3.golfpay.R;
import com.eye3.golfpay.adapter.ClubAdapter;
import com.eye3.golfpay.adapter.ClubGuestListAdapter;
import com.eye3.golfpay.common.Global;
import com.eye3.golfpay.model.guest.CaddieInfo;
import com.eye3.golfpay.model.guest.ClubInfo;

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

    ArrayList<Item> items = new ArrayList<>();

    private IListenerDialog iListenerDialog;
    private RecyclerView rvLongestOut;
    private RecyclerView rvLongestIn;
    private RecyclerView rvNearestOut;
    private RecyclerView rvNearestIn;

    private TextView tvCancel;
    private TextView tvSave;

    public interface  IListenerDialog {
        public void onSave();
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

    public void setiListenerDialog(IListenerDialog iListenerDialog) {
        this.iListenerDialog = iListenerDialog;
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
                dismiss();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        for (int i = 0; i < 9; i++) {
            Item item = new Item(hole[i], par[i]);
            items.add(item);
        }

        initRecyclerViews(rvLongestOut);
        initRecyclerViews(rvLongestIn);
        initRecyclerViews(rvNearestOut);
        initRecyclerViews(rvNearestIn);
    }

    private void initRecyclerViews(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        GameHoleAdapter adapter = new GameHoleAdapter(getContext());
        recyclerView.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        linearLayoutManager.scrollToPositionWithOffset(0, 0);
    }

    public class GameHoleAdapter extends RecyclerView.Adapter<GameHoleAdapter.GameHoleHolder> {

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

        @Override
        public int getItemCount() {
            return items.size();
        }
    }
}