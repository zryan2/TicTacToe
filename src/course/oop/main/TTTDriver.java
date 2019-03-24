//package course.oop.main;
//
//import java.util.Calendar;
//import java.util.Date;
//import java.util.Scanner;
//import java.util.Timer;
//import java.util.TimerTask;
//
//import course.oop.controller.TTTControllerImpl;
//public class TTTDriver extends TimerTask{
//	private static boolean playerOneTurn = true;
//	static boolean stillPlaying = true;
//	static int timeoutSeconds = 0;
//
//	public static void main(String[] args) {
//		// Controller object
//		TTTControllerImpl control = new TTTControllerImpl();
//
//
//
//		// Scanner Object
//		Scanner s = new Scanner(System.in);
//
//		// Prints menu
//		printMenu();
//		int input = s.nextInt();
//		int playerNum = 0;
//		while(input != 5) {
//			if(input == 1){
//				System.out.println("Creating new game...\n");
//				System.out.print("Choose number of players: ");
//				playerNum = s.nextInt();
//				System.out.print("Timeout (seconds) for each turn?: ");
//				timeoutSeconds = s.nextInt();
//
//				if(timeoutSeconds == 0) {
//					timeoutSeconds = 999;
//				}
//			}else if(input == 2) {
//				if(playerNum > 0 && playerNum <= 2) {
//					System.out.println("Creating Player 1...");
//					createPlayer(1, control, s);
//					if(playerNum == 2) {
//						System.out.println("Creating Player 2...");
//						createPlayer(2, control, s);
//					}else {
//						System.out.println("Computer Created.");
//					}
//
//
//					control.startNewGame(playerNum,timeoutSeconds);
//					int winTest = 0;
//
//					stillPlaying = true;
//
//					// Timers Objects
//					TimerTask timerTask = new TTTDriver();
//					Timer timer = new Timer(true);
//					timer.scheduleAtFixedRate(timerTask, 0, 1);
//
//					while(stillPlaying) {
//						int row;
//						int col;
//
//						if(playerOneTurn) {
//
//							if(playerTurn(1, control, s)) {
//								timerCount = 0;
//								getPlayerTurn();
//							}
//							else
//								System.out.println("Invalid placement");
//
//						}else if(!playerOneTurn && playerNum == 2){
//							timerCount = 0;
//							if(playerTurn(2, control, s)) {
//								getPlayerTurn();
//							}
//							else
//								System.out.println("Invalid placement");
//						}else if(!playerOneTurn && playerNum == 1) {
//							System.out.println("Computer choosing...");
//							row = (int)(Math.random()*3);
//							col = (int)(Math.random()*3);
//							System.out.println(row + " " + col);
//							if(control.setSelection(row,col,2))
//								getPlayerTurn();
//						}
//						System.out.println(control.getGameDisplay());
//						winTest = control.determineWinner();
//						if(winTest == 1 || winTest == 2) {
//							System.out.println("\nWinner is Player " + winTest +"!\n");
//							break;
//						}else if (winTest == 3) {
//							System.out.println("\nIt was a Tie. No winner.\n");
//						}
//					}
//					if(!stillPlaying) {
//						System.out.println("\nPlayer has timed out\n");
//					}
//					timer.cancel();
//
//				}else
//					System.out.println("Please re-enter player count or Create a new game.");
//			}else if(input == 5) {
//				System.out.println("Exiting Program");
//				break;
//			}
//			printMenu();
//			input = s.nextInt();
//
//		}
//		System.out.println("Exiting Program");
//
////		System.out.println(control.setSelection(1,1,1));
////		System.out.println(control.getGameDisplay());
////		TODO: Check winners
//	}
//
//	static void printMenu() {
//		System.out.println("\nChoose an option:\n"+
//				"1. Create New Game\n"+
//				"2. Start Game\n"+
//				"5. Quit Game\n");
//	}
//	static void createPlayer(int player, TTTControllerImpl control, Scanner s) {
//		System.out.println("Please enter a name: ");
//		String playerName = s.next();
//		System.out.println("Please enter a marker (1 Chararacter): ");
//		String playerMarker = s.next();
//		control.createPlayer(playerName, playerMarker, player);
//	}
//	static boolean playerTurn(int playerTurn, TTTControllerImpl control, Scanner s) {
//		System.out.println("Player " + playerTurn +
//				"'s Turn:\n");
//		System.out.println("Please enter a row: ");
//		int row = s.nextInt();
//		System.out.println("Please enter a col: ");
//		int col = s.nextInt();
//		return control.setSelection(row, col, playerTurn);
//	}
//
//	static boolean getPlayerTurn() {
//		playerOneTurn = !playerOneTurn;
//		return playerOneTurn;
//	}
//	static int timerCount = 0;
//
//	@Override
//	public void run() {
//		boolean isInterrupted = false;
//		timerCount++;
//		if(timerCount/1000 >= timeoutSeconds) {
//			isInterrupted = true;
//		}
//		if(isInterrupted) {
//			stillPlaying = false;
//		}
//	}
//}
