package com.example.growingshop.acceptance.helper;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.MediaType;

import java.util.Map;

import static com.example.growingshop.acceptance.AcceptanceTest.failResponseSpec;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class Requester {
    private final RequestSpecification spec;

    public Requester(RequestSpecification spec) {
        this.spec = spec;
    }

    public Requester() {
        this(null);
    }

    public ExtractableResponse<Response> request(
            String path,
            Method method,
            Map<String, String> params,
            Object body,
            Object... pathVariables
    ) {
        return createSpecification(path).log().all()
                .params(params)
                .body(body)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().request(method, path, pathVariables)
                .then().log().all()
                .extract();
    }

    private RequestSpecification createSpecification(String path) {
        RequestSpecification specification = RestAssured.given().log().all();
        String distPath = path + postfixPath(spec);

        if (spec != null) {
            specification.spec(spec)
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .filter(document(distPath.replaceAll("^[\\/]", "")));
        }

        return specification;
    }

    private static String postfixPath(RequestSpecification spec) {
        if (spec == failResponseSpec) {
            return "/fail";
        }

        return "";
    }
}
