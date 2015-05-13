package game;


import gui.GameButton;

import java.util.LinkedList;

import utility.Utility;

public class Ship {
	private LinkedList<Position> shipPosition;
	public Ship(){};
	public Ship(LinkedList<Position> shipPosition){
		this.shipPosition = shipPosition;
	}
	int size(){
		return shipPosition.size(); 
	}
	public LinkedList<Position> offset(int x,int y){
		LinkedList<Position> translated = new LinkedList<Position>();
		for (Position p : shipPosition){
			translated.add(new Position(p.x+x,p.y+y));
		}
		return translated;
	}
	public void updateShipPosition(LinkedList<Position> shipPosition){
		this.shipPosition = shipPosition;
	}
	public LinkedList<Position> getPositions(){
		return shipPosition;
	}
	public void rotate(Position p){
		for(Position elem : shipPosition){
			if(p.x-elem.x != 0){			
				elem.y = p.y - p.x + elem.x;
				elem.x = p.x;
			}
			else if	(p.y-elem.y != 0){
				elem.x = p.x + p.y - elem.y;
				elem.y = p.y;
			}
		}
	}
	
	public String toString(){
		int xmin = 100, xmax = -100, ymin = 100, ymax = -100;
		for (Position p : shipPosition){
			if (xmin > p.x) xmin = p.x;
			if (xmax < p.x) xmax = p.x;
			if (ymin > p.y) ymin = p.y;
			if (ymax < p.y) ymax = p.y;
		}
		return String.format("%c%d %c%d", Utility.intToChar(ymin),
			xmin , Utility.intToChar(ymax), xmin);
	}
	
	public Ship copy(){
		LinkedList<Position> tmp = new LinkedList<Position>();
		for (Position p : shipPosition){
			tmp.add(new Position(p.x,p.y));			
		}
		return new Ship(tmp);
	}
}
