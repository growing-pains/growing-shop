package com.example.growingshop.acceptance.restDocs.request;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public interface AcceptanceTestRequest {
    ExtractableResponse<Response> successRequest();
    ExtractableResponse<Response> failRequest();
}
