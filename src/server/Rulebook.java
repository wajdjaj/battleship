package server;

public class Rulebook {
	public final int DIM = 10;
	private final int shipSetup[] = new int[] {2,2,2,1};
	private int shipRemain[] = new int[] {16, 16};
	private int ships[][] = new int[2][4];
	private int boards[][][] = new int[2][DIM][DIM];	

	public boolean updatePlacement(int p[], int player){
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
	public boolean targetIsValid(int p[], int player){
		return (p[0] < DIM && p[0] >= 0
				&& p[1] < DIM && p[1] >= 0
				&& boards[-player+1][p[0]][p[1]] != 2);
	}
	public boolean targetIsHit(int p[], int player){		
		int tmp = boards[-player+1][p[0]][p[1]];
		boards[-player+1][p[0]][p[1]] = 2;
		return tmp == 1;
	}
	
	public boolean isGameOver(){
		for (int i = 0; i < 2; i++){
			if (shipRemain[i] == 0)
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
