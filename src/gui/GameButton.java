package gui;

import game.Position;
import game.Ship;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class GameButton extends JButton{
	private Position p;
	private Ship ship;
	public GameButton(int x, int y){
		super();	
		p = new Position(x, y);
	}
	public GameButton(ImageIcon img, int x, int y){
		super(img);
		p = new Position(x, y);
	}
	
	Position getPos(){
		return p;
	}
	public void setShip(Ship ship){
		this.ship = ship;
	}
	public Ship getShip(){
		return ship;
	}
}
