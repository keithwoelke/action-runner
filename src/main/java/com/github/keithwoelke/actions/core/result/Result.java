package com.github.keithwoelke.actions.core.result;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.github.keithwoelke.actions.core.Action;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

/**
 * The Result class stores the Result of one or more Actions.
 *
 * @param <ResultType> the Result Type of the Result object
 * @author wkwoelke
 */
@SuppressWarnings("unused")
@NoArgsConstructor
@Data
public class Result<ResultType> {

    private final List<ActionDetails<ResultType>> actionDetails = Lists.newArrayList();

    public Result(Action action, ResultType result, ExportData exportedData) {
        ActionDetails<ResultType> actionResultCollection = new ActionDetails<>(action, result, exportedData);
        this.actionDetails.add(actionResultCollection);
    }

    public Result(List<ActionDetails<ResultType>> actionDetails) {
        this.actionDetails.addAll(actionDetails);
    }

    /**
     * Merge the provided Results into this Result collection.
     *
     * @param results the Result to merge
     */
    public void add(Result<ResultType> results) {
        for (ActionDetails<ResultType> actionResultCollection : results.getActionDetails()) {
            ResultType result = actionResultCollection.getResult();

            if (result instanceof Result) {
                //noinspection unchecked
                actionDetails.addAll(((Result) result).getActionDetails());
            } else {
                actionDetails.add(actionResultCollection);
            }
        }
    }

    /**
     * Returns a list of classes ordered by execution. However, unlike a simple call to .keys(), this function will re-order the classes to match the
     * execution order according the the last execution of a class type.
     * <p>
     * Example:
     * <p>
     * Execution order: Class1, Class2, Class1
     * <p>
     * A call to .key() would return the execution order by first execution: [Class1, Class2].
     * <p>
     * A call to getClassesByMostRecentExecution() will instead return [Class2, Class1].
     *
     * @return a list of classes ordered by most recent execution.
     */
    public Set<Class> getClassesByMostRecentExecution() {
        Set<Class> classesByImportPriority = Sets.newLinkedHashSet();

        for (Class actionClass : getExportedDataByClass().keys()) {
            if (classesByImportPriority.contains(actionClass)) {
                classesByImportPriority.remove(actionClass);
            }

            classesByImportPriority.add(actionClass);
        }

        return classesByImportPriority;
    }

    /**
     * @return a map of the exported data with a lookup by the Action object.
     */
    public ListMultimap<Action, ExportData> getExportedDataByAction() {
        ListMultimap<Action, ExportData> exportData = LinkedListMultimap.create();

        for (ActionDetails<ResultType> actionResultCollection : actionDetails) {
            exportData.put(actionResultCollection.getAction(), actionResultCollection.getActionExportData());
        }

        return exportData;
    }

    /**
     * @return a map of the exported data with a lookup by the Action class.
     */
    public ListMultimap<Class, ExportData> getExportedDataByClass() {
        ListMultimap<Class, ExportData> exportData = LinkedListMultimap.create();

        for (ActionDetails<ResultType> actionResultCollection : actionDetails) {
            exportData.put(actionResultCollection.getActionClass(), actionResultCollection.getActionExportData());
        }

        return exportData;
    }

    /**
     * @return a map of the Action results with a lookup by the Action object.
     */
    public ListMultimap<Action, ResultType> getResultsByAction() {
        ListMultimap<Action, ResultType> exportData = LinkedListMultimap.create();

        for (ActionDetails<ResultType> actionResultCollection : actionDetails) {
            exportData.put(actionResultCollection.getAction(), actionResultCollection.getResult());
        }

        return exportData;
    }

    /**
     * @return a map of the Action results with a lookup by the Action class.
     */
    public ListMultimap<Class, ResultType> getResultsByClass() {
        ListMultimap<Class, ResultType> exportData = LinkedListMultimap.create();

        for (ActionDetails<ResultType> actionResultCollection : actionDetails) {
            exportData.put(actionResultCollection.getActionClass(), actionResultCollection.getResult());
        }

        return exportData;
    }
}
