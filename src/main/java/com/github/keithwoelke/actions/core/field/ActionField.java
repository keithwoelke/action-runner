package com.github.keithwoelke.actions.core.field;

import com.google.common.collect.Lists;
import com.github.keithwoelke.actions.core.Action;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

/**
 * This class encapsulates Reflection access for fields.
 *
 * @param <AnnotationType> the annotation type that this field will encapsulate
 * @author wkwoelke
 */
@SuppressWarnings({"WeakerAccess", "unused"})
@RequiredArgsConstructor
public class ActionField<AnnotationType extends Annotation> {

    protected final Field field;
    private final Action action;
    private final Class<AnnotationType> annotationClass;
    private List<AnnotationType> annotations;

    /**
     * Get annotations on this field.
     *
     * @return a list of annotations on the field
     */
    public List<AnnotationType> getAnnotations() {
        if (annotations == null) {
            this.annotations = Lists.newArrayList(field.getAnnotationsByType(annotationClass));
        }

        return this.annotations;
    }

    /**
     * Get the name of the field as defined by the import/export action.
     *
     * @return the name of the field
     */
    public String getName() {
        return field.getName();
    }

    /**
     * Get the current value of the field.
     *
     * @return the value of the field
     */
    @SneakyThrows
    public Object getValue() {
        boolean isAccessible = field.isAccessible();
        field.setAccessible(true);

        Object value = field.get(action);
        field.setAccessible(isAccessible);

        return value;
    }

    /**
     * Assign a value to a field in the provided action.
     *
     * @param value the value to inject
     */
    protected void setValue(Object value) {
        try {
            boolean isAccessible = field.isAccessible();
            field.setAccessible(true);
            field.set(action, value);
            field.setAccessible(isAccessible);
        } catch (IllegalAccessException e) {
            throw new Error(e);
        }
    }
}
