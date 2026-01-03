package io.github.jinahya.rickandmortyapi.spring.stereotype;

import io.github.jinahya.rickandmortyapi.persistence.Character;
import io.github.jinahya.rickandmortyapi.spring.data.jpa.repository.CharacterRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class CharacterService
        extends _BaseRepositoryService<CharacterRepository, Character, Integer> {

}
