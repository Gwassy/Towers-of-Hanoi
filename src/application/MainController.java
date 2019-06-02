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
            if (disk.isDraggable()) {
                disk.setCursor(Cursor.HAND);
            } else {
                disk.setCursor(Cursor.DEFAULT);
            }
        });

        disk.setOnMousePressed(event -> {
            if (disk.isDraggable()) {
                model.setCurrentDisk(disk);

                disk.setOrgSceneX(event.getSceneX());
                disk.setOrgSceneY(event.getSceneY());
                disk.setOrgTranslateX(disk.getTranslateX());
                disk.setOrgTranslateY(disk.getTranslateY());

                disk.setMouseTransparent(true);
                disk.setCursor(Cursor.CLOSED_HAND);
            } else {
                // Do nothing
            }

            event.consume();
        });

        disk.setOnDragDetected(event -> {
            if (disk.isDraggable()) {
                disk.startFullDrag();
            } else {
                // Do nothing
            }
        });

        disk.setOnMouseDragged(event -> {
            if (disk.isDraggable()) {
                double offsetX = event.getSceneX() - disk.getOrgSceneX();
                double offsetY = event.getSceneY() - disk.getOrgSceneY();
                double newTranslateX = disk.getOrgTranslateX() + offsetX;
                double newTranslateY = disk.getOrgTranslateY() + offsetY;

                disk.setTranslateX(newTranslateX);
                disk.setTranslateY(newTranslateY);
            } else {
                // Do nothing
            }
        });

        disk.setOnMouseReleased(event -> {
            if (disk.isDraggable()) {
                disk.setMouseTransparent(false);
                disk.setCursor(Cursor.DEFAULT);

                // Set it back to its location
                disk.setTranslateX(0);
                disk.setTranslateY(0);
            }
        });
    }

    public void addEventHandlers(VBox vBox) {

        vBox.setOnMouseDragReleased(event -> {
            ObservableList<Node> vBoxChildren = vBox.getChildren();
            Disk currentDisk = model.getCurrentDisk();

            // Create a reference to the disks remaining in the
            // source VBox
            VBox sourceVBox = (VBox) currentDisk.getParent();
            ObservableList<Node> sourceVBoxChildren = sourceVBox.getChildren();


            // If there are smaller disks already present on the target rod,
            // make them undraggable after the current one is added
            if (!vBoxChildren.isEmpty()) {
                for (Node node : vBoxChildren) {
                    Disk childDisk = (Disk) node;
                    childDisk.setDraggable(false);
                }
            }

            // The most recently added disk should be draggable
            currentDisk.setDraggable(true);
            vBoxChildren.add(currentDisk);

            // Reset translate values
            currentDisk.setTranslateX(0);
            currentDisk.setTranslateY(0);

            // Make the topmost disk from the source VBox draggable
            if (!sourceVBoxChildren.isEmpty()) {
                ((Disk) (sourceVBoxChildren.get(0))).setDraggable(true);
            }

            currentDisk.setMouseTransparent(false);
        });
    }
}