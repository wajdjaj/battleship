package testcases;

import static org.junit.Assert.*;

import java.io.StringReader;

import org.junit.Test;

import server.GameServer;
import client.GameClient;

public class GameServerTest {
	public static void oneTimeSetUp(){
		GameServer server = new GameServer();
		server.run();
	}
	@Test
	public void testWaitPlayers() {
		fail("Not yet implemented");
	}

	@Test
	public void testWaitClientBoardSetup() {
		GameServer server = new GameServer();
		new Thread(server).start();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		new Thread(new GameClient(new StringReader("1 A1 A1"))).start();
		new Thread(new GameClient(new StringReader("1 A1 A1"))).start();
		int board0[][] = server.getBoard(0);
		int board1[][] = server.getBoard(1);
		for (int i = 0; i < 8; i++){
			for (int j = 0; i < 8; j++){
				if (i == 0 && j == 0){
					assertTrue(board0[i][j] == 1);
					assertTrue(board1[i][j] == 1);
				}
				else{
					assertTrue(board0[i][j] == 0);
					assertTrue(board1[i][j] == 0);
				}
			}
		}				
	}

}
