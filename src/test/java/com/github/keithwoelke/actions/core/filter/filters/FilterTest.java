package com.github.keithwoelke.actions.core.filter.filters;

import com.github.keithwoelke.actions.core.filter.FilterObjectBuilder;
import com.github.keithwoelke.actions.core.filter.Persistence;
import com.github.keithwoelke.actions.core.filter.PersistenceStrategy;
import com.github.keithwoelke.actions.core.filter.strategies.FilterStrategy;
import com.github.keithwoelke.actions.core.filter.strategies.IncludeFilterStrategy;
import com.github.keithwoelke.actions.core.result.Result;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FilterTest {

    private static final int TIMES_TO_EXECUTE = 5;
    @Mock
    private FilterStrategy filterStrategyMock;
    @Mock
    private Result<Object> resultMock;
    @Mock
    private Result<Object> resultMock2;
    @Mock
    private Persistence persistenceMock;
    @Mock
    private IncludeFilterStrategy includeFilterStrategyMock;
    @Mock
    private FilterObjectBuilder filterObjectBuilderMock;
    private Filter filter;

    @Test
    public void apply_filtersApply_resultsFiltered() {
        Result<Object> results = filter.apply(resultMock);

        assertThat(results, equalTo(resultMock2));
    }

    @Test
    public void apply_noFiltersApply_resultsNotModified() {
        when(persistenceMock.consumeRunToken()).
                thenReturn(false);

        Result<Object> results = filter.apply(resultMock);

        assertThat(results, equalTo(resultMock));
    }

    @Test
    public void filter_filterStrategyAndActionsConstructor_setsCorrectValues() {
        filter = new Filter(filterObjectBuilderMock, filterStrategyMock);

        assertThat(filter.getPersistence(), equalTo(persistenceMock));
        assertThat(filter.getFilterStrategy(), equalTo(filterStrategyMock));
    }

    @Test
    public void filter_filterStrategyAndPersistenceStrategyAndActionsConstructor_setsCorrectValues() {
        filter = new Filter(filterObjectBuilderMock, includeFilterStrategyMock, PersistenceStrategy.DISABLED);

        assertThat(filter.getPersistence(), equalTo(persistenceMock));
        assertThat(filter.getFilterStrategy(), equalTo(includeFilterStrategyMock));
    }

    @Test
    public void filter_timesToExecuteAndActionsConstructor_setsCorrectValues() {
        filter = new Filter(filterObjectBuilderMock, includeFilterStrategyMock, TIMES_TO_EXECUTE);

        assertThat(filter.getPersistence(), equalTo(persistenceMock));
        assertThat(filter.getFilterStrategy(), equalTo(includeFilterStrategyMock));
    }

    @Before
    public void init() {
        filter = new Filter(filterObjectBuilderMock, includeFilterStrategyMock, TIMES_TO_EXECUTE);

        filter.setPersistence(persistenceMock);

        when(persistenceMock.consumeRunToken()).
                thenReturn(true);

        when(includeFilterStrategyMock.apply(resultMock)).
                thenReturn(resultMock2);

        when(filterObjectBuilderMock.getPersistence(anyInt())).
                thenReturn(persistenceMock);
    }
}
