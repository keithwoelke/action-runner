package com.github.keithwoelke.actions.core;

import com.github.keithwoelke.actions.core.result.ExportData;
import com.github.keithwoelke.actions.core.stubs.ActionWithExport;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

@SuppressWarnings("unused")
@RunWith(MockitoJUnitRunner.class)
public class ActionDataExporterTest {

    private static final String EXPORT_FIELD = "exportField";
    private static final String EXPORT_FIELD_WITH_NAME = "exportFieldWithName";
    private static final String EXPORT_FIELD_NAME = "exportField";

    private ActionWithExport actionWithExport;
    private ExportData export1;
    private ActionDataExporter actionDataExporter;

    @Test
    public void exportData_basicExport_exportsWithVariableName() {
        ExportData exportData = actionDataExporter.exportData(actionWithExport);

        assertThat(exportData.get(ActionWithExport.EXPORT_FIELD_NAME), equalTo(EXPORT_FIELD));
    }

    @Test
    public void exportData_exportData_exportsCorrectNumberOfValues() {
        ExportData exportData = actionDataExporter.exportData(actionWithExport);

        assertThat(exportData.size(), is(2));
    }

    @Test
    public void exportData_exportNullData_exportsNullField() {
        actionWithExport.exportField = null;

        ExportData exportData = actionDataExporter.exportData(actionWithExport);

        assertThat(exportData.get(EXPORT_FIELD_NAME), nullValue());
    }

    @Test
    public void exportData_exportWithName_exportsWithOverride() {
        ExportData exportData = actionDataExporter.exportData(actionWithExport);

        assertThat(exportData.get(ActionWithExport.EXPORT_VALUE_OVERRIDE), equalTo(EXPORT_FIELD_WITH_NAME));
    }

    @Before
    public void init() {
        actionDataExporter = new ActionDataExporter();

        actionWithExport = new ActionWithExport();

        actionWithExport.exportField = EXPORT_FIELD;
        actionWithExport.exportFieldWithName = EXPORT_FIELD_WITH_NAME;
    }
}
