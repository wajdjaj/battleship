package testcases;

import static org.junit.Assert.*;

import org.junit.Test;

import client.GUI;

public class GUITest {

	@Test
	public void testGetInput() {
		GUI gui = new GUI();
		gui.start();
		while(true)
			gui.getInput();		
	}

}
