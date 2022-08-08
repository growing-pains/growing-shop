package com.example.growingshop.acceptance.restDocs;

import org.springframework.restdocs.restassured3.RestDocumentationFilter;

import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class BodyDocsDescription implements DocsDescription {
    private final String name;
    private final Description description;

    public BodyDocsDescription(String name, Description description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public RestDocumentationFilter addDescription() {
        return document(
                name,
                requestFields(description.generateDescription())
        );
    }
}
