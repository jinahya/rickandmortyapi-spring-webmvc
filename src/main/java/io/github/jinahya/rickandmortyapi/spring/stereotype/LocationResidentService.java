package io.github.jinahya.rickandmortyapi.spring.stereotype;

import io.github.jinahya.rickandmortyapi.persistence.LocationResident;
import io.github.jinahya.rickandmortyapi.persistence.LocationResidentId;
import io.github.jinahya.rickandmortyapi.spring.data.jpa.repository.LocationResidentRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class LocationResidentService
        extends _BaseRepositoryService<LocationResidentRepository, LocationResident, LocationResidentId> {

}
