package games;

import org.javatuples.Pair;

import java.util.Collection;
import java.util.HashMap;


public class Board {

	private int numCols;
	private int numRows;
    // Fields
    private HashMap<Pair<Integer, Integer>, Cell> boardMap;

    // Methods
    public Board(int length) {
    	this(length, length);
    }
    
    public Board(int rows, int cols){
    	numCols = cols;
    	numRows = rows;
        boardMap = new HashMap<>();
        for (int xCol = 1; xCol <= rows; ++xCol) {
            for (int yRow = 1; yRow <= cols; ++yRow) {
                Pair<Integer, Integer> pair = Pair.with(xCol, yRow);
                boardMap.put(pair, new Cell(pair));
            }
        }
            
    }

    public Cell getCell(Pair<Integer, Integer> location) {
        return boardMap.get(location);
    }

    public Collection<Cell> getCells() {
        return boardMap.values();
    }
    
    public boolean isValidCoord(int col, int row){
		return col >= 1 && col <= numCols && 
				row >= 1 && row <= numRows;
    }
    
    //overload to reduce the amount of code change outside of board
    public boolean isValidCoord(Pair<Integer, Integer> location){
    	return isValidCoord(location.getValue0(), location.getValue1());
    }
    
    public int getNumCols(){
    	return numCols;
    }
    
    public int getNumRows(){
    	return numRows;
    }

}
