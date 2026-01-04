package io.github.jinahya.rickandmortyapi.spring.web.bind.type.mapper;

import io.github.jinahya.rickandmortyapi.persistence.Episode;
import io.github.jinahya.rickandmortyapi.spring.web.bind.type.EpisodeType;
import org.mapstruct.Mapper;

@Mapper
public interface EpisodeTypeMapper
        extends _BaseTypeMapper<EpisodeType, Episode, Integer> {

}
