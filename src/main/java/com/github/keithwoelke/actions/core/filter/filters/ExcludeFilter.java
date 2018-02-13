package com.github.keithwoelke.actions.core.filter.filters;

import com.github.keithwoelke.actions.core.Action;
import com.github.keithwoelke.actions.core.filter.FilterObjectBuilder;
import com.github.keithwoelke.actions.core.filter.PersistenceStrategy;
import com.github.keithwoelke.actions.core.filter.strategies.ExcludeFilterStrategy;

/**
 * This filter wraps the logic defined in the ExcludeFilterStrategy.
 *
 * @author wkwoelke
 * @see ExcludeFilterStrategy
 */
public class ExcludeFilter extends Filter {

    public ExcludeFilter(FilterObjectBuilder filterObjectBuilder, Action... actions) {
        this(filterObjectBuilder, null, actions);
    }

    public ExcludeFilter(FilterObjectBuilder filterObjectBuilder, PersistenceStrategy persistenceStrategy, Action... actions) {
        super(filterObjectBuilder, filterObjectBuilder.getExcludeFilterStrategy(actions), persistenceStrategy);
    }

    public ExcludeFilter(FilterObjectBuilder filterObjectBuilder, int timesToExecute, Action... actions) {
        super(filterObjectBuilder, filterObjectBuilder.getExcludeFilterStrategy(actions), timesToExecute);
    }
}
