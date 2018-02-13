package com.github.keithwoelke.actions.core.stubs;

import com.github.keithwoelke.actions.core.ActionImpl;
import com.github.keithwoelke.actions.core.ChangesState;
import com.github.keithwoelke.actions.core.SelfDescribingValidatableAction;
import com.github.keithwoelke.actions.core.annotations.Export;

@SuppressWarnings("unused")
public class TestAction extends ActionImpl implements ChangesState<Object, Object>, SelfDescribingValidatableAction<Object, Void, Void> {

    public static final String EXPORT_FIELD_NAME = "exportField";
    @Export
    public Object exportField;
    private Object returnValue;

    TestAction() {
        super(null);
    }

    public TestAction(Object returnValue, Object exportValue) {
        super(null);

        this.returnValue = returnValue;
        this.exportField = exportValue;
    }

    @Override
    public String describeAction() {
        return null;
    }

    @Override
    public Object doAction() {
        return returnValue;
    }

    @Override
    public Void getExpectedResult(Object result, Void baselineState, Void finalState, Void expectedState) {
        return null;
    }

    @Override
    public void validateResult(Object result, Void expected) {

    }

    @Override
    public Void getCurrentState(Object result) {
        return null;
    }

    @Override
    public Void getExpectedState(Object result, Object baselineState, Object finalState) {
        return null;
    }

    @Override
    public void validateState(Object expectedState, Object finalState) {

    }
}
