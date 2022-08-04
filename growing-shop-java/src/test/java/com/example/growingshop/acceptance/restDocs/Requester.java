package com.example.growingshop.acceptance.restDocs;

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
    private final RequestSpecification spec = RestAssured.given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE);
    private String path;
    private Method method;
    private Object[] pathVariables;

    public ExtractableResponse<Response> request() {
        return spec.when()
                .request(method, path, pathVariables)
                .then().log().all()
                .extract();
    }

    public static class RequesterBuilder {
        private Requester requester = new Requester();

        public RequesterBuilder(String path, Method method) {
            requester.path = path;
            requester.method = method;
        }

        public RequesterBuilder spec(RequestSpecification spec) {
            String distPath = requester.path + postfixPath(spec);
            requester.spec
                    .spec(spec)
                    .filter(document(distPath.replaceAll("^[\\/]", "")));

            return this;
        }

        public RequesterBuilder description(Description description) {
            requester.spec.filter(description.addDescription());

            return this;
        }

        public RequesterBuilder body(Object body) {
            requester.spec.body(body);

            return this;
        }

        public RequesterBuilder params(Map<String, Object> params) {
            requester.spec.params(params);

            return this;
        }

        public Requester build(Object... pathVariables) {
            requester.pathVariables = pathVariables;

            return requester;
        }

        private static String postfixPath(RequestSpecification spec) {
            if (spec == failResponseSpec) {
                return "/fail";
            }

            return "";
        }
    }
}
