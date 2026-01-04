package io.github.jinahya.rickandmortyapi.spring.web.bind;

import io.github.jinahya.rickandmortyapi.spring.stereotype.LocationService;
import io.github.jinahya.rickandmortyapi.spring.web.bind.type.LocationType;
import io.github.jinahya.rickandmortyapi.spring.web.bind.type.mapper.LocationTypeMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path = LocationsController.REQUEST_MAPPING_PATH)
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class LocationsController {

    static final String REQUEST_MAPPING_PATH = "locations";

    // -----------------------------------------------------------------------------------------------------------------
    private static final String PATH_NAME_ID = "id";

    private static final String PATH_VALUE_ID = "\\d+";

    static final String PATH_TEMPLATE_ID = '{' + PATH_NAME_ID + ':' + PATH_VALUE_ID + '}';

    // -----------------------------------------------------------------------------------------------------------------
    static EntityModel<LocationType> decorate(final EntityModel<LocationType> entityModel) {
        final LocationType content = entityModel.getContent();
        entityModel.add(
                WebMvcLinkBuilder
                        .linkTo(LocationsController.class)
                        .slash(content.getId())
                        .withSelfRel()
        );
        entityModel.add(
                WebMvcLinkBuilder
                        .linkTo(LocationsController.class)
                        .withRel(IanaLinkRelations.COLLECTION)
        );
        entityModel.add(
                Link.of(LocationResidentsController.getLinkHrefToCollection(content.getId(), null))
                        .withRel(LocationType.RELATION_RESIDENTS_)
        );
        return entityModel;
    }

    static PagedModel<EntityModel<LocationType>> decorate(final PagedModel<EntityModel<LocationType>> pagedModel) {
        pagedModel.forEach(LocationsController::decorate);
        return pagedModel;
    }

    // ---------------------------------------------------------------------------------------------------- CONSTRUCTORS

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Reads locations.
     *
     * @param pageable a pageable specification.
     * @return a page of locations.
     */
    @GetMapping(
            produces = {
                    MediaTypes.HAL_JSON_VALUE
            }
    )
    PagedModel<EntityModel<LocationType>> readLocations(final Pageable pageable) {
        final var found = locationService.applyRepository(r -> r.findAll(pageable));
        final var mapped = found.map(locationTypeMapper::fromEntity);
        final var assembled = pagedResourcesAssembler.toModel(mapped);
        final var decorated = decorate(assembled);
        return decorated;
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Valid
    @NotNull
    @GetMapping(
            path = {
                    '/' + PATH_TEMPLATE_ID
            },
            produces = {
                    MediaTypes.HAL_JSON_VALUE
            }
    )
    EntityModel<LocationType> readLocation(@Positive @PathVariable(PATH_NAME_ID) final int id) {
        return locationService.applyRepository(r -> r.findById(id))
                .map(locationTypeMapper::fromEntity)
                .map(EntityModel::of)
                .map(LocationsController::decorate)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    // -----------------------------------------------------------------------------------------------------------------
    private final LocationService locationService;

    private final LocationTypeMapper locationTypeMapper;

    private final PagedResourcesAssembler<LocationType> pagedResourcesAssembler;
}
