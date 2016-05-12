package com.appdevguy.orderchaos.reduced;

import java.util.Collection;

import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntSet;

public class Game {
	/**
	 * Enumerates all legal moves from g into out.
	 * Due to canonicalization, some may be duplicates.
	 */
	public static void enumerateTo(int g, Collection<? super Integer> out) {
		for (int i = 0; i < Bits.PTOC; i++) {
			int loc = 1 << i;
			if ((g & loc) == 0) {
				out.add(canonicalize(g | loc));
				out.add(canonicalize(g | loc | (loc << Bits.PTOC)));
			}
		}
	}

	/**
	 * Enumerates all legal moves from g into a new IntSet.
	 */
	public static IntSet enumerate(int g) {
		IntSet out = new IntArraySet();
		enumerateTo(g, out);
		return out;
	}

	/**
	 * Determines if state g is a winning state.
	 * Returns 1 if order wins, -1 if chaos wins, 0 if neither.
	 */
	public static int winner(int g) {
		boolean allBlocked = true;
		for (int w : Bits.WINNERS) {
			int n = winner(g, w);
			if (n == 1) { return 1; }
			if (n == 0) { allBlocked = false; }
		}
		return allBlocked ? -1 : 0;
	}
	
	private static int winner(int g, int mask) {
		int p = g & mask;
		if (p == 0) { return 0; }
		int n = Integer.bitCount(p);
		if (n == 1) { return 0; }
		int c = (g >>> Bits.PTOC) & mask;
		if (c == 0 || c == p) {
			return (n == 4) ? 1 : 0;
		}
		return -1;
	}

	/**
	 * Returns the canonical representation of state g.
	 */
	public static int canonicalize(int g) {
		int min = leastByColor(g);
		min = Math.min(min, leastByColor(Bits.R2(g)));
		min = Math.min(min, leastByColor(Bits.M1(g)));
		min = Math.min(min, leastByColor(Bits.M2(g)));
		int d = Bits.D1(g);
		min = Math.min(min, leastByColor(d));
		min = Math.min(min, leastByColor(Bits.R2(d)));
		min = Math.min(min, leastByColor(Bits.M1(d)));
		min = Math.min(min, leastByColor(Bits.M2(d)));
		return min;
	}
	
	private static int leastByColor(int g) {
		return Math.min(g, Bits.CO(g));
	}

	/**
	 * Returns a visual representation of state g suitable for printing.
	 */
	public static String visualize(int g) {
		char[] out = new char[20];
		for (int i = 4; i < out.length; i += 5) {
			out[i] = '\n';
		}
		for (int i = 0; i < Bits.PTOC; i++) {
			int loc = 1 << i;
			out[(int)(i*1.25)] = ((g & loc) == 0) ? '-' : ((g & (loc << Bits.PTOC)) == 0) ? 'W' : 'B';
		}
		return new String(out);
	}
	
	private Game() {}
}
