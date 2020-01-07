package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TowersOfHanoi extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader menuViewLoader = new FXMLLoader(getClass().getResource("/fxml/menuView.fxml"));

        Parent root = menuViewLoader.load();

        primaryStage.setTitle("Towers of Hanoi");
        primaryStage.setScene(new Scene(root, 500, 260));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}