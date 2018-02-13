package com.github.keithwoelke.actions.core;

/**
 * This interface denotes that an action has pre-state steps to execute.
 *
 * @author wkwoelke
 */
@SuppressWarnings("EmptyMethod")
public interface BeforeState {

    /**
     * This method will be called immediately prior to getting the baseline state.
     */
    void beforeState();
}
