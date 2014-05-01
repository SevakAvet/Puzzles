package com.vetrova.puzzles.gameutils;

import android.content.Context;

public class GlobalContext {
	private static Context context;

	public static void set(Context context) {
		GlobalContext.context = context;
	}
	
	public static Context get() {
		return context;
	}
}
