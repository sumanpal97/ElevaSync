package org.elevaSync.System.utilities;

public enum MoveDirection {
    UP,
    STOP,
    DOWN,
    IDLE;

    public int getValue() {
        return switch (this) {
            case UP -> 1;
            case DOWN -> -1;
            default -> 0;
        };
    }

    public MoveDirection opposite() {
        return switch (this) {
            case UP -> DOWN;
            case DOWN -> UP;
            default -> IDLE;
        };
    }
}
