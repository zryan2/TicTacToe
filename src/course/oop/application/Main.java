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
        Scene scene = new Scene(root.getRoot(), 700, 700);
        scene.getStylesheets().add(getClass().getResource("course/oop/css/stylesheet.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
