package org.elevaSync.System.utilities;


import org.elevaSync.System.Elevator.Elevator;

import java.util.ArrayList;
import java.util.Optional;

import static java.lang.Math.abs;

public class ElevatorList extends ArrayList<Elevator>{
    public Optional<Elevator> getElevator(Call call) {
        MoveDirection direction = call.direction();
        int floor = call.floor();
        Optional<Elevator> best = Optional.empty();

        for (Elevator elevator : this) {
            // if the elevator is not running, skip it
            if (elevator.getStatus() != ElevatorStatus.RUNNING) {continue;}
            // 1. if the elevator is going in the same direction as the call and the call is on the way
            if (elevator.getDirection() == direction) {
                if (isOnWay(elevator, direction, floor) && isCloser(elevator, best, floor)) {
                    best = Optional.of(elevator);
                }
            // 2. if the elevator is going to other call and then change direction
            } else if (elevator.getDirection() == direction.opposite()) {
                if (isCloser(elevator, best, floor) && elevator.willChangeDirection()) {
                    best = Optional.of(elevator);
                }
            // 3. if the elevator is idle and the elevator is closer than the best elevator
            } else if (elevator.isIdle() && isCloser(elevator, best, floor)) {
                best = Optional.of(elevator);
            }
        }
        return best;
    }

    // checks distance between elevator and floor
    private boolean isCloser(Elevator elevator, Optional<Elevator> best, int floor) {
        return best.isEmpty() || abs(elevator.getCurrentFloor() - floor) < abs(best.get().getCurrentFloor() - floor);
    }

    // checks if the elevator is on the way to the call
    private boolean isOnWay(Elevator elevator, MoveDirection direction, int floor) {
        return direction.getValue() * (floor - elevator.getCurrentFloor()) > 0 && (direction == elevator.getDirection() || elevator.isIdle());
    }
}
