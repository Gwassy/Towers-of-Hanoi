package view.automatic;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import model.DataModel;
import model.Disk;
import model.Tower;

public class AutomaticModeController {
    private DataModel model;

    @FXML
    Button startButton;

    @FXML
    VBox leftVBox;

    @FXML
    VBox middleVBox;

    @FXML
    VBox rightVBox;

    /**
     * This function is used to initialize the DataModel.
     * It adds the disks on the left tower as children of the leftVBox and adds an event handler
     * for when the user presses the start button. Pressing the start button calls the function
     * that iteratively solves the Towers of Hanoi puzzle.
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

        Tower leftTower = model.getLeftTower();
        Tower middleTower = model.getMiddleTower();
        Tower rightTower = model.getRightTower();

        // Inner class that provides a task which can be used by the background thread
        // that updates the UI
        class TowersOfHanoiTask extends Task<Void> {
            @Override
            protected Void call() throws Exception {
                TowerOfHanoi(model.getNrOfDisks(), leftTower, middleTower, rightTower);
                return null;
            }
        }

        startButton.setOnMousePressed(event -> {
            TowersOfHanoiTask task = new TowersOfHanoiTask();
            Thread backgroundThread = new Thread(task);

            backgroundThread.setDaemon(true);
            backgroundThread.start();
        });
    }

    /**
     * This function solves the Towers of Hanoi puzzle using an iterative approach
     *
     * Pseudocode:
     *
     * 1. Calculate total no. of moves as pow(2, numDisks) - 1. numDisks is no. of disks.
     * 2. If numDisks is even then interchange the destination pole with the auxiliary pole.
     * 3. for i = 1 to number of moves calculate in step 1:
     *    a. if i%3 == 1:
     *       legal movement of top disk b/w source pole and destination pole.
     *    b. if i%3 == 2:
     *       legal movement of top disk b/w source pole and auxiliary pole.
     *    c. if i%3 == 0:
     *       legal movement of top disk b/w auxiliary pole and destination pole.
     *
     * Source: https://cs.stackexchange.com/questions/96624/how-to-solve-tower-of-hanoi-iteratively
     *
     * @param numDisks the total number of disks
     * @param source the tower on which the disks are initially placed
     * @param destination the tower on which the disks will be placed at the end of the game
     * @param auxiliary this tower is used as an intermediate that aids in moving the disks to the destination
     */
    private void TowerOfHanoi(int numDisks, Tower source, Tower destination, Tower auxiliary) throws InterruptedException {
        ObservableList<Disk> sourceDisks = source.getDisksOnTower();
        ObservableList<Disk> destinationDisks = destination.getDisksOnTower();
        ObservableList<Disk> auxiliaryDisks = auxiliary.getDisksOnTower();

        Tower aux;          // Used to swap the destination and the auxiliary if there is an even number of disks

        int nrOfMoves = (int) Math.pow(2, numDisks) - 1;

        // If there is an even number of disks, swap the destination and the auxiliary towers
        if (nrOfMoves % 2 == 0) {
            aux = destination;
            destination = auxiliary;
            auxiliary = aux;
        }

        for (int i = 1; i <= nrOfMoves; i++) {
            if (i % 3 == 1) {
                if (!sourceDisks.isEmpty() && !destinationDisks.isEmpty()) {
                    // If neither of the towers is empty, perform the legal movement
                    if (sourceDisks.get(0).getSize() < destinationDisks.get(0).getSize()) {
                        move(source, destination);
                    } else {
                        move(destination, source);
                    }
                } else {
                    // If either of the towers is empty, move the disk to that tower
                    if (sourceDisks.isEmpty()) {
                        move(destination, source);
                    } else {
                        move(source, destination);
                    }
                }
                // Wait for one second so that the disk movement can be seen
                Thread.sleep(1000);
            }
            if (i % 3 == 2) {
                if (!sourceDisks.isEmpty() && !auxiliaryDisks.isEmpty()) {
                    // If neither of the towers is empty, perform the legal movement
                    if (sourceDisks.get(0).getSize() < auxiliaryDisks.get(0).getSize()) {
                        move(source, auxiliary);
                    } else {
                        move(auxiliary, source);
                    }
                } else {
                    // If either of the towers is empty, move the disk to that tower
                    if (sourceDisks.isEmpty()) {
                        move(auxiliary, source);
                    } else {
                        move(source, auxiliary);
                    }
                }
                // Wait for one second so that the disk movement can be seen
                Thread.sleep(1000);
            }
            if (i % 3 == 0) {
                if (!auxiliaryDisks.isEmpty() && !destinationDisks.isEmpty()) {
                    // If neither of the towers is empty, perform the legal movement
                    if (auxiliaryDisks.get(0).getSize() < destinationDisks.get(0).getSize()) {
                        move(auxiliary, destination);
                    } else {
                        move(destination, auxiliary);
                    }
                } else {
                    // If either of the towers is empty, move the disk to that tower
                    if (auxiliaryDisks.isEmpty()) {
                        move(destination, auxiliary);
                    } else {
                        move(auxiliary, destination);
                    }
                }
                // Wait for one second so that the disk movement can be seen
                Thread.sleep(1000);
            }
        }
    }

