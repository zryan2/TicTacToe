package course.oop.view;

import course.oop.controller.TTTControllerImpl;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
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
        Button quitBtn = new Button("Quit Game");

        gPane.add(createBtn,0,0);
        int playerCount = controller.getPlayerCount();
        if(playerCount == 1 || playerCount == 2) {
            gPane.add(startBtn, 0, 1);
            gPane.add(editBtn, 0, 2);
        }
        gPane.add(quitBtn,0,3);

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
//                 TODO: Switch view to Create Players
                root.setCenter(createPlayerView());
            }else{
                System.out.println("Please enter integers");
            }
        });

        gPane.add(backBtn,0,0);
        gPane.add(new Label("Game Mode"), 1, 1);
        gPane.add(vsPlayerBtn,0,2);
        gPane.add(vsComp,1,2);
        gPane.add(new Label("Timeout (Seconds)"), 0, 3);
        gPane.add(timeOut,1,3);
        gPane.add(nextBtn,1,4);

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
                if(p1TF.getText().length() >= 1 && p1MarkerTF.getText().length() == 1){
                    // Create Player 1
                    controller.createPlayer(p1TF.getText(), p1MarkerTF.getText(), 1);
                    root.setCenter(buildMenu());
                }
            }else if(controller.getPlayerCount() == 2){
                if(p1TF.getText().length() >= 1 && p1MarkerTF.getText().length() == 1 && p2TF.getText().length() >= 1 && p2MarkerTF.getText().length() == 1){
                    controller.createPlayer(p1TF.getText(), p1MarkerTF.getText(), 1);
                    controller.createPlayer(p2TF.getText(), p2MarkerTF.getText(), 2);
                    root.setCenter(buildMenu());
                }
            }
        });
        return bPane;
    }

    public BorderPane playGameView(){
        BorderPane bPane = new BorderPane();
        Button quitBtn = new Button("Quit Game");
        quitBtn.setOnAction((event)->{
            controller.newGame();
            root.setCenter(buildMenu());
        });

        Text p1Turn = new Text(controller.getPlayerOneName() + "'s Turn");
        Text p2Turn = new Text(controller.getPlayerTwoName() + "'s Turn");
        Text p1Win = new Text(controller.getPlayerOneName() + " is the Winner!");
        Text p2Win = new Text(controller.getPlayerTwoName() + " is the Winner!");
        Pane turnPane = new Pane();
        turnPane.getChildren().add(p1Turn);
        TextField rowTF = new TextField();
        TextField colTF = new TextField();

        Button submitBtn = new Button("Submit");
        Button playAgainBtn = new Button("Play Again");

        VBox winnerBox = new VBox(5);
        VBox selectionBox = new VBox(5);
        selectionBox.getChildren().addAll(turnPane, new Label("Row"), rowTF, new Label("Column"), colTF, submitBtn);

        bPane.setTop(quitBtn);
        bPane.setBottom(selectionBox);
        bPane.setCenter(new Text(controller.getGameDisplay()));

        submitBtn.setOnAction((event)->{
            String rowText = rowTF.getText();
            String colText = colTF.getText();

            if(rowText.matches("\\d") && colText.matches("\\d")){
                int rowNum = Integer.parseInt(rowText);
                int colNum = Integer.parseInt(colText);

                if(controller.setSelection(rowNum,colNum,controller.getPlayerTurn())){
                    if(controller.getPlayerCount() == 2) {
                        controller.nextPlayerTurn();
                    }
                    turnPane.getChildren().clear();
                    if(controller.getPlayerTurn() == 1){
                        turnPane.getChildren().add(p1Turn);
                    }else{
                        turnPane.getChildren().add(p2Turn);
                    }
                    bPane.setCenter(new Text(controller.getGameDisplay()));

                    // Check if there is a winner
                    System.out.println(controller.determineWinner());
                    int winnerNum = controller.determineWinner();
                    if(winnerNum == 1){
                        winnerBox.getChildren().clear();
                        winnerBox.getChildren().addAll(p1Win, playAgainBtn);
                        bPane.setBottom(winnerBox);
                    }else if(winnerNum == 2){
                        winnerBox.getChildren().clear();
                        winnerBox.getChildren().addAll(p2Win, playAgainBtn);
                        bPane.setBottom(winnerBox);
                    }else if(winnerNum == 3){
                        winnerBox.getChildren().clear();
                        winnerBox.getChildren().addAll(new Label("It's a Tie!"), playAgainBtn);
                        bPane.setBottom(winnerBox);
                    }

                    if(winnerNum == 0) {
                        if (controller.getPlayerCount() == 1) {
                            int row = (int) (Math.random() * 3);
                            int col = (int) (Math.random() * 3);
                            boolean compMove = controller.setSelection(row, col, 2);
                            System.out.println("Computer's Turn!: " + compMove + " row " + row + " col " + col);
                            while (!compMove) {
                                row = (int) (Math.random() * 3);
                                col = (int) (Math.random() * 3);
                                compMove = controller.setSelection(row, col, 2);
                            }
                            if (controller.determineWinner() == 2) {
                                winnerBox.getChildren().clear();
                                winnerBox.getChildren().addAll(new Text("Computer is the Winner!"), playAgainBtn);
                                bPane.setBottom(winnerBox);
                            }
                            bPane.setCenter(new Text(controller.getGameDisplay()));
                        }
                    }

                }else{
                    System.out.println("Invalid move");
                }
                rowTF.setText("");
                colTF.setText("");
            }
        });
        playAgainBtn.setOnAction((event)->{
            controller.newGame();
            turnPane.getChildren().clear();
            turnPane.getChildren().add(p1Turn);
            bPane.setBottom(selectionBox);
            bPane.setCenter(new Text(controller.getGameDisplay()));

        });
        return bPane;
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
        p1Info.getChildren().addAll(p1LabelPane, new Label("Player Name"), p1TF, new Label("Player Marker (1 char)"),
                p1MarkerTF, editP1Btn);
        if (playerCount == 2){
            p2Info.getChildren().addAll(p2LabelPane, new Label("Player Name"), p2TF, new Label("Player Marker (1 char)"),
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
            if(p1TF.getText().length() >= 1 && p1MarkerTF.getText().length() == 1) {
                controller.setPlayerOneName(p1TF.getText());
                controller.setPlayerOneMarker(p1MarkerTF.getText());
                p1LabelPane.getChildren().clear();
                p1LabelPane.getChildren().add(new Label("Player 1: " + controller.getPlayerOneName()));
            }
        });

        editP2Btn.setOnAction((event)->{
            if(p2TF.getText().length() >= 1 && p2MarkerTF.getText().length() == 1) {
                controller.setPlayerTwoName(p2TF.getText());
                controller.setPlayerTwoMarker(p2MarkerTF.getText());
                p2LabelPane.getChildren().clear();
                p2LabelPane.getChildren().add(new Label("Player 2: " + controller.getPlayerTwoName()));
            }
        });
        return bPane;
    }

    public BorderPane getRoot(){
        root.setAlignment(buildMenu(), Pos.CENTER);
        root.setTop(new Label("Tic Tac Toe"));
        root.setCenter(buildMenu());
        return root;
    }
}
