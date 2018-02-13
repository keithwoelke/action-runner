package com.github.keithwoelke.actions.core;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.github.keithwoelke.actions.core.filter.FilterControl;
import com.github.keithwoelke.actions.core.filter.FilterObjectBuilder;
import com.github.keithwoelke.actions.core.filter.FilterSet;
import com.github.keithwoelke.actions.core.filter.filters.ExcludeFilter;
import com.github.keithwoelke.actions.core.filter.filters.IncludeFilter;
import com.github.keithwoelke.actions.core.result.ActionDetails;
import com.github.keithwoelke.actions.core.result.Result;
import com.github.keithwoelke.actions.core.stubs.ActionWithExport;
import com.github.keithwoelke.actions.core.stubs.HttpRequestTestAction;
import com.github.keithwoelke.actions.core.stubs.MacroAction;
import com.github.keithwoelke.actions.core.stubs.TestAction;
import com.github.keithwoelke.assertion.AssertionRecorder;
import com.github.keithwoelke.test.core.http.RequestMethod;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.isNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
@RunWith(MockitoJUnitRunner.class)
public class ActionRunnerTest {

    @Mock
    private Response responseMock;
    @Mock
    private ActionDetails actionDetailsMock;
    @Mock
    private ActionDataImporter importAnnotationProcessorMock;
    @Mock
    private ActionDataExporter actionDataExporterMock;
    @Mock
    private IncludeFilter includeFilterMock;
    @Mock
    private ExcludeFilter excludeFilterMock;
    @Mock
    private FilterSet filterSetMock;
    @Mock
    private FilterObjectBuilder filterObjectBuilderMock;
    @Mock
    private TestAction testActionMock;
    @Mock
    private FilterControl filterControlMock;
    @Mock
    private HttpRequestTestAction httpRequestTestActionMock;
    @Mock
    private MacroAction macroActionMock;
    @Mock
    private Result resultMock;
    @Mock
    private Result filteredResultMock;
    @Mock
    private ActionWithExport actionWithExport;
    @Mock
    private AssertionRecorder assertionRecorderMock;

    private ActionRunner actionRunner;

    @Before
    public void init() {
        actionRunner = new ActionRunner(importAnnotationProcessorMock, actionDataExporterMock, filterObjectBuilderMock, assertionRecorderMock);

        when(filterObjectBuilderMock.getFilterSet()).
                thenReturn(filterSetMock);

        when(filterObjectBuilderMock.getResult()).
                thenReturn(resultMock);

        when(httpRequestTestActionMock.doAction()).
                thenReturn(responseMock);

        when(httpRequestTestActionMock.isRecognizedRequestMethod(any(String.class))).
                thenReturn(true);

        when(testActionMock.doAction()).
                thenReturn(responseMock);

        when(filterSetMock.process(testActionMock)).
                thenReturn(false);

        when(filterSetMock.process(includeFilterMock)).
                thenReturn(true);

        when(filterSetMock.process(filterControlMock)).
                thenReturn(true);

        when(filterSetMock.applyFilters(resultMock)).
                thenReturn(filteredResultMock);

        ArrayListMultimap<Action, Response> resultsByAction = ArrayListMultimap.create();
        resultsByAction.put(testActionMock, responseMock);

        when(resultMock.getActionDetails()).
                thenReturn(Lists.newArrayList(actionDetailsMock));

        when(actionWithExport.doAction()).
                thenReturn(resultMock);
    }

    @Test
    public void doActionWithDetails_singleAction_returnsDetailResults() {
        Result<Object> result = actionRunner.doActionWithDetails(testActionMock);

        assertThat(result.getActionDetails().get(0), equalTo(actionDetailsMock));
    }

    @Test
    public void doAction_actionImplementsAfterAction_callsAfterAction() {
        actionRunner.doAction(actionWithExport);

        verify(actionWithExport).afterAction(any());
    }

    @Test
    public void doAction_actionImplementsBeforeAction_callsBeforeAction() {
        actionRunner.doAction(actionWithExport);

        verify(actionWithExport).beforeAction();
    }

    @Test
    public void doAction_actionImplementsChangesState_callsGetCurrentStateTwice() {
        actionRunner.doAction(testActionMock);

        verify(testActionMock, times(2)).getCurrentState(any());
    }

