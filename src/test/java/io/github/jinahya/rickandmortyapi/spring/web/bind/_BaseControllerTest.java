package io.github.jinahya.rickandmortyapi.spring.web.bind;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.hateoas.config.HypermediaWebTestClientConfigurer;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Slf4j
@SuppressWarnings({
        "java:S101" // Class names should comply with a naming convention
})
abstract class _BaseControllerTest {

    WebTestClient webTestClient() {
        if (webTestClient == null) {
            baseUrl = "http://localhost:" + port;
            final var client = WebTestClient.bindToServer().baseUrl(baseUrl).build();
            webTestClient = client.mutateWith(clientConfigurer);
        }
        return webTestClient;
    }

    // ------------------------------------------------------------------------------------------------------------ port

    // --------------------------------------------------------------------------------------------------------- baseUrl
    String getBaseUrl() {
        return baseUrl;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @LocalServerPort
    private int port;

    private String baseUrl;

    // -----------------------------------------------------------------------------------------------------------------
    @Autowired
    private HypermediaWebTestClientConfigurer clientConfigurer;

    private WebTestClient webTestClient;
}
