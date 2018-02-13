package com.github.keithwoelke.actions.core.stubs;

import com.github.keithwoelke.actions.core.ActionImpl;
import com.github.keithwoelke.actions.core.ActionRunner;
import com.github.keithwoelke.actions.core.Actionable;

import java.util.List;

@SuppressWarnings("unused")
public class MacroAction extends ActionImpl implements Actionable<List<Object>> {

    private final ActionRunner actionRunner;
    private final Actionable[] actions;

    public MacroAction(ActionRunner actionRunner, Actionable... actions) {
        super(null);

        this.actionRunner = actionRunner;
        this.actions = actions;
    }

    @Override
    public List<Object> doAction() {
        return actionRunner.doActions(actions);
    }
}
