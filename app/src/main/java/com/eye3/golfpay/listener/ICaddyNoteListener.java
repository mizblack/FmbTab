package com.eye3.golfpay.listener;

import java.io.File;

public interface ICaddyNoteListener {
        void onShowClubInfoDlg(String id);
        void onInputCarNumber(String guestId, String text);
        void onInputPhoneNumber(String guestId, String text);
        void onInputMemo(String guestId, String text);
        void onSignatureImage(String guestId, File signatureFile);
        void onTakeClubPhoto(String guestId);
}
