package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.Socket;

public class GameClient implements Runnable {
	Socket socket;
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
			}
			while (placedShips < numberOfShips-1) {
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
			BufferedReader lbr = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
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
					lbr.readLine();
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
			PrintWriter toServer = new PrintWriter(socket.getOutputStream(),
					true);
			BufferedReader fromServer = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
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
		if (!invalidPosition(target)){
			System.out.println("You scored a crictical hit!");
			return true;
		}
		System.out.println("Invalid target");
		return false;
	}
	
	String getFirePosition(){
		return getPlacement();
	}
}
