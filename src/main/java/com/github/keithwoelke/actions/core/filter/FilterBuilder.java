package com.github.keithwoelke.actions.core.filter;

import com.github.keithwoelke.actions.core.Action;
import com.github.keithwoelke.actions.core.filter.filters.ExcludeFilter;
import com.github.keithwoelke.actions.core.filter.filters.IncludeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Builder for Filters to allow for cleaner Filter creation without inhibiting Dependency Injection.
 *
 * @author wkwoelke
 */
@SuppressWarnings("unused")
@Service
public class FilterBuilder {

    private final FilterObjectBuilder filterObjectBuilder;

    @Autowired
    public FilterBuilder(FilterObjectBuilder filterObjectBuilder) {
        this.filterObjectBuilder = filterObjectBuilder;
    }

    public ExcludeFilter excludeFilter(Action... actions) {
        return new ExcludeFilter(filterObjectBuilder, actions);
    }

    public ExcludeFilter excludeFilter(PersistenceStrategy persistenceStrategy, Action... actions) {
        return new ExcludeFilter(filterObjectBuilder, persistenceStrategy, actions);
    }

    public ExcludeFilter excludeFilter(int timesToExecute, Action... actions) {
        return new ExcludeFilter(filterObjectBuilder, timesToExecute, actions);
    }

    public IncludeFilter includeFilter(Action... actions) {
        return new IncludeFilter(filterObjectBuilder, actions);
    }

    public IncludeFilter includeFilter(PersistenceStrategy persistenceStrategy, Action... actions) {
        return new IncludeFilter(filterObjectBuilder, persistenceStrategy, actions);
    }

    public IncludeFilter includeFilter(int timesToExecute, Action... actions) {
        return new IncludeFilter(filterObjectBuilder, timesToExecute, actions);
    }
}
