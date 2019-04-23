package course.oop.objects;

import java.io.Serializable;

public class Board implements Serializable {
	private String [][] board;
	private int boardFilled;
	private int [] winTracker;
	private int boardSize;

	public Board(int size) {
		board = new String[size][size];
		winTracker = new int[(2*size)+2];
		boardFilled = 0;
		boardSize = size;
	}
	
	public boolean placePiece(int row, int col, String marker, int playerNum) {
		// If column and row are between 0 and size of board
		if(row < board.length && row >= 0 && col < board.length && col >= 0) {
			if(board[row][col] == null) {
				board[row][col] = marker;
				incrementWinners(row,col, playerNum);
				boardFilled++;
				return true;
			}
		}
		return false;
	}
	
	public String checkWinner() {
		for(int i = 0; i < winTracker.length; i++){
			if(Math.abs(winTracker[i]) == boardSize){
				if(i==(winTracker.length-2)){
					return board[boardSize-1][0];
				}else if(i==(winTracker.length-1)){
					return board[0][0];
				}else if(i <boardSize)
					return board[i][0];
				else
					return board[0][i-boardSize];
			}
		}
		if(boardFilled == board.length*board[0].length){
			return "TIE";
		}

		return null;
//		for(int i = 0; i < board.length; i++) {
//			if(checkRow(i)) {
//				return board[i][0];
//			}
//			if(checkColumn(i)) {
//				return board[0][i];
//			}
//		}
//		if(checkDiagonals()) {
//			return board[1][1];
//		}
//		if(boardFilled == board.length*board[0].length){
//			return "TIE";
//		}
//
//		return null;
	}

	public void incrementWinners(int row, int col, int playerNum){

		int val = 0;
		// If player 1, increment
		// If player 2, decrement
		if(playerNum == 1)
			val = 1;
		else
			val = -1;

		winTracker[row] += val;
		winTracker[col+boardSize] += val;
		if((row+col) == (boardSize-1)){
			winTracker[winTracker.length-2] += val;
		}
		if(row == col){
			winTracker[winTracker.length-1]+= val;
		}

	}
	public void newGame(){
	    for(int i = 0; i < board.length; i++){
	        for(int j = 0; j < board[i].length; j++){
	            board[i][j] = null;
            }
        }
	    for(int i = 0; i < winTracker.length; i++)
	    	winTracker[i]=0;

	    boardFilled = 0;
	    System.out.println("Board Size: " + boardSize);
    }

    @Override
	public String toString() {
		String results = new String();
		
		results += String.format("%8s", "-");
		for(int i = 0; i < board.length; i++) {
			
			results += String.format("%8s", "["+i+"]");
		}
		results += "\n";
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[i].length; j++) {
//				results += board[i][j] + " ";
				if(j==0)
					results += "["+i+"]";
				results += String.format("%8s", board[i][j]);
			}
			
//			results.trim();
			results += "\n";
		}
		return results.trim();
	}
}
