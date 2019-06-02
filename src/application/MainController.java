package application;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import model.DataModel;
import model.Disk;

public class MainController {
    private DataModel model;

    @FXML
    VBox vBox1;

    @FXML
    VBox vBox2;

    @FXML
    VBox vBox3;

    public void initModel(DataModel model) {
        if (this.model != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }

        this.model = model;

        vBox1.getChildren().addAll(model.getDiskList());
        addEventHandlersForDisks(model);
        addEventHandlers(vBox2);
        addEventHandlers(vBox3);
    }

    private void addEventHandlersForDisks(DataModel model) {
        ObservableList<Disk> diskList = model.getDiskList();

        for (Disk disk : diskList) {
            addEventHandlers(disk);
        }
    }

    private void addEventHandlers(Disk disk) {
        disk.setOnMouseEntered(event -> {
            disk.setCursor(Cursor.HAND);
        });

        disk.setOnMousePressed(event -> {
            model.setCurrentDisk(disk);

            disk.setOrgSceneX(event.getSceneX());
            disk.setOrgSceneY(event.getSceneY());
            disk.setOrgTranslateX(disk.getTranslateX());
            disk.setOrgTranslateY(disk.getTranslateY());

            disk.setMouseTransparent(true);
            disk.setCursor(Cursor.CLOSED_HAND);

            event.consume();
        });

        disk.setOnDragDetected(event -> {
            disk.startFullDrag();
        });

        disk.setOnMouseDragged(event -> {
            double offsetX = event.getSceneX() - disk.getOrgSceneX();
            double offsetY = event.getSceneY() - disk.getOrgSceneY();
            double newTranslateX = disk.getOrgTranslateX() + offsetX;
            double newTranslateY = disk.getOrgTranslateY() + offsetY;

            disk.setTranslateX(newTranslateX);
            disk.setTranslateY(newTranslateY);
        });

        disk.setOnMouseReleased(event -> {
            disk.setMouseTransparent(false);
            disk.setCursor(Cursor.DEFAULT);

            // Set it back to its location
            disk.setTranslateX(0);
            disk.setTranslateY(0);
        });
    }

    public void addEventHandlers(VBox vBox) {

        vBox.setOnMouseDragReleased(event -> {
            ObservableList<Node> vBoxChildren = vBox.getChildren();
            Disk currentDisk = model.getCurrentDisk();

            vBoxChildren.add(currentDisk);

            // Reset translate values
            currentDisk.setTranslateX(0);
            currentDisk.setTranslateY(0);

            currentDisk.setMouseTransparent(false);
        });
    }
}