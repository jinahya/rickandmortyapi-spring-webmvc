package io.github.jinahya.rickandmortyapi.spring.stereotype;

import io.github.jinahya.rickandmortyapi.persistence.LocationResident;
import io.github.jinahya.rickandmortyapi.persistence.LocationResidentId;
import io.github.jinahya.rickandmortyapi.persistence.LocationResidentId_;
import io.github.jinahya.rickandmortyapi.persistence.LocationResident_;
import io.github.jinahya.rickandmortyapi.spring.data.jpa.repository.LocationResidentRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class LocationResidentService
        extends _BaseRepositoryService<LocationResidentRepository, LocationResident, LocationResidentId> {

    /**
     * .
     *
     * @param id       .
     * @param pageable .
     * @return .
     */
    public Page<LocationResident> findAll(@Nullable final LocationResidentId id, final Pageable pageable) {
        return applyRepository(repo -> repo.findAll(
                (r, q, b) -> b.and(
                        Optional.ofNullable(id)
                                .map(LocationResidentId::getLocationId)
                                .map(v -> b.equal(
                                        r.get(LocationResident_.ID).get(LocationResidentId_.LOCATION_ID),
                                        v
                                ))
                                .orElseGet(b::conjunction),
                        Optional.ofNullable(id)
                                .map(LocationResidentId::getResidentId)
                                .map(v -> b.equal(
                                        r.get(LocationResident_.ID).get(LocationResidentId_.RESIDENT_ID),
                                        v
                                ))
                                .orElseGet(b::conjunction)),
                pageable
        ));
    }
}
