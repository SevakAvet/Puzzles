package com.vetrova.puzzles.puzzlesview;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;

import com.vetrova.puzzles.utils.Matrix;
import com.vetrova.puzzles.utils.Matrix.OnEachHandler;
import com.vetrova.puzzles.utils.Matrix.Position;

public class PuzzlesState {
	private Matrix<Position> state;
	private Editor editor;
	private SharedPreferences pref;
	
	public PuzzlesState(final Matrix<Bitmap> startPuzzles, final Matrix<Bitmap> puzzles) {
		state = new Matrix<Position>(puzzles.rows, puzzles.columns);
		startPuzzles.forEach(new OnEachHandler<Bitmap>() {
			@Override
			public void handle(Matrix<Bitmap> matrix, Position pos) {
				state.set(pos, positionAtPuzzles(startPuzzles, pos, puzzles));
			}
		});
	}

	private Position positionAtPuzzles(Matrix<Bitmap> startPuzzles,
									Position pos, Matrix<Bitmap> puzzles) {
		Bitmap puzzle = startPuzzles.get(pos);
		for (int row = 0; row < puzzles.rows; ++row) {
			for (int column = 0; column < puzzles.columns; ++column) {
				if (puzzles.get(row, column) == puzzle) {
					return new Position(row, column);
				}
			}
		}
		throw new RuntimeException("Cannot find puzzle");
	}
	
	public PuzzlesState(SharedPreferences pref) {
		this.pref = pref;
		loadStateMatrix();
	}

	private void loadStateMatrix() {
		int rows = pref.getInt("state.rows", -1);
		int columns = pref.getInt("state.columns", -1);
		state = new Matrix<Position>(rows, columns);
		state.forEach(new OnEachHandler<Position>() {
			@Override
			public void handle(Matrix<Position> matrix, Position pos) {
				state.set(pos, loadStatePositionAtPos(pos));
			}
		});
	}

	private Position loadStatePositionAtPos(Position pos) {
		int row = pref.getInt(posNameInPreferences(pos) + "row", -1);
		int column = pref.getInt(posNameInPreferences(pos) + "column", -1);
		return new Position(row, column);
	}
	
	private String posNameInPreferences(Position pos) {
		return "state.pos(" + pos.row + ", " + pos.column + ")";
	}
	
	PuzzlesState(Matrix<Position> statePositions) {
		this.state = statePositions;
	}
	
	Matrix<Position> getStatePositions() {
		return state;
	}
	
	public void save(SharedPreferences pref) {
		this.pref = pref;
		editor = pref.edit();
		saveStateMatrix();
		editor.commit();
	}
	
	private void saveStateMatrix() {
		editor.putInt("state.rows", state.rows);
		editor.putInt("state.columns", state.columns);
		state.forEach(new OnEachHandler<Position>() {
			@Override
			public void handle(Matrix<Position> matrix, Position pos) {
				Position statePosition = state.get(pos);
				saveStatePositionAtPos(statePosition, pos);
			}
		});
	}

	private void saveStatePositionAtPos(Position statePosition, Position pos) {
		editor.putInt(posNameInPreferences(pos) + "row", statePosition.row);
		editor.putInt(posNameInPreferences(pos) + "column", statePosition.column);
	}
	
}
