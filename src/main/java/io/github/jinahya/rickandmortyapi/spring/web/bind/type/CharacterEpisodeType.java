package io.github.jinahya.rickandmortyapi.spring.web.bind.type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.jinahya.rickandmortyapi.persistence.CharacterEpisodeId;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.server.core.Relation;

@Relation(
        itemRelation = CharacterEpisodeType.RELATION_ITEM,
        collectionRelation = CharacterEpisodeType.RELATION_COLLECTION
)
//@Setter(AccessLevel.PACKAGE)
@Setter
@Getter
@NoArgsConstructor//(access = AccessLevel.PACKAGE)
@SuppressWarnings({
        "java:S115", // Constant names should comply with a naming convention
        "java:S116"  // Field names should comply with a naming convention
})
public class CharacterEpisodeType
        extends _BaseType {

    static final String RELATION_ITEM = "characterEpisode";

    static final String RELATION_COLLECTION = "characterEpisodes";

    // -----------------------------------------------------------------------------------------------------------------
    public static final String RELATION_CHARACTER = "character";

    public static final String RELATION_EPISODE = "episode";

    // -----------------------------------------------------------------------------------------------------------------
    @Valid
    @NotNull
    private CharacterEpisodeId id;

    // -----------------------------------------------------------------------------------------------------------------
    @JsonIgnore
    @Valid
    private CharacterType character;

    @JsonIgnore
    @Valid
    private EpisodeType episode;
}
