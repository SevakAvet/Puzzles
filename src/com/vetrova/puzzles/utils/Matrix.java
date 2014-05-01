package com.vetrova.puzzles.utils;

public class Matrix<T> {

	public interface OnEachHandler<T> {
		void handle(Matrix<T> matrix, Position pos);
	}

	public static class Position {
		public final int row;
		public final int column;

		public Position(int row, int column) {
			if (row < 0 || column < 0) {
				throw new IllegalArgumentException();
			}
			this.row = row;
			this.column = column;
		}

		@Override
		public boolean equals(Object obj) {
			if ((obj == null) || !(obj instanceof Matrix.Position)) {
				return false;
			}
			Matrix.Position other = (Matrix.Position) obj;
			return (this.row == other.row) && (this.column == other.column);
		}
	}

	public final int rows;
	public final int columns;
	private T[] values;

	@SuppressWarnings("unchecked")
	public Matrix(int rows, int columns) {
		if (rows <= 0 || columns <= 0) {
			throw new IllegalArgumentException();
		}
		this.rows = rows;
		this.columns = columns;
		values = (T[]) new Object[rows * columns];
	}
	
	public Matrix(Dimension dim) {
		this(dim.rows, dim.columns);
	}
	
	public Matrix(final Matrix<T> other) {
		this(other.rows, other.columns);
		other.forEach(new OnEachHandler<T>() {
			@Override
			public void handle(Matrix<T> matrix, Position pos) {
				set(pos, other.get(pos));
			}
		});
	}

	public void set(Position pos, T value) {
		set(pos.row, pos.column, value);
	}

	public void set(int row, int column, T value) {
		checkIndexes(row, column);
		values[index(row, column)] = value;
	}
	
	private void checkIndexes(int row, int column) {
		if (row < 0 || column < 0) {
			throw new IllegalArgumentException();
		}
		if (row >= rows || column >= columns) {
			throw new IndexOutOfBoundsException();
		}
	}
	
	private int index(int row, int column) {
		return row * columns + column;
	}

	public T get(Position pos) {
		return get(pos.row, pos.column);
	}

	public T get(int row, int column) {
		checkIndexes(row, column);
		return values[index(row, column)];
	}

	@Override
	public boolean equals(Object obj) {
		if ((obj == null) || !(obj instanceof Matrix<?>)) {
			return false;
		}
		Matrix<?> other = (Matrix<?>) obj;
		if (other.rows != rows || other.columns != columns) {
			return false;
		}
		for (int row = 0; row < rows; ++row) {
			for (int column = 0; column < columns; ++column) {
				Object elementOfThis = get(row, column);
				Object elementOfOther = other.get(row, column);
				if ((elementOfThis == null && elementOfOther != null) ||
					(elementOfOther == null && elementOfThis != null) ||
					(elementOfThis != null && !elementOfThis.equals(elementOfOther))) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hashCode1 = get(0, 0).hashCode();
		int hashCode2 = get(rows - 1, columns - 1).hashCode();
		int hashCode = hashCode1 * hashCode2;
		hashCode += Math.max(hashCode1, hashCode2) / Math.min(hashCode1, hashCode2);
		return hashCode;
	}

	public void swap(Position pos1, Position pos2) {
		T temp = get(pos1);
		set(pos1, get(pos2));
		set(pos2, temp);
	}

	public void forEach(OnEachHandler<T> onEachHandler) {
		for (int row = 0; row < this.rows; ++row) {
			for (int column = 0; column < this.columns; ++column) {
				onEachHandler.handle(this, new Position(row, column));
			}
		}
	}
}
