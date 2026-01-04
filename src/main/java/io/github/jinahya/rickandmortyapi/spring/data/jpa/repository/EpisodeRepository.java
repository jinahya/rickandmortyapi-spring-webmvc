package io.github.jinahya.rickandmortyapi.spring.data.jpa.repository;

import io.github.jinahya.rickandmortyapi.persistence.Episode;
import org.springframework.stereotype.Repository;

@Repository
public interface EpisodeRepository
        extends _BaseEntityRepository<Episode, Integer> {

}
