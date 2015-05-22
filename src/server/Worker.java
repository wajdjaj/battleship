package server;

import game.Rulebook;

import java.io.*;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import utility.Utility;

public class Worker implements Runnable {
	private final CountDownLatch doneSignal;
	PrintWriter toClient;
	BufferedReader fromClient;
	Rulebook rules;
	int player;
	Worker(PrintWriter toClient, BufferedReader fromClient, Rulebook rules, int player, CountDownLatch doneSignal) {
		this.toClient = toClient;
		this.fromClient = fromClient;
		this.doneSignal = doneSignal;
		this.rules = rules; 
		this.player = player;
	}

	public void run() {
		try{		
			String placement;
			while (rules.shipsToPlace(player) && (placement = fromClient.readLine()) != null){
				//System.out.println("Worker is working. Just received line " + placement);
				int p[] = Utility.getCoords(placement);
				if (p == null || !rules.updatePlacement(p, player)){
					toClient.println("Placement Invalid");
					System.out.println("Invalid position");
					for (int i = 0; i < 4; i++){
						System.out.println(p[i]);
					}
				}else{
					System.out.println("Success");
					toClient.println("Success");
				}
			}
			doneSignal.countDown();
		}catch(IOException e){
			System.out.println("@Worker.run " + e);
			System.exit(0);
		}
	}
}
