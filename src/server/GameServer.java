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

public class GameServer {
	int board[][][] = new int[2][8][8]; //[player][x][y] 0 miss 1 hit 2 already taken
	int currentPlayer;
	Socket players[] = new Socket[2];
	public static void main(String argv[]) {
		new GameServer().run();
	}
	void run(){
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
				System.out.println("New player connected.");
			}
			server.close();
		}catch(IOException e){
			System.out.println(e); //Awesome error handling!
		}
	}
	void waitClientBoardSetup(){
		CountDownLatch doneSignal = new CountDownLatch(players.length);
		for (int i = 0; i < players.length; i++){
			new Thread(new Worker(players[i],board[i], doneSignal)).start();
		}
		try {
			doneSignal.await();
		} catch (InterruptedException e) {
			System.out.println(e);
		}
	}
	void announceTurn(){
		try{
			PrintWriter toClient = new PrintWriter(players[currentPlayer].getOutputStream(),true);
			toClient.println("Turn");
			toClient.close();
		}catch(IOException e){
			System.out.println(e);
		}
		
	}
	boolean targetHit(){
		Position p;
		do{
			//read from the proper socket
			//extract position from the message and store into Position p
			p = new Position(1,1);
		}while(board[currentPlayer][p.x][p.y] == 2);
		int state = board[currentPlayer][p.x][p.y];
		board[currentPlayer][p.x][p.y] = 2;
		return state == 1;
	} 
	
	boolean victory(){
		return false; // Check if the game is over
	}
	
	int positionToInt(Position p){
		return 1;
	}
	
	void announceWinner(int winner){
		try{
			PrintWriter toClientWinner = new PrintWriter(players[winner].getOutputStream(), true);
			toClientWinner.println("Winner");
			toClientWinner.close();
			PrintWriter toClientLoser = new PrintWriter(players[-winner + 1].getOutputStream(), true);
			toClientLoser.println("Lose");
			toClientLoser.close();
		}catch(IOException e){
			System.out.println(e);
		}
	}
	int coinFlip(){
		Random randomGenerator = new Random();
		return randomGenerator.nextInt(2);
	}
	
}
