package com.github.keithwoelke.actions.core;

/**
 * This interface denotes that an action has pre-execution steps to execute.
 *
 * @author wkwoelke
 */
@SuppressWarnings("EmptyMethod")
public interface BeforeAction {

    /**
     * This method will be called immediately prior to executing an action.
     */
    void beforeAction();
}
