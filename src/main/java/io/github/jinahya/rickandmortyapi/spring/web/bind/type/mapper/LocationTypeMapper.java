package io.github.jinahya.rickandmortyapi.spring.web.bind.type.mapper;

import io.github.jinahya.rickandmortyapi.persistence.Location;
import io.github.jinahya.rickandmortyapi.spring.web.bind.type.LocationType;
import org.mapstruct.Mapper;

@Mapper
@SuppressWarnings({
        "java:S114" // Interface names should comply with a naming convention
})
public interface LocationTypeMapper
        extends _BaseTypeMapper<LocationType, Location, Integer> {

}
