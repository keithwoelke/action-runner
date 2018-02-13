package com.github.keithwoelke.actions.core.filter.filters;

import com.github.keithwoelke.actions.core.Action;
import com.github.keithwoelke.actions.core.ActionImpl;
import com.github.keithwoelke.actions.core.filter.FilterObjectBuilder;
import com.github.keithwoelke.actions.core.filter.Persistence;
import com.github.keithwoelke.actions.core.filter.PersistenceStrategy;
import com.github.keithwoelke.actions.core.filter.strategies.FilterStrategy;
import com.github.keithwoelke.actions.core.result.Result;
import lombok.Data;

/**
 * The is the base Filter class. It will take a provided FilterStrategy and apply it to any number of actions to generate a filtered Result object
 * which meets the goal of the FilterStrategy.
 *
 * @author wkwoelke
 */
@SuppressWarnings({"unused", "WeakerAccess"})
@Data
public class Filter extends ActionImpl implements Action {

    private final FilterStrategy filterStrategy;
    private Persistence persistence;

    public Filter(FilterObjectBuilder filterObjectBuilder, FilterStrategy filterStrategy) {
        this(filterObjectBuilder, filterStrategy, null);
    }

    public Filter(FilterObjectBuilder filterObjectBuilder, FilterStrategy filterStrategy, PersistenceStrategy persistenceStrategy) {
        this(filterObjectBuilder, filterStrategy, persistenceStrategy == null ? PersistenceStrategy.RUN_ONCE.timesToExecute() : persistenceStrategy
                .timesToExecute());
    }

    public Filter(FilterObjectBuilder filterObjectBuilder, FilterStrategy filterStrategy, int timesToExecute) {
        super(null);

        this.filterStrategy = filterStrategy;
        this.persistence = filterObjectBuilder.getPersistence(timesToExecute);
    }

    /**
     * Apply the FilterStrategy to the Result object. The method first checks the the PersistenceStrategy will permit a filter execution, executing
     * only if the filter is not set to PersistenceStrategy.DISABLED or to a value of 0 for timesToExecute.
     *
     * @param results      the Result object to filter
     * @param <ResultType> the Result Type of the Result object
     * @return The filtered results, if the FilterStrategy is runnable. Otherwise, the original Result object will be returned
     */
    public <ResultType> Result<ResultType> apply(Result<ResultType> results) {
        if (persistence.consumeRunToken()) {
            return filterStrategy.apply(results);
        }

        return results;
    }
}
