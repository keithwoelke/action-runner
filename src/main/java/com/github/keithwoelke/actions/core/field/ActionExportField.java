package com.github.keithwoelke.actions.core.field;

import com.google.common.collect.Lists;
import com.github.keithwoelke.actions.core.Action;
import com.github.keithwoelke.actions.core.annotations.Export;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.List;

/**
 * This class encapsulates Reflection access at the field level. It stores a reference to a class, tightly binding a field to its corresponding
 * class.
 * <p>
 * This class provides functionality targeted toward exporting data from an Action.
 *
 * @author wkwoelke
 */
@EqualsAndHashCode(callSuper = true)
@Data
public final class ActionExportField extends ActionField<Export> {

    private List<Export> annotations;

    public ActionExportField(Field field, Action action) {
        super(field, action, Export.class);
    }

    /**
     * @see ActionField#getAnnotations()
     */
    @Override
    public List<Export> getAnnotations() {
        if (this.annotations == null) {
            this.annotations = Lists.newArrayList(field.getAnnotationsByType(Export.class));
        }

        return this.annotations;
    }

    /**
     * @see ActionField#getName()
     */
    @Override
    public String getName() {
        String fieldName = field.getName();

        Export export = getAnnotations().get(0);

        if (StringUtils.isNotEmpty(export.value())) {
            fieldName = export.value();
        }

        return fieldName;
    }
}
