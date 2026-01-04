package io.github.jinahya.rickandmortyapi.spring.web.bind;

import io.github.jinahya.rickandmortyapi.spring.web.bind.type.LocationResidentType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.test.web.reactive.server.EntityExchangeResult;

import java.util.Collection;
import java.util.Optional;

@Slf4j
class LocationsResidentsControllerTest
        extends _BaseControllerTest {

    @Test
    void read__() {
        int count = 0;
        for (String uri = "/" + LocationResidentsController.REQUEST_MAPPING_PATH_VALUE; ; ) {
            final EntityExchangeResult<PagedModel<EntityModel<LocationResidentType>>> result = webTestClient()
                    .get()
                    .uri(uri)
                    .accept(MediaTypes.HAL_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentType(MediaTypes.HAL_JSON)
                    .expectBody(new ParameterizedTypeReference<PagedModel<EntityModel<LocationResidentType>>>() {
                    })
                    .returnResult();
            final PagedModel<EntityModel<LocationResidentType>> body = result.getResponseBody();
            {
                assert body != null;
                final Collection<EntityModel<LocationResidentType>> content = body.getContent();
                count += content.size();
            }
            final Optional<Link> nextLink = body.getLink(IanaLinkRelations.NEXT);
            if (nextLink.isEmpty()) {
                break;
            }
            final String nextHref = nextLink.get().getHref();
            uri = nextHref.substring(getBaseUrl().length());
        }
    }
}
