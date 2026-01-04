package io.github.jinahya.rickandmortyapi.spring.web.bind;

import io.github.jinahya.rickandmortyapi.persistence.LocationResidentId;
import io.github.jinahya.rickandmortyapi.persistence.LocationResidentId_;
import io.github.jinahya.rickandmortyapi.persistence.LocationResident_;
import io.github.jinahya.rickandmortyapi.spring.stereotype.LocationResidentService;
import io.github.jinahya.rickandmortyapi.spring.web.bind.type.LocationResidentType;
import io.github.jinahya.rickandmortyapi.spring.web.bind.type.mapper.LocationResidentTypeMapper;
import jakarta.persistence.criteria.JoinType;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping // (path = LocationResidentsController.REQUEST_MAPPING_PATH_TEMPLATE)
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class LocationResidentsController {

    static final String REQUEST_MAPPING_PATH_NAME = "locationResidents";

    static final String REQUEST_MAPPING_PATH_VALUE = "locationResidents";

    static final String REQUEST_MAPPING_PATH_TEMPLATE =
            '{' + REQUEST_MAPPING_PATH_NAME + ':' + REQUEST_MAPPING_PATH_VALUE + '}';

    // -----------------------------------------------------------------------------------------------------------------
    static final String PARAM_NAME_ID_LOCATION_ID = "id.locationId";

    static final String PARAM_NAME_ID_RESIDENT_ID = "id.residentId";

    // -----------------------------------------------------------------------------------------------------------------
    private static final String PATH_NAME_DONTCARE = "dontcare";

    private static final String PATH_VALUE_DONTCARE = "dontcare";

    static final String PATH_TEMPLATE_DONTCARE = '{' + PATH_NAME_DONTCARE + ':' + PATH_VALUE_DONTCARE + '}';

    // -----------------------------------------------------------------------------------------------------------------
    static String getLinkHrefToCollection(@Nullable final Integer idLocationId, @Nullable final Integer idResidentId) {
        assert idLocationId != null || idResidentId != null;
        final var link = linkTo(LocationResidentsController.class)
                .slash(REQUEST_MAPPING_PATH_VALUE)
                .withSelfRel();
        final var href = new StringBuilder(link.getHref());
        if (idLocationId != null) {
            href.append(';').append(PARAM_NAME_ID_LOCATION_ID).append('=').append(idLocationId);
        }
        if (idResidentId != null) {
            href.append(';').append(PARAM_NAME_ID_RESIDENT_ID).append('=').append(idResidentId);
        }
        return href.toString();
    }

    static String getLinkHrefToItem(final Integer idLocationId, final Integer idResidentId) {
        Objects.requireNonNull(idLocationId, "idLocationId is null");
        Objects.requireNonNull(idLocationId, "idLocationId is null");
        return getLinkHrefToCollection(idLocationId, idResidentId) + '/' + PATH_VALUE_DONTCARE;
    }

    static EntityModel<LocationResidentType> decorate(final EntityModel<LocationResidentType> entityModel) {
        final LocationResidentType content = entityModel.getContent();
        entityModel.add(
                Link.of(getLinkHrefToItem(content.getId().getLocationId(), content.getId().getResidentId()))
                        .withSelfRel()
        );
        entityModel.add(
                Link.of(getLinkHrefToCollection(null, null))
                        .withRel(IanaLinkRelations.COLLECTION)
        );
        Optional.ofNullable(content.getLocation()).ifPresent(l -> {
            entityModel.add(
                    linkTo(methodOn(LocationsController.class).readLocation(l.getId()))
                            .withRel(LocationResidentType.RELATION_LOCATION)
            );
        });
        Optional.ofNullable(content.getResident()).ifPresent(l -> {
            entityModel.add(
                    linkTo(methodOn(LocationsController.class).readLocation(l.getId()))
                            .withRel(LocationResidentType.RELATION_RESIDENT)
            );
        });
        return entityModel;
    }

    static PagedModel<EntityModel<LocationResidentType>> decorate(
            final PagedModel<EntityModel<LocationResidentType>> pagedModel) {
        pagedModel.forEach(LocationResidentsController::decorate);
        return pagedModel;
    }

    // ---------------------------------------------------------------------------------------------------- CONSTRUCTORS

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Reads locationResidents.
     *
     * @param pageable a pageable specification.
     * @return a page of locationResidents.
     */
    @GetMapping(
            path = {
                    REQUEST_MAPPING_PATH_TEMPLATE
            },
            produces = {
                    MediaTypes.HAL_JSON_VALUE
            }
    )
    PagedModel<EntityModel<LocationResidentType>> read(
            @PathVariable(name = REQUEST_MAPPING_PATH_NAME) final String requestMappingPath,
            @MatrixVariable(pathVar = REQUEST_MAPPING_PATH_NAME) final MultiValueMap<String, String> matrixVariables,
            final Pageable pageable) {
        final var idLocationId = matrixVariables.getFirst(PARAM_NAME_ID_LOCATION_ID);
        final var idResidentId = matrixVariables.getFirst(PARAM_NAME_ID_RESIDENT_ID);
        final var found = locationResidentService.applyRepository(repo -> repo.findAll(
                (r, q, b) -> {
                    if (q.getResultType() != Long.class) {
                        r.fetch(LocationResident_.LOCATION, JoinType.LEFT);
                        r.fetch(LocationResident_.RESIDENT, JoinType.LEFT);
                    }
                    return b.and(
                            Optional.ofNullable(idLocationId)
                                    .map(v -> b.equal(
                                            r.get(LocationResident_.ID).get(LocationResidentId_.LOCATION_ID),
                                            v
                                    ))
                                    .orElseGet(b::conjunction),
                            Optional.ofNullable(idResidentId)
                                    .map(v -> b.equal(
                                            r.get(LocationResident_.ID).get(LocationResidentId_.RESIDENT_ID),
                                            v
                                    ))
                                    .orElseGet(b::conjunction));
                },
                pageable
        ));
        final var mapped = found.map(locationResidentTypeMapper::fromEntity);
        final var assembled = pagedResourcesAssembler.toModel(mapped);
        final var decorated = decorate(assembled);
        return decorated;
    }

    @GetMapping(
            path = {
                    REQUEST_MAPPING_PATH_TEMPLATE + '/' + PATH_TEMPLATE_DONTCARE
            },
            produces = {
                    MediaTypes.HAL_JSON_VALUE
            }
    )
    EntityModel<LocationResidentType> read(
            @PathVariable(name = REQUEST_MAPPING_PATH_NAME) final String requestMappingPath,
            @MatrixVariable(pathVar = REQUEST_MAPPING_PATH_NAME) final MultiValueMap<String, String> matrixVariables,
            @PathVariable(name = PATH_NAME_DONTCARE) final String dontcare) {
        final var idLocationId =
                Optional.ofNullable(matrixVariables.getFirst(PARAM_NAME_ID_LOCATION_ID))
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                PARAM_NAME_ID_LOCATION_ID + " is required"
                        ));
        final var idResidentId =
                Optional.ofNullable(matrixVariables.getFirst(PARAM_NAME_ID_RESIDENT_ID))
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                PARAM_NAME_ID_RESIDENT_ID + " is required"
                        ));
        final var id = LocationResidentId.of(
                Integer.valueOf(idLocationId), // NumberFormatException
                Integer.valueOf(idResidentId)  // NumberFormatException
        );
        final var found = locationResidentService
                .applyRepository(repo -> repo.findById(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        final var mapped = locationResidentTypeMapper.fromEntity(found);
        final var modeled = EntityModel.of(mapped);
        final var decorated = decorate(modeled);
        return decorated;
    }

    // -----------------------------------------------------------------------------------------------------------------
    private final LocationResidentService locationResidentService;

    private final LocationResidentTypeMapper locationResidentTypeMapper;

    private final PagedResourcesAssembler<LocationResidentType> pagedResourcesAssembler;
}
