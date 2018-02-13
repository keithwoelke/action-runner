package com.github.keithwoelke.actions.core;

import com.beust.jcommander.internal.Sets;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import com.github.keithwoelke.actions.core.result.ExportData;
import com.github.keithwoelke.actions.core.result.Result;
import com.github.keithwoelke.actions.core.stubs.ActionWithExport;
import com.github.keithwoelke.actions.core.stubs.ActionWithExport2;
import com.github.keithwoelke.actions.core.stubs.ActionWithImport;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Set;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.when;

@SuppressWarnings({"FieldCanBeLocal"})
@RunWith(MockitoJUnitRunner.class)
public class ActionDataImporterTest {

    private static final String ACTION_EXPORT = "actionExport";
    private static final String ACTION_EXPORT_2 = "actionExport2";
    @Mock
    private ImportOptional importOptionalMock;
    @Mock
    private Result<Object> resultMock;
    private ListMultimap<Class, ExportData> exportDataByClass;
    private Set<Class> classesByMostRecentExecutionOrder;
    private ExportData exportData;
    private ExportData exportData2;
    private ActionWithImport actionWithImport;
    private ActionDataImporter actionDataImporter;

    @Test
    public void doActions_importFieldHasNoCandidate_fieldIsNull() {
        actionDataImporter.importData(new Result<>(), actionWithImport);

        assertThat(actionWithImport.nonMatchingOptionalImportField, nullValue());
    }

    @Test
    public void doActions_nullOptionalImportWithCandidate_fieldIsNull() {
        actionWithImport.optionalImportField = null;

        exportData.put(ActionWithImport.IMPORT_FIELD, ACTION_EXPORT);

        actionDataImporter.importData(resultMock, actionWithImport);

        assertThat(actionWithImport.optionalImportField, nullValue());
    }

    @Test
    public void importData_fieldExportsOnce_imports() {
        actionDataImporter.importData(resultMock, actionWithImport);

        assertThat(actionWithImport.importField, equalTo(ACTION_EXPORT));
    }

    @Test
    public void importData_fieldExportsTwice_fieldCorrectlyImportsList() {
        exportDataByClass.put(ActionWithExport.class, exportData);

        actionDataImporter.importData(resultMock, actionWithImport);

        assertThat(actionWithImport.importFields.size(), is(2));
        assertThat(actionWithImport.importFields.get(0), equalTo(ACTION_EXPORT));
        assertThat(actionWithImport.importFields.get(1), equalTo(ACTION_EXPORT));
    }

    @Test
    public void importData_multipleImportAnnotationsInExecutionOrder_importsFromMostRecentExecution() {
        exportDataByClass.put(ActionWithExport2.class, exportData2);

        classesByMostRecentExecutionOrder.add(ActionWithExport2.class);

        actionDataImporter.importData(resultMock, actionWithImport);

        assertThat(actionWithImport.importWithMultipleAnnotations, equalTo(ACTION_EXPORT_2));
    }

    @Test
    public void importData_multipleImportAnnotationsInReverseExecutionOrder_importsFromMostRecentExecution() {
        exportDataByClass.clear();
        exportDataByClass.put(ActionWithExport2.class, exportData2);
        exportDataByClass.put(ActionWithExport.class, exportData);

        classesByMostRecentExecutionOrder.clear();
        classesByMostRecentExecutionOrder.add(ActionWithExport2.class);
        classesByMostRecentExecutionOrder.add(ActionWithExport.class);

        actionDataImporter.importData(resultMock, actionWithImport);

        assertThat(actionWithImport.importWithMultipleAnnotations, equalTo(ACTION_EXPORT));
    }

    @Test
    public void importData_nonNullImportField_doesNotOverwriteValue() {
        actionWithImport.importField = ACTION_EXPORT;

        actionDataImporter.importData(resultMock, actionWithImport);

        assertThat(actionWithImport.importField, equalTo(ACTION_EXPORT));
    }

