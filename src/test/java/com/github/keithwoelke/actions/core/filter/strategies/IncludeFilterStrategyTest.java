package com.github.keithwoelke.actions.core.filter.strategies;

import com.google.common.collect.Lists;
import com.github.keithwoelke.actions.core.result.ActionDetails;
import com.github.keithwoelke.actions.core.result.Result;
import com.github.keithwoelke.actions.core.stubs.TestAction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.when;

@SuppressWarnings("FieldCanBeLocal")
@RunWith(MockitoJUnitRunner.class)
public class IncludeFilterStrategyTest {

    @Mock
    private Result<Object> resultMock;
    @Mock
    private TestAction testActionMock;
    @Mock
    private TestAction testActionMock2;
    @Mock
    private ActionDetails<Object> actionDetailsMock;
    @Mock
    private ActionDetails<Object> actionDetailsMock2;

    private IncludeFilterStrategy includeFilterStrategy;

    @Test
    public void apply_multipleActions_includeOnlySpecified() {
        includeFilterStrategy = new IncludeFilterStrategy(testActionMock);

        Result<Object> filteredResults = includeFilterStrategy.apply(resultMock);

        assertThat(filteredResults.getActionDetails().size(), equalTo(1));
        assertThat(filteredResults.getActionDetails().get(0), equalTo(actionDetailsMock));
    }

    @Before
    public void init() {
        when(resultMock.getActionDetails()).
                thenReturn(Lists.newArrayList(actionDetailsMock, actionDetailsMock2));

        when(actionDetailsMock.getAction()).
                thenReturn(testActionMock);

        when(actionDetailsMock2.getAction()).
                thenReturn(testActionMock2);
    }
}
