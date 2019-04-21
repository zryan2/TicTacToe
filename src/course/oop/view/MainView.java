package course.oop.view;

import course.oop.controller.TTTControllerImpl;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;

import java.io.*;

public class MainView {
    BorderPane root;
    TTTControllerImpl controller;
    public MainView() {

        try{
            FileInputStream fis = new FileInputStream("data.txt");
            ObjectInputStream ois = new ObjectInputStream(fis);
            controller = (TTTControllerImpl)ois.readObject();
            ois.close();
            fis.close();
        }
        catch(ClassNotFoundException c){
            System.out.println("No old file 1");
            controller = new TTTControllerImpl();
        }catch(FileNotFoundException f){
            System.out.println("No old file 2");
            controller = new TTTControllerImpl();
        }
        catch (IOException e) {}
        root = new BorderPane();
    }

    public GridPane buildMenu(){
        GridPane gPane = new GridPane();
        Button createBtn = new Button("Create Game");
        Button startBtn = new Button("Start Game");
        Button editBtn = new Button("Edit Players");
        Button changeModeBtn = new Button("Change Game Mode");
        Button helpBtn = new Button("Help");
        Button quitBtn = new Button("Quit Game");

        gPane.add(createBtn,0,0);
        int playerCount = controller.getPlayerCount();
        if(playerCount == 1 || playerCount == 2) {
            gPane.add(startBtn, 0, 1);
            gPane.add(editBtn, 0, 2);
            gPane.add(changeModeBtn, 0, 3);
        }
        gPane.add(helpBtn, 0, 4);
        gPane.add(quitBtn,0,5);

        // Create Game Button
        createBtn.setOnAction((event)->{
            root.setCenter(buildCreateView());
        });
        // Start Game Button
        startBtn.setOnAction((event)->{
            root.setCenter(playGameView());
            controller.newGame();
        });

        // Edit Players Button
        editBtn.setOnAction((event)->{
            root.setCenter(editPlayerView());
        });

        // Change Game Mode
        changeModeBtn.setOnAction((event)->{
            root.setCenter(changeGameMode());
        });

        // Help Button
        helpBtn.setOnAction((event)->{
            root.setCenter(helpMenu());
        });
        // Quit Button
        quitBtn.setOnAction((event)->{
            try{
                FileOutputStream fos = new FileOutputStream("data.txt");
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(controller);
                oos.close();
                fos.close();

            }
            catch(FileNotFoundException f){}
            catch (IOException e) {}
            System.exit(20);
        });
        gPane.setAlignment(Pos.CENTER);
        gPane.setVgap(8);
        return gPane;
    }

    public GridPane buildCreateView(){
        GridPane gPane = new GridPane();
        Button backBtn = new Button("Back");
        backBtn.setOnAction((event)->{
            controller.startNewGame(0,0);
            root.setCenter(buildMenu());
        });
        RadioButton vsPlayerBtn = new RadioButton("Vs. Players");
        RadioButton vsComp = new RadioButton("Vs. Computer");
        ToggleGroup modeGroup = new ToggleGroup();

        vsPlayerBtn.setToggleGroup(modeGroup);
        vsPlayerBtn.setUserData("2");
        vsPlayerBtn.setSelected(true);

        vsComp.setToggleGroup(modeGroup);
        vsComp.setUserData("1");

        TextField timeOut = new TextField();
        Button nextBtn = new Button("Next");

        nextBtn.setOnAction((event)->{
            if(timeOut.getText().matches("\\d+")){
                int playerNum = Integer.parseInt(modeGroup.getSelectedToggle().getUserData().toString());
                int timeoutNum = Integer.parseInt(timeOut.getText());
                controller.startNewGame(playerNum, timeoutNum);
                root.setCenter(createPlayerView());
            }else{
                System.out.println("Please enter integers");
            }
        });

        gPane.add(backBtn,0,0);
        gPane.add(new Label("Basic Game Mode"), 1, 1);
        gPane.add(vsPlayerBtn,0,2);
        gPane.add(vsComp,2,2);
        gPane.add(new Label("Timeout (Seconds)"), 0, 3);
        gPane.add(timeOut,1,3);
        gPane.add(nextBtn,1,4);

        gPane.setAlignment(Pos.CENTER);
        gPane.setPadding(new Insets(8,8,8,8));
        gPane.setHgap(8);
        gPane.setVgap(8);
        return gPane;
    }

