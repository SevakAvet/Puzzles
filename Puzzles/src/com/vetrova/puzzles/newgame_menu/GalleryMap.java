package com.vetrova.puzzles.newgame_menu;

import com.vetrova.puzzles.R;


public class GalleryMap {
	static final int[] previewImageViewsIds = {
		R.id.ivPreview_1, R.id.ivPreview_2, R.id.ivPreview_3,
		R.id.ivPreview_4, R.id.ivPreview_5
	};
	
	static final int[] imagesIds = {
		R.drawable.animal_1, R.drawable.animal_2, R.drawable.nature_1,
		R.drawable.nature_2, R.drawable.nature_3
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
