package com.github.keithwoelke.actions.core;

/**
 * This interface guarantees that an action can be validated by provided information about its state and expected state. It is assumed comparing state
 * objects is a reasonable thing to do.
 *
 * @param <ResultType> the result the Action returns
 * @param <StateType>  the type of State corresponding to the Action
 * @author wkwoelke
 */
@SuppressWarnings({"EmptyMethod", "SameReturnValue", "unused"})
public interface ChangesState<ResultType, StateType> {

    StateType getCurrentState(ResultType result);

    StateType getExpectedState(ResultType result, StateType baselineState, StateType finalState);

    void validateState(StateType expectedState, StateType finalState);
}
