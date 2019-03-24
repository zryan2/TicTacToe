package course.oop.application;

import course.oop.view.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        MainView root = new MainView();
        Scene scene = new Scene(root.getRoot(), 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
