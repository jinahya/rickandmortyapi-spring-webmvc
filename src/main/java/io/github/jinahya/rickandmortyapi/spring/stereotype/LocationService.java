package io.github.jinahya.rickandmortyapi.spring.stereotype;

import io.github.jinahya.rickandmortyapi.persistence.Location;
import io.github.jinahya.rickandmortyapi.spring.data.jpa.repository.LocationRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class LocationService
        extends _BaseRepositoryService<LocationRepository, Location, Integer> {

}
