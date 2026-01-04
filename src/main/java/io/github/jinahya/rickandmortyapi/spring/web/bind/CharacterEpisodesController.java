package io.github.jinahya.rickandmortyapi.spring.web.bind;

import io.github.jinahya.rickandmortyapi.persistence.CharacterEpisodeId;
import io.github.jinahya.rickandmortyapi.persistence.CharacterEpisodeId_;
import io.github.jinahya.rickandmortyapi.persistence.CharacterEpisode_;
import io.github.jinahya.rickandmortyapi.spring.stereotype.CharacterEpisodeService;
import io.github.jinahya.rickandmortyapi.spring.web.bind.type.CharacterEpisodeType;
import io.github.jinahya.rickandmortyapi.spring.web.bind.type.mapper.CharacterEpisodeTypeMapper;
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
@RequestMapping // (path = CharacterEpisodesController.REQUEST_MAPPING_PATH_TEMPLATE)
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class CharacterEpisodesController {

    static final String REQUEST_MAPPING_PATH_NAME = "characterEpisodes";

    static final String REQUEST_MAPPING_PATH_VALUE = "characterEpisodes";

    static final String REQUEST_MAPPING_PATH_TEMPLATE =
            '{' + REQUEST_MAPPING_PATH_NAME + ':' + REQUEST_MAPPING_PATH_VALUE + '}';

    // -----------------------------------------------------------------------------------------------------------------
    static final String PARAM_NAME_ID_CHARACTER_ID = "id.characterId";

    static final String PARAM_NAME_ID_EPISODE_ID = "id.episodeId";

    // -----------------------------------------------------------------------------------------------------------------
    private static final String PATH_NAME_DONTCARE = "dontcare";

    private static final String PATH_VALUE_DONTCARE = "dontcare";

    static final String PATH_TEMPLATE_DONTCARE = '{' + PATH_NAME_DONTCARE + ':' + PATH_VALUE_DONTCARE + '}';

    // -----------------------------------------------------------------------------------------------------------------
    static String getLinkHrefToCollection(@Nullable final Integer idCharacterId, @Nullable final Integer idEpisodeId) {
        final var link = linkTo(CharacterEpisodesController.class)
                .slash(REQUEST_MAPPING_PATH_VALUE)
                .withSelfRel();
        final var href = new StringBuilder(link.getHref());
        if (idCharacterId != null) {
            href.append(';').append(PARAM_NAME_ID_CHARACTER_ID).append('=').append(idCharacterId);
        }
        if (idEpisodeId != null) {
            href.append(';').append(PARAM_NAME_ID_EPISODE_ID).append('=').append(idEpisodeId);
        }
        return href.toString();
    }

    static String getLinkHrefToItem(final Integer idCharacterId, final Integer idEpisodeId) {
        Objects.requireNonNull(idCharacterId, "idCharacterId is null");
        Objects.requireNonNull(idCharacterId, "idCharacterId is null");
        return getLinkHrefToCollection(idCharacterId, idEpisodeId) + '/' + PATH_VALUE_DONTCARE;
    }

    static EntityModel<CharacterEpisodeType> decorate(final EntityModel<CharacterEpisodeType> entityModel) {
        final CharacterEpisodeType content = entityModel.getContent();
        entityModel.add(
                Link.of(getLinkHrefToItem(content.getId().getCharacterId(), content.getId().getEpisodeId()))
                        .withSelfRel()
        );
        entityModel.add(
                Link.of(getLinkHrefToCollection(null, null))
                        .withRel(IanaLinkRelations.COLLECTION)
        );
        Optional.ofNullable(content.getCharacter()).ifPresent(l -> {
            entityModel.add(
                    linkTo(methodOn(CharactersController.class).readSingle(l.getId()))
                            .withRel(CharacterEpisodeType.RELATION_CHARACTER)
            );
        });
        Optional.ofNullable(content.getEpisode()).ifPresent(l -> {
            entityModel.add(
                    linkTo(methodOn(EpisodesController.class).readSingle(l.getId()))
                            .withRel(CharacterEpisodeType.RELATION_EPISODE)
            );
        });
        return entityModel;
    }

    static PagedModel<EntityModel<CharacterEpisodeType>> decorate(
            final PagedModel<EntityModel<CharacterEpisodeType>> pagedModel) {
        pagedModel.forEach(CharacterEpisodesController::decorate);
        return pagedModel;
    }

    // ---------------------------------------------------------------------------------------------------- CONSTRUCTORS

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Reads characterEpisodes.
     *
     * @param pageable a pageable specification.
     * @return a page of characterEpisodes.
     */
    @GetMapping(
            path = {
                    REQUEST_MAPPING_PATH_TEMPLATE
            },
            produces = {
                    MediaTypes.HAL_JSON_VALUE
            }
    )
    PagedModel<EntityModel<CharacterEpisodeType>> read(
            @PathVariable(name = REQUEST_MAPPING_PATH_NAME) final String requestMappingPath,
            @MatrixVariable(pathVar = REQUEST_MAPPING_PATH_NAME) final MultiValueMap<String, String> matrixVariables,
            final Pageable pageable) {
        final var idCharacterId = matrixVariables.getFirst(PARAM_NAME_ID_CHARACTER_ID);
        final var idEpisodeId = matrixVariables.getFirst(PARAM_NAME_ID_EPISODE_ID);
        final var found = characterEpisodeService.applyRepository(repo -> repo.findAll(
                (r, q, b) -> {
                    if (q.getResultType() != Long.class) {
                        r.fetch(CharacterEpisode_.CHARACTER, JoinType.LEFT);
                        r.fetch(CharacterEpisode_.EPISODE, JoinType.LEFT);
                    }
                    return b.and(
                            Optional.ofNullable(idCharacterId)
                                    .map(v -> b.equal(
                                            r.get(CharacterEpisode_.ID).get(CharacterEpisodeId_.CHARACTER_ID),
                                            v
                                    ))
                                    .orElseGet(b::conjunction),
                            Optional.ofNullable(idEpisodeId)
                                    .map(v -> b.equal(
                                            r.get(CharacterEpisode_.ID).get(CharacterEpisodeId_.EPISODE_ID),
                                            v
                                    ))
                                    .orElseGet(b::conjunction));
                },
                pageable
        ));
        final var mapped = found.map(characterEpisodeTypeMapper::fromEntity);
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
    EntityModel<CharacterEpisodeType> readSingle(
            @PathVariable(name = REQUEST_MAPPING_PATH_NAME) final String requestMappingPath,
            @MatrixVariable(pathVar = REQUEST_MAPPING_PATH_NAME) final MultiValueMap<String, String> matrixVariables,
            @PathVariable(name = PATH_NAME_DONTCARE) final String dontcare) {
        final var idCharacterId =
                Optional.ofNullable(matrixVariables.getFirst(PARAM_NAME_ID_CHARACTER_ID))
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                PARAM_NAME_ID_CHARACTER_ID + " is required"
                        ));
        final var idEpisodeId =
                Optional.ofNullable(matrixVariables.getFirst(PARAM_NAME_ID_EPISODE_ID))
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                PARAM_NAME_ID_EPISODE_ID + " is required"
                        ));
        final var id = CharacterEpisodeId.of(
                Integer.valueOf(idCharacterId), // NumberFormatException
                Integer.valueOf(idEpisodeId)  // NumberFormatException
        );
        final var found = characterEpisodeService
                .applyRepository(repo -> repo.findById(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        final var mapped = characterEpisodeTypeMapper.fromEntity(found);
        final var modeled = EntityModel.of(mapped);
        final var decorated = decorate(modeled);
        return decorated;
    }

    // -----------------------------------------------------------------------------------------------------------------
    private final CharacterEpisodeService characterEpisodeService;

    private final CharacterEpisodeTypeMapper characterEpisodeTypeMapper;

    private final PagedResourcesAssembler<CharacterEpisodeType> pagedResourcesAssembler;
}
