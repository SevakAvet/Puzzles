package com.vetrova.puzzles.game;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.vetrova.puzzles.R;
import com.vetrova.puzzles.gameutils.GlobalStorage;
import com.vetrova.puzzles.puzzlesview.Mixer;
import com.vetrova.puzzles.puzzlesview.OnPuzzleAssembledListener;
import com.vetrova.puzzles.puzzlesview.PuzzlesState;
import com.vetrova.puzzles.puzzlesview.PuzzlesView;
import com.vetrova.puzzles.rating.RatingActivity;
import com.vetrova.puzzles.utils.Dimension;

public class PuzzlesActivity extends Activity implements OnPuzzleAssembledListener {

	private PuzzlesView puzzlesView;
	private boolean firstShowActivity;
	private long startTime;
	private long totalTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_puzzles);
		puzzlesView = (PuzzlesView) findViewById(R.id.puzzlesView);
		puzzlesView.addOnPuzzleAssembledListener(this);
		firstShowActivity = true;
	}

	@Override
	public void onGameFinished() {
		setGameFinished();
		updateTotalTime();
		GlobalStorage.setTime(totalTime);
		Intent intent = new Intent(this, RatingActivity.class);
		intent.putExtra(RatingActivity.MODE, RatingActivity.SHOW_RESULT_AND_RATING_MODE);
		startActivityForResult(intent, 1234);
	}
	
	private void setGameFinished() {
		GlobalStorage.setExistSavedState(false);
		GlobalStorage.setExistSavedGame(false);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		finish();
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus && firstShowActivity) {
			firstShowActivity = false;
			init();
		}
	}

	private void init() {
		Bitmap bitmap = GlobalStorage.getBitmap();
		Dimension dimension = GlobalStorage.getDimension();
		PuzzlesState state;
		if (GlobalStorage.existSavedState()) {
			state = GlobalStorage.getPuzzlesState();
			totalTime = GlobalStorage.getTime();
		} else {
			state = Mixer.randomState(dimension);
			totalTime = 0;
		}
		puzzlesView.set(bitmap, dimension, state);
	}

	@Override
	protected void onResume() {
		super.onResume();
		startTime = System.currentTimeMillis();
	}

	@Override
	protected void onPause() {
		super.onPause();
		updateTotalTime(); 
	}

	private void updateTotalTime() {
		totalTime += (System.currentTimeMillis() - startTime);
	}

	@Override
	protected void onDestroy() {
		if (gameNotFinished()) {
			PuzzlesState currentState = puzzlesView.getState();
			GlobalStorage.setPuzzlesState(currentState);
			GlobalStorage.setTime(totalTime);
			GlobalStorage.setExistSavedState(true);
		}
		puzzlesView.releaseImageResources();
		super.onDestroy();
	}

	private boolean gameNotFinished() {
		return GlobalStorage.existSavedGame();
	}

}
