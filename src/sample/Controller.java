package sample;

import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;

public class Controller {

    @FXML
    public void setOnMouseEntered(MouseEvent e) {
        Disk disk = (Disk) e.getSource();

        disk.setCursor(Cursor.HAND);
    }

    @FXML
    public void setOnMousePressed(MouseEvent e) {
        Disk disk = (Disk) e.getSource();

        disk.setMouseTransparent(true);

        disk.setOrgSceneX(e.getSceneX());
        disk.setOrgSceneY(e.getSceneY());
        disk.setOrgTranslateX(disk.getTranslateX());
        disk.setOrgTranslateY(disk.getTranslateY());
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
    }
}
