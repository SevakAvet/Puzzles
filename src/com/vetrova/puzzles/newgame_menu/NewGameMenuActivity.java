package com.vetrova.puzzles.newgame_menu;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.vetrova.puzzles.R;
import com.vetrova.puzzles.bitmap_desriptor.BitmapDescriptor;
import com.vetrova.puzzles.bitmap_desriptor.GalleryBitmapDescriptor;
import com.vetrova.puzzles.bitmap_desriptor.ResourceBitmapDescriptor;
import com.vetrova.puzzles.game.FullImageActivity;
import com.vetrova.puzzles.gameutils.DimensionLoader;
import com.vetrova.puzzles.gameutils.GlobalStorage;
import com.vetrova.puzzles.gameutils.MenuStatesUtils;
import com.vetrova.puzzles.gameutils.PaymentUtils;
import com.vetrova.puzzles.utils.Dimension;

public class NewGameMenuActivity extends Activity implements OnClickListener {	
	
	private static final int CHOOSE_GALLERY_IMAGE = 22261;
	
	private Animation anim;
	private DimensionLoader dimensionLoader;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_game_menu_activity);
		
		anim = AnimationUtils.loadAnimation(this, R.anim.click_anim);
		dimensionLoader = new DimensionLoader(getResources());
		setListeners();
	}

	private void setListeners() {
		ImageView galleryItem = (ImageView) findViewById(R.id.ivGallery);
		galleryItem.setOnClickListener(this);
		for (int id : GalleryMap.previewImageViewsIds) {
			findViewById(id).setOnClickListener(this);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		boolean state = PaymentUtils.extraFeaturesHaveBeenPaid();
		MenuStatesUtils.setState(findViewById(R.id.ivGallery), state);
	}

	public void startGame(int imageId) {
		startGame(new ResourceBitmapDescriptor(imageId));
	}

	private void startGame(BitmapDescriptor descriptor) {
		try {
			tryStartGame(descriptor);
		} catch (Throwable e) {
			String message = "Cannot load image.\n" + "May be image too large";
			Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	private void tryStartGame(BitmapDescriptor descriptor) {
		GlobalStorage.setBitmapDescriptor(descriptor);
		GlobalStorage.setDimension(dimensionByCurrentDifficulty());
		GlobalStorage.setTime(0);
		GlobalStorage.setExistSavedState(false);
		Intent intent = new Intent(this, FullImageActivity.class);
		startActivity(intent);
	}

	private Dimension dimensionByCurrentDifficulty() {
		return dimensionLoader.dimension(checkedRadioButtonId());
	}

	private int checkedRadioButtonId() {
		RadioGroup radioGroup = (RadioGroup) findViewById(R.id.rbDificultMode);
		return radioGroup.getCheckedRadioButtonId();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.ivGallery) {
			onGalleryClick(v);
		} else {
			onChooseStandardImage(v);
		}
	}

	private void onChooseStandardImage(View v) {
		int chosenImageId = GalleryMap.imageIdByPreviewImageViewId(v.getId());
		startGame(chosenImageId);
	}

	private void onGalleryClick(View v) {
		v.startAnimation(anim);
		onGallery();
	}

	private void onGallery() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		Intent chooser = Intent.createChooser(intent, "Select Picture");
		startActivityForResult(chooser, CHOOSE_GALLERY_IMAGE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == CHOOSE_GALLERY_IMAGE && data != null) {
			Uri uri = data.getData();
			if (uri != null) {
				onChooseGalleryImage(uri);
			}
		}
	}

	private void onChooseGalleryImage(Uri uri) {
		try {
			String path = getRealPathFromURI(uri);
			startGame(new GalleryBitmapDescriptor(path));
		} catch (Throwable e) {
			notifyThatCannotLoadFromThisFolder();
			GlobalStorage.setExistSavedGame(false);
		}
	}

	private String getRealPathFromURI(Uri contentURI) {
	    Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
	    if (cursor == null) {
	        return contentURI.getPath();
	    } else { 
	        cursor.moveToFirst(); 
	        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA); 
	        return cursor.getString(idx); 
	    }
	}

	private void notifyThatCannotLoadFromThisFolder() {
		String message = getResources().getString(
				R.string.message_cannot_load_from_this_folder);
		Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
		View view = toast.getView();
		TextView text = (TextView) view.findViewById(android.R.id.message);
		int backgroundColor = view.getSolidColor();
		text.setBackgroundColor(backgroundColor);
		toast.show();
	}
}
