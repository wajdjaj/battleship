package game;

public class Position {
	public int x, y;
	public Position(int x, int y){
		this.x=x;
		this.y=y;
	}
	public boolean equals(Position p){
		return x == p.x && y == p.y; 
	}
}
