//package server;
//
//import org.javatuples.Pair;
//
//public class ChutesLogic extends AbstractGameLogic {
//    public ChutesLogic(ServerThread currentPlayer, ServerThread otherPlayer, int length) {
//        super(currentPlayer, otherPlayer, length);
//    }
//
//    public boolean legalMove(Pair<Integer, Integer> location, int roll) {
//        if(location.getValue1()!=1){
//            if(location.getValue0()-roll<1){
//                return false;
//          }
//        }
//        return true;
//    }
//
//    @Override
//    public boolean legalMove(Pair<Integer, Integer> location, ServerThread player) {
//        return false;
//    }
//
//    public boolean hasWinner() {
//        char hundred = boardMap.get(Pair.with(1,1));
//        return (hundred!='_');
//    }
//}
