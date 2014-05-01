package com.vetrova.puzzles.bitmap_desriptor;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;

public abstract class BitmapDescriptor {
	
	protected static final String KEY_EXISTING_FLAG = "SavedBitmapDescriptor";
	protected static final String KEY_DESCRIPTOR_TYPE = "DescriptorType";
	protected static final String TYPE_IS_RESOURCE = "ResourceType";
	protected static final String TYPE_IS_GALLERY = "GalleryType";
	protected static final String KEY_RESOURCE_VALUE = "ValueOfResourceType";
	protected static final String KEY_GALLERY_VALUE = "ValueOfGalleryType";
	private static SharedPreferences pref;
	
	public static BitmapDescriptor load(SharedPreferences pref) {
		BitmapDescriptor.pref = pref;
		if (!descriptorIsSaved()) {
			return null;
		}
		String descriptorType = getDesriptorType();
		if (descriptorType.equals(TYPE_IS_RESOURCE)) {
			return resourceBitmapDescriptor();
		} else if (descriptorType.equals(TYPE_IS_GALLERY)) {
			return galleryBitmapDescriptor();
		} else {
			throw new RuntimeException("Cannot load descriptor.");
		}
	}
	
	private static boolean descriptorIsSaved() {
		return pref.getBoolean(KEY_EXISTING_FLAG, false);
	}

	private static String getDesriptorType() {
		return pref.getString(KEY_DESCRIPTOR_TYPE, "");
	}
	
	private static BitmapDescriptor resourceBitmapDescriptor() {
		int resId = pref.getInt(KEY_RESOURCE_VALUE, -1);
		return new ResourceBitmapDescriptor(resId);
	}
	
	private static BitmapDescriptor galleryBitmapDescriptor() {
		String path = pref.getString(KEY_GALLERY_VALUE, "");
		return new GalleryBitmapDescriptor(path);
	}
	
	public abstract Bitmap getBitmap();
	
	public void save(SharedPreferences pref) {
		Editor editor = pref.edit();
		save(editor);
		editor.putBoolean(KEY_EXISTING_FLAG, true);
		editor.commit();
	}
	
	protected abstract void save(Editor editor);
}
