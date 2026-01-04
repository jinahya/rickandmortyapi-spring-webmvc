package io.github.jinahya.rickandmortyapi.spring.web.bind;

import io.github.jinahya.rickandmortyapi.persistence.LocationResident;
import io.github.jinahya.rickandmortyapi.persistence.LocationResidentId_;
import io.github.jinahya.rickandmortyapi.persistence.LocationResident_;
import io.github.jinahya.rickandmortyapi.spring.stereotype.LocationResidentService;
import io.github.jinahya.rickandmortyapi.spring.stereotype.LocationService;
import io.github.jinahya.rickandmortyapi.spring.web.bind.type.CharacterType;
import io.github.jinahya.rickandmortyapi.spring.web.bind.type.LocationType;
import io.github.jinahya.rickandmortyapi.spring.web.bind.type.mapper.LocationTypeMapper;
import jakarta.persistence.criteria.JoinType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.service.connection.ConnectionDetails;
import org.springframework.context.annotation.Lazy;
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
@SuppressWarnings({
        "java:S100", // Method names should comply with a naming convention
        "java:S115"  // Constant names should comply with a naming convention
})
class LocationsController {

    static final String REQUEST_MAPPING_PATH = "locations";

    // -----------------------------------------------------------------------------------------------------------------
    private static final String PATH_NAME_ID = "id";

    private static final String PATH_VALUE_ID = "\\d+";

    static final String PATH_TEMPLATE_ID = '{' + PATH_NAME_ID + ':' + PATH_VALUE_ID + '}';

    // -----------------------------------------------------------------------------------------------------------------
    private static final String PATH_NAME_RESIDENTS_ = "residents_";

    private static final String PATH_VALUE_RESIDENTS_ = "residents_";

    static final String PATH_TEMPLATE_RESIDENTS_ = '{' + PATH_NAME_RESIDENTS_ + ':' + PATH_VALUE_RESIDENTS_ + '}';

    @Autowired
    private ConnectionDetails connectionDetails;

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

    @Valid
    @NotNull
    @GetMapping(
            path = {
                    '/' + PATH_TEMPLATE_ID + '/' + PATH_TEMPLATE_RESIDENTS_
            },
            produces = {
                    MediaTypes.HAL_JSON_VALUE
            }
    )
    PagedModel<EntityModel<CharacterType>> readResidents_(@Positive @PathVariable(PATH_NAME_ID) final int id,
                                                          @PathVariable(PATH_NAME_RESIDENTS_) final String residents_,
                                                          final Pageable pageable) {
        final var selected = locationResidentService.applyRepository(repo -> {
                    return repo.findAll(
                            (r, q, b) -> {
                                if (q.getResultType() != Long.class) {
                                    r.fetch(LocationResident_.RESIDENT, JoinType.LEFT);
                                }
                                return b.equal(r.get(LocationResident_.ID).get(LocationResidentId_.LOCATION_ID), id);
                            },
                            pageable
                    );
                })
                .map(LocationResident::getResident);
        return charactersController.toPagedModel(selected);
    }

    // -----------------------------------------------------------------------------------------------------------------
    private final LocationService locationService;

    private final LocationTypeMapper locationTypeMapper;

    private final PagedResourcesAssembler<LocationType> pagedResourcesAssembler;

    // -----------------------------------------------------------------------------------------------------------------
    private final LocationResidentService locationResidentService;

    @Lazy
    @Autowired
    private CharactersController charactersController;
}
