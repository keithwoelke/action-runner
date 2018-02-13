package com.github.keithwoelke.actions.core.field;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.github.keithwoelke.actions.core.Action;
import com.github.keithwoelke.actions.core.ImportOptional;
import com.github.keithwoelke.actions.core.annotations.Import;
import com.github.keithwoelke.actions.core.filter.filters.Filter;
import com.github.keithwoelke.actions.core.result.Result;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This class encapsulates Reflection access at the field level. It stores a reference to a class, tightly binding a field to its corresponding
 * class.
 * <p>
 * This class provides functionality targeted toward importing data into an Action.
 *
 * @author wkwoelke
 */
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("WeakerAccess")
@Data
public final class ActionImportField extends ActionField<Import> {

    private Boolean hasRequiredImport;
    private List<Import> annotations;

    public ActionImportField(Field field, Action action) {
        super(field, action, Import.class);
    }

    /**
     * @see ActionField#getAnnotations()
     */
    @Override
    public List<Import> getAnnotations() {
        if (this.annotations == null) {
            this.annotations = Lists.newArrayList(field.getAnnotationsByType(Import.class));
        }

        return this.annotations;
    }

    /**
     * Apply any filters assigned the ImportOptional request (if one is provided). After the filter is applied, the field will import data as usual
     * (with a potentially reduced data set). It should be noted that applyFilter will reduce the data set as it receives it from any other previous
     * actions, which may already be in a filtered state. More to the point, you cannot reference actions in an ImportOptional filter if the data
     * exported from that action has already been filtered out at an earlier point in time.
     *
     * @param data         the data to filter
     * @param <ResultType> the Result Type of the Action
     * @return the filtered results
     */
    public <ResultType> Result<ResultType> applyFilter(Result<ResultType> data) {
        Result<ResultType> filteredResults = data;
        Optional<ImportOptional> importOptional = getOptionalImportRequest();

        if (importOptional.isPresent()) {
            Filter filter = importOptional.get().getFilter();

            if (filter != null) {
                filteredResults = filter.apply(data);
            }
        }

        return filteredResults;
    }

    /**
     * Get a list of classes from which a field can import.
     *
     * @return a list of classes from which the field can import
     */
    public List<Class> getAnnotationImportClasses() {
        return getAnnotations().stream().
                map(Import::actionClass).
                collect(Collectors.toList());
    }

    /**
     * Get all annotations on this field which have exportClass as their importing actionClass.
     *
     * @param actionClass the actionClass specified in the Import annotations
     * @return a list of Import annotations which have Import.actionClass() equal to actionClass
     */
    public List<Import> getAnnotationsByActionClass(Class actionClass) {
        return getAnnotationsByActionClass().get(actionClass);
    }

    /**
     * Get a map of the annotations on this field by the actionClasses they import from.
     *
     * @return a mapping of actionClass to the corresponding annotations.
     */
    public ListMultimap<Class, Import> getAnnotationsByActionClass() {
        ListMultimap<Class, Import> annotationsByImportClass = LinkedListMultimap.create();

        getAnnotations().forEach(annotation -> annotationsByImportClass.put(annotation.actionClass(), annotation));

        return annotationsByImportClass;
    }

    /**
     * Get a list of import keys by actionClass. This effectively de-dupes Imports from the same Action class and reduces them to the keys they
     * import.
     *
     * @param actionClass the actionClass to return import keys for
     * @return a List of keys which should be imported from an actions of class type actionClass.
     */
    public List<String> getImportKeysByClass(Class actionClass) {
        return getAnnotationsByActionClass(actionClass).stream().
                map(Import::importKey).
                collect(Collectors.toList());
    }

    /**
     * Get the ImportOptional request assigned to this field. The returned Optional will be empty if there is no OptionalImport request to return.
     *
     * @return the ImportOptional request assigned to this field
     */
    private Optional<ImportOptional> getOptionalImportRequest() {
        Optional<ImportOptional> value = Optional.empty();

        if (isOptionalImportRequest()) {
            value = Optional.ofNullable((ImportOptional) getValue());
        }

        return value;
    }

    /**
     * Checks whether or not a field has a required import. This will be true unless all imports are marked as optional=true.
     *
     * @return true, if there are any import annotations on the field marked as optional=false. false, if all annotations are marked as optional=true
     */
    public boolean hasRequiredImport() {
        if (this.hasRequiredImport == null) {
            this.hasRequiredImport = false;

            for (Import importAnnotation : getAnnotations()) {
                if (!importAnnotation.optional()) {
                    this.hasRequiredImport = true;
                    break;
                }
            }
        }

        return this.hasRequiredImport;
    }

    /**
     * Check if a Field type is a List
     *
     * @return true, if the Field is a List type. false, if the Field type is anything else
     */
    public boolean isListType() {
        return List.class.isAssignableFrom(field.getType());
    }

    public boolean isOptionalImportRequest() {
        return getValue() instanceof ImportOptional;
    }

    /**
     * The business logic around setting a field value. Use setValue to directly set the value of the field. Use setValueIntelligently to
     * conditionally import values based on import field type, current field value, etc.
     * <p>
     * If there are no values to import, the field will be left untouched with one exception. After all other logic has been executed, if the field
     * remains assigned an OptionalImport reference, it will be overridden with a null value. Otherwise, the logic is as follows: If there are no
     * values to import, do nothing except override any ImportOptional references. If there are values to import, check the data type of the field
     * which is importing the value. If it is a List type, then all values will be imported as a List. If the type is anything other than a List, the
     * last value in the list will be imported.
     * <p>
     * The logic of the insertion priority assumes that the values are provided in execution order and that the last value in the list will be the
     * most applicable value to import.
     *
     * @param exportValues a list of exported values which are candidates for import
     */
    public void setValueIntelligently(List<Object> exportValues) {
        if (!exportValues.isEmpty()) {
            if (isListType()) {
                setValue(exportValues);
            } else {
                Object mostRecentActionExport = exportValues.get(exportValues.size() - 1);
                setValue(mostRecentActionExport);
            }
        }

        if (isOptionalImportRequest()) {
            setValue(null);
        }
    }
}
