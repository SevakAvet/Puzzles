package com.vetrova.puzzles.game;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.vetrova.puzzles.R;
import com.vetrova.puzzles.gameutils.GlobalStorage;

public class FullImageActivity extends Activity {

	private ImageView fullImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fullimage_activity);
		fullImageView = (ImageView) findViewById(R.id.imageView);
		fullImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onGameStarted();
			}
		});
		Bitmap bitmap = null;
		Bitmap fullScreenBitmap = null;
		
		try {
			bitmap = GlobalStorage.getBitmapDescriptor().getBitmap();
		} catch (Throwable e) {
			onCannotLoadAndShowImage();
		}
		
		if (inLandscapeOrientation(bitmap)) {
			Bitmap rotatedBitmap = getRotatedBitmap(bitmap);
			if (rotatedBitmap != bitmap) {
				bitmap.recycle();
				bitmap = rotatedBitmap;
				rotatedBitmap = null;
			}
		}
		
		try {
			Point size = displaySize();
			fullScreenBitmap = Bitmap.createScaledBitmap(bitmap, size.x, size.y, true);
		} catch (Throwable e) {
			onCannotLoadAndShowImage();
		} finally {
			bitmap.recycle();
			bitmap = null;
		}
		
		GlobalStorage.setBitmap(fullScreenBitmap);
		fullScreenBitmap = null;
		setPreview();

		GlobalStorage.setExistSavedGame(true);
	}

	private boolean inLandscapeOrientation(Bitmap bitmap) {
		return (bitmap.getWidth() / ((double) bitmap.getHeight())) >= 1.2;
	}
	
	private Bitmap getRotatedBitmap(Bitmap bitmap) {
		try {
			float angle = 90.0f;
			android.graphics.Matrix matrix = new android.graphics.Matrix();
			matrix.postRotate(angle);
			Bitmap rotatedBitmap = Bitmap.createBitmap(
				bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true
			);
			return rotatedBitmap != null ? rotatedBitmap : bitmap;
		} catch (Throwable e) {
			return bitmap;
		}
	}

	@SuppressWarnings("deprecation")
	private Point displaySize() {
		Display display = getWindowManager().getDefaultDisplay();
		if (Build.VERSION.SDK_INT < 13) {
			return new Point(display.getWidth(), display.getHeight());
		} else {
			Point size = new Point();
			display.getSize(size);
			return size;
		}
	}
	

	private void setPreview() {
		Drawable drawable = new BitmapDrawable(getResources(), GlobalStorage.getBitmap());
		setPreview(drawable);
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	private void setPreview(Drawable drawable) {
		if (Build.VERSION.SDK_INT < 16) {
			fullImageView.setBackgroundDrawable(drawable);
		} else {
			fullImageView.setBackground(drawable);
		}
	}
	
	private void onCannotLoadAndShowImage() {
		String message = getResources().getString(R.string.message_problem_with_image);
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
		finish();
	}

	private void onGameStarted() {
		Intent intent = new Intent(FullImageActivity.this, PuzzlesActivity.class);
		startActivityForResult(intent, 123);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (gameFinished()) {
			finish();
		}
	}
	
	private boolean gameFinished() {
		return !GlobalStorage.existSavedGame();
	}

	@Override
	protected void onDestroy() {
		GlobalStorage.setBitmap(null);
		super.onDestroy();
	}
}
