package io.github.jinahya.rickandmortyapi.spring.web.bind.type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.jinahya.rickandmortyapi.persistence.Character_Gender;
import io.github.jinahya.rickandmortyapi.persistence.Character_Species;
import io.github.jinahya.rickandmortyapi.persistence.Character_Status;
import io.github.jinahya.rickandmortyapi.persistence.Character_Type;
import io.github.jinahya.rickandmortyapi.persistence.Location_Type;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jspecify.annotations.Nullable;
import org.springframework.hateoas.server.core.Relation;

import java.net.URL;
import java.util.List;

//@Setter(AccessLevel.PACKAGE)
@Setter
@Getter
//@NoArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Relation(collectionRelation = "characterTypeList")
@SuppressWarnings({
        "java:S116" // Field names should comply with a naming convention
})
public class CharacterType
        extends _BaseType {

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
//    @JsonIgnore
//    @Valid
//    @NotNull
//    private Location_Type origin_;
//
//    @JsonIgnore
//    @Valid
//    @NotNull
//    private Location_Type location_;
//
//    @JsonIgnore
//    private List<@Valid @NotNull EpisodeType> episodes_;
}
