package com.github.keithwoelke.actions.core.filter;

import com.github.keithwoelke.actions.core.filter.filters.Filter;
import com.github.keithwoelke.actions.core.result.Result;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FilterSetTest {

    @Mock
    private Filter filterMock;
    @Mock
    private Filter filterMock2;
    @Mock
    private FilterControl filterControlMock;
    @Mock
    private Result<Object> resultMock;
    @Mock
    private Result<Object> resultMock2;
    @Mock
    private Result<Object> resultMock3;

    private FilterSet filterSet;

    @Test
    public void apply_multipleFilters_resultsFilteredMultipleTimes() {
        filterSet.process(filterMock);
        filterSet.process(filterMock2);

        Result<Object> result = filterSet.applyFilters(resultMock);

        assertThat(filterSet.getFilters().size(), equalTo(2));
        verify(filterMock).apply(resultMock);
        verify(filterMock2).apply(resultMock2);
        assertThat(result, equalTo(resultMock3));
    }

    @Test
    public void apply_singleFilter_resultsFilteredOnce() {
        filterSet.process(filterMock);

        Result<Object> result = filterSet.applyFilters(resultMock);

        assertThat(filterSet.getFilters().size(), equalTo(1));
        verify(filterMock).apply(resultMock);
        assertThat(result, equalTo(resultMock2));
    }

    @Before
    public void init() {
        filterSet = new FilterSet();

        when(filterMock.apply(resultMock)).
                thenReturn(resultMock2);

        when(filterMock2.apply(resultMock2)).
                thenReturn(resultMock3);
    }

    @Test
    public void process_filterControl_filterControlApplied() {
        filterSet.process(filterControlMock);

        assertThat(filterSet.getFilters().size(), equalTo(0));
        verify(filterControlMock).apply();
    }

    @Test
    public void process_filter_filterAdded() {
        filterSet.process(filterMock);

        assertThat(filterSet.getFilters().size(), equalTo(1));
        verify(filterMock, times(0)).apply(resultMock);
    }
}
