package org.elevaSync.gui.utils;

public class FloorPlan {
    private final int elevators;
    private final int floors;

    public FloorPlan(int elevators, int floors) {
        this.elevators = elevators;
        this.floors = floors;
    }

    public int getElevators() {
        return elevators;
    }

    public int getFloors() {
        return floors;
    }
}
