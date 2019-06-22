package manual;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.layout.VBox;
import model.DataModel;
import model.Disk;
import model.Tower;

public class ManualModeController {
    private DataModel model;

    @FXML
    VBox leftVBox;

    @FXML
    VBox middleVBox;

    @FXML
    VBox rightVBox;

    // The bounds of the 3 VBoxes; they are used in order to calculate the translation values of the disks
    private Bounds leftVBoxBounds;
    private Bounds middleVBoxBounds;
    private Bounds rightVBoxBounds;

    /**
     * Used to initialize the DataModel.
     * It adds the disks on the left tower as children of the leftVBox and adds
     * event handlers for the disks and for the 3 VBoxes corresponding to the
     * 3 towers.
     *
     * @param model reference to the instance of the DataModel that is to be initialized
     */
    public void initModel(DataModel model) {
        if (this.model != null) {
            throw new IllegalStateException("Model can only be initialized once.");
        }

        this.model = model;

        // Add the disks to the left VBox
        leftVBox.getChildren().addAll(model.getLeftTower().getDisksOnTower());

        // Add the event handlers for the disks and the vBoxes
        addEventHandlersForDisks(model);
        addEventHandlersForVBox(leftVBox);
        addEventHandlersForVBox(middleVBox);
        addEventHandlersForVBox(rightVBox);
    }

    /**
     * Add event handlers for all the disks that are present at startup, by iterating
     * through the disks on the left tower and calling the function that adds event
     * handlers for a single disk.
     *
     * @param model for getting the disks on the left tower
     */
    private void addEventHandlersForDisks(DataModel model) {
        ObservableList<Disk> diskList = model.getLeftTower().getDisksOnTower();

        for (Disk disk : diskList) {
            addEventHandlersForVBox(disk);
        }
    }

    /**
     *  Add event handlers for the disk that is given as an argument. They are used in order
     *  to drag the disks around the scene, by way of translating the position of the disk as
     *  it is being dragged.
     *  List of handlers: onMouseEntered, onMousePressed, onDragDetected, onMouseDragged, onMouseReleased
     *
     * @param disk the disk for which the handlers will be added
     */
    private void addEventHandlersForVBox(Disk disk) {
        disk.setOnMouseEntered(event -> {
            if (disk.isDraggable()) {
                disk.setCursor(Cursor.HAND);
            } else {
                disk.setCursor(Cursor.DEFAULT);
            }
        });

        disk.setOnMousePressed(event -> {
            if (disk.isDraggable()) {
                // Get the coordinates of the cursor
                Point2D point = new Point2D(event.getSceneX(), event.getSceneY());

                // Get the scene bounds of the 3 VBoxes
                leftVBoxBounds = leftVBox.localToScene(leftVBox.getBoundsInLocal());
                middleVBoxBounds = middleVBox.localToScene(middleVBox.getBoundsInLocal());
                rightVBoxBounds = rightVBox.localToScene(rightVBox.getBoundsInLocal());

                // Set the tower from which the current disk is being dragged,
                // by checking if the coordinates of the cursor are inside of
                // one of the three towers
                if (middleVBoxBounds.contains(point)) {
                    model.getMiddleTower().setSource(true);
                } else if (rightVBoxBounds.contains(point)) {
                    model.getRightTower().setSource(true);
                } else if (leftVBoxBounds.contains(point)) {
                    model.getLeftTower().setSource(true);
                }

                model.setCurrentDisk(disk);

                // Set the initial position of the disk
                disk.setOrgSceneX(event.getSceneX());
                disk.setOrgSceneY(event.getSceneY());
                disk.setOrgTranslateX(disk.getTranslateX());
                disk.setOrgTranslateY(disk.getTranslateY());

                // Make the VBoxes solid, so that they receive MouseDragEvents while
                // the disks are being dragged over them
                disk.setMouseTransparent(true);
                leftVBox.setMouseTransparent(false);
                middleVBox.setMouseTransparent(false);
                rightVBox.setMouseTransparent(false);

                disk.setCursor(Cursor.CLOSED_HAND);
            }

            event.consume();
        });

        disk.setOnDragDetected(event -> {
            if (disk.isDraggable()) {
                disk.startFullDrag();
            }
        });

        disk.setOnMouseDragged(event -> {
            if (disk.isDraggable()) {
                // Calculate the offsets needed in order to translate the disks
                // while being dragged
                double offsetX = event.getSceneX() - disk.getOrgSceneX();
                double offsetY = event.getSceneY() - disk.getOrgSceneY();
                double newTranslateX = disk.getOrgTranslateX() + offsetX;
                double newTranslateY = disk.getOrgTranslateY() + offsetY;

                disk.setTranslateX(newTranslateX);
                disk.setTranslateY(newTranslateY);
            }
        });

        disk.setOnMouseReleased(event -> {
            if (disk.isDraggable()) {
                disk.setMouseTransparent(false);
                leftVBox.setMouseTransparent(false);

                disk.setCursor(Cursor.DEFAULT);
            }
        });
    }

