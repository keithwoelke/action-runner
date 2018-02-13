package com.github.keithwoelke.actions.core;

import com.github.keithwoelke.assertion.AssertionRecorder;

/**
 * Base action implementation.
 *
 * @author wkwoelke
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class ActionImpl implements Action {

    protected final AssertionRecorder assertionRecorder;

    protected ActionImpl(AssertionRecorder assertionRecorder) {
        this.assertionRecorder = assertionRecorder;
    }
}
