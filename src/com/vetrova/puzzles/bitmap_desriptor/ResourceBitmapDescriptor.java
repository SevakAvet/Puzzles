package com.vetrova.puzzles.bitmap_desriptor;

import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.vetrova.puzzles.gameutils.GlobalContext;

public class ResourceBitmapDescriptor extends BitmapDescriptor {
	
	private final int resId;

	public ResourceBitmapDescriptor(int resId) {
		this.resId = resId;
	}

	@Override
	public Bitmap getBitmap() {
		Resources resources = GlobalContext.get().getResources();
		return BitmapFactory.decodeResource(resources, resId);
	}

	@Override
	protected void save(Editor editor) {
		editor.putString(KEY_DESCRIPTOR_TYPE, TYPE_IS_RESOURCE);
		editor.putInt(KEY_RESOURCE_VALUE, resId);
	}
}
