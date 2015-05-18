package game;


import java.util.ArrayList;

public class Rulebook {
	public static final int DIM = 10;
	private final int shipSetup[] = new int[] {2,2,2,1};
	private final int MISS = -1;
	private final int INVALID = -2;
	private int shipRemain[] = new int[] {16, 16};
	private int ships[][] = new int[2][4];
	private int boards[][][] = new int[2][DIM][DIM];	
	ArrayList<Ship> shipList = new ArrayList<Ship>();
	
	public Rulebook(){
		for (int i = 0; i < DIM; i++){
			for (int j = 0; j < DIM; j++){
				boards[0][i][j] = MISS;
				boards[1][i][j] = MISS;
			}
		}
	}
	//update setup placement
	public boolean updatePlacement(int tp[], int player){
		int[] p = new int[4];
		for(int n = 0; n < 4; n++){
		p[n] = tp[n];
		}
		
		int dx = p[2] - p[0];
		int dy = p[3] - p[1];
		int size = Math.max(Math.abs(dx), Math.abs(dy));
		if (size >= shipSetup.length
				|| ships[player][size] >= shipSetup[size] ||
				(Math.abs(dx) > 0 && Math.abs(dy) > 0)){
			return false;
		}
		int div = size;
		if (div == 0) div = 1;
		dx = dx/(div);
		dy = dy/(div);
		for(int i = 0; i <= size; i++){
			if (!targetIsValid(new int[]{p[0],p[1]}, -player+1) || boards[player][p[0]][p[1]] == 1)
				return false;
			p[0] += dx;
			p[1] += dy;
		}
		for(int i = 0; i <=size; i++){
			boards[player][p[2]][p[3]] = 1;
			p[2] -= dx;
			p[3] -= dy;
		}
		ships[player][size]++;
		return true;
	}
	public void changePosition(){
		
	}
	
	public boolean targetIsValid(int p[], int player){
		return (p[0] < DIM && p[0] >= 0
				&& p[1] < DIM && p[1] >= 0
				&& boards[-player+1][p[0]][p[1]] != INVALID);
	}
	public static boolean targetInsideBoard(Position p){
		return (p.x < DIM && p.x >= 0
				&& p.y < DIM && p.y >= 0);
	}
	public boolean targetIsHit(int p[], int player){
		int tmp = boards[-player+1][p[0]][p[1]];
		boards[-player+1][p[0]][p[1]] = INVALID;
		if (tmp>MISS) shipRemain[-player + 1]--;
		return tmp > MISS;
	}
	
	public boolean isGameOver(){
		for (int i = 0; i < 2; i++){
			System.out.println("Number of ships remaining: " + shipRemain[i]);
			if (shipRemain[i] <= 0)
				return true;
		}
		return false;
	}
	public boolean shipsToPlace(int player){
		for (int i = 0; i < 4; i++){
			if (ships[player][i] < shipSetup[i])
				return true;
		}
		return false;
	}
}
