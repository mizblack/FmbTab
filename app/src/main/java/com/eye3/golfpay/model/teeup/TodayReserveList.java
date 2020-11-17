
package com.eye3.golfpay.model.teeup;

import com.eye3.golfpay.model.order.PlayStatus;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("unused")
public class TodayReserveList implements Serializable {

    @SerializedName("access_token")
    private Object accessToken;
    @SerializedName("aws_key")
    private Object awsKey;
    @Expose
    private Object birth;
    @Expose
    private int caddy1;
    @Expose
    private Object caddy2;
    @Expose
    private Object caddy3;
    @SerializedName("cartbill_default")
    private int cartbillDefault;
    @SerializedName("cc_id")
    private int ccId;
    @SerializedName("company_id")
    private Object companyId;
    @SerializedName("course_id")
    private Object courseId;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("created_user")
    private int createdUser;
    @SerializedName("finish_game")
    private String finishGame;
    @Expose
    private int greenfee;
    @SerializedName("greenfee_default")
    private int greenfeeDefault;
    @Expose
    private String group;
    @SerializedName("group_id")
    private Object groupId;
    @SerializedName("guest_cnt")
    private int guestCnt;
    @SerializedName("guest_data")
    private ArrayList<GuestDatum> guestData;
    @Expose
    private String guestName;
    @SerializedName("guest_type")
    private Object guestType;
    @Expose
    private Object hp;
    @Expose
    private Object hp2;
    @Expose
    private int id;
    @SerializedName("InMember")
    private Object inMember;
    @SerializedName("inout_course")
    private String inoutCourse;
    @Expose
    private String inshop;
    @Expose
    private Object jumin;
    @SerializedName("member_id")
    private Object memberId;
    @SerializedName("member_level")
    private Object memberLevel;
    @Expose
    private String memoUse;
    @Expose
    private String mode;
    @SerializedName("modify_remark")
    private Object modifyRemark;
    @SerializedName("part_time")
    private int partTime;
    @SerializedName("pay_card_no")
    private Object payCardNo;
    @SerializedName("policy_id")
    private Object policyId;
    @SerializedName("promotion_id")
    private Object promotionId;
    @SerializedName("promotion_text")
    private String promotionText;
    @SerializedName("reserve_date")
    private String reserveDate;
    @SerializedName("reserve_hole_type")
    private String reserveHoleType;
    @SerializedName("reserve_no")
    private String reserveNo;
    @SerializedName("reserve_type")
    private Object reserveType;
    @SerializedName("reserve_type1")
    private String reserveType1;
    @SerializedName("reserve_type2")
    private Object reserveType2;
    @SerializedName("sale_type")
    private Object saleType;
    @Expose
    private String status;
    @SerializedName("team_no")
    private int teamNo;
    @Expose
    private String teeoff;
    @SerializedName("time_table_id")
    private int timeTableId;
    @SerializedName("updated_at")
    private String updatedAt;
    @SerializedName("updated_user")
    private int updatedUser;
    @SerializedName("memo")
    @Expose
    private String memo;

    @SerializedName("play_status")
    private String playStatus;

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Object getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(Object accessToken) {
        this.accessToken = accessToken;
    }

    public Object getAwsKey() {
        return awsKey;
    }

    public void setAwsKey(Object awsKey) {
        this.awsKey = awsKey;
    }

    public Object getBirth() {
        return birth;
    }

    public void setBirth(Object birth) {
        this.birth = birth;
    }

    public int getCaddy1() {
        return caddy1;
    }

    public void setCaddy1(int caddy1) {
        this.caddy1 = caddy1;
    }

    public Object getCaddy2() {
        return caddy2;
    }

    public void setCaddy2(Object caddy2) {
        this.caddy2 = caddy2;
    }

    public Object getCaddy3() {
        return caddy3;
    }

    public void setCaddy3(Object caddy3) {
        this.caddy3 = caddy3;
    }

    public int getCartbillDefault() {
        return cartbillDefault;
    }

    public void setCartbillDefault(int cartbillDefault) {
        this.cartbillDefault = cartbillDefault;
    }

    public int getCcId() {
        return ccId;
    }

    public void setCcId(int ccId) {
        this.ccId = ccId;
    }

