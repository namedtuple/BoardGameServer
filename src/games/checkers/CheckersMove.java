package games.checkers;

public class CheckersMove {
	public int sourceCol;
	public int sourceRow;
	public int destCol;
	public int destRow;
	
	public CheckersMove(int srcCol, int srcRow, int destCol, int destRow){
		this.sourceCol = srcCol;
		this.sourceRow = srcRow;
		this.destCol = destCol;
		this.destRow = destRow;
	}
	
	//Expected in the form MOVE [a,b] [c,d]
	public static CheckersMove parseMove(String str){
		String[] splitStr = str.split(" ");
		String source = splitStr[2]; //[x, y]
		String dest = splitStr[5]; //[x, y]

		int sourceCol = Character.getNumericValue(source.charAt(1));
		int sourceRow = Character.getNumericValue(source.charAt(3)); 
		int destCol = Character.getNumericValue(dest.charAt(1));
		int destRow = Character.getNumericValue(dest.charAt(3));

		return new CheckersMove(sourceCol, sourceRow, destCol, destRow);
	}
	
	public boolean isDiagonal(){
		return getAbsDeltaC() == getAbsDeltaR();
	}
	
	public boolean isJump(){
		return isDiagonal() && getAbsDeltaC() == 2;
	}
	
	public boolean isMove(){
		return isDiagonal() && getAbsDeltaC() == 1;
	}
	
	public int getDeltaC(){
		return destCol - sourceCol;
	}
	
	public int getDeltaR(){
		return destRow - sourceRow;
	}
	
	private int getAbsDeltaC(){
		return Math.abs(getDeltaC());
	}
	
	private int getAbsDeltaR(){
		return Math.abs(getDeltaR());
	}
	
}