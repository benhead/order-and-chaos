package com.appdevguy.orderchaos.reduced;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.Duration;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;

public class Main {
	static IntSet orderWins = new IntOpenHashSet();
	static IntSet chaosWins = new IntOpenHashSet();
	
	public static void main(String[] args) throws Exception {
		long started = System.nanoTime();
		System.out.println(exploreNext(0, 1));
		//loadStates();
		System.out.println(Duration.ofNanos(System.nanoTime() - started));

		System.out.println("order:"+orderWins.size());
		System.out.println("chaos:"+chaosWins.size());
		//saveStates();
		
		for (int m = 0; m <= 16; m++) {
			final int moves = m;
			System.out.printf("%d\t%d\t%d\n", moves, 
					orderWins.stream().filter(g->Integer.bitCount(g & Bits.PMASK) == moves).count(),
					chaosWins.stream().filter(g->Integer.bitCount(g & Bits.PMASK) == moves).count());
		}
	}
	
	static int exploreNext(int g, int player) {
		if (orderWins.contains(g)) { return 1; }
		if (chaosWins.contains(g)) { return -1; }
		int w = exploreNextBase(g, player);
		(w == 1 ? orderWins : chaosWins).add(g);
		return w;
	}
	
	static int exploreNextBase(int g, int player) {
		int won = Game.winner(g);
		if (won != 0) { return won; }
		IntSet nexts = Game.enumerate(g);
		boolean iWin = false;
		for (int n : nexts) {
			if (exploreNext(n, -player) == player) {
				iWin = true;
			}
		}
		return iWin ? player : -player;
	}
	
	static void saveStates() throws FileNotFoundException, IOException {
		try(ObjectOutputStream str =
				new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("order.dat")))) {
			str.writeObject(orderWins);
		}
		try(ObjectOutputStream str =
				new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("chaos.dat")))) {
			str.writeObject(chaosWins);
		}
	}
	
	static void loadStates() throws Exception {
		try(ObjectInputStream str =
				new ObjectInputStream(new BufferedInputStream(new FileInputStream("order.dat")))) {
			orderWins = (IntOpenHashSet) str.readObject();
		}
		try(ObjectInputStream str =
				new ObjectInputStream(new BufferedInputStream(new FileInputStream("chaos.dat")))) {
			chaosWins = (IntOpenHashSet) str.readObject();
		}
	}
}
