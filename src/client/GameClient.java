package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.Socket;

public class GameClient implements Runnable {
	Socket socket;
	PrintWriter toServer;
	BufferedReader fromServer;
	final int numberOfShips = 7;
	private final BufferedReader br;
	public GameClient(){
		br = new BufferedReader(new InputStreamReader(System.in));
	}
	public GameClient(Reader r){
		br = new BufferedReader(r);
	}
	public static void main(String argv[]) {
		new GameClient().run();
	}
	public void run(){
		setup();
		play();
	}
	void setup() {
		int placedShips = 0;
		String placement;
		try {
			socket = new Socket("localhost", 30000);
			if(socket.isConnected()){
				System.out.println("Connected to server.");
				fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				toServer = new PrintWriter(socket.getOutputStream(), true);
			}
			while (placedShips < numberOfShips) {
				do {
					placement = getPlacement();
				} while (!invalidPosition(placement));
				placedShips++;
			}
		} catch (IOException e) {
			System.out.println("@setup " + e);
			System.exit(1);
		}

	}
		
	void play() {
		boolean gameEnd = false;
		boolean playerTurn = false; // for now...
		try {
			while (!gameEnd) {
				updateGameState();
				if (playerTurn) {
					String target;
					do {
						target = getFirePosition(); // user pick desired square to shoot at;
					} while (isHit(target));
					playerTurn = false;
				} else {
					System.out.println("Waiting for turn");
					if (fromServer.readLine().equals("Lose")){
						System.out.println("You lose!");
						break;
					}
					System.out.println("Finished waiting");
					playerTurn = true;
				}
			}
		} catch (IOException e) {
			System.out.println("@play " + e);
			System.exit(1);
		}
	}
	
	int[] getShipTypes(){
		return null;
	}

	String getPlacement(){
		// " (Length of the boat | cord1 | cord2 
		try{
			//System.out.println("Where do you want to place the goat? (ex: 2, A1, A2)");
			return br.readLine();
		}catch(IOException e){
			System.out.println("@getPlacement " + e);
			System.exit(1);
		}
		return null;
	}
	
	boolean invalidPosition(String placement) {
		try {
			toServer.println(placement);
			String result = fromServer.readLine();
			return result.equals("Success");
		} catch (IOException e) {
			System.out.println("@invalidPosition " + e);
			System.exit(1);
		}
		return false;
	}
	void updateGameState(){
		
	}
	
	boolean isHit(String target){
		try {			
			toServer.println(target);
			System.out.println("Waiting for server response");
			String result = fromServer.readLine();	
			System.out.println("Recived response from server");
			if (result == null)
				System.exit(1);
			if (result.equals("Win")){
				System.out.println("You scored a critical hit!");
				System.out.println("You win");
				System.exit(0);
			}
			if (result.equals("Success")){
				System.out.println("You scored a critical hit!");
				return true;
			}			
			if (result.equals("Invalid position")){
				System.out.println("Invalid target");
				return true;
			}
			System.out.println("Miss!");
		} catch (IOException e) {
			System.out.println("@isHit " + e);
			System.exit(1);
		}	
		return false;
	}
	
	String getFirePosition(){
		return getPlacement();
	}
}
