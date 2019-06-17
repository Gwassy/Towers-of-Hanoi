package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.DataModel;


public class TowersOfHanoi extends Application {
    private Button startGameButton;
    private ToggleGroup toggleGroup = new ToggleGroup();     // Contains the selection between the game modes (manual/automatic)
    private TextField nrOfDisksText = new TextField("3");    // Variable to store the user given number of disks (default value is 3)

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the UI and get the controller for the manual game from the .fxml file
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("mainWindow.fxml"));
        Scene manualGameScene = fxmlLoader.load();
        MainController mainController = fxmlLoader.getController();

        DataModel model = new DataModel();

        // Build the UI for the game menu
        BorderPane root = getUI();

        // Add the event handler for the start button
        startGameButton.setOnMousePressed(e -> {
            RadioButton selectedRadioButton = (RadioButton) toggleGroup.getSelectedToggle();

            model.createDiskList(Integer.parseInt(nrOfDisksText.getText()));

            if (selectedRadioButton.getText().equals("Manual")) {
                mainController.initModel(model);
                primaryStage.setScene(manualGameScene);
            } else {
            }
        });

        Scene gameMenuScene = new Scene(root, 1100, 600);
        primaryStage.setScene(gameMenuScene);
        primaryStage.show();
    }

    private BorderPane getUI() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(50, 0, 0, 100));

        GridPane outerGridPane = new GridPane();

        Label gameModeLabel = new Label("Game mode: ");
        gameModeLabel.setStyle(" -fx-font: bold 15px 'serif'");

        // Create radio buttons for the game modes and add them to a grid pane
        GridPane toggleGridPane = new GridPane();
        toggleGridPane.setAlignment(Pos.BOTTOM_CENTER);
        RadioButton rb1 = new RadioButton("Manual");
        rb1.setToggleGroup(toggleGroup);
        rb1.setSelected(true);
        RadioButton rb2 = new RadioButton("Automatic");
        rb2.setToggleGroup(toggleGroup);
        toggleGridPane.addRow(0, rb1);
        toggleGridPane.addRow(1, rb2);

        // Add the nodes on row 0 of the outer GridPane (the game modes)
        outerGridPane.addRow(0, gameModeLabel, toggleGridPane);

        // Add the nodes on row 1 of the outer GridPane (the number of disks)
        Label nrOfDisksLabel = new Label("Number of disks (between 2 and 8)");
        nrOfDisksLabel.setStyle(" -fx-font: bold 15px 'serif'");
        outerGridPane.addRow(1, nrOfDisksLabel, nrOfDisksText);

        // Add the button to start the game on the 2nd row of the outer grid pane
        startGameButton = new Button("Start game");
        startGameButton.setStyle(" -fx-font: bold 15px 'serif'");
        outerGridPane.addRow(2, startGameButton);

        root.setCenter(outerGridPane);

        return root;
    }

    public static void main(String[] args) {
        launch(args);
    }
}