    @Test
    public void doAction_actionImplementsChangesState_callsGetExpectedState() {
        actionRunner.doAction(testActionMock);

        verify(testActionMock).getExpectedState(any(), any(), any());
    }

    @Test
    public void doAction_actionImplementsChangesState_callsValidateState() {
        actionRunner.doAction(testActionMock);

        verify(testActionMock).validateState(any(), any());
    }

    @Test
    public void doAction_httpOptionsRequestMethodNotSupported_callsValidateUnsupported() {
        when(httpRequestTestActionMock.getRequestMethod()).thenReturn(RequestMethod.OPTIONS.name());
        when(httpRequestTestActionMock.isSupportedRequestMethod(any(String.class))).thenReturn(false);

        actionRunner.doAction(httpRequestTestActionMock);

        verify(httpRequestTestActionMock).validateUnsupportedRequestMethod(any(Response.class), any(List.class));
    }

    @Test
    public void doAction_httpOptionsRequestMethod_callsValidateOptions() {
        when(httpRequestTestActionMock.getRequestMethod()).thenReturn(RequestMethod.OPTIONS.name());
        when(httpRequestTestActionMock.isSupportedRequestMethod(any(String.class))).thenReturn(true);

        actionRunner.doAction(httpRequestTestActionMock);

        verify(httpRequestTestActionMock).validateOptionsRequestMethod(any(Response.class));
    }

    @Test
    public void doAction_httpRequestMethodNotRecognized_callsValidateUnrecognized() {
        when(httpRequestTestActionMock.isRecognizedRequestMethod(any(String.class))).thenReturn(false);
        when(httpRequestTestActionMock.getRequestMethod()).thenReturn(RequestMethod.OPTIONS.name());

        actionRunner.doAction(httpRequestTestActionMock);

        verify(httpRequestTestActionMock).validateUnrecognizedRequestMethod(any(Response.class));
    }

    @Test
    public void doAction_httpRequestMethod_callsValidate() {
        actionRunner.doAction(httpRequestTestActionMock);

        verify(httpRequestTestActionMock).validateResult(any(Response.class), isNull());
    }

    @Test
    public void doAction_nonHttpRequestMethod_callsValidate() {
        actionRunner.doAction(testActionMock);

        verify(testActionMock).validateResult(any(Response.class), any());
    }

    @Test
    public void doActionsWithDetails_multipleActions_returnsDetailResults() {
        Result<Object> result = actionRunner.doActionsWithDetails(testActionMock, testActionMock);

        assertThat(result.getActionDetails().get(0), equalTo(actionDetailsMock));
    }

    @Test
    public void doActions_filterAction_finalResultNotFiltered() {
        Result<Object> result = actionRunner.doActionsWithDetails(testActionMock, includeFilterMock);

        assertThat(result, equalTo(resultMock));
    }

    @Test
    public void doActions_filterAfterAction_actionExecutes() {
        actionRunner.doActions(testActionMock, includeFilterMock);

        verify(testActionMock).doAction();
        verify(filterSetMock).applyFilters(any(Result.class));
    }

    @Test
    public void doActions_filterBeforeAction_actionAndFilterExecute() {
        actionRunner.doActions(includeFilterMock, testActionMock);

        verify(testActionMock).doAction();
        verify(filterSetMock).applyFilters(any(Result.class));
    }

    @Test
    public void doActions_filterControlAfterAction_actionExecutes() {
        actionRunner.doActions(testActionMock, filterControlMock);

        verify(testActionMock).doAction();
        verify(filterSetMock).applyFilters(any(Result.class));
    }

    @Test
    public void doActions_filterControlBeforeAction_actionExecutes() {
        actionRunner.doActions(filterControlMock, testActionMock);

        verify(testActionMock).doAction();
        verify(filterSetMock).applyFilters(any(Result.class));
    }

    @Test
    public void doActions_macroWithAction_allActionsExecute() {
        actionRunner.doActions(macroActionMock, testActionMock);

        verify(macroActionMock).doAction();
        verify(testActionMock).doAction();
    }

    @Test
    public void doActions_multipleFilters_allFiltersAdded() {
        actionRunner.doActions(includeFilterMock, excludeFilterMock);

        verify(filterSetMock).process(includeFilterMock);
        verify(filterSetMock).process(excludeFilterMock);
    }
}
