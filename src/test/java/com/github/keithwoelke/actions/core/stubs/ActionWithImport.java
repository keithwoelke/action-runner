package com.github.keithwoelke.actions.core.stubs;

import com.github.keithwoelke.actions.core.annotations.Import;

import java.util.List;

@SuppressWarnings({"CanBeFinal", "unused"})
public class ActionWithImport extends TestAction {

    public static final String IMPORT_FIELD = EXPORT_FIELD_NAME;
    public static final String EXPORT_VALUE_OVERRIDE = ActionWithExport.EXPORT_VALUE_OVERRIDE;

    @Import(actionClass = ActionWithExport.class, importKey = IMPORT_FIELD)
    public Object importField;

    @Import(actionClass = ActionWithExport.class, importKey = "nonMatchingExportField")
    public Object nonMatchingImportField;

    @Import(actionClass = ActionWithExport.class, importKey = IMPORT_FIELD)
    public List<Object> importFields;

    @Import(actionClass = ActionWithExport.class, importKey = IMPORT_FIELD, optional = true)
    public Object optionalImportField;

    @Import(actionClass = ActionWithExport.class, importKey = "nonMatchingOptionalExportField", optional = true)
    public Object nonMatchingOptionalImportField;
    @Import(actionClass = ActionWithExport.class, importKey = EXPORT_VALUE_OVERRIDE)
    public Object importWithExportNameOverride;

    @Import(actionClass = ActionWithExport.class, importKey = IMPORT_FIELD)
    @Import(actionClass = ActionWithExport2.class, importKey = IMPORT_FIELD)
    public Object importWithMultipleAnnotations;

    public ActionWithImport() {
        super();
    }
}
