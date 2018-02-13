package com.github.keithwoelke.actions.core;

import com.google.common.collect.Lists;
import com.github.keithwoelke.actions.core.filter.FilterObjectBuilder;
import com.github.keithwoelke.actions.core.filter.FilterSet;
import com.github.keithwoelke.actions.core.http.HttpRequestMethod;
import com.github.keithwoelke.actions.core.result.ActionDetails;
import com.github.keithwoelke.actions.core.result.ExportData;
import com.github.keithwoelke.actions.core.result.Result;
import com.github.keithwoelke.assertion.AssertionRecorder;
import com.github.keithwoelke.assertion.ValidationResult;
import com.github.keithwoelke.assertion.ValidationType;
import com.github.keithwoelke.test.core.http.RequestMethod;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This class is responsible for processing Actions as below: <b>Action:/b> This is treated as a no-op. <b>Actionable:/b> The doAction() method will
 * be executed. <b><Validatable:/b> The getCurrentState(), getExpectedState(), and validate() methods will be executed. <b>Filter:</b> This could be
 * Filter or any of class which derives from Filter. Each filter will be executed via the FilterSet class. <b>FilterControl:</b> FilterControl classes
 * will by processed via the FilterSet class. <b>SelfDescribing:</b> The describeAction() method will be called. AfterAction: The afterAction method
 * will be called immediately following the doAction stage. BeforeAction: The beforeAction method will be called immediately prior to the doAction
 * stage. There are also some interfaces which will combine behaviors for convenience purposes: <b>SelfDescribingAction:</b> See: SelfDescribing,
 * Actionable <b>SelfDescribingValidatableAction:</b> See: SelfDescribing, Validatable, Actionable
 *
 * @author wkwoelke
 */
@SuppressWarnings({"WeakerAccess", "unused"})
@Service
@Slf4j
public class ActionRunner {

    public static final String ERROR_MESSAGE = "An error occurred in the %s stage";
    private final ActionDataImporter actionDataImporter;
    private final ActionDataExporter actionDataExporter;
    private final FilterObjectBuilder filterObjectBuilder;
    private final AssertionRecorder assertionRecorder;

    @Autowired
    public ActionRunner(ActionDataImporter actionDataImporter, ActionDataExporter actionDataExporter, FilterObjectBuilder filterObjectBuilder,
            AssertionRecorder assertionRecorder) {
        this.actionDataImporter = actionDataImporter;
        this.actionDataExporter = actionDataExporter;
        this.filterObjectBuilder = filterObjectBuilder;
        this.assertionRecorder = assertionRecorder;
    }

    /**
     * @see #validateResult(Action, Object, Object)
     */
    private static <ResultType, ExpectedResultType, StateType> void validateResult(Validatable<ResultType, ExpectedResultType, StateType> action,
            ResultType result, ExpectedResultType expectedResult) {
        boolean successfulRequest = false;

        validateHttpRequest(action, result);

        action.validateResult(result, expectedResult);
    }

    /**
     * Tells an HttpRequest action to validate its HttpRequest specific elements.
     *
     * @param action       the HttpRequest Action
     * @param result       the result of the Action
     * @param <ResultType> the Result Type of the Action
     * @param <StateType>  the State Type that corresponds to the Action
     */
    private static <ResultType, ExpectedResultType, StateType> void validateHttpRequest(Validatable<ResultType, ExpectedResultType, StateType>
            action, ResultType result) {
        if (action instanceof HttpRequestMethod) {
            HttpRequestMethod httpRequest = (HttpRequestMethod) action;
            Response response = (Response) result;
            String requestMethod = httpRequest.getRequestMethod();

            List<String> supportedRequestMethods = httpRequest.getSupportedRequestMethods();

            if (!httpRequest.isRecognizedRequestMethod(requestMethod)) {
                httpRequest.validateUnrecognizedRequestMethod(response);
            } else if (!httpRequest.isSupportedRequestMethod(requestMethod)) {
                httpRequest.validateUnsupportedRequestMethod(response, supportedRequestMethods);
            } else if (isOptionsRequestMethod(requestMethod)) {
                httpRequest.validateOptionsRequestMethod(response);
            }
        }
    }

