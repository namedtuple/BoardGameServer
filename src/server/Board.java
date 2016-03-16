package server;

public class Board {
	
	private char[][] board;
	private int numCols;
	private int numRows;
	
	public Board(int numCols, int numRows){
		this.numCols = numCols;
		this.numRows = numRows;
		board = new char[numCols][numRows];
		for (int c = 0; c < numCols; ++c){
			for (int r = 0; r < numRows; ++r){
				board[c][r] = ' ';
			}
		}
	}
	
	public boolean isValidCoord(int col, int row){
		return col >= 0 && col < numCols && 
				row >= 0 && row < numRows;
	}
	
	public char pieceAt(int col, int row){
		return board[col][row];
	}
	
	public void set(int col, int row, char piece){
		board[col][row] = piece;
	}
	
	public int getNumCols(){
		return this.numCols;
	}
	
	public int getNumRows(){
		return this.numRows;
	}
}
