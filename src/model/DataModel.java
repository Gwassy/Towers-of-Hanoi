package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import java.util.Comparator;

public class DataModel {
    private final ObservableList<Disk> diskList;    // The list of all the disks that are present at startup
    private Disk currentDisk;                       // The disk that is currently being dragged

    public DataModel(int listSize) {
        this.diskList = createDiskList(listSize);
    }

    public ObservableList<Disk> getDiskList() {
        return diskList;
    }

    public Disk getCurrentDisk() {
        return currentDisk;
    }

    public void setCurrentDisk(Disk currentDisk) {
        this.currentDisk = currentDisk;
    }

    private ObservableList<Disk> createDiskList(int listSize) {
        double width = 180.0;               // The bottom disk starts with a height of 180
        final double DISK_HEIGHT = 20.0;    // The height of all the disks is 20
        final double ARC_WIDTH = 10.0;      // These 2 specify the roundness of the corners
        final double ARC_HEIGHT = 10.0;
        int diskSize = listSize;            // Used to assign sizes to the disks that will be created

        ObservableList<Disk> diskList = FXCollections.observableArrayList();

        for (int i = 0; i < listSize; i++) {
            Disk diskToBeAdded = new Disk();

            diskToBeAdded.setSize(diskSize);
            diskToBeAdded.setWidth(width);
            diskToBeAdded.setHeight(DISK_HEIGHT);
            diskToBeAdded.setArcWidth(ARC_WIDTH);
            diskToBeAdded.setArcHeight(ARC_HEIGHT);
            diskToBeAdded.setFill(Color.GRAY);

            diskList.add(diskToBeAdded);

            // The difference in width between the disks is 20
            width = width - 20;

            // The size of the next disk is 1 less than the previous one
            diskSize = diskSize - 1;
        }

        // Only the topmost disk should be draggable when the game starts
        diskList.get(listSize - 1).setDraggable(true);

        // Sort the list by width, so that the disk with the greatest width
        // gets to be at the bottom of the rod
        diskList.sort(Comparator.comparingInt((Disk o) -> (int) o.getWidth()));

        return diskList;
    }
}