package course.oop.objects;

public class UltimateBoard {

    private Board [][] mainBoard;
    private boolean[][] winnerTracker;
    private int currBoardNum;
    private int winCountP1;
    private int winCountP2;
    private String p1Marker;
    private String p2Marker;
    private int boardFillCount;

    public UltimateBoard(String p1Marker, String p2Marker){
        currBoardNum = -1;
        winCountP1 = 0;
        winCountP2 = 0;
        this.p1Marker = p1Marker;
        this.p2Marker = p2Marker;
        boardFillCount = 0;
    }

    // Returns boolean if board is valid with no winner determined yet.
    public boolean validBoard(int boardNum){
        return !winnerTracker[getMainRow(boardNum)][getMainCol(boardNum)];
    }

    public boolean placeMarker(int row, int col, String marker, int playerNum, int boardNum){
        // If board has no winner/tie
        if(validBoard(boardNum)){
            boolean result = mainBoard[getMainRow(boardNum)][getMainCol(boardNum)].placePiece(row,col, marker, playerNum);

            // If can place in board
            if(result){
                // Change current board to the board located at [row][col]
                currBoardNum = changeBoard(row, col);
                checkBoardWinner(boardNum);
                return true;
            }else
                return false;
        }
        else
            return false;
    }



    public void newBoard(){
        mainBoard = new Board[3][3];
        winnerTracker = new boolean[3][3];
        for(int i = 0; i < mainBoard.length; i++){
            for(int j = 0; j < mainBoard[i].length; j++){
                mainBoard[i][j] = new Board(3);
            }
        }
        winCountP1 = 0;
        winCountP2 = 0;
        boardFillCount = 0;
    }

    public void checkBoardWinner(int boardNum){
        int boardRow = getMainRow(boardNum);
        int boardCol = getMainCol(boardNum);
        String winMarker = mainBoard[boardRow][boardCol].checkWinner();
        if(winMarker == null) return;
        if(winMarker.equals(p1Marker)){
            System.out.println("Player 1 won this board | " + boardNum);
            winCountP1++;
            winnerTracker[boardRow][boardCol] = true;
            boardFillCount++;

        }else if(winMarker.equals(p2Marker)){
            System.out.println("Player 2 won this board | " + boardNum);
            winCountP2++;
            winnerTracker[boardRow][boardCol] = true;
            boardFillCount++;
        }else if(winMarker.equals("TIE")){
            System.out.println("No winner on this board | " +boardNum);
            winnerTracker[boardRow][boardCol] = true;
            boardFillCount++;
        }
    }

    public int checkOverallWinner(){
        if(winCountP1== 3)
            return 1;
        else if (winCountP2 == 3)
            return 2;
        else if(boardFillCount == 9)
            return 3;
        return 0;
    }

    public int getCurrBoard(){
        return currBoardNum;
    }
    private int getMainRow(int boardNum){
        return (int)Math.floor(boardNum/3);
    }

    private int getMainCol(int boardNum){
        return boardNum%3;
    }
    private int changeBoard(int row, int col){
        return row*3 + col;
    }

    // Print a specific board
    public String printBoard(int boardNum){
        return mainBoard[getMainRow(boardNum)][getMainCol(boardNum)].toString();
    }
}
