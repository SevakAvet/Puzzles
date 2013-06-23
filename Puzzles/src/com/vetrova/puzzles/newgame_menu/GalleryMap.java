package com.vetrova.puzzles.newgame_menu;

import com.vetrova.puzzles.R;


public class GalleryMap {
	static final int[] previewImageViewsIds = {
		R.id.ivPreview_1, R.id.ivPreview_2, R.id.ivPreview_3,
		R.id.ivPreview_4, R.id.ivPreview_5
	};
	
	static final int[] imagesIds = {
		R.drawable.image_1, R.drawable.image_2, R.drawable.image_3,
		R.drawable.image_4, R.drawable.image_5
	};
	
	static int imageIdByPreviewImageViewId(int previewImageViewId) {
		for (int i = 0; i < previewImageViewsIds.length; ++i) {
			if (previewImageViewsIds[i] == previewImageViewId) {
				return imagesIds[i];
			}
		}
		throw new IllegalArgumentException("illegal argument 'previewImageViewId'");
	}
}
