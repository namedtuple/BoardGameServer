package server;

public class CheckersGame {

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
	
	private Board board;
	private ServerThread player1;
	private ServerThread player2;
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
		this.player1 = player1;
		this.player2 = player2;
		player1.setCheckersGame(this); //
		player2.setCheckersGame(this); //

		turn = Turn.BLACK;
		gameOver = false;
		mustJumpAgainC = -1;
		mustJumpAgainR = -1;
		
		board = new Board(COLS, ROWS);
		setupBoard();
		printBoard();
	}
	
	public void start(){
		sendWelcomeMessage();
	}
	
	//can be put into parent class
	public void receiveFromPlayer(ServerThread player, String message){
		if (!isCurrentTurn(player)) {
			return; 
		}
		CheckersMove move = CheckersMove.parseMove(message);
		if (validMove(move)){
			player.send("VALID_MOVE");
			makeMove(move);
			otherPlayer(player).send("OPPONENT_MOVED" + message.substring(4));
			
			if(mustJumpAgainC != -1) { //player must continue jumping
				currentPlayer().send("CONTINUE_JUMP"); 
			}
			else{
				updateTurn();
				sendTurnMessage();
			}
		}
		else {
			System.out.println("Invalid Move");
		}
	}
	
	private boolean validMove(CheckersMove move){
		//assume players turn has been validated
		char piece = board.pieceAt(move.sourceCol, move.sourceRow);

		if (!pieceMatchesSide(piece, turn)){
			return false;
		}
		return canMove(piece, move);
	}
	
	private boolean canMove(char piece, CheckersMove move){
		if (board.pieceAt(move.destCol, move.destRow) != EMPTY) {
			return false; //can only move to an empty cell
		}
		if (piece == RED){
			if (move.getDeltaR() <= 0 ) {
				return false; //cannot move backwards
			}
		}
		else if (piece == BLACK){
			if (move.getDeltaR() >= 0) {
				return false; //cannot move backwards
			}
		}

		if (move.isJump()){ //JUMP
			if (mustJumpAgainC != -1){
				if (move.sourceCol != mustJumpAgainC || move.sourceRow != mustJumpAgainR){
					return false; //not moving the required piece to move
				}
			}
			int jumpedC = move.sourceCol + Integer.signum(move.getDeltaC());
			int jumpedR = move.sourceRow + Integer.signum(move.getDeltaR());
			char jumped = board.pieceAt(jumpedC, jumpedR);
			return jumped != piece && jumped != EMPTY;
		}
		else if (move.isMove()){ //MOVE
			if (mustJump){ //must jump a piece
				return false;
			}
			if (mustJumpAgainC != -1){ //must jump previous piece
				return false;
			}
			return true;
		}
		else{
			return false;
		}
	}
	
	//returns true if player made a jump and must keep jumping that same piece
	private void makeMove(CheckersMove move) {
		char piece = removePiece(move.sourceCol, move.sourceRow);
		placePiece(move.destCol, move.destRow, piece);
		
		checkForKing(piece, move);

		if (move.isJump()){ //eat the piece
			int jumpedC = move.sourceCol + Integer.signum(move.getDeltaC());
			int jumpedR = move.sourceRow + Integer.signum(move.getDeltaR());
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
		if (move.destRow == 0 && piece == BLACK){
			removePiece(move.destCol, move.sourceRow);
			placePiece(move.destCol, move.destRow, BLACK_KING);
		}
		else if (move.destRow == board.getNumRows()-1 && piece == RED){
			removePiece(move.destCol, move.sourceRow);
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
				gameOver = true;
				currentPlayer().send("DEFEAT");
				otherPlayer(currentPlayer()).send("VICTORY");
			}
		}
	}

	//checks if black or red has a jump or move
	private boolean actionPossible(Action action, Turn turn){
		for(int c = 0; c < COLS; ++c){
			for (int r = 0; r < ROWS; ++r){
				char piece = board.pieceAt(c, r);
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
					if (validMove(new CheckersMove(col, row, tempDestC, tempDestR))){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private boolean isCurrentTurn(ServerThread player){
		if (turn == Turn.BLACK){
			return player == player1;
		}
		else {
			return player == player2;
		}
	}
	
	private void changeTurn(){
		turn = getOppositeTurn(turn);
	}
	
	private Turn getOppositeTurn(Turn turn){
		return turn == Turn.BLACK ? Turn.RED : Turn.BLACK;
	}
	
	private ServerThread currentPlayer(){
		return turn == Turn.BLACK ? player1 : player2;
	}
	
	private ServerThread otherPlayer(ServerThread player) {
		return player == player1 ? player2 : player1;
	}
	
	//can be put into parent class
	//function can be used to set-up test scenarios
	private void setupBoard(){
		//first three rows, place Red on black squares
		for (int r = 0; r < 3; ++r){
			for (int c = 0; c < COLS; ++c){
				if (isDarkSquare(c, r)){
					placePiece(c, r, RED);
				}
			}
		}
		//bottom three rows, place Red on black squares
		for (int r = ROWS - 3; r < ROWS; ++r){
			for (int c = 0; c < COLS; ++c){
				if (isDarkSquare(c, r)){
					placePiece(c, r, BLACK);
				}
			}
		}
	}
	
	private void printBoard(){
		System.out.println("===========");
		System.out.print("  ");
		for (int c = 0; c < COLS; ++c){
			System.out.print(c + " ");
		}
		System.out.println();
		for (int r = 0; r < ROWS; ++r){
			System.out.print(r + " ");
			for (int c = 0; c < COLS; ++c){
				System.out.print(board.pieceAt(c, r) + " ");
			}
			System.out.println();
		}
	}
	
	private char removePiece(int col, int row){
		char piece = board.pieceAt(col, row);
		board.set(col, row, EMPTY);
		return piece;
	}
	
	private void placePiece(int col, int row, char piece){
		board.set(col, row, piece);
	}
	
	private boolean isDarkSquare(int col, int row){
		return (col + row) % 2 != 0;
	}
	
	private void sendWelcomeMessage(){
		player1.send("WELCOME " + player1.getUserName() + " B " + player2.getUserName() + " R");
		player2.send("WELCOME " + player2.getUserName() + " R " + player1.getUserName() + " B");
	}
	
	private void sendTurnMessage(){
		if (!gameOver){
			currentPlayer().send("YOUR_TURN");
			otherPlayer(currentPlayer()).send("OPPONENT_TURN");
		}
	}
}
