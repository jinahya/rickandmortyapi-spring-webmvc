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
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path = CharacterController.REQUEST_MAPPING_PATH)
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class CharacterController {

    static final String REQUEST_MAPPING_PATH = "characters";

    // -----------------------------------------------------------------------------------------------------------------
    private static final String PATH_NAME_ID = "id";

    private static final String PATH_VALUE_ID = "\\d+";

    static final String PATH_TEMPLATE_ID = '{' + PATH_NAME_ID + ':' + PATH_VALUE_ID + '}';

    // -----------------------------------------------------------------------------------------------------------------
    private static EntityModel<CharacterType> addLinks(final EntityModel<CharacterType> entityModel) {
        return entityModel;
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
        assembled.forEach(CharacterController::addLinks);
        return assembled;
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
                .map(CharacterController::addLinks)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    // -----------------------------------------------------------------------------------------------------------------
    private final CharacterService characterService;

    private final CharacterTypeMapper characterTypeMapper;

    private final PagedResourcesAssembler<CharacterType> pagedResourcesAssembler;
}
