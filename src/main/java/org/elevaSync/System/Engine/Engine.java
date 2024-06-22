package org.elevaSync.System.Engine;

import org.elevaSync.System.Elevator.Elevator;
import org.elevaSync.System.utilities.Call;
import org.elevaSync.System.utilities.ChangeObserver;

import java.util.ArrayList;
import java.util.List;

public class Engine implements Runnable {
    private final List<Elevator> elevators = new ArrayList<>();
    private final List<ChangeObserver> observers = new ArrayList<>();
    private final Scheduler scheduler;
    private boolean running = true;

    public Engine(int numElevators) {
        for (int i = 0; i < numElevators; i++) {
            elevators.add(new Elevator());
        }
        scheduler = new Scheduler(elevators);
    }

    public void addObserver(ChangeObserver observer) {
        observers.add(observer);
    }

    public void addCall(Call call) {
        scheduler.addCall(call);
    }

    public void internalCall(Integer elevator, Integer floor) {
        elevators.get(elevator).addStop(floor);
    }

    public void run() {
        while (true) {
            updateObservers();
            scheduler.schedule();
            moveElevators();
            try {
                if (!running) {break;}
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {}
        }
    }

    private void updateObservers() {
        observers.forEach(observer -> observer.refreshView(elevators));
    }

    public void breakSimulation(){
        running = false;
    }

    private void moveElevators() {
        elevators.forEach(Elevator::move);
    }

    public void toggle(Integer elevator) {
        elevators.get(elevator).toggle();
    }
}
