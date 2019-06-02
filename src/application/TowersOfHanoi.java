package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.DataModel;

public class TowersOfHanoi extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("mainWindow.fxml"));
        Parent root = fxmlLoader.load();
        MainController mainController = fxmlLoader.getController();

        // 5 represents the number of disks that are present at startup
        // on the leftmost rod
        DataModel model = new DataModel(5);
        mainController.initModel(model);

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 1100, 600));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
