package com.vetrova.puzzles.utils;
import android.content.Context;
import android.graphics.Typeface;


public class Util {
	
	public static Typeface loadFont(Context context, String fontName) {
		return Typeface.createFromAsset(context.getAssets(), "fonts/"+ fontName);
	}
}
