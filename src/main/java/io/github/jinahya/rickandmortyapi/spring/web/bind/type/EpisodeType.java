package io.github.jinahya.rickandmortyapi.spring.web.bind.type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.jinahya.rickandmortyapi.persistence.Character_Type;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.net.URL;
import java.time.LocalDate;

@Setter(AccessLevel.PACKAGE)
@Getter(AccessLevel.PACKAGE)
@NoArgsConstructor
@SuppressWarnings({
        "java:S116" // Field names should comply with a naming convention
})
public class EpisodeType
        extends _BaseType {

    @Positive
    @NotNull
    private Integer id;

    // -----------------------------------------------------------------------------------------------------------------
    @NotNull
    private String name;

    @NotNull
    private LocalDate airDate;

    @NotBlank
    private String episode;

    // -----------------------------------------------------------------------------------------------------------------
    @JsonIgnore
    @NotNull
    private URL url;

    // -----------------------------------------------------------------------------------------------------------------
    @JsonIgnore
    @Valid
    @NotNull
    private Character_Type characters_;
}
