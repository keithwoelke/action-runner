package com.github.keithwoelke.actions.core.filter;

/**
 * An enumeration denoting the number of times to apply a Filter.
 *
 * @author wkwoelke
 */
@SuppressWarnings("unused")
public enum PersistenceStrategy {
    RUN_ONCE(1),
    RUN_TWICE(2),
    RUN_THREE_TIMES(3),
    RUN_FOUR_TIMES(4),
    RUN_FIVE_TIMES(5),
    DISABLED(0),
    RUN_FOREVER(Integer.MAX_VALUE);

    private final int timesToExecute;

    PersistenceStrategy(int timesToExecute) {
        this.timesToExecute = timesToExecute;
    }

    public int timesToExecute() {
        return timesToExecute;
    }
}
