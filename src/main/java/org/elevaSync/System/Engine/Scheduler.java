package org.elevaSync.System.Engine;

import org.elevaSync.System.Elevator.Elevator;
import org.elevaSync.System.utilities.Call;
import org.elevaSync.System.utilities.ElevatorList;

import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class Scheduler {
    private final ElevatorList elevators = new ElevatorList();
    private final SortedSet<Call> calls = new TreeSet<>();

    public Scheduler(List<Elevator> elements) {
        elevators.addAll(elements);
    }

    public void addCall(Call call) {
        calls.add(call);
    }

    public void schedule() {
        Iterator<Call> iterator = calls.iterator();
        while (iterator.hasNext()) {
            Call call = iterator.next();
            Elevator elevator = elevators.getElevator(call).orElse(null);
            if (elevator != null) {
                elevator.addStop(call.floor(), call.direction());
                iterator.remove();
            }
        }
    }
}
