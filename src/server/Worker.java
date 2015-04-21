package server;

import java.io.*;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

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
				int p[] = getCoords(placement);
				if (p == null || !rules.updatePlacement(p, player)){
					toClient.println("Placement Invalid");
					System.out.println("Invalid position");
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
	
	public static int[] getCoords(String placement){
		int p[] = new int[4];
		if (placement.length() > 4){
			for (int i = 0; i < 2; i++){
				int tmp[];
				if ((tmp = stringToPosition(placement.substring(i*2))) == null)
					return null;
				p[i*2] = tmp[0];
				p[i*2+1] = tmp[1];
			}
			return p;
		}
		return null;
	}
	
	public static int[] stringToPosition(String in){	
		if (in == null)
			return null;
		Pattern pattern = Pattern.compile("(\\w\\d+)");
		Matcher matcher = pattern.matcher(in);
		if (matcher.find()){			
			int p[] = new int[2];
			p[0] = Integer.parseInt(matcher.group(1).substring(1))-1;			
			p[1] = charToInt(matcher.group(1).charAt(0));
			return p;
		}
		return null;
	}
	
	static int charToInt(char in){		
		return Character.toLowerCase(in) - 'a';
	}
}
