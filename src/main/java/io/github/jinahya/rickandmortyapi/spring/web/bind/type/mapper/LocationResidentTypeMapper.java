package io.github.jinahya.rickandmortyapi.spring.web.bind.type.mapper;

import io.github.jinahya.rickandmortyapi.persistence.LocationResident;
import io.github.jinahya.rickandmortyapi.persistence.LocationResidentId;
import io.github.jinahya.rickandmortyapi.spring.web.bind.type.LocationResidentType;
import org.mapstruct.Mapper;

@Mapper
@SuppressWarnings({
        "java:S114" // Interface names should comply with a naming convention
})
public interface LocationResidentTypeMapper
        extends _BaseTypeMapper<LocationResidentType, LocationResident, LocationResidentId> {

}
