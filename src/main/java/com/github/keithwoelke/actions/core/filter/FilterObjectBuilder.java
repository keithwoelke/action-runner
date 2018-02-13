package com.github.keithwoelke.actions.core.filter;

import com.github.keithwoelke.actions.core.Action;
import com.github.keithwoelke.actions.core.filter.strategies.ExcludeFilterStrategy;
import com.github.keithwoelke.actions.core.filter.strategies.FilterStrategy;
import com.github.keithwoelke.actions.core.filter.strategies.IncludeFilterStrategy;
import com.github.keithwoelke.actions.core.result.Result;
import org.springframework.stereotype.Service;

/**
 * This class builds objects to be used in other classes. It is for the purpose of facilitating mocking class dependencies for testing purposes.
 *
 * @author wkwoelke
 */
@Service
public class FilterObjectBuilder {

    public FilterStrategy getExcludeFilterStrategy(Action[] actions) {
        return new ExcludeFilterStrategy(actions);
    }

    public FilterSet getFilterSet() {
        return new FilterSet();
    }

    public FilterStrategy getIncludeFilterStrategy(Action[] actions) {
        return new IncludeFilterStrategy(actions);
    }

    public Persistence getPersistence(int timesToExecute) {
        return new Persistence(timesToExecute);
    }

    public <ResultType> Result<ResultType> getResult() {
        return new Result<>();
    }
}
