package com.github.keithwoelke.actions.core.filter.filters;

import com.github.keithwoelke.actions.core.Action;
import com.github.keithwoelke.actions.core.filter.FilterObjectBuilder;
import com.github.keithwoelke.actions.core.filter.PersistenceStrategy;
import com.github.keithwoelke.actions.core.filter.strategies.IncludeFilterStrategy;

/**
 * This filter wraps the logic defined in the IncludeFilterStrategy.
 *
 * @author wkwoelke
 * @see IncludeFilterStrategy
 */
public class IncludeFilter extends Filter {

    public IncludeFilter(FilterObjectBuilder filterObjectBuilder, Action... actions) {
        this(filterObjectBuilder, null, actions);
    }

    public IncludeFilter(FilterObjectBuilder filterObjectBuilder, PersistenceStrategy persistenceStrategy, Action... actions) {
        super(filterObjectBuilder, filterObjectBuilder.getIncludeFilterStrategy(actions), persistenceStrategy);
    }

    public IncludeFilter(FilterObjectBuilder filterObjectBuilder, int timesToExecute, Action... actions) {
        super(filterObjectBuilder, filterObjectBuilder.getIncludeFilterStrategy(actions), timesToExecute);
    }
}
