package com.example.growingshop.acceptance.helper;

public class RequestHelper {
    private RequestHelper() {}

//    public static ExtractableResponse<Response> getRequest(
//            String path, Map<String, String> params, Object... pathVariables
//    ) {
//        return RestAssured.given().log().all()
//                .params(params)
//                .when().get(path, pathVariables)
//                .then().log().all()
//                .extract();
//    }
//
//    public static ExtractableResponse<Response> getRequestWithGenerateRestDocs(
//            String path, Map<String, String> params, Object... pathVariables
//    ) {
//        return replaceFilterDocumentFiler(path)
//                .params(params)
//                .when().get(path, pathVariables)
//                .then().log().all()
//                .extract();
//    }
//
//    public static ExtractableResponse<Response> postRequest(
//            String path, Map<String, String> params, Object body, Object... pathVariables
//    ) {
//        return RestAssured.given().log().all()
//                .params(params)
//                .body(body)
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .when().post(path, pathVariables)
//                .then().log().all()
//                .extract();
//    }
//
//    public static ExtractableResponse<Response> postRequestWithGenerateRestDocs(
//            String path, Map<String, String> params, Object body, Object... pathVariables
//    ) {
//        return replaceFilterDocumentFiler(path)
//                .params(params)
//                .body(body)
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .when().post(path, pathVariables)
//                .then().log().all()
//                .extract();
//    }
//
//    public static ExtractableResponse<Response> deleteRequest(
//            String path, Map<String, String> params, Object... pathVariables
//    ) {
//        return RestAssured.given().log().all()
//                .params(params)
//                .when().delete(path, pathVariables)
//                .then().log().all()
//                .extract();
//    }
//
//    public static ExtractableResponse<Response> deleteRequestWithGenerateRestDocs(
//            String path, Map<String, String> params, Object... pathVariables
//    ) {
//        return replaceFilterDocumentFiler(path)
//                .params(params)
//                .when().delete(path, pathVariables)
//                .then().log().all()
//                .extract();
//    }
//
//    public static ExtractableResponse<Response> putRequest(
//            String path, Map<String, String> params, Object body, Object... pathVariables
//    ) {
//        return RestAssured.given().log().all()
//                .params(params)
//                .body(body)
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .when().put(path, pathVariables)
//                .then().log().all()
//                .extract();
//    }
//
//    public static ExtractableResponse<Response> putRequestWithGenerateRestDocs(
//            String path, Map<String, String> params, Object body, Object... pathVariables
//    ) {
//        return replaceFilterDocumentFiler(path)
//                .params(params)
//                .body(body)
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .when().put(path, pathVariables)
//                .then().log().all()
//                .extract();
//    }
//
//    private static RequestSpecification replaceFilterDocumentFiler(String path) {
//        return RestAssured.given(AcceptanceTest.spec)
//                .log().all()
//                .accept(MediaType.APPLICATION_JSON_VALUE)
//                .filter(document(path.replaceAll("^[\\/]", "")));
//    }
}
