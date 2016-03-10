package server;

import org.javatuples.Pair;
import java.util.HashMap;

/**
 * Created by Jonathan on 3/9/16.
 */
public class ChutesLogic extends AbstractGameLogic {
    HashMap<Pair<Integer,Integer>, Pair<Integer,Integer>> hotmaps;
    public ChutesLogic(ServerThread currentPlayer, ServerThread otherPlayer, int length) {
        super(currentPlayer, otherPlayer, length);
       // HashMap<Pair<Integer,Integer>, Pair<Integer,Integer>> hm = null;
        //ladder values bottom to top, left to right
        hotmaps.put(new Pair(10,1),new Pair(3,7));
        hotmaps.put(new Pair(10,4),new Pair(7,9));
        hotmaps.put(new Pair(9,10),new Pair(10,7));
        hotmaps.put(new Pair(8,8),new Pair(4,2));
        hotmaps.put(new Pair(8,1),new Pair(2,6));
        hotmaps.put(new Pair(5,7),new Pair(4,6));
        hotmaps.put(new Pair(10,5),new Pair(7,4));
        hotmaps.put(new Pair(1,3),new Pair(1,1));
        hotmaps.put(new Pair(10,3),new Pair(10,1));

        //chute values top down left to right
        hotmaps.put(new Pair(3,1),new Pair(3,3));
        hotmaps.put(new Pair(6,1),new Pair(6,3));
        hotmaps.put(new Pair(8,1),new Pair(8,3));
        hotmaps.put(new Pair(7,2),new Pair(4,8));
        hotmaps.put(new Pair(2,4),new Pair(2,9));
        hotmaps.put(new Pair(4,4),new Pair(1,5));
        hotmaps.put(new Pair(5,5),new Pair(8,5));
        hotmaps.put(new Pair(8,6),new Pair(6,8));
        hotmaps.put(new Pair(9,6),new Pair(10,9));
        hotmaps.put(new Pair(5,9),new Pair(6,10));
    }

    public boolean legalMove(Pair<Integer, Integer> location, int roll) {
        if(location.getValue1()!=1){
            if(location.getValue0()-roll<1){
                return false;
          }
        }
        return true;
    }


    public boolean legalMove(Pair<Integer, Integer> location, ServerThread player) {
        return false;
    }

    public boolean hasWinner() {
        char hundred = boardMap.get(Pair.with(1,1)).getOccupant().getID();
        return (hundred!='_');
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

        hotPlate(current);

    }
    private void hotPlate(Pair <Integer, Integer> current){
        if (hotmaps.containsKey(current)){
            current=hotmaps.get(current);
        }
    }

    private void setupBoardMap() {
        boardMap = new HashMap<>();
        for (int xCol = 1; xCol <= LENGTH; ++xCol) {
            for (int yRow = 1; yRow <= LENGTH; ++yRow) {
                Pair<Integer, Integer> pair = Pair.with(xCol, yRow);
                boardMap.put(pair, new ChutesBoardTile());

            }
        }


    }
}
