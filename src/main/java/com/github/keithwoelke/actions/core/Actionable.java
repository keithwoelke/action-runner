package com.github.keithwoelke.actions.core;

import java.io.Serializable;

/**
 * This interface guarantees that an action has an action which can be performed. Most actions should return some sort result as makes sense to that
 * action. For HTTP requests, it typically makes sense to return the entire response object, but any type of object may be returned.
 *
 * @param <ResultType> the type of result an action will return
 * @author wkwoelke
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public interface Actionable<ResultType> extends Action, Serializable {

    /**
     * Do an action.
     *
     * @return the result of the action
     */
    ResultType doAction();
}
