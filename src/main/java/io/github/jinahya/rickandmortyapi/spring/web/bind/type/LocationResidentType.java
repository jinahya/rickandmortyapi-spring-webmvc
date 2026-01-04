package io.github.jinahya.rickandmortyapi.spring.web.bind.type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.jinahya.rickandmortyapi.persistence.LocationResidentId;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.server.core.Relation;

@Relation(
        itemRelation = LocationResidentType.RELATION_ITEM,
        collectionRelation = LocationResidentType.RELATION_COLLECTION
)
//@Setter(AccessLevel.PACKAGE)
@Setter
@Getter
@NoArgsConstructor//(access = AccessLevel.PACKAGE)
@SuppressWarnings({
        "java:S115", // Constant names should comply with a naming convention
        "java:S116"  // Field names should comply with a naming convention
})
public class LocationResidentType
        extends _BaseType {

    static final String RELATION_ITEM = "character";

    static final String RELATION_COLLECTION = "characters";

    // -----------------------------------------------------------------------------------------------------------------
    public static final String RELATION_LOCATION = "location";

    public static final String RELATION_RESIDENT = "resident";

    // -----------------------------------------------------------------------------------------------------------------
    @Valid
    @NotNull
    private LocationResidentId id;

    // -----------------------------------------------------------------------------------------------------------------
    @JsonIgnore
    @Valid
    private LocationType location;

    @JsonIgnore
    @Valid
    private CharacterType resident;
}
