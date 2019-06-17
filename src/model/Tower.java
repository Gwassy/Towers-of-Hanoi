package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Tower {
    private ObservableList<Disk> disksOnTower;      // A list of the disks that are present on one of the 3 towers (left, middle, right)
    private int currentOffset;                      // The current offset on the Y axis for the disks that are currently on the tower
    private boolean isSource;                       // This flag specifies whether the current disk is being dragged from this tower

    public Tower() {
        this.disksOnTower = FXCollections.observableArrayList();
    }

    public int getCurrentOffset() {
        return currentOffset;
    }

    public void setCurrentOffset(int currentOffset) {
        this.currentOffset = currentOffset;
    }

    public ObservableList<Disk> getDisksOnTower() {
        return disksOnTower;
    }

    public void setDisksOnTower(ObservableList<Disk> disksOnTower) {
        if (disksOnTower != null) {
            this.disksOnTower = disksOnTower;
        }
    }

    public boolean isSource() {
        return isSource;
    }

    public void setSource(boolean source) {
        isSource = source;
    }
}