    /**
     * Check if the requestMethod is OPTIONS.
     *
     * @param requestMethod the name of the request method to check
     * @return true, if the request method equals OPTIONS. false, if the request method does not equal OPTIONS
     */
    private static boolean isOptionsRequestMethod(String requestMethod) {
        return RequestMethod.OPTIONS.name().equals(requestMethod);
    }

    /**
     * Runs immediately following execution of the doAction stage. This is a good place to a prepare data for export (if necessary) due to the fact
     * that exceptions thrown here will not stop the doAction stage from returning a result.
     *
     * @param action       the action under execution
     * @param result       the result of the action execution
     * @param <ResultType> the Result Type of the action
     */
    private <ResultType, StateType> void afterAction(Action action, ResultType result) {
        if (action instanceof AfterAction) {
            try {
                logActionStep(String.format("Executing post-action steps for %s", action.getClass().getSimpleName()));
                //noinspection unchecked
                ((AfterAction) action).afterAction(result);
            } catch (Exception e) {
                logException(e, ActionStage.AFTER_ACTION);
            }
        }
    }

    /**
     * Runs immediately following execution of the getFinalState stage. This is a good place to a prepare data for export (if necessary) due to the
     * fact that exceptions thrown here will not stop the doAction stage from returning a result.
     *
     * @param action       the action under execution
     * @param result       the result of the action execution
     * @param <ResultType> the Result Type of the action
     */
    private <ResultType, StateType> void afterState(Action action, ResultType result) {
        if (action instanceof AfterState) {
            try {
                logActionStep(String.format("Executing post-final state steps for %s", action.getClass().getSimpleName()));
                //noinspection unchecked
                ((AfterState) action).afterState(result);
            } catch (Exception e) {
                logException(e, ActionStage.AFTER_STATE);
            }
        }
    }

    /**
     * Runs immediately prior to the execution of the describeAction stage. This is a good place to manipulate data before execution (such as
     * injecting imported values into HTTP request body).
     *
     * @param action       the action under execution
     * @param <ResultType> the Result Type of the action
     */
    private <ResultType> void beforeAction(Action action) {
        if (action instanceof BeforeAction) {
            try {
                logActionStep(String.format("Executing pre-action steps for %s", action.getClass().getSimpleName()));
                //noinspection unchecked
                ((BeforeAction) action).beforeAction();
            } catch (Exception e) {
                logException(e, ActionStage.BEFORE_ACTION);
            }
        }
    }

    /**
     * Runs immediately prior to the execution of the getBaselineState stage. This is a good place to manipulate data before execution (such as
     * injecting imported values into HTTP request body).
     *
     * @param action       the action under execution
     * @param <ResultType> the Result Type of the action
     */
    private <ResultType> void beforeState(Action action) {
        if (action instanceof BeforeState) {
            try {
                logActionStep(String.format("Executing pre-state steps for %s", action.getClass().getSimpleName()));
                //noinspection unchecked
                ((BeforeState) action).beforeState();
            } catch (Exception e) {
                logException(e, ActionStage.BEFORE_STATE);
            }
        }
    }

    /**
     * @param action the action which to describe
     */
    private void describeAction(Action action) {
        if (action instanceof SelfDescribing) {
            try {
                log.info(String.format("***** %s", ((SelfDescribing) action).describeAction()));
            } catch (Exception e) {
                logException(e, ActionStage.DESCRIBE_ACTION);
            }
        }
    }

    /**
     * Execute the action. Behavior depends on the implementation of the action and its interfaces.
     *
     * @param action       the action to execute
     * @param <ResultType> the Result Type of the action
     * @return a List of results returned by the actions provided
     */
    @SuppressWarnings("UnusedReturnValue")
    public <ResultType> ResultType doAction(Action action) {
        return (ResultType) doActions(action).get(0);
    }

    /**
     * @see #doAction(Action)
     */
    public <ResultType> ResultType doAction(Result<ResultType> executionDetails, Action action) {
        List<ResultType> results = doActions(executionDetails, action);

        return (ResultType) results.get(results.size() - 1);
    }

