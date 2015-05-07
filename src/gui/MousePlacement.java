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
	static Ship ship;
	static Position initial;
	static boolean moveInProgress = false;
	static GameButton lastEntered;
	public MousePlacement(GameButton board[][], StringWrapper mouseInput){
		this.board = board;
		this.mouseString = mouseInput;
	}
	public void mousePressed(MouseEvent e){
		System.out.println("Mouse pressed");
		GameButton button = (GameButton)e.getSource();
		ship = button.getShip();
		if (ship != null){
			System.out.println("Mouse movement initiated");
			moveInProgress = true;
			initial = button.getPos();
		}
	}
	public void mouseEntered(MouseEvent e){
		System.out.println("Mouse entered");
		if (moveInProgress){			
			GameButton button = (GameButton) e.getSource();
			lastEntered = button;
			Position p = button.getPos();
			LinkedList<Position> positionOffsets = ship.offset(p.x - initial.x, p.y - initial.y);
			for (Position elem : positionOffsets){
				if (Rulebook.targetInsideBoard(elem) && board[elem.x][elem.y].getShip() == null ){
					//board[elem.x][elem.y].setBorder(BorderFactory.createLineBorder(Color.green));
				}else{
					//board[elem.x][elem.y].setBorder(BorderFactory.createLineBorder(Color.red));
				}
			}
		}
	}
	public void mouseExited(MouseEvent e){
		System.out.println("Mouse exited");
		if (moveInProgress){
			GameButton button = (GameButton) e.getSource();
			Position p = button.getPos();
			LinkedList<Position> positionOffsets = ship.offset(p.x - initial.x, p.y - initial.y);
			for (Position elem : positionOffsets){
				if (Rulebook.targetInsideBoard(elem)){
					//board[elem.x][elem.y].setBorder(BorderFactory.createLineBorder(Color.white));					
				}
			}
		}
	}
	public void mouseReleased(MouseEvent e){
		System.out.println("Mouse released");
		//GameButton button = (GameButton)e.getSource();
		Position p = lastEntered.getPos();
		if (moveInProgress){
			System.out.println("Mouse movement finished");			
			boolean valid = true;
			LinkedList<Position> positionOffsets = ship.offset(p.x-initial.x, p.y-initial.y);
			for (Position elem : positionOffsets){
				if (!Rulebook.targetInsideBoard(elem) || board[elem.x][elem.y].getShip() != null){
					valid = false;
					break;
				}
			}
			if (valid){
				System.out.println("is valid");
				LinkedList<Position> oldPositions = ship.getPositions();
				for (Position elem : oldPositions){
					board[elem.x][elem.y].setShip(null);
					board[elem.x][elem.y].setBackground(ColorScheme.background);
				}
				for (Position elem : positionOffsets){
					board[elem.x][elem.y].setShip(ship);
					board[elem.x][elem.y].setBackground(ColorScheme.ship);
				}
				ship.updateShipPosition(positionOffsets);
			}
			moveInProgress = false;
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
}
