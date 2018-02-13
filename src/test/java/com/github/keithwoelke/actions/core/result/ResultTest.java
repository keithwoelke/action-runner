package com.github.keithwoelke.actions.core.result;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.github.keithwoelke.actions.core.Action;
import com.github.keithwoelke.actions.core.stubs.MacroAction;
import com.github.keithwoelke.actions.core.stubs.TestAction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.when;

@SuppressWarnings("unused")
@RunWith(MockitoJUnitRunner.class)
public class ResultTest {

    private final String ACTION_RESULT = "actionResult";
    @Mock
    private MacroAction macroActionMock;
    @Mock
    private TestAction testActionMock;
    @Mock
    private Result<Object> individualResultMock;
    @Mock
    private Result<Object> macroResultMock;
    @Mock
    private Result<Object> combinedResultMock;
    @Mock
    private Result<Object> macroFinalResultsMock;
    @Mock
    private ActionDetails<Object> individualActionDetailsMock;
    @Mock
    private ActionDetails<Object> macroActionDetailsMock;
    @Mock
    private ExportData exportDataMock;
    private Result<Object> result;

    @Test
    public void addResults_individualResult_addedToResults() {
        result.add(individualResultMock);

        assertThat(result.getActionDetails().size(), equalTo(1));
        assertThat(result.getActionDetails().get(0), equalTo(individualActionDetailsMock));
    }

    @Test
    public void addResults_multipleIndividualResults_addedToResults() {
        result.add(individualResultMock);
        result.add(individualResultMock);

        assertThat(result.getActionDetails().size(), equalTo(2));
        assertThat(result.getActionDetails().get(0), equalTo(individualActionDetailsMock));
        assertThat(result.getActionDetails().get(1), equalTo(individualActionDetailsMock));
    }

    @Test
    public void addResults_singleMacroWithSingleAction_addsIndividualResults() {
        result.add(macroResultMock);
        result.add(individualResultMock);

        assertThat(result.getActionDetails().size(), equalTo(3));
        assertThat(result.getActionDetails().get(0), equalTo(individualActionDetailsMock));
        assertThat(result.getActionDetails().get(1), equalTo(individualActionDetailsMock));
        assertThat(result.getActionDetails().get(2), equalTo(individualActionDetailsMock));
    }

    @Test
    public void addResults_singleMacro_addsIndividualResults() {
        result.add(macroResultMock);

        assertThat(result.getActionDetails().size(), equalTo(2));
        assertThat(result.getActionDetails().get(0), equalTo(individualActionDetailsMock));
        assertThat(result.getActionDetails().get(1), equalTo(individualActionDetailsMock));
    }

    @Test
    public void getResultsByAction_singleClassMultipleActionDetails_generatesExportDataByAction() {
        result = new Result<>(Lists.newArrayList(individualActionDetailsMock, individualActionDetailsMock));

        ListMultimap<Action, ExportData> results = result.getExportedDataByAction();

        assertThat(results.size(), is(2));
        assertThat(results.get(testActionMock).size(), is(2));
        assertThat(results.get(testActionMock).get(0), is(exportDataMock));
        assertThat(results.get(testActionMock).get(1), is(exportDataMock));
    }

    @Test
    public void getResultsByAction_singleClassMultipleActionDetails_generatesResultsByAction() {
        result = new Result<>(Lists.newArrayList(individualActionDetailsMock, individualActionDetailsMock));

        ListMultimap<Action, Object> results = result.getResultsByAction();

        assertThat(results.size(), is(2));
        assertThat(results.get(testActionMock).size(), is(2));
        assertThat(results.get(testActionMock).get(0), is(ACTION_RESULT));
        assertThat(results.get(testActionMock).get(1), is(ACTION_RESULT));
    }

    @Test
    public void getResultsByAction_singleClassSingleActionDetails_generatesExportDataByAction() {
        result = new Result<>(Lists.newArrayList(individualActionDetailsMock));

        ListMultimap<Action, ExportData> results = result.getExportedDataByAction();

        assertThat(results.size(), is(1));
        assertThat(results.get(testActionMock).size(), is(1));
        assertThat(results.get(testActionMock).get(0), is(exportDataMock));
    }

