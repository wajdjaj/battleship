package game;


import java.util.LinkedList;

public class Ship {
	private LinkedList<Position> shipPosition;
	int size(){
		return shipPosition.size(); 
	}
	LinkedList<Position> offset(int x,int y){
		LinkedList<Position> translated = new LinkedList<Position>();
		for (Position p : shipPosition){
			translated.add(new Position(p.x+x,p.y+y));
		}
		return translated;
	}
	public void updateShipPosition(LinkedList<Position> pos){
		shipPosition = pos;
	}
}
