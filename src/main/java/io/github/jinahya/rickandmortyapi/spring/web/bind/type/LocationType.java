package io.github.jinahya.rickandmortyapi.spring.web.bind.type;

import io.github.jinahya.rickandmortyapi.persistence.Location_Dimension;
import io.github.jinahya.rickandmortyapi.persistence.Location_Type;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jspecify.annotations.Nullable;

import java.net.URL;

//@Setter(AccessLevel.PACKAGE)
@Setter
@Getter
@NoArgsConstructor//(access = AccessLevel.PACKAGE)
public class LocationType
        extends _BaseType {

    public static final String RELATION_RESIDENTS_ = "residents_";

    @Positive
    @NotNull
    private Integer id;

    // -----------------------------------------------------------------------------------------------------------------
    @NotNull
    private String name;

    @Nullable
    private Location_Type type;

    @NotNull
    private Location_Dimension dimension;

    // -----------------------------------------------------------------------------------------------------------------
    @NotNull
    private URL url;

    // -----------------------------------------------------------------------------------------------------------------
//    @JsonIgnore
//    private List<CharacterType> residents_;
}
