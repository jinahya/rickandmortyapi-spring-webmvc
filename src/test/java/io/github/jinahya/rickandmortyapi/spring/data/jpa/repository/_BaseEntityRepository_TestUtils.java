package io.github.jinahya.rickandmortyapi.spring.data.jpa.repository;

import io.github.jinahya.rickandmortyapi.persistence._BaseEntity;
import org.springframework.core.ResolvableType;

@SuppressWarnings({
        "unchecked",
        "java:S119" // Type parameter names should comply with a naming convention
})
public final class _BaseEntityRepository_TestUtils {

    // -----------------------------------------------------------------------------------------------------------------
    public static <
            REPOSITORY extends _BaseEntityRepository<ENTITY, ?>,
            ENTITY extends _BaseEntity<?>
            >
    Class<ENTITY> resolveEntityClass(final Class<REPOSITORY> repositoryClass) {
        return (Class<ENTITY>) ResolvableType
                .forClass(repositoryClass)
                .as(_BaseEntityRepository.class)
                .getGeneric(0)
                .resolve();
    }

    public static <
            ENTITY extends _BaseEntity<ID>,
            ID
            >
    Class<ID> resolveIdClass(final Class<ENTITY> entityClass) {
        return (Class<ID>) ResolvableType
                .forClass(entityClass)
                .as(_BaseEntity.class)
                .getGeneric(0)
                .resolve();
    }

    // -----------------------------------------------------------------------------------------------------------------
    private _BaseEntityRepository_TestUtils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
