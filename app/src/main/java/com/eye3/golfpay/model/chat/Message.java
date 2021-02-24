package com.eye3.golfpay.model.chat;

// Message.java
public class Message {
    private final String text; // message body
    private final MemberData memberData; // data of the user that sent this message
    private final boolean belongsToCurrentUser; // is this message sent by us?
    private boolean emergency = false;

    public Message(String text, MemberData memberData, boolean belongsToCurrentUser, boolean emergency) {
        this.text = text;
        this.memberData = memberData;
        this.belongsToCurrentUser = belongsToCurrentUser;
        this.emergency = emergency;
    }

    public String getText() {
        return text;
    }

    public MemberData getMemberData() {
        return memberData;
    }

    public boolean isBelongsToCurrentUser() {
        return belongsToCurrentUser;
    }

    public boolean isEmergency() {
        return emergency;
    }
}