package io.github.jinahya.rickandmortyapi.spring.web.bind;

import io.github.jinahya.rickandmortyapi.spring.web.bind.type.CharacterType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.test.web.reactive.server.EntityExchangeResult;

import java.util.Optional;

@Slf4j
class CharacterControllerTest
        extends _BaseControllerTest {

    @Test
    void readCharacters__() {
        String nextUri = "/" + CharacterController.REQUEST_MAPPING_PATH;
        while (true) {
            final EntityExchangeResult<PagedModel<EntityModel<CharacterType>>> result = webTestClient()
                    .get()
                    .uri(nextUri)
                    .accept(MediaTypes.HAL_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentType(MediaTypes.HAL_JSON)
                    .expectBody(new ParameterizedTypeReference<PagedModel<EntityModel<CharacterType>>>() {
                    })
                    .returnResult();
            final PagedModel<EntityModel<CharacterType>> body = result.getResponseBody();
            final Optional<Link> nextLink = body.getLink(IanaLinkRelations.NEXT);
            if (nextLink.isEmpty()) {
                break;
            }
            final String nextHref = nextLink.get().getHref();
            nextUri = nextHref.substring(nextHref.indexOf("/", 8));
        }
    }
}
