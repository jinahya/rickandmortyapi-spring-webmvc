package io.github.jinahya.rickandmortyapi.spring.stereotype;

import io.github.jinahya.rickandmortyapi.persistence._BaseEntity;
import io.github.jinahya.rickandmortyapi.spring.data.jpa.repository._BaseEntityRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
@SuppressWarnings({
        "java:S101", // Class names should comply with a naming convention
        "java:S119", // Type parameter names should comply with a naming convention
        "java:S6813" // Field dependency injection should be avoided
})
abstract class _BaseRepositoryService<
        REPOSITORY extends _BaseEntityRepository<ENTITY, ID>,
        ENTITY extends _BaseEntity<ID>,
        ID
        > {

    // ------------------------------------------------------------------------------------------------------ repository
    @Valid
    public <R> R applyRepository(@NotNull final Function<REPOSITORY, R> function) {
        return function.apply(repository);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Autowired
    private REPOSITORY repository;
}
