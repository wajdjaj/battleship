package gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import utility.Utility;

//moved to separate file
public class MouseHandler extends MouseAdapter {
	private int x, y;
	private StringWrapper mouseString;
	public MouseHandler(int y, int x, StringWrapper mouseString) {
		super();
		this.x = x;
		this.y = y;		
		this.mouseString = mouseString;
	}

	
	public void mouseClicked(MouseEvent evt) {
		synchronized (mouseString) {
			mouseString.input = String.format("c%d", Utility.intToChar(y),
					x);
			mouseString.notifyAll();
		}
	}
}