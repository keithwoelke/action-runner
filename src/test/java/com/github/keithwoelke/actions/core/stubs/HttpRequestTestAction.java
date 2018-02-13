package com.github.keithwoelke.actions.core.stubs;

import com.github.keithwoelke.actions.core.http.HttpRequestMethod;
import io.restassured.response.Response;

import java.util.List;

@SuppressWarnings("unused")
public class HttpRequestTestAction extends TestAction implements HttpRequestMethod {

    public HttpRequestTestAction() {
        super();
    }

    @Override
    public List<String> getRecognizedRequestMethods() {
        return null;
    }

    @Override
    public String getRequestMethod() {
        return null;
    }

    @Override
    public List<String> getSupportedRequestMethods() {
        return null;
    }

    @Override
    public boolean isRecognizedRequestMethod(String requestMethod) {
        return false;
    }

    @Override
    public boolean isSupportedRequestMethod(String requestMethod) {
        return false;
    }

    @Override
    public void validateOptionsRequestMethod(Response response) {

    }

    @Override
    public void validateUnrecognizedRequestMethod(Response response) {

    }

    @Override
    public void validateUnsupportedRequestMethod(Response response, List<String> supportedRequestMethods) {

    }
}