    @Test
    public void importData_optionalImportWithCandidate_imports() {
        actionWithImport.optionalImportField = importOptionalMock;

        actionDataImporter.importData(resultMock, actionWithImport);

        assertThat(actionWithImport.optionalImportField, equalTo(ACTION_EXPORT));
    }

    @Test
    public void importData_optionalImportWithIncludeFilter_importsFromCorrectAction() {
        actionWithImport.importField = importOptionalMock;

        exportData.put(ActionWithExport.EXPORT_FIELD_NAME, ACTION_EXPORT_2);

        actionDataImporter.importData(resultMock, actionWithImport);

        assertThat(actionWithImport.importField, equalTo(ACTION_EXPORT_2));
    }

    @Test
    public void importData_optionalImportWithListAndMultipleCandidates_importsList() {
        actionWithImport.importFields = importOptionalMock;

        exportDataByClass.put(ActionWithExport.class, exportData);

        actionDataImporter.importData(resultMock, actionWithImport);

        assertThat(actionWithImport.importFields.size(), is(2));
        assertThat(actionWithImport.importFields.get(0), equalTo(ACTION_EXPORT));
        assertThat(actionWithImport.importFields.get(1), equalTo(ACTION_EXPORT));
    }

    @Test
    public void importData_optionalImportWithListAndNoCandidate_importsEmptyList() {
        actionWithImport.importFields = importOptionalMock;

        actionDataImporter.importData(new Result<>(), actionWithImport);

        assertThat(actionWithImport.importFields, nullValue());
    }

    @Test
    public void importData_optionalImportWithListAndSingleCandidate_importsList() {
        actionWithImport.importFields = importOptionalMock;

        actionDataImporter.importData(resultMock, actionWithImport);

        assertThat(actionWithImport.importFields.size(), is(1));
        assertThat(actionWithImport.importFields.get(0), equalTo(ACTION_EXPORT));
    }

    @Test
    public void importData_optionalImportWithNoCandidate_setToNull() {
        actionWithImport.nonMatchingOptionalImportField = importOptionalMock;

        actionDataImporter.importData(new Result<>(), actionWithImport);

        assertThat(actionWithImport.nonMatchingOptionalImportField, nullValue());
    }

    @Test
    public void importData_requiredImportWithImportObject_overridesImportObject() {
        actionWithImport.importField = importOptionalMock;

        actionDataImporter.importData(resultMock, actionWithImport);

        assertThat(actionWithImport.importField, equalTo(ACTION_EXPORT));
    }

    @Test
    public void importData_sameClassWithNonContiguousExecutions_importsFromMostRecentExecution() {
        exportDataByClass.put(ActionWithExport2.class, exportData2);
        exportDataByClass.put(ActionWithExport.class, exportData);

        classesByMostRecentExecutionOrder.clear();
        classesByMostRecentExecutionOrder.add(ActionWithExport2.class);
        classesByMostRecentExecutionOrder.add(ActionWithExport.class);

        actionDataImporter.importData(resultMock, actionWithImport);

        assertThat(actionWithImport.importWithMultipleAnnotations, equalTo(ACTION_EXPORT));
    }

    @Before
    public void init() {
        actionDataImporter = new ActionDataImporter();

        // when(actionDataExporterMock.getValueFromField(any(Action.class), any(Field.class))).thenCallRealMethod();

        exportData = new ExportData();
        exportData.put(ActionWithImport.IMPORT_FIELD, ACTION_EXPORT);

        exportData2 = new ExportData();
        exportData2.put(ActionWithImport.IMPORT_FIELD, ACTION_EXPORT_2);

        exportDataByClass = LinkedListMultimap.create();
        exportDataByClass.put(ActionWithExport.class, exportData);

        when(resultMock.getExportedDataByClass()).
                thenReturn(exportDataByClass);

        classesByMostRecentExecutionOrder = Sets.newLinkedHashSet();
        classesByMostRecentExecutionOrder.add(ActionWithExport.class);

        when(resultMock.getClassesByMostRecentExecution()).
                thenReturn(classesByMostRecentExecutionOrder);

        actionWithImport = new ActionWithImport();
    }
}
