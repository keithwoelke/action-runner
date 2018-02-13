package com.github.keithwoelke.actions.core.filter.strategies;

import com.github.keithwoelke.actions.core.result.Result;

/**
 * This interface defines the behavior of a FilterStrategy. FilterStrategies are used in conjunction with Filters in order to define the logic for
 * filtering Results.
 *
 * @author wkwoelke
 */
public interface FilterStrategy {

    /**
     * Filter the results.
     *
     * @param results      The Results to filter
     * @param <ResultType> The Result Type of the Results
     * @return the filtered results
     */
    <ResultType> Result<ResultType> apply(Result<ResultType> results);
}
