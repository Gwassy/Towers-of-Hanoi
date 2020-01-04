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
import manual.ManualModeController;
import model.DataModel;
import automatic.AutomaticModeController;


public class TowersOfHanoi extends Application {
    private Button startGameButton;
    private ToggleGroup toggleGroup = new ToggleGroup();     // Contains the selection between the game modes (manual/automatic)
    private TextField nrOfDisksText = new TextField("3");    // Variable to store the user given number of disks (default value is 3)

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the UI and get the controller for the manual game mode from the .fxml file
        FXMLLoader manualModeLoader = new FXMLLoader(getClass().getResource("/manual/manualMode.fxml"));
        Scene manualModeScene = manualModeLoader.load();
        ManualModeController mainController = manualModeLoader.getController();

        // Load the UI and get the controller for the automatic game mode from the .fxml file
        FXMLLoader automaticModeLoader = new FXMLLoader(getClass().getResource("/automatic/automaticMode.fxml"));
        Scene automaticModeScene = automaticModeLoader.load();
        AutomaticModeController automaticModeController = automaticModeLoader.getController();

        DataModel model = new DataModel();

        // Build the UI for the game menu
        BorderPane root = getGameMenuUI();

        // Add the event handler for the start button
        startGameButton.setOnMousePressed(e -> {
            RadioButton selectedRadioButton = (RadioButton) toggleGroup.getSelectedToggle();

            model.createDiskList(Integer.parseInt(nrOfDisksText.getText()));

            if (selectedRadioButton.getText().equals("Manual")) {
                mainController.initModel(model);
                primaryStage.setTitle("Towers of Hanoi");
                primaryStage.setScene(manualModeScene);
            } else {
                automaticModeController.initModel(model);
                primaryStage.setTitle("Towers of Hanoi");
                primaryStage.setScene(automaticModeScene);
            }
        });

        Scene gameMenuScene = new Scene(root, 1100, 600);
        primaryStage.setScene(gameMenuScene);
        primaryStage.setTitle("Towers of Hanoi");
        primaryStage.show();
    }


    /**
     * Create the user interface for the game menu. The root will be a BorderPane, whose center will contain
     * a GridPane. The GridPane will contain a TextField, in which the user inputs the total number of disks,
     * a ToggleGroup for selecting the desired mode (manual/automatic) and a button for starting the game.
     *
     * @return BorderPane containing the menu
     */
    private BorderPane getGameMenuUI() {
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