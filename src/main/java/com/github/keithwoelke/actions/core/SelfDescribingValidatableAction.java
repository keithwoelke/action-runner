package com.github.keithwoelke.actions.core;

/**
 * This interface guarantees that an action has a defined behavior and will also describe itself. It also imposes functions necessary to validate the
 * behavior of an action. Most actions should return some sort result as makes sense to that action. For HTTP requests, it typically makes sense to
 * return the entire response object, but any type of object may be returned.
 *
 * @param <ResultType>         the type of result an action will return
 * @param <ExpectedResultType> the object which represents the expected result. It should not be assumed to be directly comparable. It may only
 *                             provide information on which assertions can be derived
 * @param <StateType>          the State Type that corresponds to the Action
 * @author wkwoelke
 */
@SuppressWarnings("WeakerAccess")
public interface SelfDescribingValidatableAction<ResultType, ExpectedResultType, StateType> extends SelfDescribingAction<ResultType>,
        Validatable<ResultType, ExpectedResultType, StateType>, Actionable<ResultType> {

}
