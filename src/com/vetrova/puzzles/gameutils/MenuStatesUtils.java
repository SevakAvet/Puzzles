package com.vetrova.puzzles.gameutils;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.View;

@SuppressLint("NewApi")
public class MenuStatesUtils {

	public static void setState(View menuItem, boolean state) {
		menuItem.setClickable(state);
		if (Build.VERSION.SDK_INT >= 11) {
			menuItem.setAlpha(state ? 1.0f : 0.4f);
		}
	}
}
