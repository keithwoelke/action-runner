package com.github.keithwoelke.actions.core;

/**
 * This interface guarantees that an action has a defined behavior which can be validated.
 *
 * @param <ResultType>         the type of result an action will return
 * @param <ExpectedResultType> the object which represents the expected result. It should not be assumed to be directly comparable. It may only
 *                             provide information on which assertions can be derived.
 * @param <StateType>          the State Type that corresponds to the Action
 * @author wkwoelke
 */
@SuppressWarnings("WeakerAccess")
public interface ValidatableAction<ResultType, ExpectedResultType, StateType> extends Validatable<ResultType, ExpectedResultType, StateType>,
        Actionable<ResultType> {

}
