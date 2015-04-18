package client;

public class GameClientGui {

	public static void main(String[] args) { 
		GUI gui = new GUI();
		GameClient client = new GameClient(gui);
		client.run();
	}

}
