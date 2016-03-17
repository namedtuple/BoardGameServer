package games.checkers;

import org.javatuples.Pair;

import games.AbstractGame;
import games.Board;
import server.ServerThread;
import shared.Command;
import shared.GameName;
import shared.Request;

public class CheckersGame extends AbstractGame{

	enum Action{
		MOVE, //moving any piece one step
		JUMP // eating a piece
	}

	enum Turn{
		BLACK,
		RED
	}

	static final int COLS = 8;
	static final int ROWS = 8;

	static final char EMPTY = ' ';
	static final char RED = 'r';
	static final char BLACK = 'b';

	static final char RED_KING = 'R';
	static final char BLACK_KING = 'B';

	private Turn turn;
	private boolean gameOver;

	//last action of current player
	private Action lastAction;

	//must the current player jump?
	private boolean mustJump;

	//coordinates of piece that must jump again
	//for when a player already jumped
	private int mustJumpAgainC;
	private int mustJumpAgainR;

	public CheckersGame(ServerThread player1, ServerThread player2){
		super(player1, player2);

		turn = Turn.BLACK;
		mustJumpAgainC = -1;
		mustJumpAgainR = -1;

		board = new Board(COLS, ROWS);
		setupBoard();
		printBoard();
	}

	public void start(){
		sendWelcomeMessage();
	}


	@Override
	public boolean legalMove(ServerThread player, Request request) {
		if (!isCurrentTurn(player)) {
			System.err.println("Illegal move: not player's turn");
            return false;
		}
		CheckersMove move = CheckersMove.parseMove(request.getRequest());
        System.out.println("Move is legal");
		return validCheckersMove(move);
	}

	@Override
	public void makeMove(ServerThread player, Request request) {
		CheckersMove move = CheckersMove.parseMove(request.getRequest());
		makeCheckersMove(move);

    	//player.send(new Request(Command.VALID_MOVE));
    	player.send(new Request(Command.MOVE_TO, player.getUserName() + " " + move.getDestinationPosition().toString()));
        otherPlayer(player).send(new Request(Command.MOVE_TO, player.getUserName() + " " + move.getDestinationPosition().toString()));

		if(mustJumpAgainC != -1) { //player must continue jumping
			currentPlayer().send(new Request(Command.CONTINUE_JUMP));
	    	player.send(new Request(Command.REMOVE_FROM, player.getUserName() + " " + move.getSourcePosition().toString()));
            otherPlayer(player).send(new Request(Command.REMOVE_FROM, player.getUserName() + " " + move.getSourcePosition().toString()));
            //otherPlayer(player).send(new Request(Command.OPPONENT_MOVED, request.getRequest().substring(4)));
		}
		else{
	    	player.send(new Request(Command.REMOVE_FROM, player.getUserName() + " " + move.getSourcePosition().toString()));
            otherPlayer(player).send(new Request(Command.REMOVE_FROM, player.getUserName() + " " + move.getSourcePosition().toString()));
            //otherPlayer(player).send(new Request(Command.OPPONENT_MOVED, request.getRequest().substring(4)));
			updateTurn();
			sendTurnMessage();
		}
	}


	private boolean validCheckersMove(CheckersMove move){
		//assume players turn has been validated
		char piece = getPieceAt(move.sourceCol, move.sourceRow);

		if (!pieceMatchesSide(piece, turn)){
            System.err.println("  Invalid move: piece doesn't match side.");
			return false;
		}
		return canMove(piece, move);
	}

