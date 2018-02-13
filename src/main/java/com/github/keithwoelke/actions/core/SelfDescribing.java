package com.github.keithwoelke.actions.core;

/**
 * An interface which guarantees an action will describe itself.
 *
 * @author wkwoelke
 */
@SuppressWarnings("SameReturnValue")
public interface SelfDescribing {

    /**
     * Describes the action that is being executed.
     *
     * @return a description of the action
     */
    String describeAction();
}
