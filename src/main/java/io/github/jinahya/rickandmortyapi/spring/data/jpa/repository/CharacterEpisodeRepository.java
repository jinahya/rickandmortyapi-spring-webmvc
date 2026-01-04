package io.github.jinahya.rickandmortyapi.spring.data.jpa.repository;

import io.github.jinahya.rickandmortyapi.persistence.CharacterEpisode;
import io.github.jinahya.rickandmortyapi.persistence.CharacterEpisodeId;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterEpisodeRepository
        extends _BaseEntityRepository<CharacterEpisode, CharacterEpisodeId> {

//    @EntityGraph(attributePaths = {
//            CharacterEpisode_.CHARACTER,
//            CharacterEpisode_.EPISODE
//    })
//    Page<CharacterEpisode> findAllByIdCharacterId(int idCharacterId, Pageable pageable);
}
