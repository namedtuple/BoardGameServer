package games.checkers;

import org.javatuples.Pair;

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
		String source1 = splitStr[2]; // '[x,'
		String source2 = splitStr[3]; // 'y]'

        String dest1 = splitStr[4]; // '[x,'
        String dest2 = splitStr[5]; // 'y]'

		int sourceCol = Character.getNumericValue(source1.charAt(1));
		int sourceRow = Character.getNumericValue(source2.charAt(0));
		int destCol = Character.getNumericValue(dest1.charAt(1));
		int destRow = Character.getNumericValue(dest2.charAt(0));

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

    public Pair<Integer, Integer> getSourcePosition() {
        return Pair.with(sourceCol, sourceRow);
    }

    public Pair<Integer, Integer> getDestinationPosition() {
        return Pair.with(destCol, destRow);
    }

}