    /**
     * This function moves the top disk on the source tower to the destination tower by translating
     * the disk to hardcoded coordinates on the destination tower, taking into consideration the number
     * of disks that are already present on the destination, using the 'currentOffset' member variable of
     * the Tower class
     *
     * @param source the Tower from which the top disk is being moved
     * @param destination the Tower where the top disk will be placed
     */
    private void move(Tower source, Tower destination) {
        double translateDiskOnX = 0;        // The amount by which the disk will be translated on the X axis
        double translateDiskOnY = 0;        // The amount by which the disk will be translated on the Y axis
        Disk topDisk = source.getDisksOnTower().get(0);     // The disk on top of the source tower
        double topDiskWidth = topDisk.getWidth();
        Tower leftTower = model.getLeftTower();
        Tower middleTower = model.getMiddleTower();
        Tower rightTower = model.getRightTower();
        ObservableList<Disk> disksOnLeftTower = leftTower.getDisksOnTower();
        ObservableList<Disk> disksOnMiddleTower = middleTower.getDisksOnTower();
        ObservableList<Disk> disksOnRightTower = rightTower.getDisksOnTower();

        if (source == leftTower) {
            if (destination == middleTower) {
                // Set the translate on the X and Y axis
                translateDiskOnX = (((middleVBox.getWidth() - topDiskWidth) / 2) * 2) + topDiskWidth;
                translateDiskOnY = topDisk.getDiskOffset() - middleTower.getCurrentOffset() - 20;

                // Add the current disk to the middle tower and remove it from the left tower
                disksOnMiddleTower.add(0, topDisk);
                disksOnLeftTower.remove(topDisk);

                // Set the offsets of the 2 towers that are involved
                middleTower.setCurrentOffset(disksOnMiddleTower.size() * 20);
                leftTower.setCurrentOffset(disksOnLeftTower.size() * 20);
            } else if (destination == rightTower) {
                // Set the translate on the X and Y axis
                translateDiskOnX = 2 * ((((middleVBox.getWidth() - topDiskWidth) / 2) * 2) + topDiskWidth);
                translateDiskOnY = topDisk.getDiskOffset() - rightTower.getCurrentOffset() - 20;

                // Add the current disk to the right tower and remove it from the left tower
                disksOnRightTower.add(0, topDisk);
                disksOnLeftTower.remove(topDisk);

                // Set the offsets of the 2 towers that are involved
                rightTower.setCurrentOffset(disksOnRightTower.size() * 20);
                leftTower.setCurrentOffset(disksOnLeftTower.size() * 20);
            }
        } else if (source == middleTower) {
            if (destination == rightTower) {
                // Set the translate on the X and Y axis
                translateDiskOnX = 2 * ((((middleVBox.getWidth() - topDiskWidth) / 2) * 2) + topDiskWidth);
                translateDiskOnY = topDisk.getDiskOffset() - rightTower.getCurrentOffset() - 20;

                // Add the current disk to the right tower and remove it from the middle tower
                disksOnRightTower.add(0, topDisk);
                disksOnMiddleTower.remove(topDisk);

                // Set the offsets of the 2 towers that are involved
                rightTower.setCurrentOffset(disksOnRightTower.size() * 20);
                middleTower.setCurrentOffset(disksOnMiddleTower.size() * 20);
            } else if (destination == leftTower) {
                // Set the translate on the X and Y axis
                translateDiskOnX = 0;
                translateDiskOnY = topDisk.getDiskOffset() - leftTower.getCurrentOffset() - 20;

                // Add the current disk to the left tower and remove it from the middle tower
                disksOnLeftTower.add(0, topDisk);
                disksOnMiddleTower.remove(topDisk);

                // Set the offsets of the 2 towers that are involved
                leftTower.setCurrentOffset(disksOnLeftTower.size() * 20);
                middleTower.setCurrentOffset(disksOnMiddleTower.size() * 20);
            }
        } else if (source == rightTower) {
            if (destination == middleTower) {
                // Set the translate on the X and Y axis
                translateDiskOnX = (((middleVBox.getWidth() - topDiskWidth) / 2) * 2) + topDiskWidth;
                translateDiskOnY = topDisk.getDiskOffset() - middleTower.getCurrentOffset() - 20;

                // Add the current disk to the middle tower and remove it from the right tower
                disksOnMiddleTower.add(0, topDisk);
                disksOnRightTower.remove(topDisk);

                // Set the offsets of the 2 towers that are involved
                middleTower.setCurrentOffset(disksOnMiddleTower.size() * 20);
                rightTower.setCurrentOffset(disksOnRightTower.size() * 20);
            } else if (destination == leftTower) {
                // Set the translate on the X and Y axis
                translateDiskOnX = 0;
                translateDiskOnY = topDisk.getDiskOffset() - leftTower.getCurrentOffset() - 20;

                // Add the current disk to the left tower and remove it from the right tower
                disksOnLeftTower.add(0, topDisk);
                disksOnRightTower.remove(topDisk);

                // Set the offsets of the 2 towers that are involved
                leftTower.setCurrentOffset(disksOnLeftTower.size() * 20);
                rightTower.setCurrentOffset(disksOnRightTower.size() * 20);
            }
        }

        topDisk.setTranslateX(translateDiskOnX);
        topDisk.setTranslateY(translateDiskOnY);
    }
}