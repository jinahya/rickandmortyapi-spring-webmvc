package io.github.jinahya.rickandmortyapi.spring.web.bind.type.mapper;

import io.github.jinahya.rickandmortyapi.persistence.Character;
import io.github.jinahya.rickandmortyapi.spring.web.bind.type.CharacterType;
import org.mapstruct.Mapper;

@Mapper
@SuppressWarnings({
        "java:S114" // Interface names should comply with a naming convention
})
public interface CharacterTypeMapper
        extends _BaseTypeMapper<CharacterType, Character, Integer> {

}
