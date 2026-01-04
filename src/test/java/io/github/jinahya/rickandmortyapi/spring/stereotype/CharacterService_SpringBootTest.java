package io.github.jinahya.rickandmortyapi.spring.stereotype;

import io.github.jinahya.rickandmortyapi.persistence.Character;
import io.github.jinahya.rickandmortyapi.spring.data.jpa.repository.CharacterRepository;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@SuppressWarnings({
        "java:S119" // Type parameter names should comply with a naming convention
})
class CharacterService_SpringBootTest
        extends _BaseRepositoryService_SpringBootTest<CharacterService, CharacterRepository, Character, Integer> {

    // -----------------------------------------------------------------------------------------------------------------
    CharacterService_SpringBootTest() {
        super(CharacterService.class);
    }
}
