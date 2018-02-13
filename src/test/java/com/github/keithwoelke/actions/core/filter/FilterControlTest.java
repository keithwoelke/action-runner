package com.github.keithwoelke.actions.core.filter;

import com.github.keithwoelke.actions.core.filter.filters.Filter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class FilterControlTest {

    @Mock
    private Filter filterMock;
    @Mock
    private Filter filterMock2;

    private FilterControl filterControl;

    @Test
    public void apply_multipleFilters_persistenceStrategyAppliedToAll() {
        filterControl = new FilterControl(PersistenceStrategy.DISABLED, filterMock, filterMock2);

        filterControl.apply();

        verify(filterMock).setPersistence(any(Persistence.class));
        verify(filterMock2).setPersistence(any(Persistence.class));
    }

    @Test
    public void apply_singleFilter_persistenceStrategyApplied() {
        filterControl.apply();

        verify(filterMock).setPersistence(any(Persistence.class));
    }

    @Before
    public void init() {
        filterControl = new FilterControl(PersistenceStrategy.DISABLED, filterMock);
    }
}
