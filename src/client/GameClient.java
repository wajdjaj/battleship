package client;

public class GameClient {
	
	final int numberOfShips = 7;
	public static void main(String argv[]) {
		new GameClient().run();
	}
	void run(){
		setup();
		play();
	}
	void setup(){
		String shipSetup;
		int placedShips = 0;
		while (placedShips < numberOfShips)
			do{
				shipSetup = placeShips();
			}while(!validSetup(shipSetup));
	}	
		
	String placeShips(){
		int shipTypes[] = getShipTypes();
		StringBuilder bob = new StringBuilder();
		for (int ship : shipTypes){
			int placement;
			do{
				placement = getPlacement(ship);
			}while(invalidPosition(placement));
			bob.append(ship + " " + placement);
		}
		return bob.toString();
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
				int target;
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
	int getPlacement(int ship){
		return 0; //Player will specify position of ships in one way or another
	}
	
	boolean invalidPosition(int placement){
		return false;
	}
	void updateGameState(){
		
	}
	
	int getFirePosition(){
		return 0;	//get desired position to fire at from user
	}
}
