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
public class ExcludeFilterTest {

    private final int TIMES_TO_EXECUTE = 5;
    @Mock
    private TestAction testActionMock;
    @Mock
    private FilterObjectBuilder filterObjectBuilderMock;
    @Mock
    private Persistence persistenceMock;
    @Mock
    private FilterStrategy filterStrategyMock;
    private ExcludeFilter excludeFilter;

    @Test
    public void excludeFilter_actionsConstructor_setsCorrectValues() {
        excludeFilter = new ExcludeFilter(filterObjectBuilderMock, testActionMock);

        assertThat(excludeFilter.getPersistence(), equalTo(persistenceMock));
        assertThat(excludeFilter.getFilterStrategy(), equalTo(filterStrategyMock));
    }

    @Test
    public void excludeFilter_persistenceStrategyAndActionsConstructor_setsCorrectValues() {
        excludeFilter = new ExcludeFilter(filterObjectBuilderMock, PersistenceStrategy.DISABLED, testActionMock);

        assertThat(excludeFilter.getPersistence(), equalTo(persistenceMock));
        assertThat(excludeFilter.getFilterStrategy(), equalTo(filterStrategyMock));
    }

    @Test
    public void excludeFilter_timesToExecuteAndActionsConstructor_setsCorrectValues() {
        excludeFilter = new ExcludeFilter(filterObjectBuilderMock, TIMES_TO_EXECUTE, testActionMock);

        assertThat(excludeFilter.getPersistence(), equalTo(persistenceMock));
        assertThat(excludeFilter.getFilterStrategy(), equalTo(filterStrategyMock));
    }

    @Before
    public void init() {
        when(filterObjectBuilderMock.getPersistence(anyInt())).
                thenReturn(persistenceMock);

        when(filterObjectBuilderMock.getExcludeFilterStrategy(new Action[]{testActionMock})).
                thenReturn(filterStrategyMock);
    }
}
