package io.github.jinahya.rickandmortyapi.spring.data.jpa.repository;

import io.github.jinahya.rickandmortyapi.persistence.Character;
import org.springframework.stereotype.Repository;

@Repository
@SuppressWarnings({
        "java:S114" // Interface names should comply with a naming convention
})
public interface CharacterRepository
        extends _BaseEntityRepository<Character, Integer> {

}
