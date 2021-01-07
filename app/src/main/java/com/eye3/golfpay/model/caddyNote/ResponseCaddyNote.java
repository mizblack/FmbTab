package com.eye3.golfpay.model.caddyNote;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseCaddyNote {
	@SerializedName("ret_code")
	@Expose
	private String  resultCode = "";

	@SerializedName("ret_msg")
	@Expose
	private String resultMessage = "";

	@SerializedName("team_memo")
	@Expose
	private String team_memo = "";


//	@SerializedName("detailMessage")
//	@Expose
//	private String detailMessage = "";

	@SerializedName("data")
	@Expose
	private List<CaddyNoteInfo> data;

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMessage() {
		return resultMessage;
	}

	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}

	public List<CaddyNoteInfo> getData() {
		return data;
	}

	public void setData(List<CaddyNoteInfo> data) {
		this.data = data;
	}

	public String getTeam_memo() {
		return team_memo;
	}
}
