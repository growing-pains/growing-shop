package com.example

import io.restassured.RestAssured
import io.restassured.builder.RequestSpecBuilder
import io.restassured.filter.Filter
import io.restassured.specification.RequestSpecification
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.cli.CliDocumentation
import org.springframework.restdocs.http.HttpDocumentation
import org.springframework.restdocs.operation.preprocess.Preprocessors
import org.springframework.restdocs.restassured.RestAssuredOperationPreprocessorsConfigurer
import org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("acceptance")
@ExtendWith(RestDocumentationExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
open class InitDocs {
    @LocalServerPort
    var port = 0

    @Autowired
    private lateinit var databaseCleanup: DatabaseCleanup

    lateinit var defaultSpec: RequestSpecification

    @BeforeEach
    fun setUp(restDocumentation: RestDocumentationContextProvider) {
        if (RestAssured.port == RestAssured.UNDEFINED_PORT) {
            RestAssured.port = port
        }
        databaseCleanup.execute()
        defaultSpec = RequestSpecBuilder()
            .addFilter(defaultAssuredFilter(restDocumentation))
            .build()
    }

    private fun defaultAssuredFilter(restDocumentation: RestDocumentationContextProvider): Filter {
        return prettySnippets(restDocumentation).and()
            .snippets()
            .withDefaults(
                HttpDocumentation.httpRequest(),
                HttpDocumentation.httpResponse(),
                CliDocumentation.curlRequest()
            )
    }

    private fun prettySnippets(
        restDocumentation: RestDocumentationContextProvider
    ): RestAssuredOperationPreprocessorsConfigurer {
        return documentationConfiguration(restDocumentation)
            .operationPreprocessors()
            .withRequestDefaults(Preprocessors.prettyPrint())
            .withResponseDefaults(Preprocessors.prettyPrint())
    }
}
