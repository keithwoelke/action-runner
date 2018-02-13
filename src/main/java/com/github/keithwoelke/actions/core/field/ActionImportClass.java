package com.github.keithwoelke.actions.core.field;

import com.github.keithwoelke.actions.core.Action;
import com.github.keithwoelke.actions.core.annotations.Import;
import com.github.keithwoelke.actions.core.annotations.Imports;
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
public final class ActionImportClass implements ActionClass<ActionImportField> {

    private final Action action;
    private List<Field> importFields;

    /**
     * @see ActionClass#getActionField(Field)
     */
    @Override
    public ActionImportField getActionField(Field field) {
        return new ActionImportField(field, action);
    }

    /**
     * @see ActionClass#getAnnotatedFields()
     */
    @Override
    public List<Field> getAnnotatedFields() {
        if (this.importFields == null) {
            this.importFields = FieldUtils.getFieldsListWithAnnotation(getAction().getClass(), Import.class);
            this.importFields.addAll(FieldUtils.getFieldsListWithAnnotation(getAction().getClass(), Imports.class));
        }

        return this.importFields;
    }
}
