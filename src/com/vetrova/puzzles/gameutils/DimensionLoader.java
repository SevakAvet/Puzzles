package com.vetrova.puzzles.gameutils;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.res.Resources;

import com.vetrova.puzzles.R;
import com.vetrova.puzzles.utils.Dimension;

public class DimensionLoader {

	private List<Integer> difficultyIds;
	private List<Dimension> dimensions;
	private Resources resources;

	@SuppressLint("UseSparseArrays")
	public DimensionLoader(Resources resources) {
		this.resources = resources;
		difficultyIds = new ArrayList<Integer>();
		dimensions = new ArrayList<Dimension>();
		loadDimensions();
	}

	private void loadDimensions() {
		addByIds(R.id.rbEasy,   R.integer.easy_rows,   R.integer.easy_columns);
		addByIds(R.id.rbMedium, R.integer.medium_rows, R.integer.medium_columns);
		addByIds(R.id.rbHard,   R.integer.hard_rows,   R.integer.hard_columns);
	}

	private void addByIds(int radioButtonId, int rowsId, int columnsId) {
		int rows = resources.getInteger(rowsId);
		int columns = resources.getInteger(columnsId);
		dimensions.add(new Dimension(rows, columns));
		difficultyIds.add(radioButtonId);
	}

	public Dimension dimension(int radioButtonId) {
		for (int i = 0; i < difficultyIds.size(); ++i) {
			if (difficultyIds.get(i) == radioButtonId) {
				return dimensions.get(i);
			}
		}
		return null;
	}
	
	public List<Dimension> dimensions() {
		return new ArrayList<Dimension>(dimensions);
	}
}
