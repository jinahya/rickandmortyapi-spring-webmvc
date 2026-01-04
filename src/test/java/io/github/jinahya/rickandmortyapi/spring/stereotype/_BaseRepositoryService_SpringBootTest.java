package io.github.jinahya.rickandmortyapi.spring.stereotype;

import io.github.jinahya.rickandmortyapi.persistence._BaseEntity;
import io.github.jinahya.rickandmortyapi.spring.data.jpa.repository._BaseEntityRepository;
import io.github.jinahya.rickandmortyapi.spring.data.jpa.repository._BaseEntityRepository_TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.Objects;
import java.util.function.Function;

@SpringBootTest
@SuppressWarnings({
        "java:S119" // Type parameter names should comply with a naming convention
})
abstract class _BaseRepositoryService_SpringBootTest<
        SERVICE extends _BaseRepositoryService<REPOSITORY, ENTITY, ID>,
        REPOSITORY extends _BaseEntityRepository<ENTITY, ID>,
        ENTITY extends _BaseEntity<ID>,
        ID
        > {

    // -----------------------------------------------------------------------------------------------------------------
    _BaseRepositoryService_SpringBootTest(final Class<SERVICE> serviceClass,
                                          final Class<REPOSITORY> repositoryClass,
                                          final Class<ENTITY> entityClass,
                                          final Class<ID> idClass) {
        super();
        this.serviceClass = Objects.requireNonNull(serviceClass, "serviceClass is null");
        this.repositoryClass = Objects.requireNonNull(repositoryClass, "repositoryClass is null");
        this.entityClass = Objects.requireNonNull(entityClass, "entityClass is null");
        this.idClass = Objects.requireNonNull(idClass, "idClass is null");
    }

    _BaseRepositoryService_SpringBootTest(final Class<SERVICE> serviceClass,
                                          final Class<REPOSITORY> repositoryClass,
                                          final Class<ENTITY> entityClass) {
        this(serviceClass,
             repositoryClass,
             entityClass,
             _BaseEntityRepository_TestUtils.resolveIdClass(entityClass)
        );
    }

    _BaseRepositoryService_SpringBootTest(final Class<SERVICE> serviceClass, final Class<REPOSITORY> repositoryClass) {
        this(serviceClass,
             repositoryClass,
             _BaseEntityRepository_TestUtils.resolveEntityClass(repositoryClass)
        );
    }

    _BaseRepositoryService_SpringBootTest(final Class<SERVICE> serviceClass) {
        this(serviceClass,
             _BaseRepositoryService_TestUtils.resolveRepositoryClass(serviceClass)
        );
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    void __removeMe() {
    }

    // ------------------------------------------------------------------------------------------------- serviceInstance

    /**
     * Returns the result of the specified function applied an auto-wired instance of {@link SERVICE}.
     *
     * @param function the function to apply to the service instance.
     * @param <R>      result type parameter.
     * @return the result of the {@code function} applied to the service instance.
     */
    <R> R applyServiceInstance(final Function<? super SERVICE, ? extends R> function) {
        return Objects.requireNonNull(function, "function is null")
                .apply(serviceInstance);
    }

    // --------------------------------------------------------------------------------------------------- repositorySpy

    /**
     * Returns the result of the specified function applied an auto-wired {@link MockitoSpyBean spy} instance of
     * {@link REPOSITORY}.
     *
     * @param function the function to apply to the repository instance.
     * @param <R>      result type parameter.
     * @return the result of the {@code function} applied to the repository instance.
     */
    <R> R applyRepositorySpy(final Function<? super REPOSITORY, ? extends R> function) {
        return Objects.requireNonNull(function, "function is null")
                .apply(repositorySpy);
    }

    // -----------------------------------------------------------------------------------------------------------------
    final Class<SERVICE> serviceClass;

    final Class<REPOSITORY> repositoryClass;

    final Class<ENTITY> entityClass;

    final Class<ID> idClass;

    // -----------------------------------------------------------------------------------------------------------------
    @Autowired
    private SERVICE serviceInstance;

    @MockitoSpyBean
    private REPOSITORY repositorySpy;
}
