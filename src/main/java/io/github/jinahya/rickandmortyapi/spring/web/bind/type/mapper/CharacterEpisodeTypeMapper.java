package io.github.jinahya.rickandmortyapi.spring.web.bind.type.mapper;

import io.github.jinahya.rickandmortyapi.persistence.CharacterEpisode;
import io.github.jinahya.rickandmortyapi.persistence.CharacterEpisodeId;
import io.github.jinahya.rickandmortyapi.spring.web.bind.type.CharacterEpisodeType;
import org.mapstruct.Mapper;

@Mapper
@SuppressWarnings({
        "java:S114" // Interface names should comply with a naming convention
})
public interface CharacterEpisodeTypeMapper
        extends _BaseTypeMapper<CharacterEpisodeType, CharacterEpisode, CharacterEpisodeId> {

}
