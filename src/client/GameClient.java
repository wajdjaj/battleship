package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.Socket;

import server.Worker;

public class GameClient implements Runnable {
	Socket socket;
	PrintWriter toServer;
	BufferedReader fromServer;
	final int numberOfShips = 7;
	String updates;
	private final BufferedReader br;
	GUI gui;
	public GameClient(){
		br = new BufferedReader(new InputStreamReader(System.in));
	}
	public GameClient(Reader r){
		br = new BufferedReader(r);
	}
	public GameClient(GUI gui){
		br = null;
		this.gui = gui;
		gui.start();
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
				boolean deployed = false;
				do {
					placement = getPlacement();
					deployed = validPosition(placement);					;
				} while (!deployed);
				updateGameState(placement,0, 1);
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
				if (playerTurn) {
					String target;
					do {
						target = getFirePosition(); // user pick desired square to shoot at;
						System.out.println("Recived position: " + target);
					} while (isHit(target));
					playerTurn = false;
				} else {
					System.out.println("Waiting for turn");
					String input;
					while ((input = fromServer.readLine()) != null && !input.equals("Turn")){
						if (input.equals("Lose")){
							System.out.println("You lose!");
							break;
						}
						System.out.println("Received from server: " + input);
						if (input.contains("Hit")) updateGameState(input.substring(4), 0, 1);
						else updateGameState(input, 0, 0);
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

	String getPlacement(){
		// " (Length of the boat | cord1 | cord2
		if (gui != null) {
			return gui.getInput(0) + " " +gui.getInput(0);
		}else{
			try {
				return br.readLine();
			} catch (IOException e) {
				System.out.println("@getPlacement " + e);
				System.exit(1);
			}
		}
		return null;
	}
	
	boolean validPosition(String placement) {
		try {
			toServer.println(placement);
			String result = fromServer.readLine();
			if (result == null)
				System.exit(0);
			return result.equals("Success");
		} catch (IOException e) {
			System.out.println("@invalidPosition " + e);
			System.exit(1);
		}
		return false;
	}
	void updateGameState(String placement, int board, int status){
		if (gui != null){
			int p[];
			int state[] = new int[2];
			if (placement.length() > 3){
				p = Worker.getCoords(placement);
				state[0] = 0;
				state[1] = status;
			}
			else{
				System.out.println("updateGameState single");
				p = Worker.stringToPosition(placement);
				state[0] = board;
				state[1] = status;
			}
			gui.updateBoardState(p, state);
		}
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
				updateGameState(target,1 ,1);
				return true;
			}			
			if (result.equals("Invalid")){
				System.out.println("Invalid target");
				updateGameState(target, 1,-1);
				return true;
			}
			System.out.println("Miss!");
			updateGameState(target, 1, 0);
		} catch (IOException e) {
			System.out.println("@isHit " + e);
			System.exit(1);
		}	
		return false;
	}
	
	String getFirePosition(){
		if (gui != null)
			return gui.getInput(1);
		else 
			return getPlacement();
	}
}
