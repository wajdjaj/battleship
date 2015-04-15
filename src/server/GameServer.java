package server;

import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Position{
	int x, y;
	Position(int x, int y){
		this.x = x;
		this.y = y;
	}
}

public class GameServer implements Runnable{
	int board[][][] = new int[2][8][8]; //[player][x][y] 0 miss 1 hit 2 already taken
	int numberOfHits[] = new int[]{0,0};
	int necessaryHits = 16;
	int currentPlayer;
	BufferedReader fromClient[] = new BufferedReader[2];
	PrintWriter toClient[] = new PrintWriter[2];
	Socket players[] = new Socket[2];	
	public static void main(String argv[]) {
		new GameServer().run();
	}
	public void run(){
		int winner = -1;
		waitPlayers(30000); //input port number
		waitClientBoardSetup();
		currentPlayer = coinFlip();		
		while (winner == -1){
			announceTurn();			
			while(targetHit()){
				if (victory()){
					winner = currentPlayer;
					break;
				}
			}						
			currentPlayer = -currentPlayer + 1; // changes between player 0 and 1.
		}
		announceWinner(winner);
	}
	
	void waitPlayers(int port){
		try{
			ServerSocket server = new ServerSocket(port);
			for (int i = 0; i < players.length; i++){
				players[i] = server.accept();
				toClient[i] = new PrintWriter(players[i].getOutputStream(), true);
				fromClient[i] = new BufferedReader(new InputStreamReader(players[i].getInputStream()));
			}			
			server.close();
		}catch(IOException e){
			System.out.println("@waitPlayers" + e);
			System.exit(1);
		}
	}
	void waitClientBoardSetup(){
		CountDownLatch doneSignal = new CountDownLatch(players.length);
		for (int i = 0; i < players.length; i++){
			new Thread(new Worker(toClient[i], fromClient[i],board[i], doneSignal)).start();
		}
		try {
			doneSignal.await();
		} catch (InterruptedException e) {
			System.out.println("@waitClientBoardSetup " + e);
			System.exit(1);
		}
	}
	void announceTurn(){		
			toClient[currentPlayer].println("Turn");	
			System.out.println("Announced to player " + currentPlayer);
	}

	boolean targetHit() {
		System.out.println("Initiating targeting systems");
		Position p = null;		
			do {
				System.out.println("Waiting for input from user");
				try{
					p = stringToPosition(fromClient[currentPlayer].readLine());
				}catch(IOException e){
					System.out.println("@targetHit " + e);
					System.exit(1);
				}
				System.out.println("Received response from user");
				if (p == null ||
						p.x > 7 || p.x < 0 || 
						p.y > 7 || p.y < 0 || 
						board[-currentPlayer + 1][p.x][p.y] == 2){					
					toClient[currentPlayer].println("Invalid position");
				}
				else
					break;
			} while (true);
			System.out.println("x:"+ p.x + "y: " + p.y);
			int state = board[-currentPlayer+1][p.x][p.y];
			board[-currentPlayer+1][p.x][p.y] = 2;
			if (state == 1){
				toClient[currentPlayer].println("Success");
				return true;
			}				
			toClient[currentPlayer].println("Miss");
		return false;
	}
	
	boolean victory(){
		numberOfHits[currentPlayer]++;
		return numberOfHits[currentPlayer] == necessaryHits;
	}
	
	
	void announceWinner(int winner){
		toClient[winner].println("Winner");
		toClient[-winner+1].println("Loser");
	}
	int coinFlip(){
		Random randomGenerator = new Random();
		return randomGenerator.nextInt(2);
	}
	public int[][] getBoard(int player){
		return board[player];
	}
	
	Position stringToPosition(String in){	
		if (in == null)
			return null;
		Pattern pattern = Pattern.compile("(\\w\\d)");
		Matcher matcher = pattern.matcher(in);
		if (matcher.find()){
			System.out.println(matcher.group(1).charAt(0));
			int x = Worker.charToInt(matcher.group(1).charAt(0));
			int y = Integer.parseInt(matcher.group(1).substring(1))-1;
			System.out.println("stringToPosition");
			System.out.println(x);
			System.out.println(y);
			return new Position(x,y);
		}
		return null;
	}
	
}
