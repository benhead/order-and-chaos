package com.appdevguy.orderchaos.reduced;

public class Bits {
	// allocation of bits to pieces and colors
	static final int PTOC = 16;
	static final int PMASK = 0b1111111111111111;
	static final int CMASK = PMASK << PTOC;
	
	// row masks
	static final int ROW1 = 0b1111;
	static final int ROW2 = ROW1 << 4;
	static final int ROW3 = ROW2 << 4;
	static final int ROW4 = ROW3 << 4;
	
	// column masks
	static final int COL1 = 0b1000100010001;
	static final int COL2 = COL1 << 1;
	static final int COL3 = COL2 << 1;
	static final int COL4 = COL3 << 1;
	
	// long diagonal masks
	static final int DIA1 = 0b1000010000100001;
	static final int DIA2 = 0b1001001001000;
	
	// array of all winning lines
	static final int[] WINNERS =
		{ ROW1, ROW2, ROW3, ROW4, COL1, COL2, COL3, COL4, DIA1, DIA2 };

	// shorter diagonals used for reflection
	private static final int DIA3A = DIA1 >>> 4;
	private static final int DIA3B = DIA3A << 3;
	private static final int DIA2A = DIA3A >>> 4;
	private static final int DIA2B = DIA2A << 6;
	private static final int DIA1A = DIA2A >>> 4;
	private static final int DIA1B = DIA1A << 9;

	// convert a piece mask into a piece and color mask
	private static int withC(int n) {
		return n | (n << PTOC);
	}

	// Flip colors
	static int CO(int g) {
		return g ^ (g << PTOC);
	}
	
	// Rotate 180 degrees
	static int R2(int g) {
		int n = Integer.reverse(g);
		return (n << PTOC) | (n >>> PTOC);
	}
	
	// Mirror horizontally
	static int M1(int g) {
		return ((withC(COL1) & g) << 3) | ((withC(COL2) & g) << 1)
				| ((withC(COL3) & g) >>> 1) | ((withC(COL4) & g) >>> 3);
	}
	
	// Mirror vertically
	static int M2(int g) {
		return ((withC(ROW1) & g) << 12) | ((withC(ROW2) & g) << 4)
				| ((withC(ROW3) & g) >>> 4) | ((withC(ROW4) & g) >>> 12);
	}

	// Mirror diagonally
	static int D1(int g) {
		return (withC(DIA1) & g)
				| ((withC(DIA3A) & g) << 3) | ((withC(DIA3B) & g) >>> 3)
				| ((withC(DIA2A) & g) << 6) | ((withC(DIA2B) & g) >>> 6)
				| ((withC(DIA1A) & g) << 9) | ((withC(DIA1B) & g) >>> 9);
	}
	
	// n.b.
	// Rotate 90 degrees = D1->M2
	// Rotate 270 degrees = D1->M1
	// Mirror diagonally (other way) = D1->R2
	
	private Bits() {}
}