    /**
     * @param dataToImport the source Result object to use for importing data between Actions
     * @see #doAction(Action)
     */
    private <ResultType, StateType, ExpectedResultType> Result<ResultType> doAction(Action action, Result<ResultType> dataToImport) {
        ResultType result = null;
        StateType baselineState;
        StateType expectedState;
        StateType finalState;
        ExportData exportData = new ExportData();
        ExpectedResultType expectedResult;

        try {
            actionDataImporter.importData(dataToImport, action);
        } catch (Exception e) {
            logException(e, ActionStage.IMPORT_DATA);
        }

        beforeState(action);
        //noinspection ConstantConditions
        baselineState = getBaselineState(action, result);
        beforeAction(action);
        describeAction(action);
        result = execute(action);
        afterAction(action, result);
        finalState = getFinalState(action, result);
        afterState(action, result);

        try {
            exportData = actionDataExporter.exportData(action);
        } catch (Exception e) {
            logException(e, ActionStage.EXPORT_DATA);
        }

        expectedState = getExpectedState(action, result, baselineState, finalState);
        expectedResult = getExpectedResult(action, result, baselineState, finalState, expectedState);
        validateState(action, finalState, expectedState);
        validateResult(action, result, expectedResult);

        return new Result<>(action, result, exportData);
    }

    /**
     * Convenience method to execute the action and directly return the result. Useful if you only need the result of the action and you don't need
     * any of the metadata associated with the result.
     *
     * @param action       the action to execute
     * @param <ResultType> the Result type of theaction
     * @return The direct result of executing the doAction stage of the Action.
     * @deprecated Use doAction or doActions. It now returns result objects directly. For result details, use doActionsWithDetails()
     */
    @Deprecated
    public <ResultType> ResultType doActionAndReturnResult(Action action) {
        return doAction(action);
    }

    /**
     * Execute the action. Behavior depends on the implementation of the action and its interfaces.
     *
     * @param action       the action to execute
     * @param <ResultType> the Result Type of the action
     * @return a Result object containing information about the execution of all Actions
     */
    @SuppressWarnings("UnusedReturnValue")
    public <ResultType> Result<ResultType> doActionWithDetails(Action action) {
        return doActionsWithDetails(action);
    }

    /**
     * @see #doActionWithDetails(Action)
     */
    public <ResultType> Result<ResultType> doActionWithDetails(Result<ResultType> executionDetails, Action action) {
        return doActionsWithDetails(executionDetails, action);
    }

    /**
     * @see #doAction(Action)
     */
    public <ResultType> List<ResultType> doActions(Result<ResultType> executionDetails, Action... actions) {
        Result<ResultType> actionDetails = doActionsWithDetails(executionDetails, actions);

        return filterResults(actionDetails);
    }

    /**
     * @see #doAction(Action)
     */
    public <ResultType> List<ResultType> doActions(Action... actions) {
        return doActions(null, actions);
    }

    /**
     * @see #doActionWithDetails(Action)
     */
    public <ResultType> Result<ResultType> doActionsWithDetails(Action... actions) {
        return doActionsWithDetails(null, actions);
    }

    /**
     * @see #doActionWithDetails(Action) (Action)
     */
    public <ResultType> Result<ResultType> doActionsWithDetails(Result<ResultType> executionDetails, Action... actions) {
        Result<ResultType> completeResults = filterObjectBuilder.getResult();

        if (executionDetails != null) {
            completeResults = executionDetails;
        }

        FilterSet filters = filterObjectBuilder.getFilterSet();

        for (Action action : actions) {
            if (filters.process(action)) {
                continue;
            }

            Result<ResultType> filteredResults = filters.applyFilters(completeResults);
            Result<ResultType> result = doAction(action, filteredResults);
            completeResults.add(result);
        }

        return completeResults;
    }

    /**
     * Execute the action.
     *
     * @param action       the action to execute
     * @param <ResultType> the result type of the action
     * @return the result of the action
     */
    private <ResultType> ResultType execute(Action action) {
        ResultType result = null;

        if (action instanceof Actionable) {
            try {
                logActionStep(String.format("Executing %s action", action.getClass().getSimpleName()));
                //noinspection unchecked
                result = ((Actionable<ResultType>) action).doAction();
            } catch (Exception e) {
                logException(e, ActionStage.DO_ACTION);
                throw e;
            }
        }

        return result;
    }

