package com.example

import io.restassured.RestAssured
import io.restassured.http.Method
import io.restassured.response.ExtractableResponse
import io.restassured.response.Response
import io.restassured.specification.RequestSpecification
import org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE

class Spec {
    companion object {
        private val spec: RequestSpecification = RestAssured.given().log().all()
            .accept(APPLICATION_JSON_VALUE)
            .contentType(APPLICATION_JSON_VALUE)

        fun request(
            spec: RequestSpecification,
            method: Method,
            path: String,
            body: Any,
            vararg pathVariables: String
        ): ExtractableResponse<Response> {
            return spec.`when`()
                .spec(spec)
                .body(body)
                .request(method, path, *pathVariables)
                .then().log().all()
                .extract()
        }
    }
}