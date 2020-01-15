package com.eye3.golfpay.fmb_tab.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import java.util.Set;

// preference helper
public class PreferenceStore {

	private final Context c;
	private final SharedPreferences sp;
	
	public PreferenceStore(Context c) {
		this.c = c;
		this.sp = PreferenceManager.getDefaultSharedPreferences(c);
	}
	
	public void writePrefString(String key, String value) {
		Editor e = sp.edit();
		e.remove(key);
		e.putString(key, value);
		e.apply();
	}

	public void writePrefStringSet(String key, Set<String> values) {
		Editor e = sp.edit();
		e.remove(key);
		e.putStringSet(key, values);
		e.apply();
	}

	public void writePrefInt(String key, int value) {
		Editor e = sp.edit();
		e.remove(key);
		e.putInt(key, value);
		e.apply();
	}

	public void writePrefLong(String key, long value) {
		Editor e = sp.edit();
		e.remove(key);
		e.putLong(key, value);
		e.apply();
	}

	public void writePrefFloat(String key, float value) {
		Editor e = sp.edit();
		e.remove(key);
		e.putFloat(key, value);
		e.apply();
	}

	public void writePrefBoolean(String key, boolean value) {
		Editor e = sp.edit();
		e.remove(key);
		e.putBoolean(key, value);
		e.apply();
	}

	public String readPrefString(String key, String defValue) {
		try {
			return sp.getString(key, defValue);
		} catch (ClassCastException e) {
			e.printStackTrace();
			sp.edit().remove(key);
			return defValue;
		}
	}

	public Set<String> readPrefStringSet(String key, Set<String> defValues) {
		try {
			return sp.getStringSet(key, defValues);
		} catch (ClassCastException e) {
			e.printStackTrace();
			sp.edit().remove(key);
			return defValues;
		}
	}

	public int readPrefInt(String key, int defValue) {
		try {
			return sp.getInt(key, defValue);
		} catch (ClassCastException e) {
			e.printStackTrace();
			sp.edit().remove(key);
			return defValue;
		}
	}

	public long readPrefLong(String key, long defValue) {
		try {
			return sp.getLong(key, defValue);
		} catch (ClassCastException e) {
			e.printStackTrace();
			sp.edit().remove(key);
			return defValue;
		}
	}

	public float readPrefFloat(String key, float defValue) {
		try {
			return sp.getFloat(key, defValue);
		} catch (ClassCastException e) {
			e.printStackTrace();
			sp.edit().remove(key);
			return defValue;
		}
	}

	public boolean readPrefBoolean(String key, boolean defValue) {
		try {
			return sp.getBoolean(key, defValue);
		} catch (ClassCastException e) {
			e.printStackTrace();
			sp.edit().remove(key);
			return defValue;
		}
	}

	public void        writePrefString   (int keyRes, String value)          {        writePrefString    (c.getString(keyRes),    value ); }
	public void        writePrefStringSet(int keyRes, Set<String> values)    {        writePrefStringSet (c.getString(keyRes),    values); }
	public void        writePrefInt      (int keyRes, int value)             {        writePrefInt       (c.getString(keyRes),    value ); }
	public void        writePrefLong     (int keyRes, long value)            {        writePrefLong      (c.getString(keyRes),    value ); }
	public void        writePrefFloat    (int keyRes, float value)           {        writePrefFloat     (c.getString(keyRes),    value ); }
	public void        writePrefBoolean  (int keyRes, boolean value)         {        writePrefBoolean   (c.getString(keyRes),    value ); }
	public String      readPrefString    (int keyRes, String defValue)       { return readPrefString     (c.getString(keyRes), defValue ); }
	public Set<String> readPrefStringSet (int keyRes, Set<String> defValues) { return readPrefStringSet  (c.getString(keyRes), defValues); }
	public int         readPrefInt       (int keyRes, int defValue)          { return readPrefInt        (c.getString(keyRes), defValue ); }
	public long        readPrefLong      (int keyRes, long defValue)         { return readPrefLong       (c.getString(keyRes), defValue ); }
	public float       readPrefFloat     (int keyRes, float defValue)        { return readPrefFloat      (c.getString(keyRes), defValue ); }
	public boolean     readPrefBoolean   (int keyRes, boolean defValue)      { return readPrefBoolean    (c.getString(keyRes), defValue ); }

}
