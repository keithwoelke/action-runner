package com.github.keithwoelke.actions.core;

import com.google.common.collect.Lists;
import com.github.keithwoelke.actions.core.field.ActionImportClass;
import com.github.keithwoelke.actions.core.field.ActionImportField;
import com.github.keithwoelke.actions.core.result.ExportData;
import com.github.keithwoelke.actions.core.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class is responsible for processing the import annotations on a field and determining which data should be injected from the provided data
 * map.
 *
 * @author wkwoelke
 */
@Slf4j
@Service
public class ActionDataImporter {

    /**
     * Check if a field qualifies to have a value imported.
     *
     * @param actionField the field under consideration for import
     * @return true, if the field can be imported, false if the field should not be imported
     */
    private boolean fieldQualifiesForImport(ActionImportField actionField) {
        return (actionField.hasRequiredImport() && actionField.getValue() == null) || actionField.isOptionalImportRequest();
    }

    /**
     * Get all export values which are candidates for import into the field.
     *
     * @param <ResultType> the Result Type of the Action result
     * @param data         the exported data
     * @param field        the field to import data into
     * @return a List of the data exported by each matching class
     */
    private <ResultType> List<Object> getExportCandidatesForField(Result<ResultType> data, ActionImportField field) {
        Result<ResultType> filteredResults = field.applyFilter(data);

        Set<Class> actionClasses = filteredResults.getClassesByMostRecentExecution();

        return actionClasses.stream().
                filter(exportClass -> field.getAnnotationImportClasses().contains(exportClass)).
                flatMap(exportClass -> getExportValuesForClassWithMatchingKeys(filteredResults, exportClass, field.getImportKeysByClass
                        (exportClass)).stream()).
                collect(Collectors.toList());
    }

    /**
     * Get all export values from exportClass with a matching importKey.
     *
     * @param data         the exported data
     * @param exportClass  the class to reference for import value candidates
     * @param importKeys   the keys to filter against for final import value candidates
     * @param <ResultType> The Result Type of the Action result
     * @return a list of values which are both exported from exportClass, but also match one of the provided importKeys
     */
    private <ResultType> List<Object> getExportValuesForClassWithMatchingKeys(Result<ResultType> data, Class exportClass, List<String> importKeys) {
        List<ExportData> exportDataByClass = data.getExportedDataByClass().get(exportClass);
        List<Object> matchingValues = Lists.newArrayList();

        for (ExportData exportData : exportDataByClass) {
            for (String key : exportData.keySet()) {
                if (importKeys.contains(key)) {
                    matchingValues.add(exportData.get(key));
                }
            }
        }

        return matchingValues;
    }

    public <ResultType> void importData(Result<ResultType> data, Action action) {
        log.debug(String.format("Data available to import: %s", data.getExportedDataByClass()));
        importDataToAction(data, action);
    }

    /**
     * Import the result into the Action.
     *
     * @param <ResultType> the Result Type of the Action result
     * @param data         the exported data collection
     * @param action       the action to import values into
     */
    private <ResultType> void importDataToAction(Result<ResultType> data, Action action) {
        ActionImportClass actionClass = new ActionImportClass(action);

        for (Field field : actionClass.getAnnotatedFields()) {
            ActionImportField actionField = actionClass.getActionField(field);

            if (fieldQualifiesForImport(actionField)) {
                importDataToField(data, actionField);
            }
        }
    }

    /**
     * Sets the export value on the field.
     *
     * @param <ResultType> the Result Type of the Action result
     * @param data         the exported data
     * @param field        the field to import data into
     */
    private <ResultType> void importDataToField(Result<ResultType> data, ActionImportField field) {
        List<Object> exportValues = getExportCandidatesForField(data, field);

        field.setValueIntelligently(exportValues);
    }
}
