package com.github.keithwoelke.actions.core.filter.strategies;

import com.google.common.collect.Lists;
import com.github.keithwoelke.actions.core.Action;
import com.github.keithwoelke.actions.core.result.ActionDetails;
import com.github.keithwoelke.actions.core.result.Result;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This Filter Strategy takes a list of actions to which it should be applied. It will filter out all Actions which are not specified when the filter
 * is constructed.
 * <p>
 * Example:
 * <p>
 * Filter is constructed with [action2, action3].
 * <p>
 * The filter is applied to [action1, action2, action3, action4]
 * <p>
 * The results will be returned for [action2, action3]
 *
 * @author wkwoelke
 */
@Data
public class IncludeFilterStrategy implements FilterStrategy {

    private List<Action> actions;

    public IncludeFilterStrategy(Action... actions) {
        this.actions = Lists.newArrayList(actions);
    }

    /**
     * @see FilterStrategy#apply(Result)
     */
    @Override
    public <ResultType> Result<ResultType> apply(Result<ResultType> results) {
        List<ActionDetails<ResultType>> filteredActionDetails = results.getActionDetails().stream().
                filter(details -> actions.contains(details.getAction())).
                collect(Collectors.toList());

        return new Result<>(filteredActionDetails);
    }
}