    public Object getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Object companyId) {
        this.companyId = companyId;
    }

    public Object getCourseId() {
        return courseId;
    }

    public void setCourseId(Object courseId) {
        this.courseId = courseId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(int createdUser) {
        this.createdUser = createdUser;
    }

    public String getFinishGame() {
        return finishGame;
    }

    public void setFinishGame(String finishGame) {
        this.finishGame = finishGame;
    }

    public int getGreenfee() {
        return greenfee;
    }

    public void setGreenfee(int greenfee) {
        this.greenfee = greenfee;
    }

    public int getGreenfeeDefault() {
        return greenfeeDefault;
    }

    public void setGreenfeeDefault(int greenfeeDefault) {
        this.greenfeeDefault = greenfeeDefault;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Object getGroupId() {
        return groupId;
    }

    public void setGroupId(Object groupId) {
        this.groupId = groupId;
    }

    public int getGuestCnt() {
        return guestCnt;
    }

    public void setGuestCnt(int guestCnt) {
        this.guestCnt = guestCnt;
    }

    public ArrayList<GuestDatum> getGuestData() {
        return guestData;
    }

    public void setGuestData(ArrayList<GuestDatum> guestData) {
        this.guestData = guestData;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public Object getGuestType() {
        return guestType;
    }

    public void setGuestType(Object guestType) {
        this.guestType = guestType;
    }

    public Object getHp() {
        return hp;
    }

    public void setHp(Object hp) {
        this.hp = hp;
    }

    public Object getHp2() {
        return hp2;
    }

    public void setHp2(Object hp2) {
        this.hp2 = hp2;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Object getInMember() {
        return inMember;
    }

    public void setInMember(Object inMember) {
        this.inMember = inMember;
    }

    public String getInoutCourse() {
        return inoutCourse;
    }

    public void setInoutCourse(String inoutCourse) {
        this.inoutCourse = inoutCourse;
    }

    public String getInshop() {
        return inshop;
    }

    public void setInshop(String inshop) {
        this.inshop = inshop;
    }

    public Object getJumin() {
        return jumin;
    }

    public void setJumin(Object jumin) {
        this.jumin = jumin;
    }

    public Object getMemberId() {
        return memberId;
    }

    public void setMemberId(Object memberId) {
        this.memberId = memberId;
    }

    public Object getMemberLevel() {
        return memberLevel;
    }

    public void setMemberLevel(Object memberLevel) {
        this.memberLevel = memberLevel;
    }

    public String getMemoUse() {
        return memoUse;
    }

    public void setMemoUse(String memoUse) {
        this.memoUse = memoUse;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Object getModifyRemark() {
        return modifyRemark;
    }

    public void setModifyRemark(Object modifyRemark) {
        this.modifyRemark = modifyRemark;
    }

    public int getPartTime() {
        return partTime;
    }

    public void setPartTime(int partTime) {
        this.partTime = partTime;
    }

    public Object getPayCardNo() {
        return payCardNo;
    }

    public void setPayCardNo(Object payCardNo) {
        this.payCardNo = payCardNo;
    }

    public Object getPolicyId() {
        return policyId;
    }

    public void setPolicyId(Object policyId) {
        this.policyId = policyId;
    }

    public Object getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(Object promotionId) {
        this.promotionId = promotionId;
    }

    public String getPromotionText() {
        return promotionText;
    }

    public void setPromotionText(String promotionText) {
        this.promotionText = promotionText;
    }

    public String getReserveDate() {
        return reserveDate;
    }

    public void setReserveDate(String reserveDate) {
        this.reserveDate = reserveDate;
    }

    public String getReserveHoleType() {
        return reserveHoleType;
    }

    public void setReserveHoleType(String reserveHoleType) {
        this.reserveHoleType = reserveHoleType;
    }

    public String getReserveNo() {
        return reserveNo;
    }

    public void setReserveNo(String reserveNo) {
        this.reserveNo = reserveNo;
    }

    public Object getReserveType() {
        return reserveType;
    }

    public void setReserveType(Object reserveType) {
        this.reserveType = reserveType;
    }

    public String getReserveType1() {
        return reserveType1;
    }

    public void setReserveType1(String reserveType1) {
        this.reserveType1 = reserveType1;
    }

    public Object getReserveType2() {
        return reserveType2;
    }

    public void setReserveType2(Object reserveType2) {
        this.reserveType2 = reserveType2;
    }

    public Object getSaleType() {
        return saleType;
    }

    public void setSaleType(Object saleType) {
        this.saleType = saleType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTeamNo() {
        return teamNo;
    }

    public void setTeamNo(int teamNo) {
        this.teamNo = teamNo;
    }

    public String getTeeoff() {
        return teeoff;
    }

    public void setTeeoff(String teeoff) {
        this.teeoff = teeoff;
    }

    public int getTimeTableId() {
        return timeTableId;
    }

    public void setTimeTableId(int timeTableId) {
        this.timeTableId = timeTableId;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getUpdatedUser() {
        return updatedUser;
    }

    public void setUpdatedUser(int updatedUser) {
        this.updatedUser = updatedUser;
    }

    public String getPlayStatus() {
        return playStatus;
    }
}
