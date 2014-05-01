package com.vetrova.puzzles.rating;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.swarmconnect.Swarm;
import com.swarmconnect.SwarmActivity;
import com.swarmconnect.SwarmLeaderboard;
import com.vetrova.puzzles.R;
import com.vetrova.puzzles.gameutils.DimensionLoader;
import com.vetrova.puzzles.gameutils.GlobalStorage;
import com.vetrova.puzzles.utils.Dimension;


public class RatingActivity extends SwarmActivity {

	public static final String MODE = "MODE";
	public static final String SHOW_RESULT_AND_RATING_MODE = "SHOW_RESULT_AND_RATING";
	public static final String SHOW_ONLY_RATING_MODE = "SHOW_ONLY_RATING";
	private String mode;
	private boolean firstResume;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rating);
		
		firstResume = true;
		Intent intent = getIntent();
		mode = intent.getStringExtra(MODE);
		Swarm.init(this, SwarmInfo.APP_ID, SwarmInfo.APP_KEY);
		Swarm.setAllowGuests(true);
		if (mode.equals(SHOW_RESULT_AND_RATING_MODE)) {
			showResultAndRating();
		} else {
			Swarm.showLeaderboards();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (firstResume) {
			firstResume = false;
		} else {
			finish();
		}
	}

	private void showResultAndRating() {
		long ratingPoints = RatingCalculator.getRatingPoints();
		Toast.makeText(this, String.valueOf(ratingPoints), Toast.LENGTH_SHORT).show();
		int leaderboardId = leaderboardId();
		SwarmLeaderboard.submitScore(leaderboardId, ratingPoints);
		SwarmLeaderboard.showLeaderboard(leaderboardId);
	}

	private int leaderboardId() {
		final int[] LEADERBOARD_IDS = new int[] {
			SwarmInfo.EASY_LEVEL, SwarmInfo.MEDIUM_LEVEL, SwarmInfo.HARD_LEVEL
		};
		DimensionLoader dimensionLoader = new DimensionLoader(getResources());
		List<Dimension> dimensions = dimensionLoader.dimensions();
		Dimension currentDimension = GlobalStorage.getDimension();
		for (int i = 0; i < dimensions.size(); ++i) {
			if (currentDimension.equals(dimensions.get(i))) {
				return LEADERBOARD_IDS[i];
			}
		}
		throw new IllegalArgumentException("Cannot get leaderboardId");
	}
}
