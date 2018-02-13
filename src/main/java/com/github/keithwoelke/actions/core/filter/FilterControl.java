package com.github.keithwoelke.actions.core.filter;

import com.google.common.collect.Lists;
import com.github.keithwoelke.actions.core.Action;
import com.github.keithwoelke.actions.core.ActionImpl;
import com.github.keithwoelke.actions.core.filter.filters.Filter;

import java.util.List;

/**
 * The FilterControl class provides a finer control mechanism for Filters. It allows enabling/disabling a filter before or after any action in a
 * scenario. It is also possible to change the execution count through the same mechanism.
 *
 * @author wkwoelke
 */
@SuppressWarnings({"SameParameterValue", "WeakerAccess", "unused"})
public class FilterControl extends ActionImpl implements Action {

    private final Persistence persistence;
    private final List<Filter> filters;

    public FilterControl(PersistenceStrategy persistenceStrategy, Filter... filters) {
        this(persistenceStrategy.timesToExecute(), filters);
    }

    public FilterControl(int timesToExecute, Filter... filters) {
        super(null);

        this.persistence = new Persistence(timesToExecute);
        this.filters = Lists.newArrayList(filters);
    }

    /**
     * Apply the Filter Control to the Filters.
     */
    public void apply() {
        for (Filter filter : filters) {
            filter.setPersistence(persistence);
        }
    }
}
