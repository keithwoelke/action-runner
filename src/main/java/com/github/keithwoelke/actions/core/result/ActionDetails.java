package com.github.keithwoelke.actions.core.result;

import com.github.keithwoelke.actions.core.Action;
import lombok.Data;

/**
 * This class is responsible for storing the details of an Action execution. It stores the data which was exported by the action, the result of the
 * Action.doAction() call, the action itself, and its Class type.
 *
 * @param <ResultType> the Result Type of the Action
 * @author wkwoelke
 */
@Data
public class ActionDetails<ResultType> {

    private final ExportData actionExportData;
    private final ResultType result;
    private final Action action;
    private final Class actionClass;

    public ActionDetails(Action action, ResultType result, ExportData actionExportData) {
        this.actionExportData = actionExportData;
        this.result = result;
        this.action = action;
        this.actionClass = action.getClass();
    }
}
