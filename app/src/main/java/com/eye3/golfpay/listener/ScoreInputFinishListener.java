package com.eye3.golfpay.listener;

import com.eye3.golfpay.model.teeup.Player;

import java.util.List;

public interface ScoreInputFinishListener {
    void OnScoreInputFinished(List<Player> playerList);
}