    /**
     * Filter out the extraneous execution details and get the results of the action execution(s).
     *
     * @param actionDetails the execution details.
     * @param <ResultType>  the Result Type of the action
     * @return a list containing the results returned by the actions.
     */
    public <ResultType> List<ResultType> filterResults(Result<ResultType> actionDetails) {
        List<ResultType> results = Lists.newArrayList();

        for (ActionDetails<ResultType> actionDetail : actionDetails.getActionDetails()) {
            results.add(actionDetail.getResult());
        }

        return results;
    }

    /**
     * Get the baseline State as reported by the Action.
     *
     * @param action       the action from which to retrieve state
     * @param result       the result of the action
     * @param <ResultType> the Result Type of the action
     * @param <StateType>  the State Type that corresponds to the Action
     * @return the state reported by the action
     */
    private <ResultType, StateType> StateType getBaselineState(Action action, ResultType result) {
        StateType baselineState = null;

        if (action instanceof ChangesState) {
            try {
                logActionStep(String.format("Storing baseline state for %s", action.getClass().getSimpleName()));
                //noinspection unchecked
                baselineState = ((ChangesState<ResultType, StateType>) action).getCurrentState(result);
            } catch (Exception e) {
                logException(e, ActionStage.GET_BASELINE);
            }
        }

        return baselineState;
    }

    /**
     * Get the expected result of the action. This is more specifica to actions which do not affect state.
     *
     * @param action               the action for which to calculate the expected result
     * @param baselineState        the initial state prior to action execution
     * @param finalState           the final state after action execution
     * @param expectedState        the state expected after action execution
     * @param <ResultType>         the Result Type of the action
     * @param <ExpectedResultType> the expected result type
     * @param <StateType>          the State Type that corresponds to the Action
     * @return a state object which represents the result of the action execution
     */
    private <ResultType, ExpectedResultType, StateType> ExpectedResultType getExpectedResult(Action action, ResultType result, StateType
            baselineState, StateType finalState, StateType expectedState) {
        ExpectedResultType expectedResult = null;

        if (action instanceof Validatable) {
            try {
                logActionStep(String.format("Determining expected result for %s", action.getClass().getSimpleName()));
                //noinspection unchecked
                expectedResult = ((Validatable<ResultType, ExpectedResultType, StateType>) action).getExpectedResult(result, baselineState,
                        finalState, expectedState);
            } catch (Exception e) {
                logException(e, ActionStage.GET_EXPECTED_RESULT);
                throw e;
            }
        }

        return expectedResult;
    }

    /**
     * Get the expected State as reported by the Action.
     *
     * @param <ResultType>  the Result Type of the action
     * @param <StateType>   the State Type that corresponds to the Action
     * @param action        the action from which to retrieve state
     * @param result        the result of the action
     * @param baselineState the baseline State to compare against
     * @param finalState
     * @return the state reported by the action
     */
    private <ResultType, StateType> StateType getExpectedState(Action action, ResultType result, StateType baselineState, StateType finalState) {
        StateType expectedState = null;

        if (action instanceof ChangesState) {
            try {
                logActionStep(String.format("Calculating expected state for %s", action.getClass().getSimpleName()));
                //noinspection unchecked
                expectedState = ((ChangesState<ResultType, StateType>) action).getExpectedState(result, baselineState, finalState);
            } catch (Exception e) {
                logException(e, ActionStage.EXPECTED);
                throw e;
            }
        }

        return expectedState;
    }

    /**
     * Get the final State as reported by the Action.
     *
     * @param action       the action from which to retrieve state
     * @param result       the result of the action
     * @param <ResultType> the Result Type of the action
     * @param <StateType>  the State Type that corresponds to the Action
     * @return the state reported by the action
     */
    private <ResultType, StateType> StateType getFinalState(Action action, ResultType result) {
        StateType finalState = null;

        if (action instanceof ChangesState) {
            try {
                logActionStep(String.format("Storing final state after executing %s", action.getClass().getSimpleName()));
                //noinspection unchecked
                finalState = ((ChangesState<ResultType, StateType>) action).getCurrentState(result);
            } catch (Exception e) {
                logException(e, ActionStage.GET_FINAL_STATE);
            }
        }

        return finalState;
    }

