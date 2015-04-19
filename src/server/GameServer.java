package server;

import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

class Position{
	int x, y;
	Position(int x, int y){
		this.x = x;
		this.y = y;
	}
}

public class GameServer implements Runnable{
	Rulebook rules = new Rulebook();
	private int currentPlayer;
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
		while (!rules.isGameOver()){
			announceTurn();			
			while(gameRound()){				
			}						
			changePlayer();
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
			new Thread(new Worker(toClient[i], fromClient[i], rules, i, doneSignal)).start();
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
	
	boolean gameRound(){
		int p[];
		try{
			while(true){
				p = Worker.stringToPosition(fromClient[currentPlayer].readLine());		
				if (p != null && rules.targetIsValid(p, currentPlayer))
					break;
				toClient[currentPlayer].println("Invalid position");
			}
			if (rules.targetIsHit(p, currentPlayer)){
				toClient[currentPlayer].println("Success");
				return true;
			}
			else
				toClient[currentPlayer].println("Miss");
		}catch(IOException e){
			System.out.println("@gameRound " + e);
			System.exit(1);
		}
		return false;
	}
	
	void announceWinner(int winner){
		toClient[-winner+1].println("Lose");
	}
	int coinFlip(){
		Random randomGenerator = new Random();
		return randomGenerator.nextInt(2);
	}
	void changePlayer(){
		currentPlayer = -currentPlayer + 1;
	}
}
