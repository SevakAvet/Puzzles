package com.vetrova.puzzles.bitmap_desriptor;

import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class GalleryBitmapDescriptor extends BitmapDescriptor {
	
	private final String path;

	public GalleryBitmapDescriptor(String path) {
		this.path = path;
	}

	@Override
	public Bitmap getBitmap() {
		return BitmapFactory.decodeFile(path);
	}

	@Override
	protected void save(Editor editor) {
		editor.putString(KEY_DESCRIPTOR_TYPE, TYPE_IS_GALLERY);
		editor.putString(KEY_GALLERY_VALUE, path);
	}
}
