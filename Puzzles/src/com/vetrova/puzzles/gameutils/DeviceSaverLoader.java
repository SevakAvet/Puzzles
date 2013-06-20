package com.vetrova.puzzles.gameutils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.vetrova.puzzles.bitmap_desriptor.BitmapDescriptor;
import com.vetrova.puzzles.puzzlesview.PuzzlesState;
import com.vetrova.puzzles.utils.Dimension;

public class DeviceSaverLoader {
	
	public static void saveDimension(Dimension dim) {
		Editor editor = preferences().edit();
		editor.putInt("dim.rows", dim.rows);
		editor.putInt("dim.columns", dim.columns);
		editor.commit();
	}
	
	public static Dimension loadDimension() {
		int rows = preferences().getInt("dim.rows", 0);
		int columns = preferences().getInt("dim.columns", 0);
		return new Dimension(rows, columns);
	}
	
	private static SharedPreferences preferences() {
		Activity activity = (Activity) GlobalContext.get();
		return activity.getPreferences(Context.MODE_PRIVATE);
	}
	
	public static void saveBoolean(String key, boolean value) {
		Editor editor = preferences().edit();
		editor.putBoolean(key.toLowerCase(), value);
		editor.commit();
	}
	
	public static boolean loadBoolean(String key) {
		return preferences().getBoolean(key.toLowerCase(), false);
	}

	public static void savePuzzlesState(PuzzlesState state) {
		state.save(preferences());
	}

	public static PuzzlesState loadPuzzlesState() {
		return new PuzzlesState(preferences());
	}

	public static void saveBitmapDescriptor(BitmapDescriptor bitmapDescriptor) {
		bitmapDescriptor.save(preferences());
	}

	public static BitmapDescriptor loadBitmapDescriptor() {
		return BitmapDescriptor.load(preferences());
	}

	public static void saveTime(long time) {
		Editor editor = preferences().edit();
		editor.putLong("time", time);
		editor.commit();
	}

	public static long loadTime() {
		return preferences().getLong("time", 0);
	}
}
