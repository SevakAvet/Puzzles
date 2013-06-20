package com.vetrova.puzzles.game;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
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
		try {
			setPreview();
		} catch (Exception e) {
			onCannotSetPreview();
		}
		GlobalStorage.setExistSavedGame(true);
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
	
	private void onCannotSetPreview() {
		String message = "Cannot show image. Please, select other image";
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
}
