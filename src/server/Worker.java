package server;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Worker implements Runnable {
	private final CountDownLatch doneSignal;
	private Socket socket;
	int board[][];
	int numberOfShip[];
	final int shipsToPlace = 7; 
	Worker(Socket socket, int[][] board, CountDownLatch doneSignal) {
		this.socket = socket;
		this.doneSignal = doneSignal;
		this.board = board;
		numberOfShip = new int[] {2,2,2,1};
	}

	public void run() {
		try{
			System.out.println("Inside worker");
			PrintWriter toClient = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader fromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));			
			int placedShips = 0;
			String placement;
			while ((placement = fromClient.readLine()) != null && placedShips != shipsToPlace){
				System.out.println("Worker is working. Just received line " + placement);
				if (!placeShip(placement)){
					toClient.println("Invalid position");
				}
				else{
					placedShips++;
					toClient.println("Success");
					System.out.println("Placed boat at" + placement);
				}
			}
			doneSignal.countDown();
		}catch(IOException e){
			System.out.println(e);
		}
	}
	
	boolean placeShip(String placement){
		Pattern pattern = Pattern.compile("(\\d) ([a-hA-H][\\d]) ([a-hA-H][\\d])");
		Matcher matcher = pattern.matcher(placement);
		int p[] = new int[4];
		if (matcher.find()){
			int size = Integer.parseInt(matcher.group(1));
			for (int i = 0; i < 2; i++){
				p[i*2] = charToInt(matcher.group(i+2).charAt(0));
				p[i*2+1] = Integer.parseInt(matcher.group(i+2).substring(1));
			}			
			for (int i = 0; i < 4; i++){
				if (p[i] >= 8 && p[i] < 0) // if position is outside allowed region return false
					return false;
			}			
			if (p[0] == p[2] && Math.abs(p[1]-p[3])+1 == size) return addShipToBoard(size, p);
			if (p[1] == p[3] && Math.abs(p[0]-p[2])+1 == size) return addShipToBoard(size, p);
		}			
		return false;
	}
	int charToInt(char in){		
		return Character.toLowerCase(in) - 'a';
	}

	boolean addShipToBoard(int size, int p[]) {
		size = size - 1; // reducing size in order for it to work as an index
		if (size < 1 && size > 4)
			return false;
		if (numberOfShip[size] < 1)
			return false;
		int changeX = (p[2] - p[0]) / size;
		int changeY = (p[3] - p[1]) / size;
		int currentX = p[0];
		int currentY = p[1];
		for (int i = 0; i <= size; i++) {
			if (board[currentX][currentY] != 0)
				return false;
			currentX += changeX;
			currentY += changeY;
		}
		currentX = p[0];
		currentY = p[1];
		for (int i = 0; i <= size; i++) {
			board[currentX][currentY] = 1;
			currentX += changeX;
			currentY += changeY;
		}
		numberOfShip[size]--;
		return true;
	}
}
