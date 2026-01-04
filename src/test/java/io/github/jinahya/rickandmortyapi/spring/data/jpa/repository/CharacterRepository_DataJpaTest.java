package io.github.jinahya.rickandmortyapi.spring.data.jpa.repository;

import io.github.jinahya.rickandmortyapi.persistence.Character;

class CharacterRepository_DataJpaTest
        extends _BaseEntityRepository_DataJpaTest<CharacterRepository, Character, Integer> {

    // -----------------------------------------------------------------------------------------------------------------
    CharacterRepository_DataJpaTest() {
        super(CharacterRepository.class);
    }
}
