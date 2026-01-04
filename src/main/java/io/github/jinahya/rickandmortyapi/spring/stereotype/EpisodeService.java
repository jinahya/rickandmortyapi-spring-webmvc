package io.github.jinahya.rickandmortyapi.spring.stereotype;

import io.github.jinahya.rickandmortyapi.persistence.Episode;
import io.github.jinahya.rickandmortyapi.spring.data.jpa.repository.EpisodeRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class EpisodeService
        extends _BaseRepositoryService<EpisodeRepository, Episode, Integer> {

}
