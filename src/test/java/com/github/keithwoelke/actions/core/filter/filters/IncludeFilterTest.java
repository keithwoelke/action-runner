package com.github.keithwoelke.actions.core.filter.filters;

import com.github.keithwoelke.actions.core.Action;
import com.github.keithwoelke.actions.core.filter.FilterObjectBuilder;
import com.github.keithwoelke.actions.core.filter.Persistence;
import com.github.keithwoelke.actions.core.filter.PersistenceStrategy;
import com.github.keithwoelke.actions.core.filter.strategies.FilterStrategy;
import com.github.keithwoelke.actions.core.stubs.TestAction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SuppressWarnings("FieldCanBeLocal")
@RunWith(MockitoJUnitRunner.class)
public class IncludeFilterTest {

    private final int TIMES_TO_EXECUTE = 5;
    @Mock
    private TestAction testActionMock;
    @Mock
    private FilterObjectBuilder filterObjectBuilderMock;
    @Mock
    private Persistence persistenceMock;
    @Mock
    private FilterStrategy filterStrategyMock;
    private IncludeFilter includeFilter;

    @Test
    public void includeFilter_actionsConstructor_setsCorrectValues() {
        includeFilter = new IncludeFilter(filterObjectBuilderMock, testActionMock);

        assertThat(includeFilter.getPersistence(), equalTo(persistenceMock));
        assertThat(includeFilter.getFilterStrategy(), equalTo(filterStrategyMock));
    }

    @Test
    public void includeFilter_persistenceStrategyAndActionsConstructor_setsCorrectValues() {
        includeFilter = new IncludeFilter(filterObjectBuilderMock, PersistenceStrategy.DISABLED, testActionMock);

        assertThat(includeFilter.getPersistence(), equalTo(persistenceMock));
        assertThat(includeFilter.getFilterStrategy(), equalTo(filterStrategyMock));
    }

    @Test
    public void includeFilter_timesToExecuteAndActionsConstructor_setsCorrectValues() {
        includeFilter = new IncludeFilter(filterObjectBuilderMock, TIMES_TO_EXECUTE, testActionMock);

        assertThat(includeFilter.getPersistence(), equalTo(persistenceMock));
        assertThat(includeFilter.getFilterStrategy(), equalTo(filterStrategyMock));
    }

    @Before
    public void init() {
        when(filterObjectBuilderMock.getPersistence(anyInt())).
                thenReturn(persistenceMock);

        when(filterObjectBuilderMock.getIncludeFilterStrategy(new Action[]{testActionMock})).
                thenReturn(filterStrategyMock);
    }
}