    public BorderPane createPlayerView(){
        BorderPane bPane = new BorderPane();
        Button submitBtn = new Button("Submit");
        Button backBtn = new Button("Back");
        backBtn.setOnAction((event)->{
            root.setCenter(buildCreateView());
        });

        TextField p1TF = new TextField();
        TextField p1MarkerTF = new TextField();

        TextField p2TF = new TextField();
        TextField p2MarkerTF = new TextField();


        VBox p1Info = new VBox(10);
        VBox p2Info = new VBox(10);

        p1Info.getChildren().addAll(new Label("Player 1"), new Label("Player Name"), p1TF, new Label("Player Marker (1 char)"),
                p1MarkerTF);
        p2Info.getChildren().addAll(new Label("Player 2"), new Label("Player Name"), p2TF, new Label("Player Marker (1 char)"),
                p2MarkerTF);

        bPane.setTop(backBtn);
        if(controller.getPlayerCount() == 2) {
            bPane.setLeft(p1Info);
            bPane.setRight(p2Info);
        }else if(controller.getPlayerCount() == 1){
            bPane.setCenter(p1Info);
        }
        bPane.setBottom(submitBtn);

        submitBtn.setOnAction((event)->{
            if(controller.getPlayerCount() == 1){
                if(p1TF.getText().length() >= 1  && p1MarkerTF.getText().length()>=1 && !p1MarkerTF.getText().equals("C")){
                    // Create Player 1
                    controller.createPlayer(p1TF.getText(), p1MarkerTF.getText(), 1);
                    root.setCenter(buildMenu());
                }
            }else if(controller.getPlayerCount() == 2){
                if(p1TF.getText().length() >= 1 && p2TF.getText().length() >= 1  && p1MarkerTF.getText().length()>=1  && p2MarkerTF.getText().length()>=1 && !p1MarkerTF.getText().equals(p2MarkerTF.getText())){
                    controller.createPlayer(p1TF.getText(), p1MarkerTF.getText(), 1);
                    controller.createPlayer(p2TF.getText(), p2MarkerTF.getText(), 2);
                    root.setCenter(buildMenu());
                }
            }
        });
        p1Info.setMaxWidth(200);
        p2Info.setMaxWidth(200);
        bPane.setAlignment(submitBtn, Pos.TOP_CENTER);
        bPane.setAlignment(p1Info, Pos.CENTER);
        bPane.setAlignment(p2Info, Pos.CENTER);
        bPane.setMaxSize(450,300);
        bPane.setPadding(new Insets(15,15,15,15));
        return bPane;
    }

