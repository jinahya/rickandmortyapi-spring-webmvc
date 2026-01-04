package io.github.jinahya.rickandmortyapi.spring.web.bind.type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.server.core.Relation;

import java.net.URL;
import java.time.LocalDate;

@Relation(itemRelation = EpisodeType.RELATION_ITEM, collectionRelation = EpisodeType.RELATION_COLLECTION)
@Setter
@Getter
@NoArgsConstructor
@SuppressWarnings({
        "java:S116" // Field names should comply with a naming convention
})
public class EpisodeType
        extends _BaseType {

    static final String RELATION_ITEM = "episode";

    static final String RELATION_COLLECTION = "episodes";

    // -----------------------------------------------------------------------------------------------------------------
    @Positive
    @NotNull
    private Integer id;

    // -----------------------------------------------------------------------------------------------------------------
    @NotNull
    private String name;

    @NotBlank
    private String episode;

    // -----------------------------------------------------------------------------------------------------------------
    @JsonIgnore
    @NotNull
    private URL url;

    // -----------------------------------------------------------------------------------------------------------------
    private LocalDate airDateIso_;
}
