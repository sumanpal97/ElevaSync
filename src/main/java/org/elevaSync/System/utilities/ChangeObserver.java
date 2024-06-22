package org.elevaSync.System.utilities;


import org.elevaSync.System.Elevator.Elevator;

import java.util.List;

public interface ChangeObserver {
    void refreshView(List<Elevator> elevators);
}
