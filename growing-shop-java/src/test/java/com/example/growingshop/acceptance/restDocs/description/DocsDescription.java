package com.example.growingshop.acceptance.restDocs.description;

import org.springframework.restdocs.restassured3.RestDocumentationFilter;
import org.springframework.stereotype.Component;

@Component
public interface DocsDescription {
    RestDocumentationFilter addDescription();
}