    @Test
    public void getResultsByAction_singleClassSingleActionDetails_generatesResultsByAction() {
        result = new Result<>(Lists.newArrayList(individualActionDetailsMock));

        ListMultimap<Action, Object> results = result.getResultsByAction();

        assertThat(results.size(), is(1));
        assertThat(results.get(testActionMock).size(), is(1));
        assertThat(results.get(testActionMock).get(0), is(ACTION_RESULT));
    }

    @Test
    public void getResultsByClass_singleClassMultipleActionDetails_generatesExportDataByClass() {
        result = new Result<>(Lists.newArrayList(individualActionDetailsMock, individualActionDetailsMock));

        ListMultimap<Class, ExportData> results = result.getExportedDataByClass();

        assertThat(results.size(), is(2));
        assertThat(results.get(testActionMock.getClass()).size(), is(2));
        assertThat(results.get(testActionMock.getClass()).get(0), is(exportDataMock));
        assertThat(results.get(testActionMock.getClass()).get(1), is(exportDataMock));
    }

    @Test
    public void getResultsByClass_singleClassMultipleActionDetails_generatesResultsByClass() {
        result = new Result<>(Lists.newArrayList(individualActionDetailsMock, individualActionDetailsMock));

        ListMultimap<Class, Object> results = result.getResultsByClass();

        assertThat(results.size(), is(2));
        assertThat(results.get(testActionMock.getClass()).size(), is(2));
        assertThat(results.get(testActionMock.getClass()).get(0), is(ACTION_RESULT));
        assertThat(results.get(testActionMock.getClass()).get(1), is(ACTION_RESULT));
    }

    @Test
    public void getResultsByClass_singleClassSingleActionDetails_generatesExportDataByClass() {
        result = new Result<>(Lists.newArrayList(individualActionDetailsMock));

        ListMultimap<Class, ExportData> results = result.getExportedDataByClass();

        assertThat(results.size(), is(1));
        assertThat(results.get(testActionMock.getClass()).size(), is(1));
        assertThat(results.get(testActionMock.getClass()).get(0), is(exportDataMock));
    }

    @Test
    public void getResultsByClass_singleClassSingleActionDetails_generatesResultsByClass() {
        result = new Result<>(Lists.newArrayList(individualActionDetailsMock));

        ListMultimap<Class, Object> results = result.getResultsByClass();

        assertThat(results.size(), is(1));
        assertThat(results.get(testActionMock.getClass()).size(), is(1));
        assertThat(results.get(testActionMock.getClass()).get(0), is(ACTION_RESULT));
    }

    @Before
    public void init() {
        result = new Result<>();

        when(macroResultMock.getActionDetails()).
                thenReturn(Lists.newArrayList(macroActionDetailsMock));

        when(macroActionDetailsMock.getResult()).
                thenReturn(combinedResultMock);

        when(combinedResultMock.getActionDetails()).
                thenReturn(Lists.newArrayList(individualActionDetailsMock, individualActionDetailsMock));

        when(individualResultMock.getActionDetails()).
                thenReturn(Lists.newArrayList(individualActionDetailsMock));

        when(individualActionDetailsMock.getActionClass()).
                thenReturn(testActionMock.getClass());

        when(individualActionDetailsMock.getResult()).
                thenReturn(ACTION_RESULT);

        when(individualActionDetailsMock.getAction()).
                thenReturn(testActionMock);

        when(individualActionDetailsMock.getActionExportData()).
                thenReturn(exportDataMock);
    }

    @Test
    public void result_actionResultExportedDataConstructor_setsCorrectValues() {
        result = new Result<>(testActionMock, ACTION_RESULT, exportDataMock);

        assertThat(result.getActionDetails().size(), equalTo(1));
        assertThat(result.getActionDetails().get(0).getActionClass(), equalTo(testActionMock.getClass()));
        assertThat(result.getActionDetails().get(0).getAction(), equalTo(testActionMock));
        assertThat(result.getActionDetails().get(0).getResult(), equalTo(ACTION_RESULT));
        assertThat(result.getActionDetails().get(0).getActionExportData(), equalTo(exportDataMock));
    }
}
