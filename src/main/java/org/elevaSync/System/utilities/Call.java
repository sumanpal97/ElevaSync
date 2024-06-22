package org.elevaSync.System.utilities;

import java.util.Comparator;

public record Call(int floor, MoveDirection direction) implements Comparable<Call> {
    @Override
    public int compareTo(Call o) {
        return Comparator.comparing(Call::direction)
                .thenComparing(Call::floor)
                .compare(this, o);
    }
}