    public ScrollPane playGameView() {
        ScrollPane scrollPane = new ScrollPane();
        int gameMode = controller.getGameMode();
        BorderPane bPane = new BorderPane();

        HBox topBar = new HBox(5);
        Button quitBtn = new Button("Quit Game");
        quitBtn.setOnAction((event) -> {
            controller.newGame();
            root.setCenter(buildMenu());
        });

        // Path to audio files
        final String loseSoundFile = "src/course/oop/media/lose.mp3";
        final String winSoundFile = "src/course/oop/media/win.mp3";
        Media loseSound = new Media(new File(loseSoundFile).toURI().toString());
        Media winSound = new Media(new File(winSoundFile).toURI().toString());
        MediaPlayer loseMedia = new MediaPlayer(loseSound);
        MediaPlayer winMedia = new MediaPlayer(winSound);
        MediaView loseMediaView = new MediaView(loseMedia);
        MediaView winMediaView = new MediaView(winMedia);

        Text p1Turn = new Text(controller.getPlayerOneName() + "'s Turn");
        Text p1Win = new Text(controller.getPlayerOneName() + " is the Winner!");

        Text p2Turn = new Text(controller.getPlayerTwoName() + "'s Turn");
        Text p2Win = new Text(controller.getPlayerTwoName() + " is the Winner!");
        Pane turnPane = new Pane();
        turnPane.getChildren().add(p1Turn);
        TextField rowTF = new TextField();
        TextField colTF = new TextField();
        TextField boardNumTF = new TextField();

        Button submitBtn = new Button("Submit");
        Button playAgainBtn = new Button("Play Again");

        VBox winnerBox = new VBox(5);
        VBox selectionBox = new VBox(5);
        if (gameMode == 1 || gameMode == 2 || gameMode == 6){
            selectionBox.getChildren().addAll(turnPane, new Label("Row"), rowTF, new Label("Column"), colTF, submitBtn);
        }else if(gameMode == 3) {
            selectionBox.getChildren().addAll(turnPane, new Label("Row"), rowTF,
                    new Label("Column"), colTF,
                    new Label("Board"), boardNumTF, submitBtn);
        }

        VBox playerOneDisplay = setPlayerDisplay(1);
        VBox playerTwoDisplay = setPlayerDisplay(2);

        topBar.getChildren().addAll(quitBtn, winMediaView, loseMediaView);
        bPane.setTop(topBar);
        bPane.setBottom(selectionBox);
        if(gameMode == 1 || gameMode == 2 || gameMode == 6) {
            bPane.setCenter(new Text(controller.getGameDisplay()));
            bPane.setAlignment(new Text(controller.getGameDisplay()),Pos.CENTER);
        }else if(gameMode == 3){
            bPane.setCenter(displayUltimateBoard());
            bPane.setAlignment(displayUltimateBoard(),Pos.CENTER);
        }
        bPane.setLeft(playerOneDisplay);
        bPane.setRight(playerTwoDisplay);
        selectionBox.setAlignment(Pos.CENTER_LEFT);
        winnerBox.setAlignment(Pos.CENTER);

        // Submit Button
        submitBtn.setOnAction((event)-> {
            String rowText = rowTF.getText();
            String colText = colTF.getText();
            if (rowText.matches("\\d") && colText.matches("\\d")) {
                //Get Row and Col from textfields
                int rowNum = Integer.parseInt(rowText);
                int colNum = Integer.parseInt(colText);
                boolean placePieceSuccessful = false;
                // Game Mode: Regular Tic-Tac-Toe (vs Comp & vs Player)
                if(gameMode == 1 || gameMode == 2 || gameMode == 6){
                    placePieceSuccessful = controller.setSelection(rowNum, colNum, controller.getPlayerTurn());
                    if (placePieceSuccessful) {
                        // Changes player turn
                        if (controller.getPlayerCount() == 2) {
                            controller.nextPlayerTurn();
                        }
                        bPane.setCenter(new Text(controller.getGameDisplay()));

                    } else {
                        System.out.println("Invalid move");
                    }
                } // Game Mode: Ultimate Tic Tac Toe
                else if(gameMode == 3){
                    String boardNumText = boardNumTF.getText();
                    if(boardNumText.matches("\\d")){
                        int boardNum = Integer.parseInt(boardNumText);
                        placePieceSuccessful = controller.placeUltimatePiece(rowNum, colNum, controller.getPlayerTurn(), boardNum);
                        if(placePieceSuccessful){
                            // Changes player turn
                            if (controller.getPlayerCount() == 2) {
                                controller.nextPlayerTurn();
                            }
                            bPane.setCenter(displayUltimateBoard());
                            // Switches "Current" Playable Board
                            if(controller.isValidUltimateBoard(controller.currUltimateBoard())){
                                boardNumTF.setDisable(true);
                                boardNumTF.setText(controller.currUltimateBoard()+"");
                            }else{
                                boardNumTF.setDisable(false);
                                boardNumTF.setText("");
                            }
                        }else{
                            System.out.println("Invalid move");
                        }
                    }
                }

                // Check Winner
                // Check if there is a winner
                if(placePieceSuccessful) {
                    int winnerNum = controller.determineWinner();
                    if (winnerNum == 1) { // Winner is player 1
                        winnerBox.getChildren().clear();
                        winnerBox.getChildren().addAll(p1Win, playAgainBtn);
                        controller.playerWin(1);
                        VBox playerDisplay = setPlayerDisplay(1);
                        winMedia.play();
                        bPane.setLeft(playerDisplay);
                        bPane.setBottom(winnerBox);
                    } else if (winnerNum == 2) { // Winner is player 2
                        winnerBox.getChildren().clear();
                        winnerBox.getChildren().addAll(p2Win, playAgainBtn);
                        controller.playerWin(2);
                        VBox playerDisplay = setPlayerDisplay(2);
                        bPane.setRight(playerDisplay);
                        bPane.setBottom(winnerBox);
                        winMedia.play();
                    } else if (winnerNum == 3) { // Tie
//                    winnerBox.getChildren().clear();
//                    winnerBox.getChildren().addAll(new Label("It's a Tie!"), playAgainBtn);
//                    bPane.setBottom(winnerBox);
//                    loseMedia.play();
                        bPane.setBottom(tieBreaker(bPane, winnerBox, playAgainBtn));
                    }

                    // If no winner
                    if (winnerNum == 0) {
                        // Computer plays
                        if (gameMode == 1 || gameMode == 2 || gameMode == 6) {
                            if (controller.getPlayerCount() == 1) {
                                int row = (int) (Math.random() * controller.getBoardSize());
                                int col = (int) (Math.random() * controller.getBoardSize());
                                boolean compMove = controller.setSelection(row, col, 2);
                                System.out.println("Computer's Turn!: " + compMove + " row " + row + " col " + col);
                                while (!compMove) {
                                    row = (int) (Math.random() * controller.getBoardSize());
                                    col = (int) (Math.random() * controller.getBoardSize());
                                    compMove = controller.setSelection(row, col, 2);
                                }
                                // If computer wins
                                if (controller.determineWinner() == 2) {
                                    winnerBox.getChildren().clear();
                                    winnerBox.getChildren().addAll(new Text("Computer is the Winner!"), playAgainBtn);
                                    controller.playerWin(2);
                                    VBox playerDisplay = setPlayerDisplay(2);
                                    bPane.setRight(playerDisplay);
                                    bPane.setBottom(winnerBox);
                                    loseMedia.play();
                                }else if(controller.determineWinner() == 3){
                                    bPane.setBottom(tieBreaker(bPane, winnerBox, playAgainBtn));
                                }
                                bPane.setCenter(new Text(controller.getGameDisplay()));
                            }
                        }
                    }
                }
            }

            // Change Current Player Turn Display
            turnPane.getChildren().clear();
            if (controller.getPlayerTurn() == 1) {
                turnPane.getChildren().add(p1Turn);
            } else {
                turnPane.getChildren().add(p2Turn);
            }
            // Clears textfields
            rowTF.setText("");
            colTF.setText("");
        });

        playAgainBtn.setOnAction((event)->{
            controller.newGame();
            turnPane.getChildren().clear();
            turnPane.getChildren().add(p1Turn);
            bPane.setBottom(selectionBox);
            bPane.setCenter(new Text(controller.getGameDisplay()));

        });
        bPane.setPadding(new Insets(15,15,15,15));
        bPane.setMargin(playerOneDisplay, new Insets(15,15,15,15));
        bPane.setMargin(playerTwoDisplay, new Insets(15,15,15,15));
        bPane.setMargin(selectionBox, new Insets(15,15,15,15));
        bPane.setMargin(winnerBox, new Insets(15,15,15,15));
        scrollPane.setContent(bPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setPadding(new Insets(15,15,15,15));
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        return scrollPane;
    }

    // Create buttons for tic tac toe board
//    private GridPane createTicTacToeButtons(){
//        GridPane gPane = new GridPane();
//        int boardSize = controller.getBoardSize();
//        int row = 0;
//        int col = 0;
//        for(int i = 0; i < boardSize; i++){
//            int row = i;
//            for(int j = 0; j < boardSize; j++){
//                Button button = new Button(" ");
//                button.setOnAction((event)->{
//                    controller.setSelection(i,j,controller.getPlayerTurn());
//                });
//            }
//        }
//        return gPane;
//    }


    // Tie Breaker
    private GridPane tieBreaker(BorderPane gameView, VBox winnerBox, Button playAgain){
        GridPane gPane = new GridPane();
        int randNum = (int)(Math.random()*1000);
        gPane.setAlignment(Pos.CENTER);
        gPane.add(new Text("Tie Breaker!"), 1,0);
        TextField playerOneGuess = new TextField();
        TextField playerTwoGuess = new TextField();

        Text p1Win = new Text(controller.getPlayerOneName() + " is the Winner!");
        Text p2Win = new Text(controller.getPlayerTwoName() + " is the Winner!");
        Text compWin = new Text("Computer is the Winner!");

        if(controller.getPlayerCount() == 1){
            gPane.add(new Text(controller.getPlayerOneName() +"'s Guess"), 1,1);
            gPane.add(playerOneGuess, 1,2);
        }else{
            gPane.add(new Text(controller.getPlayerOneName() +"'s Guess"), 0,1);
            gPane.add(playerOneGuess, 0,2);
            gPane.add(new Text(controller.getPlayerTwoName() +"'s Guess"), 2,1);
            gPane.add(playerTwoGuess, 2,2);
        }
        Button guessBtn = new Button("Guess");
        guessBtn.setOnAction((event)->{
            int guess1 = Integer.parseInt(playerOneGuess.getText());
            int guess2 = -1;
            if(controller.getPlayerCount() == 1){
                guess2 = (int)Math.random()*1000;
            }else{
                guess2 = Integer.parseInt(playerTwoGuess.getText());
            }

            if(Math.abs(guess1-randNum) < Math.abs(guess2-randNum)){
                // Player 1 is winner
                winnerBox.getChildren().clear();
                winnerBox.getChildren().addAll(new Text("Winning Number is: " + randNum), p1Win, playAgain);
                controller.playerWin(1);
                gameView.setBottom(winnerBox);
            }else if(Math.abs(guess1-randNum) > Math.abs(guess2-randNum)){
                // Player 2 is winner
                winnerBox.getChildren().clear();
                if(controller.getPlayerCount() == 2)
                    winnerBox.getChildren().addAll(new Text("Winning Number is: " + randNum), p2Win, playAgain);
                else
                    winnerBox.getChildren().addAll(new Text("Winning Number is: " + randNum), compWin, playAgain);
                controller.playerWin(2);
                gameView.setBottom(winnerBox);

            }else{
                // Tie, so redo?
                gameView.setBottom(tieBreaker(gameView, winnerBox,playAgain));
            }
        });

        gPane.add(guessBtn, 1,3);
        return gPane;
    }
    // Displays player information during game
    private VBox setPlayerDisplay(int playerNum){
        VBox playerDisplay = new VBox(5);
        if(playerNum == 1) {
            playerDisplay.getChildren().addAll(new Label("Player 1"), new Text("Name: " + controller.getPlayerOneName()), new Text("Marker: " + controller.getPlayerOneMarker()),
                    new Text("Win(s): " + controller.getPlayerWin(1)));
        }else {
            if (controller.getPlayerCount() == 2)
                playerDisplay.getChildren().addAll(new Label("Player 2"), new Text("Name: " + controller.getPlayerTwoName()),
                        new Text("Marker: " + controller.getPlayerTwoMarker()), new Text("Win(s): " + controller.getPlayerWin(2)));
            else {
                playerDisplay.getChildren().addAll(new Label("Player 2"), new Text("Name: Computer"), new Text("Marker: C"), new Text("Win(s): " + controller.getPlayerWin(2)));
            }
        }
        playerDisplay.setAlignment(Pos.CENTER);
        return playerDisplay;
    }

    public BorderPane editPlayerView() {

        int playerCount = controller.getPlayerCount();
        BorderPane bPane = new BorderPane();
        Button editP1Btn = new Button("Submit");
        Button editP2Btn = new Button("Submit");

        Button backBtn = new Button("Back");
        backBtn.setOnAction((event) -> {
            root.setCenter(buildMenu());
        });


        TextField p1TF = new TextField(controller.getPlayerOneName());
        TextField p1MarkerTF = new TextField(controller.getPlayerOneMarker());

        TextField p2TF = new TextField(controller.getPlayerTwoName());
        TextField p2MarkerTF = new TextField(controller.getPlayerTwoMarker());


        VBox p1Info = new VBox(10);
        VBox p2Info = new VBox(10);

        Pane p1LabelPane = new Pane(new Label("Player 1: " + controller.getPlayerOneName()));
        Pane p2LabelPane = new Pane(new Label("Player 2: " + controller.getPlayerTwoName()));
        Pane p1MarkerPane = new Pane (new Label("Player Marker: " + controller.getPlayerOneMarker()));
        Pane p2MarkerPane = new Pane (new Label("Player Marker: " + controller.getPlayerTwoMarker()));
        p1Info.getChildren().addAll(p1LabelPane, new Label("Player Name"), p1TF, p1MarkerPane,
                p1MarkerTF, editP1Btn);
        if (playerCount == 2){
            p2Info.getChildren().addAll(p2LabelPane, new Label("Player Name"), p2TF, p2MarkerPane,
                    p2MarkerTF, editP2Btn);
        }

        bPane.setTop(backBtn);
        if(controller.getPlayerCount() == 2) {
            bPane.setLeft(p1Info);
            bPane.setRight(p2Info);
        }else if(controller.getPlayerCount() == 1){
            bPane.setCenter(p1Info);
        }
        editP1Btn.setOnAction((event)->{
            if(p1TF.getText().length() >= 1 && !p1MarkerTF.getText().equals("C") && !p1MarkerTF.getText().equals(p2MarkerTF.getText()) && p1MarkerTF.getText().length()>=1) {
                controller.setPlayerOneName(p1TF.getText());
                controller.setPlayerOneMarker(p1MarkerTF.getText());
                p1LabelPane.getChildren().clear();
                p1LabelPane.getChildren().add(new Label("Player 1: " + controller.getPlayerOneName()));
                p1MarkerPane.getChildren().clear();
                p1MarkerPane.getChildren().add(new Label("Player Marker: " + controller.getPlayerOneMarker()));
            }
        });

        editP2Btn.setOnAction((event)->{
            if(p2TF.getText().length() >= 1 && !p1MarkerTF.getText().equals(p2MarkerTF.getText()) && p2MarkerTF.getText().length()>=1) {
                controller.setPlayerTwoName(p2TF.getText());
                controller.setPlayerTwoMarker(p2MarkerTF.getText());
                p2LabelPane.getChildren().clear();
                p2LabelPane.getChildren().add(new Label("Player 2: " + controller.getPlayerTwoName()));
                p2MarkerPane.getChildren().clear();
                p2MarkerPane.getChildren().add(new Label("Player Marker: " + controller.getPlayerTwoMarker()));
            }
        });
        return bPane;
    }

    private GridPane displayUltimateBoard(){
        GridPane gPane = new GridPane();
        int currRow = 0;
        int currCol = 0;
        int currBoard = 0;
        for(int i = 0; i < 12; i++){
            if(i%4 == 0){
                for(int j = 0; j < 3; j++)
                    gPane.add(new Text("Board "+ (currBoard+j)), currCol++,currRow);
            }else {
                gPane.add(new Text(controller.getUltimateBoard(currBoard++)), currCol++, currRow);
            }
            if(currCol == 3){
                currCol = 0;
                currRow++;
            }
        }
        gPane.setHgap(20);
        gPane.setVgap(20);
        gPane.setAlignment(Pos.CENTER);
        return gPane;
    }
    public VBox boardSizeView(){
        VBox vBox = new VBox(10);
        Button backBtn= new Button("Back");
        TextField boardSize = new TextField();
        Button submitBtn = new Button("Submit");
        vBox.getChildren().addAll(backBtn, new Label("Size of Board (min. 3)"), boardSize, submitBtn);
        backBtn.setOnAction((event)->{
          root.setCenter(changeGameMode());
        });

        submitBtn.setOnAction((event)->{
            controller.changeGameMode(6);
            String boardSizeText = boardSize.getText();
            if(boardSizeText.matches("\\d")){
                controller.newGame(Integer.parseInt(boardSizeText));
                root.setCenter(buildMenu());
            }

        });
        return vBox;
    }
    public BorderPane changeGameMode(){
        BorderPane bPane = new BorderPane();
        Button vsPlayerBtn = new Button("Vs. Player");
        Button vsComBtn = new Button("Vs. Computer");
        Button ultimateBtn = new Button("Ultimate Tic Tac Toe");
        Button nXnBtn = new Button("n x n Tic Tac Toe");
        Button backBtn = new Button("Back");
        backBtn.setOnAction((event) -> {
            root.setCenter(buildMenu());
        });

        GridPane gameModeDisplay = new GridPane();
        gameModeDisplay.add(vsPlayerBtn, 0, 0);
        gameModeDisplay.add(vsComBtn, 1, 0);
        gameModeDisplay.add(ultimateBtn, 2, 0);
        gameModeDisplay.add(nXnBtn,1,1);

        vsPlayerBtn.setOnAction((event)->{
            controller.changeGameMode(1);
            root.setCenter(editPlayerView());
        });

        vsComBtn.setOnAction((event)->{
            controller.changeGameMode(2);
            root.setCenter(buildMenu());
        });

        ultimateBtn.setOnAction((event)->{
            controller.changeGameMode(3);
            root.setCenter(buildMenu());
        });

        nXnBtn.setOnAction((event)->{
            root.setCenter(boardSizeView());
        });
        gameModeDisplay.setAlignment(Pos.CENTER);
        gameModeDisplay.setHgap(8);
        gameModeDisplay.setVgap(8);
        bPane.setAlignment(gameModeDisplay, Pos.CENTER);
        bPane.setTop(backBtn);
        bPane.setCenter(gameModeDisplay);
        return bPane;
    }
    public ScrollPane helpMenu(){
        ScrollPane scrollPane = new ScrollPane();
        VBox vBox = new VBox(10);
        Button backBtn = new Button("Back");
        backBtn.setOnAction((event)->{
            root.setCenter(buildMenu());
        });
        Label normalLabel = new Label("Regular Tic-Tac-Toe (Vs. Player / Vs. Computer");
        Text normalhelpText = new Text("Play against another player or a computer in a basic 3x3 Tic-Tac-Toe Board!");
        Label ultimateLabel = new Label("Ultimate Tic-Tac-Toe");
        Text ultimateHelpText = new Text("Player against another player\n" +
                "1. Win three games of Tic Tac Toe in a row.\n" +
                "2. You may only play in the big field that corresponds to the \n" +
                "   last small field your opponent played.\n" +
                "3. When your are sent to a field that is already decided, you choose freely");
        Label nxnLabel = new Label("N x N Tic-Tac-Toe");
        Text nxnHelpText = new Text("Play against another player or a computer. \n" +
                "Choose a number N, and you are given an NxN board. \n" +
                "The winner is decided when N in a row is achieved.");
        vBox.getChildren().addAll(backBtn, normalLabel, normalhelpText, ultimateLabel, ultimateHelpText, nxnLabel, nxnHelpText);
        vBox.setAlignment(Pos.CENTER);
        vBox.setMaxWidth(200);
        vBox.setPadding(new Insets(15,15,15,15));

        scrollPane.setContent(vBox);
        scrollPane.setPadding(new Insets(15,15,15,15));
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        return scrollPane;
    }
    public BorderPane getRoot(){
        Label title= new Label("Tic-Tac-Toe");
        root.setTop(title);
        root.setCenter(buildMenu());
        root.setAlignment(title,Pos.CENTER);
        root.setAlignment(buildMenu(), Pos.CENTER);
        return root;
    }
}
