package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import java.util.Comparator;

public class DataModel {
    private Disk currentDisk;                       // The disk that is currently being dragged
    private final int nrOfDisks;                    // The total number of disks
    private Tower leftTower;                        // The 3 towers
    private Tower middleTower;
    private Tower rightTower;


    public DataModel(int nrOfDisks) {
        this.nrOfDisks = nrOfDisks;

        leftTower = new Tower(nrOfDisks * 20);              // The argument of this constructor is the initial offset
        this.leftTower.setDisksOnTower(createDiskList());

        middleTower = new Tower();
        rightTower = new Tower();
    }

    public Tower getLeftTower() {
        return leftTower;
    }

    public Tower getMiddleTower() {
        return middleTower;
    }

    public Tower getRightTower() {
        return rightTower;
    }

    public Disk getCurrentDisk() {
        return currentDisk;
    }

    public void setCurrentDisk(Disk currentDisk) {
        this.currentDisk = currentDisk;
    }

    private ObservableList<Disk> createDiskList() {
        double width = 180.0;                         // The bottom disk starts with a width of 180
        final double DISK_HEIGHT = 20.0;              // The height of all the disks is 20
        final double ARC_WIDTH = 10.0;                // These 2 specify the roundness of the corners
        final double ARC_HEIGHT = 10.0;
        int diskSize = this.nrOfDisks;                // Used to assign sizes to the disks that will be created
        int offset = 20;                              // Used to assign the offsets of the disks; will be used for translating the
                                                      // disks on the Y axis

        ObservableList<Disk> diskList = FXCollections.observableArrayList();

        for (int i = 0; i < this.nrOfDisks; i++) {
            Disk diskToBeAdded = new Disk();

            // Set the size and color of the disk to be added
            diskToBeAdded.setSize(diskSize);
            diskToBeAdded.setWidth(width);
            diskToBeAdded.setDiskOffset(offset);
            diskToBeAdded.setHeight(DISK_HEIGHT);
            diskToBeAdded.setArcWidth(ARC_WIDTH);
            diskToBeAdded.setArcHeight(ARC_HEIGHT);
            diskToBeAdded.setFill(Color.GRAY);

            // Add the disk to the top of the stack
            diskList.add(diskToBeAdded);

            // The difference in width between the disks is 20
            width = width - 20;

            // The size of the next disk is 1 less than the previous one
            diskSize = diskSize - 1;

            // The offsets increase by 20, starting from the bottom
            offset = offset + 20;
        }

        // Only the top disk should be draggable when the game starts
        diskList.get(this.nrOfDisks - 1).setDraggable(true);

        // Sort the list by width, so that the disk with the greatest width
        // gets to be at the bottom of the rod
        diskList.sort(Comparator.comparingInt((Disk o) -> (int) o.getWidth()));

        return diskList;
    }
}