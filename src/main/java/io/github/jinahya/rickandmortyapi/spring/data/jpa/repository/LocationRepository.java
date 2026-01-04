package io.github.jinahya.rickandmortyapi.spring.data.jpa.repository;

import io.github.jinahya.rickandmortyapi.persistence.Location;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@SuppressWarnings({
        "java:S114" // Interface names should comply with a naming convention
})
public interface LocationRepository
        extends _BaseEntityRepository<Location, Integer> {

    //    @EntityGraph(attributePaths = {
//            Location_.ORIGIN,
//            Location_.LOCATION,
//            Location_.EPISODES_
//    })
    @Override
    Page<Location> findAll(@NonNull Pageable pageable);
}
