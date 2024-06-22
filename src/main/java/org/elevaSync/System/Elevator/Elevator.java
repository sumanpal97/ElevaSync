package org.elevaSync.System.Elevator;


import org.elevaSync.System.utilities.ElevatorStatus;
import org.elevaSync.System.utilities.MoveDirection;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Elevator {
    private Integer currentFloor = 1;
    private MoveDirection direction = MoveDirection.IDLE;
    private ElevatorStatus status = ElevatorStatus.RUNNING;
    private MoveDirection directionOfStops = MoveDirection.UP;
    private final Set<Integer> currentStops = new HashSet<>();
    private final Set<Integer> bufferStops = new HashSet<>();

    public Integer getCurrentFloor() {
        return currentFloor;
    }

    public MoveDirection getDirection() {
        return direction;
    }

    public ElevatorStatus getStatus() {
        return status;
    }

    public void addStop(Integer floor) {
        if (this.status != ElevatorStatus.RUNNING) {return;}
        (this.direction.getValue() * (floor - currentFloor) >= 0 ? currentStops : bufferStops).add(floor);
    }

    public void addStop(Integer floor, MoveDirection direction) {
        if (this.direction == MoveDirection.IDLE) {
            currentStops.add(floor);
            directionOfStops = direction;
        } else {
            if (willChangeDirection() && this.direction.getValue() * (floor - currentStops.iterator().next()) > 0) {
                bufferStops.add(currentStops.iterator().next());
                currentStops.clear();
                currentStops.add(floor);
            } else {
                (direction == this.direction ? currentStops : bufferStops).add(floor);
            }
        }
    }

    public void move() {
        switch (direction) {
            case UP, DOWN -> go();
            case STOP -> start();
            case IDLE -> check();
        }
    }

    private void go() {
        currentFloor += direction.getValue();
        if (currentStops.contains(currentFloor)) {
            stop();
        }
    }

    private void stop() {
        this.direction = MoveDirection.STOP;
        currentStops.remove(currentFloor);
        if (currentStops.isEmpty()) {
            currentStops.addAll(bufferStops);
            bufferStops.clear();
        }
    }

    private void start() {
        if (currentStops.isEmpty()) {
            direction = MoveDirection.IDLE;
        } else {
            if (currentFloor - currentStops.iterator().next() == 0) {
                stop();
            } else {
                direction = currentStops.iterator().next() > currentFloor ? MoveDirection.UP : MoveDirection.DOWN;
            }
        }
    }

    private void check() {
        if (!currentStops.isEmpty()) {
            if (Objects.equals(currentStops.iterator().next(), currentFloor)) {
                stop();
            } else {
                direction = currentStops.iterator().next() > currentFloor ? MoveDirection.UP : MoveDirection.DOWN;
            }
        } else if (!bufferStops.isEmpty()) {
            direction = bufferStops.iterator().next() > currentFloor ? MoveDirection.UP : MoveDirection.DOWN;
        }
    }

    public boolean isIdle() {
        return direction == MoveDirection.IDLE;
    }

    public void toggle() {
        status = status.opposite();
        direction = MoveDirection.IDLE;
        currentStops.clear();
        bufferStops.clear();
    }

    public boolean willChangeDirection() {
        return directionOfStops != direction && currentStops.size() == 1;
    }
}
