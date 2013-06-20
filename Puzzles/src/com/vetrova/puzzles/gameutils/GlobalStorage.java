package com.vetrova.puzzles.gameutils;

import android.graphics.Bitmap;

import com.vetrova.puzzles.bitmap_desriptor.BitmapDescriptor;
import com.vetrova.puzzles.puzzlesview.PuzzlesState;
import com.vetrova.puzzles.utils.Dimension;

public class GlobalStorage {
	private static Bitmap bitmap;
	private static Dimension dimension;
	private static PuzzlesState state;
	private static boolean existSavedState;
	private static boolean existSavedGame;
	private static BitmapDescriptor descriptor;
	private static long time;
	
	public static void setBitmap(Bitmap bitmap) {
		if (GlobalStorage.bitmap != null) {
			GlobalStorage.bitmap.recycle();
		}
		GlobalStorage.bitmap = bitmap;
	}
	
	public static Bitmap getBitmap() {
		return bitmap;
	}

	public static void setBitmapDescriptor(BitmapDescriptor descriptor) {
		GlobalStorage.descriptor = descriptor;
	}

	public static BitmapDescriptor getBitmapDescriptor() {
		return descriptor;
	}

	public static void setDimension(Dimension dimension) {
		GlobalStorage.dimension = dimension;
	}
	
	public static Dimension getDimension() {
		return dimension;
	}
	
	public static void setPuzzlesState(PuzzlesState state) {
		GlobalStorage.state = state;
	}
	
	public static PuzzlesState getPuzzlesState() {
		return state;
	}

	public static void setExistSavedState(boolean exist) {
		existSavedState = exist;
	}
	
	public static boolean existSavedState() {
		return existSavedState;
	}

	public static void setExistSavedGame(boolean exist) {
		existSavedGame = exist;
	}

	public static boolean existSavedGame() {
		return existSavedGame;
	}

	public static void saveToDevice() {
		DeviceSaverLoader.saveBoolean("ExistSavedGame", existSavedGame());
		DeviceSaverLoader.saveBoolean("ExistSavedState", existSavedState());
		if (existSavedGame() && existSavedState() && (getBitmap() != null)) {
			DeviceSaverLoader.saveBitmapDescriptor(getBitmapDescriptor());
			DeviceSaverLoader.saveDimension(getDimension());
			DeviceSaverLoader.savePuzzlesState(getPuzzlesState());
			DeviceSaverLoader.saveTime(getTime());
		}
	}

	public static void loadFromDevice() {
		loadBooleanFlags();
		if (!existSavedGameAndState()) {
			return;
		}
		tryLoadSavedDescriptor();
		if (cannotLoadSavedDescriptor()) {
			setNotExistSavedGameAndState();
		} else {
			setDimension(DeviceSaverLoader.loadDimension());
			setPuzzlesState(DeviceSaverLoader.loadPuzzlesState());	
			setTime(DeviceSaverLoader.loadTime());
		}
	}

	private static void loadBooleanFlags() {
		setExistSavedGame(DeviceSaverLoader.loadBoolean("ExistSavedGame"));
		setExistSavedState(DeviceSaverLoader.loadBoolean("ExistSavedState"));
	}
	
	private static boolean existSavedGameAndState() {
		return existSavedGame() && existSavedState();
	}

	private static void tryLoadSavedDescriptor() {
		BitmapDescriptor savedDescriptor;
		try {
			savedDescriptor = DeviceSaverLoader.loadBitmapDescriptor();
		} catch (Exception e) {
			savedDescriptor = null;
		}
		setBitmapDescriptor(savedDescriptor);
	}

	private static boolean cannotLoadSavedDescriptor() {
		return getBitmapDescriptor() == null;
	}

	public static void setNotExistSavedGameAndState() {
		setExistSavedState(false);
		setExistSavedGame(false);
	}

	public static long getTime() {
		return time;
	}

	public static void setTime(long time) {
		GlobalStorage.time = time;
	}
}
