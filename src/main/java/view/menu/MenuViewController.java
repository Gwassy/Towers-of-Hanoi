package view.menu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.DataModel;
import org.apache.commons.lang3.StringUtils;
import view.automatic.AutomaticModeController;
import view.manual.ManualModeController;

import java.io.IOException;

public class MenuViewController {
    @FXML
    private ChoiceBox<String> gameModeChoiceBox;

    @FXML
    private TextField nrOfDisksTextField;

    /**
     * Validates the user input (number of disks) and
     * loads the corresponding UI: either the one for
     * the manual mode, or the one for the automatic
     * mode.
     *
     */
    public void startGame(ActionEvent event) {
        Stage stage;
        Parent root = null;
        DataModel model = new DataModel();

        Button button = (Button) event.getSource();
        stage = (Stage) button.getScene().getWindow();

        // Get the mode and the number of disks from
        // the user
        String gameMode = gameModeChoiceBox.getSelectionModel().getSelectedItem();
        String nrOfDisksText = nrOfDisksTextField.getText();
        int nrOfDisks;

        // Validate the number of disks input: it should be a
        // numeric value between 2 and 8
        if (StringUtils.isNumeric(nrOfDisksText)) {
            nrOfDisks = Integer.parseInt(nrOfDisksText);
            if (nrOfDisks < 2 || nrOfDisks > 8) {
                System.out.println("Enter a number between 2 and 8");
                return;
            }
        } else {
            System.out.println("Enter a number");
            return;
        }

        model.createDiskList(nrOfDisks);

        try {
            if (gameMode.equals("Manual")) {
                // Load the UI for the manual game mode
                FXMLLoader manualModeLoader = new FXMLLoader(getClass().getResource("/fxml/manualMode.fxml"));
                root = manualModeLoader.load();
                ManualModeController manualModeController = manualModeLoader.getController();
                manualModeController.initModel(model);
            } else if (gameMode.equals("Automatic")) {
                // Load the UI for the automatic game mode
                FXMLLoader automaticModeLoader = new FXMLLoader(getClass().getResource("/fxml/automaticMode.fxml"));
                root = automaticModeLoader.load();
                AutomaticModeController automaticModeController = automaticModeLoader.getController();
                automaticModeController.initModel(model);
            }

            Scene scene = new Scene(root, 1200, 600);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();

        } catch (IOException ex) {
            System.out.println("Load FXML exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
