package com.example.growingshop.acceptance.restDocs.request;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public interface AcceptanceTestDocsRequest {
    ExtractableResponse<Response> successRequestWithDocs();
    ExtractableResponse<Response> failRequestWithDocs();
}
