package com.github.keithwoelke.actions.core.filter;

import lombok.Data;

/**
 * The Persistence class is responsible for storing/tracking the number of remaining times a Filter should be applied.
 *
 * @author wkwoelke
 */
@SuppressWarnings("unused")
@Data
public class Persistence {

    private int timesToExecute;

    public Persistence(PersistenceStrategy persistenceStrategy) {
        this(persistenceStrategy.timesToExecute());
    }

    public Persistence(int timesToExecute) {
        this.timesToExecute = timesToExecute;
    }

    /**
     * This method will determine if a Filter can be applied. If an execution is permissible, the timesToExecute will be decremented and a value of
     * true will be returned. If a value of false is returned, it means the filter has exhausted its allotted runs or is in a disabled state.
     *
     * @return true, if the timesToExecute > 0. false, if timesToExecute is PersistenceStrategy.DISABLED or a value of 0
     */
    public boolean consumeRunToken() {
        if (timesToExecute <= 0) {
            return false;
        } else {
            timesToExecute--;
        }

        return true;
    }
}
