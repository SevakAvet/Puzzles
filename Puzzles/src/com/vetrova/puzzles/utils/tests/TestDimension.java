package com.vetrova.puzzles.utils.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import com.vetrova.puzzles.utils.Dimension;

public class TestDimension {
	
	@Test
	public void testCreation() {
		final int rows = 1;
		final int columns = 2;
		Dimension dim = new Dimension(rows, columns);
		assertEquals(rows, dim.rows);
		assertEquals(columns, dim.columns);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testRowsIsZero() {
		new Dimension(0, 1);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testRowsIsNegative() {
		new Dimension(-1, 1);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testColumnsIsZero() {
		new Dimension(1, 0);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testColumnsIsNegative() {
		new Dimension(1, -1);
	}

	public void testEquals() {
		assertTrue(new Dimension(33, 22).equals(new Dimension(33, 22)));
		assertFalse(new Dimension(1, 2).equals(new Dimension(3, 1)));
	}
}
