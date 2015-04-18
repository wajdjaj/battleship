package client;

import java.awt.EventQueue;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;

import javax.swing.border.LineBorder;

public class GUI {
	
	private JFrame frame;
	private StringWrapper mouseString;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		mouseString = new StringWrapper();
		initialize();
	}
	public void start(){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 900, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JPanel bodyPanel = new JPanel();
		bodyPanel.setBounds(0, 120, 900, 480);
		frame.getContentPane().add(bodyPanel);
		bodyPanel.setLayout(null);

		JPanel playerView = new JPanel();
		playerView.setBounds(0, 0, 320, 480);
		bodyPanel.add(playerView);
		playerView.setLayout(null);

		JPanel playerGrid = new JPanel();
		playerGrid.setBorder(new LineBorder(new Color(0, 0, 0)));
		playerGrid.setBounds(10, 45, 300, 300);
		playerView.add(playerGrid);
		playerGrid.setLayout(new GridLayout(11, 11, 0, 0));
		JButton[][] playBoard = fillGrid(playerGrid, 0);
		
		JPanel buttonPanelviewOne = new JPanel();
		buttonPanelviewOne.setBounds(320, 0, 245, 480);
		bodyPanel.add(buttonPanelviewOne);

		JPanel opponentView = new JPanel();
		opponentView.setBounds(565, 0, 320, 480);
		bodyPanel.add(opponentView);
		opponentView.setLayout(null);

		JPanel opponentGrid = new JPanel();
		opponentGrid.setBorder(new LineBorder(new Color(0, 0, 0)));
		opponentGrid.setBounds(10, 45, 300, 300);
		opponentView.add(opponentGrid);
		opponentGrid.setLayout(new GridLayout(11, 11, 0, 0));
		JButton[][] opponentBoard = fillGrid(opponentGrid, 1);

		JPanel buttonPanelviewTwo = new JPanel();
		buttonPanelviewTwo.setBounds(320, 0, 245, 480);
		bodyPanel.add(buttonPanelviewTwo);

		JPanel headerPanel = new JPanel();
		headerPanel.setBounds(0, 0, 900, 120);

	}
//player Player = 0 Opponent = 1
	public JButton[][] fillGrid(JPanel jp, int player) {
		JLabel[] a_j = new JLabel[11];
		JLabel[] one_ten = new JLabel[11];
		JButton[][] playBoard = new JButton[10][10];
		
		for (int i = 0; i < 11; i++){
			one_ten[i] = new JLabel();
			one_ten[i].setHorizontalAlignment(SwingConstants.CENTER);
			a_j[i] = new JLabel();
			a_j[i].setHorizontalAlignment(SwingConstants.CENTER);
			if (i > 0){
				one_ten[i].setText(Integer.toString(i));
				a_j[i].setText(Character.toString((char)('A'+i-1)));
			}
		}
		for (int i = 0; i < 11; i++){
			for (int j = 0; j < 11; j++){
				if (i == 0) jp.add(one_ten[j]);				
				else if (j == 0) jp.add(a_j[i]);
				else{
					playBoard[j-1][i-1] = new JButton();
					playBoard[j-1][i-1].addMouseListener(new MouseHandler(i-1,j,player, mouseString));
					jp.add(playBoard[j-1][i-1]);
				}
			}
		}
		return playBoard;
	}
	//get input
	public String getInput(){
		synchronized(mouseString){
			try {
				mouseString.wait();
				System.out.println(mouseString.input);
				return mouseString.input;
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}		
		return null;
	}
	//string containing information about what part of the board to update
	void updateBoardState(String update){
		
	}
}

class MouseHandler extends MouseAdapter{
	private int x, y, player;
	private StringWrapper mouseString;
	public MouseHandler(int y,int x, int player, StringWrapper mouseString){
		super();
		this.x = x;
		this.y = y;
		this.player = player;
		this.mouseString = mouseString;	
	}
	public void mouseClicked(MouseEvent evt){
		if (player == 0){
			synchronized(mouseString){
				mouseString.input = String.format("%c%d",intToChar(y), x);
				mouseString.notifyAll();
			}
		}			
	}
	private char intToChar(int in){
		return (char)((int)'a'+in);
	}
}

class StringWrapper{ //This construct is necessary for the wait/notify mechanism used in MouseHandler and getInput to work
	String input;	
}

