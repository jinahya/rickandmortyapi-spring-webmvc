package io.github.jinahya.rickandmortyapi.spring.web.bind;

import io.github.jinahya.rickandmortyapi.persistence._PersistenceConstants;
import io.github.jinahya.rickandmortyapi.spring.web.bind.type.CharacterType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.test.web.reactive.server.EntityExchangeResult;

import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class CharactersControllerTest
        extends _BaseControllerTest {

    @Test
    void readCharacters__() {
        int count = 0;
        for (String uri = "/" + CharactersController.REQUEST_MAPPING_PATH; ; ) {
            final EntityExchangeResult<PagedModel<EntityModel<CharacterType>>> result = webTestClient()
                    .get()
                    .uri(uri)
                    .accept(MediaTypes.HAL_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentType(MediaTypes.HAL_JSON)
                    .expectBody(new ParameterizedTypeReference<PagedModel<EntityModel<CharacterType>>>() {
                    })
                    .returnResult();
            final PagedModel<EntityModel<CharacterType>> body = result.getResponseBody();
            {
                assert body != null;
                final Collection<EntityModel<CharacterType>> content = body.getContent();
                count += content.size();
            }
            final Optional<Link> nextLink = body.getLink(IanaLinkRelations.NEXT);
            if (nextLink.isEmpty()) {
                break;
            }
            final String nextHref = nextLink.get().getHref();
            uri = nextHref.substring(getBaseUrl().length());
        }
        assertThat(count).isEqualTo(_PersistenceConstants.NUMBER_OF_ALL_CHARACTERS);
    }

    @ValueSource(ints = {1, 2, 3})
    @ParameterizedTest
    void readCharacter__(final int id) {
        final EntityExchangeResult<EntityModel<CharacterType>> result = webTestClient()
                .get()
                .uri(b -> b.pathSegment(CharactersController.REQUEST_MAPPING_PATH,
                                        CharactersController.PATH_TEMPLATE_ID)
                        .build(id)
                )
                .accept(MediaTypes.HAL_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaTypes.HAL_JSON)
                .expectBody(new ParameterizedTypeReference<EntityModel<CharacterType>>() {
                })
                .returnResult();
        final EntityModel<CharacterType> body = result.getResponseBody();
        assert body != null;
        final CharacterType content = body.getContent();
        assertThat(content.getId()).isEqualTo(id);
    }
}
