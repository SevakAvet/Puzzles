package com.vetrova.puzzles.rating;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.swarmconnect.Swarm;
import com.swarmconnect.SwarmActivity;
import com.swarmconnect.SwarmLeaderboard;
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
		
		firstResume = true;
		Intent intent = getIntent();
		mode = intent.getStringExtra(MODE);
		if (mode.equals(SHOW_RESULT_AND_RATING_MODE)) {
			initShowResultAndRatingMode();
		} else {
			initShowOnlyRatingMode();
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

	private void initShowResultAndRatingMode() {
		final long ratingPoints = RatingCalculator.getRatingPoints();
		Toast.makeText(this, String.valueOf(ratingPoints), Toast.LENGTH_SHORT).show();
		Swarm.init(this, SwarmInfo.APP_ID, SwarmInfo.APP_KEY);
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
		Dimension dim = GlobalStorage.getDimension();
		for (int i = 0; i < dimensions.size(); ++i) {
			if (dim.equals(dimensions.get(i))) {
				return LEADERBOARD_IDS[i];
			}
		}
		throw new IllegalArgumentException("Cannot get leaderboardId");
	}

	private void initShowOnlyRatingMode() {
		Swarm.init(this, SwarmInfo.APP_ID, SwarmInfo.APP_KEY);
		Swarm.showLeaderboards();
	}
}
