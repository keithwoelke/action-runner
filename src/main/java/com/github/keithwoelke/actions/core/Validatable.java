package com.github.keithwoelke.actions.core;

/**
 * This interface guarantees that an action can be validated by provided information about its state and a mechanism to validate the action.
 *
 * @param <ResultType>         the result the Action returns
 * @param <ExpectedResultType> the object which represents the expected result. It should not be assumed to be directly comparable. It may only
 *                             provide information on which assertions can be derived.
 * @param <StateType>          the State Type that corresponds to the Action
 */
@SuppressWarnings({"EmptyMethod", "SameReturnValue", "unused"})
public interface Validatable<ResultType, ExpectedResultType, StateType> {

    ExpectedResultType getExpectedResult(ResultType result, StateType baselineState, StateType finalState, StateType expectedState);

    void validateResult(ResultType result, ExpectedResultType expectedResult);
}
