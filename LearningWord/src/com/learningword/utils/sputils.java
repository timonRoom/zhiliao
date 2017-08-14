package com.learningword.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View.OnClickListener;

public class sputils {
	/**
	 * 
	 * @param context иообнд
	 * @param key 
	 * @return
	 */
	public static boolean getspresult(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences("timon", Context.MODE_PRIVATE);
		return sp.getBoolean(key, false);
	}

	/**
	 * TODO 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void putboolean(Context context, String key, boolean value) {
		SharedPreferences sp = context.getSharedPreferences("timon", Context.MODE_PRIVATE);
		sp.edit().putBoolean(key, value).commit();
	}

}
