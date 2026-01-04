package io.github.jinahya.rickandmortyapi.spring.web.bind.type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.jinahya.rickandmortyapi.persistence.Character_Gender;
import io.github.jinahya.rickandmortyapi.persistence.Character_Species;
import io.github.jinahya.rickandmortyapi.persistence.Character_Status;
import io.github.jinahya.rickandmortyapi.persistence.Character_Type;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jspecify.annotations.Nullable;
import org.springframework.hateoas.server.core.Relation;

import java.net.URL;

@Relation(itemRelation = CharacterType.RELATION_ITEM, collectionRelation = CharacterType.RELATION_COLLECTION)
@Setter
@Getter
@NoArgsConstructor
@SuppressWarnings({
        "java:S115", // Constant names should comply with a naming convention
        "java:S116"  // Field names should comply with a naming convention
})
public class CharacterType
        extends _BaseType {

    static final String RELATION_ITEM = "character";

    static final String RELATION_COLLECTION = "characters";

    public static final String RELATION_ORIGIN_ = "origin_";

    public static final String RELATION_LOCATION_ = "location_";

    public static final String RELATION_EPISODES_ = "episodes_";

    // -----------------------------------------------------------------------------------------------------------------
    @Positive
    @NotNull
    private Integer id;

    // -----------------------------------------------------------------------------------------------------------------
    @NotNull
    private String name;

    @NotNull
    private Character_Status status;

    @NotNull
    private Character_Species species;

    @Nullable
    private Character_Type type;

    @NotNull
    private Character_Gender gender;

    @JsonIgnore
    @NotNull
    private URL image;

    // -----------------------------------------------------------------------------------------------------------------
    @JsonIgnore
    @NotNull
    private URL url;

    // -----------------------------------------------------------------------------------------------------------------
    @JsonIgnore
    @Valid
    @NotNull
    private LocationType origin_;

    @JsonIgnore
    @Valid
    @NotNull
    private LocationType location_;
}
