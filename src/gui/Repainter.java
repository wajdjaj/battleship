package gui;

import java.awt.Color;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;

import javax.swing.JButton;

//part of drag and drop support
public class Repainter implements DropTargetListener{
	JButton button;
	public Repainter(JButton button){
		this.button = button;
	}
	@Override
	public void dragEnter(DropTargetDragEvent dtde) {	
		button.setBackground(Color.orange);		
	}

	@Override
	public void dragExit(DropTargetEvent dte) {
		button.setBackground(Color.gray);
	}

	
	@Override
	public void dragOver(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drop(DropTargetDropEvent dtde) {
		button.setBackground(Color.red);		
	}

	@Override
	public void dropActionChanged(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub
		
	}
	
}
