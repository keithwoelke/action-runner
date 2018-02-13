package com.github.keithwoelke.actions.core.stubs;

import com.github.keithwoelke.actions.core.AfterAction;
import com.github.keithwoelke.actions.core.BeforeAction;
import com.github.keithwoelke.actions.core.annotations.Export;

@SuppressWarnings("unused")
public class ActionWithExport extends TestAction implements AfterAction<Object>, BeforeAction {

    public static final String EXPORT_VALUE_OVERRIDE = "exportValueOverride";

    @Export(EXPORT_VALUE_OVERRIDE)
    public Object exportFieldWithName;

    public ActionWithExport() {
        super();
    }

    @Override
    public void afterAction(Object result) {

    }

    @Override
    public void beforeAction() {

    }
}
