package com.github.keithwoelke.actions.core.http;

import com.google.common.collect.Lists;
import com.github.keithwoelke.actions.core.ActionImpl;
import com.github.keithwoelke.actions.core.http.markers.Options;
import com.github.keithwoelke.actions.core.http.markers.raw.RawConnectMarker;
import com.github.keithwoelke.actions.core.http.markers.raw.RawDeleteMarker;
import com.github.keithwoelke.actions.core.http.markers.raw.RawGetMarker;
import com.github.keithwoelke.actions.core.http.markers.raw.RawHeadMarker;
import com.github.keithwoelke.actions.core.http.markers.raw.RawOptionsMarker;
import com.github.keithwoelke.actions.core.http.markers.raw.RawPatchMarker;
import com.github.keithwoelke.actions.core.http.markers.raw.RawPostMarker;
import com.github.keithwoelke.actions.core.http.markers.raw.RawPutMarker;
import com.github.keithwoelke.actions.core.http.markers.raw.RawTraceMarker;
import com.github.keithwoelke.assertion.AssertionRecorder;
import com.github.keithwoelke.test.core.http.RequestMethod;
import io.restassured.response.Response;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.keithwoelke.test.core.restassured.spec.ResponseSpec.allowHeaderResponseSpec;
import static com.github.keithwoelke.test.core.restassured.spec.ResponseSpec.httpStatus200ResponseSpec;
import static com.github.keithwoelke.test.core.restassured.spec.ResponseSpec.httpStatus405ResponseSpec;
import static com.github.keithwoelke.test.core.restassured.spec.ResponseSpec.httpStatus501ResponseSpec;

@SuppressWarnings({"unused", "WeakerAccess"})
public class RestAction extends ActionImpl implements HttpRequestMethod, Options {

    protected final String requestMethod;

    protected RestAction(AssertionRecorder assertionRecorder, String requestMethod) {
        super(assertionRecorder);
        this.requestMethod = requestMethod;
    }

    @Override
    public List<String> getRecognizedRequestMethods() {
        return Arrays.stream(RequestMethod.values()).
                map(Enum::name).
                collect(Collectors.toList());
    }

    @Override
    public String getRequestMethod() {
        return this.requestMethod;
    }

    @Override
    public List<String> getSupportedRequestMethods() {
        List<String> supportedRequestMethods = Lists.newArrayList();

        if (this instanceof RawPutMarker) {
            supportedRequestMethods.add(RequestMethod.PUT.name());
        }

        if (this instanceof RawPostMarker) {
            supportedRequestMethods.add(RequestMethod.POST.name());
        }

        //noinspection ConstantConditions
        if (this instanceof RawOptionsMarker) {
            supportedRequestMethods.add(RequestMethod.OPTIONS.name());
        }

        if (this instanceof RawGetMarker) {
            supportedRequestMethods.add(RequestMethod.GET.name());
        }

        if (this instanceof RawHeadMarker) {
            supportedRequestMethods.add(RequestMethod.HEAD.name());
        }

        if (this instanceof RawTraceMarker) {
            supportedRequestMethods.add(RequestMethod.TRACE.name());
        }

        if (this instanceof RawConnectMarker) {
            supportedRequestMethods.add(RequestMethod.CONNECT.name());
        }

        if (this instanceof RawDeleteMarker) {
            supportedRequestMethods.add(RequestMethod.DELETE.name());
        }

        if (this instanceof RawPatchMarker) {
            supportedRequestMethods.add(RequestMethod.PATCH.name());
        }

        return supportedRequestMethods;
    }

    @Override
    public boolean isRecognizedRequestMethod(String requestMethod) {
        return getRecognizedRequestMethods().stream().
                anyMatch(method -> method.equals(requestMethod));
    }

    @Override
    public boolean isSupportedRequestMethod(String requestMethod) {
        List<String> supportedRequestMethods = getSupportedRequestMethods();

        return supportedRequestMethods.contains(requestMethod);
    }

    @Override
    public void validateOptionsRequestMethod(Response response) {
        assertionRecorder.expectThat("options request should return 200 OK", response, httpStatus200ResponseSpec());
        assertionRecorder.expectThat("options response should list allowed methods", response, allowHeaderResponseSpec(getSupportedRequestMethods()));
    }

    @Override
    public void validateUnrecognizedRequestMethod(Response response) {
        assertionRecorder.expectThat("response for an unrecognized request method should be 501 NOT IMPLEMENTED", response,
                httpStatus501ResponseSpec());
    }

    @Override
    public void validateUnsupportedRequestMethod(Response response, List<String> supportedRequestMethods) {
        assertionRecorder.expectThat("unsupported request method should respond with 405 METHOD NOT ALLOWED", response, httpStatus405ResponseSpec());
        assertionRecorder.expectThat("allow header should return allowed methods", response, allowHeaderResponseSpec(supportedRequestMethods));
    }
}
