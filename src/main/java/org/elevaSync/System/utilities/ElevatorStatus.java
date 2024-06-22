package org.elevaSync.System.utilities;

public enum ElevatorStatus {
    RUNNING,
    OUT_OF_SERVICE;

    public ElevatorStatus opposite() {
        return switch (this) {
            case RUNNING -> OUT_OF_SERVICE;
            case OUT_OF_SERVICE -> RUNNING;
        };
    }
}