	private boolean canMove(char piece, CheckersMove move){
		if (getPieceAt(move.destCol, move.destRow) != EMPTY) {
            System.err.println("    Cannot Move: cell not empty");
			return false; //can only move to an empty cell
		}
		if (piece == RED){
			if (move.getDeltaR() <= 0 ) {
                System.err.println("    Cannot Move: RED cannot move backward");
				return false; //cannot move backwards
			}
		}
		else if (piece == BLACK){
			if (move.getDeltaR() >= 0) {
                System.err.println("    Cannot Move: BLACK cannot move backward");
				return false; //cannot move backwards
			}
		}

		if (move.isJump()){ //JUMP
			if (mustJumpAgainC != -1){
                System.out.println("    mustJumpAgainC != -1");
				if (move.sourceCol != mustJumpAgainC || move.sourceRow != mustJumpAgainR){
                    System.err.println("      move.sourceCol != mustJumpAgainC || move.sourceRow != mustJumpAgainR");
					return false; //not moving the required piece to move
				}
			}
			int jumpedC = move.sourceCol + Integer.signum(move.getDeltaC());
			int jumpedR = move.sourceRow + Integer.signum(move.getDeltaR());
			char jumped = getPieceAt(jumpedC, jumpedR);
			return jumped != piece && jumped != EMPTY;
		}
		else if (move.isMove()){ //MOVE
			if (mustJump){ //must jump a piece
                System.err.println("    Cannot Move: must jump a piece");
				return false;
			}
			if (mustJumpAgainC != -1){ //must jump previous piece
                System.err.println("    Cannot Move: must jump previous piece");
				return false;
			}
            System.out.println("    Can Move");
			return true;
		}
		else{
            System.err.println("    Cannot Move: isn't a move");
			return false;
		}
	}

	private void makeCheckersMove(CheckersMove move) {
		char piece = removePiece(move.sourceCol, move.sourceRow);
		placePiece(move.destCol, move.destRow, piece);

		checkForKing(piece, move);

		if (move.isJump()){ //eat the piece
			int jumpedC = move.sourceCol + Integer.signum(move.getDeltaC());
			int jumpedR = move.sourceRow + Integer.signum(move.getDeltaR());
			System.out.println("ABOUT TO EAT PIECE: (" + jumpedC + ", " + jumpedR + ")");
            removePiece(jumpedC, jumpedR);

			//check if piece can jump again
			if (canAct(Action.JUMP, piece, move.destCol, move.destRow)){
				mustJumpAgainC = move.destCol;
				mustJumpAgainR = move.destRow;
			} else {
				mustJumpAgainC = -1;
				mustJumpAgainR = -1;
			}
			lastAction = Action.JUMP;
		}
		else {
			lastAction = Action.MOVE;
		}
		printBoard();
	}


	//converts a piece to king, if necessary
	private void checkForKing(char piece, CheckersMove move){
		if (move.destRow == 1 && piece == BLACK){
			removePiece(move.destCol, move.destRow);
			placePiece(move.destCol, move.destRow, BLACK_KING);
		}
		else if (move.destRow == board.getNumRows() && piece == RED){
			removePiece(move.destCol, move.destRow);
			placePiece(move.destCol, move.destRow, RED_KING);
		}
	}


	//determines next turn, or ends game
	private void updateTurn(){
		//check if current player needs to jump
		mustJump = actionPossible(Action.JUMP, turn);
		if (mustJump && lastAction == Action.JUMP){
			System.out.println("must jump");
			return;
		}
		//otherwise change turn and check their moves
		else {
			System.out.println("doesnt need to jump, changing turn");
			changeTurn();
			mustJump = actionPossible(Action.JUMP, turn);
			if (mustJump){
				return;
			}
			if (!actionPossible(Action.MOVE, turn)){
				endGameWinner(otherPlayer(currentPlayer()));
			}
		}
	}

