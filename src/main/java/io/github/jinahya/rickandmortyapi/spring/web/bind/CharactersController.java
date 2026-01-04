package io.github.jinahya.rickandmortyapi.spring.web.bind;

import io.github.jinahya.rickandmortyapi.persistence.Character;
import io.github.jinahya.rickandmortyapi.persistence.CharacterEpisode;
import io.github.jinahya.rickandmortyapi.persistence.CharacterEpisode_;
import io.github.jinahya.rickandmortyapi.persistence.Character_;
import io.github.jinahya.rickandmortyapi.spring.stereotype.CharacterEpisodeService;
import io.github.jinahya.rickandmortyapi.spring.stereotype.CharacterService;
import io.github.jinahya.rickandmortyapi.spring.web.bind.type.CharacterType;
import io.github.jinahya.rickandmortyapi.spring.web.bind.type.EpisodeType;
import io.github.jinahya.rickandmortyapi.spring.web.bind.type.mapper.CharacterTypeMapper;
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
import org.springframework.hateoas.Link;
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
@SuppressWarnings({
        "java:S100", // Method names should comply with a naming convention
        "java:S115"  // Constant names should comply with a naming convention
})
class CharactersController {

    static final String REQUEST_MAPPING_PATH = "characters";

    // -----------------------------------------------------------------------------------------------------------------
    private static final String PATH_NAME_ID = "id";

    private static final String PATH_VALUE_ID = "\\d+";

    static final String PATH_TEMPLATE_ID = '{' + PATH_NAME_ID + ':' + PATH_VALUE_ID + '}';

    // -----------------------------------------------------------------------------------------------------------------
    private static final String PATH_NAME_EPISODES_ = "episodes_";

    private static final String PATH_VALUE_EPISODES_ = "episodes_";

    static final String PATH_TEMPLATE_EPISODES_ = '{' + PATH_NAME_EPISODES_ + ':' + PATH_VALUE_EPISODES_ + '}';

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
        entityModel.add(
                Link.of(CharacterEpisodesController.getLinkHrefToCollection(content.getId(), null))
                        .withRel(CharacterType.RELATION_EPISODES_)
        );
        return entityModel;
    }

    static PagedModel<EntityModel<CharacterType>> decorate(final PagedModel<EntityModel<CharacterType>> pagedModel) {
        pagedModel.forEach(CharactersController::decorate);
        return pagedModel;
    }

    // -----------------------------------------------------------------------------------------------------------------
    PagedModel<EntityModel<CharacterType>> toPagedModel(final Page<Character> selected) {
        final var mapped = selected.map(characterTypeMapper::fromEntity);
        final var assembled = pagedResourcesAssembler.toModel(mapped);
        final var decorated = decorate(assembled);
        return decorated;
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
    PagedModel<EntityModel<CharacterType>> read(final Pageable pageable) {
        final var selected = characterService.applyRepository(r -> r.findAll(pageable));
        return toPagedModel(selected);
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
    EntityModel<CharacterType> readSingle(@Positive @PathVariable(PATH_NAME_ID) final int id) {
        return characterService.applyRepository(r -> r.findById(id))
                .map(characterTypeMapper::fromEntity)
                .map(EntityModel::of)
                .map(CharactersController::decorate)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Valid
    @NotNull
    @GetMapping(
            path = {
                    '/' + PATH_TEMPLATE_ID + '/' + PATH_TEMPLATE_EPISODES_
            },
            produces = {
                    MediaTypes.HAL_JSON_VALUE
            }
    )
    PagedModel<EntityModel<EpisodeType>> readEpisodes_(@Positive @PathVariable(PATH_NAME_ID) final int id,
                                                       @PathVariable(PATH_NAME_EPISODES_) final String episodes,
                                                       final Pageable pageable) {
        final var selected = characterEpisodeService
                .applyRepository(repo -> repo.findAll(
                        (r, q, b) -> {
                            if (q.getResultType() != Long.class) {
                                r.fetch(CharacterEpisode_.EPISODE, JoinType.LEFT);
                            }
                            return b.equal(r.get(CharacterEpisode_.CHARACTER).get(Character_.ID), id);
                        },
                        pageable
                ))
                .map(CharacterEpisode::getEpisode);
        return episodesController.getPagedModel(selected);
    }

    // -----------------------------------------------------------------------------------------------------------------
    private final CharacterService characterService;

    private final CharacterTypeMapper characterTypeMapper;

    private final PagedResourcesAssembler<CharacterType> pagedResourcesAssembler;

    // -----------------------------------------------------------------------------------------------------------------
    private final CharacterEpisodeService characterEpisodeService;

    @Lazy
    @Autowired
    private EpisodesController episodesController;
}
