package io.github.jinahya.rickandmortyapi.spring.data.jpa.repository;

import io.github.jinahya.rickandmortyapi.persistence.LocationResident;
import io.github.jinahya.rickandmortyapi.persistence.LocationResidentId;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationResidentRepository
        extends _BaseEntityRepository<LocationResident, LocationResidentId> {

    Page<LocationResident> findAllByIdLocationId(int idLocationId, @NonNull Pageable pageable);
}
