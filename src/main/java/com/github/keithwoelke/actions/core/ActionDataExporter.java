package com.github.keithwoelke.actions.core;

import com.github.keithwoelke.actions.core.field.ActionExportClass;
import com.github.keithwoelke.actions.core.field.ActionExportField;
import com.github.keithwoelke.actions.core.result.ExportData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;

/**
 * This class is responsible for processing the export annotations on a field and determining which data should be exported.
 *
 * @author wkwoelke
 */
@Service
@Slf4j
public class ActionDataExporter {

    /**
     * Get the exported data from the Action.
     *
     * @param action the action to pull the export data from
     * @return an exported data
     */
    public ExportData exportData(Action action) {
        ActionExportClass actionClass = new ActionExportClass(action);

        ExportData exportedData = new ExportData();
        for (Field field : actionClass.getAnnotatedFields()) {
            ActionExportField actionField = actionClass.getActionField(field);
            exportedData.put(actionField.getName(), actionField.getValue());
        }

        log.debug(String.format("%s exports: %s", action.getClass().getSimpleName(), exportedData));

        return exportedData;
    }
}
