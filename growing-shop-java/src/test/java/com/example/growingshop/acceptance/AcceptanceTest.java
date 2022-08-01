package com.example.growingshop.acceptance;

import com.example.growingshop.acceptance.helper.DatabaseCleanup;
import com.example.growingshop.acceptance.helper.Requester;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.Filter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.restassured3.RestAssuredOperationPreprocessorsConfigurer;
import org.springframework.test.context.ActiveProfiles;

import static org.springframework.restdocs.cli.CliDocumentation.curlRequest;
import static org.springframework.restdocs.http.HttpDocumentation.httpRequest;
import static org.springframework.restdocs.http.HttpDocumentation.httpResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestBody;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseBody;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("acceptance")
@ExtendWith(RestDocumentationExtension.class)
public class AcceptanceTest {
    @LocalServerPort
    int port;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    public static RequestSpecification defaultSpec;
    public static RequestSpecification failResponseSpec;

    protected Requester requester = new Requester();
    protected Requester defaultRequester;
    protected Requester failRequester;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        if (RestAssured.port == RestAssured.UNDEFINED_PORT) {
            RestAssured.port = port;
        }

        databaseCleanup.execute();

        defaultSpec = new RequestSpecBuilder()
                .addFilter(defaultAssuredFilter(restDocumentation))
                .build();

        failResponseSpec = new RequestSpecBuilder()
                .addFilter(failAssuredFilter(restDocumentation))
                .build();

        defaultRequester = new Requester(defaultSpec);
        failRequester = new Requester(failResponseSpec);
    }

    private Filter defaultAssuredFilter(RestDocumentationContextProvider restDocumentation) {
        return prettySnippets(restDocumentation).and()
                .snippets()
                .withDefaults(
                        httpRequest(), requestBody(),
                        httpResponse(), responseBody(),
                        curlRequest()
                );
    }

    private Filter failAssuredFilter(RestDocumentationContextProvider restDocumentation) {
        return prettySnippets(restDocumentation).and()
                .snippets()
                .withDefaults(httpResponse());
    }

    private RestAssuredOperationPreprocessorsConfigurer prettySnippets(
            RestDocumentationContextProvider restDocumentation
    ) {
        return documentationConfiguration(restDocumentation)
                .operationPreprocessors()
                .withRequestDefaults(prettyPrint())
                .withResponseDefaults(prettyPrint());
    }
}
