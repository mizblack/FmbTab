package com.eye3.golfpay.net;

import com.eye3.golfpay.model.info.BasicInfo;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ResponseChatNameList implements Serializable {
	@SerializedName("ret_code")
	@Expose
	private String  resultCode = "";

	@SerializedName("ret_msg")
	@Expose
	private String resultMessage = "";

	@SerializedName("list")
	@Expose
	private Moya list;

	public String getResultCode() {
		return resultCode;
	}

	public String getResultMessage() {
		return resultMessage;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getError() {
		return resultMessage;
	}

	public void setError(String error) {
		this.resultMessage = error;
	}

	public Moya getList(){
		return list;
	}

	public void setList(Moya list){
		this.list = list;
	}

	public class Moya {
		@SerializedName("caddy_list")
		@Expose
		public ArrayList<BasicInfo>	caddy_list;

		@SerializedName("group_list")
		@Expose
		public ArrayList<BasicInfo>	group_list;
	}
}
