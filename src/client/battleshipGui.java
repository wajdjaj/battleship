package client;
import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import java.awt.Color;

import javax.swing.BoxLayout;

import java.awt.GridBagLayout;
import java.awt.GridLayout;



import javax.swing.border.LineBorder;

public class battleshipGui {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					battleshipGui window = new battleshipGui();
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
	public battleshipGui() {
		initialize();
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
		JButton[][] playBoard = fillGrid(playerGrid);
		
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
		JButton[][] opponentBoard = fillGrid(opponentGrid);

		JPanel buttonPanelviewTwo = new JPanel();
		buttonPanelviewTwo.setBounds(320, 0, 245, 480);
		bodyPanel.add(buttonPanelviewTwo);

		JPanel headerPanel = new JPanel();
		headerPanel.setBounds(0, 0, 900, 120);
		frame.getContentPane().add(headerPanel);

	}

	public JButton[][] fillGrid(JPanel jp) {
		JLabel[] a_j = new JLabel[11];
		JLabel[] one_ten = new JLabel[11];
		JButton[][] playBoard = new JButton[10][10];
		
		for (int i = 0; i < 11; i++){
			one_ten[i] = new JLabel();
			a_j[i] = new JLabel();
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
					jp.add(playBoard[j-1][i-1]);
				}
			}
		}
		return playBoard;
	}

}
