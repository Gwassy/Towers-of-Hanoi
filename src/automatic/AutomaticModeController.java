package automatic;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import model.DataModel;

public class AutomaticModeController {
    private DataModel model;

    @FXML
    VBox leftVBox;

    @FXML
    VBox middleVBox;

    @FXML
    VBox rightVBox;

    public void initModel(DataModel model) {
        if (this.model != null) {
            throw new IllegalStateException("Model can only be initialized once.");
        }

        this.model = model;

        // Add the disks to the left VBox
        leftVBox.getChildren().addAll(model.getLeftTower().getDisksOnTower());
    }
}