    /**
     * Add event handlers for the vBox that is given as an argument. Only the event handler for the
     * mouseDragReleased is being treated in this function. This event handler contains the logic of
     * what happens when the disk is being dropped to the destination tower. As the disk is being dragged,
     * its image is being translated along, but when it is dropped inside one of the VBoxes corresponding to
     * the 3 towers, its position is instantly being changed from the source to the destination by hardcoding
     * the coordinates of where it should be placed.
     *
     * @param vBox the VBox for which the handlers will be added
     */
    public void addEventHandlersForVBox(VBox vBox) {
        vBox.setOnMouseDragReleased(event -> {
            double translateDiskOnX = 0;    // The amount by which the disk will be translated on the X axis
            double translateDiskOnY = 0;    // The amount by which the disk will be translated on the Y axis
            double currentDiskWidth = model.getCurrentDisk().getWidth();
            ObservableList<Disk> disksOnLeftTower = model.getLeftTower().getDisksOnTower();
            ObservableList<Disk> disksOnMiddleTower = model.getMiddleTower().getDisksOnTower();
            ObservableList<Disk> disksOnRightTower = model.getRightTower().getDisksOnTower();
            Tower leftTower = model.getLeftTower();
            Tower middleTower = model.getMiddleTower();
            Tower rightTower = model.getRightTower();

            VBox sourceVBox = (VBox) event.getSource();

            // Handle the case when the current disk is dragged from the left tower
            if (model.getLeftTower().isSource()) {
                if (sourceVBox.equals(middleVBox)) {
                    // If trying to drop from the left tower onto the middle tower and
                    // if the top disk on the middle tower has a greater size than
                    // the one being dropped
                    if (isDroppingAllowed(model.getMiddleTower().getDisksOnTower())) {
                        // Set the translate on the X and Y axis
                        translateDiskOnX = (((middleVBox.getWidth() - currentDiskWidth) / 2) * 2) + currentDiskWidth;
                        translateDiskOnY = model.getCurrentDisk().getDiskOffset() - middleTower.getCurrentOffset() - 20;

                        // If there are disks on the middle tower, make them undraggable
                        if (!disksOnMiddleTower.isEmpty()) {
                            for (Disk disk : disksOnMiddleTower) {
                                disk.setDraggable(false);
                            }
                        }

                        // Add the current disk to the middle tower and remove it from the left tower
                        disksOnMiddleTower.add(0, model.getCurrentDisk());
                        disksOnLeftTower.remove(model.getCurrentDisk());

                        // If there is a top disk on the left tower, make it draggable
                        if (!disksOnLeftTower.isEmpty()) {
                            disksOnLeftTower.get(0).setDraggable(true);
                        }

                        // Set the offsets of the 2 towers that are involved
                        middleTower.setCurrentOffset(disksOnMiddleTower.size() * 20);
                        leftTower.setCurrentOffset(disksOnLeftTower.size() * 20);
                    } else {
                        // Reset the position back to the left tower
                        translateDiskOnX = 0;
                        translateDiskOnY = 0;
                    }
                } else if (sourceVBox.equals(rightVBox)) {
                    // If trying to drop from the left tower onto the right tower and
                    // if the top disk on the right tower has a greater size than
                    // the one being dropped
                    if (isDroppingAllowed(model.getRightTower().getDisksOnTower())) {
                        translateDiskOnX = 2 * ((((middleVBox.getWidth() - currentDiskWidth) / 2) * 2) + currentDiskWidth);
                        translateDiskOnY = model.getCurrentDisk().getDiskOffset() - rightTower.getCurrentOffset() - 20;

                        // If there are disks on the right tower, make them undraggable
                        if (!disksOnRightTower.isEmpty()) {
                            for (Disk disk : disksOnRightTower) {
                                disk.setDraggable(false);
                            }
                        }

                        // Add the current disk to the right tower and remove it from the left tower
                        disksOnRightTower.add(0, model.getCurrentDisk());
                        disksOnLeftTower.remove(model.getCurrentDisk());

                        // If there is a top disk on the left tower, make it draggable
                        if (!disksOnLeftTower.isEmpty()) {
                            disksOnLeftTower.get(0).setDraggable(true);
                        }

                        // Set the offsets of the 2 towers that are involved
                        rightTower.setCurrentOffset(disksOnRightTower.size() * 20);
                        leftTower.setCurrentOffset(disksOnLeftTower.size() * 20);
                    } else {
                        // Reset the position back to the left tower
                        translateDiskOnX = 0;
                        translateDiskOnY = 0;
                    }
                } else {
                    // Reset the position back to the left tower
                    translateDiskOnX = 0;
                    translateDiskOnY = 0;
                }
            }

            // Handle the case when the current disk is dragged from the middle tower
            if (model.getMiddleTower().isSource()) {
                if (sourceVBox.equals(rightVBox)) {
                    // If trying to drop from the middle tower onto the right tower and
                    // if the top disk on the right tower has a greater size than
                    // the one being dropped
                    if (isDroppingAllowed(disksOnRightTower)) {
                        translateDiskOnX = 2 * ((((middleVBox.getWidth() - currentDiskWidth) / 2) * 2) + currentDiskWidth);
                        translateDiskOnY = model.getCurrentDisk().getDiskOffset() - rightTower.getCurrentOffset() - 20;

                        // If there are disks on the right tower, make them undraggable
                        if (!disksOnRightTower.isEmpty()) {
                            for (Disk disk : disksOnRightTower) {
                                disk.setDraggable(false);
                            }
                        }

                        // Add the current disk to the right tower and remove it from the middle tower
                        disksOnRightTower.add(0, model.getCurrentDisk());
                        disksOnMiddleTower.remove(model.getCurrentDisk());

                        // If there is a top disk on the middle tower, make it draggable
                        if (!disksOnMiddleTower.isEmpty()) {
                            disksOnMiddleTower.get(0).setDraggable(true);
                        }

                        // Set the offsets of the 2 towers that are involved
                        rightTower.setCurrentOffset(disksOnRightTower.size() * 20);
                        middleTower.setCurrentOffset(disksOnMiddleTower.size() * 20);
                    } else {
                        // Reset the position back to the middle tower
                        translateDiskOnX = (((middleVBox.getWidth() - currentDiskWidth) / 2) * 2) + currentDiskWidth;
                        translateDiskOnY = model.getCurrentDisk().getDiskOffset() - middleTower.getCurrentOffset();
                    }
                } else if ((event.getSceneX() > middleVBoxBounds.getMinX() && event.getSceneX() < middleVBoxBounds.getMaxX()) ||
                        event.getSceneY() < middleVBoxBounds.getMinY()) {
                    // If trying to drop from the middle tower to anywhere outside of the left or right towers
                    translateDiskOnX = (((middleVBox.getWidth() - currentDiskWidth) / 2) * 2) + currentDiskWidth;
                    translateDiskOnY = model.getCurrentDisk().getDiskOffset() - middleTower.getCurrentOffset();
                } else {
                    // If trying to drop from the middle tower onto the left tower and
                    // if the top disk on the left tower has a greater size than
                    // the one being dropped
                    if (isDroppingAllowed(disksOnLeftTower)) {

                        translateDiskOnX = 0;
                        translateDiskOnY = model.getCurrentDisk().getDiskOffset() - leftTower.getCurrentOffset() - 20;

                        // If there are disks on the left tower, make them undraggable
                        if (!disksOnLeftTower.isEmpty()) {
                            for (Disk disk : disksOnLeftTower) {
                                disk.setDraggable(false);
                            }
                        }

                        // Add the current disk to the left tower and remove it from the middle tower
                        disksOnLeftTower.add(0, model.getCurrentDisk());
                        disksOnMiddleTower.remove(model.getCurrentDisk());

                        // If there is a top disk on the middle tower, make it draggable
                        if (!disksOnMiddleTower.isEmpty()) {
                            disksOnMiddleTower.get(0).setDraggable(true);
                        }

                        // Set the offsets of the 2 towers that are involved
                        leftTower.setCurrentOffset(disksOnLeftTower.size() * 20);
                        middleTower.setCurrentOffset(disksOnMiddleTower.size() * 20);
                    } else {
                        // Reset the position back to the middle tower
                        translateDiskOnX = (((middleVBox.getWidth() - currentDiskWidth) / 2) * 2) + currentDiskWidth;
                        translateDiskOnY = model.getCurrentDisk().getDiskOffset() - middleTower.getCurrentOffset();
                    }
                }
            }

            // Handle the case when the current disk is dragged from the right tower
            if (model.getRightTower().isSource()) {
                if (sourceVBox.equals(middleVBox)) {
                    // If trying to drop from the right tower onto the middle tower and
                    // if the top disk on the middle tower has a greater size than
                    // the one being dropped
                    if (isDroppingAllowed(disksOnMiddleTower)) {
                        translateDiskOnX = (((middleVBox.getWidth() - currentDiskWidth) / 2) * 2) + currentDiskWidth;
                        translateDiskOnY = model.getCurrentDisk().getDiskOffset() - middleTower.getCurrentOffset() - 20;

                        // If there are disks on the middle tower, make them undraggable
                        if (!disksOnMiddleTower.isEmpty()) {
                            for (Disk disk : disksOnMiddleTower) {
                                disk.setDraggable(false);
                            }
                        }

                        // Add the current disk to the middle tower and remove it from the right tower
                        disksOnMiddleTower.add(0, model.getCurrentDisk());
                        disksOnRightTower.remove(model.getCurrentDisk());

                        // If there is a top disk on the right tower, make it draggable
                        if (!disksOnRightTower.isEmpty()) {
                            disksOnRightTower.get(0).setDraggable(true);
                        }

                        // Set the offsets of the 2 towers that are involved
                        middleTower.setCurrentOffset(disksOnMiddleTower.size() * 20);
                        rightTower.setCurrentOffset(disksOnRightTower.size() * 20);
                    } else {
                        // Reset the position back to the right tower
                        translateDiskOnX = 2 * ((((middleVBox.getWidth() - currentDiskWidth) / 2) * 2) + currentDiskWidth);
                        translateDiskOnY = model.getCurrentDisk().getDiskOffset() - rightTower.getCurrentOffset();
                    }
                } else if ((event.getSceneX() > rightVBoxBounds.getMinX() && event.getSceneX() < rightVBoxBounds.getMaxX()) ||
                        event.getSceneY() < rightVBoxBounds.getMinY()) {
                    // If trying to drop from the right tower to anywhere outside of the left or middle towers

                    translateDiskOnX = 2 * ((((middleVBox.getWidth() - currentDiskWidth) / 2) * 2) + currentDiskWidth);
                    translateDiskOnY = model.getCurrentDisk().getDiskOffset() - rightTower.getCurrentOffset();
                } else {
                    // If trying to drop from the right tower onto the left tower and
                    // if the top disk on the left tower has a greater size than
                    // the one being dropped
                    if (isDroppingAllowed(disksOnLeftTower)) {
                        translateDiskOnX = 0;
                        translateDiskOnY = model.getCurrentDisk().getDiskOffset() - leftTower.getCurrentOffset() - 20;

                        // If there are disks on the left tower, make them undraggable
                        if (!disksOnLeftTower.isEmpty()) {
                            for (Disk disk : disksOnLeftTower) {
                                disk.setDraggable(false);
                            }
                        }

                        // Add the current disk to the left tower and remove it from the right tower
                        disksOnLeftTower.add(0, model.getCurrentDisk());
                        disksOnRightTower.remove(model.getCurrentDisk());

                        // If there is a top disk on the right tower, make it draggable
                        if (!disksOnRightTower.isEmpty()) {
                            disksOnRightTower.get(0).setDraggable(true);
                        }

                        // Set the offsets of the 2 towers that are involved
                        leftTower.setCurrentOffset(disksOnLeftTower.size() * 20);
                        rightTower.setCurrentOffset(disksOnRightTower.size() * 20);
                    } else {
                        // Reset the position back to the right tower
                        translateDiskOnX = 2 * ((((middleVBox.getWidth() - currentDiskWidth) / 2) * 2) + currentDiskWidth);
                        translateDiskOnY = model.getCurrentDisk().getDiskOffset() - rightTower.getCurrentOffset();
                    }
                }
            }

            // Translate the current disk according to the calculated offsets
            model.getCurrentDisk().setTranslateX(translateDiskOnX);
            model.getCurrentDisk().setTranslateY(translateDiskOnY);

            // Reset the source flags
            model.getLeftTower().setSource(false);
            model.getMiddleTower().setSource(false);
            model.getRightTower().setSource(false);

            // Set the vBoxes to transparent, so that the disks can be clicked again
            model.getCurrentDisk().setMouseTransparent(false);
            middleVBox.setMouseTransparent(true);
            leftVBox.setMouseTransparent(true);
            rightVBox.setMouseTransparent(true);
        });
    }

    /**
     * This function checks if the user is allowed to drop the current disk on top
     * of the disks that are already present on the target tower. This is achieved by comparing the
     * sizes of the disks.
     *
     * @param disksOnTower a list of the disks that are currently present on the target tower
     * @return true, if the size of the disk to be dropped is smaller than either one of the
     *         disks on the tower, false otherwise
     */
    private boolean isDroppingAllowed(ObservableList<Disk> disksOnTower) {
        // If there is no disk on the tower, then the current disk can be added
        if (disksOnTower == null || disksOnTower.isEmpty()) {
            return true;
        }

        // Check if any of the disks on the tower has a lesser size than the disk being dropped
        for (Disk disk : disksOnTower) {
            if (model.getCurrentDisk().getSize() > disk.getSize()) {
                return false;
            }
        }

        return true;
    }
}