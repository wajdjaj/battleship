package client;

import gui.GUI;

public class GameClientGui {

	public static void main(String[] args) { 
		GUI gui = new GUI();
		gui.start();
		GameClient client = new GameClient(gui);
		client.run();
	}

}
