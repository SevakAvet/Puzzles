package com.vetrova.puzzles.puzzlesview;
import java.util.Random;

import android.graphics.Bitmap;

import com.vetrova.puzzles.utils.Dimension;
import com.vetrova.puzzles.utils.Matrix;
import com.vetrova.puzzles.utils.Matrix.Position;

public class Mixer {
	private Random random;
	private Dimension dim;
	private int countOfNondisplaced;
	
	public static PuzzlesState randomState(Dimension dim) {
		return new Mixer(dim).randomState();
	}
	
	private Mixer(Dimension dim) {
		this.dim = dim;
	}

	public Mixer() {
		// do nothing
	}
	
	public void mix(final Matrix<Bitmap> puzzles, final Matrix<Position> statePositions) {
		final Matrix<Bitmap> copyOfPuzzles = new Matrix<Bitmap>(puzzles);
		copyOfPuzzles.forEach(new Matrix.OnEachHandler<Bitmap>() {
			@Override
			public void handle(Matrix<Bitmap> matrix, Position pos) {
				Bitmap puzzle = copyOfPuzzles.get(pos);
				Position newPosition = statePositions.get(pos);
				puzzles.set(newPosition, puzzle);
			}
		});
	}

	private PuzzlesState randomState() {
		return new PuzzlesState(randomStatePositions());
	}

	private Matrix<Position> randomStatePositions() {
		random = new Random(System.nanoTime());
		final Matrix<Position> randomStatePositions = new Matrix<Position>(dim);
		randomStatePositions.forEach(new Matrix.OnEachHandler<Position>() {
			@Override
			public void handle(Matrix<Position> matrix, Position pos) {
				randomStatePositions.set(pos, pos);
			}
		});
		int numberOfElements = dim.rows * dim.columns;
		int maxNumberOfNondisplaced = (int) (0.3 * numberOfElements);
		do {
			randomStatePositions.swap(randomPosition(), randomPosition());
		} while (numberOfNondisplaced(randomStatePositions) >= maxNumberOfNondisplaced);
		return randomStatePositions;
	}

	private Position randomPosition() {
		int row = random.nextInt(dim.rows);
		int column = random.nextInt(dim.columns);
		return new Matrix.Position(row, column);
	}
	
	private int numberOfNondisplaced(Matrix<Position> matrix) {
		countOfNondisplaced = 0;
		matrix.forEach(new Matrix.OnEachHandler<Matrix.Position>() {
			@Override
			public void handle(Matrix<Position> matrix, Position pos) {
				if (pos.equals(matrix.get(pos))) {
					++countOfNondisplaced;
				}
			}
		});
		return countOfNondisplaced;
	}
}
