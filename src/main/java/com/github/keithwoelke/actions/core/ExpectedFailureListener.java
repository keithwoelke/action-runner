package com.github.keithwoelke.actions.core;

import com.google.common.collect.Lists;
import com.github.keithwoelke.actions.core.annotations.ExpectedFailure;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.lang.reflect.Method;
import java.util.List;

public class ExpectedFailureListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
    }

    @Override
    public void onTestSuccess(ITestResult result) {
    }

    @Override
    public void onTestFailure(ITestResult result) {
        Method testMethod = result.getMethod().getConstructorOrMethod().getMethod();

        if (testMethod.isAnnotationPresent(ExpectedFailure.class)) {
            ExpectedFailure annotation = testMethod.getAnnotation(ExpectedFailure.class);
            List<String> jiraTickets = Lists.newArrayList(annotation.value());

            for (String jiraTicket : jiraTickets) {

            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
    }

    @Override
    public void onStart(ITestContext context) {
    }

    @Override
    public void onFinish(ITestContext context) {
    }
}