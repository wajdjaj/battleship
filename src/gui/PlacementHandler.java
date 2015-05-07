package gui;

import java.awt.Color;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

import javax.swing.JComponent;
import javax.swing.TransferHandler;


//drag and drop support
public class PlacementHandler extends TransferHandler{
	//Somehow specify it only importing from other gameboard jbuttons
	public boolean canImport(TransferHandler.TransferSupport info){
		System.out.println("canImport");
		info.getComponent().setBackground(Color.pink);
		return true;
	}
	public boolean importData(JComponent comp, Transferable t){
		System.out.println("importData");
		comp.setBackground(Color.blue);
		return false;
	}

	
	public int getSourceActions(JComponent c){
		System.out.println("getSourceActions");
		return MOVE;
	}
	
	//This is what I want it to do at least
	public Transferable createTransferable(JComponent c){
		System.out.println("createTransferable");
		return new StringSelection(c.getBackground().toString());
	}
	
	public void exportDone(JComponent c, Transferable t, int action){
		System.out.println("exportDone");
		c.setBackground(Color.red);//do whatever is appropriate for the method
		/*if (action == MOVE){
			;
		}*/
	}
	public void exportData(){
		
	}
}
