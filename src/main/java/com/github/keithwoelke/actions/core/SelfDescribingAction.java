package com.github.keithwoelke.actions.core;

/**
 * This interface guarantees that an action has a defined behavior and will also describe itself. Most actions should return some sort result as makes
 * sense to that action. For HTTP requests, it typically makes sense to return the entire response object, but any type of object may be returned.
 *
 * @param <ResultType> the type of result an action will return
 * @author wkwoelke
 */
@SuppressWarnings("WeakerAccess")
public interface SelfDescribingAction<ResultType> extends Actionable<ResultType>, SelfDescribing {

}
