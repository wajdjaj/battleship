package gui;

import javax.swing.JButton;

public class GameButton extends JButton{
	private int x, y;
	public GameButton(int x, int y){
		super();	
		this.x = x;
		this.y = y;		
	}
	public int getBoardX(){
		return x;
	}
	public int getBoardY(){
		return y;
	}
}
