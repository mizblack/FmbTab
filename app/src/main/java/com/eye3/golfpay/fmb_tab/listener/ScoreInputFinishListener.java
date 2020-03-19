package com.eye3.golfpay.fmb_tab.listener;

import com.eye3.golfpay.fmb_tab.model.teeup.Player;

import java.util.ArrayList;
import java.util.List;

public interface ScoreInputFinishListener {
    void OnScoreInputFinished(ArrayList<Player> playerList);
}
