package io.github.jinahya.rickandmortyapi.spring.web.bind;

import io.github.jinahya.rickandmortyapi.persistence.CharacterEpisode;
import io.github.jinahya.rickandmortyapi.persistence.CharacterEpisode_;
import io.github.jinahya.rickandmortyapi.persistence.Character_;
import io.github.jinahya.rickandmortyapi.persistence.Episode;
import io.github.jinahya.rickandmortyapi.spring.stereotype.CharacterEpisodeService;
import io.github.jinahya.rickandmortyapi.spring.stereotype.EpisodeService;
import io.github.jinahya.rickandmortyapi.spring.web.bind.type.CharacterType;
import io.github.jinahya.rickandmortyapi.spring.web.bind.type.EpisodeType;
import io.github.jinahya.rickandmortyapi.spring.web.bind.type.mapper.EpisodeTypeMapper;
import jakarta.persistence.criteria.JoinType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
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
@RequestMapping(path = EpisodesController.REQUEST_MAPPING_PATH)
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class EpisodesController {

    static final String REQUEST_MAPPING_PATH = "episodes";

    // -----------------------------------------------------------------------------------------------------------------
    private static final String PATH_NAME_ID = "id";

    private static final String PATH_VALUE_ID = "\\d+";

    static final String PATH_TEMPLATE_ID = '{' + PATH_NAME_ID + ':' + PATH_VALUE_ID + '}';

    // -----------------------------------------------------------------------------------------------------------------
    private static final String PATH_NAME_CHARACTERS_ = "characters_";

    private static final String PATH_VALUE_CHARACTERS_ = "characters_";

    static final String PATH_TEMPLATE_CHARACTERS_ = '{' + PATH_NAME_CHARACTERS_ + ':' + PATH_VALUE_CHARACTERS_ + '}';

    // -----------------------------------------------------------------------------------------------------------------
    static EntityModel<EpisodeType> decorate(final EntityModel<EpisodeType> entityModel) {
        final EpisodeType content = entityModel.getContent();
        entityModel.add(
                WebMvcLinkBuilder
                        .linkTo(EpisodesController.class)
                        .slash(content.getId())
                        .withSelfRel()
        );
        entityModel.add(
                WebMvcLinkBuilder
                        .linkTo(EpisodesController.class)
                        .withRel(IanaLinkRelations.COLLECTION)
        );
        return entityModel;
    }

    static PagedModel<EntityModel<EpisodeType>> decorate(final PagedModel<EntityModel<EpisodeType>> pagedModel) {
        pagedModel.forEach(EpisodesController::decorate);
        return pagedModel;
    }

    // -----------------------------------------------------------------------------------------------------------------
    PagedModel<EntityModel<EpisodeType>> getPagedModel(final Page<Episode> selected) {
        final var mapped = selected.map(episodeTypeMapper::fromEntity);
        final var assembled = pagedResourcesAssembler.toModel(mapped);
        final var decorated = decorate(assembled);
        return decorated;
    }

    // ---------------------------------------------------------------------------------------------------- CONSTRUCTORS

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Reads episodes.
     *
     * @param pageable a pageable specification.
     * @return a page of episodes.
     */
    @GetMapping(
            produces = {
                    MediaTypes.HAL_JSON_VALUE
            }
    )
    PagedModel<EntityModel<EpisodeType>> read(final Pageable pageable) {
        final var selected = episodeService.applyRepository(r -> r.findAll(pageable));
        return getPagedModel(selected);
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
    EntityModel<EpisodeType> readSingle(@Positive @PathVariable(PATH_NAME_ID) final int id) {
        return episodeService.applyRepository(r -> r.findById(id))
                .map(episodeTypeMapper::fromEntity)
                .map(EntityModel::of)
                .map(EpisodesController::decorate)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Valid
    @NotNull
    @GetMapping(
            path = {
                    '/' + PATH_TEMPLATE_ID + '/' + PATH_TEMPLATE_CHARACTERS_
            },
            produces = {
                    MediaTypes.HAL_JSON_VALUE
            }
    )
    PagedModel<EntityModel<CharacterType>> readCharacters(@Positive @PathVariable(PATH_NAME_ID) final int id,
                                                          @PathVariable(PATH_NAME_CHARACTERS_) final String characters_,
                                                          final Pageable pageable) {
        final var selected = characterEpisodeService
                .applyRepository(repo -> repo.findAll(
                        (r, q, b) -> {
                            if (q.getResultType() != Long.class) {
                                r.fetch(CharacterEpisode_.CHARACTER, JoinType.LEFT);
                            }
                            return b.equal(r.get(CharacterEpisode_.CHARACTER).get(Character_.ID), id);
                        },
                        pageable
                ))
                .map(CharacterEpisode::getCharacter);
        return charactersController.toPagedModel(selected);
    }

    // -----------------------------------------------------------------------------------------------------------------
    private final EpisodeService episodeService;

    private final EpisodeTypeMapper episodeTypeMapper;

    private final PagedResourcesAssembler<EpisodeType> pagedResourcesAssembler;

    // -----------------------------------------------------------------------------------------------------------------
    private final CharacterEpisodeService characterEpisodeService;

    @Lazy
    @Autowired
    private CharactersController charactersController;
}
