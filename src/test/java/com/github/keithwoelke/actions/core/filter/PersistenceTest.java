package com.github.keithwoelke.actions.core.filter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@SuppressWarnings("FieldCanBeLocal")
@RunWith(MockitoJUnitRunner.class)
public class PersistenceTest {

    private final int TIMES_TO_EXECUTE = 500;
    private Persistence persistence;

    @Test
    public void getTimesToExecute_disabled_matchesEnum() {
        int timesToExecute = persistence.getTimesToExecute();

        assertThat(timesToExecute, equalTo(PersistenceStrategy.DISABLED.timesToExecute()));
    }

    @Test
    public void getTimesToExecute_runOnce_matchesEnum() {
        persistence = new Persistence(PersistenceStrategy.RUN_ONCE);

        int timesToExecute = persistence.getTimesToExecute();

        assertThat(timesToExecute, equalTo(PersistenceStrategy.RUN_ONCE.timesToExecute()));
    }

    @Before
    public void init() {
        persistence = new Persistence(PersistenceStrategy.DISABLED);
    }

    @Test
    public void isRunnable_callToIsRunnable_subtractsFromTimesToExecute() {
        persistence = new Persistence(PersistenceStrategy.RUN_ONCE);

        persistence.consumeRunToken();

        assertThat(persistence.getTimesToExecute(), equalTo(0));
    }

    @Test
    public void isRunnable_disabled_returnsFalse() {
        boolean isRunnable = persistence.consumeRunToken();

        assertThat(isRunnable, equalTo(false));
    }

    @Test
    public void isRunnable_runOnce_returnsTrue() {
        persistence = new Persistence(PersistenceStrategy.RUN_ONCE);

        boolean isRunnable = persistence.consumeRunToken();

        assertThat(isRunnable, equalTo(true));
    }

    @Test
    public void persistence_intConstructor_setsCorrectValues() {
        persistence = new Persistence(TIMES_TO_EXECUTE);

        assertThat(persistence.getTimesToExecute(), equalTo(TIMES_TO_EXECUTE));
    }
}
