package com.github.keithwoelke.actions.core.field;

import com.github.keithwoelke.actions.core.Action;
import com.github.keithwoelke.actions.core.annotations.Export;
import lombok.Data;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.List;

/**
 * This class encapsulates Reflection access for classes which Export data.
 *
 * @author wkwoelke
 */
@Data
public final class ActionExportClass implements ActionClass<ActionExportField> {

    private final Action action;
    private List<Field> exportFields;

    /**
     * @see ActionClass#getActionField(Field)
     */
    @Override
    public ActionExportField getActionField(Field field) {
        return new ActionExportField(field, action);
    }

    /**
     * @see ActionClass#getAnnotatedFields()
     */
    @Override
    public List<Field> getAnnotatedFields() {
        if (this.exportFields == null) {
            this.exportFields = FieldUtils.getFieldsListWithAnnotation(action.getClass(), Export.class);
        }

        return this.exportFields;
    }
}
