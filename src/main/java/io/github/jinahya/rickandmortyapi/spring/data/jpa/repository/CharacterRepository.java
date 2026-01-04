package io.github.jinahya.rickandmortyapi.spring.data.jpa.repository;

import io.github.jinahya.rickandmortyapi.persistence.Character;
import io.github.jinahya.rickandmortyapi.persistence.Character_;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterRepository
        extends _BaseEntityRepository<Character, Integer> {

    @EntityGraph(attributePaths = {
            Character_.ORIGIN,
            Character_.LOCATION,
            Character_.EPISODES_
    })
    @Override
    Page<Character> findAll(@NonNull Pageable pageable);
}
