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

//POSSIBLE TODO
// Add Player Start notification
// Inactivate "myboard" and "rdybtn" after "/Ready".
// Announce winner and loser.
// Add Rematch option.
// Add IP-connect ability.
// Add bgm
// Add hit graphics