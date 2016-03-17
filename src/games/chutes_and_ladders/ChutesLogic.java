package games.chutes_and_ladders;

import games.AbstractGame ;
import games.Board;
import games.Cell;
import org.javatuples.Pair;
import server.Server;
import server.ServerThread;
import shared.Request;

import java.util.HashMap;
import java.util.Random;


public class ChutesLogic extends AbstractGame {

    // Fields
    private HashMap<Cell, Cell> hotmaps;
    private HashMap<String, Pair<Integer, Integer>> playerLocations;
    //private Pair<Integer, Integer> location;
    //private HashMap<ServerThread, Pair<Integer,Integer>> player;
    //private ServerThread currentPlayer;

    public static final int COLS = 10;
    public static final int ROWS = 10;
    
    private ServerThread currentPlayer;
    private Pair<Integer, Integer> player1Loc;
    private Pair<Integer, Integer> player2Loc;

    // Methods
    public ChutesLogic(ServerThread player1, ServerThread player2) {
        super(player1, player2);
        
        currentPlayer = player1;
        board = new Board(COLS, ROWS);
        
        hotmaps = new HashMap<>();

        //ladder values bottom to top, left to right
        hotmaps.put(new Cell(Pair.with(10,1)), new Cell(Pair.with(3,7)));
        hotmaps.put(new Cell(Pair.with(10,4)), new Cell(Pair.with(7,9)));
        hotmaps.put(new Cell(Pair.with(9,10)), new Cell(Pair.with(10,7)));
        hotmaps.put(new Cell(Pair.with(8,8)),  new Cell(Pair.with(4,2)));
        hotmaps.put(new Cell(Pair.with(8,1)),  new Cell(Pair.with(2,6)));
        hotmaps.put(new Cell(Pair.with(5,7)),  new Cell(Pair.with(4,6)));
        hotmaps.put(new Cell(Pair.with(10,5)), new Cell(Pair.with(7,4)));
        hotmaps.put(new Cell(Pair.with(1,3)),  new Cell(Pair.with(1,1)));
        hotmaps.put(new Cell(Pair.with(10,3)), new Cell(Pair.with(10,1)));

        //chute values top down left to right
        hotmaps.put(new Cell(Pair.with(3,1)),  new Cell(Pair.with(3,3)));
        hotmaps.put(new Cell(Pair.with(6,1)),  new Cell(Pair.with(6,3)));
        hotmaps.put(new Cell(Pair.with(8,1)),  new Cell(Pair.with(8,3)));
        hotmaps.put(new Cell(Pair.with(7,2)),  new Cell(Pair.with(4,8)));
        hotmaps.put(new Cell(Pair.with(2,4)),  new Cell(Pair.with(2,9)));
        hotmaps.put(new Cell(Pair.with(4,4)),  new Cell(Pair.with(1,5)));
        hotmaps.put(new Cell(Pair.with(5,5)),  new Cell(Pair.with(8,5)));
        hotmaps.put(new Cell(Pair.with(8,6)),  new Cell(Pair.with(6,8)));
        hotmaps.put(new Cell(Pair.with(9,6)),  new Cell(Pair.with(10,9)));
        hotmaps.put(new Cell(Pair.with(5,9)),  new Cell(Pair.with(6,10)));
    }

    public boolean legalMove() {
        return true;
    }


    /*
    public boolean legalMove(ServerThread currentPlayer) {
        //location=currentPlayer.
        Random r = new Random();
        int roll = r.nextInt(7-1) + 1;

        //Pair<Integer,Integer> tempLocation = player.get(currentPlayer);
        Pair<Integer,Integer> tempLocation = playerLocations.get(currentPlayer.getUserName());


        if(tempLocation.getValue1()!=1){
            if(tempLocation.getValue0()-roll<1){
                return false;
            }
        }
        move(tempLocation,roll);
        return true;
    }*/

    public boolean hasWinner() {
        //ServerThread winner = boardMap.get(Pair.with(1,1)).getOccupant();
        return board.getCell(Pair.with(1,1)).getOccupant() != null;
        //return (winner!=null);
    }

    public boolean tied() {
        return false;
    }

    private void move(Pair<Integer, Integer> current, int roll){
       //even row goes left
        if (current.getValue1()%2==0) {
            if (current.getValue0() + roll > 10) {
                roll = roll - (current.getValue0() - 1);
                current.setAt1(current.getValue1() - 1);
                current.setAt0(10-roll+1);
            } else {
                current.setAt0(current.getValue1() + roll);
            }
        }
            //odd row goes right
        else {

            if (current.getValue1()>1 || (current.getValue1()==1 && current.getValue0()>=8)) {
                if (current.getValue1() - roll < 1) {
                    roll = roll - (10 - current.getValue1());
                    current.setAt1(current.getValue1() - 1);
                    current.setAt0(roll);
                } else {
                    current.setAt0(current.getValue1() - roll);
                }
            }
            else{
                if (current.getValue0()-roll==1){
                    current.setAt0(1);
                }

            }
        }

        Cell curr = board.getCell(current);
        hotPlate(curr);

    }

    private void hotPlate(Cell current) {
        if (hotmaps.containsKey(current)) {
            current=hotmaps.get(current);
        }
    }
    
    @Override
    public void start() {
    	
    }
    
    @Override
    public boolean legalMove(ServerThread player, Request request) {
        return false;
    }
    
    @Override
    public void makeMove(ServerThread player, Request request) {

    }
    
    @Override
    public ServerThread currentPlayer() {
        return null;
    }
}
