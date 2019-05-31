package sample;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class Controller {
    private Disk g_currentDisk;         //reference to the disk that is currently being dragged

    @FXML
    public void setOnMouseEntered(MouseEvent e) {
        Disk disk = (Disk) e.getSource();

        disk.setCursor(Cursor.HAND);
    }

    @FXML
    public void setOnMousePressed(MouseEvent e) {
        Disk disk = (Disk) e.getSource();

        g_currentDisk = disk;

        disk.setOrgSceneX(e.getSceneX());
        disk.setOrgSceneY(e.getSceneY());
        disk.setOrgTranslateX(disk.getTranslateX());
        disk.setOrgTranslateY(disk.getTranslateY());

        disk.setMouseTransparent(true);
        disk.setCursor(Cursor.CLOSED_HAND);
    }

    @FXML
    public void setOnDragDetected(MouseEvent e) {
        Disk disk = (Disk) e.getSource();

        disk.startFullDrag();
    }

    @FXML
    public void setOnMouseDragged(MouseEvent e) {
        Disk disk = (Disk) e.getSource();

        double offsetX = e.getSceneX() - disk.getOrgSceneX();
        double offsetY = e.getSceneY() - disk.getOrgSceneY();
        double newTranslateX = disk.getOrgTranslateX() + offsetX;
        double newTranslateY = disk.getOrgTranslateY() + offsetY;

        disk.setTranslateX(newTranslateX);
        disk.setTranslateY(newTranslateY);
    }

    @FXML
    public void setOnMouseReleased(MouseEvent e) {
        Disk disk = (Disk) e.getSource();

        disk.setMouseTransparent(false);
        disk.setCursor(Cursor.DEFAULT);
    }

    @FXML
    public void setOnMouseDragReleased(MouseEvent e) {
        StackPane stackPane = (StackPane) e.getSource();
        VBox vBox = (VBox) stackPane.getChildren().get(1);

        ObservableList<Node> vBoxChildren = vBox.getChildren();

        vBoxChildren.add(g_currentDisk);

        // reset translate values
        g_currentDisk.setTranslateX(0);
        g_currentDisk.setTranslateY(0);
    }
}
