package course.oop.objects;

import java.io.Serializable;

public class Board implements Serializable {
	private String [][] board;
	private int boardFilled;

	public Board(int size) {
		board = new String[size][size];
//		boardFilled = 0;
	}
	
	public boolean placePiece(int row, int col, String marker) {
		// If column and row are between 0 and size of board
		if(row < board.length && row >= 0 && col < board.length && row >= 0) {
			if(board[row][col] == null) {
				board[row][col] = marker;
				boardFilled++;
				return true;
			}
		}
		return false;
	}
	
	public String checkWinner() {

		for(int i = 0; i < board.length; i++) {
			if(checkRow(i)) {
				return board[i][0];
			}
			if(checkColumn(i)) {
				return board[0][i];
			}
		}
		if(checkDiagonals()) {
			return board[1][1];
		}
		if(boardFilled == board.length*board[0].length){
			return "TIE";
		}

		return null;
	}
	private boolean checkRow(int row) {
		String mark = board[row][0];
		for(int i = 1; i < board[row].length; i++) {
			if(board[row][i] == null) 
				return false;
			if (board[row][i].equals(mark))
				continue;
			else return false;
		}
		return true;
	}
	
	private boolean checkColumn(int col) {
		String mark = board[0][col];
		for(int i = 1; i < board.length; i++) {
			if(board[i][col] == null)
				return false;
			if (board[i][col].equals(mark))
				continue;
			else return false;
		}
		return true;
	}
	private boolean checkDiagonals() {
		String mark = board[1][1];
		if(mark == null)
			return false;
		boolean checkDiagOne = true;
		
		if(board[0][0] == null)
			checkDiagOne = false;
		if(board[2][2] == null)
			checkDiagOne = false;
		if(checkDiagOne)
			if(mark.equals(board[0][0]) && mark.equals(board[2][2]))
				return true;
		
		if(board[0][2] == null)
			return false;
		if(board[2][0] == null)
			return false;
		if(mark.equals(board[0][2]) && mark.equals(board[2][0]))
			return true;
		return false;
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
