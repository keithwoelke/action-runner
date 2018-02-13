package com.github.keithwoelke.actions.core.field;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Store an Action for the purpose of encapsulating access through Reflection. This class is primarily used to retrieve an ActionField object but is
 * also used to get information about fields with annotations.
 *
 * @author wkwoelke
 */
@SuppressWarnings("unused")
public interface ActionClass<ActionFieldType> {

    /**
     * Retrieve an ActionField object for the class/field combination. Encapsulates Reflection access to the field without needing to individually
     * store both Class and Field references.
     *
     * @param field the field to access within the corresponding class
     * @return an ActionField object encapsulating access to the Class/Field combination
     */
    ActionFieldType getActionField(Field field);

    /**
     * Get annotated fields in the Class.
     *
     * @return a list of annotated fields
     */
    List<Field> getAnnotatedFields();
}
