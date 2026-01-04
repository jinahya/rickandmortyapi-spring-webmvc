package io.github.jinahya.rickandmortyapi.spring.stereotype;

import io.github.jinahya.rickandmortyapi.persistence.CharacterEpisode;
import io.github.jinahya.rickandmortyapi.persistence.CharacterEpisodeId;
import io.github.jinahya.rickandmortyapi.spring.data.jpa.repository.CharacterEpisodeRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class CharacterEpisodeService
        extends _BaseRepositoryService<CharacterEpisodeRepository, CharacterEpisode, CharacterEpisodeId> {

}
