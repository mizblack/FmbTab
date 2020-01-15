package com.eye3.golfpay.fmb_tab.util;

import android.content.Context;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Logger {
	
	public static String TAG = "temp";
	public static String DEBUG_TAG = "Chauffeur";
	/** Client Setting Log */
	public static boolean mIsLog = true;
	/** Developer Log */
	public static boolean mTestLog = true;
	public static final boolean m_logFileSave = true;	///< false가 기본 / true:파일 저장 false: 파일저장 안함
	public static final boolean errorTextSave = true;	///< 에러 포워딩
	
	public Logger(Context context){

	}
	
	/**
	 * 시스템 정보 가져와 로그에 출력. 클래스,메서드,라인넘버
	 * 
	 * @param msg
	 * @return
	 */
	public static String BuildLogMSG(Object msg) {

		StackTraceElement ste = Thread.currentThread().getStackTrace()[4];
		StringBuilder sb = new StringBuilder();
//		at com.kr.ik.icsuserministop.activity.AgreementActivity.onClick(AgreementActivity.java:71)
//		sb.append("at [\n");
//		sb.append(ste.getFileName());
//		sb.append("\n >  ");
//		sb.append(ste.getMethodName());
//		sb.append(" >#  ");
//		sb.append(ste.getLineNumber());
//		sb.append(" ]  \n");
		
		sb.append("at ");
//		sb.append(msg);
		sb.append("("+ste.getFileName() + ":" + ste.getLineNumber() + ") > ");
		sb.append(ste.getMethodName() + "\n");
		sb.append(msg);

		return sb.toString();
	}
	public static void ctwd(String msg){
		if(mTestLog) {
			String temp = BuildLogMSG(msg);
			android.util.Log.d("ctw", temp);
			if(m_logFileSave){
				appendLog(temp);
			}
		}
	}
	
	public static void d(String tag, String msg){
		if(mTestLog || (mIsLog && DEBUG_TAG.equalsIgnoreCase(tag))) {
			android.util.Log.d(tag, " " + msg);
		}
	}
	
	public static void d(String tag, String msg, Throwable tr){
		if(mTestLog || (mIsLog && DEBUG_TAG.equalsIgnoreCase(tag))) {
			android.util.Log.d(tag, " " + msg, tr);
		}
	}
	
	public static void e(String tag, String msg){
		if(mTestLog || (mIsLog && DEBUG_TAG.equalsIgnoreCase(tag))) {
			android.util.Log.e(tag, " " + msg);
//			DataPack ackpack = new DataPack();
//			ackpack.datatype = Def.DB_WAKE_TYPE_TRLOGINFO_INSERT;
//			ackpack.data = setTRLogInfo(tag, msg);
//			DBManager.getInstance().ackDB(ackpack);
		}
	}
	
	public static void e(String tag, String msg, Throwable tr){
		if(mTestLog || (mIsLog && DEBUG_TAG.equalsIgnoreCase(tag))) {
			android.util.Log.e(tag, " " + msg, tr);
//			DataPack ackpack = new DataPack();
//			ackpack.datatype = Def.DB_WAKE_TYPE_TRLOGINFO_INSERT;
//			ackpack.data = setTRLogInfo(tag, msg);
//			DBManager.getInstance().ackDB(ackpack);
		}
	}
	
	public static void v(String tag, String msg){
		if(mTestLog || (mIsLog && DEBUG_TAG.equalsIgnoreCase(tag))) {
			android.util.Log.v(tag, " " + msg);
		}
	}
	
	public static void v(String tag, String msg, Throwable tr){
		if(mTestLog || (mIsLog && DEBUG_TAG.equalsIgnoreCase(tag))) {
			android.util.Log.v(tag, " " + msg, tr);
		}
	}
	
	public static void w(String tag, String msg){
		if(mTestLog || (mIsLog && DEBUG_TAG.equalsIgnoreCase(tag))) {
			android.util.Log.w(tag, " " + msg);
		}
	}
	
	public static void w(String tag, String msg, Throwable tr){
		if(mTestLog || (mIsLog && DEBUG_TAG.equalsIgnoreCase(tag))) {
			android.util.Log.w(tag, " " + msg, tr);
		}
	}
	
	public static void i(String tag, String msg){
		if(mTestLog || (mIsLog && DEBUG_TAG.equalsIgnoreCase(tag))) {
			android.util.Log.i(tag, " " + msg);
//			DataPack ackpack = new DataPack();
//			ackpack.datatype = Def.DB_WAKE_TYPE_TRLOGINFO_INSERT;
//			ackpack.data = setTRLogInfo(tag, msg);
//			DBManager.getInstance().ackDB(ackpack);
		}
	}
	
	public static void i(String tag, String msg, Throwable tr){
		if(mTestLog || (mIsLog && DEBUG_TAG.equalsIgnoreCase(tag))) {
			android.util.Log.i(tag, " " + msg, tr);
//			DataPack ackpack = new DataPack();
//			ackpack.datatype = Def.DB_WAKE_TYPE_TRLOGINFO_INSERT;
//			ackpack.data = setTRLogInfo(tag, msg);
//			DBManager.getInstance().ackDB(ackpack);
		}
	}
	
	/**
	 * 로그를 파일에 저장
	 * @param text 저장될 로그
	 */
	static public void appendLog(String text) {
		String filePath = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
		filePath += "/Macaron_Chauffeur";
	    File storageDir = new File(filePath);
	    storageDir.mkdirs();
	    String day = new SimpleDateFormat("yyyyMMdd").format(new Date());
	    String fileName = day + "_log.txt";
//	    File logFile;
//		logFile = new File(storageDir.getPath() + File.separator + fileName);
		File logFile = new File("sdcard/log.file");
		if (!logFile.exists()) {
			try {
				logFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			// BufferedWriter for performance, true to set append to file flag
			BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss.SSS").format(new Date());
			buf.append(timeStamp + " : " +text);
			buf.newLine();
			buf.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
