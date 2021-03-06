package com.eye3.golfpay.model.score;

import com.eye3.golfpay.model.field.Course;
import com.eye3.golfpay.model.field.Hole;
import com.eye3.golfpay.model.teeup.Player;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*
 *   스코어를 서버에 최종적으로 보낼때 사용, ReserveScore에 리스트를 포함함.
 */
public class ReserveScore implements Serializable {
    @SerializedName("ret_code")
    @Expose
    public String ret_code;

    @SerializedName("ret_msg")
    @Expose
    public String ret_msg;

    @SerializedName("reserve_id")
    @Expose
    public String reserve_id;

    @SerializedName("hole_id")
    @Expose
    public String hole_id;

    @SerializedName("gamenow")
    @Expose
    public String gamenow;

    @SerializedName("guest_score")
    @Expose
    public ArrayList<ScoreSend> guest_score_list = new ArrayList<>();

    public ReserveScore(List<Player> playerList,  String reserve_id, String hole_id, int tabIdx, int mHoleScoreLayoutIdx) {
        for (int i = 0; playerList.size() > i; i++) {
            Hole aHole = playerList.get(i).course.get(tabIdx).holes.get(mHoleScoreLayoutIdx);

            guest_score_list.add(new ScoreSend(playerList.get(i).guest_id, aHole.playedScore.par,
                    aHole.playedScore.putting, aHole.playedScore.tar, aHole.playedScore.teeShot));

        }
        this.reserve_id = reserve_id;
        this.hole_id = hole_id;
    }
}
