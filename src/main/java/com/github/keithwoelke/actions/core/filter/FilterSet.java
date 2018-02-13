package com.github.keithwoelke.actions.core.filter;

import com.google.common.collect.Sets;
import com.github.keithwoelke.actions.core.Action;
import com.github.keithwoelke.actions.core.filter.filters.Filter;
import com.github.keithwoelke.actions.core.result.Result;
import lombok.Data;

import java.util.Set;

/**
 * A FilterSet stores Filters and processes both Filters and FilterControls. When process(Action) is called, if the provided Action is a Filter, it
 * will simply be stored for later use. If the Action is a FilterControl, it will be immediately applied to all stored Filters.
 *
 * @author wkwoelke
 */
@Data
public class FilterSet {

    private Set<Filter> filters = Sets.newLinkedHashSet();

    /**
     * Apply the Filters sequentially, accumulating all changes as each intermediate result is processed by subsequent filters.
     *
     * @param results      the Result object to filter
     * @param <ResultType> the Result Type of the Result object
     * @return the filtered results
     */
    public <ResultType> Result<ResultType> applyFilters(Result<ResultType> results) {
        for (Filter filter : filters) {
            results = filter.apply(results);
        }

        return results;
    }

    /**
     * Processes either a Filter or a FilterControl. If a Filter is provided, it will be stored for later use. If a FilterControl is supplied, it will
     * be immediately executed. All other Action types will be ignored.
     *
     * @param action the Action to process
     * @return true, if a Filter or FilterControl was processed. false, if any other type of action was provided
     */
    public boolean process(Action action) {
        if (action instanceof Filter) {
            filters.add((Filter) action);
            return true;
        }

        if (action instanceof FilterControl) {
            ((FilterControl) action).apply();
            return true;
        }

        return false;
    }
}
