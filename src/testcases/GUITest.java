package testcases;

import static org.junit.Assert.*;

import org.junit.Test;

public class GUITest {

	@Test
	public void testGetInput() {
		gui.GUI gui = new gui.GUI();
		gui.start();
		while(true)
			gui.getInput(0);		
	}

}
