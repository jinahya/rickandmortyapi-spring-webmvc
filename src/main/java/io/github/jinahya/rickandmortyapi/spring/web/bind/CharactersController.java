package io.github.jinahya.rickandmortyapi.spring.web.bind;

import io.github.jinahya.rickandmortyapi.spring.stereotype.CharacterService;
import io.github.jinahya.rickandmortyapi.spring.web.bind.type.CharacterType;
import io.github.jinahya.rickandmortyapi.spring.web.bind.type.mapper.CharacterTypeMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping(path = CharactersController.REQUEST_MAPPING_PATH)
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class CharactersController {

    static final String REQUEST_MAPPING_PATH = "characters";

    // -----------------------------------------------------------------------------------------------------------------
    private static final String PATH_NAME_ID = "id";

    private static final String PATH_VALUE_ID = "\\d+";

    static final String PATH_TEMPLATE_ID = '{' + PATH_NAME_ID + ':' + PATH_VALUE_ID + '}';

    // -----------------------------------------------------------------------------------------------------------------
    static EntityModel<CharacterType> decorate(final EntityModel<CharacterType> entityModel) {
        final CharacterType content = entityModel.getContent();
        entityModel.add(
                WebMvcLinkBuilder
                        .linkTo(CharactersController.class)
                        .slash(content.getId())
                        .withSelfRel()
        );
        entityModel.add(
                WebMvcLinkBuilder
                        .linkTo(CharactersController.class)
                        .withRel(IanaLinkRelations.COLLECTION)
        );
        Optional.ofNullable(content.getOrigin_()).ifPresent(l -> {
            entityModel.add(
                    WebMvcLinkBuilder
                            .linkTo(LocationsController.class)
                            .slash(l.getId())
                            .withRel(LinkRelation.of(CharacterType.RELATION_ORIGIN_))
            );
        });
        Optional.ofNullable(content.getLocation_()).ifPresent(l -> {
            entityModel.add(
                    WebMvcLinkBuilder
                            .linkTo(LocationsController.class)
                            .slash(l.getId())
                            .withRel(LinkRelation.of(CharacterType.RELATION_LOCATION_))
            );
        });
        return entityModel;
    }

    static PagedModel<EntityModel<CharacterType>> decorate(final PagedModel<EntityModel<CharacterType>> pagedModel) {
        pagedModel.forEach(CharactersController::decorate);
        return pagedModel;
    }

    // ---------------------------------------------------------------------------------------------------- CONSTRUCTORS

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Reads characters.
     *
     * @param pageable a pageable specification.
     * @return a page of characters.
     */
    @GetMapping(
            produces = {
                    MediaTypes.HAL_JSON_VALUE
            }
    )
    PagedModel<EntityModel<CharacterType>> readCharacters(final Pageable pageable) {
        final var found = characterService.applyRepository(r -> r.findAll(pageable));
        final var mapped = found.map(characterTypeMapper::fromEntity);
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
    EntityModel<CharacterType> readCharacter(@Positive @PathVariable(PATH_NAME_ID) final int id) {
        return characterService.applyRepository(r -> r.findById(id))
                .map(characterTypeMapper::fromEntity)
                .map(EntityModel::of)
                .map(CharactersController::decorate)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    // -----------------------------------------------------------------------------------------------------------------
    private final CharacterService characterService;

    private final CharacterTypeMapper characterTypeMapper;

    private final PagedResourcesAssembler<CharacterType> pagedResourcesAssembler;
}
