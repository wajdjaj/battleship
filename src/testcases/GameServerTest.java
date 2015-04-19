/*package testcases;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import org.junit.Test;

import server.GameServer;
import client.GameClient;

public class GameServerTest {
	@Test
	public void testWaitClientBoardSetup() {
		GameServer server = new GameServer();
		new Thread(server).start();
		FileReader fromFile0 = null;
		FileReader fromFile1 = null;
		try{
			fromFile0 = new FileReader("datafiles/boardsetup");
			fromFile1 = new FileReader("datafiles/boardsetup");
		}catch(Exception e){
			System.out.println("Couldn't open file");
			System.exit(1);
		}
			
		new Thread(new GameClient(fromFile0)).start();
		new Thread(new GameClient(fromFile1)).start();
		try {
			Thread.sleep(5000);			
		} catch (InterruptedException e) {			
			e.printStackTrace();
			System.exit(1);
		}		
		try{
			fromFile0.close();
			fromFile1.close();			
		}catch(IOException e){
			System.exit(1);
		}		
//		int board0[][] = server.getBoard(0);
//		int board1[][] = server.getBoard(1);
		Scanner scanner = null;;
		try {
			scanner = new Scanner(new File("datafiles/truth"));
		} catch (FileNotFoundException e) {
			System.out.println("Couldn't open file");
			System.exit(1);
		}
		for (int i = 0; i < 8; i++){
			for (int j = 0; j < 8; j++){
				int val = scanner.nextInt();
				assertTrue(board0[j][i] == val);
				assertTrue(board1[j][i] == val);
			}
			System.out.println();
		}
		scanner.close();
	}

}*/