	//checks if black or red has a jump or move
	private boolean actionPossible(Action action, Turn turn){
		for(int c = 1; c <= COLS; ++c){
			for (int r = 1; r <= ROWS; ++r){
				char piece = getPieceAt(c, r);
				if (pieceMatchesSide(piece, turn)){
					if (canAct(action, piece, c, r)){
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean pieceMatchesSide(char piece, Turn side){
		switch(piece){
		case BLACK:
		case BLACK_KING:
			return side == Turn.BLACK;
		case RED:
		case RED_KING:
			return side == Turn.RED;
		default:
			return false;
		}
	}

	//checks if a piece can jump or move
	private boolean canAct(Action action, char piece, int col, int row){
		int[] deltaCs;
		int[] deltaRs;
		switch(action){
		case MOVE:
			deltaCs = new int[] {-1, 1};
			deltaRs = new int[] {-1, 1};
			break;
		case JUMP:
			deltaCs = new int[] {-2, 2};
			deltaRs = new int[] {-2, 2};
			break;
		default:
			return false;
		}

		for (int dc : deltaCs){
			for (int dr : deltaRs){
				int tempDestC = col + dc;
				int tempDestR = row + dr;
				if (board.isValidCoord(tempDestC, tempDestR)){
					if (validCheckersMove(new CheckersMove(col, row, tempDestC, tempDestR))){
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean isCurrentTurn(ServerThread player){
		if (turn == Turn.BLACK){
            System.out.println("Turn OK");
			return player == player1;
		}
		else {
            System.err.println("Turn not OK");
			return player == player2;
		}
	}

	private void changeTurn(){
		turn = getOppositeTurn(turn);
	}

	private Turn getOppositeTurn(Turn turn){
		return turn == Turn.BLACK ? Turn.RED : Turn.BLACK;
	}

	public ServerThread currentPlayer(){
		return turn == Turn.BLACK ? player1 : player2;
	}

	//can be put into parent class
	//function can be used to set-up test scenarios
	private void setupBoard(){
		//first three rows, place Red on black squares
		for (int r = 1; r <= 3; ++r){
			for (int c = 1; c <= COLS; ++c){
				if (isDarkSquare(c, r)){
					placePiece(c, r, RED);
				}
			}
		}
		//bottom three rows, place Red on black squares
		for (int r = ROWS - 2; r <= ROWS; ++r){
			for (int c = 1; c <= COLS; ++c){
				if (isDarkSquare(c, r)){
					placePiece(c, r, BLACK);
				}
			}
		}
	}

	private void printBoard(){
		System.out.println("===========");
		System.out.print("  ");
		for (int c = 1; c <= COLS; ++c){
			System.out.print(c + " ");
		}
		System.out.println();
		for (int r = 1; r <= ROWS; ++r){
			System.out.print(r + " ");
			for (int c = 1; c <= COLS; ++c){
				System.out.print(getPieceAt(c, r) + " ");
			}
			System.out.println();
		}
	}


	private char getPieceAt(int col, int row){
		String piece = board.getCell(Pair.with(col, row)).getOccupant();
		if (piece == null) {
            return EMPTY;
        } else {
            return piece.charAt(0);
        }
	}

	private char removePiece(int col, int row){
		char piece = getPieceAt(col, row);
		board.getCell(Pair.with(col, row)).removeAllOccupants();
        Pair<Integer, Integer> pair = Pair.with(col, row);
        player1.send(new Request(Command.REMOVE_FROM, player1.getUserName() + " " + pair.toString()));
        otherPlayer(player1).send(new Request(Command.REMOVE_FROM, player1.getUserName() + " " + pair.toString()));
        //board.getCell(Pair.with(col, row)).addOccupant(Character.toString(EMPTY));
		return piece;
	}

	private void placePiece(int col, int row, char piece){
		board.getCell(Pair.with(col, row)).addOccupant(Character.toString(piece));
	}

	private boolean isDarkSquare(int col, int row){
		return (col + row) % 2 != 0;
	}

	private void sendWelcomeMessage() {
        String p1 = player1.getUserName();
		String p2 = player2.getUserName();
        player1.send(new Request(Command.NEW_GAME, GameName.CHECKERS + " " + p1 + " X " + p2 + " O ")); // 'NEW_GAME username1 X username2 O '
		player2.send(new Request(Command.NEW_GAME, GameName.CHECKERS + " " + p1 + " X " + p2 + " O ")); // 'NEW_GAME username2 O username1 X '
	}

	private void sendTurnMessage(){
		if (!gameOver){
			currentPlayer().send(new Request(Command.YOUR_TURN));
			otherPlayer(currentPlayer()).send(new Request(Command.OPPONENT_TURN));
		}
	}
}
