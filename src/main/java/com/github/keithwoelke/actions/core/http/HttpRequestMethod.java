package com.github.keithwoelke.actions.core.http;

import io.restassured.response.Response;

import java.util.List;

@SuppressWarnings("unused")
public interface HttpRequestMethod {

    List<String> getRecognizedRequestMethods();

    String getRequestMethod();

    List<String> getSupportedRequestMethods();

    boolean isRecognizedRequestMethod(String requestMethod);

    boolean isSupportedRequestMethod(String requestMethod);

    void validateOptionsRequestMethod(Response response);

    void validateUnrecognizedRequestMethod(Response response);

    void validateUnsupportedRequestMethod(Response response, List<String> supportedRequestMethods);
}
