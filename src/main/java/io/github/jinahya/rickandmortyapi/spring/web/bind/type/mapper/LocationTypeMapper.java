package io.github.jinahya.rickandmortyapi.spring.web.bind.type.mapper;

import io.github.jinahya.rickandmortyapi.persistence.Location;
import io.github.jinahya.rickandmortyapi.spring.web.bind.type.LocationType;
import org.mapstruct.Mapper;

@Mapper
public interface LocationTypeMapper
        extends _BaseTypeMapper<LocationType, Location, Integer> {

}
