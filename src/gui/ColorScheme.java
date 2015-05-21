package gui;

import java.awt.Color;

import javax.swing.JButton;

//global colorscheme
public class ColorScheme {
	static Color board = new JButton().getBackground();
	static Color miss = new Color(0,0,80);
	static Color hitGood = new Color(90,190,90);
	static Color hitBad = Color.red;
	static Color ship = Color.CYAN;
	static Color borderDefault = new Color(100,100,100);
	static Color borderValid = Color.green;
	static Color borderInvalid = Color.red;
			
//	static Color background = new Color(238,238,238);
}
