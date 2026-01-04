package io.github.jinahya.rickandmortyapi.spring.stereotype;

import io.github.jinahya.rickandmortyapi.spring.data.jpa.repository._BaseEntityRepository;
import org.springframework.core.ResolvableType;

@SuppressWarnings({
        "unchecked",
        "java:S119" // Type parameter names should comply with a naming convention
})
public final class _BaseRepositoryService_TestUtils {

    // -----------------------------------------------------------------------------------------------------------------
    public static <
            SERVICE extends _BaseRepositoryService<REPOSITORY, ?, ?>,
            REPOSITORY extends _BaseEntityRepository<?, ?>
            >
    Class<REPOSITORY> resolveRepositoryClass(final Class<SERVICE> serviceClass) {
        return (Class<REPOSITORY>) ResolvableType
                .forClass(serviceClass)
                .as(_BaseRepositoryService.class)
                .getGeneric(0)
                .resolve();
    }

    // -----------------------------------------------------------------------------------------------------------------
    private _BaseRepositoryService_TestUtils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
