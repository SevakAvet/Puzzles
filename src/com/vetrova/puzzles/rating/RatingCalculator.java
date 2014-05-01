package com.vetrova.puzzles.rating;

import com.vetrova.puzzles.gameutils.GlobalStorage;
import com.vetrova.puzzles.utils.Dimension;

public class RatingCalculator {
	
	public static long getRatingPoints() {
		long D = difficultyCoefficient();
		long T = totalTimeInSeconds();
		return calculateRating(D, T);
	}
	
	private static long difficultyCoefficient() {
		Dimension dim = GlobalStorage.getDimension();
		int numberOfPuzzles = dim.rows * dim.columns;
		return (long) (numberOfPuzzles * Math.log(numberOfPuzzles));
	}

	private static long totalTimeInSeconds() {
		return (GlobalStorage.getTime() / 1000);
	}
	
	private static long calculateRating(long D, long T) {
		float rating = D / (0.01f + T / 3600.0f);
		return (long) rating;
	}
}
