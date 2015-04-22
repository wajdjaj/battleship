package client;

import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.border.LineBorder;

public class GUI {
	JButton[][][] boards;
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
		boards = new JButton[2][10][10];
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

		JPanel playerGrid = createGrid();
		playerView.add(playerGrid);
		boards[0] = fillGrid(playerGrid, 0);
		
		JPanel buttonPanelviewOne = new JPanel();
		buttonPanelviewOne.setBounds(320, 0, 245, 480);
		bodyPanel.add(buttonPanelviewOne);
		JButton battleship = new JButton("Battleship");
		JButton cruiser = new JButton("Cruiser");
		buttonPanelviewOne.add(battleship);
		buttonPanelviewOne.add(cruiser);

		JPanel opponentView = new JPanel();
		opponentView.setBounds(565, 0, 320, 480);
		bodyPanel.add(opponentView);
		opponentView.setLayout(null);

		JPanel opponentGrid = createGrid();
		opponentView.add(opponentGrid);
		boards[1] = fillGrid(opponentGrid, 1);

		JPanel buttonPanelviewTwo = new JPanel();
		buttonPanelviewTwo.setBounds(320, 0, 245, 480);
		bodyPanel.add(buttonPanelviewTwo);

		JPanel headerPanel = new JPanel();
		headerPanel.setBounds(0, 0, 900, 120);

	}
	
	private JPanel createGrid() {
		JPanel grid = new JPanel();
		grid.setBorder(new LineBorder(new Color(0, 0, 0)));
		grid.setBounds(10, 45, 300, 300);
		grid.setLayout(new GridLayout(11, 11, 0, 0));
		return grid;
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
					ImageIcon water = new ImageIcon("water.jpg");
					playBoard[j-1][i-1] = new JButton(water);
					
					playBoard[j-1][i-1].addMouseListener(new MouseHandler(i-1,j,player, mouseString));
					jp.add(playBoard[j-1][i-1]);
				}
			}
		}
		return playBoard;
	}
	//get input
	public String getInput(int player){
		synchronized(mouseString){
			try {
				do{
					mouseString.wait();
					System.out.println(mouseString.input);
				}while (Character.getNumericValue(mouseString.input.charAt(0)) != player);
				return mouseString.input.substring(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}		
		return null;
	}
	//string containing information about what part of the board to update
	void updateBoardState(int p[], int state[]) {
		if (state[1] == -1 || p == null)
			return;
		if (p.length <= 2) { // if fire
			Color color;
			if (state[0] == 0 && state[1] == 1) color = Color.red; // player hit
			else if (state[0] == 1 && state[1] == 1) color = Color.green; // opponent hit
			else color = Color.black;			
			drawFire(p, state[0], color);
		} else if (p.length == 4) {
			drawPlacement(p, state[0]);
		}
	}

	private void drawFire(int p[], int player, Color color) {
		boards[player][p[0]][p[1]].setBackground(color);
		return;
	}
	
	private void drawPlacement(int p[], int player){
		int dx = p[2] - p[0];
		int dy = p[3] - p[1];		
		int size = Math.max(Math.abs(dx), Math.abs(dy));
		if (size == 0) size = 1;
		dx = dx/size;
		dy = dy/size;
		for (int i = 0; i <= size; i++){
			boards[player][p[0]][p[1]].setBackground(Color.cyan);
			p[0] += dx;
			p[1] += dy;
		}			
	}
}

class MouseHandler extends MouseAdapter{
	private int x, y, lplayer;
	private StringWrapper mouseString;
	public MouseHandler(int y,int x, int lplayer, StringWrapper mouseString){
		super();
		this.x = x;
		this.y = y;
		this.lplayer = lplayer;
		this.mouseString = mouseString;	
	}

	public void mouseClicked(MouseEvent evt) {
		synchronized (mouseString) {
			mouseString.input = String.format("%d %c%d", lplayer, intToChar(y), x);
			mouseString.notifyAll();
		}
	}
	private char intToChar(int in){
		return (char)((int)'a'+in);
	}
}

class StringWrapper{ //This construct is necessary for the wait/notify mechanism used in MouseHandler and getInput to work
	String input;	
}

