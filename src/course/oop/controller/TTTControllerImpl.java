package course.oop.controller;

import course.oop.controller.TTTControllerInterface;
import course.oop.objects.Board;
import course.oop.objects.Player;
import course.oop.objects.UltimateBoard;

import java.io.Serializable;
import java.util.Timer;

public class TTTControllerImpl implements TTTControllerInterface, Serializable {

	private boolean vsHuman;
	private int timeout;
	private Board board;
	private UltimateBoard ultimateBoard;
	private Player player1;
	private Player player2;
	private Player playerComp;

	private int playerCount;
	private int playerTurn;

	private int gameMode;
	// Constructor
	public TTTControllerImpl() {
		gameMode = 1;
		playerCount = 0;
		playerTurn = 1;
		System.out.println(playerCount);
	}
	
	@Override
	public void startNewGame(int numPlayers, int timeoutInSecs) {
		playerCount = numPlayers;
		switch (numPlayers) {
		case 1:
			vsHuman = false;
			playerComp = new Player("Computer", "C");
			break;
		case 2:
			vsHuman = true;
			playerComp = new Player("Computer", "C");
			break;
		default:
			System.out.println("Invalid");
			return;
		}
		
//		TODO Timeout system
		if(timeoutInSecs == 0) {
			timeout = 999;
		}else
			timeout = timeoutInSecs;
		// Creates a board object
		board = new Board(3);
		System.out.println("New game started with " + numPlayers + " players." +"\n");
	}

	@Override
	public void createPlayer(String username, String marker, int playerNum) {
		if(playerNum == 1 || playerNum == 2) {
			if (playerNum == 1) {
				player1 = new Player(username, marker);
			}else {
				player2 = new Player(username, marker);
			}
			System.out.println(username + " has been created.");
		}else {
			System.out.println("Invalid player");
		}
		
	}

	@Override
	public boolean setSelection(int row, int col, int currentPlayer) {
		if(currentPlayer == 1)
			return board.placePiece(row,col, player1.getMarker());
		else if (currentPlayer == 2) {
			if(vsHuman) {
				return board.placePiece(row, col, player2.getMarker());
			}else
				return board.placePiece(row,col, playerComp.getMarker());
		}
		return false;
	}

	@Override
	public int determineWinner() {
		if(gameMode == 1 || gameMode == 2) {
			String winMarker = board.checkWinner();
			System.out.println(winMarker);
			if (winMarker == null)
				return 0;
			else if (winMarker.equals(player1.getMarker()))
				return 1;
			else if (vsHuman) {
				if (winMarker.equals(player2.getMarker()))
					return 2;
			} else if (!vsHuman) {
				if (winMarker.equals(playerComp.getMarker()))
					return 2;
			}
			return 3;
		}else if(gameMode == 3){
			return ultimateBoard.checkOverallWinner();
		}
		return 0;
	}

	@Override
	public String getGameDisplay() {
		String results = new String();
		if(gameMode == 1 || gameMode == 2)
			results = board.toString();
		return results;
	}

	public boolean placeUltimatePiece(int row, int col, int player, int boardNum){
		if(player == 1){
			return ultimateBoard.placeMarker(row,col,player1.getMarker(), boardNum);
//			return ultimateBoard.placePiece(row,col, player1.getMarker());
		}
		else if (player == 2) {
			if(vsHuman) {
				return ultimateBoard.placeMarker(row,col,player2.getMarker(), boardNum);
			}else
				return ultimateBoard.placeMarker(row,col,playerComp.getMarker(), boardNum);
		}
		return false;
	}

	public String getUltimateBoard(int boardNum){
		return ultimateBoard.printBoard(boardNum);
	}
	public int currUltimateBoard(){
		return ultimateBoard.getCurrBoard();
	}
	public boolean isValidUltimateBoard(int boardNum){
		return ultimateBoard.validBoard(boardNum);
	}


	public int getTimeout() {
		return timeout;
	}

	public int getPlayerCount(){return playerCount;}

	public void newGame(){
		if(gameMode == 1 || gameMode == 2)
			board.newGame();
		else if(gameMode == 3)
			ultimateBoard.newBoard();
		playerTurn = 1;
	}

	public String getPlayerOneName(){
		return player1.getUsername();
	}
	public String getPlayerOneMarker(){
		return player1.getMarker();
	}
	public void setPlayerOneName(String newName){
		player1.setUsername(newName);
	}
	public void setPlayerOneMarker(String newMarker){
		player1.setMarker(newMarker);
	}
	public String getPlayerTwoName(){
		return player2.getUsername();
	}
	public String getPlayerTwoMarker(){
		return player2.getMarker();
	}
	public void setPlayerTwoName(String newName){
		player2.setUsername(newName);
	}
	public void setPlayerTwoMarker(String newMarker){
		player2.setMarker(newMarker);
	}
	public Player getPlayerTwo(){return player2;};
	public void playerWin(int playerNum){
		if(playerNum == 1)
			player1.playerWin();
		else {
			if(playerCount == 2)
				player2.playerWin();
			else
				playerComp.playerWin();
		}
	}
	public int getPlayerWin(int playerNum){
		if(playerNum == 1)
			return player1.getWinCount();
		else {
			if(playerCount == 2)
				return player2.getWinCount();
			else
				return playerComp.getWinCount();
		}
	}

	public int getPlayerTurn(){
		return playerTurn;
	}

	public void nextPlayerTurn(){
		if(playerTurn == 1){
			playerTurn = 2;
		}else{
			playerTurn = 1;
		}
	}

	public int getGameMode(){return gameMode;}
	public void changeGameMode(int gameMode){
		switch(gameMode){
			case 1:
				// Against Player
				System.out.println("vs Player Mode");
				vsHuman = true;
				playerCount = 2;
				break;
			case 2:
				// Against Computer
				System.out.println("vs Computer Mode");
				vsHuman = false;
				playerCount = 1;
				break;
			case 3:
				// Ultimate Tic Tac Toe
				ultimateBoard = new UltimateBoard(player1.getMarker(), player2.getMarker());
				ultimateBoard.newBoard();
				break;
			default:
				System.out.println("Invalid Game Mode");
		}
		this.gameMode = gameMode;

	}
}
