package com.eye3.golfpay.model.caddyNote;

import java.util.ArrayList;

public class SendSMS {

    public int reserve_id;
    public ArrayList<GuestInfo> guest_info = new ArrayList<>();

    public static class GuestInfo {
        public String guest_id = "";
        public String hp = "";
    }
}
