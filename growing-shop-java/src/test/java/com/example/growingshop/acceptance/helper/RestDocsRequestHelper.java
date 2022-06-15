package com.example.growingshop.acceptance.helper;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.MediaType;

import java.util.Map;

import static com.example.growingshop.acceptance.AcceptanceTest.failResponseSpec;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class RestDocsRequestHelper {
    private RestDocsRequestHelper() {}

    public static ExtractableResponse<Response> getRequest(
            RequestSpecification spec, String path, Map<String, String> params, Object... pathVariables
    ) {
        return replaceFilterDocumentFiler(spec, path)
                .params(params)
                .when().get(path, pathVariables)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> postRequest(
            RequestSpecification spec, String path, Map<String, String> params, Object body, Object... pathVariables
    ) {
        return replaceFilterDocumentFiler(spec, path)
                .params(params)
                .body(body)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(path, pathVariables)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> deleteRequest(
            RequestSpecification spec, String path, Map<String, String> params, Object... pathVariables
    ) {
        return replaceFilterDocumentFiler(spec, path)
                .params(params)
                .when().delete(path, pathVariables)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> putRequest(
            RequestSpecification spec, String path, Map<String, String> params, Object body, Object... pathVariables
    ) {
        return replaceFilterDocumentFiler(spec, path)
                .params(params)
                .body(body)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put(path, pathVariables)
                .then().log().all()
                .extract();
    }

    private static RequestSpecification replaceFilterDocumentFiler(RequestSpecification spec, String path) {
        String distPath = path + postfixPath(spec);

        return RestAssured.given(spec)
                .log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .filter(document(distPath.replaceAll("^[\\/]", "")));
    }

    private static String postfixPath(RequestSpecification spec) {
        if (spec == failResponseSpec) {
            return "/fail";
        }

        return "";
    }
}
