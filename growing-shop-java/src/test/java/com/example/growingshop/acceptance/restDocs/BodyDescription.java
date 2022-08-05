package com.example.growingshop.acceptance.restDocs;

import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class BodyDescription implements Description {
    private final String name;
    private final Map<String, String> description;

    public BodyDescription(String name, Map<String, String> description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public RestDocumentationFilter addDescription() {
        return document(
                name,
                requestFields(generateDescription())
        );
    }

    private List<FieldDescriptor> generateDescription() {
        return description.entrySet()
                .stream()
                .map(entry -> fieldWithPath(entry.getKey()).description(entry.getValue()))
                .collect(Collectors.toList());
    }
}
