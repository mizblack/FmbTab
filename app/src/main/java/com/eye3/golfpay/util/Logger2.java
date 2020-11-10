package com.eye3.golfpay.util;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Logger2 {

	private static SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.ROOT);
	private static final String TAG = "MACARON_CHAUFFEUR";
	private static final int LOG_ARCHIVE_SIZE = 30;
	
	private static BlockingQueue<String> msg = new LinkedBlockingQueue<String>(1024);
	private static Thread logThread = new Thread("Logger2 Thread") {
		private File logF = null;
		private SimpleDateFormat fmtYMD = new SimpleDateFormat("yyyy-MM-dd", Locale.ROOT);
		private FileWriter writer;
		
		@Override
		public void run() {
			if (!openLog())
				return;
			int retryFuse = 10;
			while (true) {
				try {
					writer.write(msg.take());
				} catch (IOException e) {
					e.printStackTrace();
					retryFuse --;
					if (retryFuse < 0)
						return;
				} catch (InterruptedException e) {
					//noop
				}
			}
		};
		
		boolean openLog() {
			if (logF != null) 
				return true;
			
			File storage = new File(Environment.getExternalStorageDirectory() + File.separator + "a99_log");
			storage.mkdirs();
			
			List<File> logsAll = new LinkedList<File>();
			for (File f : Arrays.asList(storage.listFiles(new FilenameFilter() {
				
				@Override
				public boolean accept(File dir, String filename) {
					return filename.toLowerCase(Locale.ROOT).endsWith(".log");
				}
			}))) logsAll.add(f);
			
			Collections.sort(logsAll, new Comparator<File>() {

				@Override
				public int compare(File lhs, File rhs) {
					return lhs.getName().compareTo(rhs.getName());
				}
			});
			
			while (logsAll.size() >= LOG_ARCHIVE_SIZE) {
				File f = logsAll.get(0);
				f.delete();
				logsAll.remove(0);
			}
			
			synchronized (fmtYMD) {
				logF = new File(storage, fmtYMD.format(new Date()) + ".log");
				try {
					writer = new FileWriter(logF, true);
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			}
			
			return true;
		}
	};
	
	static {
		logThread.setDaemon(true);
		logThread.start();
	}
	
	private static String getCallerInfo() {
		String info = " ";

		StackTraceElement ste = Thread.currentThread().getStackTrace()[4];
		if (ste == null)
			return info;

		String fileName = ste.getFileName();
		String methodName = ste.getClassName() + "@" + ste.getMethodName();
		int lineNumber = ste.getLineNumber();
		info = "(at " + fileName + " " + methodName + " " + Integer.toString(lineNumber) + ")" ;

		return info;
	}

	private static void logToFile(String lv, String message) {
		StringBuffer strBuf = new StringBuffer();
		synchronized (fmt) {
			strBuf.append(fmt.format(new Date()));
		}
		strBuf.append("\t").append(lv).append("\t").append(message).append("\n");
		
		msg.offer(strBuf.toString());
	}
	
	private static void logToFile(String lv, Throwable tr) {
		for (StackTraceElement e : tr.getStackTrace())
			logToFile(lv, e.toString() + "\t" + e.getFileName() + ", line" + e.getLineNumber());
	}

	public static void v(String info, String value) {
		v(info + " : " + value);
	}

	public static void d(String info, String value) {
		d(info + " : " + value);
	}

	public static void i(String info, String value) {
		i(info + " : " + value);
	}

	public static void w(String info, String value) {
		w(info + " : " + value);
	}

	public static void e(String info, String value) {
		e(info + " : " + value);
	}

	public static void v(String value) {
		Log.v(TAG, value + " " + getCallerInfo());
		logToFile("v", value);
	}

	public static void d(String value) {
		Log.d(TAG, value + " " + getCallerInfo());
		logToFile("d", value);
	}

	public static void i(String value) {
		Log.i(TAG, value + " " + getCallerInfo());
		logToFile("i", value);
	}

	public static void w(String value) {
		Log.w(TAG, value + " " + getCallerInfo());
		logToFile("w", value);
	}

	public static void e(String value) {
		Log.e(TAG, value + " " + getCallerInfo());
		logToFile("e", value);
	}    

	public static void v(String value, Throwable tr) {
		Log.v(TAG, value + " " + getCallerInfo(), tr);
		logToFile("v", value);
		logToFile("v", tr);
	}

	public static void d(String value, Throwable tr) {
		Log.d(TAG, value + " " + getCallerInfo(), tr);
		logToFile("d", value);
		logToFile("d", tr);
	}

	public static void i(String value, Throwable tr) {
		Log.i(TAG, value + " " + getCallerInfo(), tr);
		logToFile("i", value);
		logToFile("i", tr);
	}

	public static void w(String value, Throwable tr) {
		Log.w(TAG, value + " " + getCallerInfo(), tr);
		logToFile("w", value);
		logToFile("w", tr);
	}

	public static void e(String value, Throwable tr) {
		Log.e(TAG, value + " " + getCallerInfo(), tr);
		logToFile("e", value);
		logToFile("e", tr);
	}    

	public static void i_stack() {
		String caller = getCallerInfo();
		Log.i(TAG, caller);
		logToFile("i", caller);
	}
	
}
