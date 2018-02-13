package com.github.keithwoelke.actions.core;

import com.github.keithwoelke.actions.core.filter.filters.Filter;
import lombok.Getter;

import java.util.ArrayList;

/**
 * ImportOptional can be assigned to fields in an action to control the Import mechanism. It provides the ability to conditionally import values from
 * other actions as well as control which actions the data is being imported from.
 *
 * @author wkwoelke
 */
@SuppressWarnings({"SameParameterValue", "WeakerAccess", "unused"})
@Getter
public class ImportOptional extends ArrayList<Object> {

    private final String description;
    private final Filter filter;

    public ImportOptional(String description) {
        this.description = description;
        filter = null;
    }

    public ImportOptional(String description, Filter filter) {
        this.description = description;
        this.filter = filter;
    }
}
