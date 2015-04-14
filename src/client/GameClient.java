package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


public class GameClient {
	Socket socket;
	final int numberOfShips = 7;
	public static void main(String argv[]) {
		new GameClient().run();
	}
	void run(){
		setup();
		play();
	}

	void setup() {
		int placedShips = 0;
		String placement;
		try {
			socket = new Socket("localhost", 30000);
			while (placedShips < numberOfShips) {
				do {
					placement = getPlacement();
				} while (!invalidPosition(placement));
				placedShips++;
			}
		} catch (IOException e) {
			System.out.println(e);
		}

	}
		
	
	boolean validSetup(String shipSetup){
		return false; //Server will confirm if the setup is acceptable or not.
	}
	
	void play(){
		boolean gameEnd = false;
		boolean playerTurn = true; //for now...
		while (!gameEnd){
			updateGameState();
			if (playerTurn){
				String target;
				do{
					target = getFirePosition(); // user pick desired square to shoot at;
				}while(invalidPosition(target));
			}else{
				//Wait for opponent to finish
			}				
		}
	}
	
	int[] getShipTypes(){
		return null;
	}
	
	
	/**
	 * 
	 * @param ship is length of ship to place
	 * @return desired ship position
	 */
	String getPlacement(){
		Scanner scan = new Scanner(System.in);
		String txt = scan.nextLine();
		scan.close();
		return txt;
	}
	
	boolean invalidPosition(String placement) {
		try {
			PrintWriter toServer = new PrintWriter(socket.getOutputStream(),
					true);
			BufferedReader fromServer = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
			toServer.println(placement);
			String result = fromServer.readLine();
			toServer.close();
			fromServer.close();
			return result.equals("Success");
		} catch (IOException e) {
			System.out.println(e);
		}
		return false;
	}
	void updateGameState(){
		
	}
	
	String getFirePosition(){
		return getPlacement();
	}
}
