package gui;

import game.Position;
import game.Rulebook;
import game.Ship;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import javax.swing.BorderFactory;

import utility.Utility;

public class MousePlacement extends MouseAdapter{
	StringWrapper mouseString;
	GameButton board[][];
	// This is not a good solution
	static Ship ship;
	static Ship tShip;
	static Position lastPosition;
	static boolean moveInProgress = false;
	static GameButton lastEntered;
	static boolean leftDown = false;
	static boolean rightDown = false; 
	public MousePlacement(GameButton board[][], StringWrapper mouseInput){
		this.board = board;
		this.mouseString = mouseInput;
	}
	public void mousePressed(MouseEvent e){		
		if (e.getButton() == MouseEvent.BUTTON1) leftPressed(e);
		else if (e.getButton() == MouseEvent.BUTTON3) rightPressed(e);
	}
	public void mouseEntered(MouseEvent e){
		if (moveInProgress){			
			System.out.println("Mouse entered");
			GameButton button = (GameButton) e.getSource();			 
			lastEntered = button;
			Position p = button.getPos();
			LinkedList<Position> positionOffsets = tShip.offset(p.x - lastPosition.x, p.y - lastPosition.y);
			tShip.updateShipPosition(positionOffsets);
			colorBorder(tShip);
			lastPosition = p;
		}
	}
	public void mouseExited(MouseEvent e){
		if (moveInProgress){
			eraseBorder(tShip);
		}
	}
	public void mouseReleased(MouseEvent e){	
		if (moveInProgress){
			if (e.getButton() == MouseEvent.BUTTON1) leftReleased(new MouseEvent(lastEntered, 0, 0, 0, 0, 0, 0, false));
			else if (e.getButton() == MouseEvent.BUTTON3) rightReleased(new MouseEvent(lastEntered, 0, 0, 0, 0, 0, 0, false));
		}			
	}
	//Need to fix  proper input
	void updateMouseString(int xmin, int ymin, int xmax, int ymax){
		synchronized (mouseString) {
			mouseString.input = String.format("%c%d %c%d", Utility.intToChar(ymin),
					xmin , Utility.intToChar(ymax), xmin);
			mouseString.notifyAll();
		}
	}
	public void leftPressed(MouseEvent e){
		leftDown = true;
		System.out.println("Leftmouse pressed");
		GameButton button = (GameButton)e.getSource();
		ship = button.getShip();
		if (ship != null){
			tShip = ship.copy();
			moveInProgress = true;
			lastPosition = button.getPos();
			setBoard(ship, null);
			lastEntered = button;
		}
	}
	public void rightPressed(MouseEvent e){
		rightDown = true;		
		if (!leftDown){
			leftPressed(e);
			leftDown = false;
		}		
	}
	public void leftReleased(MouseEvent e){
		leftDown = false;
		if (moveInProgress){
			System.out.println("Leftmouse released");
			mouseExited(e);
			if (isValid(tShip)){
				colorShip(ship, ColorScheme.board);
				colorShip(tShip, ColorScheme.ship);
				setBoard(ship, null);
				setBoard(tShip,tShip);
				//updateMouseString()
			}else if (ship != null)
				setBoard(ship, ship);
		}
		moveInProgress = false;
	}
	public void rightReleased(MouseEvent e){
		rightDown = false;
		System.out.println("Rightmouse released");
		if (tShip != null){
			eraseBorder(tShip);
			tShip.rotate(lastPosition);			
			if (leftDown){
				colorBorder(tShip);
				return;
			}
			if (isValid(tShip)){
				colorShip(ship, ColorScheme.board);
				colorShip(tShip, ColorScheme.ship);
				setBoard(tShip, tShip);
			}else if (ship != null){
				setBoard(ship, ship);
			}
			moveInProgress = false;
		}
	}
	boolean isValid(Ship s){
		LinkedList<Position> pos = s.getPositions();
		for (Position p : pos){
			if (!Rulebook.targetInsideBoard(p) || board[p.x][p.y].getShip() != null)
				return false;
		}
		return true;
	}
	
	void colorShip(Ship s, Color c){
		LinkedList<Position> pos = s.getPositions();
		for (Position p : pos){
			board[p.x][p.y].setBackground(c);
		}
	}
	void setBoard(Ship s, Ship to){
		LinkedList<Position> pos = s.getPositions();
		for (Position p : pos){
			board[p.x][p.y].setShip(to);
		}
	}	
	void colorBorder(Ship s){
		LinkedList<Position> pos = s.getPositions();
		for (Position p : pos){
			if (Rulebook.targetInsideBoard(p)){
				if(board[p.x][p.y].getShip() == null ){
					board[p.x][p.y].setBorder(BorderFactory.createLineBorder(ColorScheme.borderValid));
				}else{
					board[p.x][p.y].setBorder(BorderFactory.createLineBorder(ColorScheme.borderInvalid));
				}
			}
		}
	}
	void eraseBorder(Ship s){
		LinkedList<Position> pos = s.getPositions();
		for (Position elem : pos){
			if (Rulebook.targetInsideBoard(elem)){
				board[elem.x][elem.y].setBorder(
						BorderFactory.createLineBorder(ColorScheme.borderDefault));					
			}
		}
	}
}
