package com.github.keithwoelke.actions.core;

/**
 * This interface denotes that an action has post-execution steps to execute.
 *
 * @param <ResultType> the type of result returned by the Action
 * @author wkwoelke
 */
@SuppressWarnings({"EmptyMethod", "unused"})
public interface AfterAction<ResultType> {

    /**
     * This method will be called immediately following the doAction stage.
     *
     * @param result     the result of the action execution
     */
    void afterAction(ResultType result);
}