    private void logActionStep(String logMessage) {
        log.debug(String.format("*** %s ***", logMessage));
    }

    /**
     * Log the exception. If debug or trace are enabled, the stack track will be logged. Otherwise, only the exception message will be logged.
     *
     * @param e     the exception to log
     * @param stage the stage of the action execution during which the exception occurred
     */
    private void logException(Exception e, ActionStage stage) {
        if (log.isDebugEnabled()) {
            log.warn(String.format(ERROR_MESSAGE, stage.getStage()), e);
        } else {
            log.warn(String.format(ERROR_MESSAGE + ": %s", stage.getStage(), e));
        }
    }

    /**
     * Tells the Action to validate itself. Some additional validations will be executed if the Action is an HttpRequest. Specifically, the following
     * Action methods <i>may</i> be called: validateUnrecognizedRequestMethod(), validateUnsupportedRequestMethod, validateOptionsRequestMethod().
     *
     * @param action               the action to validate
     * @param result               the result of the Action
     * @param expectedResult       the object which represents the expected result of the action
     * @param <ResultType>         the result type of the action * @param
     * @param <ExpectedResultType> the expected result type
     * @param <StateType>          the State Type that corresponds to the Action
     */
    private <ResultType, ExpectedResultType, StateType> void validateResult(Action action, ResultType result, ExpectedResultType expectedResult) {

        if (action instanceof Validatable) {
            try {
                logActionStep(String.format("Validating %s result", action.getClass().getSimpleName()));
                //noinspection unchecked
                validateResult(((Validatable<ResultType, ExpectedResultType, StateType>) action), result, expectedResult);
            } catch (Exception e) {
                ValidationResult.ValidationResultBuilder validationResultBuilder = ValidationResult.builder();
                validationResultBuilder.
                        success(false).
                        message(String.format(ERROR_MESSAGE, ActionStage.VALIDATE.getStage())).
                        stackTrace(e.getStackTrace()).
                        type(ValidationType.EXPECTATION);

                assertionRecorder.recordValidations(validationResultBuilder.build());
                logException(e, ActionStage.VALIDATE);
                throw e;
            }
        }
    }

    /**
     * Validate that the state after action execution matches the expected state.
     *
     * @param action        the action to validate
     * @param expectedState the object which represents the expected result of the action
     * @param finalState    the object which represents the final state after action execution
     * @param <StateType>   the State Type that corresponds to the Action
     */
    private <ResultType, StateType> void validateState(Action action, StateType finalState, StateType expectedState) {
        if (action instanceof ChangesState) {
            try {
                logActionStep(String.format("Validating %s state", action.getClass().getSimpleName()));
                //noinspection unchecked
                ((ChangesState<ResultType, StateType>) action).validateState(finalState, expectedState);
            } catch (Exception e) {
                logException(e, ActionStage.VALIDATE_STATE);
                throw e;
            }
        }
    }

    /**
     * Stages of an action.
     */
    public enum ActionStage {
        IMPORT_DATA("importData"),
        DESCRIBE_ACTION("(describeAction"),
        GET_BASELINE("getBaselineState"),
        BEFORE_ACTION("beforeAction"),
        DO_ACTION("doAction"),
        AFTER_ACTION("afterAction"),
        EXPORT_DATA("exportData"),
        EXPECTED("getExpectedState"),
        GET_FINAL_STATE("getFinalState"),
        VALIDATE("validateResult"),
        GET_EXPECTED_RESULT("getExpectedResult"),
        VALIDATE_STATE("validateState"),
        BEFORE_STATE("beforeState"),
        AFTER_STATE("afterState");

        private final String stage;

        ActionStage(String stage) {
            this.stage = stage;
        }

        @Override
        public String toString() {
            return "ActionStage{" + "stage='" + stage + '\'' + '}';
        }

        public String getStage() {
            return stage;
        }
    }
}
