package model;

import javafx.scene.shape.Rectangle;

public class Disk extends Rectangle {
    private double orgSceneX;            // These 4 variables are used for moving the disk
    private double orgSceneY;
    private double orgTranslateX;
    private double orgTranslateY;
    private int size;                    // The size will keep track of which disk is allowed to go on top of another
    private boolean draggable = false;   // This boolean is used to specify whether the user is allowed to drag a disk

    public double getOrgSceneX() {
        return orgSceneX;
    }

    public void setOrgSceneX(double orgSceneX) {
        this.orgSceneX = orgSceneX;
    }

    public double getOrgSceneY() {
        return orgSceneY;
    }

    public void setOrgSceneY(double orgSceneY) {
        this.orgSceneY = orgSceneY;
    }

    public double getOrgTranslateX() {
        return orgTranslateX;
    }

    public void setOrgTranslateX(double orgTranslateX) {
        this.orgTranslateX = orgTranslateX;
    }

    public double getOrgTranslateY() {
        return orgTranslateY;
    }

    public void setOrgTranslateY(double orgTranslateY) {
        this.orgTranslateY = orgTranslateY;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isDraggable() {
        return draggable;
    }

    public void setDraggable(boolean draggable) {
        this.draggable = draggable;
    }
}
